package com.dubu.turnover.configure;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.dubu.turnover.core.Result;
import com.dubu.turnover.core.ResultCode;
import com.dubu.turnover.core.ServiceException;
import com.dubu.turnover.exception.UserException;


/**
 * Spring MVC 配置
 */
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private final Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    //统一异常处理
	@Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add((request, response, handler, e) -> {
            Result result = new Result();
            if (e instanceof UserException) {
            	UserException userException = (UserException)e;
            	result.setCode(ResultCode.FAIL).setMessage(e.getMessage());
            	result.setMessage(e.getMessage());
            	if(userException.getMessage().contains("未登陆")){
            		  logger.info(e.getMessage());
            	}else{
            		  logger.error("请求异常URL:"+request.getRequestURI()+"查询参数"+request.getQueryString());
            		  logger.error(e.getMessage());
            	}
            }
            else if (e instanceof ServiceException) {
            	this.writeLog(request, e);
                result.setCode(((ServiceException) e).getCode()).setMessage(e.getMessage());
            } else if (e instanceof NoHandlerFoundException) {
            	this.writeLog(request, e);
                result.setCode(ResultCode.NOT_FOUND).setMessage("系统操作错误");
            } else if (e instanceof ServletException) {
            	this.writeLog(request, e);
                result.setCode(ResultCode.FAIL).setMessage("系统操作错误");
            } else {
                result.setCode(ResultCode.INTERNAL_SERVER_ERROR).setMessage("系统操作错误");
                String message;
                if (handler instanceof HandlerMethod) {
                    HandlerMethod handlerMethod = (HandlerMethod) handler;
                    message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s",
                            request.getRequestURI(),
                            handlerMethod.getBean().getClass().getName(),
                            handlerMethod.getMethod().getName(),
                            e.getMessage());
                } else {
                    message = e.getMessage();
                }
                this.writeLog(request, e);
            }
            ModelAndView modelAndView=new ModelAndView(new MappingJackson2JsonView());
            Map<String,Object> map = new HashMap<>();
            map.put("code", result.getCode());
            map.put("message",result.getMessage().length()>300?result.getMessage().substring(0, 1024):result.getMessage());
            modelAndView.addAllObjects(map);
            return modelAndView;
        });
    }

	private void writeLog(HttpServletRequest request,Exception e){
		logger.error("请求异常URL:"+request.getRequestURI()+"查询参数"+request.getQueryString());
    	logger.error(e.getMessage());
	}
    //解决跨域问题
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .maxAge(3600)
                .exposedHeaders(String.format(Configurer.HEADER_TOKEN_KEY, "erp"), String.format(Configurer.HEADER_TOKEN_KEY, "app"))
                .allowCredentials(true);
    }
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }
    
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}
}
