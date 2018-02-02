package com.posun.lightui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * package light3:com.posun.lightui.recyclerview.LightChildHelper.class
 * 作者：zyq on 2018/2/2 15:30
 * 邮箱：zyq@posun.com
 */

public class LightChildHelper {
    private Object object;

    public LightChildHelper(RecyclerView.LayoutManager layoutManager) {
        getChildHelper(layoutManager);
    }

    private void getChildHelper(Object object) {
        try {
            String name = "mChildHelper";
            Field field = RecyclerView.LayoutManager.class.getDeclaredField(name);
            field.setAccessible(true);
            this.object = field.get(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addView(View view, boolean hidden) {
        String name = "addView";
        Method method = null;
        try {
            method = Class.forName("android.support.v7.widget.ChildHelper").getDeclaredMethod(name, new Class[]{View.class, boolean.class});
            method.setAccessible(true);
            method.invoke(object, view, hidden);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeView(View view) {
        String name = "removeView";
        Method method = null;
        try {
            method = Class.forName("android.support.v7.widget.ChildHelper").getDeclaredMethod(name, new Class[]{View.class});
            method.setAccessible(true);
            method.invoke(object, view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
