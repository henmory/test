package com.digiarty.phoneassistant.model.net;

import android.os.Message;

import com.digiarty.phoneassistant.boot.GlobalApplication;
import com.digiarty.phoneassistant.file.AndroidStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

import static com.digiarty.phoneassistant.model.net.NetDataType.COMMAND;
import static com.digiarty.phoneassistant.model.net.NetDataType.FILE;
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
class ShortConnectionTask implements ITask {

    private static Logger logger = LoggerFactory.getLogger(ListenPCConnectionTask.class);
    private ServerSocketWrap socketWrap;



    private Socket shortSocket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Boolean socketFlag = FALSE;
    private NetDataType type;
    private IShortConnectionTask taskInterface;


    ShortConnectionTask(Socket socket) {
        shortSocket = socket;
        type = COMMAND; //默认接收的数据类型
        socketWrap = new ServerSocketWrap();
    }

    public Socket getShortSocket() {
        return shortSocket;
    }

    public NetDataType getType() {
        return type;
    }

    public void setType(NetDataType type) {
        this.type = type;
    }

    public void setTaskInterface(IShortConnectionTask taskInterface) {
        this.taskInterface = taskInterface;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("与PC进行socket通信的线程");
        logger.debug("线程id = " + Thread.currentThread().getId() + "的线程开始启动，进行socket通信");

        if (!getInputOutPutStream()) {
            notifyNetManagerCommunicationTaskGetInOutStreamError();
            logger.debug("线程id = " + Thread.currentThread().getId() + "的线程销毁");
            System.out.println("线程id = " + Thread.currentThread().getId() + "的线程销毁");
            return;
        }

        socketFlag = TRUE;

        while (socketFlag) {

            if (!shortSocket.isConnected()) {
                logger.debug("接收数据前，连接已经断开");
                System.out.println("接收数据前，连接已经断开");
                break;
            }
            logger.debug("==================================================开始接收数据==================================================");
            byte[] datas = readDatasFromPCAndPrepareDatasToPC(inputStream, type);
            if (null == datas){
                logger.debug("发生错误，读取pc端数据为空,可能网络已经断开，或者解析数据出现错误");
                break;
            }
            logger.debug("==================================================开始发送数据==================================================");
            logger.debug("发送给客户端的数据字节码为 " + Arrays.toString(datas));
            boolean ret = writeResponseToPC(outputStream, datas);
            if (!ret) {
                logger.debug("写数据失败.....");
                break;
            }
            logger.debug("写数据给PC完成");

        }
        closeSocketOfCommunicating();
        notifyNetTaskManagerCommunicationTaskDestory();
        logger.debug("线程id = " + Thread.currentThread().getId() + "的线程销毁");
        System.out.println("线程id = " + Thread.currentThread().getId() + "的线程销毁");
    }

    private boolean getInputOutPutStream() {
        if (null != shortSocket) {
            try {
                inputStream = shortSocket.getInputStream();
                outputStream = shortSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
                logger.debug("从socket获取输入输出流发生异常");
                return false;
            }
        } else {
            logger.debug("从socket获取输入输出流失败---shortSocket为空");
            return false;
        }
        return true;
    }

    private byte[] readDatasFromPCAndPrepareDatasToPC(InputStream inputStream, NetDataType type) {
        byte[] datas = readDatasFromPC(inputStream, type);
        if (null == datas) {
            logger.debug("读取数据为空");
            return null;
        }
//        ParseDatasHandlerManager handlerManager = ParseDatasHandlerManager.getInstance();
        Reply reply = taskInterface.doActionAndPrepareDatasToPC(type,datas);
        if (null != reply){
            setType(reply.getNextReceicedDataType());
            return reply.getDatas();
        }else{
            return null;
        }

    }

    private void setcachedFilePath(String path) {
        socketWrap.setCachedFilePath(path);
    }

    private byte[] readDatasFromPC(InputStream inputStream, NetDataType type) {

        if (type.equals(NetDataType.COMMAND)) {
            logger.debug("接收的数据类型为 " + "COMMAND");
            return socketWrap.readCommandFromInputStream(inputStream);
        } else if (type.equals(NetDataType.JSONOBJECT)) {
            logger.debug("接收的数据类型为 " + "JSONOBJECT");
            return socketWrap.readCommandFromInputStream(inputStream);
        } else if (type.equals(NetDataType.FILE)) {
            logger.debug("接收的数据类型为 " + "FILE");
            setcachedFilePath(AndroidStorage.ExternalStorage.getCacheDirPath(GlobalApplication.getContext()) + "/1.jpg");
            System.out.println("图片缓存路径" + socketWrap.getCachedFilePath());
            return socketWrap.readFilesFromInputStream(inputStream);
        } else {
            logger.debug("无效的数据类型");
            return null;
        }

    }


    private boolean writeResponseToPC(OutputStream outputStream, byte[] datas) {
        return socketWrap.writeDatasToOutputStream(outputStream, datas);
    }

    private void notifyNetManagerCommunicationTaskGetInOutStreamError() {
        logger.debug("通知网络管理器短连接任务获取输入输出流失败");
        Message message = Message.obtain();
        message.what = NetTaskManager.MSG_NOTIFY_NET_MANAGER_COMMUNICATION_TASK_GET_INOUT_STREAM_ERROR;
        message.setTarget(NetTaskManager.handler);
        message.obj = this;
        NetTaskManager.handler.sendMessage(message);
    }

    private void notifyNetTaskManagerCommunicationTaskDestory() {
        logger.debug("通知网络管理器端连接任务结束");
        Message message = Message.obtain();
        message.what = NetTaskManager.MSG_NOTIFY_NET_MANAGER_COMMUNICATION_TASK_DESTPRY;
        message.setTarget(NetTaskManager.handler);
        message.obj = this;
        NetTaskManager.handler.sendMessage(message);

    }

    private void closeInputOutPutStread() {
        if (null != inputStream || null != outputStream) {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void closeSocketOfCommunicating() {
        socketFlag = FALSE;
        closeInputOutPutStread();
        socketWrap.closeSocket(shortSocket);
    }

    @Override
    public void closeCurrentTask() {
        logger.debug("资源在回收");
        closeSocketOfCommunicating();
        logger.debug("资源释放完成");
    }


}
