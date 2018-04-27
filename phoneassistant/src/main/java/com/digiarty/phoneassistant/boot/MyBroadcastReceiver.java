package com.digiarty.phoneassistant.boot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.digiarty.phoneassistant.global.GlobalApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    //定义日志对象
    private static Logger logger = LoggerFactory.getLogger(MyBroadcastReceiver.class);

    private final static String BROADCAST_START_ACTION = GlobalApplication.getGlobalPackageName() + ".broadcast.START_SERVICE";
    private final static String BROADCAST_STOP_ACTION = GlobalApplication.getGlobalPackageName() + ".broadcast.STOP_SERVICE";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        logger.debug("action = " + action);
        if (null != action){

            if (action.equalsIgnoreCase(BROADCAST_START_ACTION)){
                logger.debug("startService");
                context.startService(new Intent(context, MyService.class));

            }else if (action.equalsIgnoreCase(BROADCAST_STOP_ACTION)){
                logger.debug("stopService");
                context.stopService(new Intent(context, MyService.class));

            }else{
                logger.debug("不能匹配 action" + action);
            }

        }else {
            logger.debug("intent.action is null");
        }

    }
}
