package com.personal.mpush;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.common.ApiException;
import com.personal.mpush.receiver.MyLocalNotifyReceiver;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

public class MiPushModule extends ReactContextBaseJavaModule {
    public static ReactApplicationContext context;

    public static String XM_APP_ID;
    public static String XM_APP_KEY;

    private static String TAG = "pushlog";

    private static String HwToken = "";

    public static String hwchannelid = "";

    public MiPushModule(ReactApplicationContext reactContext) {
        super(reactContext);
        context = reactContext;
    }


    @ReactMethod
    public void getPhoneType(Promise p) {
        String brand = android.os.Build.BRAND;
        p.resolve(brand);
    }

    @NonNull
    @Override
    public String getName() {
        return "MiPush";
    }


    //小米推送注册
    @ReactMethod
    public void registerPush(String channelname, String channeldec, String channelid) throws JSONException {

        createNotificationChannel(channelname, channeldec, channelid);
        hwchannelid = channelid;


        if (android.os.Build.BRAND.equals("Xiaomi")) {
            ApplicationInfo appInfo = null;
            try {
                appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            assert appInfo != null;
            String xm_appkey=appInfo.metaData.getString("xiaomiappkey");
            String xm_appid=appInfo.metaData.getString("xiaomiappid");
            XM_APP_KEY=xm_appkey.substring(2,xm_appkey.length());
            XM_APP_ID=xm_appid.substring(2,xm_appid.length());

            //小米
            if (!XM_APP_KEY.equals("")&&!XM_APP_ID.equals("")) {

                MiPushClient.registerPush(context, XM_APP_ID, XM_APP_KEY);

                //打开Log
                LoggerInterface newLogger = new LoggerInterface() {

                    @Override
                    public void setTag(String tag) {
                        // ignore
                    }

                    @Override
                    public void log(String content, Throwable t) {
                        Log.d("mipushlog", content, t);
                    }

                    @Override
                    public void log(String content) {
                        Log.d("mipushlog", content);
                    }
                };
                Logger.setLogger(context, newLogger);
            }


        } else if (android.os.Build.BRAND.equals("HUAWEI")) {
            getToken();
        } else {
            Log.e(TAG, "暂不支持");
        }

    }

    private void getToken() {
        new Thread() {
            @Override
            public void run() {
                try {
                    // read from agconnect-services.json
                    String appId = AGConnectServicesConfig.fromContext(context).getString("client/app_id");

                    String token = HmsInstanceId.getInstance(getCurrentActivity()).getToken(appId, "HCM");
                    Log.i(TAG, "get token:" + token);
                    if (!TextUtils.isEmpty(token)) {
                        HwToken = token;
                    }
                } catch (ApiException e) {
                    Log.e(TAG, "get token failed, " + e);
                }
            }
        }.start();
    }


    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = context.getApplicationInfo().processName;

        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }


