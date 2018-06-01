package com.digiarty.phoneassistant.model.net;

import java.util.Arrays;

/***
 *
 * Created on：2018/5/31
 *
 * Created by：henmory
 *
 * Description: 短连接，不同用户通过不同的数据解析器，但是最后要返回同样的结构
 *
 *
 **/
public class Reply {
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
