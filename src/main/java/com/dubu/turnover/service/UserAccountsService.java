package com.dubu.turnover.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import lombok.Synchronized;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;

import tk.mybatis.mapper.entity.Condition;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonResponse;
import com.dubu.turnover.component.ThreadRequestContext;
import com.dubu.turnover.component.config.SysConfig;
import com.dubu.turnover.component.redis.RedisClient;
import com.dubu.turnover.configure.Configurer;
import com.dubu.turnover.constants.Constants;
import com.dubu.turnover.core.AbstractService;
import com.dubu.turnover.core.Result;
import com.dubu.turnover.core.ResultCode;
import com.dubu.turnover.core.ResultGenerator;
import com.dubu.turnover.core.ServiceException;
import com.dubu.turnover.domain.AccountCheckVo;
import com.dubu.turnover.domain.entity.UserAccounts;
import com.dubu.turnover.domain.enums.EncryptEnum;
import com.dubu.turnover.exception.UserException;
import com.dubu.turnover.mapper.UserAccountsMapper;
import com.dubu.turnover.utils.EmailUtil;
import com.dubu.turnover.utils.HttpClientUtils;
import com.dubu.turnover.utils.JsonUtils;
import com.dubu.turnover.utils.Md5Utils;
import com.dubu.turnover.utils.SmsUtils;
import com.github.pagehelper.Page;

/*
 *  @author: smart boy
 *  @date: 2019-03-15
 */
@Service
public class UserAccountsService extends AbstractService<UserAccounts> {
	private static final Log logger = LogFactory.getLog(UserAccountsService.class);

	@Autowired
	private RedisClient redisClient;

	@Autowired
	private StringRedisTemplate srt;

	@Resource
	private UserAuthService userAuthService;

	@Resource
	private UserAccountsMapper userAccountsMapper;

	@Resource
	private UserBankService userBankService;
	
    @Autowired
    SysConfig sysConfig;

	@Value("classpath:static/bank.json")
	private org.springframework.core.io.Resource bankFile;
    
	public UserAccounts selectByUserId(Integer userId){
		return userAccountsMapper.selectByUserId(userId);
	}
	
	public UserAccounts selectByUsername(String username){
		return userAccountsMapper.selectByUsername(username);
	}
	public UserAccounts selectByPhone(String phone){
		return userAccountsMapper.selectByUserPhone(phone);
	}
	
	public UserAccounts selectByEmail(String phone){
		return userAccountsMapper.selectByUserEmail(phone);
	}

	
	@Transactional
	public UserAccounts insertAccount(UserAccounts account)  {
		 account.setStatus("1");
		 account.setTotalBalance(BigDecimal.ZERO);
		 account.setFreezeBalance(BigDecimal.ZERO);
		 account.setCreateTime(new Date());
		 userAccountsMapper.insert(account);
		 return account;
	}

	/**
	 * 更新余额
	 * @return
	 */
	public int updateBalance(Integer id, BigDecimal balance){
		return userAccountsMapper.updateBalance(id,balance);
	}
	/**
	 * 冻结资金
	 * @return
	 */
	public int freezeBalance(Integer id, BigDecimal balance){
		return userAccountsMapper.freezeBalance(id,balance);
	}
	/**
	 * 解冻资金
	 * @return
	 */
	public int unfreezeBalance(Integer id, BigDecimal balance){
		return userAccountsMapper.unfreezeBalance(id,balance);
	}


	/**
	 * 手机发送验证码
	 * @param mobile
	 * @return
	 * @throws Exception 
	 */
	public void priSmsSend(String phone) throws UserException{
		Result r = checkFreq(phone);
		if(r.getCode()==200){
			String validateCode = gerSmsCode();
			Map<String, String> param = new HashMap<String, String>();
			param.put("Mobile", phone);
			param.put("Token", Md5Utils.md5(phone, Constants.SERVERKEY).toUpperCase());
			String result = HttpClientUtils.postJson2(sysConfig.getSmsUrl(),
					com.alibaba.fastjson.JSON.toJSONString(param));
			JSONObject jsonResult = JSONObject.fromObject(result);
			if (jsonResult.getString("Status").toUpperCase().equals("TRUE")) {
				saveCode(phone);
				redisClient.put(Configurer.PRI_ACC_VCODE_SMS_KEY + phone, validateCode, Configurer.SMS_EXPIRE_TIME);
			} else {
				throw new ServiceException(jsonResult.getString("Error"));
			}

		}else{
			throw new ServiceException(r.getMessage());
		}
	}
	/**
	 * 邮箱发送验证码
	 * @param mobile
	 * @return
	 */
	public void priEmailSend(String email,String type,String url){
		Result r = checkFreq(email);
		if(r.getCode()==200){
			String validateCode = gerSmsCode();
			//EmailUtil.send_email(email,validateCode,type,url);
			saveCode(email);
			redisClient.put(Configurer.PRI_ACC_VCODE_SMS_KEY + email, validateCode, Configurer.SMS_EXPIRE_TIME);
		}else{
			throw new ServiceException(r.getMessage());
		}
	}

	/**
	 * 校验验证码
	 * @param mobile
	 * @return
	 */
	public void checkValidateCode(String mobile,String validateCode){
		String redisVCode = redisClient.get(Configurer.PRI_ACC_VCODE_SMS_KEY + mobile);
		if(redisVCode==null || !redisVCode.equals(validateCode)){
			throw new ServiceException("验证码错误");
		}
	}

	/**
	 * 设置/修改支付密码
	 * @param userAccounts
	 * @return
	 */
    @Transactional
	public void savePayPwd(UserAccounts userAccounts){

	}

