package com.dubu.turnover;



import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.dubu.turnover.core.ResultGenerator;
import com.dubu.turnover.domain.entity.BusAssets;
import com.dubu.turnover.domain.entity.BusAssetsAudit;
import com.dubu.turnover.service.BusAssetsAuditService;
import com.dubu.turnover.service.BusAssetsService;
import com.dubu.turnover.utils.DateUtil;
import com.dubu.turnover.utils.ExcelTemplate;
@WebAppConfiguration
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PriorityApplication.class, loader = SpringBootContextLoader.class)
public class TestWordService {

    @Resource
	private BusAssetsService busAssetsService;
    
    @Resource
   	private  BusAssetsAuditService busAssetsAuditService;

	
	
	
	@Test
    public void add() throws Exception {
		 List<BusAssets> list = busAssetsService.selectByIds("252");
		 try{

		 			 ExcelTemplate excel = new ExcelTemplate("D://librarysign.xls");
		 			LinkedHashMap<Integer, LinkedList<String>> rows = new LinkedHashMap<>();
		 			// 创建第一个行区域里面填充的值，ExcelTemplate会按从左至右，
		 			// 从上往下的顺序，挨个填充区域里面的${}，所以创建的时候注意顺序就好
		 			int i = 1;
		 			for (BusAssets att : list) {
		 				att.setId(i);
		 				LinkedList<String> row1 = new LinkedList<>();
		 				row1.add(att.getId().toString());
		 				row1.add(att.getAssetsNo()!=null?att.getAssetsNo():"");
		 				row1.add(att.getAssetsName());
				         row1.add(att.getAssetsModel()!=null?att.getAssetsModel():"");
		 				row1.add(att.getFinanceDate() != null ? DateUtil.parseDate(att.getFinanceDate(), DateEnum.DATE) : "");
		 				row1.add(att.getPrice() != null ? att.getPrice().toString() : "");
		 				BusAssetsAudit audit=busAssetsAuditService.getAssetsAuditById(att.getId());
		 				if(audit!=null && att.getStatus()==2){
			 				row1.add(audit.getReceiveDeptName()!=null?audit.getReceiveDeptName():"");
			 				row1.add(audit.getDepositAddress()!=null?audit.getDepositAddress():"");
			 				row1.add(audit.getUseUserName()!=null?audit.getUseUserName():"");
			 				row1.add(att.getUserName()!=null?att.getUserName():"");
		 				}else{
			 				row1.add("");
			 				row1.add("");
			 				row1.add("");
			 				row1.add(att.getUserName()!=null?att.getUserName():"");
		 				}
		 				rows.put(i, row1);
		 				i++;
		 			}
		 			excel.addRowByExist(0, 2, 2, 3, rows, true);

		 			excel.save("D://kkk.xls");
     }catch(Exception e){
    	 e.printStackTrace();
     }
	
	}

}
