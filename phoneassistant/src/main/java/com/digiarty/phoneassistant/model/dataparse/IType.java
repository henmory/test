package com.digiarty.phoneassistant.model.dataparse;

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
@Deprecated
public interface IType {
    <T> T parse(String jsonString);
    int doAction();
    String reply();
    void setNextReceivedDataType();
}
