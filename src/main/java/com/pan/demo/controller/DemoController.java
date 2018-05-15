package com.pan.demo.controller;

import com.pan.demo.service.DemoService;
import com.pan.mvcframework.annotation.PanAutowired;
import com.pan.mvcframework.annotation.PanController;
import com.pan.mvcframework.annotation.PanRequestMapping;
import com.pan.mvcframework.annotation.PanRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@PanController
@PanRequestMapping("/web")
public class DemoController {

    @PanAutowired
    private DemoService demoService;

    @PanRequestMapping("/query.do")
    public void query(HttpServletRequest request, HttpServletResponse response , @PanRequestParam("name")  String name){
        String result = demoService.get(name);
        try {
            response.getWriter().write(result);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @PanRequestMapping("/add.do")
    public void add(HttpServletRequest request, HttpServletResponse response , @PanRequestParam("name") String name){
        String result = demoService.get(name);
        try {
            response.getWriter().write(result);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @PanRequestMapping("/update.do")
    public void update(HttpServletRequest request, HttpServletResponse response , @PanRequestParam("name")  String name){
        String result = demoService.get(name);
        try {
            response.getWriter().write(result);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @PanRequestMapping("/delete.do")
    public void delete(HttpServletRequest request, HttpServletResponse response , @PanRequestParam("name")  String name){
        String result = demoService.get(name);
        try {
            response.getWriter().write(result);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @PanRequestMapping("/upload.do")
    public void upload(HttpServletRequest request, HttpServletResponse response , @PanRequestParam("name")  String name){
        String result = demoService.get(name);
        try {
            response.getWriter().write(result);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
