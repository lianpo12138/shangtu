package com.dubu.turnover.mapper;

import com.dubu.turnover.domain.entity.BusAssets;

import org.apache.ibatis.annotations.Param;

import com.dubu.turnover.core.Mapper;

/*
 *  @author: smart boy
 *  @date: 2021-03-13
 */
public interface  BusAssetsMapper extends Mapper<BusAssets>{
   
	public void updateStatus(@Param("id")Integer id,@Param("status")Integer status,@Param("updateBy")String updateBy);
	
	public void updateAssetsAudit(@Param("id")Integer id,@Param("deptId")Integer deptId,@Param("deptName")String deptName,
			@Param("userId")Integer userId,@Param("userName")String userName,
			@Param("useUserName")String useUserName,@Param("depositAddress")String depositAddress
			,@Param("remarks")String remarks);
	
	
	public void updateTransfer(@Param("id")Integer id,@Param("isTransfer")Integer isTransfer,@Param("updateBy")String updateBy);
	
	
	public void updateApply(@Param("id")Integer id,@Param("netWorth")String netWorth,
			@Param("decrease")String decrease,@Param("expireYear")String expireYear);
	
	public void updateAdress(@Param("id")Integer id,@Param("address")String address);
}
