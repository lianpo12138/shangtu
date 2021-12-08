package com.dubu.turnover.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 不需要登录，不需要授权
 * <p>
 * Date: 2018/1/26
 * Time: 18:10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Guest {
    /**
     * API 的名称或者描述
     *
     * @return Sting
     */
    String value() default "";
}
