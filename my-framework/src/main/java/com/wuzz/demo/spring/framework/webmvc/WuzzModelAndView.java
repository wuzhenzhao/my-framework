package com.wuzz.demo.spring.framework.webmvc;

import java.util.Map;

/**
 * @description:
 * @author: Wuzhenzhao@hikvision.com.cn
 * @time 2020/3/21 11:27
 * @since 1.0
 **/
public class WuzzModelAndView {

    private String viewName;

    private Map<String,?> model;

    public WuzzModelAndView() {
    }

    public WuzzModelAndView(String viewName) { this.viewName = viewName; }

    public WuzzModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

//    public void setViewName(String viewName) {
//        this.viewName = viewName;
//    }

    public Map<String, ?> getModel() {
        return model;
    }

//    public void setModel(Map<String, ?> model) {
//        this.model = model;
//    }
}
