package com.pan.annotation.custom;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class MyTest {
    @MyAnnotation(hello = "beijing",word = "shanghai")
    public void output(){
        System.out.println("output something");
    }
    public static void main(String[] args){

    }
}
@MyAnnotation(word = "123",hello = "xv")
class  Parent {
    @MyAnnotation(word = "456",hello = "ced")
    public void method1(){
    }
}
class  Child extends  Parent{}
@MyAnnotation(word = "456",hello = "ced")
interface  ParentInterface{}
class  ChildImpl implements ParentInterface{}
class VerifyInHerited{
    public static void main(String[] args){
        try {
            Class clazz = Class.forName("com.pan.annotation.custom.Child");
            Method method = clazz.getMethod("method1",new Class[]{});
            if(method.isAnnotationPresent(MyAnnotation.class)){
                MyAnnotation myAnnotation = method.getAnnotation(MyAnnotation.class);
                System.out.println("测试子类方法"+myAnnotation.hello());
            }
            if(clazz.isAnnotationPresent(MyAnnotation.class)){
                Annotation myAnnotation = clazz.getAnnotation(MyAnnotation.class);
                System.out.println("测试子类是否继承超类注解(含@Inherited)"+myAnnotation.annotationType());
            }
            Class clazz1 = Class.forName("com.pan.annotation.custom.ChildImpl");
            if(clazz.isAnnotationPresent(MyAnnotation.class)){
                Annotation myAnnotation = clazz1.getAnnotation(MyAnnotation.class);
                System.out.println("测试接口实现类是否继承注解(含@Inherited)："+myAnnotation.annotationType());
            }

        }catch (Exception e){

        }


    }
}