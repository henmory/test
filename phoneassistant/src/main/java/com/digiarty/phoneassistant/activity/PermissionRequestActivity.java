package com.digiarty.phoneassistant.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.digiarty.phoneassistant.R;
import com.digiarty.phoneassistant.boot.GlobalApplication;
import com.digiarty.phoneassistant.boot.MyIntentService;
import com.digiarty.phoneassistant.net.ServerConfig;
import com.digiarty.phoneassistant.presenter.PermissionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class PermissionRequestActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, IPermissionRequest {

    private final static Logger logger = LoggerFactory.getLogger(PermissionRequestActivity.class);
    private PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_permissions);

        if (!ServerConfig.ADBDConfig.parseADBDListenPort(getIntent())) {
            logger.debug("解析用户传来的pc 端口失败");
            finish();
            return;
        }

        logger.debug("解析的PC端socket端口为" + ServerConfig.ADBDConfig.getADBDPort());
        addAndRequestPermissions();

    }

    private void addAndRequestPermissions(){
        permissionManager = new PermissionManager(this);
        List<PermissionManager.PermissionType> types =  new ArrayList<>();
        types.add(PermissionManager.PermissionType.CONTACT);
        types.add(PermissionManager.PermissionType.SMS);
        permissionManager.requestPermissions(types);
    }

    //同一组的任何一个权限被授权了，其他权限也自动被授权。
    @Override
    public boolean activityCheckSelfPermission(String[] permissions) {

        // 判断权限是否拥有
        boolean ret = false;
        for (int i = 0; i < permissions.length; i++) {
            ret = (ret || (ActivityCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED));
        }
        return ret;
    }

    @Override
    public boolean activityShouldShowRequestPermissionRationale(String[] permissions) {
        // 第一次不会运行里面代码，会走else请求权限，如果用户选择拒绝，那么当再次运行这个代码时，会走这里的代码
        boolean ret = false;
        for (int i = 0; i < permissions.length; i++) {
            ret = (ret || (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])));
        }
        return ret;

    }


    public void activityStartRequestPermissions(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    //用户点击权限允许之后的处理结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


    @Override
    public void activityShowWarningIfRefusePermission(final String[] permissions) {

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
                        permissionManager.startRequestPermissions(permissions);

                    }
                });
        normalDialog.setNegativeButton("拒绝",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logger.debug("用户再度拒绝授权，退出应用");
                        permissionManager.userRefusePermissions();

                    }
                });
        // 显示
        normalDialog.show();
    }


    @Override
    public void userGrantAllPermissions() {
        Intent intent = new Intent();
        intent.setAction(MyIntentService.getStartService());
        intent.setPackage(GlobalApplication.getGlobalPackageName());
        startService(intent);
        finish();
    }

    @Override
    public void userRefusePermissions(){
        finish();
    }

}
