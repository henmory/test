package com.digiarty.phoneassistant.net;

import com.digiarty.phoneassistant.utils.ByteOrderUtils;

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
public class LongConnectionTask implements Runnable{
    private static Logger logger = LoggerFactory.getLogger(LongConnectionTask.class);

    private Socket longSocket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Boolean socketFlag = FALSE;

    @Override
    public void run() {

        Thread.currentThread().setName("长连接线程");
        logger.debug("线程id = " + Thread.currentThread().getId() + "的线程开始启动，先发送android端服务的端口号，之后监听pc端心跳");

        longSocket = createSocket(ServerConfig.PCServerConfig.getServerIp(), ServerConfig.PCServerConfig.getServerPort());
        if (null == longSocket){
            logger.debug("创建长连接socket失败");
            // TODO: 03/05/2018 杀死应用吧,重新开始
            logger.debug("线程id = " + Thread.currentThread().getId() + "的线程销毁");
            return;
        }
        logger.debug("创建长连接成功");
        socketFlag = TRUE;

        getInputOutPutStream();

        boolean ret = sendAndroidServerPortToPC();
        if (!ret){
            closeSocketOfCommunicating();
            logger.debug("线程id = " + Thread.currentThread().getId() + "的线程销毁");
            return;
        }

        while(socketFlag){
            if (!longSocket.isConnected()) {
                logger.debug("接收数据前，连接已经断开");
                break;
            }

            byte[] datas = readDatasFromPC(inputStream);
            if (null == datas){
                break;
            }

            //先写后读
            boolean ret1 = writeDatasToPC(outputStream, new String("收到心跳包").getBytes());
            if (!ret1){
                break;
            }

        }
        closeSocketOfCommunicating();
        logger.debug("线程id = " + Thread.currentThread().getId() + "的线程销毁");

    }

    private Socket createSocket(String ip, int serverPort){
        return ServerSocketWrap.createSocket(ip, serverPort);
    }

    private void getInputOutPutStream(){
        if (null != longSocket){
            try {
                inputStream = longSocket.getInputStream();
                outputStream = longSocket.getOutputStream();
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

    private void closeSocketOfCommunicating(){
        socketFlag = FALSE;
        closeInputOutPutStread();
        ServerSocketWrap.closeSocket(longSocket);
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
    private boolean sendAndroidServerPortToPC(){
        boolean ret = writeDatasToPC(outputStream, ByteOrderUtils.int2byte(ServerConfig.AndroidServerConfig.getServerPort()));
        return ret;
    }

    private byte[] readDatasFromPC(InputStream inputStream){
        byte[] readDatas = ServerSocketWrap.readDatasFromInputStream(inputStream);
        if (null != readDatas){
            logger.debug("从PC读取到的数据为:" + new String(readDatas));
        }
        return readDatas;
    }

    private boolean writeDatasToPC(OutputStream outputStream,byte[] datas){
        return ServerSocketWrap.writeDatasToOutputStream(outputStream, datas);
    }
}
