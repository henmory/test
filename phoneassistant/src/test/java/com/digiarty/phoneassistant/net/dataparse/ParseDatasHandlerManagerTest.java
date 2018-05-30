package com.digiarty.phoneassistant.net.dataparse;

import com.digiarty.phoneassistant.net.NetDataType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
class ParseDatasHandlerManagerTest {

    ParseDatasHandlerManager manager;
    @BeforeEach
    void setUp() {
        manager = ParseDatasHandlerManager.getInstance();
    }

    @Test
    void doActionAndPrepareDatasToPC() {
        NetDataType type = NetDataType.COMMAND;
        String data = "{\"command\": \"AddContacts\", \"num\":20}";

        ParseDatasHandlerManager.Reply reply = manager.doActionAndPrepareDatasToPC(type, data.getBytes());
        byte[] datas = reply.getDatas();
        System.out.println(new String(datas));


    }
}