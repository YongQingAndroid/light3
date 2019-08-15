package com.lightpermission.permission;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lightpermission.permission.request.IRequestPermissions;
import com.lightpermission.permission.request.RequestPermissions;
import com.lightpermission.permission.requestresult.IRequestPermissionsResult;
import com.lightpermission.permission.requestresult.RequestPermissionsResultSetApp;

import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;


@SuppressLint("ValidFragment")
public class HookFragment extends android.support.v4.app.Fragment {
    View rootView;
    Activity mactivity;
    IRequestPermissions requestPermissions = RequestPermissions.getInstance();//动态权限请求
    IRequestPermissionsResult requestPermissionsResult = RequestPermissionsResultSetApp.getInstance();//动态权限请求结果处理
    boolean ready = false;
    LinkedHashMap<String, RequestItem> permissMap = new LinkedHashMap<>();

    @SuppressLint("ValidFragment")
    HookFragment(Activity activity) {
        this.mactivity = activity;
    }

    public Activity getThisActivity() {
        if (getActivity() == null) {
            return mactivity;
        }
        return getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = new LinearLayout(mactivity);
        return rootView;
    }

    //请求权限
    public boolean requestPermissions(String[] permissions, PermissionCallBack permissionCallBack) {
        boolean flag = requestPermissions.requestAllPermission(getThisActivity(), permissions);
        if (!flag) {
            RequestItem item = new RequestItem(permissionCallBack, permissions);
            permissMap.put(item.getCallback().toString(), item);
            executeTask();
        }
        return flag;
    }

    //请求权限
    private boolean realRequestPermissions(RequestItem requestItem) {
        //开始请求权限
        return requestPermissions.requestPermissions(
                this,
                getThisActivity(),
                requestItem
        );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ready = true;
        executeTask();
    }

    public void executeTask() {
        if (ready && permissMap.size() > 0) {
            String mapKey = permissMap.keySet().iterator().next();
            realRequestPermissions(permissMap.get(mapKey));

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RequestItem requestItem = permissMap.get(String.valueOf(requestCode));
        if (requestItem.getCallback() != null) {
            requestItem.getCallback().over(requestPermissionsResult.getPermissionResult(getThisActivity(), permissions, grantResults));
        }
        permissMap.remove(String.valueOf(requestCode));
        executeTask();
    }

    public static class RequestItem {
        WeakReference<PermissionCallBack> callback;
        String[] permissions;
        int mResultCode;

        RequestItem(PermissionCallBack callback, String[] permissions) {
            this.permissions = permissions;
            this.callback = new WeakReference<>(callback);
            mResultCode = PermissionUtils.getResultCode();
        }

        public PermissionCallBack getCallback() {
            if (callback == null)
                return null;
            return callback.get();
        }

        public String[] getPermissions() {
            return permissions;
        }

        public int getmResultCode() {
            return mResultCode;
        }
    }
}
