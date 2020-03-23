package com.wuzz.demo.spring.framework.context.support;

/**
 * @description:IOC容器实现的顶层设计
 * @author: Wuzhenzhao@hikvision.com.cn
 * @time 2020/3/17 18:11
 * @since 1.0
 **/
public abstract  class WuzzAbstractApplicationContext {

    public WuzzAbstractApplicationContext() {
    }

    /**
     * 功能描述: <br>
     * 受保护，只提供给子类重写
     * @Param: []
     * @Return: void
     * @Author: wuzhenzhao
     * @Date: 2020/3/17 18:13
     */
    protected void refresh() throws Exception {}
}
