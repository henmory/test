package com.digiarty.phoneassistant.nettest;

import com.digiarty.phoneassistant.utils.ByteOrderUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

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
    final static int SERVER_PORT = 9000;

    public static void main(String[] arg) {
        try {
            Process p = Runtime.getRuntime().exec("adb reverse tcp:8000 tcp:9000"); // 端口转换
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        doListen();


    }

    private static void doListen() {
        serverSocket = null;
        try {
            System.out.println("开始监听客户端socket");
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("服务的socket创建完成，等待客户端接入");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("为客户端创建socket");
                if (null == socket) {
                    System.out.println("为客户端创建socket 失败");
                    return;
                }

                new Thread(new ThreadReadWriterIOSocket(socket)).start();
                System.out.println("有数据接入");
            }
        } catch (IOException e) {
            System.out.println("创建服务器套接字失败");
            e.printStackTrace();
        }
    }

    static class ThreadReadWriterIOSocket implements Runnable {
        Socket socket;

        public ThreadReadWriterIOSocket(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            try {
                BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
                BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
                boolean flag = true;
                while (flag) {

                    String datas = readFromSocket(in);
                    System.out.println("服务器接收数据为: " + datas);

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    byte[] sned = datas.getBytes();
                    System.out.println("服务器发送的数据为：" + new String(sned));


                    out.write(sned);
                    out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("服务器发生异常");
            }
        }
    }

    /* 从InputStream流中读数据 */
    public static String readFromSocket(InputStream in) {
        int MAX_BUFFER_BYTES = 4000;
        String msg = "";
        byte[] tempbuffer = new byte[MAX_BUFFER_BYTES];
        try {
            int numReadedBytes = in.read(tempbuffer, 0, tempbuffer.length);
//			Log.d(TAG, "readFromSocket: numReadedBytes" + numReadedBytes);
            msg = new String(tempbuffer, 0, numReadedBytes, "utf-8");

            tempbuffer = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

    private static byte[] sendCommand() {


        String data = "{\"command\": \"AddContacts\", \"num\":20}";
        byte[] bytes = data.getBytes(Charset.defaultCharset());

        Long a = new Long(8 + bytes.length);
        byte[] b = ByteOrderUtils.long2byte(a);

        byte[] datas = new byte[bytes.length + b.length];
        int i = 0;
        int j = 0;
        for (; i < b.length; i++) {
            datas[i] = b[i];
        }
        for (; j < bytes.length; j++, i++) {
            datas[i] = bytes[j];
        }
        return datas;
    }

}
