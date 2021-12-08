package com.dubu.turnover.service;

import com.dubu.turnover.mapper.BusConsumeProjectMapper;
import com.dubu.turnover.domain.entity.BusConsumeProject;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.dubu.turnover.core.AbstractService;

/*
 *  @author: smart boy
 *  @date: 2021-04-01
 */
@Service
public class BusConsumeProjectService extends AbstractService<BusConsumeProject> {

	@Resource
	private BusConsumeProjectMapper busConsumeProjectMapper;

}
