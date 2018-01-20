package com.posun.lightui.richView.view;

import android.content.Context;
import android.view.View;

import com.posun.lightui.richView.LightRichActivityManager;

import java.lang.reflect.Field;

/**
 * package Kotlin3:com.posun.lightui.richView.view.LightTextInputView.class
 * 作者：zyq on 2018/1/17 10:07
 * 邮箱：zyq@posun.com
 */

public class LightTextInputView extends android.support.design.widget.TextInputLayout implements LightRichActivityManager.LightItemIntface {
    private android.support.design.widget.TextInputEditText right;
    private String labeText, value;
    private Field field;

    public LightTextInputView(Context context, String labeText, String value) {
        super(context);
        this.labeText = labeText;
        this.value = value;
        initUi();
    }

    private void initUi() {
        setFocusable(true);
        setFocusableInTouchMode(true);
        setPadding(0, 10, 0, 10);
        right = new android.support.design.widget.TextInputEditText(getContext());
        right.setHint(labeText);
        addView(right, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        right.setText(value);
    }
    @Override
    public void setField(Field field) {
        this.field = field;
    }

    @Override
    public Field getItemField() {
        return field;
    }

    @Override
    public View getMyView() {
        return this;
    }

    @Override
    public void saveValue(Object object) {
        try {
            field.set(object, String.valueOf(right.getText()));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
