package com.digiarty.phoneassistant.net.dataparse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
class AddContactCommandTest {

    AddContactCommand contactCommand;
    @BeforeEach
    void setUp() {
        contactCommand =  new AddContactCommand();
    }

    @Test
    void parse() {
        String data = "{\"command\": \"AddContacts\", \"num\":20}";
        contactCommand.parse(data);
        assumeThat(contactCommand.getFromPC().getCommand(),equalTo("AddContacts"));
        assumeThat(contactCommand.getFromPC().getNum(),equalTo("20"));
    }

    @Test
    void doAction() {
    }

    @Test
    void reply() {
        AddContactCommand.ToPC toPC = contactCommand.getToPC();
        toPC.result =  new ArrayList<>();
        toPC.setCommand("AddContacts");

        AddContactCommand.Request request = new AddContactCommand.Request();
        request.setNumber(1 + "");
        toPC.setRequest(request);

        AddContactCommand.Result result = new AddContactCommand.Result();
        result.setState(1 + "");
        result.setoId(0 + "");
        result.setnId(2+ "");
        toPC.result.add(result);

        result.setState(11 + "");
        result.setnId(00 + "");
        result.setoId(22+ "");
        toPC.result.add(result);

        assumeThat(null, equalTo(contactCommand.reply()));

    }
}