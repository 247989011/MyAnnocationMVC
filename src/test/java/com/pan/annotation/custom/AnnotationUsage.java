package com.pan.annotation.custom;

/**
 * annotation 用法
 * public @interface AnnotationTest {
 *     String value()default "abc";
 * }
 * @interface AnnotationTest1{
 *     String value();
 * }
 * @interface AnnotationTest2{
 * }
 * The type Annotation usage.
 */
@AnnotationTest1("abc")
@AnnotationTest2
@AnnotationTest
@AnnotationTest3(value1 = {"abc","bcd"})
//@Test    手动继承Annotation接口不是注解类型;所有@interface注解隐示的集成了Annotation接口
public class AnnotationUsage {

    @AnnotationTest
    public void method(){
        System.out.println("this is a @AnnotationTest marker annotation method");
    }

    public static void main(String[] args){
        AnnotationUsage annotationUsage = new AnnotationUsage();
        annotationUsage.method();
    }

}
