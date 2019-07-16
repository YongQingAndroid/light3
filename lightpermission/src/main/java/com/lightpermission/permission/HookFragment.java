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

import java.util.LinkedHashMap;
import java.util.UUID;


@SuppressLint("ValidFragment")
public class HookFragment extends android.support.v4.app.Fragment {
    View rootView;
    Activity mactivity;
    IRequestPermissions requestPermissions = RequestPermissions.getInstance();//动态权限请求
    IRequestPermissionsResult requestPermissionsResult = RequestPermissionsResultSetApp.getInstance();//动态权限请求结果处理
    boolean ready = false;
    String mapKey = "";
    LinkedHashMap<String, String[]> permissMap = new LinkedHashMap<>();

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
    public void requestPermissions(String[] permissions) {
        if (ready && permissMap.size() == 0) {
            realRequestPermissions(permissions);
        } else {
            permissMap.put(UUID.randomUUID().toString(), permissions);
            executeTask();
        }
    }

    //请求权限
    private void realRequestPermissions(String[] permissions) {
        //开始请求权限
        requestPermissions.requestPermissions(
                this,
                getThisActivity(),
                permissions,
                PermissionUtils.ResultCode1);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ready = true;
        executeTask();
    }

    public void executeTask() {
        if (ready && permissMap.size() > 0) {
            mapKey = permissMap.keySet().iterator().next();
            realRequestPermissions(permissMap.get(mapKey));
            permissMap.remove(mapKey);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //用户给APP授权的结果
        //判断grantResults是否已全部授权，如果是，执行相应操作，如果否，提醒开启权限
        if (requestPermissionsResult.doRequestPermissionsResult(getThisActivity(), permissions, grantResults)) {
            //请求的权限全部授权成功，此处可以做自己想做的事了
            //输出授权结果
            Toast.makeText(getThisActivity(), "授权成功，请重新点击刚才的操作！", Toast.LENGTH_LONG).show();
        } else {
            //输出授权结果
            Toast.makeText(getThisActivity(), "请给APP授权，否则功能无法正常使用！", Toast.LENGTH_LONG).show();
        }
        executeTask();
    }
}
