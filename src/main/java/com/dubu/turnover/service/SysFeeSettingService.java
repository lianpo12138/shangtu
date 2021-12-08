package com.dubu.turnover.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dubu.turnover.core.AbstractService;
import com.dubu.turnover.core.ServiceException;
import com.dubu.turnover.domain.entity.SysFeeSetting;
import com.dubu.turnover.mapper.SysFeeSettingMapper;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

/*
 *  @author: smart boy
 *  @date: 2019-04-03
 */
@Service
public class SysFeeSettingService extends AbstractService<SysFeeSetting> {

	@Resource
	private SysFeeSettingMapper sysFeeSettingMapper;

	public SysFeeSetting getFeeByType(Integer feeSubjectType, Integer feeSubject, Integer feeType) {
		return sysFeeSettingMapper.getFeeByType(feeSubjectType, feeSubject, feeType);
	}

	public void updateById(SysFeeSetting sysFeeSetting) {
		if (isTimeOverlap(sysFeeSetting)) {
			throw new ServiceException("费用有效期与其他相同科目费用重复!");
		}
		mapper.updateByPrimaryKeySelective(sysFeeSetting);
	}
	
	public void save(SysFeeSetting sysFeeSetting) {
//		if (isTimeOverlap(sysFeeSetting)) {
//			throw new ServiceException("费用有效期与其他相同科目费用重复!");
//		}
		mapper.insertSelective(sysFeeSetting);
	}

	/**
	 * 
	* @Title: isTimeOverlap 
	* @Description: 判断是否与其他相同科目费用的有效期重叠
	* @param @param sysFeeSetting
	* @param @return
	* @return boolean
	* @throws
	 */
	public boolean isTimeOverlap(SysFeeSetting sysFeeSetting) {
		Condition condition = new Condition(SysFeeSetting.class);
		condition.createCriteria().andEqualTo("feeSubject", sysFeeSetting.getFeeSubject())
				.andEqualTo("feeSubjectType",sysFeeSetting.getFeeSubjectType());
		Example.Criteria criteria = condition.createCriteria();
		criteria.orBetween("startTime", sysFeeSetting.getStartTime(), sysFeeSetting.getEndTime())
		.orBetween("endTime", sysFeeSetting.getStartTime(), sysFeeSetting.getEndTime());
		condition.and(criteria);
		List<SysFeeSetting> list = sysFeeSettingMapper.selectByCondition(condition);
		if (list!=null&&list.size()>0) {
			return true;
		}
		return false;
	}
}
