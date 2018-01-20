package com.posun.lightui.richView;

import com.posun.lightui.richView.annotation.LightBtnItem;
import com.posun.lightui.richView.annotation.LightCheckBox;
import com.posun.lightui.richView.annotation.LightSimpleClick;
import com.posun.lightui.richView.annotation.LightTextLab;

/**
 * Created by qing on 2018/1/11.
 */

public enum LightItemType {
    TEXT(LightTextLab.class),
    CHECKBOX(LightCheckBox.class),
    SWITCH(LightCheckBox.class),
    CHOICE(LightTextLab.class),
    BUTTON(LightBtnItem.class),
    ACTION_LIGHTSIMPLE_CLICK(LightSimpleClick.class);
    private Class clazz;
    LightItemType(Class clazz) {
        this.clazz = clazz;
    }

    public Class getViewClass() {
        return clazz;
    }
}
