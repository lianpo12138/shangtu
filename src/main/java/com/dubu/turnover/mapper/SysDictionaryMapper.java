package com.dubu.turnover.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dubu.turnover.core.Mapper;
import com.dubu.turnover.domain.AccountCheckVo;
import com.dubu.turnover.domain.entity.SysDictionary;

/*
 *  @author: smart boy
 *  @date: 2019-02-28
 */
public interface  SysDictionaryMapper extends Mapper<SysDictionary>{
	
	public List<SysDictionary> getDictList(@Param("dictType")String dictType, @Param("dictPid")String dictPid);

}
