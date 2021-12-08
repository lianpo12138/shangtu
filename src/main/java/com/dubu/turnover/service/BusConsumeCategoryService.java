package com.dubu.turnover.service;

import com.dubu.turnover.mapper.BusConsumeCategoryMapper;
import com.dubu.turnover.domain.entity.BusConsumeCategory;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.dubu.turnover.core.AbstractService;

/*
 *  @author: smart boy
 *  @date: 2021-04-01
 */
@Service
public class BusConsumeCategoryService extends AbstractService<BusConsumeCategory> {

	@Resource
	private BusConsumeCategoryMapper busConsumeCategoryMapper;

}
