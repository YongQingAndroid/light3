package com.posun.lightui.richView;

/**
 * package Kotlin3:com.posun.lightui.richView.LightActionType.class
 * 作者：zyq on 2018/1/16 14:37
 * 邮箱：zyq@posun.com
 */

public enum LightActionType {
    NET_GET("net:get"), NET_POST("net:post"), ACTION("action"), SELECTDATA("selectdata"), TIMEPICKER("timepicker");
    private   String type;

    LightActionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
