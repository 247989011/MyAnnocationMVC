package com.pan.annotation;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class SuppressWarningTest {

    public static void main(String[] args){
        Map<String,Date> map = new TreeMap<String,Date>();
        map.put("hello",new Date());
        System.out.println(map.get("hello"));

        Map map1 = new TreeMap();
        map1.put("1",23);
        map1.put("2","hello");
        System.out.println(map1);
    }
}
