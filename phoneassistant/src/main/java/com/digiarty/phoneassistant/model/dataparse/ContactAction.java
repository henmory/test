package com.digiarty.phoneassistant.model.dataparse;

import com.alibaba.fastjson.JSON;
import com.digiarty.phoneassistant.boot.GlobalApplication;
import com.digiarty.phoneassistant.model.bean.beanfromclient.ContactBean;
import com.digiarty.phoneassistant.model.dataprovider.ModelManager;
import com.digiarty.phoneassistant.model.net.NetDataType;
import com.digiarty.phoneassistant.model.bean.beanfromclient.AddContactCommandFromClientBean;
import com.digiarty.phoneassistant.model.bean.beanfromclient.AddContactDataFromClientBean;
import com.digiarty.phoneassistant.model.bean.beantoclient.AddContactCommandToClientBean;
import com.digiarty.phoneassistant.model.bean.beantoclient.AddContactDataToClientBean;
import com.digiarty.phoneassistant.utils.Base64Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.digiarty.phoneassistant.model.dataprovider.ProviderDataType.CONTACT;

/***
 *
 * Created on：2018/5/29
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public class ContactAction implements IAction {
    private static Logger logger = LoggerFactory.getLogger(ContactAction.class);
    private AddContactCommandFromClientBean commandFromClient = new AddContactCommandFromClientBean();
    private AddContactCommandToClientBean commandToClient = new AddContactCommandToClientBean();
    private List<ContactBeans> contactBeans = new ArrayList<>(); //包装后从PC端拿来的数据
    private AddContactDataToClientBean dataToClient = new AddContactDataToClientBean();
    private final int requestContactNum = 20;
    @Override
    public int parseCommand(String jsonString) {
        logger.debug("开始解析命令....... ");
        try {
            commandFromClient = JSON.parseObject(jsonString, AddContactCommandFromClientBean.class);
        } catch (Exception e) {
            logger.debug("解析数据出现异常 " + e.getMessage());
            return 0;
        }
        if (null == commandFromClient) {
            return 0;
        }
        logger.debug("解析出来的数据为: " + commandFromClient.toString());
        return 1;
    }

    @Override
    public int doActionByCommand() {

        //没有数据同步
        ArrayList<AddContactCommandToClientBean.Result> results = new ArrayList<>();
        AddContactCommandToClientBean.Result result = new AddContactCommandToClientBean.Result();
        result.setOid(0 + "");
        result.setOid(0 + "");
        result.setState(-1 + "");
        results.add(result);
        commandToClient.setResult(results);

        //每次请求requestContactNum条联系人数据
        AddContactCommandToClientBean.Request request = new AddContactCommandToClientBean.Request();
        request.setNum(requestContactNum + "");

        commandToClient.setRequest(request);
        commandToClient.setCommand(commandFromClient.getCommand());
        return 1;
    }

    @Override
    public String replyByCommand() {
        String reply;
        try {
            reply = JSON.toJSONString(commandToClient);
            logger.debug("回复pc端数据为 " + reply);

        } catch (Exception e) {
            logger.debug("回复PC端数据出现异常 " + e.getMessage());
            return null;
        }
        return reply;
    }

    @Override
    public void setNextReceivedDataTypeByCommand() {
        ParseDatasHandlerManager.getInstance().setDataType(NetDataType.JSONOBJECT);
    }
    @Override
    public int parseDatas(String jsonString) {
        logger.debug("开始解析数据........ ");
        try {
            AddContactDataFromClientBean dataFromClient = JSON.parseObject(jsonString, AddContactDataFromClientBean.class);
            ContactBeans bean = new ContactBeans();
            for (int i = 0; i < dataFromClient.getData().size(); i++) {
                bean.contactBean = dataFromClient.getData().get(i);
                bean.image = Base64Util.decode(dataFromClient.getData().get(i).getImage().getBytes());//base64转换
                contactBeans.add(bean);
            }
        } catch (Exception e) {
            logger.debug("解析数据出现异常 " + e.getMessage());
            return 0;
        }
        if (null == contactBeans) {
            return 0;
        }
        logger.debug("解析出来的数据为: " + contactBeans.toString());
        return 1;
    }

    //写数据到手机上===dataFromClient
    @Override
    public int doActionByDatas() {
        logger.debug("开始写联系人信息到手机上.............");

        ModelManager manager = ModelManager.getInstance();
        manager.insertDatas(GlobalApplication.getContext(), CONTACT, contactBeans);

        logger.debug("联系人数据洗完，准备要下一波数据，并把本次操作结果返回");
        return 1;
    }


    @Override
    public String replyByDatas() {
        ArrayList<AddContactCommandToClientBean.Result> results = new ArrayList<>();
        AddContactCommandToClientBean.Result result = new AddContactCommandToClientBean.Result();
        result.setOid(1 + "");
        result.setNid(2 + "");
        result.setState(1 + "");
        results.add(result);
        commandToClient.setResult(results);

        AddContactCommandToClientBean.Request request = new AddContactCommandToClientBean.Request();
        request.setNum(requestContactNum + "");

        commandToClient.setRequest(request);
        commandToClient.setCommand(commandToClient.getCommand());

        String reply;
        try {
            reply = JSON.toJSONString(commandToClient);
            logger.debug("回复pc端数据为 " + reply);

        } catch (Exception e) {
            logger.debug("回复PC端数据出现异常 " + e.getMessage());
            return null;
        }
        return reply;
    }

    @Override
    public void setNextReceivedDataTypeByDatas() {
        ParseDatasHandlerManager.getInstance().setDataType(NetDataType.JSONOBJECT);
    }

    public static class ContactBeans{
        ContactBean contactBean;
        byte[] image;

        public ContactBeans() {
        }

        public ContactBean getContactBean() {
            return contactBean;
        }

        public void setContactBean(ContactBean contactBean) {
            this.contactBean = contactBean;
        }

        public byte[] getImage() {
            return image;
        }

        public void setImage(byte[] image) {
            this.image = image;
        }

        @Override
        public String toString() {
            return "ContactBeans{" +
                    "contactBean=" + contactBean +
                    ", image=" + Arrays.toString(image) +
                    '}';
        }
    }

    public static void main(String[] arg){
//        AddContactCommandFromClientBean data1 = new AddContactCommandFromClientBean();
//        data1.setCommand("command");
//        data1.setNum(null);
////        String data = "{\"command\":\"AddContacts\",\"num\":\"20\"}";
//        String data = JSON.toJSONString(data1);
//        System.out.println(data1);
//        AddContactCommandFromClientBean commandFromClient = JSON.parseObject(data, AddContactCommandFromClientBean.class);
//        System.out.println(commandFromClient);



        b b1 = new b();
        b1.setA("b");
        b1.setData("b");
        c c1 = new c();
        c1.setA("c");
        c1.setData("c");
        String bdata = JSON.toJSONString(b1);
        System.out.println("b = " + bdata);
        String cdata = JSON.toJSONString(c1);
        System.out.println("c = " + cdata);
        a ab = JSON.parseObject(bdata, a.class);
        System.out.println("ab = " +  ab);
        a ac = JSON.parseObject(cdata, a.class);
        System.out.println("ac = " +  ac);

    }




    static class a{
        String a;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        String data;
        public a() {
        }

        public a(String a) {
            this.a = a;
        }

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        @Override
        public String toString() {
            return "a{" +
                    "a='" + a + '\'' +
                    ", data='" + data + '\'' +
                    '}';
        }
    }
    static class b extends a{




        public b() {
        }


        @Override
        public String toString() {
            return "b{" +
                    "a='" + a + '\'' +
                    ", data='" + data + '\'' +
                    '}';
        }
    }
    static class c extends a{


        public c() {
        }


        @Override
        public String toString() {
            return "c{" +
                    "a='" + a + '\'' +
                    ", data='" + data + '\'' +
                    '}';
        }
    }

}
