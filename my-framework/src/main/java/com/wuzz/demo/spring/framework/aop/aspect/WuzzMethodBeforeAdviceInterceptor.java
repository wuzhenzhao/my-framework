package com.wuzz.demo.spring.framework.aop.aspect;

import com.wuzz.demo.spring.framework.aop.intercept.WuzzMethodInterceptor;
import com.wuzz.demo.spring.framework.aop.intercept.WuzzMethodInvocation;

import java.lang.reflect.Method;

/**
 * @description:
 * @author: Wuzhenzhao@hikvision.com.cn
 * @time 2020/3/23 10:52
 * @since 1.0
 **/
public class WuzzMethodBeforeAdviceInterceptor extends WuzzAbstractAspectAdvice implements WuzzAdvice, WuzzMethodInterceptor {


    private WuzzJoinPoint joinPoint;

    public WuzzMethodBeforeAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    private void before(Method method, Object[] args, Object target) throws Throwable {
        //传送了给织入参数
        //method.invoke(target);
        super.invokeAdviceMethod(this.joinPoint, null, null);

    }

    @Override
    public Object invoke(WuzzMethodInvocation mi) throws Throwable {
        //从被织入的代码中才能拿到，JoinPoint
        this.joinPoint = mi;
        before(mi.getMethod(), mi.getArguments(), mi.getThis());
        return mi.proceed();
    }
}
