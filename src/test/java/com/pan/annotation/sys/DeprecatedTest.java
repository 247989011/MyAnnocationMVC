package com.pan.annotation.sys;

import java.util.Date;

public class DeprecatedTest {
    //标记注解；用于修饰已经过时的方法;
    @Deprecated
    public void doSomething(){
        System.out.println("do something");
    }

    public static void main(String[] args){
        DeprecatedTest test = new DeprecatedTest();
        test.doSomething();
        new Date().toLocaleString();
    }
}
class ChildDeprecated extends DeprecatedTest{

    @Override
    public void doSomething(){
        System.out.println("child do some thing");
    }
    public static void main(String[] args){
        new ChildDeprecated().doSomething();
    }

}