	/**
	 * 校验支付密码
	 * @param userAccounts
	 * @param password
	 * @return
	 */
	public void checkPayPwd(UserAccounts userAccounts,String password){

	}


	/**
	 * 密码是否设置
	 */
	public boolean isSetPayPassword(Integer userId){
		UserAccounts ownAccount = userAccountsMapper.selectByUserId(userId);
		if(ownAccount==null){
			throw new ServiceException("账户不存在");
		}
		if(!StringUtils.hasText(ownAccount.getPassword())){
			return false;
		}
		return true;
	}
	
	/**
	 * 查询用户资金账户
	 */
	public UserAccounts getUserAccount(Integer userId){
		UserAccounts ownAccount = userAccountsMapper.selectByUserId(userId);
		if(ownAccount==null){
			throw new ServiceException("账户不存在");
		}
		return ownAccount;
	}
	
	
	/**
	 * 查询用户资金账户
	 */
	public UserAccounts selectByUserPhone(String phone){
		UserAccounts ownAccount = userAccountsMapper.selectByUserPhone(phone);
		return ownAccount;
	}
	
	/**
	 * 查询用户资金账户
	 */
	public UserAccounts selectByUserEmail(String phone){
		UserAccounts ownAccount = userAccountsMapper.selectByUserEmail(phone);
		return ownAccount;
	}

	/**
	 * 校验短信验证码频率
	 *
	 * @param mobile
	 * @return
	 */
	private Result checkFreq(String mobile) {
//		if (!DetectUtil.isMobileNum(mobile)) {
//			return ResultGenerator.error("手机号码格式不正确");
//		}
		Integer minNum = redisClient.get(Configurer.PRI_ACC_SMS_MIN_KEY + mobile);
		Integer hourNum = redisClient.get(Configurer.PRI_ACC_SMS_HOUR_KEY + mobile);
		Integer dayNum = redisClient.get(Configurer.PRI_ACC_SMS_DAY_KEY + mobile);
		if (minNum == null) {
			if (hourNum == null || hourNum <= Configurer.HOUR_FREQ) {
				if (dayNum == null || dayNum <= Configurer.DAY_FREQ) {

				} else {
					return ResultGenerator.error("短信验证码发送频繁");
				}
			} else {
				return ResultGenerator.error("短信验证码发送频繁");
			}
		} else {
			return ResultGenerator.error("短信验证码发送频繁");
		}
		return ResultGenerator.success();
	}

	/**
	 * 保存发送频率次数
	 *
	 * @param mobile
	 */
	private void saveCode(String mobile) {
		Integer minNum = redisClient.get(Configurer.PRI_ACC_SMS_MIN_KEY + mobile);
		Integer hourNum = redisClient.get(Configurer.PRI_ACC_SMS_HOUR_KEY + mobile);
		Integer dayNum = redisClient.get(Configurer.PRI_ACC_SMS_DAY_KEY + mobile);
		if (minNum == null) {
			redisClient.put(Configurer.PRI_ACC_SMS_MIN_KEY + mobile, 1, 60L);
		}
		if (hourNum == null) {
			redisClient.put(Configurer.PRI_ACC_SMS_HOUR_KEY + mobile, 1, 60 * 60L);
		} else {
			redisClient.put(Configurer.PRI_ACC_SMS_HOUR_KEY + mobile, hourNum + 1,
					srt.getExpire((Configurer.PRI_ACC_SMS_HOUR_KEY + mobile), TimeUnit.SECONDS).longValue());
		}

		if (dayNum == null) {
			redisClient.put(Configurer.PRI_ACC_SMS_DAY_KEY + mobile, 1, 24 * 60 * 60L);
		} else {
			redisClient.put(Configurer.PRI_ACC_SMS_DAY_KEY + mobile, dayNum + 1,
					srt.getExpire((Configurer.PRI_ACC_SMS_DAY_KEY + mobile), TimeUnit.SECONDS).longValue());
		}

	}

	/**
	 * 生成6位数验证码
	 *
	 * @return
	 */
	private String gerSmsCode() {
		return String.valueOf(RandomUtils.nextInt(100000, 999999));
	}
	
	

	/**
	 * 检查账号状态
	 */
	public void checkAccountStatus(Integer userId){
		if(userId==null || userId==0){
			throw new ServiceException("用户id不能为空");
		}
		UserAccounts account=userAccountsMapper.selectByUserId(userId);
		if(account==null){
			 throw new ServiceException("账号不存在");
		}
		if("2".equals(account.getStatus()) || "3".equals(account.getStatus())){
			 throw new ServiceException(ResultCode.FREZEE_ACOUNT_ERROR,"您的账号已冻结，联系客服请拨021-23099900");
		}
	}
    
	public int updatePassWord(Integer id,String password){
	   return 0;
	}
    
	public UserAccounts selectByOpenId(String openId){
		return userAccountsMapper.selectByOpenId(openId);
	}
	public void updateOpenId(Integer id,String openId,String nickname){
		 userAccountsMapper.updateOpenId(id, openId,nickname);
	}
	
	public void updateUserName(Integer id,String username){
		 userAccountsMapper.updateUserName(id, username);
	}
	
	public void updateMobile(Integer id,String mobile){
		 userAccountsMapper.updateMobile(id, mobile);
	}
	
	public void updateEmail(Integer id,String mobile){
		 userAccountsMapper.updateEmail(id, mobile);
	}
	public void updateHeadImg(Integer id,String mobile){
		 userAccountsMapper.updateHeadImg(id, mobile);
	}
}
