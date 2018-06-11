package com.digiarty.phoneassistant.model.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;

/***
 *
 * Created on：2018/5/31
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public class ResponseToClientBean {
//    @JSONField(name = "Command")
//    private String Command;
    @JSONField(name = "Result")
    ArrayList<Result> Result;
    @JSONField(name = "Request")
    Request Request;

    public ResponseToClientBean() {
    }

//    public String getCommand() {
//        return Command;
//    }
//
//    public void setCommand(String command) {
//        Command = command;
//    }

    public ArrayList<ResponseToClientBean.Result> getResult() {
        return Result;
    }

    public void setResult(ArrayList<ResponseToClientBean.Result> result) {
        Result = result;
    }

    public ResponseToClientBean.Request getRequest() {
        return Request;
    }

    public void setRequest(ResponseToClientBean.Request request) {
        Request = request;
    }

    @Override
    public String toString() {
        return "ResponseToClientBean{" +
//                "Command='" + Command + '\'' +
                ", Result=" + Result +
                ", Request=" + Request +
                '}';
    }

    public static class Result {
        @JSONField(name = "Oid")
        private String Oid;
        @JSONField(name = "Nid")
        private String Nid;
        @JSONField(name = "State")
        private String State;

        public Result() {
        }

        public String getOid() {
            return Oid;
        }

        public void setOid(String oid) {
            Oid = oid;
        }

        public String getNid() {
            return Nid;
        }

        public void setNid(String nid) {
            Nid = nid;
        }

        public String getState() {
            return State;
        }

        public void setState(String state) {
            State = state;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "Oid='" + Oid + '\'' +
                    ", Nid='" + Nid + '\'' +
                    ", State='" + State + '\'' +
                    '}';
        }
    }

    public static class Request {
        @JSONField(name = "Num")
        private String Num;

        public Request() {
        }

        public String getNum() {
            return Num;
        }

        public void setNum(String num) {
            Num = num;
        }

        @Override
        public String toString() {
            return "Request{" +
                    "Num='" + Num + '\'' +
                    '}';
        }
    }
}
