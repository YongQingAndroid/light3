package com.posun.lightui.richView.view;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.posun.lightui.QlightUnit;
import com.posun.lightui.TableSelectGroup;
import com.posun.lightui.richView.LightRichActivityManager;
import com.posun.lightui.richView.instent.ResourcesJsonArray;

import org.json.JSONArray;

import java.lang.reflect.Field;

public class LightCheckGroup extends LinearLayout implements LightRichActivityManager.LightItemIntface {
    private String labeText, value;
    private android.support.v7.widget.AppCompatTextView leftView;
    private TableSelectGroup rightView;
    private Field field;
    private ResourcesJsonArray resourcesJsonArray, resultJsonArray;

    public LightCheckGroup(Context context, String labeText, String value, ResourcesJsonArray resourcesJsonArray, ResourcesJsonArray resultJsonArray) {
        super(context);
        this.labeText = labeText;
        this.value = value;
        this.resourcesJsonArray = resourcesJsonArray;
        this.resultJsonArray = resultJsonArray;
        initUI();
    }

    private int getDefultSelect(String arg) throws Exception {
        JSONArray jsonArray = null;
        if (QlightUnit.isEmpty(arg))
            return 0;
        if (resultJsonArray != null && resultJsonArray.length() > 0) {
            jsonArray = resultJsonArray;
        } else {
            jsonArray = resourcesJsonArray;
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            if (arg.equals(jsonArray.getString(i))) {
                return i;
            }
        }
        return 0;
    }

    public void setField(Field field) {
        this.field = field;
    }

    private void initUI() {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.RIGHT);
        LayoutParams leftlayoutParams = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
        LayoutParams layoutParams = new LayoutParams(QlightUnit.dip2px(getContext(), 200), QlightUnit.dip2px(getContext(), 30));
        leftView = new android.support.v7.widget.AppCompatTextView(getContext());
        leftView.setText(labeText);
        leftView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        addView(leftView, leftlayoutParams);
        rightView = new TableSelectGroup(getContext());
        rightView.setColor(Color.BLUE);
        if (resourcesJsonArray != null)
            try {
                rightView.setData(resourcesJsonArray.toStringArray());
                rightView.setSeletIndex(getDefultSelect(value));
            } catch (Exception e) {
                e.printStackTrace();
            }
        addView(rightView, layoutParams);

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
            String item = null;
            if (resultJsonArray != null && resultJsonArray.length() > 0) {
                item = resultJsonArray.getString(rightView.getSelectIndex());
            } else {
                item = resourcesJsonArray.getString(rightView.getSelectIndex());
            }
            field.set(object, String.valueOf(item));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}