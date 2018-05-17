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
public interface IGetDatasFromMobilePhoneService<T> {
    List<T> getDatasFromMobilPhone(Context context, ProviderDataType dataType);
}
