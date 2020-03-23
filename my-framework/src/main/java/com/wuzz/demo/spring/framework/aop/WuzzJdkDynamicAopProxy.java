package com.wuzz.demo.spring.framework.aop;

import com.wuzz.demo.spring.framework.aop.intercept.WuzzMethodInvocation;
import com.wuzz.demo.spring.framework.aop.support.WuzzAdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @description: AOP  JDK动态代理实现类
 * @author: Wuzhenzhao@hikvision.com.cn
 * @time 2020/3/23 10:26
 * @since 1.0
 **/
public class WuzzJdkDynamicAopProxy implements  WuzzAopProxy, InvocationHandler {

    private WuzzAdvisedSupport advised;

    public WuzzJdkDynamicAopProxy(WuzzAdvisedSupport config){
        this.advised = config;
    }

    @Override
    public Object getProxy() {
        return getProxy(this.advised.getTargetClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader,this.advised.getTargetClass().getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<Object> interceptorsAndDynamicMethodMatchers = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method,this.advised.getTargetClass());
        WuzzMethodInvocation invocation = new WuzzMethodInvocation(proxy,this.advised.getTarget(),method,args,this.advised.getTargetClass(),interceptorsAndDynamicMethodMatchers);
        return invocation.proceed();
    }
}
