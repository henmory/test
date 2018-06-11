package com.digiarty.phoneassistant.model.dataparse;

/***
 *
 * Created on：2018/5/29
 *
 * Created by：henmory
 *
 * Description: 解析数据的基本步骤
 *
 *
 **/
interface IHandler {
    int parse(byte[] datas);
    int doAction();
    byte[] reply();
    void setNextReceivedDataType();
}
