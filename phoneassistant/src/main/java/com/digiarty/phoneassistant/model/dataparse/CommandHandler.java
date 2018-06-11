package com.digiarty.phoneassistant.model.dataparse;

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
class CommandHandler implements IHandler {
    private static Logger logger = LoggerFactory.getLogger(CommandHandler.class);
    private IAction action;

    public CommandHandler() {
        logger.debug("CommandHandler 开始处理");
    }

    @Override
    public int parse(byte[] datas) {

        String str = new String(datas, Charset.defaultCharset());
        if (str.contains(CommandKey.KEY_ADD_CONTACTS)) {
            action = new ContactAction();
        }else{
            logger.debug("解析数据失败,无效command");
            logger.debug("出错的数据字节码为 " + datas);
            logger.debug("出错的数据为 " + str);
            return 0;
        }
        logger.debug("收到的PC端数据为 " + str);
        return action.parseCommand(str);
    }

    @Override
    public int doAction() {
        action.doActionByCommand();
        return 1;
    }

    @Override
    public byte[] reply() {
        String data = action.replyByCommand();
        return data.getBytes(Charset.defaultCharset());
    }

    @Override
    public void setNextReceivedDataType() {
        action.setNextReceivedDataTypeByCommand();
    }
}
