package com.pan.demo.service.Impl;

import com.pan.demo.service.DemoService;
import com.pan.mvcframework.annotation.PanService;

@PanService
public class DemoServiceImpl implements DemoService {


    public String get(String name) {
        System.out.println("this is DemoServiceImpl class's method :get");

        return "this is DemoServiceImpl class's method :get"+name;
    }
}
