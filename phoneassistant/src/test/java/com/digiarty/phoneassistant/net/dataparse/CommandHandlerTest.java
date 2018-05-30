package com.digiarty.phoneassistant.net.dataparse;

import com.alibaba.fastjson.JSON;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assume.assumeThat;
import static org.junit.jupiter.api.Assertions.*;

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
        AddContactCommand.FromPC pc = new AddContactCommand.FromPC();
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