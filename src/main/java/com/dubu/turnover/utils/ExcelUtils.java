package com.dubu.turnover.utils;

import java.beans.PropertyDescriptor;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.ServiceException;
import com.dubu.turnover.annotation.DateEnum;
import com.dubu.turnover.component.aliyun.AliyunOssClient;

/**
 * Excel 工具类
 */
public class ExcelUtils {
    private static final Logger log = LoggerFactory.getLogger(ExcelUtils.class);

    private static DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    public static Iterator<LinkedHashMap> read(MultipartFile file, List<String> headers, int skipHead) {
        return read(file, LinkedHashMap.class, headers, skipHead);
    }

    public static Iterator<LinkedHashMap> read(File file, List<String> headers, int skipHead) {
        return read(file, LinkedHashMap.class, headers, skipHead);
    }
    /**
     * 一次性读取所有行
     *
     * @param file
     * @param tClass
     * @param fields
     * @param skipHead
     * @param <E>
     * @return
     */
    public static <E> List<E> readAll(MultipartFile file, Class<E> tClass, List<String> fields, int skipHead) {
        Iterator<E> it   = read(file, tClass, fields, skipHead);
        List<E>     list = new ArrayList<>();
        while (it.hasNext()) {
            list.add(it.next());
        }
        return list;
    }

