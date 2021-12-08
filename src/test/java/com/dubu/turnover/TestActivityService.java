package com.dubu.turnover;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
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
import com.dubu.turnover.domain.entity.BusConsumeUse;
import com.dubu.turnover.domain.entity.BusTask;
import com.dubu.turnover.domain.entity.BusTaskRelation;
import com.dubu.turnover.service.BusAssetsService;
import com.dubu.turnover.service.BusConsumeUseService;
import com.dubu.turnover.service.BusTaskRelationService;
import com.dubu.turnover.service.BusTaskService;
import com.dubu.turnover.utils.DateUtil;
import com.dubu.turnover.utils.ExcelTemplate;
import com.dubu.turnover.utils.WordUtils;
import com.dubu.turnover.vo.TableForm;
@WebAppConfiguration
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PriorityApplication.class, loader = SpringBootContextLoader.class)
public class TestActivityService {

    @Resource
	private BusAssetsService busAssetsService;

    @Resource
   	private BusConsumeUseService busConsumeUseService;
	
    @Resource
	private BusTaskRelationService busTaskRelationService;
    
    @Resource
   	private BusTaskService busTaskService;
	
	@Test
    public void add() throws Exception {
		BusTask task=busTaskService.selectById(427);
		List<BusTaskRelation> listRelation = busTaskRelationService.getRelationList(427);
		List<String> strList=listRelation.stream().map(BusTaskRelation::getTaskSourceId).map(String::valueOf).collect(Collectors.toList());
		List<BusConsumeUse> useList = busConsumeUseService.selectByIds(String.join(",",strList));
		String fileadress="D:\\use.docx";
		//String fileadress=sysConfig.getFileUrl() + File.separator + "temp" + File.separator+ "use.docx";
		try (XWPFDocument document = new XWPFDocument(new FileInputStream(fileadress))) {
			    WordUtils wordUtils = new WordUtils();
	            List<Map<String, String>> tableTextList = new ArrayList<>();
	            Map<String, String> tableMap = new HashMap<>();
	            tableMap.put("name", useList.get(0).getDeptName()!=null?useList.get(0).getDeptName():"");
	            tableMap.put("sexual", DateUtil.getNow(DateEnum.DATE));
	            tableMap.put("address", useList.get(0).getUseUserName()!=null?useList.get(0).getUseUserName():"");
	            tableTextList.add(tableMap);
	            Map<String, String> tableMap2 = new HashMap<>();
	            tableTextList.add(tableMap2);
	            wordUtils.changeTableText(document, tableTextList);

	            List<TableForm> list = new ArrayList<>();
	            TableForm tableForm = new TableForm();
	            tableForm.setStartLine(2);
	            for(BusConsumeUse use:useList){
	                tableForm.getData().add(new String[]{use.getProjectName(),
	                		use.getConsumeName(), use.getConsumeType()
	                		, use.getConsumeModel(), use.getNumber().toString(), 
	                		  use.getStoreAddress()
	                		, use.getPurpose()});
	            }
	            list.add(tableForm);
	            TableForm tableForm2 = new TableForm();
	            tableForm2.setStartLine(1);
	            if(task.getTaskType()==6){
		            tableForm2.getData().add(new String[]{"领用人签字:","验收人签字:"});
		            tableForm2.getData().add(new String[]{"领用日期:","验收日期:"});	
	            }else{
		            tableForm2.getData().add(new String[]{"领用人签字:",""});
		            tableForm2.getData().add(new String[]{"领用日期:",""});
	            }
	            tableForm2.getData().add(new String[]{"领用部门分管签字:","耗材管理员出库签字:"});
	            tableForm2.getData().add(new String[]{"日期:","日期:"});
	            list.add(tableForm2);
	            wordUtils.copyHeaderInsertText(document,list);
	            String ad=UUID.randomUUID().toString();
	           // String address=sysConfig.getFileUrl()+ File.separator + "excel" + File.separator + ad + File.separator + "耗材领用记录.docx";
	            String address="d://ggg.docx";
//	             File file2=new File(sysConfig.getFileUrl()+ File.separator + "excel" + File.separator + ad + File.separator );
//	             if(file2.exists()==false){
//	            	 file2.mkdirs();
//	             }
	            try (FileOutputStream fileOut = new FileOutputStream(address)) {
	                document.write(fileOut);
	            }
//			 return ResultGenerator.success(sysConfig.getShowUrl() + File.separator + "excel" + File.separator + ad
//					+ File.separator + "耗材领用记录.docx");
		} catch (Exception e) {
			e.printStackTrace();
		}     
		
	}
	
	

}
