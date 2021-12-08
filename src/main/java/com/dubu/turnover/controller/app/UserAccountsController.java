package com.dubu.turnover.controller.app;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import com.dubu.turnover.annotation.NoLogin;
import com.dubu.turnover.component.ThreadRequestContext;
import com.dubu.turnover.component.redis.RedisClient;
import com.dubu.turnover.configure.Configurer;
import com.dubu.turnover.core.Result;
import com.dubu.turnover.core.ResultGenerator;
import com.dubu.turnover.core.ServiceException;
import com.dubu.turnover.domain.UserAuthVo;
import com.dubu.turnover.domain.UserInfo;
import com.dubu.turnover.domain.entity.UserAccounts;
import com.dubu.turnover.domain.enums.PushEnum.PushChannels;
import com.dubu.turnover.domain.enums.PushEnum.PushTypes;
import com.dubu.turnover.domain.rest.OSInfo;
import com.dubu.turnover.service.UserAccountsService;
import com.dubu.turnover.utils.RequestUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.jws.soap.SOAPBinding;
import java.util.List;

/*
 *  @author: smart boy
 *  @date: 2019-03-15
 */
@RestController
@CrossOrigin
@RequestMapping("/useraccounts/bind")
@Api(value = "个人中心",tags="个人中心")
public class UserAccountsController {

    @Resource
	private UserAccountsService userAccountsService;
    
	@Autowired
	private RedisClient redisClient;
	
    /**
     * 修改用户名称
     */
    @RequestMapping(value = "/updateUserName", method = RequestMethod.GET)
    @ApiOperation(value = "修改用户名称",notes = "修改用户名称")
    public Result updateUserName(@RequestParam(required = true) String  userName) {
		UserInfo userInfo = ThreadRequestContext.current();
		UserAccounts user=userAccountsService.selectByUsername(userName);
		if(user!=null){
			userAccountsService.updateUserName(userInfo.getId().intValue(), userName);
	        return ResultGenerator.success("");
		}
	    return ResultGenerator.error("该账号已经被使用");
    }
    
    /**
     * 修改头像
     */
    @RequestMapping(value = "/updateHeadImg", method = RequestMethod.GET)
    @ApiOperation(value = "修改用户名称",notes = "修改用户名称")
    public Result updateHeadImg(@RequestParam(required = true) String  headImag) {
		UserInfo userInfo = ThreadRequestContext.current();
		userAccountsService.updateHeadImg(userInfo.getId().intValue(), headImag);
	    return ResultGenerator.success("");
    }
    
    /**
     * 设置密码
     */
    @RequestMapping(value = "/setPassword", method = RequestMethod.GET)
    @ApiOperation(value = "修改用户名称",notes = "修改用户名称")
    public Result setPassword(@RequestParam(required = true) String  password) {
		UserInfo userInfo = ThreadRequestContext.current();
		userAccountsService.updatePassWord(userInfo.getId().intValue(), password);
	    return ResultGenerator.success("");
    }

    /**
     * 绑定手机
     */
    @RequestMapping(value = "/bindPhone", method = RequestMethod.POST)
    @ApiOperation(value = "绑定手机",notes = "必填参数用userId，phone，validationCode")
    public Result bindPhone(@RequestBody UserAuthVo userAuthVo) {
    	UserAccounts user=userAccountsService.selectByUserPhone(userAuthVo.getPhone());
    	if(user==null){
    		String redisVCode = redisClient.get(Configurer.PRI_ACC_VCODE_SMS_KEY + userAuthVo.getPhone());
    		if(redisVCode==null || !redisVCode.equals(userAuthVo.getValidationCode())){
    			throw new ServiceException("验证码错误");
    		}
    		userAccountsService.updateMobile(userAuthVo.getUserId(), userAuthVo.getPhone());
            return ResultGenerator.success(user);
    	}
        return ResultGenerator.error("该账号已经被使用");
    }
    /**
     * 绑定邮箱
     */
    @RequestMapping(value = "/bindEmail", method = RequestMethod.GET)
    @ApiOperation(value = "点击邮箱链接自动绑定邮箱",notes = "点击邮箱链接自动绑定邮箱")
    public Result bindEmail(@RequestParam(required = true)  Integer userId,@RequestParam(required = true)  String email,@RequestParam(required = true) String validationCode) {
    	UserAccounts user=userAccountsService.selectByUserEmail(email);
    	if(user==null){
    		String redisVCode = redisClient.get(Configurer.PRI_ACC_VCODE_SMS_KEY + email);
    		if(redisVCode==null || !redisVCode.equals(validationCode)){
    			throw new ServiceException("验证码错误");
    		}
    		userAccountsService.updateEmail(userId, email);
            return ResultGenerator.success(user);
    	}
        return ResultGenerator.error("该账号已经被使用");
    }
    
    /**
     * 解除绑定
     */
    @RequestMapping(value = "/delBind", method = RequestMethod.GET)
    @ApiOperation(value = "解除绑定",notes = "解除绑定type1解除手机2解除邮箱3解除qq4解除微信")
    public Result bindEmail(@RequestParam(required = true)  String  type) {

        return ResultGenerator.error("该账号已经被使用");
    }
    
    /**
     * 修改用户信息
     */
    @RequestMapping(value = "/updateUserBaseInfo", method = RequestMethod.GET)
    @ApiOperation(value = "修改用户信息",notes = "解除绑定type1解除手机2解除邮箱3解除qq4解除微信")
    public Result updateUserBaseInfo(@RequestParam(required = true)  String  type) {

        return ResultGenerator.error("该账号已经被使用");
    }
}

