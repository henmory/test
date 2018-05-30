package com.digiarty.phoneassistant.nettest;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/***
 *
 * Created on：15/05/2018
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public class Server {
    static ServerSocket serverSocket = null;
    final static int  SERVER_PORT = 8000;

    public static void main(String[] arg){
        new Thread()
        {
            public void run()
            {
                doListen();
            }
        }.start();
        try {
            Process p = Runtime.getRuntime().exec("adb reverse tcp:8000 tcp:9000"); // 端口转换
            System.out.println(p.toString());
            while(true){
                ;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void doListen()
    {
        serverSocket = null;
        try
        {
            System.out.println("开始监听客户端socket");
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("服务的socket创建完成，等待客户端接入");
            while (true)
            {
                Socket socket = serverSocket.accept();
                System.out.println("为客户端创建socket");
                if (null == socket){
                    System.out.println("为客户端创建socket 失败");
                    return;
                }

//                new Thread(new ThreadReadWriterIOSocket(this, socket)).start();
                System.out.println("有数据接入");
            }
        } catch (IOException e)
        {
            System.out.println("创建服务器套接字失败");
            e.printStackTrace();
        }
    }

}
