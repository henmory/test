package com.digiarty.phoneassistant.net.dataparse;

import com.alibaba.fastjson.JSON;
import com.digiarty.phoneassistant.net.NetDataType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
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
public class AddContactCommand implements ICommand {
    private static Logger logger = LoggerFactory.getLogger(AddContactCommand.class);

    private FromPC fromPC = new FromPC();

    private ToPC toPC = new ToPC();

    public FromPC getFromPC() {
        return fromPC;
    }

    public ToPC getToPC() {
        return toPC;
    }

    @Override
    public int parse(String jsonString) {
        try {
            fromPC = JSON.parseObject(jsonString, FromPC.class);
        } catch (Exception e) {
            logger.debug("解析数据出现异常 " + e.getMessage());
            return 0;
        }
        if (null == fromPC) {
            return 0;
        }
        return 1;
    }

    //存数据
    @Override
    public int doAction() {
        ArrayList<Result> results =  new ArrayList<>();
        Result result = new Result();
        result.setoId(1 +"");
        result.setnId(2 +"");
        result.setState(1 +"");
        results.add(result);
        toPC.setResult(results);

        Request request = new Request();
        request.number = 20 + "";

        toPC.setRequest(request);
        toPC.setCommand(fromPC.getCommand());
        return 1;
    }

    @Override
    public String reply() {
        String reply;
        try {
            reply = JSON.toJSONString(toPC);
            System.out.println(reply);

        } catch (Exception e) {
            logger.debug("回复PC端数据出现异常 " + e.getMessage());
            return null;
        }
        return reply;
    }

    @Override
    public void setNextReceivedDataType() {
        ParseDatasHandlerManager.getInstance().setType(NetDataType.JSONOBJECT);
    }


    static class ToPC {
        public String command;
        ArrayList<Result> result;
        Request request;

        public ToPC() {
        }

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }

        public ArrayList<Result> getResult() {
            return result;
        }

        public void setResult(ArrayList<Result> result) {
            this.result = result;
        }

        public Request getRequest() {
            return request;
        }

        public void setRequest(Request request) {
            this.request = request;
        }
    }

    static class Result {
        private String oId;
        private String nId;
        private String state;

        public Result() {
        }

        public String getoId() {
            return oId;
        }

        public void setoId(String oId) {
            this.oId = oId;
        }

        public String getnId() {
            return nId;
        }

        public void setnId(String nId) {
            this.nId = nId;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "oId='" + oId + '\'' +
                    ", nId='" + nId + '\'' +
                    ", state='" + state + '\'' +
                    '}';
        }
    }

    static class Request {
        private String number;

        public Request() {
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        @Override
        public String toString() {
            return "Request{" +
                    "number='" + number + '\'' +
                    '}';
        }
    }

    static class FromPC {
        private String command;
        private String num;

        public FromPC() {
        }

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            command = command;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            num = num;
        }

        @Override
        public String toString() {
            return "FromPC{" +
                    "Command='" + command + '\'' +
                    ", Num='" + num + '\'' +
                    '}';
        }
    }

}
