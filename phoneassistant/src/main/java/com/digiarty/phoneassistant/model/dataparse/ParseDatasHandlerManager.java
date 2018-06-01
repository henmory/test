package com.digiarty.phoneassistant.model.dataparse;


import com.digiarty.phoneassistant.model.net.IShortConnectionTask;
import com.digiarty.phoneassistant.model.net.NetDataType;
import com.digiarty.phoneassistant.model.net.Reply;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/***
 *
 * Created on：2018/5/25
 *
 * Created by：henmory
 *
 * Description: 数据解析器，仅仅服务解析不同类型的数据
 *
 *
 **/
public class ParseDatasHandlerManager implements IShortConnectionTask {
    private static Logger logger = LoggerFactory.getLogger(ParseDatasHandlerManager.class);
    private NetDataType dataType;
    private IHandler handler;


    public NetDataType getDataType() {
        return dataType;
    }

    public void setDataType(NetDataType dataType) {
        this.dataType = dataType;
    }

    private static class SingletonHolder {
        private static final ParseDatasHandlerManager INSTANCE = new ParseDatasHandlerManager();
    }

    public static ParseDatasHandlerManager getInstance() {
        return ParseDatasHandlerManager.SingletonHolder.INSTANCE;
    }

    @Override
    public Reply doActionAndPrepareDatasToPC(NetDataType type, byte[] datas){
        dataType = type;

        Reply reply = new Reply();
        int ret;
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

        reply.setDatas(data);
        reply.setNextReceicedDataType(dataType);//注意这里的参数
        return reply;
    }

    private void prepareReplyErrorData(Reply reply){
        reply.setNextReceicedDataType(NetDataType.COMMAND); //出现错误了，等待PC端发送新的指令
        reply.setDatas(new byte[]{-1});
    }

}
