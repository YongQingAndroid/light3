package com.posun.lightui.richView.instent;

import com.posun.lightui.richView.LightActionType;

/**
 * package Kotlin3:com.posun.lightui.richView.instent.EventBean.class
 * 作者：zyq on 2018/1/17 18:03
 * 邮箱：zyq@posun.com
 */

public class EventBean {
    private LightActionType type;
    private String value;

    public EventBean(LightActionType type, String value) {
        this.value = value;
        this.type = type;

    }

    public LightActionType getType() {
        return type;
    }

    public void setType(LightActionType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
