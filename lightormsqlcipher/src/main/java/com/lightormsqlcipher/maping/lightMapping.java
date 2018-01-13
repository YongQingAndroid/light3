package com.lightormsqlcipher.maping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
/***
 * 关系映射支持一对一，一对多
 * 数据模型必须为ArrayList
 * value为当前表的字段
 * mapping为映射表的匹配字段
 * 当执行存入数据库是这个字段会自动忽略
 * **/
public @interface lightMapping {
    String value() default "";
    String mapping() default "";
}
