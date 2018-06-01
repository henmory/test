package com.digiarty.phoneassistant.model.bean.beanfromclient;


import java.util.List;

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
public class AddContactDataFromClientBean {
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
