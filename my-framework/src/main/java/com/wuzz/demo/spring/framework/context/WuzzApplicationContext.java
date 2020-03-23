package com.wuzz.demo.spring.framework.context;

import com.wuzz.demo.spring.framework.annotation.WuzzAutowired;
import com.wuzz.demo.spring.framework.annotation.WuzzController;
import com.wuzz.demo.spring.framework.annotation.WuzzService;
import com.wuzz.demo.spring.framework.aop.WuzzAopProxy;
import com.wuzz.demo.spring.framework.aop.WuzzCglibAopProxy;
import com.wuzz.demo.spring.framework.aop.WuzzJdkDynamicAopProxy;
import com.wuzz.demo.spring.framework.aop.config.WuzzAopConfig;
import com.wuzz.demo.spring.framework.aop.support.WuzzAdvisedSupport;
import com.wuzz.demo.spring.framework.beans.WuzzBeanWrapper;
import com.wuzz.demo.spring.framework.beans.config.WuzzBeanDefinition;
import com.wuzz.demo.spring.framework.beans.config.WuzzBeanPostProcessor;
import com.wuzz.demo.spring.framework.beans.support.WuzzBeanDefinitionReader;
import com.wuzz.demo.spring.framework.beans.support.WuzzDefaultListableBeanFactory;
import com.wuzz.demo.spring.framework.core.WuzzBeanFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 源码里有  ClassPathXmlApplicationContext ，AnnotationApplicationContext ,这里直接就搞一个ApplicationContext就好
 * @author: Wuzhenzhao@hikvision.com.cn
 * @time 2020/3/17 18:07
 * @since 1.0
 **/
public class WuzzApplicationContext extends WuzzDefaultListableBeanFactory implements WuzzBeanFactory {

    private String[] configLoactions;
    private WuzzBeanDefinitionReader reader;

    //单例的IOC容器缓存
    private Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<String, Object>();
    //通用的IOC容器
    private Map<String, WuzzBeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<String, WuzzBeanWrapper>();

    public WuzzApplicationContext() {

    }

