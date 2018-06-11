package com.digiarty.phoneassistant.model.bean;

import com.alibaba.fastjson.annotation.JSONField;

/***
 *
 * Created on：2018/6/7
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public class CommandFromClientBean {
    @JSONField(name = "Command")
    private String Command;
    @JSONField(name = "Num")
    private String Num;

    public CommandFromClientBean() {
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
        return "CommandFromClientBean{" +
                "Command='" + Command + '\'' +
                ", Num='" + Num + '\'' +
                '}';
    }
}
