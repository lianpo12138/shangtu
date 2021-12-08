package com.dubu.turnover.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来描述  REST CONTROLLER 中的方法， 用户必须登录才能访问该方法， 否则 返回401，无权访问
 * 改注解只能用在 @RestController 的类型的方法，系统将通过拦截器，到HTTP header 中拿 
 * X-Zhao-UserId 的 value 进行验证
 * @author dengsuping
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Login {
	
	/**
	 * API 的名称或者描述
	 * @return Sting
	 */
    String value() default "";
}
