package com.digiarty.phoneassistant.log;

import android.content.Context;

import com.digiarty.phoneassistant.file.Storage;

import java.io.File;

/***
 *
 * Created on：03/05/2018
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public class FileLog {

    private static final String LOG_FILE_DIR = "log";

    public static void createLogFileDirOnExternalPrivateStorage(Context context){
        File path = Storage.ExternalStorage.createDirInFilesDir(context, null, LOG_FILE_DIR);
        if (null != path){
            String abPath = path.getAbsolutePath();
            System.out.println("log path = " + abPath);
        }else{
            System.out.println("日志文件夹没有找到");
        }
        System.out.println("日志文件夹 " + path + " 创建成功");
    }
}
