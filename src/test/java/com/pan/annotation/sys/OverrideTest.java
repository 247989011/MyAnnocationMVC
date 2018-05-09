package com.pan.annotation.sys;

public class OverrideTest {
    //@Override：是一个标记注解类型，它被用作标注方法。它说明了被标注的方法重载了父类的方法，起到了断言的作用。
    @Override
    public String toString(){
        System.out.println("this is override");
        return "this is override";
    }
    public static void main(String[] args){
        OverrideTest test = new OverrideTest();
        System.out.println(test.toString());

    }
}

