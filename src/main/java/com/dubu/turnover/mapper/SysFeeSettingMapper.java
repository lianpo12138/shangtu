package com.dubu.turnover.mapper;

import org.apache.ibatis.annotations.Param;

import com.dubu.turnover.core.Mapper;
import com.dubu.turnover.domain.entity.SysFeeSetting;

/*
 *  @author: smart boy
 *  @date: 2019-04-03
 */
public interface  SysFeeSettingMapper extends Mapper<SysFeeSetting>{
   public SysFeeSetting getFeeByType(@Param("feeSubjectType")Integer feeSubjectType,@Param("feeSubject") Integer feeSubject,@Param("feeType") Integer feeType);
}
