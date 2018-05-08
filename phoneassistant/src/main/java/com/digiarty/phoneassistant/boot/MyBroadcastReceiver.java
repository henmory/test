package com.digiarty.phoneassistant.boot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.digiarty.phoneassistant.global.GlobalApplication;
import com.digiarty.phoneassistant.net.ServerConfig;

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
    private final static String PC_PORT = "port";
    @Override
    public void onReceive(Context context, Intent intent) {

        handleUserBroadcast(context,intent);
    }


    private void handleUserBroadcast(Context context, Intent intent){

        if (null == intent){
            logger.debug("用户发送的intent为空");
            return;
        }

        if (!parsePCPort(intent)){
            logger.debug("解析用户传来的pc 端口失败");
            return;
        }

        logger.debug("解析的PC端socket端口为" + ServerConfig.PCConfig.getPort());

        String action = intent.getAction();

        if (null != action){
            logger.debug("action = " + action);


            if (action.equalsIgnoreCase(BROADCAST_START_ACTION)){
                logger.debug("startService");
                Intent intent1 = new Intent();
                intent1.setAction(MyService.getStartService());
                intent1.setPackage(GlobalApplication.getGlobalPackageName());
                context.startService(intent1);

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

    private boolean parsePCPort(Intent intent){
        Bundle bundle = intent.getExtras();
        if (null == bundle){
            return false;
        }
        String port = bundle.getString(PC_PORT);
        if (null == port){
            return false;
        }
        ServerConfig.PCConfig.setPort(Integer.parseInt(port));
        return true;
    }
}
