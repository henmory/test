package com.digiarty.phoneassistant.model.net;

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
interface ITask extends Runnable {
    void closeCurrentTask();
}
