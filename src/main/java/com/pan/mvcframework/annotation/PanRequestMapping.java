package com.pan.mvcframework.annotation;

import java.lang.annotation.*;

@Documented
@Target(value = {ElementType.TYPE,ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
public @interface PanRequestMapping {
    String value() default "";
}
