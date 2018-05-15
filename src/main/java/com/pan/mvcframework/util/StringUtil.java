package com.pan.mvcframework.util;

public class StringUtil {

    public static String lowerFirstLetter(String str){
        char[] chars = str.toCharArray();
        if (chars[0]>=65 && chars[0] <=90){
            chars[0] += 32;
        }else{
            return str;//不是大写字母不做处理
        }
        return String.valueOf(chars);
    }
}
