package com.posun.lightui.richView.annotation;

import com.posun.lightui.richView.LightRichType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package light3:com.posun.lightui.richView.annotation.LightResources.class
 * 作者：zyq on 2018/1/22 16:34
 * 邮箱：zyq@posun.com
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LightResources {
    int type() default LightRichType.RESOURCES_FINAL;

    String resources() default "[]";

    String result() default "[]";
}
