package com.dubu.turnover.controller.app;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.dubu.turnover.annotation.Guest;
import com.dubu.turnover.annotation.Login;
import com.dubu.turnover.annotation.NoLogin;
import com.dubu.turnover.component.AuthUtil;
import com.dubu.turnover.component.config.SysConfig;
import com.dubu.turnover.component.http.HttpUtil;
import com.dubu.turnover.component.redis.RedisClient;
import com.dubu.turnover.configure.Configurer;
import com.dubu.turnover.constants.Constants;
import com.dubu.turnover.core.Result;
import com.dubu.turnover.core.ResultGenerator;
import com.dubu.turnover.core.ServiceException;
import com.dubu.turnover.domain.LoginInfo;
import com.dubu.turnover.domain.UserAuthVo;
import com.dubu.turnover.domain.UserPwdInfo;
import com.dubu.turnover.domain.entity.UserAccounts;
import com.dubu.turnover.domain.enums.PushEnum.PushChannels;
import com.dubu.turnover.domain.enums.PushEnum.PushTypes;
import com.dubu.turnover.domain.rest.OSInfo;
import com.dubu.turnover.exception.UserException;
import com.dubu.turnover.service.UserAccountsService;
import com.dubu.turnover.service.UserAuthService;
import com.dubu.turnover.utils.HttpClientUtils;
import com.dubu.turnover.utils.JsonUtils;
import com.dubu.turnover.utils.QQLoginUtils;
import com.dubu.turnover.utils.RequestUtils;
import com.dubu.turnover.utils.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParams;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user/auth")
@Api(value = "用户登录注册类",tags="用户登录注册类")
public class UserAuthController {

    
}
