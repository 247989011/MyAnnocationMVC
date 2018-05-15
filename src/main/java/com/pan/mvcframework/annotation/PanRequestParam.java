package com.pan.mvcframework.annotation;

import java.lang.annotation.*;

@Documented
@Target(value = ElementType.PARAMETER)
@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
public @interface PanRequestParam {
    String value() default "";

}
