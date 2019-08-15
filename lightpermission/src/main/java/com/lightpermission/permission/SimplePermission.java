package com.lightpermission.permission;

import android.support.v7.app.AppCompatActivity;

public class SimplePermission {
    HookFragment hookFragment;

    public boolean requestPermissions(AppCompatActivity activity, String... permission) {
        return requestPermissions(activity, null, permission);
    }

    public boolean requestPermissions(AppCompatActivity activity, PermissionCallBack permissionCallBack, String... permission) {
        if (hookFragment == null) {
            hookFragment = new HookFragment(activity);
            activity.getSupportFragmentManager().beginTransaction().add(hookFragment, "hookFragment").commit();
        }
        return hookFragment.requestPermissions(permission, permissionCallBack);
    }

}
