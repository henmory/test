package com.digiarty.phoneassistant.model.dataparse;

import com.alibaba.fastjson.JSON;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assume.assumeThat;

/***
 *
 * Created on：2018/5/30
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
class CommandHandlerTest {

    CommandHandler command;
    @BeforeEach
    void setUp() {
        command =  new CommandHandler();
    }

    @Test
    void parse() {
        ContactAction.FromPC pc = new ContactAction.FromPC();
        pc.setCommand("AddContacts");
        pc.setNum("20");
        byte[] bytes = JSON.toJSONString(pc).getBytes();
        assumeThat(1, equalTo(command.parse(bytes)));
    }

    @Test
    void doAction() {
    }

    @Test
    void reply() {
    }
}