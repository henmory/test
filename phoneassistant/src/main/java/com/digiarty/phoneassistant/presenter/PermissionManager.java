package com.digiarty.phoneassistant.presenter;

import android.Manifest;

import com.digiarty.phoneassistant.activity.IPermissionRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.digiarty.phoneassistant.presenter.PermissionManager.PermissionParams.contactPermission;
import static com.digiarty.phoneassistant.presenter.PermissionManager.PermissionParams.smsPermission;


/***
 *
 * Created on：2018/5/23
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public class PermissionManager implements PermissionRequestPresenter.PermissionRequestCallBack {
    private final static Logger logger = LoggerFactory.getLogger(PermissionManager.class);

    private PermissionRequestPresenter presenter;
    private List<PermissionStruct> permissionList = new ArrayList<>();

    public PermissionManager(IPermissionRequest activity) {
        presenter = new PermissionRequestPresenter(activity);
    }

    public void requestPermissions(List<PermissionType> types) {
        initAllPermissionParams(types);
        requestPermission(permissionList.get(0));
    }

    private void initAllPermissionParams(List<PermissionType> types) {
        for (int i = 0; i < types.size(); i++) {
            switch (types.get(i)) {
                case CONTACT:
                    permissionList.add(contactPermission);
                    break;
                case SMS:
                    permissionList.add(smsPermission);
                    break;
                default:
                    break;
            }
        }

    }
    private void requestPermission(PermissionStruct permissionStruct) {
        presenter.requestPermission(permissionStruct, this);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void startRequestPermissions(String[] permissions) {
        presenter.startRequestPermissions(permissions);
    }

    public void userRefusePermissions() {
        presenter.userRefusePermissions();
    }

    @Override
    public void fail(int requestCode) {
        logger.debug("有权限申请失败，结束后面操作");
        presenter.userRefusePermissions();
    }

    @Override
    public void success(int requestCode) {
        for (int i = 0; i < permissionList.size(); i++){
            if (requestCode ==  permissionList.get(i).REQUEST_CODE){
                permissionList.remove(i);
            }
        }
        if (permissionList.size() > 0){
            requestPermission(permissionList.get(0));
        }else{
            logger.debug("所有权限请求完成");
            presenter.userGrantAllPermissions();
        }
    }

    public enum PermissionType {
        CONTACT,
        SMS,
        PICTURE,
        STOREAGE
    }

    static class PermissionParams {

        private static final int REQUEST_CODE = 0; /* 联系人请求码 */

        final static PermissionStruct contactPermission = new PermissionStruct(REQUEST_CODE + 1, new String[]{
                Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, PermissionType.CONTACT);

        final static PermissionStruct smsPermission = new PermissionStruct(REQUEST_CODE + 2, new String[]{Manifest.permission.READ_SMS}, PermissionType.SMS);
    }


    static class PermissionStruct {

        public int REQUEST_CODE; /* 联系人请求码 */
        public String[] permissions;
        PermissionType type;

        public PermissionStruct(int REQUEST_CODE, String[] permissions, PermissionType type) {
            this.REQUEST_CODE = REQUEST_CODE;
            this.permissions = permissions;
            this.type = type;
        }
    }


}
