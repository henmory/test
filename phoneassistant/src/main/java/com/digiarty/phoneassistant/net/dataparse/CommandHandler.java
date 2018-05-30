package com.digiarty.phoneassistant.net.dataparse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/***
 *
 * Created on：2018/5/28
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public class CommandHandler implements IHandler {
    private static Logger logger = LoggerFactory.getLogger(CommandHandler.class);
    private ICommand command;
    @Override
    public int parse(byte[] datas) {

        String str = new String(datas, Charset.defaultCharset());
        if (str.contains(CommandKey.KEY_ADD_CONTACTS)) {
            command = new AddContactCommand();
        }else{
            logger.debug("解析数据失败,无效command");
            return 0;
        }
        logger.debug("解析得到PC端数据为 " + str);
        return command.parse(str);
    }

    @Override
    public int doAction() {
        command.doAction();
        return 1;
    }

    @Override
    public byte[] reply() {
        String data = command.reply();
        return data.getBytes(Charset.defaultCharset());
    }

    @Override
    public void setNextReceivedDataType() {
        command.setNextReceivedDataType();
    }
}
