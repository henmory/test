package com.digiarty.phoneassistant.model;

import android.content.Context;

import com.digiarty.phoneassistant.file.AndroidStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/***
 *
 * Created on：11/05/2018
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public abstract class ICommonProvider<T> {

    private Logger logger = LoggerFactory.getLogger(ICommonProvider.class);
    protected int externalNumbers = -1;
    protected List<T> datas = new ArrayList<>();
    protected Context context;


    public List<T> getCommonDatas(Context context, ProviderDataType dataType){
        this.context = context;

        externalNumbers = getExternalStorageNumbers(context);
        logger.debug("获取外部存储的数量为 " + externalNumbers);
        if (0 == externalNumbers){
            logger.debug("外部存储数量为0，说明外部存储没有挂载到手机上，或者已经被电脑挂载，目前不能使用");
            return null;
        }

        logger.debug("从系统程序中获取数据开始-------");
        getDataFromSystemApplications(dataType);
        logger.debug("从系统程序中获取数据结束-------");
        logger.debug("获取的数据大小为" + datas.size());
        logger.debug("从第三方程序中获取数据开始-------");
        getDataFromThirdApplications(dataType);
        logger.debug("从第三方程序中获取数据结束--------");

        return  datas;

    }


    private int getExternalStorageNumbers(Context context){
        return AndroidStorage.getExternalStorageNumbers(context);
    }

    private void getDataFromSystemApplications(ProviderDataType dataType) {

        List<T> data = getDataFromSystemApplicationsInstalledInExternalStorage(dataType);
        if (null != data){
            datas.addAll(data);
        }else{
            logger.debug("从 <外部存储1> 的 <系统应用> 中获取数据为空");
        }

        if (2 == externalNumbers){
            data = getDataFromSystemApplicationsInstalledInSDCard(dataType);
            if (null != data){
                datas.addAll(data);
            }else{
                logger.debug("从 <外部存储2> 的 <系统应用> 中获取数据为空");
            }
        }
    }



    private void getDataFromThirdApplications(ProviderDataType dataType){

        List<T> data = getDataFromThirdApplicationsInstalledInExternalStorage(dataType);
        if (null != data){
            datas.addAll(data);
        }else{
            logger.debug("从 <外部存储1> 的 <三方应用> 中获取数据为空");
        }
        if (2 == externalNumbers){
            data = getDataFromThirdApplicationsInstalledInSDCard(dataType);
            if (null != data){
                datas.addAll(data);
            }else{
                logger.debug("从 <外部存储1> 的 <三方应用> 中获取数据为空");
            }
        }
    }

    protected abstract List<T> getDataFromSystemApplicationsInstalledInExternalStorage(ProviderDataType dataType);
    protected abstract List<T> getDataFromSystemApplicationsInstalledInSDCard(ProviderDataType dataType);
    protected abstract List<T> getDataFromThirdApplicationsInstalledInExternalStorage(ProviderDataType dataType);
    protected abstract List<T> getDataFromThirdApplicationsInstalledInSDCard(ProviderDataType dataType);

}
