package com.digiarty.phoneassistant.net.dataparse;

import android.os.Looper;

import com.digiarty.phoneassistant.net.NetDataType;
import com.digiarty.phoneassistant.net.NetTaskManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;


/***
 *
 * Created on：2018/5/25
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public class ParseDatasHandlerManager {
    private static Logger logger = LoggerFactory.getLogger(ParseDatasHandlerManager.class);
    private NetDataType type;
    private IHandler handler;


    NetDataType getType() {
        return type;
    }
    void setType(NetDataType type) {
        this.type = type;
    }


    private static class SingletonHolder {
        private static final ParseDatasHandlerManager INSTANCE = new ParseDatasHandlerManager();
    }

    public static final ParseDatasHandlerManager getInstance() {
        return ParseDatasHandlerManager.SingletonHolder.INSTANCE;
    }


    public Reply doActionAndPrepareDatasToPC(NetDataType type, byte[] datas){
        this.type = type;

        Reply reply = new Reply();
        int ret = 0;

        if (type.equals(NetDataType.COMMAND)) {
            handler = new CommandHandler();
        } else if (type.equals(NetDataType.JSONOBJECT)) {
            handler = new DatasHandler();
        } else if (type.equals(NetDataType.FILE)) {
            handler = new FileHandler();
        } else {
            logger.debug("无效的数据类型");
            prepareReplyErrorData(reply);
            return reply;
        }
        ret = handler.parse(datas);
        if (0 == ret){
            logger.debug("数据解析失败");
            prepareReplyErrorData(reply);
            return reply;
        }

        ret = handler.doAction();
        if (0 == ret){
            logger.debug("操作失败");
            prepareReplyErrorData(reply);
            return reply;
        }
        byte[] data = handler.reply();
        handler.setNextReceivedDataType();

        reply.datas = data;
        reply.nextReceicedDataType = type;
        return reply;
    }

    private void prepareReplyErrorData(Reply reply){
        reply.setDatas(new byte[]{-1});
    }


    public class Reply{
        private byte[] datas;
        private NetDataType nextReceicedDataType;

        public Reply() {
        }

        public byte[] getDatas() {
            return datas;
        }

        public void setDatas(byte[] datas) {
            this.datas = datas;
        }

        public NetDataType getNextReceicedDataType() {
            return nextReceicedDataType;
        }

        public void setNextReceicedDataType(NetDataType nextReceicedDataType) {
            this.nextReceicedDataType = nextReceicedDataType;
        }

        @Override
        public String toString() {
            return "Reply{" +
                    "datas=" + Arrays.toString(datas) +
                    ", nextReceicedDataType=" + nextReceicedDataType +
                    '}';
        }
    }







}
