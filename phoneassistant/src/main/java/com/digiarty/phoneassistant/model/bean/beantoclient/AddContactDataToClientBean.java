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
public class AddContactDataToClientBean {
    private String command;
    ArrayList<Result> result;
    Request request;

    public AddContactDataToClientBean() {
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


    public static class Result {
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

    public static class Request {
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

    @Override
    public String toString() {
        return "AddContactDataToClientBean{" +
                "command='" + command + '\'' +
                ", result=" + result +
                ", request=" + request +
                '}';
    }
}
