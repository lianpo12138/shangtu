package com.dubu.turnover.configure;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import com.dubu.turnover.domain.entity.SysFeeSetting;
import com.dubu.turnover.mapper.SysFeeSettingMapper;
import com.dubu.turnover.utils.SpringApplicationUtils;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

/**
 * Created by DELL on 2019/4/3.
 */
@Component
public class SysFeeSettingConfigurer {
	private static final Log logger = LogFactory.getLog(SysFeeSettingConfigurer.class);
	private LoadingCache<String, SysFeeSetting> studentCache;
	private Object lock = new Object();

	public LoadingCache<String, SysFeeSetting> getFeeSet() {
		synchronized (lock) {
			if (studentCache == null) {
				// 缓存接口这里是LoadingCache，LoadingCache在缓存项不存在时可以自动加载缓存
				studentCache
				// CacheBuilder的构造函数是私有的，只能通过其静态方法newBuilder()来获得CacheBuilder的实例
				= CacheBuilder.newBuilder()
				// 设置并发级别为8，并发级别是指可以同时写缓存的线程数
						.concurrencyLevel(8)
						// 设置写缓存后8秒钟过期
						.expireAfterWrite(8, TimeUnit.HOURS)
						// 设置缓存容器的初始容量为10
						.initialCapacity(10)
						// 设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
						.maximumSize(100)
						// 设置要统计缓存的命中率
						.recordStats()
						// 设置缓存的移除通知
						.removalListener(new RemovalListener<Object, Object>() {
							@Override
							public void onRemoval(RemovalNotification<Object, Object> notification) {
								logger.info(notification.getKey() + " was removed, cause is "
										+ notification.getCause());
							}
						}).build(// build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
								new CacheLoader<String, SysFeeSetting>() {
									@Override
									public SysFeeSetting load(String key) throws Exception {
										String[] subjectType = key.split("_");
										// 获取到结标时间已过，尚未结标的非一口价的藏品
										Condition condition = new Condition(SysFeeSetting.class);
										Example.Criteria criteria = condition.createCriteria();
										criteria.andLessThan("startTime", new Date());
										criteria.andGreaterThan("endTime", new Date());
										criteria.andEqualTo("status", 1);
										criteria.andEqualTo("feeSubjectType", subjectType[0]);
										criteria.andEqualTo("feeSubject", subjectType[1]);

										SysFeeSettingMapper sysFeeSettingMapper = SpringApplicationUtils
												.getBean(SysFeeSettingMapper.class);
										List<SysFeeSetting> sysFeeSettingList = sysFeeSettingMapper
												.selectByCondition(condition);
										if (sysFeeSettingList != null && sysFeeSettingList.size() > 0) {
											return sysFeeSettingList.get(0);
										}

										return null;
									}
								});
			}
			return studentCache;
		}
	}

}
