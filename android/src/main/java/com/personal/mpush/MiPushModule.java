package com.personal.mpush;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class MiPushModule extends ReactContextBaseJavaModule {
    public static ReactApplicationContext context;

    public static String APP_ID;
    public static String APP_KEY;

    public MiPushModule(ReactApplicationContext reactContext) {
        super(reactContext);
        context = reactContext;
    }

    @NonNull
    @Override
    public String getName() {
        return "MiPush";
    }


    //小米推送注册
    @ReactMethod
    public void registerPush(String appid,String appkey){
        if(shouldInit()&&!appid.equals("")&&!appkey.equals("")){
            APP_ID=appid;
            APP_KEY=appkey;
            MiPushClient.registerPush(context,APP_ID, APP_KEY);
        }

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

    //关闭MiPush推送服务
    @ReactMethod
    public void unregisterPush(){
        MiPushClient.unregisterPush(context);
    }

    //启用MiPush推送服务
    @ReactMethod
    public void enablePush(){
        MiPushClient.enablePush(context);
    }

    //禁用MiPush推送服务
    @ReactMethod
    public void disablePush(){
        MiPushClient.disablePush(context);
    }

    //设置alias
    @ReactMethod
    public void setAlias(String alias){
        MiPushClient.setAlias(context,alias,null);
    }

    //取消alias
    @ReactMethod
    public void unsetAlias(String alias){
        MiPushClient.setAlias(context,alias,null);
    }


    //暂停接收MiPush服务推送的消息
    @ReactMethod
    public void pausePush(){
        MiPushClient.pausePush(context,null);
    }

    //恢复接收MiPush服务推送的消息
    @ReactMethod
    public void resumePush(){
        MiPushClient.resumePush(context,null);
    }

    @ReactMethod
    public void getAllAlias(Promise promise){
        try{
            List<String> alias = MiPushClient.getAllAlias(context);
            promise.resolve(alias);
        }catch (Exception e){
            promise.reject("发生错误");
        }

    }

    @ReactMethod
    public void clearNotification(){
        MiPushClient.clearNotification(context);
    }

    @ReactMethod
    public void getRegId(Promise p){
        String id=MiPushClient.getRegId(context);
        p.resolve(id);
    }




}