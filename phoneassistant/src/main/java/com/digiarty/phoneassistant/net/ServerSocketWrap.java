package com.digiarty.phoneassistant.net;

import com.digiarty.phoneassistant.boot.GlobalApplication;
import com.digiarty.phoneassistant.file.AndroidStorage;
import com.digiarty.phoneassistant.utils.ByteOrderUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
class ServerSocketWrap {

    private static Logger logger = LoggerFactory.getLogger(ServerSocketWrap.class);
    private static BufferedInputStream bis;
    private static BufferedOutputStream bos;
    private static ByteArrayOutputStream outWriter; //数据缓存到字节数组中
    private static FileOutputStream fileOutputStream;//文件缓存到文件里

    //数据长度的字节数====协议定死的
    private static final int BYTES_OF_DATA_SIZE = 8;
    private static final int CACHED_SIZE = 1024 * 8;


//    public static String cachedFilePath = null;//缓存文件路径
    public static String cachedFilePath = AndroidStorage.InternalStorage.getFilesDir(GlobalApplication.getContext()).getAbsolutePath();//缓存文件路径

    public static String getCachedFilePath() {
        return cachedFilePath;
    }

    public static void setCachedFilePath(String cachedFilePath) {
        ServerSocketWrap.cachedFilePath = cachedFilePath;
    }

