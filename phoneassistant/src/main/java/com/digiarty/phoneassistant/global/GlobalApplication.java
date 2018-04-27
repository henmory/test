package com.digiarty.phoneassistant.global;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.digiarty.phoneassistant.file.FileHelper;

import java.io.File;

/***
 *
 * Created on：26/04/2018
 *
 * Created by：henmory
 *
 * Description:
 *
 * 注意:这里还不能使用logger因为需要先初始化文件之后，才可以用
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
        createLogDirectoryInMyApplicationExternalDirectory();
    }

    private void getGlobalMessages() {
        globalPackageName = getApplicationContext().getPackageName();
    }


    public static String getGlobalPackageName() {
        return globalPackageName;
    }

    private void createLogDirectoryInMyApplicationExternalDirectory(){
        File path = Environment.getExternalStorageDirectory();
        String abPath = path.getAbsolutePath() + "/" + globalPackageName;
        System.out.println("log path = " + abPath);
        FileHelper.createDir(abPath);
    }

}
