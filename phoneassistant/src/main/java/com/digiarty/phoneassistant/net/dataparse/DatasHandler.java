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
public class DatasHandler implements IHandler{
    @Override
    public int parse(byte[] datas) {
        return 1;
    }

    @Override
    public int doAction() {
        return 1;
    }

    @Override
    public byte[] reply() {
        return new byte[0];
    }

    @Override
    public void setNextReceivedDataType() {

    }
}