    //创建channel
    private void createNotificationChannel(String channel_name, String channel_dec, String channelid) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelid, channel_name, importance);
            channel.setDescription(channel_dec);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    //关闭MiPush推送服务
    @ReactMethod
    public void unregisterPush() {
        if (Build.BRAND.equals("Xiaomi")) {
            MiPushClient.unregisterPush(context);
        } else if (Build.BRAND.equals("HUAWEI")) {
            //注销token
            new Thread() {
                @Override
                public void run() {
                    try {
                        // read from agconnect-services.json
                        String appId = AGConnectServicesConfig.fromContext(context).getString("client/app_id");
                        HmsInstanceId.getInstance(context).deleteToken(appId, "HCM");
                        Log.i(TAG, "deleteToken success.");
                    } catch (ApiException e) {
                        Log.e(TAG, "deleteToken failed." + e);
                    }
                }
            }.start();
        } else {
            Log.e(TAG, "暂不支持");
        }

    }

    //启用MiPush推送服务
    @ReactMethod
    public void enablePush() {
        if (Build.BRAND.equals("Xiaomi")) {
            MiPushClient.enablePush(context);
        }

    }

    //禁用MiPush推送服务
    @ReactMethod
    public void disablePush() {
        if (Build.BRAND.equals("Xiaomi")) {
            MiPushClient.disablePush(context);
        }
    }

    //设置alias
    @ReactMethod
    public void setAlias(String alias) {
        if (Build.BRAND.equals("Xiaomi")) {
            MiPushClient.setAlias(context, alias, null);
        }
    }

    //取消alias
    @ReactMethod
    public void unsetAlias(String alias) {
        if (Build.BRAND.equals("Xiaomi")) {
            MiPushClient.setAlias(context, alias, null);
        }
    }


    //暂停接收MiPush服务推送的消息
    @ReactMethod
    public void pausePush() {
        if (Build.BRAND.equals("Xiaomi")) {
            MiPushClient.pausePush(context, null);
        }
    }

    //恢复接收MiPush服务推送的消息
    @ReactMethod
    public void resumePush() {
        if (Build.BRAND.equals("Xiaomi")) {
            MiPushClient.resumePush(context, null);
        }
    }

    @ReactMethod
    public void getAllAlias(Promise promise) {
        if (Build.BRAND.equals("Xiaomi")) {
            try {
                List<String> alias = MiPushClient.getAllAlias(context);
                promise.resolve(alias);
            } catch (Exception e) {
                promise.reject("发生错误");
            }

        }
    }

    @ReactMethod
    public void clearNotification() {
        if (Build.BRAND.equals("Xiaomi")) {
            MiPushClient.clearNotification(context);
        }
    }

    @ReactMethod
    public void getRegId(Promise p) {
        if (Build.BRAND.equals("Xiaomi")) {
            String id = MiPushClient.getRegId(context);
            p.resolve(id);
        }
    }


    //适用于华为获取token
    @ReactMethod
    public void getHuaweitoken(Promise promise) {
        promise.resolve(HwToken);
    }


    //获取华为普通推送 intent参数
    @ReactMethod
    public void gethuaweiintentstr() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
// Scheme协议（例如：pushscheme://com.huawei.codelabpush/deeplink?）需要开发者自定义
        intent.setData(Uri.parse("pushscheme://com.huawei.codelabpush/deeplink?"));
// 往intent中添加参数，用户可以根据自己的需求进行添加参数：
        intent.putExtra("name", "abc");
        intent.putExtra("age", 180);
// 应用必须带上该Flag，如果不添加该选项有可能会显示重复的消息
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String intentUri = intent.toUri(Intent.URI_INTENT_SCHEME);
// 打印出的intentUri值就是设置到推送消息中intent字段的值
        Log.d("intentUri", intentUri);
    }

    //本地推
    @ReactMethod
    public void sendLocalNotification(String title,String text,String params) {

            Random rand = new Random();
            int msgId=rand.nextInt(999999) + 1;

            Intent notifyIntent = new Intent(context, MyLocalNotifyReceiver.class);
            // Set the Activity to start in a new, empty task
            notifyIntent.setAction("com.personal.mpush.sendReceiver");

            notifyIntent.putExtra("params",params);
            notifyIntent.putExtra("messageid",msgId);

            // Create the PendingIntent
            PendingIntent notifyPendingIntent = PendingIntent.getBroadcast(
                    context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
            );


            if(!MiPushModule.hwchannelid.equals("")){
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MiPushModule.hwchannelid)
                        .setSmallIcon(context.getApplicationInfo().icon)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(notifyPendingIntent)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

                // notificationId is a unique int for each notification that you must define



                notificationManager.notify(msgId, builder.build());

            }
    }


    //推送是否开启
    @ReactMethod
    public void isopenNotification(Promise p){
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        boolean isOpened = manager.areNotificationsEnabled();
        p.resolve(isOpened);
    }

    //去往app权限设置
    @ReactMethod
    public void startSettingAppInfo(){
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }



}