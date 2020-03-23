package com.wuzz.demo.spring.framework.aop.aspect;

import java.lang.reflect.Method;

/**
 * @description: Aop 切点类
 * @author: Wuzhenzhao@hikvision.com.cn
 * @time 2020/3/23 10:32
 * @since 1.0
 **/
public interface WuzzJoinPoint {

    Object getThis();

    Object[] getArguments();

    Method getMethod();

    void setUserAttribute(String key, Object value);

    Object getUserAttribute(String key);
}
