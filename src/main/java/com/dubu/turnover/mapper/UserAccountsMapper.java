package com.dubu.turnover.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dubu.turnover.core.Mapper;
import com.dubu.turnover.domain.AccountCheckVo;
import com.dubu.turnover.domain.entity.UserAccounts;
import com.github.pagehelper.Page;

/*
 *  @author: smart boy
 *  @date: 2019-03-15
 */
public interface  UserAccountsMapper extends Mapper<UserAccounts>{
	UserAccounts selectByUserId(@Param("userId") Integer userId);
	UserAccounts selectByUsername(@Param("username") String username);
	UserAccounts selectByUserPhone(@Param("phone") String phone);
	UserAccounts selectByUserEmail(@Param("email") String email);
	UserAccounts selectByOpenId(@Param("openId") String openId);
	int updateBalance(@Param("id") Integer id, @Param("balance") BigDecimal balance);
	public int freezeBalance(@Param("id") Integer id, @Param("balance") BigDecimal balance);
	public int unfreezeBalance(@Param("id") Integer id, @Param("balance") BigDecimal balance);
	public int freezeAccount(@Param("id")Integer id,@Param("status")Integer status);
	public int updatePassWord(@Param("id")Integer id,@Param("password")String password);
	public int updateMobile(@Param("id")Integer id,@Param("mobile")String mobile);
	public int userRemarks(@Param("id")Integer id,@Param("remarks")String remarks);
	public List<AccountCheckVo> selectAccountPage(@Param("p") AccountCheckVo check, @Param("page")Page<AccountCheckVo> page);
	public void updateOpenId(@Param("id")Integer id,@Param("openId")String openId,@Param("nickname")String nickname);
	public void updateUserName(@Param("id")Integer id,@Param("username")String username);
	public void updateEmail(@Param("id")Integer id,@Param("mobile")String mobile);
	public void updateHeadImg(@Param("id")Integer id,@Param("headImg")String headImg);
	
}
