package com.digiarty.phoneassistant.net;

import android.os.Message;

import com.digiarty.phoneassistant.global.GlobalApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;
import java.util.ArrayList;
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
    private static List<ITask> tasks = new ArrayList<>();

    private static ExecutorService createExecutor(){
        return mExecutorService = Executors.newCachedThreadPool();  //创建一个线程池
    }

    private static void closeExecutor(){
        if (mExecutorService.isShutdown()){
            return;
        }
        mExecutorService.shutdown();

    }

    public static void newTaskToHandleDataTransition(){
        mExecutorService = createExecutor();
        newTaskToListenPCSocket();
    }

    public static void newTaskToListenPCSocket(){
        ITask task = new ListenPCSocketTask();
        tasks.add(task);
        mExecutorService.execute(task);

    }

    public static void newTaskToSendAndroidServerPortToPC(int serverPort){
        ServerConfig.AndroidConfig.setPort(serverPort);
        ITask task = new LongConnectionTask();
        tasks.add(task);
        mExecutorService.execute(task);
    }

    public static void newTaskToCommunicateWithPC(Socket socketToCommunicateWithPC) {
        ITask task = new CommunicateWithPCTask(socketToCommunicateWithPC);
        tasks.add(task);
        mExecutorService.execute(task);
    }

    public static void clearTaskResource(){
        logger.debug("目前剩余任务的个数为: " + tasks.size());
        for (int i = 0; i < tasks.size();){
            tasks.get(i).closeCurrentTask();
            tasks.remove(i);
        }
        closeExecutor();
        // TODO: 07/05/2018 是否需要自己关闭，以后继续学习
        GlobalApplication.notifyApplicationClose();

    }

    public static void notifyNetTaskManagerClearAllTask(){
        Message message = Message.obtain();
        message.what = GlobalApplication.MSG_CLEAR_TASKS_RESOURCE;
        GlobalApplication.getMainHandler().sendMessage(message);
    }
}
