package com.dubu.turnover.component.aspect;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


import com.alibaba.fastjson.JSON;
import com.dubu.turnover.annotation.Log;
import com.dubu.turnover.annotation.NoLogin;
import com.dubu.turnover.annotation.RequiresPermissions;
import com.dubu.turnover.component.ThreadRequestContext;
import com.dubu.turnover.component.http.HttpAPIService;
import com.dubu.turnover.component.redis.RedisClient;
import com.dubu.turnover.configure.Configurer;
import com.dubu.turnover.core.Mapper;
import com.dubu.turnover.core.ResultCode;
import com.dubu.turnover.core.ResultGenerator;
import com.dubu.turnover.domain.UserInfo;
import com.dubu.turnover.domain.entity.SysAdmin;
import com.dubu.turnover.domain.entity.UserAccounts;
import com.dubu.turnover.service.SysRoleService;
import com.dubu.turnover.service.UserAccountsService;
import com.dubu.turnover.utils.CommonUtil;
import com.dubu.turnover.utils.CompareUtils;
import com.dubu.turnover.utils.Hmac;
import com.dubu.turnover.utils.JsonUtils;
import com.dubu.turnover.utils.ReflectUtil;
import com.dubu.turnover.utils.SpringApplicationUtils;

@Component
@Aspect
public class AuthComponent {

    private static final Logger logger = LoggerFactory.getLogger(AuthComponent.class);
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private HttpAPIService httpAPIService;
	@Autowired
	private Environment environment;
    @Autowired
    private AuthConfig authConfig;
    @Autowired
    private UserAccountsService userAccountsService;    
    @Autowired
    private  SysRoleService sysRoleService;
    
    @Pointcut("execution(public * com.dubu.turnover.controller.app.*.*(..))")
    public void app() {
    }

    @Pointcut("execution(public * com.dubu.turnover.controller.erp.*.*(..))")
    public void erp() {
    }

      @Around("app()")
       public Object aroundApp(ProceedingJoinPoint joinPoint) throws Throwable {
           HttpServletRequest request = CommonUtil.getRequest();
            this.accessLog(request);
           NoLogin annotation = CommonUtil.getAnnotation(joinPoint, NoLogin.class);
           //不需要登录就可访问
           if (null != annotation) {
        	   UserInfo userInfo  =new UserInfo();
        	   if(!StringUtils.isEmpty(request.getHeader("X-Test-UserId"))){
        		   userInfo.setId(Long.parseLong(request.getHeader("X-Test-UserId")));   
        		   ThreadRequestContext.set(userInfo);
        	   }
        	   Object object =  joinPoint.proceed();
               ThreadRequestContext.remove();//考虑到tomcat每次请求是线程池，则此Map会越来越大，请求结束后将鲜橙清空回收
               return object;
           }
//           String testUserId = request.getHeader("X-Test-UserId");
//           if("test".equals(environment.getActiveProfiles()[0]) &&  testUserId!= null){
//        	    UserInfo userInfo  =new UserInfo();
//                userInfo.setId(Long.parseLong(testUserId));
//                userInfo.setNickname("test");
//                ThreadRequestContext.set(userInfo);
//        	   return joinPoint.proceed();
//           }
            //动态验证
            if(!this.getAppLogin()){
            	return ResultGenerator.error(ResultCode.UNAUTHORIZED, "请登录");
            }
           Object object =  joinPoint.proceed();
           ThreadRequestContext.remove();//考虑到tomcat每次请求是线程池，则此Map会越来越大，请求结束后将鲜橙清空回收
           return object;
       }

    //设置当前登录用户信息
       public void setUserInfo(String userId){
    	   UserAccounts account=userAccountsService.selectByUserId(Integer.valueOf(userId));
           UserInfo userInfo  =new UserInfo();
           userInfo.setId(Long.parseLong(userId));
           userInfo.setNickname(account.getNikeName());
           ThreadRequestContext.set(userInfo);
       }


    @Around("erp()")
    public Object aroundErp(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = CommonUtil.getRequest();
        this.accessLog(request);
        //TODO
        String testUserId = request.getHeader("X-Test-UserId");
        if("test".equals(environment.getActiveProfiles()[0]) &&  testUserId!= null){
     	    UserInfo userInfo  =new UserInfo();
             userInfo.setId(Long.parseLong(testUserId));
             userInfo.setNickname("test");
             List<Integer> depts=sysRoleService.getDeptsIdsByUserId(Integer.valueOf(testUserId));
             userInfo.setDeptIdList(depts);
             List<Integer> ids=sysRoleService.getRolesIdsByUserId(Integer.valueOf(testUserId));
             userInfo.setRoleIdList(ids);
             ThreadRequestContext.set(userInfo);
     	   return joinPoint.proceed();
        }
        NoLogin annotation = CommonUtil.getAnnotation(joinPoint, NoLogin.class);
        //需要登录而没登录
        if (null == annotation && !this.getLogin()) {
            return ResultGenerator.error(ResultCode.UNAUTHORIZED, "未登录");
        }
        UserInfo userInfo = ThreadRequestContext.current();
        if(annotation!=null){//不需要登录
        	Object object =  joinPoint.proceed();
            ThreadRequestContext.remove();//考虑到tomcat每次请求是线程池，则此Map会越来越大，请求结束后将鲜橙清空回收
            return object;
        }
        RequiresPermissions requiresPermissions = CommonUtil.getAnnotation(joinPoint, RequiresPermissions.class);
        if (requiresPermissions != null) {
            //校验权限
            Set<String> permissions =   redisClient.get(String.format(Configurer.ERP_PERMISSION_CACHE, userInfo.getId()));
            if (permissions == null || !this.match(Arrays.asList(requiresPermissions.value()), permissions)) {
                return ResultGenerator.error(ResultCode.UNAUTHORIZED, "用户未操作权限");
            }
        }
        //操作日志记录
        this.log(joinPoint);
        
        Object object =  joinPoint.proceed();
        ThreadRequestContext.remove();//考虑到tomcat每次请求是线程池，则此Map会越来越大，请求结束后将鲜橙清空回收
        return object;
    }

