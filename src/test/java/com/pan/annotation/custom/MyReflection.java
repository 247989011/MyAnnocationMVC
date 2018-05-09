package com.pan.annotation.custom;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class MyReflection {

    public static void main(String[] args){

        Class<MyTest> clazz = MyTest.class;
        //Annotation[] annottations = clazz.getAnnotations();
        try {

            Method method = clazz.getDeclaredMethod("output");
            method.setAccessible(true);
            Annotation[] methodAnnotations = method.getAnnotations();
            if(method.isAnnotationPresent(MyAnnotation.class)){
                Annotation annotation = method.getAnnotation(MyAnnotation.class);
                String hello = ((MyAnnotation) annotation).hello();
                String world = ((MyAnnotation) annotation).word();
                System.out.println(hello+world);
            }
            for (int i = 0; i < methodAnnotations.length; i++) {
                Annotation annotation = methodAnnotations[0];
                System.out.println(methodAnnotations[0]);
            }

            method.setAccessible(false);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }






    }
}
