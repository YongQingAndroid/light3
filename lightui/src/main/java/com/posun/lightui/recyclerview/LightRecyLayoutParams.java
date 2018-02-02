package com.posun.lightui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.lang.reflect.Field;

/**
 * package light3:com.posun.lightui.recyclerview.LightRecyLayoutParams.class
 * 作者：zyq on 2018/2/2 16:02
 * 邮箱：zyq@posun.com
 */

public class LightRecyLayoutParams extends RecyclerView.LayoutParams {
    private Field viewHolder;

    public LightRecyLayoutParams(int width, int height) {
        super(width, height);
    }

    public LightRecyLayoutParams(RecyclerView.ViewHolder view) {
        super(WRAP_CONTENT, WRAP_CONTENT);
        Field field = getViewHolderFiled();
        try {
            field.set(this, view);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public LightGroupRecycler.GroupHolder getGroupViewHolderInt() {
        Field field = getViewHolderFiled();
        try {
            return (LightGroupRecycler.GroupHolder) field.get(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Field getViewHolderFiled() {
        if (viewHolder == null) {
            try {
                viewHolder = getClass().getSuperclass().getDeclaredField("mViewHolder");
                viewHolder.setAccessible(true);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return viewHolder;
    }
}
