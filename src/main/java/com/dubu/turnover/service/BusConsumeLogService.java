package com.dubu.turnover.service;

import com.dubu.turnover.mapper.BusConsumeLogMapper;
import com.dubu.turnover.domain.entity.BusConsumeLog;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.dubu.turnover.core.AbstractService;

/*
 *  @author: smart boy
 *  @date: 2021-03-13
 */
@Service
public class BusConsumeLogService extends AbstractService<BusConsumeLog> {

	@Resource
	private BusConsumeLogMapper busConsumeLogMapper;

}
