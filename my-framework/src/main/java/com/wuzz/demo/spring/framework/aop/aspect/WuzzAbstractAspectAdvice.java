package com.wuzz.demo.spring.framework.aop.aspect;

import java.lang.reflect.Method;

/**
 * @description: Aop 抽象的 切面类
 * @author: Wuzhenzhao@hikvision.com.cn
 * @time 2020/3/23 10:45
 * @since 1.0
 **/
public abstract class WuzzAbstractAspectAdvice {

    private Method aspectMethod;
    private Object aspectTarget;

    public WuzzAbstractAspectAdvice(Method aspectMethod, Object aspectTarget) {
        this.aspectMethod = aspectMethod;
        this.aspectTarget = aspectTarget;
    }

    public Object invokeAdviceMethod(WuzzJoinPoint joinPoint, Object returnValue, Throwable tx) throws Throwable {
        Class<?>[] paramTypes = this.aspectMethod.getParameterTypes();
        if (null == paramTypes || paramTypes.length == 0) {
            return this.aspectMethod.invoke(aspectTarget);
        } else {
            Object[] args = new Object[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++) {
                if (paramTypes[i] == WuzzJoinPoint.class) {
                    args[i] = joinPoint;
                } else if (paramTypes[i] == Throwable.class) {
                    args[i] = tx;
                } else if (paramTypes[i] == Object.class) {
                    args[i] = returnValue;
                }
            }
            return this.aspectMethod.invoke(aspectTarget, args);
        }
    }
}
