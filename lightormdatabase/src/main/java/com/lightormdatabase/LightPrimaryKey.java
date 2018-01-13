//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.lightormdatabase;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * orm 数据库主键
 * */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LightPrimaryKey {
    String AUTOINCREMENT="AUTOINCREMENT";
    String value() default "";
}
