package com.digiarty.phoneassistant.model.net;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.digiarty.phoneassistant.model.dataparse.ParseDatasHandlerManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
public class NetTaskManager {

    private static Logger logger = LoggerFactory.getLogger(NetTaskManager.class);

    private static ExecutorService mExecutorService = null; //线程池
    private static List<Socket> socketPool /*= Collections.synchronizedList(new ArrayList<Socket>())*/; //socket连接池

    private static List<ITask> tasks /*= Collections.synchronizedList(new ArrayList<ITask>())*/;//任务池
    static NetManagerHandler handler = null;


    private static class SingletonHolder {
        private static final NetTaskManager INSTANCE = new NetTaskManager();
    }

    public static final NetTaskManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private NetTaskManager() {
        Looper looper = Looper.myLooper();
        handler = new NetManagerHandler(looper);
        logger.debug("准备网络管理线程的消息队列和handler" + handler);

        mExecutorService = createExecutor();
        socketPool = Collections.synchronizedList(new ArrayList<Socket>());
        tasks = Collections.synchronizedList(new ArrayList<ITask>());
    }


    //管理网络的线程
    public void main() {
        logger.debug("开始处理与网络有关的任务");
        ListenPCConnectionTask task = newTaskToListenPCConnection();

        // TODO: 2018/5/21 处理延迟
        while (true) {
            if (task.serverSocket != null) {
                logger.debug("android端服务器开启成功，准备开始新任务通知PC自己的端口号码");
                newTaskToSendAndroidServerPortForPCToForward(task.serverSocket.getLocalPort());
                break;
            }
            logger.debug("等待android服务端创建,成功后发送端口信息......");
        }

        Looper.loop();
        logger.debug("处理网络的线程结束");
    }

    private ExecutorService createExecutor() {
        return mExecutorService = Executors.newCachedThreadPool();  //创建一个线程池
    }

    private void closeExecutor() {
        if (mExecutorService.isShutdown()) {
            return;
        }
        mExecutorService.shutdown();
    }

    public ListenPCConnectionTask newTaskToListenPCConnection() {

        ListenPCConnectionTask task = new ListenPCConnectionTask();
        tasks.add(task);
        mExecutorService.execute(task);
        return task;
    }

    public void newTaskToSendAndroidServerPortForPCToForward(int serverPort) {
        ServerConfig.AndroidConfig.setServerPort(serverPort);
        LongConnectionTask task = new LongConnectionTask();
        tasks.add(task);
        mExecutorService.execute(task);
        socketPool.add(task.longSocket);
    }

    public void newTaskToCommunicateWithPC(Socket socketToCommunicateWithPC) {
        ShortConnectionTask task = new ShortConnectionTask(socketToCommunicateWithPC);
        task.setTaskInterface(ParseDatasHandlerManager.getInstance());//设置短连接数据解析处理器
        tasks.add(task);
        mExecutorService.execute(task);
        socketPool.add(socketToCommunicateWithPC);

    }


    public void clearTaskResource() {
        logger.debug("目前剩余任务的个数为: " + tasks.size());
        for (int i = 0; i < tasks.size(); ) {
            tasks.get(i).closeCurrentTask();
            tasks.remove(i);
        }
        closeExecutor();
        socketPool.clear();
        // TODO: 07/05/2018 是否需要自己关闭，以后继续学习
//        GlobalApplication.notifyApplicationClose();

    }

    //listen task
    static final int MSG_NOTIFY_NET_MANAGER_NEW_CLIENT_CONNECTION = 1;
    static final int MSG_NOTIFY_NET_MANAGER_LISTEN_TASK_CREATE_FAIL = 2;
    static final int MSG_NOTIFY_NET_MANAGER_LISTEN_TASK_DESTORY = 3;

    //long connection task
    static final int MSG_NOTIFY_NET_MANAGER_LONG_TASK_CREATE_FAIL = 4;
    static final int MSG_NOTIFY_NET_MANAGER_LONG_TASK_SEND_ANDROID_PORT_FAIL = 5;
    static final int MSG_NOTIFY_NET_MANAGER_LONG_TASK_DESTPRY = 6;

    //communicate task通知listen task维护sockets列表
    static final int MSG_NOTIFY_NET_MANAGER_COMMUNICATION_TASK_GET_INOUT_STREAM_ERROR = 7;
    static final int MSG_NOTIFY_NET_MANAGER_COMMUNICATION_TASK_DESTPRY = 8;


    static class NetManagerHandler extends Handler {
        NetManagerHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_NOTIFY_NET_MANAGER_NEW_CLIENT_CONNECTION:
                    NetTaskManager.getInstance().newTaskToCommunicateWithPC((Socket) msg.obj);
                    break;
                case MSG_NOTIFY_NET_MANAGER_LISTEN_TASK_CREATE_FAIL:
                case MSG_NOTIFY_NET_MANAGER_LISTEN_TASK_DESTORY:
                case MSG_NOTIFY_NET_MANAGER_LONG_TASK_CREATE_FAIL:
                case MSG_NOTIFY_NET_MANAGER_LONG_TASK_SEND_ANDROID_PORT_FAIL:
                case MSG_NOTIFY_NET_MANAGER_LONG_TASK_DESTPRY:
                    NetTaskManager.getInstance().clearTaskResource();
                    break;
                case MSG_NOTIFY_NET_MANAGER_COMMUNICATION_TASK_GET_INOUT_STREAM_ERROR:
                case MSG_NOTIFY_NET_MANAGER_COMMUNICATION_TASK_DESTPRY:
                    logger.debug("网络管理器清理短连接资源");
                    ShortConnectionTask connectionTask = (ShortConnectionTask) msg.obj;
                    tasks.remove(connectionTask);
                    socketPool.remove(connectionTask.getShortSocket());
                    break;
                default:
                    break;
            }
        }
    }

}
