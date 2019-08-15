package com.lightpermission.permission.requestresult;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.lightpermission.permission.PermissionUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 类：RequestPermissionsResult
 * 处理权限申请的结果，如果未允许，提示用户,并跳转至设置APP权限页面
 * 作者： zyq
 */
public class RequestPermissionsResultSetApp implements IRequestPermissionsResult {
    private static RequestPermissionsResultSetApp requestPermissionsResult;

    public static RequestPermissionsResultSetApp getInstance() {
        if (requestPermissionsResult == null) {
            requestPermissionsResult = new RequestPermissionsResultSetApp();
        }
        return requestPermissionsResult;
    }

    @Override
    public boolean doRequestPermissionsResult(Activity activity, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean isAllGranted = true;
        // 判断是否所有的权限都已经授予了
        for (int grant : grantResults) {
            if (grant != PackageManager.PERMISSION_GRANTED) {
                isAllGranted = false;
                break;
            }
        }

        //已全部授权
        if (isAllGranted) {
            return true;
        }
        //引导用户去授权
        else {
            List<String> deniedPermission = new ArrayList<>();
            //如果选择了“不再询问”，则弹出“权限指导对话框”
            for (int i = 0; i < permissions.length; i++) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i])) {
                    deniedPermission.add(permissions[i]);
                }
            }
            if (deniedPermission.size() > 0) {
                String name = PermissionUtils.getInstance().getPermissionNames(deniedPermission);
                SetPermissions.openAppDetails(activity, name);
            }
        }
        return false;
    }

    @Override
    public PermissionResult getPermissionResult(Activity activity, @NonNull String[] permissions, @NonNull int[] grantResults) {
        List<String> allow = new ArrayList<>();
        List<String> disAllow = new ArrayList<>();
        for (int index = 0; index < grantResults.length; index++) {
            int grant = grantResults[index];
            if (grant != PackageManager.PERMISSION_GRANTED) {
                disAllow.add(permissions[index]);
            } else {
                allow.add(permissions[index]);
            }
        }
        PermissionResult result = new PermissionResult(allow.toArray(new String[]{}), disAllow.toArray(new String[]{}));
        return result;
    }


}
