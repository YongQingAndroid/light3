package com.lightpermission.permission;

import android.support.v7.app.AppCompatActivity;

public class SimplePermission {
    HookFragment hookFragment;

    public void requestPermissions(AppCompatActivity activity, String... permission) {
        if (hookFragment == null) {
            hookFragment = new HookFragment(activity);
            activity.getSupportFragmentManager().beginTransaction().add(hookFragment, "hookFragment").commit();
        }
        hookFragment.requestPermissions(permission);
    }

}