    public WuzzApplicationContext(String... configLoactions) {
        this.configLoactions = configLoactions;
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void refresh() throws Exception {
        //1、定位，定位配置文件
        reader = new WuzzBeanDefinitionReader(this.configLoactions);

        //2、加载配置文件，扫描相关的类，把它们封装成BeanDefinition
        List<WuzzBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();

        //3、注册，把配置信息放到容器里面(伪IOC容器)
        doRegisterBeanDefinition(beanDefinitions);

        //4、把不是延时加载的类，有提前初始化
        doAutowrited();
    }

    //只处理非延时加载的情况
    private void doAutowrited() {
        for (Map.Entry<String, WuzzBeanDefinition> beanDefinitionEntry : super.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            if (!beanDefinitionEntry.getValue().isLazyInit()) {
                try {
                    getBean(beanName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doRegisterBeanDefinition(List<WuzzBeanDefinition> beanDefinitions) throws Exception {

        for (WuzzBeanDefinition beanDefinition : beanDefinitions) {
            if (super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new Exception("The “" + beanDefinition.getFactoryBeanName() + "” is exists!!");
            }
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
        }
        //到这里为止，容器初始化完毕
    }

    public Object getBean(Class<?> beanClass) throws Exception {
        return getBean(beanClass.getName());
    }

    //依赖注入，从这里开始，通过读取BeanDefinition中的信息
    //然后，通过反射机制创建一个实例并返回
    //Spring做法是，不会把最原始的对象放出去，会用一个BeanWrapper来进行一次包装
    //装饰器模式：
    //1、保留原来的OOP关系
    //2、我需要对它进行扩展，增强（为了以后AOP打基础）
    public Object getBean(String beanName) throws Exception {

        WuzzBeanDefinition wuzzBeanDefinition = this.beanDefinitionMap.get(beanName);
        Object instance = null;

        //这个逻辑还不严谨，自己可以去参考Spring源码，这里只是写着，没作用
        //工厂模式 + 策略模式
        WuzzBeanPostProcessor postProcessor = new WuzzBeanPostProcessor();

        postProcessor.postProcessBeforeInitialization(instance, beanName);

        instance = instantiateBean(beanName, wuzzBeanDefinition);
        if (instance == null) {
            return null;
        }


        //3、把这个对象封装到BeanWrapper中
        WuzzBeanWrapper beanWrapper = new WuzzBeanWrapper(instance);

        //singletonObjects

        //factoryBeanInstanceCache

        //4、把BeanWrapper存到IOC容器里面
//        //1、初始化

//        //class A{ B b;}
//        //class B{ A a;}
//        //先有鸡还是先有蛋的问题，一个方法是搞不定的，要分两次

        //2、拿到BeanWraoper之后，把BeanWrapper保存到IOC容器中去
        this.factoryBeanInstanceCache.put(beanName, beanWrapper);

        postProcessor.postProcessAfterInitialization(instance, beanName);

//        //3、注入
        populateBean(beanName, new WuzzBeanDefinition(), beanWrapper);


        return this.factoryBeanInstanceCache.get(beanName).getWrappedInstance();
    }

    private void populateBean(String beanName, WuzzBeanDefinition wuzzBeanDefinition, WuzzBeanWrapper wuzzBeanWrapper) {
        Object instance = wuzzBeanWrapper.getWrappedInstance();

//        wuzzBeanDefinition.getBeanClassName();

        Class<?> clazz = wuzzBeanWrapper.getWrappedClass();
        //判断只有加了注解的类，才执行依赖注入
        if (!(clazz.isAnnotationPresent(WuzzController.class) || clazz.isAnnotationPresent(WuzzService.class))) {
            return;
        }

        //获得所有的fields
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(WuzzAutowired.class)) {
                continue;
            }

            WuzzAutowired autowired = field.getAnnotation(WuzzAutowired.class);

            String autowiredBeanName = autowired.value().trim();
            if ("".equals(autowiredBeanName)) {
                autowiredBeanName = field.getType().getName();
            }

            //强制访问
            field.setAccessible(true);

            try {
                //为什么会为NULL，先留个坑
                if (this.factoryBeanInstanceCache.get(autowiredBeanName) == null) {
                    continue;
                }
//                if(instance == null){
//                    continue;
//                }
                field.set(instance, this.factoryBeanInstanceCache.get(autowiredBeanName).getWrappedInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

    }

    private Object instantiateBean(String beanName, WuzzBeanDefinition wuzzBeanDefinition) {
        //1、拿到要实例化的对象的类名
        String className = wuzzBeanDefinition.getBeanClassName();

        //2、反射实例化，得到一个对象
        Object instance = null;
        try {
//            wuzzBeanDefinition.getFactoryBeanName()
            //假设默认就是单例,细节暂且不考虑，先把主线拉通
            if (this.factoryBeanObjectCache.containsKey(className)) {
                instance = this.factoryBeanObjectCache.get(className);
            } else {
                Class<?> clazz = Class.forName(className);
                boolean anAbstract = Modifier.isAbstract(clazz.getModifiers());
                if (anAbstract) {
                    return null;
                }
                instance = clazz.newInstance();
                WuzzAdvisedSupport config = instantionAopConfig(wuzzBeanDefinition);
                //这里的操作包括初始化AOP调用链
                config.setTargetClass(clazz);
                //注入当前类实例
                config.setTarget(instance);
                if (config.pointCutMatch()) {
                    //创建代理工厂，并且获取代理，所以注入到IOC的实例已经被代理了，
                    //这样子后续调用方法才能走AOP的流程
                    instance = createProxy(config).getProxy();
                }
                //
                this.factoryBeanObjectCache.put(className,instance);
                //
                this.factoryBeanObjectCache.put(wuzzBeanDefinition.getFactoryBeanName(),instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return instance;
    }

    private WuzzAdvisedSupport instantionAopConfig(WuzzBeanDefinition beanDefinition) throws Exception {
        WuzzAopConfig config = new WuzzAopConfig();
        config.setPointCut(reader.getConfig().getProperty("pointCut"));
        config.setAspectClass(reader.getConfig().getProperty("aspectClass"));
        config.setAspectBefore(reader.getConfig().getProperty("aspectBefore"));
        config.setAspectAfter(reader.getConfig().getProperty("aspectAfter"));
        config.setAspectAfterThrow(reader.getConfig().getProperty("aspectAfterThrow"));
        config.setAspectAfterThrowingName(reader.getConfig().getProperty("aspectAfterThrowingName"));
        return new WuzzAdvisedSupport(config);
    }

    private WuzzAopProxy createProxy(WuzzAdvisedSupport config) {
        Class targetClass = config.getTargetClass();
        if (targetClass.getInterfaces().length > 0) {
            return new WuzzJdkDynamicAopProxy(config);
        }
        return new WuzzCglibAopProxy(config);
    }

    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }

    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    public Properties getConfig() {
        return this.reader.getConfig();
    }
}
