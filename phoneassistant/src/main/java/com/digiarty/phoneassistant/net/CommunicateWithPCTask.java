package com.digiarty.phoneassistant.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
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
public class CommunicateWithPCTask implements Runnable {
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



        Thread.currentThread().setName("与客户端进行socket通信的线程");
        logger.debug("线程id = " + Thread.currentThread().getId() + "的线程开始启动，进行socket通信");
        getInputOutPutStream();

        while(socketFlag){

            if (!socketToCommunicateWithPC.isConnected()) {
                logger.debug("接收数据前，连接已经断开");
                break;
            }
            //先读后写
            byte[] datas = readDatasFromPC(inputStream);
            if (null == datas){
                break;
            }
            boolean ret = writeDatasToPC(outputStream,datas);
            if (!ret){

                break;
            }

        }
        socketFlag = FALSE;
        closeSocketOfCommunicating();
        logger.debug("线程id = " + Thread.currentThread().getId() + "的线程销毁");
    }

    private void getInputOutPutStream(){
        if (null != socketToCommunicateWithPC){
            try {
                inputStream = socketToCommunicateWithPC.getInputStream();
                outputStream = socketToCommunicateWithPC.getOutputStream();
                socketFlag = TRUE;
            } catch (IOException e) {
                e.printStackTrace();
                socketFlag = FALSE;
                logger.debug("从socket获取输入输出流发生异常");
            }
        }else{
            socketFlag = FALSE;
            logger.debug("从socket获取输入输出流失败");
        }
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

    private byte[] readDatasFromPC(InputStream inputStream){
        byte[] readDatas = ServerSocketWrap.readDatasFromInputStream(inputStream);
        if (null != readDatas){
            logger.debug("从客户端读取到的数据为:" + new String(readDatas));
        }
        return readDatas;
    }

    private boolean writeDatasToPC(OutputStream outputStream,byte[] datas){
        return ServerSocketWrap.writeDatasToOutputStream(outputStream, datas);
    }



    private void closeSocketOfCommunicating(){
        closeInputOutPutStread();
        ServerSocketWrap.closeSocket(socketToCommunicateWithPC);
    }
}
