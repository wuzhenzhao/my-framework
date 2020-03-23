package com.wuzz.demo.spring.framework.webmvc;

import com.wuzz.demo.spring.framework.context.WuzzApplicationContext;

import java.lang.reflect.Constructor;

/**
 * @description:
 * @author: Wuzhenzhao@hikvision.com.cn
 * @time 2020/3/21 15:36
 * @since 1.0
 **/
public class Test {
    public static void main(String[] args) {
//        WuzzApplicationContext context =new WuzzApplicationContext("classpath:application.properties");
//        System.out.println(context);
        Class<?> aClass = null;
        try {
            aClass = Class.forName("com.wuzz.demo.spring.framework.context.support.WuzzAbstractApplicationContext");
            final Constructor<?>[] constructors = aClass.getConstructors();
            Object o = aClass.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }
}
