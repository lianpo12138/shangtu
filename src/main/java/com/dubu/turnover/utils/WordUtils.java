package com.dubu.turnover.utils;



import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.Units;
import org.apache.poi.xddf.usermodel.XDDFColor;
import org.apache.poi.xddf.usermodel.XDDFShapeProperties;
import org.apache.poi.xddf.usermodel.XDDFSolidFillProperties;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;

import com.dubu.turnover.vo.TableForm;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Copyright (C), 2006-2010, ChengDu ybya info. Co., Ltd.
 * FileName: WordUtils.java
 *
 * @author lh
 * @version 1.0.0
 * @Date 2021/02/02 16:04
 */
public class WordUtils {

    /**
     * 获取图表对象
     *
     * @param document word对象
     * @param width    默认15
     * @param height   默认10
     * @return
     */
    public XWPFChart getChart(XWPFDocument document, Integer width, Integer height) throws IOException, InvalidFormatException {
        if (width == null) {
            width = 15;
        }
        if (height == null) {
            height = 10;
        }
        return document.createChart(width * Units.EMU_PER_CENTIMETER, height * Units.EMU_PER_CENTIMETER);
    }

   
    /**
     * 添加word中的标记数据 标记方式为 ${text}
     *
     * @param document word对象
     * @param textMap  需要替换的信息集合
     */
    public void changeParagraphText(XWPFDocument document, Map<String, String> textMap) {
        //获取段落集合
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            //判断此段落时候需要进行替换
            String text = paragraph.getText();
            if (checkText(text)) {
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    //替换模板原来位置
                    run.setText(changeValue(run.toString(), textMap), 0);
                }
            }
        }
    }

    /**
     * 替换表格中标记的数据 标记方式为 ${text}
     * 这里有个奇怪的问题 输入${}符号的时候需要把输入法切换到中文
     * ${}中间不能用数字,不能有下划线
     *
     * @param document word对象
     * @param textMap  需要替换的信息集合
     */
    public void changeTableText(XWPFDocument document, List<Map<String, String>> tableTextList) {
        //获取表格对象集合
        List<XWPFTable> tables = document.getTables();
        for (int i = 0; i < tables.size(); i++) {
            Map<String, String> textMap = tableTextList.get(i);
            //只处理行数大于等于2的表格
            XWPFTable table = tables.get(i);
            if (table.getRows().size() > 1) {
                //判断表格是需要替换还是需要插入，判断逻辑有$为替换，表格无$为插入
                if (checkText(table.getText())) {
                    List<XWPFTableRow> rows = table.getRows();
                    //遍历表格,并替换模板
                    eachTable(rows, textMap);
                }
            }
        }
    }

    /**
     * 复制表头,插入行数据,这里的样式和表头一样
     *
     * @param document word对象
     * @param list     集合个数和word中的表格个数必须相同
     */
    public void copyHeaderInsertText(XWPFDocument document, List<TableForm> list) {
        //获取表格对象集合
        List<XWPFTable> tables = document.getTables();
        // 循环word中的所有表格
        for (int k = 0; k < tables.size(); k++) {
            // 获取单个表格
            XWPFTable table = tables.get(k);
            // 获取要替换的数据
            TableForm tableForm = list.get(k);
            Integer headerIndex = tableForm.getStartLine();
            List<String[]> tableList = tableForm.getData();
            if (null == tableList) {
                return;
            }
            XWPFTableRow copyRow = table.getRow(headerIndex);
            List<XWPFTableCell> cellList = copyRow.getTableCells();
            if (null == cellList) {
                break;
            }
            //遍历要添加的数据的list
            for (int i = 0; i < tableList.size(); i++) {
                //插入一行
                XWPFTableRow targetRow = table.insertNewTableRow(headerIndex  + i);
                //复制行属性
                targetRow.getCtRow().setTrPr(copyRow.getCtRow().getTrPr());

                String[] strings = tableList.get(i);
                for (int j = 0; j < strings.length; j++) {
                    XWPFTableCell sourceCell = cellList.get(j);
                    //插入一个单元格
                    XWPFTableCell targetCell = targetRow.addNewTableCell();
                    //复制列属性
                    targetCell.getCTTc().setTcPr(sourceCell.getCTTc().getTcPr());
                    targetCell.setText(strings[j]);
                }
            }
        }
    }

    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument(new FileInputStream("D:\\FreeMarker.docx"))) {
            WordUtils wordUtils = new WordUtils();
            Map<String, String> paragraphMap = new HashMap<>();
            paragraphMap.put("number", "10000");
            paragraphMap.put("date", "2020-03-25");
            wordUtils.changeParagraphText(document, paragraphMap);

            List<Map<String, String>> tableTextList = new ArrayList<>();
            Map<String, String> tableMap = new HashMap<>();
            tableMap.put("name", "赵云");
            tableMap.put("sexual", "男");
            tableMap.put("birthday", "2020-01-01");
            tableMap.put("identify", "123456789");
            tableMap.put("phone", "18377776666");
            tableMap.put("address", "王者荣耀");
            tableMap.put("domicile", "中国-腾讯");
            tableMap.put("QQ", "是");
            tableMap.put("chat", "是");
            tableMap.put("blog", "是");
            tableTextList.add(tableMap);
            Map<String, String> tableMap2 = new HashMap<>();
            tableMap2.put("spring", "sony的名称");
            tableTextList.add(tableMap2);
            wordUtils.changeTableText(document, tableTextList);

            List<TableForm> list = new ArrayList<>();
            TableForm tableForm = new TableForm();
            tableForm.setStartLine(7);
            tableForm.getData().add(new String[]{"露娜", "女", "野友", "666", "6660"});
            tableForm.getData().add(new String[]{"鲁班", "男", "2友", "222", "2220"});
            tableForm.getData().add(new String[]{"程咬金", "男", "1友", "999", "9990"});
            tableForm.getData().add(new String[]{"太乙真人", "男", "3友", "111", "1110"});
            tableForm.getData().add(new String[]{"貂蝉", "女", "法友", "888", "8880"});
            list.add(tableForm);
            TableForm tableForm2 = new TableForm();
            tableForm2.setStartLine(1);
            tableForm2.getData().add(new String[]{"18581588710", "蜘蛛侠", "100"});
            tableForm2.getData().add(new String[]{"18581588710", "战神", "200"});
            list.add(tableForm2);
            wordUtils.copyHeaderInsertText(document,list);
            try (FileOutputStream fileOut = new FileOutputStream("D://CreateWordXDDFChart.docx")) {
                document.write(fileOut);
            }
        } catch (Exception e) {

        }




    }

    /**
     * 判断文本中时候包含$
     *
     * @param text 文本
     * @return 包含返回true, 不包含返回false
     */
    public static boolean checkText(String text) {
        boolean check = false;
        if (text.indexOf("$") != -1) {
            check = true;
        }
        return check;
    }

    /**
     * 匹配传入信息集合与模板
     *
     * @param value   模板需要替换的区域
     * @param textMap 传入信息集合
     * @return 模板需要替换区域信息集合对应值
     */
    public static String changeValue(String value, Map<String, String> textMap) {
        Set<Map.Entry<String, String>> textSets = textMap.entrySet();
        for (Map.Entry<String, String> textSet : textSets) {
            //匹配模板与替换值 格式${key}
            String key = "${" + textSet.getKey() + "}";
            if (value.indexOf(key) != -1) {
                value = textSet.getValue();
            }
        }
        //模板未匹配到区域替换为空
        if (checkText(value)) {
            value = "";
        }
        return value;
    }

    /**
     * 遍历表格,并替换模板
     *
     * @param rows    表格行对象
     * @param textMap 需要替换的信息集合
     */
    public static void eachTable(List<XWPFTableRow> rows, Map<String, String> textMap) {
        for (XWPFTableRow row : rows) {
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                //判断单元格是否需要替换
                if (checkText(cell.getText())) {
                    List<XWPFParagraph> paragraphs = cell.getParagraphs();
                    for (XWPFParagraph paragraph : paragraphs) {
                        List<XWPFRun> runs = paragraph.getRuns();
                        for (XWPFRun run : runs) {
                            run.setText(changeValue(run.toString(), textMap), 0);
                        }
                    }
                }
            }
        }
    }

    static CellReference setTitleInDataSheet(XWPFChart chart, String title, int column) throws Exception {
        XSSFWorkbook workbook = chart.getWorkbook();
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow row = sheet.getRow(0);
        if (row == null)
            row = sheet.createRow(0);
        XSSFCell cell = row.getCell(column);
        if (cell == null)
            cell = row.createCell(column);
        cell.setCellValue(title);
        return new CellReference(sheet.getSheetName(), 0, column, true, true);
    }
}