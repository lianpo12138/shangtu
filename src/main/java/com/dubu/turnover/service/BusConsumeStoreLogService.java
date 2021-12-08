package com.dubu.turnover.service;

import com.dubu.turnover.mapper.BusConsumeStoreLogMapper;
import com.dubu.turnover.domain.entity.BusConsumeStoreLog;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.dubu.turnover.core.AbstractService;

/*
 *  @author: smart boy
 *  @date: 2021-04-26
 */
@Service
public class BusConsumeStoreLogService extends AbstractService<BusConsumeStoreLog> {

	@Resource
	private BusConsumeStoreLogMapper busConsumeStoreLogMapper;

}
