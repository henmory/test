package com.digiarty.phoneassistant.presenter;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.digiarty.phoneassistant.activity.IPermissionRequest;
import com.digiarty.phoneassistant.boot.GlobalApplication;
import com.digiarty.phoneassistant.sharedpreference.IPreference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static com.digiarty.phoneassistant.boot.GlobalApplication.PERMISSION_REQUEST_STATE;
import static com.digiarty.phoneassistant.sharedpreference.IPreference.DataType.INTEGER;

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
public class PermissionRequestPresenter {

    private final static Logger logger = LoggerFactory.getLogger(PermissionRequestPresenter.class);

    private IPermissionRequest activity;
    private PermissionManager.PermissionStruct permission;
    private int permissionRequestDontAskState = 0;
    private PermissionRequestCallBack callBack;

    public PermissionRequestPresenter(IPermissionRequest activity) {
        this.activity = activity;
    }


    void requestPermission(PermissionManager.PermissionStruct permission, PermissionRequestCallBack callBack) {
        this.permission = permission;
        this.callBack = callBack;
        getPremissionRequestDontAskStateInSharedPreference();
        // 判断权限是否拥有
        if (activity.activityCheckSelfPermission(permission.permissions)) {
            logger.debug("权限未被授予，需要申请！权限码为 " + permission.REQUEST_CODE);

            //权限未被授予，需要申请！
            notifyActivityrequestPermissions();
        } else {
            // 权限已经被授予，显示细节页面！

            userGrantAllPermissions();
        }
    }


    /**
     * 申请联系人读取权限
     */
    void notifyActivityrequestPermissions() {
        // 第一次不会运行里面代码，会走else请求权限，如果用户选择拒绝，那么当再次运行这个代码时，会走这里的代码
        if (activity.activityShouldShowRequestPermissionRationale(permission.permissions)) {

            // 如果是第二次申请，需要向用户说明为何使用此权限，会带出一个不再询问的复选框！
            //再次提醒用户，必须选择允许，否则不能操作
            activity.activityShowWarningIfRefusePermission(permission.permissions);
        } else {
            if (userDontWantToShowPermissonTip()) {
                return;
            }
            // 第一次申请此权限，直接申请
            activity.activityStartRequestPermissions(permission.permissions, permission.REQUEST_CODE);
        }
    }

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

            if (requestCode == permission.REQUEST_CODE) {
                boolean ret = true;
                for (int i = 0; i < grantResults.length; i++) {
                    ret = (ret && (grantResults[i] == PackageManager.PERMISSION_GRANTED));
                }
                if (ret) {
                    // Permission has been granted.
                    logger.debug("用户授权码为 " + requestCode);
//                    userGrantAllPermissions();
                    callBack.success(requestCode);
                } else {
                    // Permission request was denied.
                    //拒绝，但是没有点击之后一直不显示
                    if (activity.activityShouldShowRequestPermissionRationale(permissions)) {
                        logger.debug("用户拒绝了权限申请，但是还没有点击不再询问");
                        activity.activityShowWarningIfRefusePermission(permissions);
                    } else {
                        setPremissionRequestDontAskStateInSharedPreference();
//                        userRefusePermissions();
                    }
                    callBack.fail(requestCode);

                }
            } else {
                logger.debug("权限请求结果返回了无效的请求码,请求码为 " + requestCode);
            }

    }

    void startRequestPermissions(String[] permissions) {

            if (Arrays.equals(permissions, permission.permissions)) {
                activity.activityStartRequestPermissions(permission.permissions, permission.REQUEST_CODE);
            } else {
                logger.debug("申请权限未知");
            }

    }

    void userGrantAllPermissions() {
        activity.userGrantAllPermissions();
    }

    void userRefusePermissions() {
        logger.debug("用户拒绝所有权限");
        activity.userRefusePermissions();
        GlobalApplication.notifyApplicationClose();
    }

    private void setPremissionRequestDontAskStateInSharedPreference() {
        IPreference.IPreferenceHolder holder = IPreference.prefHolder;
        IPreference preference = holder.getPreference(GlobalApplication.getContext(), GlobalApplication.SHARED_PREFERENCE);
        preference.put(PERMISSION_REQUEST_STATE, 1);
    }

    private void getPremissionRequestDontAskStateInSharedPreference() {
        IPreference.IPreferenceHolder holder = IPreference.prefHolder;
        IPreference preference = holder.getPreference(GlobalApplication.getContext(), GlobalApplication.SHARED_PREFERENCE);
        permissionRequestDontAskState = preference.get(PERMISSION_REQUEST_STATE, INTEGER);
    }

    //外面在进入activity之前判断一下
    public boolean userDontWantToShowPermissonTip() {
        if (permissionRequestDontAskState == 1) {
            logger.debug("用户已经不再要求提醒");
            userRefusePermissions();
            return true;
        }
        logger.debug("用户还没有点击不再询问");
        return false;
    }
    interface PermissionRequestCallBack{
        void fail(int requestCode);
        void success(int requestCode);
    }


}
