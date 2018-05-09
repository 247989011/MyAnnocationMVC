package com.pan.annotation.sys;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class SuppressWarningTest {

    //用于有选择的关闭编译器对类、方法、成员变量、变量初始化的警告。
    @SuppressWarnings({"unchecked","deprecated","abc"})
    public static void main(String[] args){
        Map<String,Date> map = new TreeMap<String,Date>();
        map.put("hello",new Date());
        System.out.println(map.get("hello"));

        Map map1 = new TreeMap();
        map1.put("1",23);
        map1.put("2","hello");
        System.out.println(map1);
        DeprecatedTest deprecatedTest = new DeprecatedTest();
        deprecatedTest.doSomething();
    }
}
