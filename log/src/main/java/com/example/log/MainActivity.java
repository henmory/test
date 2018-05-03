package com.example.log;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.log.file.FileHelper;
import com.example.log.global.GlobalApplication;
import com.example.log.log.FileLog;

import java.io.File;

/**
 * @param
 * @author henmory
 * @date
 * @return
 * @description 文件权限的获取需要在activity中才行，但是我们的软件没有activity
 */

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private final static String TAG = MainActivity.class.getSimpleName();
    private TextView textView;
    //定义日志对象
//    private static Logger logger = LoggerFactory.getLogger(MainActivity.class);
    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        createFileOnExternalStorage();
        FileLog.createFileOnExternalPrivateStorage(this);
        finish();
    }

    private void createFileOnExternalStorage() {

        //检查权限是否授予
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, 开始创建文件
            Log.d(TAG, "createFileOnExternalStorage: 用户有创建文件的权限，直接创建文件");
            createLogDirectoryInPublicDocuments();
            finish();
            //没有授予，请求权限
        } else {
            // Permission is missing and must be requested.
            Log.d(TAG, "createFileOnExternalStorage: 用户没有创建文件的权限，开始请求权限");
            requestWriteExternalStoragePermission();
        }

    }


    private void requestWriteExternalStoragePermission() {

        // 第一次不会运行里面代码，会走else请求权限，如果用户选择拒绝，那么当再次运行这个代码时，会走这里的代码
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //再次提醒用户，必须选择允许，否则不能操作
            showWarningIfRefusePermission();

        } else {
            //请求权限
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }


    //用户点击权限允许之后的处理结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted.
                Log.d(TAG, "onRequestPermissionsResult: 用户授权，可以创建了");
                createLogDirectoryInPublicDocuments();
                finish();
            } else {
                // Permission request was denied.
                Log.d(TAG, "onRequestPermissionsResult: 用户拒绝创建，告诉用户如果不同意，则不能使用app");
                showWarningIfRefusePermission();

            }
        }
    }


    private void showWarningIfRefusePermission() {

        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(MainActivity.this);
        normalDialog.setIcon(R.drawable.ic_launcher_background);
        normalDialog.setTitle("警告");
        normalDialog.setCancelable(false);
        normalDialog.setMessage("应用程序的运行，需要相应的权限，如果用户不允许，将导致app不能使用");
        normalDialog.setPositiveButton("去开启权限",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Request the permission
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
                    }
                });
        normalDialog.setNegativeButton("拒绝",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: 用户再度拒绝授权，退出应用");
                        finish();
                    }
                });
        // 显示
        normalDialog.show();
    }

    private void createLogDirectoryInPublicDocuments() {

        Log.d("MainActivity", "createLogDirectoryInPublicDocuments: 开始创建");
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        String abPath = path.getAbsolutePath() + "/" + GlobalApplication.getGlobalPackageName();
        System.out.println("log path = " + abPath);
        FileHelper.createDir(abPath);

    }
}
