package com.dubu.turnover.service;

import com.dubu.turnover.mapper.BusAssetsTypeMapper;
import com.dubu.turnover.utils.TreeUtil;

import tk.mybatis.mapper.entity.Example;

import com.dubu.turnover.domain.entity.BusAssetsType;
import com.dubu.turnover.domain.entity.BusConsumeType;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.dubu.turnover.core.AbstractService;

/*
 *  @author: smart boy
 *  @date: 2021-04-08
 */
@Service
public class BusAssetsTypeService extends AbstractService<BusAssetsType> {

	@Resource
	private BusAssetsTypeMapper busAssetsTypeMapper;
	
	public List<BusAssetsType> selectAllProject(Integer categoryId){
		Example con = new Example(BusAssetsType.class);
		if(categoryId != null){
			con.createCriteria().andEqualTo("assetsCategoryId",categoryId);
		}
		List<BusAssetsType> menus = busAssetsTypeMapper.selectByCondition(con);
		return  TreeUtil.tree(menus);
	}

}
