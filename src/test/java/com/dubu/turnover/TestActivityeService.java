package com.dubu.turnover;



import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.dubu.turnover.annotation.DateEnum;
import com.dubu.turnover.domain.UserInfo;
import com.dubu.turnover.domain.entity.BusAssets;
import com.dubu.turnover.service.BusAssetsService;
import com.dubu.turnover.service.BusTaskService;
import com.dubu.turnover.utils.DateUtil;
import com.dubu.turnover.utils.ExcelTemplate;
@WebAppConfiguration
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PriorityApplication.class, loader = SpringBootContextLoader.class)
public class TestActivityeService {

    @Resource
	private BusAssetsService busAssetsService;
    

    @Resource
	private BusTaskService busTaskService;

	
	
	
	@Test
    public void add() throws Exception {
		Integer count=busAssetsService.getCardIdCount("111", 329);
		System.out.println("11111--"+count);
     }
	
	

}
