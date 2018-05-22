package com.digiarty.phoneassistant.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;



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
class PicturesProvider extends ICommonProvider<String> implements IGetDatasService<String> {

    private Logger logger = LoggerFactory.getLogger(PicturesProvider.class);
    private static List<String> picutres = new ArrayList<>();

    @Override
    public List<String> getDatasFromMobilPhone(Context context, ProviderDataType dataType) {
        logger.debug("开始获取数据，获取的数据类型为：图片");
        picutres = getCommonDatas(context, dataType);
        logger.debug("获取数据完成");
        return  picutres;
    }

    @Override
    protected List<String> getDataFromSystemApplicationsInstalledInExternalStorage(ProviderDataType dataType) {

        List<String> temp = new ArrayList<>();
        // 扫描外部设备中的照片
        String str[] = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, str, null, null, null);
        if (null == cursor){
            logger.debug("从 <外部存储1> 位置的 <系统应用> 中获取图片数据---指针为空");
            return null;
        }
        while (cursor.moveToNext()) {
            temp.add(cursor.getString(2)); // 图片绝对路径
        }
        cursor.close();
        return temp;
    }

    @Override
    protected List<String> getDataFromSystemApplicationsInstalledInSDCard(ProviderDataType dataType) {

        List<String> temp = new ArrayList<>();
        // 扫描外部设备中的照片
//        String str[] = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.INTERNAL_CONTENT_URI, null, null, null, null);
        if (null == cursor){
            logger.debug("从 <外部存储2>  位置的 <系统应用> 中获取图片数据---指针为空");
            return null;
        }
        while (cursor.moveToNext()) {
            temp.add(cursor.getString(2)); // 图片绝对路径
        }
        cursor.close();
        return temp;
    }

    @Override
    protected List<String> getDataFromThirdApplicationsInstalledInExternalStorage(ProviderDataType dataType) {
        return null;
    }

    @Override
    protected List<String> getDataFromThirdApplicationsInstalledInSDCard(ProviderDataType dataType) {
        return null;
    }


}
