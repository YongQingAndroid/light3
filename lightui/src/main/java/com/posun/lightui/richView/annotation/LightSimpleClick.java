package com.posun.lightui.richView.annotation;

import com.posun.lightui.richView.LightActionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.posun.lightui.richView.LightActionType.ACTION;

/**
 * package Kotlin3:com.posun.lightui.richView.annotation.LightSimpleClick.class
 * 作者：zyq on 2018/1/15 16:58
 * 邮箱：zyq@posun.com
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LightSimpleClick {
    String value() default "";
    LightActionType type() default ACTION;
}
