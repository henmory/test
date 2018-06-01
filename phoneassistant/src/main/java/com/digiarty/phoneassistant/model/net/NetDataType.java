package com.digiarty.phoneassistant.model.net;

/***
 *
 * Created on：2018/5/25
 *
 * Created by：henmory
 *
 * Description: 获取数据的类型
 *              FILE： 文件类型
 *              COMMAND： 字符串，基本上一次读就可以完成的
 *              JSONOBJECT：需要多次读才能读取完成的数据
 *
 *
 **/
public enum  NetDataType {
    FILE,
    COMMAND,
    JSONOBJECT
}
