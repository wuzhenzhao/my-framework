package com.wuzz.demo.spring.framework.aop.aspect;

import com.wuzz.demo.spring.framework.aop.intercept.WuzzMethodInterceptor;
import com.wuzz.demo.spring.framework.aop.intercept.WuzzMethodInvocation;

import java.lang.reflect.Method;

/**
 * @description:
 * @author: Wuzhenzhao@hikvision.com.cn
 * @time 2020/3/23 10:55
 * @since 1.0
 **/
public class WuzzAfterReturningAdviceInterceptor extends WuzzAbstractAspectAdvice implements WuzzAdvice, WuzzMethodInterceptor {

    private WuzzJoinPoint joinPoint;

    public WuzzAfterReturningAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(WuzzMethodInvocation mi) throws Throwable {
        Object retVal = mi.proceed();
        this.joinPoint = mi;
        this.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());
        return retVal;
    }

    private void afterReturning(Object retVal, Method method, Object[] arguments, Object aThis) throws Throwable {
        super.invokeAdviceMethod(this.joinPoint, retVal, null);
    }
}
