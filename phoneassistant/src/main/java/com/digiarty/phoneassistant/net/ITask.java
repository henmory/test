package com.digiarty.phoneassistant.net;

/***
 *
 * Created on：04/05/2018
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public interface ITask extends Runnable {
    void closeCurrentTask();
}
