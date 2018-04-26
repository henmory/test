package com.digiarty.phoneassistant.global;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/***
 *
 * Created on：26/04/2018
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public class GlobalApplication extends Application {

    private final static String TAG = GlobalApplication.class.getSimpleName();

    private static String globalPackageName;

    @Override
    public void onCreate() {
        super.onCreate();
        getGlobalMessages();
        Log.d(TAG, "onCreate: packageName = " + globalPackageName);
        createLogDirectoryInPublicDocuments();
    }

    private void getGlobalMessages() {
        globalPackageName = getApplicationContext().getPackageName();
    }


    public static String getGlobalPackageName() {
        return globalPackageName;
    }

    private void createLogDirectoryInPublicDocuments(){
        String path = Environment.DIRECTORY_DOWNLOADS + "log/";
        createDir(path);
    }



    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        //创建目录
        if (dir.mkdirs()) {
            System.out.println("创建目录" + destDirName + "成功！");
            return true;
        } else {
            System.out.println("创建目录" + destDirName + "失败！");
            return false;
        }
    }
}
