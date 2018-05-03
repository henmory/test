package com.example.log.log;

import android.content.Context;

import com.example.log.file.Storage;
import com.example.log.global.GlobalApplication;

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

    private static final String LOG_FILE_NAME = "log.txt";
    public static void createFileOnExternalPrivateStorage(Context context){
        File path = Storage.ExternalStorage.createDirInFilesDir(context, null, LOG_FILE_NAME);
        if (null != path){
            String abPath = path.getAbsolutePath();
            System.out.println("log path = " + abPath);
        }else{
            System.out.println("日志文件夹没有找到");
        }
    }
}
