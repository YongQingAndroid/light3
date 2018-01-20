package com.posun.lightui.richView.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.posun.lightui.richView.LightRichActivityManager;

import java.lang.reflect.Field;

public class LightCheckGroup extends LinearLayout implements LightRichActivityManager.LightItemIntface {
    private String labeText, value;
    private android.support.v7.widget.AppCompatTextView leftView;
    private android.support.v7.widget.AppCompatCheckBox rightView;
    private Field field;

    public LightCheckGroup(Context context, String labeText, String value) {
        super(context);
        this.labeText = labeText;
        this.value = value;
        initUI();
    }

    public void setField(Field field) {
        this.field = field;
    }

    private void initUI() {
        setOrientation(HORIZONTAL);
        LayoutParams layoutParams = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
        leftView = new android.support.v7.widget.AppCompatTextView(getContext());
        leftView.setText(labeText);
        addView(leftView, layoutParams);
        rightView = new android.support.v7.widget.AppCompatCheckBox(getContext());
        rightView.setText(value);
        addView(rightView);
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
            field.set(object,String.valueOf(rightView.isChecked()));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}