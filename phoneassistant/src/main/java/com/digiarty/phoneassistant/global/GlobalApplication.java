package com.digiarty.phoneassistant.global;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.digiarty.phoneassistant.R;
import com.digiarty.phoneassistant.activity.MyNotification;
import com.digiarty.phoneassistant.boot.MyService;
import com.digiarty.phoneassistant.log.FileLog;
import com.digiarty.phoneassistant.net.NetTaskManager;

import org.slf4j.LoggerFactory;

import org.slf4j.Logger;


/***
 *
 * Created on：26/04/2018
 *
 * Created by：henmory
 *
 * Description:
 *
 * 注意:这里还不能使用logger因为需要先初始化文件之后，才可以用
 *
 *
 **/
public class GlobalApplication extends Application {

    private final static String TAG = GlobalApplication.class.getSimpleName();
    private static Context context = null;
    private static String globalPackageName;
    private static Handler mainHandler;
    public final static int MSG_CLOSE_APP = 1;
    public final static int MSG_CLEAR_TASKS_RESOURCE = 2;

    private void getGlobalInformation() {
        globalPackageName = getApplicationContext().getPackageName();
        Log.d(TAG, "onCreate: packageName = " + globalPackageName);
    }


    public static String getGlobalPackageName() {
        return globalPackageName;
    }

    public static Handler getMainHandler() {
        return mainHandler;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        getGlobalInformation();
        FileLog.createLogFileDirOnExternalPrivateStorage(this);

        mainHandler = new GlobalHandler();
        MyNotification.createNotificationChannelForGlobalApplication(this);
        context = this;
    }


    // 程序终止的时候执行
    @Override
    public void onTerminate() {
        super.onTerminate();
        System.out.println("application---------onTerminate");
    }

    // 低内存的时候执行
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.out.println("application---------onLowMemory");
    }

    // 程序在内存清理的时候执行
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        System.out.println("application---------onTrimMemory");
    }

    private static class GlobalHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_CLOSE_APP){
                closeApp();
            }else if(msg.what == MSG_CLEAR_TASKS_RESOURCE){
                NetTaskManager.clearTaskResource();
            }
        }
        private void closeApp(){
            android.os.Process.killProcess(android.os.Process.myPid());
        }

    }



    public static void notifyApplicationClose(){
        Message message = Message.obtain();
        message.what = GlobalApplication.MSG_CLOSE_APP;
        mainHandler.sendMessage(message);
    }





}
