package com.dubu.turnover.service;

import com.dubu.turnover.mapper.BusAssetsCategoryMapper;
import com.dubu.turnover.domain.entity.BusAssetsCategory;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.dubu.turnover.core.AbstractService;

/*
 *  @author: smart boy
 *  @date: 2021-04-21
 */
@Service
public class BusAssetsCategoryService extends AbstractService<BusAssetsCategory> {

	@Resource
	private BusAssetsCategoryMapper busAssetsCategoryMapper;

}
