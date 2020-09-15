package com.personal.mpush.service;

import android.app.Service;
import android.content.ServiceConnection;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;
import com.personal.mpush.MiPushModule;
import com.personal.mpush.R;

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
            json = new JSONObject(data);

            if(!MiPushModule.hwchannelid.equals("")){
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), MiPushModule.hwchannelid)
                        .setSmallIcon(android.R.mipmap.sym_def_app_icon)
                        .setContentTitle(json.getString("title"))
                        .setContentText(json.getString("body"))
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                // notificationId is a unique int for each notification that you must define

                int msgid=0;
                Random rand = new Random();
                msgid=rand.nextInt(999999) + 1;
                notificationManager.notify(msgid, builder.build());
            }



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
