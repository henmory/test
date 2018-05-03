package com.digiarty.phoneassistant.net;

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
public class ListenPCSocketTask {

    private static Logger logger = LoggerFactory.getLogger(ListenPCSocketTask.class);

    private static ExecutorService mExecutorService = null; //线程池

    private static Boolean serverListenSocketFlag = FALSE;


    public static void startListenPCSocketConnect() {

        ServerSocket serverSocket = createServerSocketForPCToConnect(ServerConfig.getServerPort());
        if (null != serverSocket) {
            logger.debug("服务器socket信息: serverSocket = " + serverSocket.toString());
        }
        mExecutorService = Executors.newCachedThreadPool();  //创建一个线程池

        if (null != serverSocket) {
            logger.debug("服务的socket创建完成，等待客户端接入");
            serverListenSocketFlag = TRUE;

            while (serverListenSocketFlag) {
                Socket socketToCommunicateWithPC = listenPCConnectAndCreateNewSocketForPCConnection(serverSocket);
                mExecutorService.execute(new CommunicateWithPCTask(socketToCommunicateWithPC)); //启动一个新的线程来处理连接
            }
            mExecutorService.shutdown();
            serverListenSocketFlag = FALSE;
            closeListenSocket(serverSocket);
        }
    }

    private static Socket listenPCConnectAndCreateNewSocketForPCConnection(ServerSocket serverSocket){

        Socket socketToCommunicateWithPC = listenAndCreateSocketForPCToCommunicate(serverSocket);
        logger.debug("出现新的PC连接,为其创建新的socket");

        if (socketToCommunicateWithPC != null) {
            logger.debug("新创建的socket信息: socketToCommunicateWithClient = " +  socketToCommunicateWithPC.toString());
        }else{
            logger.debug("新创建socket失败");
        }
        return socketToCommunicateWithPC;
    }

    public static ServerSocket createServerSocketForPCToConnect(int port) {
        return ServerSocketWrap.createSocketForListen(port);
    }

    public static Socket listenAndCreateSocketForPCToCommunicate(ServerSocket serverSocket){
        return ServerSocketWrap.listenAndCreatSocketForNewConnection(serverSocket);
    }


    public static void closeListenSocket(ServerSocket serverSocket) {
        ServerSocketWrap.closeListenSocket(serverSocket);
    }

}
