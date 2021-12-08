package com.dubu.turnover.controller.erp;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dubu.turnover.annotation.RequiresPermissions;
import com.dubu.turnover.configure.Configurer;
import com.dubu.turnover.core.Result;
import com.dubu.turnover.core.ResultGenerator;
import com.dubu.turnover.core.ServiceException;
import com.dubu.turnover.domain.AccountCheckVo;
import com.dubu.turnover.domain.ErpUserVo;
import com.dubu.turnover.domain.UserAuthVo;
import com.dubu.turnover.domain.entity.UserAccounts;
import com.dubu.turnover.service.UserAccountsService;
import com.dubu.turnover.service.UserAuthService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

/*
 *  @author: smart boy
 *  @date: 2019-03-15
 */
@CrossOrigin
@RestController
@RequestMapping("/erp/useraccounts")
public class StUserAccountsController {

	@Resource
	private UserAccountsService userAccountsService;
	@Resource
	private UserAuthService userAuthService;

	/**
	 * 用户账号查询
	 * 
	 * @param page
	 *            当前页
	 * @param size
	 *            每页大小
	 * @return
	 */
	@GetMapping
	@RequiresPermissions(value={"accounts:useraccounts:list"})
	public Result list(@RequestParam(required = false) String userName, @RequestParam(required = false) Integer userId,
			@RequestParam(required = false) String key, @RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "10") Integer size) {

		PageHelper.startPage(page, size);
		Condition condition = new Condition(UserAccounts.class);
		Example.Criteria criteria = condition.createCriteria();
		if (userId != null) {
			criteria.andEqualTo("userId", userId);
		}
		if (!StringUtil.isEmpty(userName)) {
			criteria.andLike("userName", "%" + userName + "%");
		}
		if (!StringUtils.isEmpty(key)) {
			Pattern pattern = Pattern.compile("[0-9]*");
			Matcher isNum = pattern.matcher(key);
			if (isNum.matches()) {
				criteria.andEqualTo("userId", Long.parseLong(key));
			} else {
				criteria.andLike("userName", "%" + key + "%");
			}
		}
		condition.orderBy("createTime").desc();
		List<UserAccounts> list = userAccountsService.selectByCondition(condition);
		PageInfo<UserAccounts> pageInfo = new PageInfo<>(list);
		return ResultGenerator.success(pageInfo);
	}


}
