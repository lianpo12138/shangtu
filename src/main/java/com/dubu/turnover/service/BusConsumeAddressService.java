package com.dubu.turnover.service;

import com.dubu.turnover.mapper.BusConsumeAddressMapper;
import com.dubu.turnover.domain.entity.BusConsumeAddress;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.dubu.turnover.core.AbstractService;

/*
 *  @author: smart boy
 *  @date: 2021-05-28
 */
@Service
public class BusConsumeAddressService extends AbstractService<BusConsumeAddress> {

	@Resource
	private BusConsumeAddressMapper busConsumeAddressMapper;

}
