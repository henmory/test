package com.digiarty.phoneassistant.net.dataparse;

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
public interface IHandler {
    int parse(byte[] datas);
    int doAction();
    byte[] reply();
    void setNextReceivedDataType();
}
