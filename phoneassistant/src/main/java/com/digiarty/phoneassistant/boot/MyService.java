package com.digiarty.phoneassistant.boot;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/***
 *
 * Created on：2018/4/23
 *
 * Created by：henmory
 *
 * Description: 服务声明为android:exported="false" 防止其他应用通过adb指令开启服务，尽最大可能保证系统安全
 *
 *
 **/
public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    
    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("on onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("on onDestroy");
    }
}
