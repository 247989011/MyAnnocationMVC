package com.pan.annotation;

import java.util.Date;

public class DeprecatedTest {

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