    /**
     * 读取excel文件
     *
     * @param file   excel文件
     * @param tClass 转换的类对象
     * @param fields excel列对应对象字段, 按顺序
     * @return
     */
    public static <E> RowIterator<E> read(MultipartFile file, Class<E> tClass, List<String> fields, int skipHead) throws ServiceException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }
        try {
            return read(file.getOriginalFilename(), file.getInputStream(), tClass, fields, skipHead);
        } catch (IOException e) {
            throw new ServiceException("文件读取错误", e);
        }
    }

    public static <E> RowIterator<E> read(File file, Class<E> tClass, List<String> fields, int skipHead) throws ServiceException {
        try {
            return read(file.getName(), new FileInputStream(file), tClass, fields, skipHead);
        } catch (FileNotFoundException e) {
            throw new ServiceException("文件读取错误", e);
        }
    }

    public static <E> RowIterator<E> read(String filename, InputStream inputStream, Class<E> tClass, List<String> fields, int skipHead) throws ServiceException {
        Workbook workbook;
        try {
            if (filename.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(inputStream);
            } else if (filename.endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream);
            } else {
                throw new ServiceException("文件格式错误");
            }
        } catch (IOException e) {
            throw new ServiceException("文件读取错误", e);
        }
        Sheet sheet = workbook.getSheetAt(0);
        return new RowIterator<>(sheet, skipHead < 0 ? 0 : skipHead, tClass, fields);
    }

    /**
     * 导出Excel对象
     *
     * @param headerMap           表头 key为对象字段名, value为表头描述
     * @param rowAccessWindowSize 缓冲大小
     * @return
     */
    public static <E> ExcelExporter<E> newExcelExport(Class<E> eClass, LinkedHashMap<String, String> headerMap, int rowAccessWindowSize) {
        return new ExcelExporter<>(headerMap, rowAccessWindowSize);
    }

    public static ExcelExporter<Map<String, Object>> newExcelExport(LinkedHashMap<String, String> headerMap, int rowAccessWindowSize) {
        return new ExcelExporter<>(headerMap, rowAccessWindowSize);
    }

    public static class ExcelExporter<E> {
        private SXSSFWorkbook                 workbook;
        private SXSSFSheet                    sheet;
        private LinkedHashMap<String, String> headerMap;

        private AtomicInteger rows = new AtomicInteger();

        public ExcelExporter(LinkedHashMap<String, String> headerMap, int rowAccessWindowSize) {
            this.headerMap = headerMap;
            workbook = new SXSSFWorkbook(rowAccessWindowSize);
            workbook.setCompressTempFiles(true);
        }

        /**
         * 写数据
         *
         * @param obj
         * @throws IllegalAccessException
         * @throws NoSuchMethodException
         * @throws InvocationTargetException
         */
        public void write(E obj) {
            if (rows.get() == 65535 || rows.get() == 0) {
                sheet = workbook.createSheet();
                rows.set(0);
            }
            if (rows.incrementAndGet() == 1) {
                //表头 rowIndex=0
                SXSSFRow titleRow = sheet.createRow(0);
                int      col      = 0;
                for (String s : headerMap.values()) {
                    titleRow.createCell(col).setCellValue(s);
                    col++;
                }
            }
            SXSSFRow contentRow = sheet.createRow(rows.get());
            int      col        = 0;
            for (String s : headerMap.keySet()) {
                String value = "";
                try {
                    Object property = BeanUtilsBean.getInstance().getPropertyUtils().getNestedProperty(obj, s);
                    if (property != null && property.getClass().isAssignableFrom(Date.class)) {
                        value = new DateTime(property).toString(fmt);
                    } else {
                        value = BeanUtilsBean.getInstance().getConvertUtils().convert(property);
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    log.error("", e);
                } catch (NestedNullException ignore) {
                }
                contentRow.createCell(col).setCellValue(value);
                col++;
            }
        }

        public void flush(String filename, HttpServletResponse response) throws IOException {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            workbook.write(os);
            workbook.close();
            workbook.dispose();

            byte[]      content = os.toByteArray();
            InputStream is      = new ByteArrayInputStream(content);

            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((filename + ".xlsx").getBytes(), "ISO8859-1"));
            response.setContentLength(content.length);

            ServletOutputStream  outputStream = response.getOutputStream();
            BufferedInputStream  bis          = new BufferedInputStream(is);
            BufferedOutputStream bos          = new BufferedOutputStream(outputStream);
            byte[]               buff         = new byte[8192];
            int                  bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);

            }
            bis.close();
            bos.close();
            outputStream.flush();
            outputStream.close();
        }
        
        public String ossExcelFile(String showUrl,String savefilePath,String fileName) throws IOException {

			String path =File.separator+"shangtu" +File.separator+java.util.UUID.randomUUID().toString()+File.separator;
	        File targetFile = new File(savefilePath+path);  
	        if(!targetFile.exists()){  
	            targetFile.mkdirs();
	        }  
	        //保存  
	        FileOutputStream os = new FileOutputStream(savefilePath+path+fileName);
            workbook.write(os);
            workbook.close();
            workbook.dispose();
           
            return showUrl+path+fileName;
        }
        
        public void flush(OutputStream out) throws IOException {
            workbook.write(out);
            workbook.close();
            workbook.dispose();
        }
    }


    public static class RowIterator<E> implements Iterator<E> {
        private Sheet        sheet;
        private Class<E>     aClass;
        private int          row;
        private List<String> fields;
        private int          numberOfRows;

        public RowIterator(Sheet sheet, int startRow, Class<E> aClass, List<String> fields) {
            this.sheet = sheet;
            this.row = startRow;
            this.aClass = aClass;
            this.fields = fields;
            this.numberOfRows = sheet.getPhysicalNumberOfRows();
        }

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return this.row < numberOfRows;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public E next() {
            int c = 0;
            E   value;
            try {
                Row row = sheet.getRow(this.row);
                if (row == null) {
                    return null;
                }
                value = aClass.newInstance();
                c = 0;
                int cl = fields.size() > row.getLastCellNum() ? row.getLastCellNum() : fields.size();
                for (; c < cl; c++) {
                    Cell   cell = row.getCell(c);
                    if (cell != null && !"".equals(cell) ) {
                    	System.out.print(c+"____"+cell);
                        readPropertyValue(c, value, cell);
                    }
                }
            } catch (InstantiationException e) {
                throw new ServiceException("对象创建失败", e);
            } catch (InvocationTargetException e) {
                throw new ServiceException(String.format("第 %d 行第 %d 列写入错误", row, c), e);
            } catch (IllegalAccessException | NoSuchMethodException e) {
                throw new ServiceException(String.format("第 %d 行第 %d 列写入失败", row, c), e);
            } catch (ValidationException | IllegalArgumentException e) {
                throw new ServiceException("数据验证错误", e);
            } catch (ServiceException e) {
                throw e;
            } catch (Exception e) {
                throw new ServiceException("导入失败, 请检查导入模版", e);
            } finally {
                this.row++;
            }
            return value;
        }

        private void readPropertyValue(int c, E value, Cell cell) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
            Object cellValue;
            switch (cell.getCellType()) {
                case FORMULA:
                    break;
                case NUMERIC:
                    if (!Map.class.isAssignableFrom(aClass)) {
                        PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(value, fields.get(c));
                        Class              type               = propertyDescriptor.getPropertyType();
                        if (String.class.isAssignableFrom(type)) {
                            cellValue = String.valueOf(BigDecimal.valueOf(cell.getNumericCellValue()).toBigInteger().toString());
                        } else if (Date.class.isAssignableFrom(type) || HSSFDateUtil.isCellDateFormatted(cell)) {
                            cellValue = cell.getDateCellValue();
                        } else {
                            cellValue = cell.getNumericCellValue();
                        }
                        BeanUtils.setProperty(value, fields.get(c), cellValue);
                        break;
                    }
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        ((Map) value).put(fields.get(c), cell.getDateCellValue());
                        break;
                    }
                    BigDecimal n = new BigDecimal(cell.getNumericCellValue());
                    double point = n.subtract(new BigDecimal((long) cell.getNumericCellValue())).doubleValue();
                    if (point == 0) {
                        ((Map) value).put(fields.get(c), (long) cell.getNumericCellValue());
                    } else {
                        ((Map) value).put(fields.get(c), n);
                    }
                    break;
                case STRING:
                    if (!Map.class.isAssignableFrom(aClass)) {
                        PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(value, fields.get(c));
                       System.out.println("qqqqq:"+propertyDescriptor.getPropertyType());
                       System.out.println("qqqqq:"+fields.get(c));
                        Class              type               = propertyDescriptor.getPropertyType();
                        if (Date.class.isAssignableFrom(type)) {
                        	if("".equals(cell.getStringCellValue()) || cell.getStringCellValue()==null){
                                try {
                  
                                } catch (Exception e) {
                                    throw new ServiceException(String.format("第 %d 行 %d 不正确", this.row+1, c + 1));
                                }
                        	}else if (cell.getStringCellValue().matches("\\d{4}-\\d{2}-\\d{2}")) {
                                try {
                                    DateTime d = DateTime.parse(cell.getStringCellValue(), DateTimeFormat.forPattern("yyyy-MM-dd"));
                                    BeanUtils.setProperty(value, fields.get(c), d.toDate());
                                } catch (Exception e) {
                                    throw new ServiceException(String.format("第 %d 行 %d 列 日期不正确", this.row+1, c + 1));
                                }
                            } else if (cell.getStringCellValue().matches("\\d{4}/\\d{1,2}/\\d{1,2}")) {
                                try {
                                    DateTime d = DateTime.parse(cell.getStringCellValue(), DateTimeFormat.forPattern("yyyy/MM/dd"));
                                    BeanUtils.setProperty(value, fields.get(c), d.toDate());
                                } catch (Exception e) {
                                    throw new ServiceException(String.format("第 %d 行 %d 列 日期不正确", this.row+1, c + 1));
                                }
                            } else {
                                throw new ServiceException(String.format("第 %d 行 %d 列 日期格式不正确. 支持格式 yyyy-MM-dd 或 yyyy/MM/dd.", this.row+1, c + 1));
                            }
                        } else if (Number.class.isAssignableFrom(type)) {
                            if (org.apache.commons.lang3.StringUtils.isNumeric(cell.getStringCellValue())) {
                                Double d = Double.valueOf(cell.getStringCellValue());
                                BeanUtils.setProperty(value, fields.get(c), d);
                            }
                        } else {
                            BeanUtils.setProperty(value, fields.get(c), cell.getStringCellValue());
                        }
                        break;
                    }
                    ((Map) value).put(fields.get(c), cell.getStringCellValue());
                    break;
                case BLANK:
                    break;
                case BOOLEAN:
                    break;
                case ERROR:
                    break;
                default:
            }
        }

    }
}
