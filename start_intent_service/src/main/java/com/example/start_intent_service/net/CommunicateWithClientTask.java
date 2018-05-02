package com.example.start_intent_service.net;

import android.util.Log;

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
public class CommunicateWithClientTask implements Runnable {

    private String TAG = CommunicateWithClientTask.class.getSimpleName();
    private Socket socketToCommunicateWithClient;
    private InputStream inputStream;
    private OutputStream outputStream;

    private Boolean serverHandleClientConnectSocketFlag = FALSE;

    public CommunicateWithClientTask(Socket socket) {
        socketToCommunicateWithClient = socket;

    }

    @Override
    public void run() {
        long thread_id = Thread.currentThread().getId();
        Log.d(TAG, "run: 线程id开始" +thread_id);
        getInputOutPutStream();

        while(serverHandleClientConnectSocketFlag){

            if (!socketToCommunicateWithClient.isConnected()) {
                Log.d(TAG, "run: 接收数据前，连接已经断开");
                break;
            }
            //先读后写
            byte[] datas = readDatasFromClient(inputStream);
            if (null == datas){
                break;
            }
            boolean ret = writeDatasToSocket(outputStream,datas);
            if (!ret){

                break;
            }

        }
        serverHandleClientConnectSocketFlag = FALSE;
        closeSocketOfCommunicating();
        Log.d(TAG, "run: 线程id = 结束" +thread_id);
    }

    private void getInputOutPutStream(){
        if (null != socketToCommunicateWithClient){
            try {
                inputStream = socketToCommunicateWithClient.getInputStream();
                outputStream = socketToCommunicateWithClient.getOutputStream();
                serverHandleClientConnectSocketFlag = TRUE;
            } catch (IOException e) {
                e.printStackTrace();
                serverHandleClientConnectSocketFlag = FALSE;
                Log.d(TAG, "CommunicateWithClientTask: 从socket获取输入输出流发生异常");
            }
        }else{
            serverHandleClientConnectSocketFlag = FALSE;
            Log.d(TAG, "CommunicateWithClientTask: 从socket获取输入输出流失败");
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

    private byte[] readDatasFromClient(InputStream inputStream){
        byte[] readDatas = ServerSocketWrap.readDatasFromInputStream(inputStream);
        if (null != readDatas){
            Log.d(TAG, "run: 读取到的数据为" + new String(readDatas));
        }
        return readDatas;
    }

    private boolean writeDatasToSocket(OutputStream outputStream,byte[] datas){
        return ServerSocketWrap.writeDatasToOutputStream(outputStream, datas);
    }

    private void closeSocketOfCommunicating(){
        closeInputOutPutStread();
        ServerSocketWrap.closeSocket(socketToCommunicateWithClient);
    }
}
