package com.wuzz.demo.spring.framework.aop;

import com.wuzz.demo.spring.framework.aop.support.WuzzAdvisedSupport;

/**
 * @description: AOP  CGlib代理实现类
 * @author: Wuzhenzhao@hikvision.com.cn
 * @time 2020/3/23 10:24
 * @since 1.0
 **/
public class WuzzCglibAopProxy implements  WuzzAopProxy {

    public WuzzCglibAopProxy(WuzzAdvisedSupport config) {
    }

    @Override
    public Object getProxy() {
        return null;
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }
}
