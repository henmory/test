package com.digiarty.phoneassistant.model.bean.beanfromclient;

import com.alibaba.fastjson.annotation.JSONField;

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
    @JSONField(name = "Command")
    private String Command;
    @JSONField(name = "Num")
    private String Num;

    public AddContactCommandFromClientBean() {
    }


    public String getCommand() {
        return Command;
    }

    public void setCommand(String command) {
        Command = command;
    }

    public String getNum() {
        return Num;
    }

    public void setNum(String num) {
        Num = num;
    }

    @Override
    public String toString() {
        return "AddContactCommandFromClientBean{" +
                "Command='" + Command + '\'' +
                ", Num='" + Num + '\'' +
                '}';
    }
}
