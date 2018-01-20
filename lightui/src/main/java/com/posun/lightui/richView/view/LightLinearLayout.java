package com.posun.lightui.richView.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * package Kotlin3:com.posun.lightui.richView.view.LightLinearLayout.class
 * 作者：zyq on 2018/1/17 14:03
 * 邮箱：zyq@posun.com
 */

public class LightLinearLayout extends LinearLayout{
    boolean isEditor=true;
    public LightLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    private void initUI() {
    }

    public LightLinearLayout(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(isEditor)
            return super.onInterceptTouchEvent(ev);
        return true;
    }
}
