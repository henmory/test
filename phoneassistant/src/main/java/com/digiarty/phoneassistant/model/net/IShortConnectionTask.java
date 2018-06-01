package com.digiarty.phoneassistant.model.net;


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
public interface IShortConnectionTask {
    /**
     * @date
     *
     * @author henmory
     *
     * @param   type ：数据类型
     * @param   datas:数据内容
     * @return  Reply 返回的数据，以及下次希望得到什么数据
     *
     *
     * @description
     *
     */
    Reply doActionAndPrepareDatasToPC(NetDataType type, byte[] datas);
}
