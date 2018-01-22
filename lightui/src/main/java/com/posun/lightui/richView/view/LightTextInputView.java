package com.posun.lightui.richView.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.posun.lightui.QlightUnit;
import com.posun.lightui.richView.LightRichActivityManager;
import com.posun.lightui.richView.drawbale.LabTextDrawable;

import java.lang.reflect.Field;

/**
 * package Kotlin3:com.posun.lightui.richView.view.LightTextInputView.class
 * 作者：zyq on 2018/1/17 10:07
 * 邮箱：zyq@posun.com
 */

public class LightTextInputView extends android.support.v7.widget.AppCompatEditText implements LightRichActivityManager.LightItemIntface {
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
        int padding=QlightUnit.dip2px(getContext(),15);
        setPadding(padding, padding, padding, padding);
        setText(value);
//        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LabTextDrawable labTextDrawable=new LabTextDrawable();
        labTextDrawable.setColor(Color.parseColor("#FF4081"));
        labTextDrawable.setRound(QlightUnit.dip2px(getContext(),5));
        labTextDrawable.setText(labeText);
        labTextDrawable.setTextSize(QlightUnit.sp2px(getContext(),12));
        setBackgroundDrawable(labTextDrawable);
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
            field.set(object, String.valueOf(getText()));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
