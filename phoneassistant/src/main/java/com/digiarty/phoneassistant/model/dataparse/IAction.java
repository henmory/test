package com.digiarty.phoneassistant.model.dataparse;

/***
 *
 * Created on：2018/5/29
 *
 * Created by：henmory
 *
 * Description: 针对不同数据类型做的不同操作
 *
 *
 **/
interface IAction {
    int parseCommand(String jsonString);
    int doActionByCommand();
    String replyByCommand();
    void setNextReceivedDataTypeByCommand();


    int parseDatas(String jsonString);
    int doActionByDatas();
    String replyByDatas();
    void setNextReceivedDataTypeByDatas();
}
