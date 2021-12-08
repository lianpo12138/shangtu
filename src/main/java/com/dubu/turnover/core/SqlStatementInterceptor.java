package com.dubu.turnover.core;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.log4j.Logger;

import com.dubu.turnover.component.ThreadRequestContext;
import com.dubu.turnover.domain.UserInfo;

@Intercepts({
        @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class SqlStatementInterceptor implements Interceptor{

    private static Logger logger = Logger.getLogger(SqlStatementInterceptor.class);

    @SuppressWarnings("unused")
    private Properties properties;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
    	UserInfo userInfo = ThreadRequestContext.current();
    	MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
    	Object object = invocation.getArgs()[1];
    	SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        if (SqlCommandType.INSERT.equals(sqlCommandType)) {
        	if(object instanceof HashMap){
        		HashMap map = (HashMap)object;
        		for (Object key : map.keySet()) { 
        			if(map.get(key) instanceof ArrayList){
        				ArrayList list = (ArrayList)map.get(key);
        				for(Object obj : list){
        					try{
        						this.setNameAndTime(userInfo, obj, 0);
        					}catch(Exception e){
        						//忽略
        					}
        				}
        			}
        			} 
        		
        	}else{
        		try{
        			this.setNameAndTime(userInfo, object, 0);
        		}catch(Exception e){
					//忽略
				}
        	}
           
        }else if (SqlCommandType.UPDATE.equals(sqlCommandType)){
        	if(object instanceof HashMap){
        		HashMap map = (HashMap)object;
        		for (Object key : map.keySet()) { 
        			if(map.get(key) instanceof ArrayList){
        				ArrayList list = (ArrayList)map.get(key);
        				for(Object obj : list){
        					try{
        						this.setNameAndTime(userInfo, obj, 1);
        					}catch(Exception e){
        						//忽略
        					}
        				}
        			}
        			} 
        		
        	}else{
        		try{
        			this.setNameAndTime(userInfo, object, 1);
        		}catch(Exception e){
					//忽略
				}
        	}
        }
        return invocation.proceed();
    }

    private void setNameAndTime(UserInfo userInfo,Object obj,int type) throws Exception{
    	if(type==0 && userInfo!=null){
    		Field createTime = obj.getClass().getDeclaredField("createTime");
    		createTime.setAccessible(true);
    		createTime.set(obj, new Date());

    		Field createBy = obj.getClass().getDeclaredField("createBy");
    		createBy.setAccessible(true);
    		createBy.set(obj, userInfo.getNickname());

    		Field updateTime = obj.getClass().getDeclaredField("updateTime");
    		updateTime.setAccessible(true);
    		updateTime.set(obj, null);

    		Field updateBy = obj.getClass().getDeclaredField("updateBy");
    		updateBy.setAccessible(true);
    		updateBy.set(obj, null);
    	}else if(type==1 && userInfo!=null){
    		Field createTime = obj.getClass().getDeclaredField("createTime");
			createTime.setAccessible(true);
			createTime.set(obj, null);
	
			Field createBy = obj.getClass().getDeclaredField("createBy");
			createBy.setAccessible(true);
			createBy.set(obj, null);
	
			Field updateTime = obj.getClass().getDeclaredField("updateTime");
			updateTime.setAccessible(true);
			updateTime.set(obj, new Date());
	
			Field updateBy = obj.getClass().getDeclaredField("updateBy");
			updateBy.setAccessible(true);
			updateBy.set(obj, userInfo.getNickname());
		}else{
			Field createTime = obj.getClass().getDeclaredField("createTime");
			createTime.setAccessible(true);
			createTime.set(obj, new Date());
	
			Field createBy = obj.getClass().getDeclaredField("createBy");
			createBy.setAccessible(true);
			createBy.set(obj, "TASK");
	
			Field updateTime = obj.getClass().getDeclaredField("updateTime");
			updateTime.setAccessible(true);
			updateTime.set(obj, new Date());
	
			Field updateBy = obj.getClass().getDeclaredField("updateBy");
			updateBy.setAccessible(true);
			updateBy.set(obj, "TASK");
		}
    }
    private static String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }

        }
        return value;
    }

    @Override
    public Object plugin(Object arg0) {
        return Plugin.wrap(arg0, this);
    }

    @Override
    public void setProperties(Properties arg0) {
        this.properties = arg0;
    }

}