package com.digiarty.phoneassistant.model.dataprovider;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

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
public class AudioProvider extends CommonProvider<String>{
    private Logger logger = LoggerFactory.getLogger(AudioProvider.class);

    public AudioProvider(Context context) {
        super(context);
    }

    @Override
    protected List<String> getDatasFromEmbeddedSystemApplications() {
        getAudiosFromMediaStoreInInternalStorage();
        getAudiosFromMediaStoreInSDCard();
        return null;
    }

    @Override
    protected List<String> getDatasFromThirdApplicationsInstalledInExternalStorage() {
        return null;
    }

    @Override
    protected List<String> getDatasFromThirdApplicationsInstalledInSDCard() {
        return null;
    }

    private List<String> getAudiosFromMediaStoreInInternalStorage() {

        // 扫描外部设备中的照片
        String str[] = {MediaStore.Audio.Media.DATA};
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, str, null, null, null);
        if (null == cursor){
            logger.debug("从 内部存储的 MediaStore 中获取音频数据---指针为空");
            return null;
        }
        while (cursor.moveToNext()) {
            datas.add(cursor.getString(0)); // 图片绝对路径
        }
        cursor.close();
        return datas;
    }

    private List<String> getAudiosFromMediaStoreInSDCard() {

        // 扫描外部设备中的照片
        String str[] = {MediaStore.Audio.Media.DATA};
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, str, null, null, null);
        if (null == cursor){
            logger.debug("从 外部存储的  MediaStore 中获取音频数据---指针为空");
            return null;
        }
        while (cursor.moveToNext()) {
            datas.add(cursor.getString(0)); // 图片绝对路径
        }
        cursor.close();
        return datas;
    }
}
