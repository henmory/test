package com.digiarty.phoneassistant.model;

import android.content.Context;
import android.media.Image;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/***
 *
 * Created on：10/05/2018
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public class PicturesProvider {

    private Logger logger = LoggerFactory.getLogger(PicturesProvider.class);
    private CommonProvider<Image> commonProvider =  new CommonProvider<>();
    private static List<Image> picutres = new ArrayList<>();

    public List<Image> getPictres(Context context){
        logger.debug("开始获取数据，获取的数据类型为：图片");
        picutres = commonProvider.getCommonDatas(context, ProviderDataType.PICTURE);
        return  picutres;

    }

}
