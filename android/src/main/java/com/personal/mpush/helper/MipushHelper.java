package com.personal.mpush.helper;

import android.util.Log;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.personal.mpush.MiPushModule;

public class MipushHelper {

    public static final String Arrived="MessageArrived";

    public static final String Clicked="MessageClicked";


    public static void sendEvent(String eventName, WritableMap params) {
        try {
            MiPushModule.context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
        }catch (Throwable throwable){
            Log.e("mipushlog","sendEvent error:"+throwable.getMessage());
        }
    }
}
