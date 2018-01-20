package com.posun.lightui.richView.annotation;

import com.posun.lightui.richView.LightUINB;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LightRichUI {
    LightUINB value() default LightUINB.ONE;
    int order() default 0;
}
