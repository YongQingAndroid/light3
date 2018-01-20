package com.posun.lightui.richView.view;

import android.content.Context;
import android.view.View;

import com.posun.lightui.richView.LightRichActivityManager;

import java.lang.reflect.Field;

/**
 * package Kotlin3:com.posun.lightui.richView.lightBtnItemView.class
 * 作者：zyq on 2018/1/15 10:28
 * 邮箱：zyq@posun.com
 */

public class LightBtnItemView extends android.support.v7.widget.AppCompatButton implements LightRichActivityManager.LightItemIntface {

    public LightBtnItemView(Context context, String lab) {
        super(context);
        setText(lab);
    }

    @Override
    public Field getItemField() {
        return null;
    }

    @Override
    public View getMyView() {
        return this;
    }

    @Override
    public void saveValue(Object object) {

    }

    @Override
    public void setField(Field field) {

    }
}
