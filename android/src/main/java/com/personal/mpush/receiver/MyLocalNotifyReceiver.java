package com.personal.mpush.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.personal.mpush.helper.MipushHelper;

public class MyLocalNotifyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.e("notify","收到一个广播");
        WritableMap param= Arguments.createMap();
        param.putString("params",intent.getStringExtra("params"));
        param.putInt("msgid",intent.getIntExtra("messageid",0));
        MipushHelper.sendEvent(MipushHelper.LocalNotification,param);
    }
}
