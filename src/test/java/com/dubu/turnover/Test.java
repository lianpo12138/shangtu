package com.dubu.turnover;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.dubu.turnover.utils.WordUtils;
import com.dubu.turnover.vo.TableForm;

public class Test {
	 public static void main(String[] args) {
	        try (XWPFDocument document = new XWPFDocument(new FileInputStream("D:\\FreeMarker333.docx"))) {
	            WordUtils wordUtils = new WordUtils();

	            List<Map<String, String>> tableTextList = new ArrayList<>();
	            Map<String, String> tableMap = new HashMap<>();
	            tableMap.put("name", "赵云");
	            tableMap.put("sexual", "男");
	            tableTextList.add(tableMap);
	            Map<String, String> tableMap2 = new HashMap<>();
	            tableTextList.add(tableMap2);
	            wordUtils.changeTableText(document, tableTextList);

	            List<TableForm> list = new ArrayList<>();
	            TableForm tableForm = new TableForm();
	            tableForm.setStartLine(2);
	            tableForm.getData().add(new String[]{"露娜", "女", "野友"});
	            tableForm.getData().add(new String[]{"鲁班", "男", "2友"});
	            tableForm.getData().add(new String[]{"程咬金", "男", "1友"});
	            tableForm.getData().add(new String[]{"太乙真人", "男", "3友"});
	            tableForm.getData().add(new String[]{"貂蝉", "女", "法友"});
	            
	            tableForm.getData().add(new String[]{"貂蝉", "女", "法友"});
	            list.add(tableForm);
	            TableForm tableForm2 = new TableForm();
	            tableForm2.setStartLine(3);
	            tableForm2.getData().add(new String[]{"","18581588710"});
	            tableForm2.getData().add(new String[]{"","18581588710"});
	            list.add(tableForm2);
	            wordUtils.copyHeaderInsertText(document,list);
	            try (FileOutputStream fileOut = new FileOutputStream("D://CreateWordXDDFChart.docx")) {
	                document.write(fileOut);
	            }
	        } catch (Exception e) {
e.printStackTrace();
	        }
	 }
}
