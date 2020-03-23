package com.wuzz.demo.spring.demo.action;

import com.wuzz.demo.spring.demo.service.IModifyService;
import com.wuzz.demo.spring.demo.service.IQueryService;
import com.wuzz.demo.spring.framework.annotation.WuzzAutowired;
import com.wuzz.demo.spring.framework.annotation.WuzzController;
import com.wuzz.demo.spring.framework.annotation.WuzzRequestMapping;
import com.wuzz.demo.spring.framework.annotation.WuzzRequestParam;
import com.wuzz.demo.spring.framework.webmvc.WuzzModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 公布接口url
 *
 * @author Tom
 */
@WuzzController
@WuzzRequestMapping("/web")
public class MyAction {

    @WuzzAutowired
    IQueryService queryService;
    @WuzzAutowired
    IModifyService modifyService;

    @WuzzRequestMapping("/query.json")
    public WuzzModelAndView query(HttpServletRequest request, HttpServletResponse response,
                                  @WuzzRequestParam("name") String name) {
        String result = queryService.query(name);
        return out(response, result);
    }

    @WuzzRequestMapping("/add*.json")
    public WuzzModelAndView add(HttpServletRequest request, HttpServletResponse response,
                                @WuzzRequestParam("name") String name, @WuzzRequestParam("addr") String addr) {
        String result = null;
        try {
            result = modifyService.add(name, addr);
            return out(response, result);
        } catch (Exception e) {
//			e.printStackTrace();
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("detail", e.getMessage());
//			System.out.println(Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]",""));
            model.put("stackTrace", Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]", ""));
            return new WuzzModelAndView("500", model);
        }

    }

    @WuzzRequestMapping("/remove.json")
    public WuzzModelAndView remove(HttpServletRequest request, HttpServletResponse response,
                                   @WuzzRequestParam("id") Integer id) {
        String result = modifyService.remove(id);
        return out(response, result);
    }

    @WuzzRequestMapping("/edit.json")
    public WuzzModelAndView edit(HttpServletRequest request, HttpServletResponse response,
                                 @WuzzRequestParam("id") Integer id,
                                 @WuzzRequestParam("name") String name) {
        String result = modifyService.edit(id, name);
        return out(response, result);
    }


    private WuzzModelAndView out(HttpServletResponse resp, String str) {
        try {
            resp.getWriter().write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
