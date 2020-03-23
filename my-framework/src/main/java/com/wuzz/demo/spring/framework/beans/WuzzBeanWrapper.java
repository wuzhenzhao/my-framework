package com.wuzz.demo.spring.framework.beans;

/**
 * @description:
 * @author: Wuzhenzhao@hikvision.com.cn
 * @time 2020/3/17 18:11
 * @since 1.0
 **/
public class WuzzBeanWrapper {

    private Object wrappedInstance;
    private Class<?> wrappedClass;

    public WuzzBeanWrapper() {
    }

    public WuzzBeanWrapper(Object wrappedInstance){
        this.wrappedInstance = wrappedInstance;
    }

    public Object getWrappedInstance(){
        return this.wrappedInstance;
    }

    // 返回代理以后的Class
    // 可能会是这个 $Proxy0
    public Class<?> getWrappedClass(){
        return this.wrappedInstance.getClass();
    }
}
