package com.digiarty.phoneassistant.boot;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.digiarty.phoneassistant.net.ServerListeningClientTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
    private static Logger logger = LoggerFactory.getLogger(MyIntentService.class);

    private final static String START_SERVICE = "com.digiarty.phoneassistant.service.START_LISTEN_CLIENT";
    public static String getStartService() {
        return START_SERVICE;
    }
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public MyIntentService() {
        super("监听客户端接入的线程");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        logger.debug("创建服务");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
        logger.debug("接收的服务: action = " +  action);

        if (action.equalsIgnoreCase(START_SERVICE)){
            logger.debug("开启服务action");
            logger.debug("开启安卓端socket，监听客户端的链接");
            ServerListeningClientTask.androidServerStartListenForClientConnect();
        }else{
            logger.debug("无效的服务action");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logger.debug("onDestroy: ");
    }
}
