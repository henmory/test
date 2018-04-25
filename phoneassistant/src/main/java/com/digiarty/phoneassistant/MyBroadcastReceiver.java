package com.digiarty.phoneassistant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/***
 *
 * Created on：2018/4/23
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public class MyBroadcastReceiver extends BroadcastReceiver {

    private final static String TAG = MyBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        String action = intent.getAction();
        Log.d(TAG, "onReceive: action = " + action);
        if (null != action){

            Intent i = new Intent("com.digiarty.phoneassistant.service.START_LISTEN_CLIENT");
            i.setPackage("com.digiarty.phoneassistant");

            if (action.equalsIgnoreCase("com.digiarty.phoneassistant.broadcast.START_SERVICE")){
                Log.d(TAG, "onReceive: startService");
                context.startService(i);
            }else if (action.equalsIgnoreCase("com.digiarty.phoneassistant.broadcast.STOP_SERVICE")){
                context.stopService(i);
                Log.d(TAG, "onReceive: stopService");
            }else{
                Log.d(TAG, "onReceive: 不能匹配action");
            }

        }else {
            Log.d(TAG, "onReceive: intent.action is null");
        }

    }
}
