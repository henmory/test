package com.digiarty.phoneassistant.model.dataparse;

import com.alibaba.fastjson.JSON;
import com.digiarty.phoneassistant.boot.GlobalApplication;
import com.digiarty.phoneassistant.model.bean.CommandFromClientBean;
import com.digiarty.phoneassistant.model.bean.ContactBean;
import com.digiarty.phoneassistant.model.bean.ContactBeanWrap;
import com.digiarty.phoneassistant.model.bean.ResponseToClientBean;
import com.digiarty.phoneassistant.model.dataprovider.ModelManager;
import com.digiarty.phoneassistant.model.net.NetDataType;
import com.digiarty.phoneassistant.utils.Base64Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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

    private CommandFromClientBean commandFromClient = new CommandFromClientBean();
    private ResponseToClientBean commandToClient = new ResponseToClientBean();
    private List<ContactBeanWrap> contactBeanWraps = new ArrayList<>(); //包装后从PC端拿来的数据

    //必须为static 需要有记录
    private static int alreadyReceivedContactNum = 0;//已经收到的数量
    private static int contactNum = 0;//总数量
    private static int requestContactNum;//本次请求的数量

    @Override
    public int parseCommand(String jsonString) {
        logger.debug("开始解析命令....... ");
        try {
            commandFromClient = JSON.parseObject(jsonString, CommandFromClientBean.class);
            contactNum = Integer.parseInt(commandFromClient.getNum());
            requestContactNum = ((contactNum > 10) ? 10 : contactNum);
        } catch (Exception e) {
            logger.debug("解析数据出现异常 " + e.getMessage());
            logger.debug("出错的数据为 " + jsonString);
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
        ArrayList<ResponseToClientBean.Result> results = new ArrayList<>();
        ResponseToClientBean.Result result = new ResponseToClientBean.Result();
        result.setOid(0 + "");
        result.setNid(0 + "");
        result.setState(-1 + "");
        results.add(result);
        commandToClient.setResult(results);

        //每次请求requestContactNum条联系人数据
        ResponseToClientBean.Request request = new ResponseToClientBean.Request();
        request.setNum(requestContactNum + "");

        commandToClient.setRequest(request);
//        commandToClient.setCommand(commandFromClient.getCommand());
        return 1;
    }

    @Override
    public String replyByCommand() {
        String reply = null;
        try {
            reply = JSON.toJSONString(commandToClient);
            logger.debug("回复pc端数据为 " + reply);

        } catch (Exception e) {
            logger.debug("回复PC端数据出现异常 " + e.getMessage());
            logger.debug("出现异常的数据为 " + reply);
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
            for (int i = 0; i < dataFromClient.getData().size(); i++) {
                ContactBeanWrap beanWrap = new ContactBeanWrap();
                beanWrap.setContactBean(dataFromClient.getData().get(i));
                beanWrap.setImage(Base64Util.decode(dataFromClient.getData().get(i).getImage().getBytes()));//base64转换
                contactBeanWraps.add(beanWrap);
            }
        } catch (Exception e) {
            logger.debug("解析数据出现异常 " + e.getMessage());
            logger.debug("失败的数据为" + jsonString);
            return 0;
        }
        if (null == contactBeanWraps) {
            logger.debug("解析出来的数据长度为0");
            return 1;
        }
        logger.debug("解析出来的数据为: " + contactBeanWraps.toString());
        return 1;
    }

    private void requestNextContactDataNumber(ArrayList<ResponseToClientBean.Result> replies) {

        logger.debug("已经接收的联系人数量为 " + alreadyReceivedContactNum);
        logger.debug("总数量为  " + contactNum);

        commandToClient.setResult(replies);
        ResponseToClientBean.Request request = new ResponseToClientBean.Request();


        if (alreadyReceivedContactNum < contactNum) {
            logger.debug("继续发送同步数据请求，联系人数据没有同步完成");
            request.setNum(requestContactNum + "");
        } else {
            logger.debug("数据同步完成，总共接收数据长度为" + alreadyReceivedContactNum);
            request.setNum("0");
            contactNum = 0;
            alreadyReceivedContactNum = 0;
        }

        commandToClient.setRequest(request);
//        commandToClient.setCommand(commandToClient.getCommand());
    }

    //写数据到手机上===dataFromClient
    @Override
    public int doActionByDatas() {
        logger.debug("开始写联系人信息到手机上.............");


        ModelManager manager = ModelManager.getInstance();
        ArrayList<ResponseToClientBean.Result> replies = (ArrayList<ResponseToClientBean.Result>) manager.insertDatas(GlobalApplication.getContext(), CONTACT, contactBeanWraps);

        alreadyReceivedContactNum += contactBeanWraps.size();


        logger.debug("联系人数据写完，准备要下一波数据，并把本次操作结果返回");
        requestNextContactDataNumber(replies);
        return 1;
    }


    @Override
    public String replyByDatas() {

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
        if (alreadyReceivedContactNum < contactNum) {
            ParseDatasHandlerManager.getInstance().setDataType(NetDataType.JSONOBJECT);
            logger.debug("联系人数据没有同步完成，设置下次接收数据类型");
        } else {
            ParseDatasHandlerManager.getInstance().setDataType(NetDataType.COMMAND);
            contactNum = 0;
            alreadyReceivedContactNum = 0;
            logger.debug("联系人数据同步完成，设置下次接收数据类型");
        }
    }


    public static class AddContactDataFromClientBean {
        private String Command;
        private List<ContactBean> Data;

        public AddContactDataFromClientBean() {
        }

        public String getCommand() {
            return Command;
        }

        public void setCommand(String command) {
            Command = command;
        }

        public List<ContactBean> getData() {
            return Data;
        }

        public void setData(List<ContactBean> data) {
            Data = data;
        }

        @Override
        public String toString() {
            return "AddContactDataFromClientBean{" +
                    "Command='" + Command + '\'' +
                    ", Data=" + Data +
                    '}';
        }





    }





    public static void main(String[] arg) {
//        CommandFromClientBean data1 = new CommandFromClientBean();
//        data1.setCommand("command");
//        data1.setNum(null);
////        String data = "{\"command\":\"AddContacts\",\"num\":\"20\"}";
//        String data = JSON.toJSONString(data1);
//        System.out.println(data1);
//        CommandFromClientBean commandFromClient = JSON.parseObject(data, CommandFromClientBean.class);
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
        System.out.println("ab = " + ab);
        a ac = JSON.parseObject(cdata, a.class);
        System.out.println("ac = " + ac);

    }


    static class a {
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

    static class b extends a {


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

    static class c extends a {


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
