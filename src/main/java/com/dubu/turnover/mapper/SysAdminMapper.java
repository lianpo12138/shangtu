package com.dubu.turnover.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dubu.turnover.core.Mapper;
import com.dubu.turnover.domain.entity.SysAdmin;

/*
 *  @author: smart boy
 *  @date: 2019-02-28
 */
public interface  SysAdminMapper extends Mapper<SysAdmin>{
	
	public List<SysAdmin> getAdminList(@Param("deptId")Integer deptId);
	
	
	public List<SysAdmin> getAdminByDept(@Param("deptId")Integer deptId,@Param("userName")String userName);
	
	public List<SysAdmin> getAdminByRole(@Param("roleId")Integer roleId);
}
