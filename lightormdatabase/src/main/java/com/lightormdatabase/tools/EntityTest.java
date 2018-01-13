package com.lightormdatabase.tools;

import com.lightormdatabase.log.LightLog;

/**
 * package Kotlin3:com.lightormdatabase.tools.EntityTest.class
 * 作者：zyq on 2017/11/27 16:42
 * 邮箱：zyq@posun.com
 */

public class EntityTest {
    private String msg = "EntityTest.class";

    public EntityTest() {

    }

    public EntityTest(String msg) {
        this.msg = msg;
    }

    public void printlog() {
        LightLog.E(msg);
    }
}
