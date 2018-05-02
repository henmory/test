package com.example.start_intent_service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.start_intent_service.net.ServerListeningClientTask;
import com.example.start_intent_service.net.ServerSocketWrap;

/***
 *
 * Created on：28/04/2018
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public class MyIntentService extends IntentService {

    private final static String TAG = MyIntentService.class.getSimpleName();
    private final static String START_SERVICE = "com.example.start_intent_service.START_SERVICE";
    private final static String STOP_SERVICE = "com.example.start_intent_service.STOP_SERVICE";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
//    public MyIntentService(String name) {
//        super(name);
//    }
    public MyIntentService() {
        super("开启socket权限");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "onHandleIntent: action = " +  action);
        if (action.equalsIgnoreCase(START_SERVICE)){
            Log.d(TAG, "onHandleIntent: 开启服务action");
            Log.d(TAG, "onHandleIntent: 开启安卓端socket，监听客户端的链接");
            ServerListeningClientTask.androidServerStartListenForClientConnect();
        }else if (action.equalsIgnoreCase(STOP_SERVICE)){
            Log.d(TAG, "onHandleIntent: 关闭服务action");
        }else{
            Log.d(TAG, "onHandleIntent: 无效的服务action");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
