package com.digiarty.phoneassistant.nettest;

import com.digiarty.phoneassistant.utils.ByteOrderUtils;
import com.digiarty.phoneassistant.utils.FileHelper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;


/**
 * 测试usb与pc通信 通过adb端口转发方式
 *
 * @author chl
 */
public class testPcClient {
    private static final String TAG = "testPcClient";

    public static void main(String[] args) throws InterruptedIOException {

        try {
            // adb 指令
            Runtime.getRuntime().exec("adb shell am start -S -n \"com.digiarty.phoneassistant/com.digiarty.phoneassistant.activity.PermissionRequestActivity\" -a android.intent.action.MAIN --es port \"8000\"");
            Thread.sleep(3000);
            Runtime.getRuntime().exec("adb forward tcp:12580 tcp:10086"); // 端口转换
			Thread.sleep(3000);
//			Runtime.getRuntime().exec("adb shell am broadcast -a NotifyServiceStart -n com.example.codetest/.ServiceBroadcastReceiver");
//			Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();

        }
        Socket socket = null;
        try {
            InetAddress serveraddr = null;
            serveraddr = InetAddress.getByName("127.0.0.1");

            System.out.println("TCP 1111    " + "Client: Connecting...");

            socket = new Socket(serveraddr, 12580);

            System.out.println("TCP 221122" + "C:RECEIVE");

            BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
            BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
            boolean flag = true;
            while (flag) {
                byte[] datas = sendCommand();
                out.write(datas);
                out.flush();

                System.out.println("1 finish sending the data");
                String strFormsocket = readFromSocket(in);
                System.out.println("the data sent by server is: " + strFormsocket);
                System.out.println("=============================================");

                
            }

        } catch (UnknownHostException e1) {
            System.out.println("TCP 331133" + "ERROR:" + e1.toString());
        } catch (Exception e2) {
            System.out.println("TCP 441144" + "ERROR:" + e2.toString());
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                    System.out.println("socket.close()");
                }
            } catch (IOException e) {
                System.out.println("TCP 5555" + "ERROR:" + e.toString());
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
        byte[] b  = ByteOrderUtils.changeLongBytestoBigEndian(a);

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

    private static byte[] sendData() {
        return null;
    }

    private static byte[] sendFile() {
//        out.write("4".getBytes());
//        out.flush();
//        System.out.println("send file finish sending the CMD：");
//
//        /* 服务器反馈：准备接收 */
//        String strFormsocket = readFromSocket(in);
//        System.out.println("service ready receice data:UPDATE_CONTACTS:" + strFormsocket);
//        byte[] filebytes = FileHelper.readFile("ucliulanqi.apk");
//        System.out.println("fileszie = " + filebytes.length);
//        /* 将整数转成4字节byte数组 */
//        byte[] filelength = new byte[4];
//        filelength = MyUtil.intToByte(filebytes.length);
//        byte[] fileformat = null;
//        fileformat = ".png".getBytes();
//        System.out.println("fileformat length=" + fileformat.length);
//        /* 字节流中前4字节为文件长度，4字节文件格式，以后是文件流 */
//        /* 注意如果write里的byte[]超过socket的缓存，系统自动分包写过去，所以对方要循环写完 */
////					out.write(filelength);
//        out.flush();
//        String strok1 = readFromSocket(in);
//        System.out.println("service receive filelength :" + strok1);
//
//        System.out.println("write data to android");
////					out.write(filebytes);
//        out.flush();
//        System.out.println("*********");
//
//        /* 服务器反馈：接收成功 */
//        String strread = readFromSocket(in);
//        System.out.println(" send data success:" + strread);
//        System.out.println("=============================================");
        return null;
    }
}

