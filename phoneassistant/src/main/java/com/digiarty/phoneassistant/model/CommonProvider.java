package com.digiarty.phoneassistant.model;

import android.content.Context;

import com.digiarty.phoneassistant.file.AndroidStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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
public class CommonProvider<T> {

    private Logger logger = LoggerFactory.getLogger(CommonProvider.class);
    private  int externalNumbers = -1;
    private List<T> datas = new ArrayList<>();


    public List<T> getCommonDatas(Context context, ProviderDataType dataType){

        externalNumbers = getExternalStorageNumbers(context);
        logger.debug("获取外部存储的数量为 " + externalNumbers);
        if (0 == externalNumbers){
            logger.debug("外部存储数量为0，说明外部存储没有挂载到手机上，或者已经被电脑挂载，目前不能使用");
            return null;
        }

        getDataFromSystemApplications(dataType);
        getDataFromThirdApplications(dataType);

        return  datas;

    }


    private int getExternalStorageNumbers(Context context){
        return AndroidStorage.getExternalStorageNumbers(context);
    }

    private void getDataFromSystemApplications(ProviderDataType dataType) {

        getDataFromSystemApplicationsInstalledInExternalStorage(dataType);
        logger.debug("从外部存储1的系统应用中获取数据完成");
        if (2 == externalNumbers){
            getDataFromSystemApplicationsInstalledInSDCard(dataType);
            logger.debug("从外部存储2的系统应用中获取数据完成");
        }
    }



    private void getDataFromThirdApplications(ProviderDataType dataType){
        getDataFromThirdApplicationsInstalledInExternalStorage(dataType);
        logger.debug("从外部存储1的三方应用中获取数据完成");
        if (2 == externalNumbers){
            getDataFromThirdApplicationsInstalledInSDCard(dataType);
            logger.debug("从外部存储2的三方应用中获取数据完成");
        }
    }

    private void getDataFromSystemApplicationsInstalledInExternalStorage(ProviderDataType dataType) {

    }

    private void getDataFromSystemApplicationsInstalledInSDCard(ProviderDataType dataType){

    }

    private void getDataFromThirdApplicationsInstalledInExternalStorage(ProviderDataType dataType) {
    }

    private void getDataFromThirdApplicationsInstalledInSDCard(ProviderDataType dataType){

    }
}
