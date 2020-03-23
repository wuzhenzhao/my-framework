package com.wuzz.demo.spring.framework.webmvc;

import com.wuzz.demo.spring.framework.annotation.WuzzController;
import com.wuzz.demo.spring.framework.annotation.WuzzRequestMapping;
import com.wuzz.demo.spring.framework.context.WuzzApplicationContext;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description:
 * @author: Wuzhenzhao@hikvision.com.cn
 * @time 2020/3/21 11:16
 * @since 1.0
 **/
@Slf4j
public class WuzzDispatcherServlet extends HttpServlet {

    private final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";

    private WuzzApplicationContext context;


    private List<WuzzHandlerMapping> handlerMappings = new ArrayList<WuzzHandlerMapping>();

    private Map<WuzzHandlerMapping,WuzzHandlerAdapter> handlerAdapters = new HashMap<WuzzHandlerMapping,WuzzHandlerAdapter>();

    private List<WuzzViewResolver> viewResolvers = new ArrayList<WuzzViewResolver>();

    public WuzzDispatcherServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            this.doDispatch(req,resp);
        }catch(Exception e){
            resp.getWriter().write("500 Exception,Details:\r\n" + Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]", "").replaceAll(",\\s", "\r\n"));
            e.printStackTrace();
//            new GPModelAndView("500");

        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        //1、通过从request中拿到URL，去匹配一个HandlerMapping
        WuzzHandlerMapping handler = getHandler(req);

        if(handler == null){
            if("/".equals(req.getRequestURI())){
                processDispatchResult(req,resp,new WuzzModelAndView("first"));
                return;
            }else{
                processDispatchResult(req,resp,new WuzzModelAndView("404"));
                return;
            }

        }

        //2、准备调用前的参数
        WuzzHandlerAdapter ha = getHandlerAdapter(handler);

        //3、真正的调用方法,返回ModelAndView存储了要穿页面上值，和页面模板的名称
        WuzzModelAndView mv = ha.handle(req,resp,handler);

        //这一步才是真正的输出
        processDispatchResult(req, resp, mv);

    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, WuzzModelAndView mv) throws Exception{
        //把给我的ModleAndView变成一个HTML、OuputStream、json、freemark、veolcity
        //ContextType
        if(null == mv){return;}

        //如果ModelAndView不为null，怎么办？
        if(this.viewResolvers.isEmpty()){return;}

        for (WuzzViewResolver viewResolver : this.viewResolvers) {
            WuzzView view = viewResolver.resolveViewName(mv.getViewName(),null);
            view.render(mv.getModel(),req,resp);
            return;
        }


    }

    private WuzzHandlerAdapter getHandlerAdapter(WuzzHandlerMapping handler) {
        if(this.handlerAdapters.isEmpty()){return null;}
        WuzzHandlerAdapter ha = this.handlerAdapters.get(handler);
        if(ha.supports(handler)){
            return ha;
        }
        return null;
    }


    private WuzzHandlerMapping getHandler(HttpServletRequest req) throws Exception{
        if(this.handlerMappings.isEmpty()){ return null; }

        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");

        for (WuzzHandlerMapping handler : this.handlerMappings) {
            try{
                Matcher matcher = handler.getPattern().matcher(url);
                //如果没有匹配上继续下一个匹配
                if(!matcher.matches()){ continue; }

                return handler;
            }catch(Exception e){
                throw e;
            }
        }
        return null;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        //1、初始化ApplicationContext
        context = new WuzzApplicationContext(config.getInitParameter(CONTEXT_CONFIG_LOCATION));
        //2、初始化Spring MVC 九大组件
        initStrategies(context);
    }


    //初始化策略
    protected void initStrategies(WuzzApplicationContext context) {
        //多文件上传的组件
        initMultipartResolver(context);
        //初始化本地语言环境
        initLocaleResolver(context);
        //初始化模板处理器
        initThemeResolver(context);


        //handlerMapping，必须实现
        initHandlerMappings(context);
        //初始化参数适配器，必须实现
        initHandlerAdapters(context);
        //初始化异常拦截器
        initHandlerExceptionResolvers(context);
        //初始化视图预处理器
        initRequestToViewNameTranslator(context);


        //初始化视图转换器，必须实现
        initViewResolvers(context);
        //参数缓存器
        initFlashMapManager(context);
    }

    private void initFlashMapManager(WuzzApplicationContext context) {
    }

    private void initViewResolvers(WuzzApplicationContext context) {

        //拿到模板的存放目录
        String templateRoot = context.getConfig().getProperty("templateRoot");
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();

        File templateRootDir = new File(templateRootPath);
        String[] templates = templateRootDir.list();
        for (int i = 0; i < templates.length; i ++) {
            //这里主要是为了兼容多模板，所有模仿Spring用List保存
            //在我写的代码中简化了，其实只有需要一个模板就可以搞定
            //只是为了仿真，所有还是搞了个List
            this.viewResolvers.add(new WuzzViewResolver(templateRoot));
        }

    }

    private void initRequestToViewNameTranslator(WuzzApplicationContext context) {
    }

    private void initHandlerExceptionResolvers(WuzzApplicationContext context) {
    }

    private void initHandlerAdapters(WuzzApplicationContext context) {

        //把一个requet请求变成一个handler，参数都是字符串的，自动配到handler中的形参

        //可想而知，他要拿到HandlerMapping才能干活
        //就意味着，有几个HandlerMapping就有几个HandlerAdapter
        for (WuzzHandlerMapping handlerMapping : this.handlerMappings) {
            this.handlerAdapters.put(handlerMapping,new WuzzHandlerAdapter());
        }


    }

    private void initHandlerMappings(WuzzApplicationContext context) {

        String [] beanNames = context.getBeanDefinitionNames();

        try {

            for (String beanName : beanNames) {

                Object controller = context.getBean(beanName);
                if(null == controller)
                return;

                Class<?> clazz = controller.getClass();

                if(!clazz.isAnnotationPresent(WuzzController.class)){
                    continue;
                }

                String baseUrl = "";
                //获取Controller的url配置
                if(clazz.isAnnotationPresent(WuzzRequestMapping.class)){
                    WuzzRequestMapping requestMapping = clazz.getAnnotation(WuzzRequestMapping.class);
                    baseUrl = requestMapping.value();
                }

                //获取Method的url配置
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {

                    //没有加RequestMapping注解的直接忽略
                    if(!method.isAnnotationPresent(WuzzRequestMapping.class)){ continue; }

                    //映射URL
                    WuzzRequestMapping requestMapping = method.getAnnotation(WuzzRequestMapping.class);
                    //  /demo/query

                    //  (//demo//query)

                    String regex = ("/" + baseUrl + "/" + requestMapping.value().replaceAll("\\*",".*")).replaceAll("/+", "/");
                    Pattern pattern = Pattern.compile(regex);

                    this.handlerMappings.add(new WuzzHandlerMapping(pattern,controller,method));
                    log.info("Mapped " + regex + "," + method);
                }


            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void initThemeResolver(WuzzApplicationContext context) {
    }

    private void initLocaleResolver(WuzzApplicationContext context) {
    }

    private void initMultipartResolver(WuzzApplicationContext context) {
    }

}
