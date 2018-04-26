package com.example.log.global;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.example.log.file.FileHelper;

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

        File path = Environment.getExternalStorageDirectory();
        String abPath = path.getAbsolutePath() + "/" + globalPackageName;
        System.out.println("log path = " + abPath);
        FileHelper.createDir(abPath);
    }



//    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
//
//
//    public void getPermission(Context context, String permission) {
//
//        if (Build.VERSION.SDK_INT >= 23) {
//
//            int hasPermission = ContextCompat.checkSelfPermission(context, permission);
//
//            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
//
//                //这里就会弹出对话框
//                ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.READ_CONTACTS}, 123);
//
//            } else {
//                FileHelper.createDir(paht)
//
//            }
//        } else {
//            createLogDirectoryInPublicDocuments();
//
//        }
//    }
//
//
//    //权限设置后的回调函数，判断相应设置，requestPermissions传入的参数为几个权限，则permissions和grantResults为对应权限和设置结果
//
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//
//        switch (requestCode) {
//            case 123: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // permission was granted, yay! Do the
//                    createLogDirectoryInPublicDocuments();
//
//                } else {
//
//                    // permission denied, boo! Disable the
//                    Log.d(TAG, "onRequestPermissionsResult: 用户拒绝授予权限，不能新建文件");
//                }
//            }
//
//        }
//    }
//

}
