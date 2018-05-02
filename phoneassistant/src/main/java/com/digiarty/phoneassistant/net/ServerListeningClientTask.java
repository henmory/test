package com.digiarty.phoneassistant.net;

import android.util.Log;

import com.digiarty.phoneassistant.boot.MyBroadcastReceiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
public class ServerListeningClientTask {

    private static Logger logger = LoggerFactory.getLogger(ServerListeningClientTask.class);

    private static ExecutorService mExecutorService = null; //线程池

    private static Boolean serverListenSocketFlag = FALSE;

    private final static String TAG = ServerListeningClientTask.class.getSimpleName();

    public static void androidServerStartListenForClientConnect() {

        ServerSocket serverSocket = createServerSocketForClientToConnect(ServerConfig.getServerPort());
        if (null != serverSocket){
            logger.debug("服务器socket信息: serverSocket = " +serverSocket.toString());
        }
        mExecutorService = Executors.newCachedThreadPool();  //创建一个线程池

        if (null != serverSocket) {
            logger.debug("服务的socket创建完成，等待客户端接入");
            serverListenSocketFlag = TRUE;

            while (serverListenSocketFlag) {

                    Socket socketToCommunicateWithClient = createSocketForClientToCommunicate(serverSocket);
                    if (socketToCommunicateWithClient != null) {
                        logger.debug("为客户端新创建的socket信息: socketToCommunicateWithClient = " +  socketToCommunicateWithClient.toString());
                    }else{
                        logger.debug("与客户端通信的socket创建为空");
                    }
                    logger.debug("为客户端创建socket");
                    if (null != socketToCommunicateWithClient) {
                        mExecutorService.execute(new CommunicateWithClientTask(socketToCommunicateWithClient)); //启动一个新的线程来处理连接
                    } else {
                        logger.debug("为客户端创建socket失败");
                        break;
                    }

            }
            mExecutorService.shutdown();
            serverListenSocketFlag = FALSE;
            closeListenSocket(serverSocket);
        }
    }

    public static ServerSocket createServerSocketForClientToConnect(int port){
        return ServerSocketWrap.createSocketForListen(port);
    }

    public static Socket createSocketForClientToCommunicate(ServerSocket serverSocket){
        return ServerSocketWrap.creatSocketForNewConnection(serverSocket);
    }
    public static void closeListenSocket(ServerSocket serverSocket){
        ServerSocketWrap.closeListenSocket(serverSocket);
    }

}
