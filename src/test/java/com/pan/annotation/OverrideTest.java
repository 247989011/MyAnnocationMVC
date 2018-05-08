package com.pan.annotation;

public class OverrideTest {

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

