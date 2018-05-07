package com.digiarty.phoneassistant.net;

import android.util.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/***
 *
 * Created on：24/04/2018
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public class ServerSocketWrap {
    private static Logger logger = LoggerFactory.getLogger(ServerSocketWrap.class);

    private static BufferedInputStream bis;
    private static BufferedOutputStream bos;
    //查一下怎么设置tcp缓存区大小和socket默认的缓存区

    private final static int SOCKET_SEND_BUFFER_SIZE = 1024;
    private final static int SOCKET_RECV_BUFFER_SIZE = 1024;

    private static final int SIZE = 1024 * 8;

    public static Socket createSocket(String hostIp, int port){
        try {
            InetAddress serveraddr = InetAddress.getByName(hostIp);
            return new Socket(serveraddr, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static ServerSocket createSocketForListen(int port) {

        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            if (null != serverSocket){
                getServerSocketInformationCreatedBySystemDefault(serverSocket);
//                logger.debug("监听socketIP = " + ServerConfig.AndroidConfig.getIp() + "port= " + ServerConfig.AndroidConfig.getPort());
            }else{
                logger.debug("ServerSocket 为空");
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug("创建监听服务器套接字发生异常，信息为: " + e.getStackTrace().toString());
            return null;
        }
        return serverSocket;
    }

    public static void getServerSocketInformationCreatedBySystemDefault(ServerSocket serverSocket){
        int port = serverSocket.getLocalPort();
        String host = serverSocket.getInetAddress().getHostAddress();
        ServerConfig.AndroidConfig.setIp(host);
        ServerConfig.AndroidConfig.setPort(port);
    }


    public static Socket listenAndCreatSocketForNewConnection(ServerSocket serverSocket) {

        Socket socket = null;
        try {
            socket = serverSocket.accept();
        } catch (IOException e) {
            logger.debug("为客户端链接创建socket失败，异常信息为：" + e.getStackTrace());
            return null;
        }
        return socket;
    }

    public static byte[] readDatasFromInputStream(InputStream inputStream) {
        byte[] datas = null;

        InputStream is = inputStream;
        if (null != is) {
            datas = readBytes(is);
            return datas;
        } else {
            return null;
        }
    }

    public static boolean writeDatasToOutputStream(OutputStream outputStream, byte[] datas) {
        OutputStream os = outputStream;
        if (null != os) {
            return writeBytes(os, datas);
        } else {
            logger.debug("由于OutputStream为空，导致数据不能写入");
            return false;
        }
    }


    public static void closeSocket(Socket socket) {

        if (socket != null) {
            if (socket.isClosed()) {
                logger.debug("关闭socket");
                return;
            }
            try {
                socket.close();
            } catch (IOException e) {
                logger.debug("socket关闭发生异常");
                e.printStackTrace();
            }
        } else {
            logger.debug("socket关闭时，发现为空");
        }
        closeInputOutStream();
    }

    public static void closeListenSocket(ServerSocket socket) {

        if (socket != null) {
            if (socket.isClosed()) {
                return;
            }
            try {
                socket.close();
            } catch (IOException e) {
                logger.debug("关闭监听socket");
                e.printStackTrace();
            }
        } else {
            logger.debug("监听socket关闭时，发现为 空");
        }
    }


    /**
     * 以字节流的方式读取到字符串。
     *
     * @param is 输入流
     * @return 字节数组
     */
    public static byte[] readBytes(InputStream is) {
        byte[] bytes = null;
        try {
            bis = new BufferedInputStream(is);
            byte[] cbuf = new byte[SIZE];
            int len;

            //创建的时候，创建数据缓存区
            //在网络传输中我们往往要传输很多变量，我们可以利用ByteArrayOutputStream把所有的变量收集到一起，然后一次性把数据发送出去
            ByteArrayOutputStream outWriter = new ByteArrayOutputStream();
            len = bis.read(cbuf);
            if (-1 == len) {
                logger.debug("读取数据失败");
                outWriter.close();
                return null;
            } else {
                outWriter.write(cbuf, 0, len);
                outWriter.flush();

                //写到字节中
                bytes = outWriter.toByteArray();
                outWriter.close();
//                logger.debug("读取数据成功");
            }

        } catch (UnsupportedEncodingException e) {
            logger.debug("读取socket数据异常" + e.getStackTrace().toString());
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            logger.debug("读取socket数据异常" + e.getStackTrace().toString());
            e.printStackTrace();
            return null;
        }
        return bytes;
    }


    /**
     * 通过字节输出流输出bytes
     *
     * @param os   输出流
     * @param text 字节数组
     */
    public static boolean writeBytes(OutputStream os, byte[] text) {
        return writeBytes(os, text, 0, text.length);
    }

    /**
     * 通过字节输出流输出bytes
     *
     * @param os     输出流
     * @param text   字节数组
     * @param off    数组起始下标
     * @param lenght 长度
     */
    public static boolean writeBytes(OutputStream os, byte[] text, int off, int lenght) {
        try {
            bos = new BufferedOutputStream(os);
            bos.write(text, off, lenght);
            bos.flush();
//            logger.debug("写数据成功");
            return true;
        } catch (UnsupportedEncodingException e) {
            logger.debug("写入socket数据异常" + e.getStackTrace());
            e.printStackTrace();
        } catch (IOException e) {
            logger.debug("写入socket数据异常" + e.getStackTrace());
            e.printStackTrace();
        }
        return false;
    }

    public static void closeInputOutStream(){
        if (bis != null){
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (null != bos){
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
