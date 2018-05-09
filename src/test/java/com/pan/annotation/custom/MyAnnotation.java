package com.pan.annotation.custom;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)     //用于指定Annotation可以保留多长时间。
@Target({ElementType.ANNOTATION_TYPE,ElementType.TYPE,ElementType.METHOD})//指定Annotation用于修饰哪些程序元素。
@Inherited  //指定Annotation具有继承性。允许子类继承父类的该注解
//@Documented     //则在用javadoc命令生成API文档后，所有使用注解A修饰的程序元素，将会包含注解A的说明。
public @interface MyAnnotation {
    String hello();
    String word();
}
