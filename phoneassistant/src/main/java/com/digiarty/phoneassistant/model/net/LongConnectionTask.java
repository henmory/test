package com.digiarty.phoneassistant.model.net;

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
 * Created on：03/05/2018
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
class LongConnectionTask implements ITask {

    private static Logger logger = LoggerFactory.getLogger(LongConnectionTask.class);
    Socket longSocket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Boolean socketFlag = FALSE;
    private ServerSocketWrap socketWrap;

    public LongConnectionTask() {
        socketWrap = new ServerSocketWrap();
    }

    @Override
    public void run() {

        Thread.currentThread().setName("长连接线程");
        logger.debug("线程id = " + Thread.currentThread().getId() + "的线程开始启动，先发送android端服务的端口号，之后监听pc端心跳");


        logger.debug("开始创建长连接socket");

        longSocket = createSocket();
        if (null == longSocket){
            notifyNetTaskManagerLongConnectionTaskCreateFail();
            logger.debug("线程id = " + Thread.currentThread().getId() + "的线程销毁");
            return;
        }
        logger.debug("创建长连接成功");

        if(!getInputOutPutStream()){
            notifyNetTaskManagerLongConnectionTaskCreateFail();
            logger.debug("线程id = " + Thread.currentThread().getId() + "的线程销毁");
            return;
        }

        logger.debug("开始发送android端口");
        boolean ret = sendAndroidServerPortToPC();
        if (!ret){
            notifyNetTaskManagerLongConnectionTaskSendAndroidPortFail();
            logger.debug("线程id = " + Thread.currentThread().getId() + "的线程销毁");
            return;
        }

        logger.debug("发送android端口成功");
        socketFlag = TRUE;

        while(socketFlag){

            if (!longSocket.isConnected()) {
                logger.debug("接收数据前，连接已经断开");
                break;
            }

            logger.debug("等待pc回复.....");
            byte[] datas = readDatasFromPC(inputStream);
            if (null == datas){
                logger.debug("读数据为空");
                break;
            }

            logger.debug("发数据给PC,内容为： " + new String(datas));
            boolean ret1 = writeDatasToPC(outputStream, datas);
            if (!ret1){
                break;
            }

        }
        notifyNetTaskManagerLongConnectionTaskDestory();
        logger.debug("线程id = " + Thread.currentThread().getId() + "的线程销毁");

    }

    private Socket createSocket(){


        //服务器IP地址其实是本机的127.0.0.1 端口实际是adbd监听的端口,该端口是pc端调用reverse时，adbd监听的
        longSocket = socketWrap.createSocket(ServerConfig.ADBDConfig.getADBDIp(), ServerConfig.ADBDConfig.getADBDPort());
        if (null == longSocket){
            logger.debug("创建长连接socket失败");
            return null;
        }
        return longSocket;
    }

    private boolean getInputOutPutStream(){
        if (null != longSocket){
            try {
                inputStream = longSocket.getInputStream();
                outputStream = longSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
                logger.debug("从socket获取输入输出流发生异常");
                return false;
            }
        }else{
            logger.debug("从socket获取输入输出流失败");
            return false;
        }
        return true;
    }


    private boolean sendAndroidServerPortToPC(){
        boolean ret = writeDatasToPC(outputStream, Long.toString(ServerConfig.AndroidConfig.getServerPort()).getBytes());
        if (!ret){
            logger.debug("发送android端口失败");
            logger.debug("线程id = " + Thread.currentThread().getId() + "的线程销毁");
        }

        return ret;
    }

    private byte[] readDatasFromPC(InputStream inputStream){
        return socketWrap.readTickFromInputStream(inputStream);
    }

    private boolean writeDatasToPC(OutputStream outputStream,byte[] datas){
        return socketWrap.writeTickToOutputStream(outputStream, datas);
    }

    private void notifyNetTaskManagerLongConnectionTaskCreateFail(){
        logger.debug("通知网络管理器长连接任务创建失败");
        Message message = Message.obtain();
        message.what = NetTaskManager.MSG_NOTIFY_NET_MANAGER_LONG_TASK_CREATE_FAIL;
        message.setTarget(NetTaskManager.handler);
        NetTaskManager.handler.sendMessage(message);
    }

    private void notifyNetTaskManagerLongConnectionTaskSendAndroidPortFail(){
        logger.debug("通知网络管理器长连接任务发送安卓端口失败");
        Message message = Message.obtain();
        message.what = NetTaskManager.MSG_NOTIFY_NET_MANAGER_LONG_TASK_SEND_ANDROID_PORT_FAIL;
        message.setTarget(NetTaskManager.handler);
        NetTaskManager.handler.sendMessage(message);
    }
    private void notifyNetTaskManagerLongConnectionTaskDestory(){
        logger.debug("通知网络管理器长连接任务结束");
        Message message = Message.obtain();
        message.what = NetTaskManager.MSG_NOTIFY_NET_MANAGER_LONG_TASK_DESTPRY;
        message.setTarget(NetTaskManager.handler);
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

    private void closeSocketOfLongConnect(){
        socketFlag = FALSE;
        closeInputOutPutStread();
        socketWrap.closeSocket(longSocket);
    }

    @Override
    public void closeCurrentTask(){
        logger.debug("资源在回收");
        socketFlag = false;
        closeSocketOfLongConnect();
        logger.debug("资源释放完成");
    }



    public static void main(String[] args){

        try {
            Runtime.getRuntime().exec("adb forward tcp:12580 tcp:10086"); // 端口转换
        } catch (IOException e) {
            e.printStackTrace();
        }
        ServerConfig.ADBDConfig.setADBDPort(12580);
        new Thread( new LongConnectionTask()).start();
    }
}
