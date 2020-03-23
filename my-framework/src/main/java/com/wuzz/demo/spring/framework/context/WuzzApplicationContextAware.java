package com.wuzz.demo.spring.framework.context;

/**
 * @description:通过解耦方式获得IOC容器的顶层设计
 *  * 后面将通过一个监听器去扫描所有的类，只要实现了此接口，
 *  * 将自动调用setApplicationContext()方法，从而将IOC容器注入到目标类中
 * @author: Wuzhenzhao@hikvision.com.cn
 * @time 2020/3/21 11:09
 * @since 1.0
 **/
public interface WuzzApplicationContextAware {

    void setApplicationContext(WuzzApplicationContext applicationContext);

}
