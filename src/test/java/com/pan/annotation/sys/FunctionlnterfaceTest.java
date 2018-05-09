package com.pan.annotation.sys;
/**
 * 非default、static方法不能有实现，否则编译错误：Abstract methods do not specify a body
 * default、static方法必须有具体的实现，否则编译错误：This method requires a body instead of a semicolon
 * 可以拥有多个default方法
 * 可以拥有多个static方法
 * 使用接口中类型时，仅仅需要实现抽象方法，default、static方法不需要强制自己新实现
 */
@FunctionalInterface
public interface FunctionlnterfaceTest {

    static void foo(){
        System.out.println("foo静态方法");
    }
    static void foo2(){
        System.out.println("第二个static方法");
    }
    /**
     * Java8新特性–Interface中的default方法（接口默认方法）;该特性又叫扩展方法。
     *
     */
    default void bar(){
        System.out.println("bar 默认方法");
    }
    default void bar2(){
        System.out.println("第二个default方法");
    }

    void tests();

    //@FunctionalInterface 只能拥有 一个抽象方法
    //void test2();

    //void test3();
}

class SomeClass implements FunctionlnterfaceTest{

    @Override
    public void tests() {
        System.out.println("someclass implements the method");
    }

    //@Override
    public void test2() {

    }

    //@Override
    public void test3() {

    }

    public static void main(String[] args){
        SomeClass some = new SomeClass();
        some.tests();
        some.bar();
        FunctionlnterfaceTest.foo();

    }
}