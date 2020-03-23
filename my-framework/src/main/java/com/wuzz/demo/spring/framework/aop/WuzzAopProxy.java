package com.wuzz.demo.spring.framework.aop;

/**
 * @description: AOP顶层实现类
 * @author: Wuzhenzhao@hikvision.com.cn
 * @time 2020/3/23 10:23
 * @since 1.0
 **/
public interface WuzzAopProxy {

    Object getProxy();


    Object getProxy(ClassLoader classLoader);
}
