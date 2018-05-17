package com.digiarty.phoneassistant.global;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.digiarty.phoneassistant.R;
import com.digiarty.phoneassistant.boot.MyService;
import com.digiarty.phoneassistant.net.ServerConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PermissionRequestActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

//    private final static String TAG = PermissionRequestActivity.class.getSimpleName();
    private final static Logger logger = LoggerFactory.getLogger(PermissionRequestActivity.class);
    private final static String PC_PORT = "port";

    /* 联系人请求码 */
    private static final int REQUEST_CONTACTS = 1;

    /* 请求读取联系人权限 */
    private static String[] PERMISSIONS_CONTACT = {Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_permissions);

        if (!parsePCPort(getIntent())){
            logger.debug("解析用户传来的pc 端口失败");
            finish();
            return;
        }

        logger.debug("解析的PC端socket端口为" + ServerConfig.PCConfig.getPort());
        requestAllPermissionsForApplicationRunningNormally();
    }


    public void requestAllPermissionsForApplicationRunningNormally(){
        requestReadAndWriteContactsPermissions();
    }



    public void requestReadAndWriteContactsPermissions(){

        // 判断权限是否拥有
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            logger.debug("读写联系人权限未被授予，需要申请！");

            // 读写联系人权限未被授予，需要申请！
            requestContactsPermissions();
        } else {
            // 权限已经被授予，显示细节页面！

            userGrantAllPermissions();
        }
    }



    /**
     * 申请联系人读取权限
     */
    private void requestContactsPermissions() {
        // 第一次不会运行里面代码，会走else请求权限，如果用户选择拒绝，那么当再次运行这个代码时，会走这里的代码
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CONTACTS)) {
            // 如果是第二次申请，需要向用户说明为何使用此权限，会带出一个不再询问的复选框！
            //再次提醒用户，必须选择允许，否则不能操作
            showWarningIfRefusePermission();
        } else {
            // 第一次申请此权限，直接申请
            ActivityCompat.requestPermissions(this, PERMISSIONS_CONTACT, REQUEST_CONTACTS);
        }
    }



    //用户点击权限允许之后的处理结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CONTACTS) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted.
                logger.debug("onRequestPermissionsResult: 用户授权");
                userGrantAllPermissions();
            } else {
                // Permission request was denied.
                logger.debug("onRequestPermissionsResult: 用户拒绝创建，告诉用户如果不同意，则不能使用app");
                showWarningIfRefusePermission();

            }
        }
    }


    private void showWarningIfRefusePermission() {

        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(PermissionRequestActivity.this);
        normalDialog.setIcon(R.drawable.ic_launcher_background);
        normalDialog.setTitle("警告");
        normalDialog.setCancelable(false);
        normalDialog.setMessage("应用程序的运行，需要相应的权限，如果用户不允许，将导致app不能使用");
        normalDialog.setPositiveButton("去开启权限",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Request the permission
                        ActivityCompat.requestPermissions(PermissionRequestActivity.this, PERMISSIONS_CONTACT, REQUEST_CONTACTS);
                    }
                });
        normalDialog.setNegativeButton("拒绝",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logger.debug( "onClick: 用户再度拒绝授权，退出应用");
                        userRefusePermissions();

                    }
                });
        // 显示
        normalDialog.show();
    }

    private void userGrantAllPermissions(){
        logger.debug("用户授予所有权限，开始开始服务");
        Intent intent = new Intent();
        intent.setAction(MyService.getStartService());
        intent.setPackage(GlobalApplication.getGlobalPackageName());
        startService(intent);
        finish();
    }

    private void userRefusePermissions(){
        finish();
        GlobalApplication.notifyApplicationClose();
    }



    private boolean parsePCPort(Intent intent){

        String port = intent.getStringExtra(PC_PORT);
        if (null == port){
            logger.debug("端口为空");
            return false;
        }
        ServerConfig.PCConfig.setPort(Integer.parseInt(port));
        return true;
    }


}
