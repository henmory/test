package com.digiarty.phoneassistant.model.dataprovider;

import android.content.Context;


import com.digiarty.phoneassistant.bean.ContactBean;
import com.digiarty.phoneassistant.model.dataparse.ContactAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/***
 *
 * Created on：2018/5/22
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public class ModelManager<T>  {
    private static Logger logger = LoggerFactory.getLogger(ModelManager.class);

    private CommonProvider<T> commonProvider;
    private List<T> datas;

    private static class SingletonHolder {
        private static final ModelManager INSTANCE = new ModelManager();
    }
    public static ModelManager getInstance() {
        return ModelManager.SingletonHolder.INSTANCE;
    }

    public List<T> getDatas(Context context, ProviderDataType type){

        switch (type){
            case AUDIO:
                logger.debug("开始获取数据，获取的数据类型为：AUDIO");
                commonProvider = (CommonProvider<T>) new AudioProvider(context);
                break;
            case MUSIC:
                logger.debug("开始获取数据，获取的数据类型为：MUSIC");
                break;
            case VIDEO:
                logger.debug("开始获取数据，获取的数据类型为：VIDEO");
                break;
            case PICTURE:
                logger.debug("开始获取数据，获取的数据类型为：PICTURE");
                commonProvider = (CommonProvider<T>) new PicturesProvider(context);
                break;
            case CONTACT:
                logger.debug("开始获取数据，获取的数据类型为：CONTACT");
                return datas = (List<T>) new ContactsProvider(context).getContacts();
            default:
                logger.debug("获取数据类型未知");
                break;
        }
        datas = commonProvider.getCommonDatas();
        logger.debug("获取数据完成");
        return datas;
    }

    public List<T> insertDatas(Context context, ProviderDataType type, T t){

        switch (type){
            case AUDIO:
                logger.debug("开始获取数据，获取的数据类型为：AUDIO");
                commonProvider = (CommonProvider<T>) new AudioProvider(context);
                break;
            case MUSIC:
                logger.debug("开始获取数据，获取的数据类型为：MUSIC");
                break;
            case VIDEO:
                logger.debug("开始获取数据，获取的数据类型为：VIDEO");
                break;
            case PICTURE:
                logger.debug("开始获取数据，获取的数据类型为：PICTURE");
                commonProvider = (CommonProvider<T>) new PicturesProvider(context);
                break;
            case CONTACT:
                logger.debug("开始插入数据，获取的数据类型为：CONTACT");
                return (List<T>) new ContactsProvider(context).insertData(context, (List<ContactAction.ContactBeanWrap>) t);
            default:
                logger.debug("获取数据类型未知");
                break;
        }
        datas = commonProvider.getCommonDatas();
        logger.debug("获取数据完成");
        return datas;
    }
}
