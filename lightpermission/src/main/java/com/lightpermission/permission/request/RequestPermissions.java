package com.lightpermission.permission.request;

import android.app.Activity;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import java.util.List;

/**
 * 类：RequestPermissions
 * 作者： zyq
 */
public class RequestPermissions implements IRequestPermissions {
    private static RequestPermissions requestPermissions;

    public static RequestPermissions getInstance() {
        if (requestPermissions == null) {
            requestPermissions = new RequestPermissions();
        }
        return requestPermissions;
    }

    @Override
    public boolean requestPermissions(Activity activity, String[] permissions, int resultCode) {
        //判断手机版本是否23以下，如果是，不需要使用动态权限
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }

        //判断并请求权限
        return requestNeedPermission(activity, permissions, resultCode);
    }

    @Override
    public boolean requestPermissions(Fragment fragment, Activity activity, String[] permissions, int resultCode) {
        //判断手机版本是否23以下，如果是，不需要使用动态权限
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }

        //判断并请求权限
        return requestNeedPermission(fragment, activity, permissions, resultCode);
    }
    @Override
    public boolean requestAllPermission(Activity activity, String[] permissions) {
        //判断是否已赋予了全部权限
        boolean isAllGranted = CheckPermission.checkPermissionAllGranted(activity, permissions);
        if (isAllGranted) {
            return true;
        }
        return false;
    }

    private boolean requestNeedPermission(Fragment fragment, Activity activity, String[] permissions, int resultCode) {
        List<String> list = CheckPermission.checkPermissionDenied(activity, permissions);
        if (list.size() == 0) {
            return true;
        }

        //请求权限
        String[] deniedPermissions = list.toArray(new String[list.size()]);
        fragment.requestPermissions(deniedPermissions, resultCode);
        return false;
    }

    private boolean requestNeedPermission(Activity activity, String[] permissions, int resultCode) {
        List<String> list = CheckPermission.checkPermissionDenied(activity, permissions);
        if (list.size() == 0) {
            return true;
        }

        //请求权限
        String[] deniedPermissions = list.toArray(new String[list.size()]);
        ActivityCompat.requestPermissions(activity, deniedPermissions, resultCode);
        return false;
    }
}
