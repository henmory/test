package com.digiarty.phoneassistant.boot;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.digiarty.phoneassistant.R;
import com.digiarty.phoneassistant.activity.MyNotification;
import com.digiarty.phoneassistant.net.NetTaskManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 *
 * Created on：04/05/2018
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public class MyService extends Service {
    private static Logger logger = LoggerFactory.getLogger(MyService.class);

    private final static String START_SERVICE = "com.digiarty.phoneassistant.service.START_LISTEN_PC_SOCKET";
    private final static String STOP_SERVICE = "com.digiarty.phoneassistant.broadcast.STOP_SERVICE";
    private final static int FORE_GROUND_NOTIFICATION_ID = 1;

    public static String getStartService() {
        return START_SERVICE;
    }

    public static String getStopService() {
        return STOP_SERVICE;
    }


    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        logger.debug("创建服务");

    }


    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //服务不重启
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (null == intent){
            logger.debug("intent为null");
            return START_NOT_STICKY;
        }
        String action = intent.getAction();
        if (null == action){
            logger.debug("接收的服务action为null");
            return START_NOT_STICKY;
        }

        startForeground(FORE_GROUND_NOTIFICATION_ID, MyNotification.buildForegroundNotification(this));

        logger.debug("接收的服务: action = " +  action);
        if (action.equalsIgnoreCase(START_SERVICE)){
            logger.debug("开启服务action");
            logger.debug("开启安卓端socket，监听客户端的链接");
            NetTaskManager.newTaskToHandleDataTransition();
        }else if(action.equalsIgnoreCase(STOP_SERVICE)){
            logger.debug("关闭服务");
            stopSelf();
        }else{
            logger.debug("无效的服务action");
        }
        // TODO: 04/05/2018 服务包活
        return START_NOT_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        logger.debug("onDestroy: ");
    }




}
