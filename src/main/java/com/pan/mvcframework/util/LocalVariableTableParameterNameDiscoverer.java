package com.pan.mvcframework.util;

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

/**
 * 使用javassist获取函数参数名称;获取方法上的参数名称，非参数类型名称.
 *
 */
public class LocalVariableTableParameterNameDiscoverer {

    public static String[] getParameterNames(Class<?> clazz,String methodName) {
        ClassPool pool = ClassPool.getDefault();

        try {
            CtClass ctClass = pool.get(clazz.getName());
            CtMethod ctMethod = ctClass.getDeclaredMethod(methodName);

            // 使用javassist的反射方法的参数名
            MethodInfo methodInfo = ctMethod.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
                    .getAttribute(LocalVariableAttribute.tag);
            if (attr != null) {
                ctMethod.getParameterTypes();
                int len = ctMethod.getParameterTypes().length;
                String[] parameters = new String[len];
                // 非静态的成员函数的第一个参数是this
                int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
                for (int i = 0; i < len; i++) {
                    System.out.print(attr.variableName(i + pos) + ' ');
                    parameters[i-pos] = attr.variableName(i + pos);
                }
                return parameters;
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args){
        String[] parameters = getParameterNames(LocalVariableTableParameterNameDiscoverer.class, "test");
        System.out.println(parameters);

    }

    public static void test(String name, int aslxied) {
        System.out.println(name + aslxied);
    }
}
