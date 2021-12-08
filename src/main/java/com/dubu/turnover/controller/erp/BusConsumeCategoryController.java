package com.dubu.turnover.controller.erp;

import com.dubu.turnover.mapper.BusConsumeStoreMapper;
import com.dubu.turnover.service.BusConsumeCategoryService;
import com.dubu.turnover.service.BusConsumeProjectService;
import com.dubu.turnover.service.BusConsumeStoreService;
import com.dubu.turnover.domain.UserInfo;
import com.dubu.turnover.domain.entity.BusConsumeCategory;
import com.dubu.turnover.domain.entity.BusConsumeProject;
import com.dubu.turnover.domain.entity.BusConsumeStore;
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
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;

/*
 *  @author: smart boy
 *  @date: 2021-03-13
 */
@RestController
@RequestMapping("/erp/busconsumecategory")
@Api(value = "耗材项目分类",tags="耗材项目分类")
public class BusConsumeCategoryController {

    @Resource
	private BusConsumeCategoryService busConsumeCategoryService;
    @Resource
	private BusConsumeProjectService busConsumeProjectService;
	@Resource
	private BusConsumeStoreMapper busConsumeStoreMapper;

	@GetMapping("/list")
	@ApiOperation(value = "耗材分类列表", notes = "耗材分类列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "page", dataType = "Integer"),
        @ApiImplicitParam(name = "size", value = "size", dataType = "Integer"),
        @ApiImplicitParam(name = "consumeName", value = "配件名称", dataType = "String"),
        @ApiImplicitParam(name = "projectId", value = "项目id", dataType = "String"),
        @ApiImplicitParam(name = "projectName", value = "项目名称", dataType = "String"),
        @ApiImplicitParam(name = "consumeTypeId", value = "耗材类型id", dataType = "String"),
        @ApiImplicitParam(name = "orderBy", value = "排序字段", dataType = "String"),
        @ApiImplicitParam(name = "asc", value = "排序方式:asc和desc", dataType = "String"),
    })
    public Result list(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size,
    		@RequestParam(required = false) String consumeTypeId,
    		@RequestParam(required = false) String consumeName,
    		@RequestParam(required = false) Integer projectId,
    		@RequestParam(required = false) String projectName,
    		@RequestParam(required = false) String orderBy,
    		@RequestParam(required = false) String asc) {
		UserInfo userInfo = ThreadRequestContext.current();
        PageHelper.startPage(page, size);
        Condition condition =  new Condition(BusConsumeCategory.class);
		Example.Criteria criteria = condition.createCriteria();
		if(!userInfo.getDeptIdList().contains(100)){
			criteria.andIn("deptId", userInfo.getDeptIdList());
		}
        if(consumeTypeId!=null){
        	criteria.andEqualTo("consumeTypeId", consumeTypeId);
        }
        if(!StringUtils.isEmpty(projectName)){
        	criteria.andLike("projectName", "%"+projectName+"%");
        }
        if(!StringUtils.isEmpty(consumeName)){
        	criteria.andLike("consumeName", "%"+consumeName+"%");
        }
        if(projectId!=null){
        	criteria.andEqualTo("projectId", projectId);
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
        List<BusConsumeCategory> list = busConsumeCategoryService.selectByCondition(condition);
        PageInfo<BusConsumeCategory> pageInfo = new PageInfo<>(list);
        return ResultGenerator.success(pageInfo);
	}
	
	
	@PostMapping("/add")
	@ApiOperation(value = "耗材分类新增", notes = "耗材分类新增")
    public Result add(@RequestBody BusConsumeCategory busConsumeCategory) {
		BusConsumeProject project=busConsumeProjectService.selectById(busConsumeCategory.getProjectId());
		busConsumeCategory.setDeptId(project.getDeptId());
		busConsumeCategory.setDeptName(project.getDeptName());
        busConsumeCategoryService.save(busConsumeCategory);
        return ResultGenerator.success();
	}

    @GetMapping("/delete/{id}")
    @ApiOperation(value = "耗材分类删除", notes = "耗材分类删除")
    public Result delete(@PathVariable Integer id) {
    	Condition condition = new Condition(BusConsumeStore.class);
		condition.createCriteria().andEqualTo("categoryId", id);
		Integer count  = busConsumeStoreMapper.selectCountByCondition(condition);
		if(count>0){
		    return ResultGenerator.error("该分类下有库存数据，不能删除");
		}
        busConsumeCategoryService.deleteById(id);
        return ResultGenerator.success();
	}

    @PostMapping("/update")
    @ApiOperation(value = "耗材分类修改", notes = "耗材分类修改")
    public Result update(@RequestBody BusConsumeCategory busConsumeCategory) {
        busConsumeCategoryService.updateById(busConsumeCategory);
        return ResultGenerator.success();
	}

    @ApiOperation(value = "耗材分类详情", notes = "耗材分类详情")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Integer id) {
        BusConsumeCategory busConsumeCategory = busConsumeCategoryService.selectById(id);
        return ResultGenerator.success(busConsumeCategory);
	}
}

