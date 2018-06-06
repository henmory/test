package com.digiarty.phoneassistant.model.dataparse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/***
 *
 * Created on：2018/5/29
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public class DatasHandler implements IHandler{
    private static Logger logger = LoggerFactory.getLogger(DatasHandler.class);
    private IAction action;

    public DatasHandler() {
        logger.debug("DatasHandler 开始处理");
    }

    @Override
    public int parse(byte[] datas) {
//        logger.debug("收到的PC端数据为 " + new String(datas, 0, datas.length));
        String str = new String(datas, Charset.defaultCharset());
        if (str.contains(CommandKey.KEY_ADD_CONTACTS)) {
            action = new ContactAction();
        }else{
            logger.debug("解析数据失败,无效action");
            return 0;
        }
        return action.parseDatas(str);
    }

    @Override
    public int doAction() {
        return action.doActionByDatas();
    }

    @Override
    public byte[] reply() {
        String data = action.replyByDatas();
        return data.getBytes(Charset.defaultCharset());
    }

    @Override
    public void setNextReceivedDataType() {
        action.setNextReceivedDataTypeByDatas();
    }
}
