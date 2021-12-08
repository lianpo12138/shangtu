package com.dubu.turnover.controller.erp;

import com.dubu.turnover.service.BusConsumeCategoryService;
import com.dubu.turnover.service.BusConsumeProjectService;
import com.dubu.turnover.domain.UserInfo;
import com.dubu.turnover.domain.entity.BusConsumeCategory;
import com.dubu.turnover.domain.entity.BusConsumeProject;
import com.dubu.turnover.domain.entity.BusConsumeStore;
import com.dubu.turnover.domain.entity.BusConsumeType;
import com.dubu.turnover.mapper.BusConsumeStoreMapper;
import com.dubu.turnover.component.ThreadRequestContext;
import com.dubu.turnover.core.Result;
import com.dubu.turnover.core.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

import java.util.List;

/*
 *  @author: smart boy
 *  @date: 2021-03-18
 */
@RestController
@RequestMapping("/erp/busconsumeproject")
@Api(value = "耗材项目管理",tags="耗材项目管理")
public class BusConsumeProjectController {

    @Resource
	private BusConsumeProjectService busConsumeProjectService;
    
	@Resource
	private BusConsumeStoreMapper busConsumeStoreMapper;
	
    @Resource
	private BusConsumeCategoryService busConsumeCategoryService;

    @ApiOperation(value = "耗材项目列表", notes = "耗材项目列表")
	@GetMapping("/list")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "page", dataType = "Integer"),
        @ApiImplicitParam(name = "size", value = "size", dataType = "Integer"),
        @ApiImplicitParam(name = "projectName", value = "项目名称", dataType = "String"),
        @ApiImplicitParam(name = "orderBy", value = "排序字段", dataType = "String"),
        @ApiImplicitParam(name = "asc", value = "排序方式:asc和desc", dataType = "String")
    })
    public Result list(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size,
    		@RequestParam(required = false) String projectName,
    		@RequestParam(required = false) String orderBy,
    		@RequestParam(required = false) String asc) {
		UserInfo userInfo = ThreadRequestContext.current();
        PageHelper.startPage(page, size);
        Condition condition =  new Condition(BusConsumeProject.class);
		Example.Criteria criteria = condition.createCriteria();
        if(!StringUtils.isEmpty(projectName)){
        	criteria.andLike("projectName", "%"+projectName+"%");
        }
		if(!userInfo.getDeptIdList().contains(100)){
			criteria.andIn("deptId", userInfo.getDeptIdList());
		}
		if(!StringUtil.isEmpty(orderBy)){
			if("asc".equals(asc)){
				condition.orderBy(orderBy).asc();	
			}else{
				condition.orderBy(orderBy).desc();	
			}
		}else{
			condition.orderBy("createTime").desc();	
		}
        List<BusConsumeProject> list = busConsumeProjectService.selectByCondition(condition);
        PageInfo<BusConsumeProject> pageInfo = new PageInfo<>(list);
        return ResultGenerator.success(pageInfo);
	}

    @ApiOperation(value = "耗材项目新增", notes = "耗材项目新增")
	@PostMapping("/add")
    public Result add(@RequestBody BusConsumeProject busConsumeProject) {
    	Condition condition = new Condition(BusConsumeProject.class);
		condition.createCriteria().andEqualTo("projectName", busConsumeProject.getProjectName());
		Integer count  = busConsumeProjectService.selectCountByCondition(condition);
		if(count>0){
		    return ResultGenerator.error("项目名称已存在,不能新增");
		}
        busConsumeProjectService.save(busConsumeProject);
        return ResultGenerator.success();
	}

    @ApiOperation(value = "耗材项目删除", notes = "耗材项目删除")
    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
    	Condition condition = new Condition(BusConsumeStore.class);
		condition.createCriteria().andEqualTo("projectId", id);
		Integer count  = busConsumeStoreMapper.selectCountByCondition(condition);
		if(count>0){
		    return ResultGenerator.error("该分类下有库存数据，不能删除");
		}
        busConsumeProjectService.deleteById(id);
        Condition condition2 =  new Condition(BusConsumeCategory.class);
		Example.Criteria criteria2 = condition2.createCriteria();
			criteria2.andEqualTo("projectId", id);
        List<BusConsumeCategory> list = busConsumeCategoryService.selectByCondition(condition);
        for(BusConsumeCategory category:list){
        	busConsumeCategoryService.deleteById(category.getId());
        }
        return ResultGenerator.success();
	}

    @ApiOperation(value = "耗材项目修改", notes = "耗材项目修改")
    @PostMapping("/update")
    public Result update(@RequestBody BusConsumeProject busConsumeProject) {
    	Condition condition = new Condition(BusConsumeProject.class);
		condition.createCriteria().andEqualTo("projectName",
				busConsumeProject.getProjectName()).andNotEqualTo("id", busConsumeProject.getId());
		Integer count  = busConsumeProjectService.selectCountByCondition(condition);
		if(count>0){
		    return ResultGenerator.error("耗材类型名称已存在,不能新增");
		}
        busConsumeProjectService.updateById(busConsumeProject);
        return ResultGenerator.success();
	}

    @ApiOperation(value = "耗材项目详情", notes = "耗材项目详情")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Integer id) {
        BusConsumeProject busConsumeProject = busConsumeProjectService.selectById(id);
        return ResultGenerator.success(busConsumeProject);
	}
    
    @ApiOperation(value = "耗材项目列表", notes = "耗材项目列表")
	@RequestMapping(value="/getCoumsumeProjectList",method = RequestMethod.GET)
	@ResponseBody
	public Result getCoumsumeProjectList() throws Exception {
    	UserInfo userInfo = ThreadRequestContext.current();
		Condition condition = new Condition(BusConsumeProject.class);
		Example.Criteria criteria = condition.createCriteria();
		if(!userInfo.getDeptIdList().contains(100)){
			criteria.andIn("deptId", userInfo.getDeptIdList());
		}
		List<BusConsumeProject> list = busConsumeProjectService.selectByCondition(condition);
		return ResultGenerator.success(list);
	}
}

