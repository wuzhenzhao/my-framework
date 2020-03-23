package com.wuzz.demo.spring.framework.beans.config;

/**
 * @description:
 * @author: Wuzhenzhao@hikvision.com.cn
 * @time 2020/3/17 18:20
 * @since 1.0
 **/
public class WuzzBeanPostProcessor {

    //为在 Bean 的初始化前提供回调入口
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    //为在 Bean 的初始化之后提供回调入口
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }
}
