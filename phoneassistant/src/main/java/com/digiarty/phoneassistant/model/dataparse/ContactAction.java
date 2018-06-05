package com.digiarty.phoneassistant.model.dataparse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
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
    private List<ContactBeanWrap> contactBeanWraps = new ArrayList<>(); //包装后从PC端拿来的数据
    private AddContactDataToClientBean dataToClient = new AddContactDataToClientBean();
    //必须为static 需要有记录
    private static int alreadyReceivedContactNum = 0;
    private static int contactNum = 0;

    private static int requestContactNum;

    @Override
    public int parseCommand(String jsonString) {
        logger.debug("开始解析命令....... ");
        try {
            commandFromClient = JSON.parseObject(jsonString, AddContactCommandFromClientBean.class);
            contactNum = Integer.parseInt(commandFromClient.getNum());
            requestContactNum = ((contactNum > 10) ? 10 : contactNum);
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
        result.setNid(0 + "");
        result.setState(-1 + "");
        results.add(result);
        commandToClient.setResult(results);

        //每次请求requestContactNum条联系人数据
        AddContactCommandToClientBean.Request request = new AddContactCommandToClientBean.Request();
        request.setNum(requestContactNum + "");

        commandToClient.setRequest(request);
//        commandToClient.setCommand(commandFromClient.getCommand());
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


    //回复数据
    @Override
    public int parseDatas(String jsonString) {
        logger.debug("开始解析数据........ ");
        try {
            AddContactDataFromClientBean dataFromClient = JSON.parseObject(jsonString, AddContactDataFromClientBean.class);
            for (int i = 0; i < dataFromClient.getData().size(); i++) {
                ContactBeanWrap beanWrap = new ContactBeanWrap();
                beanWrap.contactBean = dataFromClient.getData().get(i);
                beanWrap.image = Base64Util.decode(dataFromClient.getData().get(i).getImage().getBytes());//base64转换
                contactBeanWraps.add(beanWrap);
            }
        } catch (Exception e) {
            logger.debug("解析数据出现异常 " + e.getMessage());
            return 0;
        }
        if (null == contactBeanWraps) {
            return 0;
        }
        logger.debug("解析出来的数据为: " + contactBeanWraps.toString());
        return 1;
    }

    private void requestNextContactDataNumber(ArrayList<AddContactCommandToClientBean.Result> replies) {
        logger.debug("已经接收的联系人数量为 " + alreadyReceivedContactNum);
        logger.debug("总数量为  " + contactNum);

        commandToClient.setResult(replies);
        AddContactCommandToClientBean.Request request = new AddContactCommandToClientBean.Request();


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
        ArrayList<AddContactCommandToClientBean.Result> replies = (ArrayList<AddContactCommandToClientBean.Result>) manager.insertDatas(GlobalApplication.getContext(), CONTACT, contactBeanWraps);

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

    public static class ContactBeanWrap {
        @JSONField(name = "ContactBean")
        ContactBean contactBean;
        @JSONField(name = "Image")
        byte[] image;

        public ContactBeanWrap() {
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
            return "ContactBeanWrap{" +
                    "contactBean=" + contactBean +
                    ", image=" + Arrays.toString(image) +
                    '}';
        }
    }

    public static void main(String[] arg) {
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
