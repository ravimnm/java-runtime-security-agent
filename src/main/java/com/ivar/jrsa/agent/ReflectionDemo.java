package com.ivar.jrsa.agent;

import java.lang.reflect.Method;

public class ReflectionDemo {

    public void dangerousReflection() throws Exception {

        Class<?> clazz =
                Class.forName("java.lang.Runtime");

        Method method =
                clazz.getMethod("getRuntime");

        Object runtime =
                method.invoke(null);

        System.out.println(runtime);
    }

    public static void main(String[] args)
            throws Exception {

        ReflectionDemo demo =
                new ReflectionDemo();

        demo.dangerousReflection();
    }
}