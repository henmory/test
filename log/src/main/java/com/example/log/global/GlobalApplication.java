package com.example.log.global;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.log.file.FileHelper;
import com.example.log.log.FileLog;

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
//        createLogDirectoryInPublicDocuments();
        FileLog.createFileOnExternalPrivateStorage(this);

    }

    private void getGlobalMessages() {
        globalPackageName = getApplicationContext().getPackageName();
    }


    public static String getGlobalPackageName() {
        return globalPackageName;
    }






}
