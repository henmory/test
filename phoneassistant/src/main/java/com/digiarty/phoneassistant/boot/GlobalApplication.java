package com.digiarty.phoneassistant.boot;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.digiarty.phoneassistant.activity.MyNotification;
import com.digiarty.phoneassistant.log.FileLog;
import com.digiarty.phoneassistant.model.net.NetTaskManager;
import com.digiarty.phoneassistant.sharedpreference.IPreference;


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
    public final static String SHARED_PREFERENCE = "shared_preference";
    public final static String PERMISSION_REQUEST_STATE = "permission_request_state";


    private static Context context = null;
    private static String globalPackageName;
    private static Handler mainHandler;
    public final static int MSG_CLOSE_APP = 1;
    public final static int MSG_CLEAR_TASKS_RESOURCE = 2;

    private void getGlobalInformation() {
        globalPackageName = getApplicationContext().getPackageName();
        Log.d(TAG, "onCreate: packageName = " + globalPackageName);
    }
    public static Context getContext() {
        return context;
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
        initSharedPreference();
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

    private void initSharedPreference(){
        Log.d(TAG, "初始化SharedPreference");
        IPreference.IPreferenceHolder holder = IPreference.prefHolder;
        holder.newPreference(this, SHARED_PREFERENCE);
    }

    private static class GlobalHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_CLOSE_APP){
                closeApp();
            }else if(msg.what == MSG_CLEAR_TASKS_RESOURCE){
                NetTaskManager.getInstance().clearTaskResource();
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
