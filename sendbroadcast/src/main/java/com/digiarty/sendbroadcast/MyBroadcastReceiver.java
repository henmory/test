package com.digiarty.sendbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("onReceive1111111111111111");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        Intent i = new Intent("com.digiarty.test.MyService");
//        i.setPackage("com.example.adbstartservicedirectly");
//        context.startService(i);
    }
}
