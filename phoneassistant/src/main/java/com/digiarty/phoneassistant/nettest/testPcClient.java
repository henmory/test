package com.digiarty.phoneassistant.nettest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.digiarty.phoneassistant.model.bean.beanfromclient.AddContactDataFromClientBean;
import com.digiarty.phoneassistant.model.bean.beanfromclient.ContactBean;
import com.digiarty.phoneassistant.utils.Base64Util;
import com.digiarty.phoneassistant.utils.ByteOrderUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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

                System.out.println("开始发送数据..................................");
                byte[] datas = sendCommand();
                out.write(datas);
                out.flush();

                System.out.println("1 finish sending the data");
                System.out.println("开始读取数据..................................");
                String strFormsocket = readFromSocket(in);
                System.out.println("the data sent by server is: " + strFormsocket);
                Thread.sleep(5000);

                System.out.println("开始发送数据..................................");
                datas = sendData();
                System.out.println("发送的字符串为 " + new String(datas));
                out.write(datas);
                out.flush();
                Thread.sleep(3000);

                System.out.println("开始读取数据..................................");
                strFormsocket = readFromSocket(in);
                System.out.println("the data sent by server is: " + strFormsocket);

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


        String data = "{\"Command\":\"AddContacts\",\"Num\":\"20\"}";
        byte[] bytes = data.getBytes(Charset.defaultCharset());

        Long a = new Long(bytes.length);
        byte[] b  = ByteOrderUtils.long2byte(a);

        byte[] datas = new byte[bytes.length + b.length];
        int i = 0;
        int j = 0;
        for (; i < b.length; i++) {
            datas[i] = b[i];
        }
        for (; j < bytes.length; j++, i++) {
            datas[i] = bytes[j];
        }
        System.out.println("数据字节码为 : " + Arrays.toString(datas));
        return datas;
    }

    public static byte[] readPicture(String path){
        BufferedInputStream in = null;
        byte[] content = null;
        try {
            in = new BufferedInputStream(new FileInputStream(path));
            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);

            byte[] temp = new byte[1024];
            int size = 0;
            while ((size = in.read(temp)) != -1) {
                out.write(temp, 0, size);
            }
            in.close();
            content = out.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;

    }

    private static byte[] sendData() {

        AddContactDataFromClientBean addContactDataFromClientBean = new AddContactDataFromClientBean();
        addContactDataFromClientBean.setCommand("AddContacts");

        List<ContactBean> contactBeans = new ArrayList<>();
        ContactBean bean =  new ContactBean();
        bean.setCompanyName("company");
        bean.setDepartment("department");
        bean.setLastName("henmory");
        bean.setFirstName("han");
        bean.setMiddleName("maohui");
        bean.setJobTitle("engineer");
        bean.setKey("123456");
        bean.setNickname("hmh");
        bean.setKey("key");
        bean.setAddressList(null);
        bean.setEmailAddressList(null);
        bean.setEvent(null);
        bean.setImList(null);
        bean.setNotes("note");
        bean.setUrlList(null);
        bean.setRelatedNameList(null);
        bean.setPhoneNumberList(null);

        byte[] image = readPicture("/Users/henmory/Downloads/2.jpg");
        String str = Base64Util.encode(image);
        bean.setImage(str);

        contactBeans.add(bean);
        addContactDataFromClientBean.setData(contactBeans);

        byte[] bytes = JSON.toJSONString(addContactDataFromClientBean, SerializerFeature.WriteMapNullValue).getBytes(Charset.defaultCharset());
        Long a = new Long(bytes.length);
        byte[] b  = ByteOrderUtils.long2byte(a);

        byte[] datas = new byte[bytes.length + b.length];
        System.out.println("发送给android的数据大小为 " + a);

        int i = 0;
        int j = 0;
        for (; i < b.length; i++) {
            datas[i] = b[i];
        }
        for (; j < bytes.length; j++, i++) {
            datas[i] = bytes[j];
        }
        System.out.println("发送的字节码为 " + Arrays.toString(datas));

        return datas;

    }

    private static byte[] sendFile() {

        /* 服务器反馈：准备接收 */
        byte[] filebytes = readPicture("/Users/henmory/Downloads/20180601_114647.jpg");
        System.out.println("发送文件的大小 = " + filebytes.length);

        /* 将整数转成4字节byte数组 */
        byte[] filelength = new byte[8];
        filelength = ByteOrderUtils.long2byte(filebytes.length);
        System.out.println("大小的字节码 = " + Arrays.toString(filelength));


        byte[] datas = new byte[filebytes.length + 8];
        int i = 0;
        int j = 0;
        for (; i < filelength.length; i++) {
            datas[i] = filelength[i];
        }
        for (; j < filebytes.length; j++, i++) {
            datas[i] = filebytes[j];
        }
        System.out.println("数据字节码为 : " + Arrays.toString(datas));
        return datas;
    }


}

