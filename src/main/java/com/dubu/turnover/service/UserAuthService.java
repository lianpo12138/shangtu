package com.dubu.turnover.service;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dubu.turnover.component.aspect.AuthConfig;
import com.dubu.turnover.component.http.HttpUtil;
import com.dubu.turnover.component.redis.RedisClient;
import com.dubu.turnover.configure.Configurer;
import com.dubu.turnover.controller.app.UserAuthController;
import com.dubu.turnover.core.ResultGenerator;
import com.dubu.turnover.core.ServiceException;
import com.dubu.turnover.domain.UserAuthVo;
import com.dubu.turnover.domain.UserPwdInfo;
import com.dubu.turnover.domain.entity.SysAdmin;
import com.dubu.turnover.domain.entity.UserAccounts;
import com.dubu.turnover.domain.enums.EncryptEnum;
import com.dubu.turnover.domain.rest.OSInfo;
import com.dubu.turnover.exception.UserException;
import com.dubu.turnover.utils.CommonUtil;
import com.dubu.turnover.utils.HttpClientUtils;
import com.dubu.turnover.utils.JsonUtils;
import com.dubu.turnover.utils.RequestUtils;
import com.dubu.turnover.utils.StringUtil;
import com.dubu.turnover.utils.WxUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Service
public class UserAuthService {
    private static Logger LOG = LoggerFactory.getLogger(UserAuthService.class);


    @Autowired
    @Lazy
    private UserAccountsService userAccountsService;

	@Autowired
	private RedisClient redisClient;


    /**
     * ??????????????????OsInfo
     *
     * @return
     */
    private Map<String, String> getOsInfo() {
        OSInfo info = RequestUtils.getOSInfo();
        Map<String, String> osInfo = new HashMap<>();
        osInfo.put("userId", info.getUserId() + "");
        osInfo.put("os", info.getOs().name());
        osInfo.put("osVersion", info.getOsVersion());
        osInfo.put("appVersion", info.getAppVersion());
        osInfo.put("deviceId", info.getDeviceId());
        return osInfo;
    }

    /**
     * ?????????+????????????
     *
     * @param userAuthVo
     * @return
     * @throws UserException
     */
    public UserAccounts login(UserAuthVo userAuthVo) throws UserException {
    	 UserAccounts user= userAccountsService.selectByUsername(userAuthVo.getUserName()); 
         if(user==null){
 			throw new ServiceException("???????????????!");
         }

         return user; 

    }
    
    /**
     * ????????????
     *
     * @param userAuthVo
     * @return
     * @throws UserException
     */
    public UserAccounts quickLogin(UserAuthVo userAuthVo) throws UserException {
		UserAccounts ownAccount = userAccountsService.selectByPhone(userAuthVo.getPhone());
		if(ownAccount==null){
			throw new ServiceException("???????????????");
		}
        return ownAccount;

    }


    /**
     * ????????????
     *
     * @param k
     */
    public void logout() {


    }




    /**
     * ???????????????
     *
     * @param mobile
     * @return
     * @throws UserException
     */
    public void sendPhoneCode(String mobile) throws UserException {      
    	userAccountsService.priSmsSend(mobile);
    }
    
    /**
     * ???????????????
     *
     * @param mobile
     * @return
     * @throws UserException
     */
    public void sendEmaiCode(String mobile,String type,String url) throws UserException {      
    	userAccountsService.priEmailSend(mobile,type,url);
    }


    /**
     * ????????? + ????????? ????????????????????????
     *
     * @param pwd
     * @throws UserException
     */
    public void modifyLoginPasswordByPhoneCode(UserPwdInfo pwd) throws UserException {


    }
    /**
     * ????????? + ????????? ????????????????????????
     *
     * @param pwd
     * @throws UserException
     */
    public void validationByPhoneCode(UserPwdInfo pwd) throws UserException {
		UserAccounts ownAccount = userAccountsService.selectByPhone(pwd.getPhone());
		if(ownAccount==null){
			throw new ServiceException("???????????????");
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ValideCode", pwd.getValidationCode());
		param.put("mobile", pwd.getPhone());
		String result = HttpClientUtils.get("http://passport.ipsos.com.cn/api/Validate/ValideCodeSubmit", param, null);
		net.sf.json.JSONObject jsonResult =  net.sf.json.JSONObject.fromObject(result);
		if (jsonResult.getString("Status").toUpperCase().equals("TRUE")) {
		}else{
			throw new ServiceException("???????????????");
		}

    }

    /**
     * ?????? + ????????? ????????????????????????
     *
     * @param pwd
     * @throws UserException
     */
    public void modifyLoginPasswordByEmailCode(UserPwdInfo pwd) throws UserException {

    }

    /**
     * H5 ?????????????????????
     * @param userAuthVo
     * @return
     * @throws UserException
     */
    public void sendRegisterCaptcha(UserAuthVo userAuthVo) throws UserException {
        LOG.info("--------sendRegisterCaptcha--------" + userAuthVo);
        userAccountsService.priSmsSend(userAuthVo.getPhone());
    }

    
    /**
     * H5 ???????????????????????????????????????
     * @param userAuthVo
     * @return
     * @throws UserException
     */
    public UserAccounts registerUser(UserAuthVo userAuthVo) throws UserException {
        UserAccounts ua = new UserAccounts();
        return ua;
    }
    
    /**
     * 
     * @param userAuthVo
     * @return
     * @throws UserException
     */
	public String createToken(UserAccounts accounts) {
        String token = String.format(Configurer.CACHE_TOKEN_KEY, "user",
                UUID.randomUUID().toString());
        redisClient.put(String.format(Configurer.HEADER_TOEKN_USERINFO, token), accounts, 24*60*60*10L);

        CommonUtil
                .getResponse()
                .setHeader(
                        "token",
                        token);
        return token;
    }
	
    /**
     * ???????????????????????????
     *
     * @param openId
     * @param channelId
     * @param unionId
     * @return
     * @throws UserException
     * @throws RemotingException
     */
    public UserAccounts  selectByOpenId(String openId, String channelId) throws UserException {
    	UserAccounts accounts=userAccountsService.selectByOpenId(openId);
    	accounts.setPassword(null);
    	return accounts;
    }
    
    /**
     * ???????????????
     *
     * @param userAuthVo
     * @return
     * @throws UserException
     */
    public UserAccounts noPasswordLogin(String userName) throws UserException {

         UserAccounts user= userAccountsService.selectByUsername(userName);
         if(user==null){
 			throw new ServiceException("???????????????!");
         }     
         String token=createToken(user);
         user.setPassword(null);
         user.setToken(token);
         return user; 

    }
    /**
     * ??????????????????????????????
     *
     * @param userAuthVo
     * @return
     * @throws UserException
     * @throws RemotingException
     */
    public UserAccounts thirdPartyBindingAndLogin(UserAuthVo userAuthVo) throws UserException {
        UserAccounts user= userAccountsService.selectByPhone(userAuthVo.getPhone());
        if(user==null){
			throw new ServiceException("???????????????!");
        }   
        userAccountsService.updateOpenId(user.getUserId(),userAuthVo.getOpenId(),userAuthVo.getNickname());
        UserAccounts user1= userAccountsService.selectByPhone(userAuthVo.getPhone());
        user1.setPassword(null);
        return user1;
    }


}
