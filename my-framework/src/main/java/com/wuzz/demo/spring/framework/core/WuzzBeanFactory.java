package com.wuzz.demo.spring.framework.core;

/**
 * @description:单例工厂的顶层设计
 * @author: Wuzhenzhao@hikvision.com.cn
 * @time 2020/3/17 18:03
 * @since 1.0
 **/
public interface WuzzBeanFactory {

    /**
     * 功能描述: <br>
     * 根据beanName从IOC容器中获得一个实例Bean
     *
     * @Param: [beanName]
     * @Return: java.lang.Object
     * @Author: wuzhenzhao
     * @Date: 2020/3/17 18:04
     */
    Object getBean(String beanName) throws Exception;

    /**
     * 功能描述: <br>
     * 根据beanClass从IOC容器中获得一个实例Bean
     *
     * @Param: [beanClass]
     * @Return: java.lang.Object
     * @Author: wuzhenzhao
     * @Date: 2020/3/17 18:04
     */
    Object getBean(Class<?> beanClass) throws Exception;
}
