package com.digiarty.phoneassistant.model.dataparse;

import com.alibaba.fastjson.JSON;
import com.digiarty.phoneassistant.model.net.NetDataType;
import com.digiarty.phoneassistant.model.bean.beanfromclient.AddContactCommandFromClientBean;
import com.digiarty.phoneassistant.model.bean.beanfromclient.AddContactDataFromClientBean;
import com.digiarty.phoneassistant.model.bean.beantoclient.AddContactCommandToClientBean;
import com.digiarty.phoneassistant.model.bean.beantoclient.AddContactDataToClientBean;
import com.digiarty.phoneassistant.utils.Base64Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

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
    private AddContactDataFromClientBean dataFromClient = new AddContactDataFromClientBean();
    private AddContactDataToClientBean dataToClient = new AddContactDataToClientBean();


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
        ArrayList<AddContactCommandToClientBean.Result> results = new ArrayList<>();
        AddContactCommandToClientBean.Result result = new AddContactCommandToClientBean.Result();
        result.setoId(1 + "");
        result.setnId(2 + "");
        result.setState(1 + "");
        results.add(result);
        commandToClient.setResult(results);

        AddContactCommandToClientBean.Request request = new AddContactCommandToClientBean.Request();
        request.setNumber("20");

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
            dataFromClient = JSON.parseObject(jsonString, AddContactDataFromClientBean.class);
        } catch (Exception e) {
            logger.debug("解析数据出现异常 " + e.getMessage());
            return 0;
        }
        if (null == dataFromClient) {
            return 0;
        }
        logger.debug("解析出来的数据为: " + dataFromClient.toString());
        return 1;
    }

    @Override
    public int doActionByDatas() {
        logger.debug("开始写联系人信息到手机上.............");
        byte[] image = Base64Util.decode(dataFromClient.getContactBeans().get(0).getImage().getBytes());//base64转换
        try {
            Thread.sleep(5000);//做耗时操作
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.debug("联系人数据洗完，准备要下一波数据，并把本次操作结果返回");
        return 1;
    }


    @Override
    public String replyByDatas() {
        ArrayList<AddContactCommandToClientBean.Result> results = new ArrayList<>();
        AddContactCommandToClientBean.Result result = new AddContactCommandToClientBean.Result();
        result.setoId(1 + "");
        result.setnId(2 + "");
        result.setState(1 + "");
        results.add(result);
        commandToClient.setResult(results);

        AddContactCommandToClientBean.Request request = new AddContactCommandToClientBean.Request();
        request.setNumber("20");

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
