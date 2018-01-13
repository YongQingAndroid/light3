package com.posun.lightui.layout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * package Kotlin3:com.posun.lightui.layout.PickerView.class
 * 作者：zyq on 2017/11/15 15:26
 * 邮箱：zyq@posun.com
 */

public class PickerView extends LinearLayout {
    public PickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public PickerView(Context context) {
        super(context);
        initUi();
    }

    private void initUi() {
        setOrientation(VERTICAL);

    }
}
