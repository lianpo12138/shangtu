package com.dubu.turnover.controller.app;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dubu.turnover.annotation.NoLogin;
import com.dubu.turnover.core.Result;
import com.dubu.turnover.core.ResultGenerator;
import com.dubu.turnover.domain.entity.*;
import com.dubu.turnover.domain.enums.AdvertEnum.Platform;
import com.dubu.turnover.domain.rest.OSInfo;
import com.dubu.turnover.service.*;
import com.dubu.turnover.utils.EmailUtil;
import com.dubu.turnover.utils.RequestUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * 首页展示
 */
@Api(value = "architectures", description = "首页接口")
@RestController
@RequestMapping("/index")
public class IndexController {
	
	@Resource
   	private  SysAdminRoleService sysAdminRoleService;
	
	@Autowired
	private SysAdminService adminsService;
	
	@NoLogin
	@RequestMapping(value = "/getStatus", method = RequestMethod.GET)
    public Boolean getStatus(@RequestParam(required = true) String loginName) {
		SysAdmin admin=adminsService.getAdminByLoginName(loginName);
		if(admin==null){
			   return false;
		}
        Integer count=sysAdminRoleService.getUserList(admin.getId());
        if(count>0){
        	 return true;
        }
        return false;
    }
	
	@NoLogin
	@RequestMapping(value = "/send", method = RequestMethod.GET)
    public void send() {
		EmailUtil.send_email("363938720@qq.com", "11", "22");
    }

}
