package com.posun.lightui.richView.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * package Kotlin3:com.posun.lightui.richView.view.LightLinearLayout.class
 * 作者：zyq on 2018/1/17 14:03
 * 邮箱：zyq@posun.com
 */

public class LightRecyclerView extends RecyclerView {
    boolean isEditor = true;

    public LightRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public LightRecyclerView(Context context) {
        super(context);
        initUI();
    }

    private void initUI() {
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isEditor)
            return super.onInterceptTouchEvent(ev);
        return true;
    }
}
