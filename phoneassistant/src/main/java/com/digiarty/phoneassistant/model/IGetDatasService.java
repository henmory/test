package com.digiarty.phoneassistant.model;

import android.content.Context;

import java.util.List;

/***
 *
 * Created on：2018/5/17
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public interface IGetDatasService<T> {
    List<T> getDatasFromMobilePhone(Context context, ProviderDataType dataType);
}
