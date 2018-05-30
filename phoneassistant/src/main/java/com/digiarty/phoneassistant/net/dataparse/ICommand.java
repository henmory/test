package com.digiarty.phoneassistant.net.dataparse;

import com.digiarty.phoneassistant.net.NetDataType;

import org.json.JSONException;
import org.json.JSONObject;

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
public interface ICommand {
    int parse(String jsonString);
    int doAction();
    String reply();
    void setNextReceivedDataType();
}
