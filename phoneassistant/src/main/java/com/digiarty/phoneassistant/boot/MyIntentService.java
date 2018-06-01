package com.digiarty.phoneassistant.boot;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.digiarty.phoneassistant.activity.MyNotification;
import com.digiarty.phoneassistant.model.net.NetTaskManager;

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

//    private final static String START_SERVICE = "com.digiarty.phoneassistant.service.START_LISTEN_CLIENT";
    private final static String START_SERVICE = "com.digiarty.phoneassistant.service.START_LISTEN_PC_SOCKET";
    private final static String STOP_SERVICE = "com.digiarty.phoneassistant.broadcast.STOP_SERVICE";
    private final static int FORE_GROUND_NOTIFICATION_ID = 1;

    public static String getStartService() {
        return START_SERVICE;
    }
    public static String getStopService() {
        return STOP_SERVICE;
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public MyIntentService() {
        super("管理网络的线程");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        logger.debug("创建服务");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        startForeground(FORE_GROUND_NOTIFICATION_ID, MyNotification.buildForegroundNotification(this));

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (null == intent){
            logger.debug("intent为null");
        }
        String action = intent.getAction();
        if (null == action){
            logger.debug("接收的服务action为null");
        }


        logger.debug("接收的服务: action = " +  action);
        if (action.equalsIgnoreCase(START_SERVICE)){
            logger.debug("开启服务action");
            logger.debug("开启安卓端socket，监听客户端的链接");
            NetTaskManager.getInstance().main();
        }else if(action.equalsIgnoreCase(STOP_SERVICE)){
            logger.debug("关闭服务");
            stopSelf();
        }else{
            logger.debug("无效的服务action");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        logger.debug("onDestroy: ");
    }
}
