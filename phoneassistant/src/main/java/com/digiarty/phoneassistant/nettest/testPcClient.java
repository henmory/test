package com.digiarty.phoneassistant.nettest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.digiarty.phoneassistant.model.bean.ContactBean;
import com.digiarty.phoneassistant.model.dataparse.ContactAction;
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

            System.out.println("开始发送命令..................................");
            byte[] datas = sendCommand();
            out.write(datas);
            out.flush();

            System.out.println("1 finish sending the data");
            System.out.println("开始读取数据..................................");
            String strFormsocket = readFromSocket(in);
            System.out.println("the data sent by server is: " + strFormsocket);

            while (flag) {

                System.out.println("开始发送数据..................................");
                datas = sendData();
                System.out.println("发送的字符串为 " + new String(datas));
                out.write(datas);
                out.flush();
                Thread.sleep(5000);

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


        String data = "{\"Command\":\"AddContacts\",\"Num\":\"5\"}";
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

        ContactAction.AddContactDataFromClientBean addContactDataFromClientBean = new ContactAction.AddContactDataFromClientBean();
        addContactDataFromClientBean.setCommand("AddContacts");

        List<ContactBean> beanList = new ArrayList<>();
        ContactBean bean = new ContactBean();
        //设置key
        bean.setKey("key");

        //设置图片
//        byte[] image = FileHelper.readBytes(new File("/storage/emulated/0/DCIM/1.png"));
//        beanWrap.setImage(image);

        //设置名字
        bean.setLastName("aaa");
        bean.setFirstName("aaa");
        bean.setMiddleName("hen111mory");

        //设置组织
        bean.setCompanyName("digiarty");
        bean.setDepartment("development");
        bean.setJobTitle("engineer");

        //设置手机
        List<ContactBean.ContactKeyValueEntity> phoneList = new ArrayList<>();
        phoneList.add(new ContactBean.ContactKeyValueEntity(2+"", "13488783239"));
        phoneList.add(new ContactBean.ContactKeyValueEntity(1+"", "1215644454565"));
        bean.setPhoneNumberList(phoneList);

        //设置电子邮箱
        List<ContactBean.ContactKeyValueEntity> mailList = new ArrayList<>();
        mailList.add(new ContactBean.ContactKeyValueEntity(2+"", "han@gmail.com"));
        mailList.add(new ContactBean.ContactKeyValueEntity(1+"", "han@163.com"));
        bean.setEmailAddressList(phoneList);

        //设置地址
        List<ContactBean.ContactAddressEntity> addressEntityList = new ArrayList<>();

        ContactBean.ContactAddressEntity addressEntity = new ContactBean.ContactAddressEntity();
        addressEntity.setCity("chengdu");
        addressEntity.setCountry("china");
        addressEntity.setPostalCode("20155");
        addressEntity.setState("sichuan");
        addressEntity.setStreet("tianfu 3th");
        addressEntity.setType("home");
        addressEntityList.add(addressEntity);

        bean.setAddressList(addressEntityList);

        //设置网址
        List<ContactBean.ContactKeyValueEntity> urlList = new ArrayList<>();
        urlList.add(new ContactBean.ContactKeyValueEntity(2+"", "www.baidu.com"));
        urlList.add(new ContactBean.ContactKeyValueEntity(1+"", "www.digiarty.com"));
        bean.setUrlList(urlList);

        //设置事件
        List<ContactBean.ContactKeyValueEntity> eventList = new ArrayList<>();
        eventList.add(new ContactBean.ContactKeyValueEntity(2+"", "2018112456658"));
        eventList.add(new ContactBean.ContactKeyValueEntity(1+"", "201811245665855555"));
        bean.setEvent(eventList);


        //设置注释 todo 与ios不同
//        List<ContactBean.ContactKeyValueEntity> noteList = new ArrayList<>();
//        noteList.add(new ContactBean.ContactKeyValueEntity(2+"", "注释1"));
//        noteList.add(new ContactBean.ContactKeyValueEntity(1+"", "注释12"));
//        bean.setNotes(noteList);
        bean.setNotes("注释");

        //设置im
        List<ContactBean.ContactKeyValueEntity> IMList = new ArrayList<>();
        IMList.add(new ContactBean.ContactKeyValueEntity(2+"", "qq15156465"));
        IMList.add(new ContactBean.ContactKeyValueEntity(1+"", "gmail 456465"));
        bean.setImList(IMList);

        //设置昵称
        bean.setNickname("hmh");

        //设置关系
        List<ContactBean.ContactKeyValueEntity> relationList = new ArrayList<>();
        relationList.add(new ContactBean.ContactKeyValueEntity(2+"", "李四"));
        relationList.add(new ContactBean.ContactKeyValueEntity(1+"", "张三"));
        bean.setRelatedNameList(relationList);


        beanList.add(bean);
        beanList.add(bean);
        addContactDataFromClientBean.setData(beanList);





        byte[] image = readPicture("/Users/henmory/Downloads/2.jpg");
        String str = Base64Util.encode(image);
        bean.setImage(str);


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

