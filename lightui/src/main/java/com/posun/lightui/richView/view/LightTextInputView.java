package com.posun.lightui.richView.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;

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
    private boolean hastriangle = false;
    private int type = 0;

    public LightTextInputView(Context context, String labeText, String value, int type) {
        super(context);
        this.labeText = labeText;
        this.value = value;
        this.type = type;
        initUi();
    }

    private void initUi() {
        setTextColor(Color.DKGRAY);
        setTextSize(QlightUnit.sp2px(getContext(), 12));
        int labTextSize = QlightUnit.sp2px(getContext(), 12);
        int padding = QlightUnit.dip2px(getContext(), 15);
        setPadding(padding, padding + (labTextSize / 2), padding, padding);
        setText(value);
        int[] states = new int[]{android.R.attr.state_focused};
        StateListDrawable drawable = new StateListDrawable();
        LabTextDrawable labTextDrawable = new LabTextDrawable();
        labTextDrawable.setColor(Color.parseColor("#FF4081"));
        labTextDrawable.setRound(QlightUnit.dip2px(getContext(), 5));
        labTextDrawable.setText(labeText);
        labTextDrawable.setTextSize(labTextSize);
        labTextDrawable.setHastriangle(hastriangle);
        drawable.addState(states, labTextDrawable);

        LabTextDrawable nlabTextDrawable = new LabTextDrawable();
        nlabTextDrawable.setColor(Color.parseColor("#779FEB"));
        nlabTextDrawable.setRound(QlightUnit.dip2px(getContext(), 5));
        nlabTextDrawable.setText(labeText);
        nlabTextDrawable.setTextSize(labTextSize);
        nlabTextDrawable.setHastriangle(hastriangle);
        drawable.addState(new int[]{}, nlabTextDrawable);
        if (hastriangle) {
            setFocusable(false);
            setFocusableInTouchMode(false);
        }
        setBackgroundDrawable(drawable);
        setInputType(type);
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
