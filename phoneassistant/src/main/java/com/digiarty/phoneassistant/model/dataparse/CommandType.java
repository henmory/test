package com.digiarty.phoneassistant.model.dataparse;

/***
 *
 * Created on：2018/5/31
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
@Deprecated
public class CommandType implements IType {

    @Override
    public Object parse(String jsonString) {
        return null;
    }

    @Override
    public int doAction() {
        return 0;
    }

    @Override
    public String reply() {
        return null;
    }

    @Override
    public void setNextReceivedDataType() {

    }
}
