package com.dubu.turnover.controller.erp;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dubu.turnover.annotation.NoLogin;
import com.dubu.turnover.component.redis.RedisClient;
import com.dubu.turnover.configure.Configurer;
import com.dubu.turnover.core.Result;
import com.dubu.turnover.core.ResultGenerator;
import com.dubu.turnover.core.ServiceException;
import com.dubu.turnover.domain.entity.SysAdmin;
import com.dubu.turnover.domain.entity.SysDept;
import com.dubu.turnover.service.SysAdminService;
import com.dubu.turnover.service.SysDeptService;
import com.dubu.turnover.service.SysRoleService;
import com.dubu.turnover.utils.CommonUtil;
import com.dubu.turnover.utils.MD5Util;
import com.dubu.turnover.vo.DeptVo;

@RestController
@RequestMapping("/erp")
public class SysLoginController {
	private Logger logger = LoggerFactory.getLogger(SysLoginController.class);
	@Autowired
	private SysAdminService adminsService;
	@Resource
	private SysRoleService rolesService;
	@Autowired
	private RedisClient redisClient;
	  
	@Autowired
	private  SysRoleService sysRoleService;
	
    @Resource
	private SysDeptService sysDeptService;

	@PostMapping("login")
	@NoLogin
	public Result add(HttpServletRequest request, @RequestBody SysAdmin admin)
			throws ServiceException {
		// 用户名或密码为空 错误
		if (StringUtils.isEmpty(admin.getLoginName())
				) {
			logger.error(String.format("用户名为空。", admin.getLoginName()));
			throw new ServiceException("用户名为空");
		}
		// 查询用户信息
		SysAdmin adminx = adminsService.getAdminByLoginNameAndPassword(
				admin.getLoginName(), null);

		if (adminx == null) {
			throw new ServiceException("账号不存在，请确认");
		}

		// 插入用户权限列表到缓存
		this.createToken(adminx);

		// 更新用户登录信息
		SysAdmin updateAdmin = new SysAdmin();
		updateAdmin.setUpdateBy(admin.getUserName());
		updateAdmin.setUpdateTime(DateTime.now().toDate());
		updateAdmin.setLoginIp(CommonUtil.getClientIp());
		updateAdmin.setLoginDate(DateTime.now().toDate());
		updateAdmin.setId(adminx.getId());
		adminsService.updateById(updateAdmin);
        List<Integer> depts=sysRoleService.getDeptsIdsByUserId(adminx.getId());
    	List<DeptVo> deptList = new ArrayList<DeptVo>();
        for(Integer id:depts){
        	SysDept dept=sysDeptService.selectById(id);
        	DeptVo vo=new DeptVo();
        	vo.setDeptId(dept.getId());
        	vo.setDeptName(dept.getName());
        	deptList.add(vo);
        }
        adminx.setDeptList(deptList);
		return ResultGenerator.success(adminx);
	}

	public void createToken(SysAdmin admin) {
		try{
	        String token = String.format(Configurer.CACHE_TOKEN_KEY, "erp",
	                UUID.randomUUID().toString());
	        // 查询用户权限并插入到cache 方便后面权限校验
	        Set<String> set = rolesService.selectPermsByUserId(admin.getId());
	        redisClient.put(String.format(Configurer.ERP_PERMISSION_CACHE, admin.getId()), set, 24*60*60*10L);
	        redisClient.put(String.format(Configurer.ERP_LOGIN_CACHE, token), admin, 24*60*60*10L);
	        CommonUtil
	                .getResponse()
	                .setHeader(
	                        String.format(Configurer.HEADER_TOKEN_KEY, "erp"),
	                        token);			
		}catch(Exception e){
			e.printStackTrace();
		}

    }
	
	public boolean matches(SysAdmin admin, String newPassword) {
		return admin.getPassword().equals(
				encryptPassword(admin.getLoginName(), newPassword,
						admin.getSalt()));
	}

	public String encryptPassword(String username, String password, String salt) {
		return MD5Util.MD5Encode(username + password + salt, "UTF-8").toString();
	}
}
