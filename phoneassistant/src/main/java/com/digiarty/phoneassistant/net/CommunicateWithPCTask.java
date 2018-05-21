package com.digiarty.phoneassistant.net;

import android.os.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

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
class CommunicateWithPCTask implements ITask {

    private static Logger logger = LoggerFactory.getLogger(ListenPCConnectionTask.class);
    final String name = CommunicateWithPCTask.class.getSimpleName();
    private Socket socketToCommunicateWithPC;
    private int remoteServer;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Boolean socketFlag = FALSE;

    public CommunicateWithPCTask(Socket socket, String remoteServer) {
        socketToCommunicateWithPC = socket;
        this.remoteServer = Integer.parseInt(remoteServer);
        logger.debug("communication的远程socket端口号是: " + this.remoteServer);
    }

    @Override
    public void run() {

        Thread.currentThread().setName("与PC进行socket通信的线程");
        logger.debug("线程id = " + Thread.currentThread().getId() + "的线程开始启动，进行socket通信");

        if(!getInputOutPutStream()){
//            NetTaskManager.notifyNetTaskManagerClearAllTask();
            notifyNetManagerCommunicationTaskGetInOutStreamError();
            logger.debug("线程id = " + Thread.currentThread().getId() + "的线程销毁");
            System.out.println("线程id = " + Thread.currentThread().getId() + "的线程销毁");
            return;
        }

        socketFlag = TRUE;

        while(socketFlag){

            if (!socketToCommunicateWithPC.isConnected()) {
                logger.debug("接收数据前，连接已经断开");
                System.out.println("接收数据前，连接已经断开");
                break;
            }

            if (!socketToCommunicateWithPC.isConnected()) {
                logger.debug("接收数据前，连接已经断开");
                break;
            }
            //先读后写
            byte[] datas = readDatasFromPC(inputStream);
            if (null == datas){
                break;
            }
            logger.debug("从PC读取到的数据为:" + new String(datas));
            logger.debug("开始写数据给PC:");
            boolean ret = writeDatasToPC(outputStream,datas);
            if (!ret){

                break;
            }
            logger.debug("写数据给PC完成");

        }
//        NetTaskManager.notifyNetTaskManagerClearAllTask();
        closeSocketOfCommunicating();
        notifyNetTaskManagerCommunicationTaskDestory();
        logger.debug("线程id = " + Thread.currentThread().getId() + "的线程销毁");
        System.out.println("线程id = " + Thread.currentThread().getId() + "的线程销毁");
    }

    private boolean getInputOutPutStream(){
        if (null != socketToCommunicateWithPC){
            try {
                inputStream = socketToCommunicateWithPC.getInputStream();
                outputStream = socketToCommunicateWithPC.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
                logger.debug("从socket获取输入输出流发生异常");
                return false;
            }
        }else{
            logger.debug("从socket获取输入输出流失败---socketToCommunicateWithPC为空");
            return false;
        }
        return true;
    }


    private byte[] readDatasFromPC(InputStream inputStream){
        return ServerSocketWrap.readDatasFromInputStream(inputStream);

    }

    private boolean writeDatasToPC(OutputStream outputStream,byte[] datas){
        return ServerSocketWrap.writeDatasToOutputStream(outputStream, datas);
    }

    private void notifyNetManagerCommunicationTaskGetInOutStreamError(){
        logger.debug("通知网络管理器短连接任务获取输入输出流失败");
        Message message = Message.obtain();
        message.what = NetTaskManager.MSG_NOTIFY_NET_MANAGER_COMMUNICATION_TASK_GET_INOUT_STREAM_ERROR;
        message.setTarget(NetTaskManager.handler);
        message.arg1 = remoteServer;
        NetTaskManager.handler.sendMessage(message);
    }

    private void notifyNetTaskManagerCommunicationTaskDestory(){
        logger.debug("通知网络管理器端连接任务结束");
        Message message = Message.obtain();
        message.what = NetTaskManager.MSG_NOTIFY_NET_MANAGER_COMMUNICATION_TASK_DESTPRY;
        message.setTarget(NetTaskManager.handler);
        message.arg1 = remoteServer;
        NetTaskManager.handler.sendMessage(message);

    }

    private void closeInputOutPutStread(){
        if (null != inputStream || null != outputStream){
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void closeSocketOfCommunicating(){
        socketFlag = FALSE;
        closeInputOutPutStread();
        ServerSocketWrap.closeSocket(socketToCommunicateWithPC);
    }

    @Override
    public void closeCurrentTask() {
        logger.debug("资源在回收");
        closeSocketOfCommunicating();
        logger.debug("资源释放完成");
    }

}
