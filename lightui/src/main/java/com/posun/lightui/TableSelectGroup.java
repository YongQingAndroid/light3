package com.posun.lightui;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * package kuaiyixiao:org.feezu.liuli.timeselector.view.TableSelectGroup.class
 * 作者：zyq on 2017/12/12 14:07
 * 邮箱：zyq@posun.com
 */

public class TableSelectGroup extends RadioGroup {
    String[] data;
    private DataChangeListener listener;
    private int radio = 5, color = Color.BLUE;
    private int index = 0;

    public TableSelectGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setListener(DataChangeListener listener) {
        this.listener = listener;
    }

    public void setColor(int color) {
        this.color = color;
        initUI();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initUI() {
        setGravity(Gravity.CENTER);
        setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (listener == null)
                    return;
                TableSelectGroup.this.index = getIndexById(checkedId);
                listener.onDataSelect(index, data[index]);
            }
        });

        int radio_bg = QlightUnit.dip2px(getContext(), radio);
        float outRectr[] = new float[]{radio_bg, radio_bg, radio_bg, radio_bg, radio_bg, radio_bg, radio_bg, radio_bg};
        RoundRectShape rectShape = new RoundRectShape(outRectr, null, null);
        ShapeDrawable pressedDrawable = new ShapeDrawable(rectShape);
        pressedDrawable.getPaint().setColor(color);
        pressedDrawable.getPaint().setTypeface(Typeface.DEFAULT_BOLD);
        pressedDrawable.getPaint().setStyle(Paint.Style.STROKE);
        setBackground(pressedDrawable);
    }

    public int getSelectIndex() {
        return index;
    }

    public int getIndexById(int id) {
        return id - 10000;
    }

    public int getId(int i) {
        return 10000 + i;
    }

    public void setSeletIndex(int index) {
        ((RadioButton) getChildAt(index)).setChecked(true);
    }

    public void setData(String[] data) {
        removeAllViews();
        this.data = data;
        if (data == null || data.length == 0)
            return;
        int size = data.length;
        int radio = QlightUnit.dip2px(getContext(), this.radio);
        LayoutParams layoutParams = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
        for (int i = 0; i < size; i++) {
            LightRichBubbleRadioButton radioButton = new LightRichBubbleRadioButton(getContext());
            if (i == 0) {
                radioButton.setRadio_left_bottom(radio);
                radioButton.setRadio_left_top(radio);
                radioButton.setChecked(true);
            }
            if (i == size - 1) {
                radioButton.setRadio_right_bottom(radio);
                radioButton.setRadio_right_top(radio);
            }
            radioButton.setBg_active_color(color);
            radioButton.setBg_color(Color.TRANSPARENT);
            radioButton.setBg_press_color(color);

            radioButton.setText_active_color(Color.WHITE);
            radioButton.setText_color(color);
            radioButton.setText_press_color(Color.WHITE);
            radioButton.setText(data[i]);
            radioButton.setGravity(Gravity.CENTER);


            radioButton.setId(getId(i));
            radioButton.commit();
            this.addView(radioButton, layoutParams);
        }
    }

    public interface DataChangeListener {
        void onDataSelect(int index, String arg);
    }
}
