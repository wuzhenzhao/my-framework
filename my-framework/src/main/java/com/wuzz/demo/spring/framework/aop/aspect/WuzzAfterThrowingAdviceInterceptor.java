package com.wuzz.demo.spring.framework.aop.aspect;

import com.wuzz.demo.spring.framework.aop.intercept.WuzzMethodInterceptor;
import com.wuzz.demo.spring.framework.aop.intercept.WuzzMethodInvocation;

import java.lang.reflect.Method;

/**
 * @description:
 * @author: Wuzhenzhao@hikvision.com.cn
 * @time 2020/3/23 10:56
 * @since 1.0
 **/
public class WuzzAfterThrowingAdviceInterceptor extends WuzzAbstractAspectAdvice implements WuzzAdvice, WuzzMethodInterceptor {


    private String throwingName;


    public WuzzAfterThrowingAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(WuzzMethodInvocation mi) throws Throwable {
        try {
            return mi.proceed();
        } catch (Throwable e) {
            invokeAdviceMethod(mi, null, e.getCause());
            throw e;
        }
    }

    public void setThrowName(String throwName) {
        this.throwingName = throwName;
    }
}
