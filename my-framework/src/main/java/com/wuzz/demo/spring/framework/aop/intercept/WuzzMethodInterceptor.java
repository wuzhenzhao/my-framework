package com.wuzz.demo.spring.framework.aop.intercept;

/**
 * @description:
 * @author: Wuzhenzhao@hikvision.com.cn
 * @time 2020/3/23 10:35
 * @since 1.0
 **/
public interface WuzzMethodInterceptor {

    Object invoke(WuzzMethodInvocation invocation) throws Throwable;
}
