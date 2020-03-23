package com.wuzz.demo.spring.framework.beans.support;

import com.wuzz.demo.spring.framework.beans.config.WuzzBeanDefinition;
import com.wuzz.demo.spring.framework.context.support.WuzzAbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: Wuzhenzhao@hikvision.com.cn
 * @time 2020/3/17 18:15
 * @since 1.0
 **/
public class WuzzDefaultListableBeanFactory extends WuzzAbstractApplicationContext {

    //存储注册信息的BeanDefinition
    protected final Map<String, WuzzBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, WuzzBeanDefinition>();

}
