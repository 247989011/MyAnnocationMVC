package com.pan.annotation.custom;

import java.lang.annotation.Annotation;

public @interface AnnotationTest {
    EnumTest  value()default EnumTest.Hello;
}
enum EnumTest{
    Hello,World,Welcome;
}
@interface  AnnotationTest1{
    String value();
}
@interface  AnnotationTest2{
}
@interface  AnnotationTest3{
    String[] value1();
}

interface  Test extends Annotation {

}