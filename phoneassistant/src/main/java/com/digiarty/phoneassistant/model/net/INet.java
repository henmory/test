package com.digiarty.phoneassistant.model.net;

/***
 *
 * Created on：2018/5/21
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
//todo 以后再抽象成接口吧
    @Deprecated
public interface INet {
    INet getNetManager();
    ListenPCConnectionTask newTaskToListenPCConnection();
    void newTaskToSendAndroidServerPortForPCToForward(int serverPort);
    void newTaskToCommunicateWithPC(String port);
}
