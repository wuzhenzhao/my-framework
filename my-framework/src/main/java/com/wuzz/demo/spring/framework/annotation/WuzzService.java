package com.wuzz.demo.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * @description:
 * @author: Wuzhenzhao@hikvision.com.cn
 * @time 2020/3/17 18:11
 * @since 1.0
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WuzzService {
	String value() default "";
}
