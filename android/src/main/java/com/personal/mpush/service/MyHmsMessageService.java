package com.personal.mpush.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;
import com.personal.mpush.MiPushModule;
import com.personal.mpush.R;
import com.personal.mpush.helper.MipushHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Random;

//华为透传
public class MyHmsMessageService extends HmsMessageService {
    private static String TAG="hmstokenlog";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"create");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"destroy");
    }




    // {
    //  title:"",
    //  body:""
    // }
    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);
        Log.i(TAG, "onMessageReceived is called");
        if (message == null) {
            Log.e(TAG, "Received message entity is null!");
            return;
        }

        String data=message.getData();
        JSONObject json= null;
        try {
            Random rand = new Random();
            int msgId=rand.nextInt(999999) + 1;
            json = new JSONObject(data);
            WritableMap writableMap= Arguments.createMap();
            writableMap.putString("extra",json.toString());
            writableMap.putString("title",null);
            writableMap.putString("description",null);
            writableMap.putString("content",null);
            writableMap.putString("token",message.getToken());
            writableMap.putInt("messageid",msgId);
            writableMap.putString("messagetype","华为透传");
            MipushHelper.sendEvent(MipushHelper.Arrived,writableMap);

//            Intent notifyIntent = new Intent(getApplicationContext(), );
//            // Set the Activity to start in a new, empty task
//            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            // Create the PendingIntent
//            PendingIntent notifyPendingIntent = PendingIntent.getActivity(
//                    this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
//            );
//
//
//            if(!MiPushModule.hwchannelid.equals("")){
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), MiPushModule.hwchannelid)
//                        .setSmallIcon(android.R.mipmap.sym_def_app_icon)
//                        .setContentTitle(json.getString("title"))
//                        .setContentText(json.getString("body"))
//                        .setPriority(NotificationCompat.PRIORITY_HIGH)
//                        .setContentIntent(notifyPendingIntent)
//                        .setAutoCancel(true);
//
//                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
//
//                // notificationId is a unique int for each notification that you must define
//
//                int msgid=0;
//
//                notificationManager.notify(msgid, builder.build());

//            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onMessageDelivered(String s, Exception e) {
        super.onMessageDelivered(s, e);
        Log.e(TAG,"onMessageDelivered");
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
        Log.e(TAG,"onMessageSent");
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        Log.e(TAG,"onDeletedMessages");
    }

    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e(TAG,s);
    }

    @Override
    public void onTokenError(Exception e) {
        super.onTokenError(e);
        Log.e(TAG,"token error");
    }
}
