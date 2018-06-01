package com.digiarty.phoneassistant.model.dataparse;

import com.alibaba.fastjson.JSON;
import com.digiarty.phoneassistant.model.bean.beanfromclient.AddContactCommandFromClientBean;
import com.digiarty.phoneassistant.model.bean.beanfromclient.AddContactDataFromClientBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 *
 * Created on：2018/6/1
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public class PictureAction implements IAction{
    private static Logger logger = LoggerFactory.getLogger(PictureAction.class);

    @Override
    public int parseCommand(String jsonString) {
        logger.debug("开始解析命令....... ");
        try {
            logger.debug("客服端发送同步图片的请求 ");
//            commandFromClient = JSON.parseObject(jsonString, AddContactCommandFromClientBean.class);
        } catch (Exception e) {
            logger.debug("解析数据出现异常 " + e.getMessage());
            return 0;
        }
//        if (null == commandFromClient) {
//            return 0;
//        }
//        logger.debug("解析出来的数据为: " + commandFromClient.toString());
        return 1;
    }

    @Override
    public int doActionByCommand() {
        return 0;
    }

    @Override
    public String replyByCommand() {
        return null;
    }

    @Override
    public void setNextReceivedDataTypeByCommand() {

    }

    @Override
    public int parseDatas(String jsonString) {

//        logger.debug("开始解析数据........ ");
//        try {
//            dataFromClient = JSON.parseObject(jsonString, AddContactDataFromClientBean.class);
//        } catch (Exception e) {
//            logger.debug("解析数据出现异常 " + e.getMessage());
//            return 0;
//        }
//        if (null == dataFromClient) {
//            return 0;
//        }
//        logger.debug("解析出来的数据为: " + dataFromClient.toString());
        return 1;
    }

    @Override
    public int doActionByDatas() {
        return 0;
    }

    @Override
    public String replyByDatas() {
        return null;
    }

    @Override
    public void setNextReceivedDataTypeByDatas() {

    }
}
