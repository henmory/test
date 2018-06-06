package com.digiarty.phoneassistant.model.net;

import android.os.Bundle;
import android.os.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.Boolean.logicalOr;

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
class ListenPCConnectionTask implements ITask {

    private static Logger logger = LoggerFactory.getLogger(ListenPCConnectionTask.class);
    private Boolean socketFlag = FALSE;
    ServerSocket serverSocket = null;
    @Override
    public void run() {
        Thread.currentThread().setName("监听pc端socket线程");
        logger.debug("线程id = " + Thread.currentThread().getId() + "的线程开始启动，监听socket连接");

        serverSocket = createServerSocketWaitingForPCToConnect();
        if (null != serverSocket) {
            logger.debug("服务器socket信息: serverSocket = " + serverSocket.toString());
        } else {
            notifyNetManagerListenTaskCreateFail();
            logger.debug("线程id = " + Thread.currentThread().getId() + "的线程销毁");
            return;
        }

        logger.debug("开始监听PC端socket....");

        socketFlag = TRUE;
        while (socketFlag) {

            if (serverSocket.isClosed()) {
                logger.debug("接收数据前，连接已经断开");
                System.out.println("接收数据前，连接已经断开");
                break;
            }

            Socket socketToCommunicateWithPC = listenPCConnectAndCreateNewSocketForPCConnection(serverSocket);
            if (socketToCommunicateWithPC != null) {
                logger.debug("新创建的socket信息为: " + socketToCommunicateWithPC.toString());
                notifyNetManagerThereIsANewConnection(socketToCommunicateWithPC);
            } else {
                logger.debug("未知原因监听socket出现问题,结束监听线程");
                break;
            }
        }
        notifyNetManagerLongConnectionTaskDestory();
        logger.debug("线程id = " + Thread.currentThread().getId() + "的线程销毁");
    }


    private ServerSocket createServerSocketWaitingForPCToConnect() {
        //本地应用程序需要监听的端口，客户端往这个端口发送数据
        return serverSocket = ServerSocketWrap.createSocketForListen(ServerConfig.AndroidConfig.getServerPort());
    }

    private Socket listenPCConnectAndCreateNewSocketForPCConnection(ServerSocket serverSocket) {

        Socket socketToCommunicateWithPC = ServerSocketWrap.listenAndCreatSocketForNewConnection(serverSocket);

        if (socketToCommunicateWithPC != null) {
            logger.debug("出现新的PC连接,为其创建新的socket");
            logger.debug("新创建的socket信息: socketToCommunicateWithClient = " + socketToCommunicateWithPC.toString());
        } else {
            return null;
        }
        return socketToCommunicateWithPC;
    }
    private void notifyNetManagerLongConnectionTaskDestory(){
        logger.debug("通知网络管理器监听任务结束");
        Message message = Message.obtain();
        message.what = NetTaskManager.MSG_NOTIFY_NET_MANAGER_LISTEN_TASK_DESTORY;
        message.setTarget(NetTaskManager.handler);
        NetTaskManager.handler.sendMessage(message);
    }

    private void notifyNetManagerListenTaskCreateFail(){
        logger.debug("通知网络管理器监听任务创建失败");
        Message message = Message.obtain();
        message.what = NetTaskManager.MSG_NOTIFY_NET_MANAGER_LISTEN_TASK_CREATE_FAIL;
        message.setTarget(NetTaskManager.handler);

        NetTaskManager.handler.sendMessage(message);
    }
    private void notifyNetManagerThereIsANewConnection(Socket socketToCommunicateWithPC) {
        logger.debug("通知网络管理器监听任务收到一个新连接,开启socket准备与其通信");
        Message message = Message.obtain();
        message.what = NetTaskManager.MSG_NOTIFY_NET_MANAGER_NEW_CLIENT_CONNECTION;
        message.setTarget(NetTaskManager.handler);
        message.obj = socketToCommunicateWithPC;
        NetTaskManager.handler.sendMessage(message);
    }


    private void closeListenSocket(ServerSocket serverSocket) {
        socketFlag = FALSE;
        ServerSocketWrap.closeListenSocket(serverSocket);
    }

    @Override
    public void closeCurrentTask() {
        logger.debug("资源在回收");
        socketFlag = false;
        closeListenSocket(serverSocket);
        logger.debug("资源释放完成");
    }


}