    @SuppressWarnings("rawtypes")
	private void log(ProceedingJoinPoint joinPoint){
    	Log log = CommonUtil.getAnnotation(joinPoint, Log.class);
    	if(log!=null){
    		Class cls = log.object();
    		for(Object obj : joinPoint.getArgs()){
    			if(obj.getClass() == cls){
    				try{
    					Object modifyObject = cls.cast(obj);
    					Mapper mapper = SpringApplicationUtils.getBean(CompareUtils.toLowerCaseFirstOne(cls.getSimpleName())+"Mapper");
    					Object id =ReflectUtil.getFieldValue(modifyObject, "id");
    					if(id!=null){
    						Object beforeObject = mapper.selectByPrimaryKey(id);
    						this.write(id,beforeObject, modifyObject,log.type());
    					}
    				}catch(Exception e){
    				}
    			}
    		}
    	}
    }
    @Async
    private void write(Object id,Object source,Object target,String type){
    	try{

        	//过滤空值
        	Map<String, Object> map = CompareUtils.getModifyContent(source, target);
        	Map<String, Object> beforeRevision = new HashMap<String, Object>();
    		Map<String, Object> after_revision = new HashMap<String, Object>();
        	for(Entry<String, Object> entry : map.entrySet()){
        		if(entry.getValue()!=null){
        			beforeRevision.put(entry.getKey(), ReflectUtil.getFieldValue(source,entry.getKey()));
        			after_revision.put(entry.getKey(), ReflectUtil.getFieldValue(target,entry.getKey()));
        		}
    		}
        	if("db".equals(type)){
        		if(!beforeRevision.isEmpty() ||!after_revision.isEmpty()){
        			UserInfo userInfo = ThreadRequestContext.current();
        		}
        	}else{
        		logger.info("modify before->"+JsonUtils.toJson(beforeRevision)+"modify after->"+JsonUtils.toJson(after_revision));
        	}
    	}catch(Exception e){
    	}
    }
    private boolean getLogin(){
    	//认证通过，设置上下文用户登录信息
        String token = CommonUtil.getRequest().getHeader(String.format(Configurer.HEADER_TOKEN_KEY, "erp"));
        if (!StringUtils.isEmpty(token)) {
            SysAdmin admins = redisClient.get(String.format(Configurer.ERP_LOGIN_CACHE, token));
            if(admins!=null){
            	UserInfo userInfo = new UserInfo();
            	userInfo.setId(admins.getId().longValue());
            	userInfo.setNickname(admins.getUserName());
                List<Integer> depts=sysRoleService.getDeptsIdsByUserId(admins.getId());
                userInfo.setDeptIdList(depts);
                List<Integer> ids=sysRoleService.getRolesIdsByUserId(admins.getId());
                userInfo.setRoleIdList(ids);
            	ThreadRequestContext.set(userInfo);
            	return true;
            }
        }
        return false;
    }
    private void accessLog(HttpServletRequest request){
        logger.debug("请求接口：{}，请求IP：{}，请求参数：{}",
                request.getRequestURI(), CommonUtil.getClientIp(), JSON.toJSONString(request.getParameterMap()));
    }
    private boolean match(List<String> requestPermisssions , Set<String> permissionAll){
        for(String s : requestPermisssions){
            Iterator<String> set =  permissionAll.iterator();
            while(set.hasNext()){
                if(set.next().equals(s)){
                    return true;
                }
            }
        }
        return false;
    }
    private boolean getAppLogin(){
    	//认证通过，设置上下文用户登录信息
        String token = CommonUtil.getRequest().getHeader("token");
        if (!StringUtils.isEmpty(token)) {
        	UserAccounts admins = redisClient.get(String.format(Configurer.HEADER_TOEKN_USERINFO, token));
            if(admins!=null){
            	UserInfo userInfo = new UserInfo();
            	userInfo.setId(admins.getUserId().longValue());
            	userInfo.setNickname(admins.getUserName());
            	ThreadRequestContext.set(userInfo);
            	return true;
            }
        }
        return false;
    }
}
