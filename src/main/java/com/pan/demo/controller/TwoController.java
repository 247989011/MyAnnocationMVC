package com.pan.demo.controller;

import com.pan.demo.service.DemoService;
import com.pan.demo.service.Impl.DemoServiceImpl;
import com.pan.mvcframework.annotation.PanAutowired;
import com.pan.mvcframework.annotation.PanController;
import com.pan.mvcframework.annotation.PanRequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@PanController
@PanRequestMapping("two")
public class TwoController {

    @PanAutowired
    private DemoServiceImpl demoService;
    @PanRequestMapping("/edit.do")
    public void edit(HttpServletRequest request , HttpServletResponse response,String name){
        String result = demoService.get(name);
        try{
            response.getWriter().write(result);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
