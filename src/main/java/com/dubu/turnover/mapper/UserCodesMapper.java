package com.dubu.turnover.mapper;

import org.apache.ibatis.annotations.Param;

import com.dubu.turnover.core.Mapper;
import com.dubu.turnover.domain.entity.UserCodes;

/*
 *  @author: smart boy
 *  @date: 2019-03-15
 */
public interface  UserCodesMapper extends Mapper<UserCodes>{
	UserCodes selectByPhone(@Param("phone") String phone);
	
}
