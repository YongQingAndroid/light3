package com.posun.lightui.richView.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.widget.TextView;

import com.posun.lightui.QlightUnit;
import com.posun.lightui.richView.LightRichActivityManager;
import com.posun.lightui.richView.drawbale.LabTextDrawable;

import java.lang.reflect.Field;

/**
 * package Kotlin3:com.posun.lightui.richView.view.LightTextInputView.class
 * 作者：zyq on 2018/1/17 10:07
 * 邮箱：zyq@posun.com
 */

public class LightTextSelectView extends android.support.v7.widget.AppCompatTextView implements LightRichActivityManager.LightItemIntface {
    private String labeText, value;
    private Field field;
    private boolean hastriangle=false;
    public LightTextSelectView(Context context, String labeText, String value, boolean hastriangle) {
        super(context);
        this.labeText = labeText;
        this.value = value;
        this.hastriangle=hastriangle;
        initUi();
    }
    private void initUi() {
        setTextColor(Color.DKGRAY);
        setTextSize(QlightUnit.sp2px(getContext(),12));
        int labTextSize=QlightUnit.sp2px(getContext(),12);
        int padding=QlightUnit.dip2px(getContext(),15);
        setPadding(padding, padding+(labTextSize/2), padding, padding);
        setText(value);

        LabTextDrawable labTextDrawable=new LabTextDrawable();
        labTextDrawable.setColor(Color.parseColor("#779FEB"));
        labTextDrawable.setRound(QlightUnit.dip2px(getContext(),5));
        labTextDrawable.setText(labeText);
        labTextDrawable.setTextSize(labTextSize);
        labTextDrawable.setHastriangle(hastriangle);
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
