package com.wuzz.demo.spring.framework.webmvc;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @description:
 * @author: Wuzhenzhao@hikvision.com.cn
 * @time 2020/3/21 11:25
 * @since 1.0
 **/
public class WuzzHandlerMapping {

    private Object controller;    //保存方法对应的实例
    private Method method;        //保存映射的方法
    private Pattern pattern;    //URL的正则匹配

    public WuzzHandlerMapping() {
    }

    public WuzzHandlerMapping(Pattern pattern, Object controller, Method method) {
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

}
