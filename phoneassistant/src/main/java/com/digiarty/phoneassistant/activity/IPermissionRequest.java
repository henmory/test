package com.digiarty.phoneassistant.activity;

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
public interface IPermissionRequest {

    boolean activityCheckSelfPermission(String[] permission);
    boolean activityShouldShowRequestPermissionRationale(String[] permissions);
    void activityStartRequestPermissions(String[] permissions, int requestCode);
    void activityShowWarningIfRefusePermission(String[] permissions);
    void userGrantAllPermissions();
    void userRefusePermissions();

}
