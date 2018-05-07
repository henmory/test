package com.digiarty.phoneassistant.net;

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
public class CommunicateWithPCTask implements ITask {

    private static Logger logger = LoggerFactory.getLogger(ListenPCSocketTask.class);
    private Socket socketToCommunicateWithPC;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Boolean socketFlag = FALSE;

    public CommunicateWithPCTask(Socket socket) {
        socketToCommunicateWithPC = socket;
    }

    @Override
    public void run() {

        Thread.currentThread().setName("与PC进行socket通信的线程");
        logger.debug("线程id = " + Thread.currentThread().getId() + "的线程开始启动，进行socket通信");

        if(!getInputOutPutStream()){
            NetTaskManager.notifyNetTaskManagerClearAllTask();
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
            logger.debug("从socket获取输入输出流失败");
            return false;
        }
        return true;
    }


    private byte[] readDatasFromPC(InputStream inputStream){
        byte[] readDatas = ServerSocketWrap.readDatasFromInputStream(inputStream);
        return readDatas;
    }

    private boolean writeDatasToPC(OutputStream outputStream,byte[] datas){
        return ServerSocketWrap.writeDatasToOutputStream(outputStream, datas);
    }

    public void closeInputOutPutStread(){
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
