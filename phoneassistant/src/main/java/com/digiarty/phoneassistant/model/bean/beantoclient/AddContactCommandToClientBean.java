package com.digiarty.phoneassistant.model.bean.beantoclient;

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
public class AddContactCommandToClientBean {
    private String Command;
    ArrayList<Result> Result;
    Request Request;

    public AddContactCommandToClientBean() {
    }

    public String getCommand() {
        return Command;
    }

    public void setCommand(String command) {
        Command = command;
    }

    public ArrayList<AddContactCommandToClientBean.Result> getResult() {
        return Result;
    }

    public void setResult(ArrayList<AddContactCommandToClientBean.Result> result) {
        Result = result;
    }

    public AddContactCommandToClientBean.Request getRequest() {
        return Request;
    }

    public void setRequest(AddContactCommandToClientBean.Request request) {
        Request = request;
    }

    @Override
    public String toString() {
        return "AddContactCommandToClientBean{" +
                "Command='" + Command + '\'' +
                ", Result=" + Result +
                ", Request=" + Request +
                '}';
    }

    public static class Result {
        private String Oid;
        private String Nid;
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
