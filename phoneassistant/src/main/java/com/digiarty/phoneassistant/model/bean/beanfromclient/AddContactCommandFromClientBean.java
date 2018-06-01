package com.digiarty.phoneassistant.model.bean.beanfromclient;

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
public class AddContactCommandFromClientBean {
    private String command;
    private String num;

    public AddContactCommandFromClientBean() {
    }


    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "AddContactCommandFromClientBean{" +
                "command='" + command + '\'' +
                ", num='" + num + '\'' +
                '}';
    }
}
