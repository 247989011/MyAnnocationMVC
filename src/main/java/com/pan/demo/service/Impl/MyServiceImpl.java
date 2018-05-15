package com.pan.demo.service.Impl;

import com.pan.demo.service.DemoService;
import com.pan.mvcframework.annotation.PanService;

@PanService("my")
public class MyServiceImpl implements DemoService {

    public String get(String name) {
        return "My name is :" + name;
    }
}
