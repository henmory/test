package com.digiarty.phoneassistant.boot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.digiarty.phoneassistant.global.GlobalApplication;

/***
 *
 * Created on：2018/4/23
 *
 * Created by：henmory
 *
 * Description:广播接收器主要是接收广播，开启服务或者关闭服务
 *              广播的发送需要权限，这就尽最大努力保证了软件的安全性
 *              防止其他应用随意开启广播
 *
 *
 **/
public class MyBroadcastReceiver extends BroadcastReceiver {

    private final static String TAG = MyBroadcastReceiver.class.getSimpleName();

    private final static String BROADCAST_START_ACTION = GlobalApplication.getGlobalPackageName() + "broadcast.START_SERVICE";
//    private final static String BROADCAST_START_ACTION = "com.digiarty.phoneassistant.broadcast.START_SERVICE";
//    private final static String BROADCAST_STOP_ACTION = "com.digiarty.phoneassistant.broadcast.STOP_SERVICE";
    private final static String BROADCAST_STOP_ACTION = GlobalApplication.getGlobalPackageName() + "broadcast.STOP_SERVICE";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        String action = intent.getAction();
        Log.d(TAG, "onReceive: action = " + action);

        if (null != action){

            if (action.equalsIgnoreCase(BROADCAST_START_ACTION)){
                Log.d(TAG, "onReceive: startService");
                context.startService(new Intent(context, MyService.class));

            }else if (action.equalsIgnoreCase(BROADCAST_STOP_ACTION)){
                Log.d(TAG, "onReceive: stopService");
                context.stopService(new Intent(context, MyService.class));

            }else{
                Log.d(TAG, "onReceive: 不能匹配action");
            }

        }else {
            Log.d(TAG, "onReceive: intent.action is null");
        }

    }
}