    public static Socket createSocket(String hostIp, int port) {
        try {
            logger.debug("ip = " + hostIp);
            logger.debug("port = " + port);
            InetAddress serveraddr = InetAddress.getByName(hostIp);
            return new Socket(serveraddr, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            logger.debug(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug(e.getMessage());
        }
        return null;
    }

    public static ServerSocket createSocketForListen(int port) {

        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            if (null != serverSocket) {
                getServerSocketInformationCreatedBySystemDefault(serverSocket);
            } else {
                logger.debug("ServerSocket 为空");
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug("创建监听服务器套接字发生异常，信息为: " + e.getStackTrace().toString());
            return null;
        }
        return serverSocket;
    }

    public static void getServerSocketInformationCreatedBySystemDefault(ServerSocket serverSocket) {
        int port = serverSocket.getLocalPort();
//        String host = serverSocket.getInetAddress().getHostAddress();
        ServerConfig.AndroidConfig.setServerPort(port);
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



    private static long readDatasLengthFromBufferedInputStream() {
        byte[] bytes = new byte[BYTES_OF_DATA_SIZE];
        long dataLength;

        try {
            int len = bis.read(bytes);
            if (len > 0) {
                logger.debug("数据长度占得字节数为 " + len);
                dataLength = ByteOrderUtils.getLongFromBigEndian(bytes);//获取数据总长度
                logger.debug("数据的总长度为  " + dataLength);
                return dataLength;
            } else {
                logger.debug("第一行的字节数为 " + len);//如果为0，表示没有数据
                return -1;
            }

        } catch (IOException e) {
            logger.debug("读取socket数据异常" + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    private static byte[] readDatasFromBufferedInputStream(long dataLength) {
        byte[] bytes = null;
        byte[] cached = new byte[CACHED_SIZE];
        int readLenAlready = BYTES_OF_DATA_SIZE;
        int readlenNow = 0;

        outWriter = new ByteArrayOutputStream();
        try {
            while ((readlenNow = bis.read(cached)) != -1) {
                readLenAlready += readlenNow;
                outWriter.write(cached);
                if (readLenAlready == dataLength) {
                    logger.debug("数据读取完成");
                    break;
                }
            }
            outWriter.flush();
            bytes = outWriter.toByteArray();
            outWriter.close();
            logger.debug("读取数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("读取socket数据异常" + e.getMessage());
            return null;
        }
        return bytes;
    }

    public static byte[] readCommandFromInputStream(InputStream inputStream) {
        byte[] datas = null;
        if (null != inputStream) {
            bis = new BufferedInputStream(inputStream);

            long dataLength = readDatasLengthFromBufferedInputStream();
            if (dataLength != BYTES_OF_DATA_SIZE && dataLength != -1){
                datas = readDatasFromBufferedInputStream(dataLength);
                logger.debug("有数据要读");
            }else{
                logger.debug("读取数据的总长度发生错误");
            }
        } else {
            logger.debug("inputStream 为空");
            return null;
        }
        return datas;
    }

    //返回文件名称
    private static byte[] readFilesFromBufferedInputStream(long dataLength) {
        byte[] cached = new byte[CACHED_SIZE];
        int readLenAlready = 0;
        int readlenNow;

        try {
            logger.debug("缓存文件路径为 " + cachedFilePath);
            fileOutputStream = new FileOutputStream(cachedFilePath);
            while ((readlenNow = bis.read(cached)) != -1) {
                readLenAlready += readlenNow;
                fileOutputStream.write(cached);
                if (readLenAlready == dataLength) {
                    logger.debug("数据读取完成");
                    break;
                }
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            logger.debug("读取数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("读取socket数据异常" + e.getMessage());
            return null;
        }
        return cachedFilePath.getBytes();
    }


    public static byte[] readFilesFromInputStream(InputStream inputStream) {

        byte[] fileName = null;
        if (null != inputStream) {
            bis = new BufferedInputStream(inputStream);

            long dataLength = readDatasLengthFromBufferedInputStream();
            if (dataLength != BYTES_OF_DATA_SIZE){
                fileName = readFilesFromBufferedInputStream(dataLength - BYTES_OF_DATA_SIZE);
                logger.debug("有数据要读");
            }else{
                logger.debug("只有长度，没有内容");
                return null;
            }
        } else {
            logger.debug("inputStream 为空");
            return null;
        }
        return fileName;
    }

    public static boolean writeRequestToOutputStream(OutputStream outputStream, byte[] datas) {
        return writeDatasToOutputStream(outputStream, datas);
    }

    public static boolean writeDatasToOutputStream(OutputStream outputStream, byte[] datas) {
        OutputStream os = outputStream;
        byte[] bytes;

        //首BYTES_OF_DATA_SIZE字节为数据总长度
        if (BYTES_OF_DATA_SIZE != 0){
            long size;
            if (datas != null){
                size = datas.length + BYTES_OF_DATA_SIZE;
            }else{
                size = BYTES_OF_DATA_SIZE;
            }

            bytes =  new byte[(int) size];
            byte[] bytesOfLongLen = ByteOrderUtils.changeLongBytestoBigEndian(size);
            int i = 0;
            for (; i < bytesOfLongLen.length; i++){
                bytes[i] = bytesOfLongLen[i];
            }
            for (int j = 0; i < size; i++,j++){
                bytes[i] = datas[j];
            }

        }

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
        closeInputOutPutStream();
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
//    public static byte[] readBytes(InputStream is) {
//        byte[] bytes = null;
//        try {
//            bis = new BufferedInputStream(is);
//            byte[] cbuf = new byte[SIZE];
//            long data_sum_len = 0;
//            int len = 0;
//
//            //创建的时候，创建数据缓存区
//            //在网络传输中我们往往要传输很多变量，我们可以利用ByteArrayOutputStream把所有的变量收集到一起，然后一次性把数据发送出去
//            outWriter = new ByteArrayOutputStream();
//            len = bis.read(cbuf);//读取第一行数据
//            logger.debug("第一行的字节数为 " + len);
//            data_sum_len = ParseUtil.parseDataByteNums(cbuf);//获取数据总长度
//            logger.debug("数据的总长度为  " + data_sum_len);
//
//            //数据没有收完
//            if (len != data_sum_len) {
//                logger.debug("数据没有收完毕");
//                outWriter.write(cbuf, 0, len);
//                while ((len = bis.read(cbuf)) != -1) {
//                    len += len;
//                    logger.debug("读取的数据长度 len = " + len);
//                    outWriter.write(cbuf, 0, len);
//                    if (len == data_sum_len) {
//                        logger.debug("数据收取完毕");
//                        break;//数据收完
//                    }
//                }
//            }
//
//            outWriter.flush();
//            bytes = outWriter.toByteArray();
//            outWriter.close();
//            logger.debug("读取数据成功");
//
//        } catch (UnsupportedEncodingException e) {
//            logger.debug("读取socket数据异常" + e.getStackTrace().toString());
//            e.printStackTrace();
//            return null;
//        } catch (IOException e) {
//            logger.debug("读取socket数据异常" + e.getStackTrace().toString());
//            e.printStackTrace();
//            return null;
//        }
//        return bytes;
//    }


    /**
     * 通过字节输出流输出bytes
     *
     * @param os   输出流
     * @param text 字节数组
     */
    public static boolean writeBytes(OutputStream os, byte[] text) {
        if (text == null){
            return writeBytes(os, text, 0, 0);
        }
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
            return true;
        } catch (UnsupportedEncodingException e) {
            logger.debug("写入socket数据异常" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.debug("写入socket数据异常" + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static void closeInputOutPutStream() {
        if (bis != null) {
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (null != bos) {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (null != outWriter) {
            try {
                outWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if (null != fileOutputStream){
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws JSONException {

//        long len1 =  8 ;
//        byte[] bytesoflen = ByteOrderUtils.changeLongBytestoBigEndian(len1);
//        System.out.println(ByteOrderUtils.getLongFromBigEndian(bytesoflen));


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", "value");

        byte[] json = jsonObject.toString().getBytes();

        long len =  8 + json.length;
        byte[] bytesoflen = ByteOrderUtils.changeLongBytestoBigEndian(len);

        byte[] datas = new byte[(int) len];
        for (int i  =0; i < 8; i++){
            datas[i] = bytesoflen[i];
        }
        int j = 0;
        for (int i = 8; i < len && j < json.length; i++, j++) {
            datas[i] = json[j];
        }

//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //            byteArrayOutputStream.write(datas);
//            byteArrayOutputStream.flush();
//            byteArrayOutputStream.toByteArray();
        ByteArrayInputStream byteArrayInputStream =  new ByteArrayInputStream(datas);
        try {
            FileInputStream fileInputStream = new FileInputStream("/Users/henmory/Downloads/111.jpg");
            setCachedFilePath("/Users/henmory/Downloads/2.jpg");

            System.out.println(readFilesFromInputStream(fileInputStream).toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ByteOrderUtils.getLongFromBigEndian(datas);


    }

}
