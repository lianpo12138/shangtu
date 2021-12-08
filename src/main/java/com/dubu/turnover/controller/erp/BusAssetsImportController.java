package com.dubu.turnover.controller.erp;

import com.dubu.turnover.service.BusAssetsAuditService;
import com.dubu.turnover.service.BusAssetsService;
import com.dubu.turnover.service.BusAttachmentsService;
import com.dubu.turnover.service.BusConsumeStoreService;
import com.dubu.turnover.service.BusConsumeUseService;
import com.dubu.turnover.service.BusTaskAuditService;
import com.dubu.turnover.service.BusTaskService;
import com.dubu.turnover.service.SysDictionaryService;
import com.dubu.turnover.utils.DateUtil;
import com.dubu.turnover.utils.ExcelTemplate;
import com.dubu.turnover.utils.ExcelUtils;
import com.dubu.turnover.vo.OptConsumeNumberVo;
import com.dubu.turnover.vo.OptConsumeUseVo;
import com.dubu.turnover.domain.UserInfo;
import com.dubu.turnover.domain.entity.BusAssets;
import com.dubu.turnover.domain.entity.BusAssetsAudit;
import com.dubu.turnover.domain.entity.BusAttachments;
import com.dubu.turnover.domain.entity.BusConsumeStore;
import com.dubu.turnover.domain.entity.BusConsumeUse;
import com.dubu.turnover.domain.entity.BusTask;
import com.dubu.turnover.domain.entity.BusTaskAudit;
import com.dubu.turnover.domain.entity.SysDictionary;
import com.dubu.turnover.domain.entity.SysRole;
import com.dubu.turnover.annotation.DateEnum;
import com.dubu.turnover.annotation.NoLogin;
import com.dubu.turnover.component.ThreadRequestContext;
import com.dubu.turnover.component.config.SysConfig;
import com.dubu.turnover.core.Result;
import com.dubu.turnover.core.ResultGenerator;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/*
 *  @author: smart boy
 *  @date: 2021-03-13
 */
@RestController
@RequestMapping("/erp/busassetsimport")
@Api(value = "固定资产导入", tags = "固定资产导入")
public class BusAssetsImportController {

    @Resource
    private BusAssetsService busAssetsService;

    @Resource
    private BusTaskService busTaskService;

    @Autowired
    SysConfig sysConfig;

    @Resource
    private BusAssetsAuditService busAssetsAuditService;


    @InitBinder
    public void initDateFormate(WebDataBinder dataBinder) {
        dataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
    }

    private static LinkedHashMap<String, String> listExcelMap = new LinkedHashMap<String, String>() {
        {
            put("assetsNo", "资产编号");
            put("assistNo", "辅助编号");
            put("assetsBelongName", "资产归属");
            put("assetsName", "资产名称");
            put("useDeptName", "使用部门");
            put("financeStatus", "登账状态");
            put("cardId", "卡片编号");
            put("oriAssetsNo", "原资产编号");
            put("bookAssetsNo", "图情资产编号");
            put("assetsCategoryName", "资产大类");
            put("assetsDetailCategoryName", "资产分类");
            put("buyDate", "购置日期");
            put("financeDate", "财务入账日期");
            put("price", "单价");
            put("number", "数量");
            put("useUserName", "使用人");
            put("useStatus", "使用状况");
            put("depositAddress", "存放地点");
            put("assetsModel", "规格型号");
            put("useWay", "用途");
            put("cardAddress", "卡片路径");

        }
    };

    /**
     * 导入资产
     *
     * @param file
     * @return
     */
    @PostMapping("/import")
    @ApiOperation(value = "馆所资产导入", notes = "馆所资产导入")
    public Result importContract(@RequestParam("file") MultipartFile file) {
        try {
            UserInfo userInfo = ThreadRequestContext.current();
            String str = busAssetsService.importAssets(file, userInfo);
            return ResultGenerator.success(str);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.error(e.getMessage());
        }
    }


    /**
     * 导入资产
     *
     * @param file
     * @return
     */
    @PostMapping("/companyImport")
    @ApiOperation(value = "图情资产导入", notes = "图情资产导入")
    public Result companyImport(@RequestParam("file") MultipartFile file) {
        try {
            UserInfo userInfo = ThreadRequestContext.current();
            String str = busAssetsService.importCompanyAssets(file, userInfo);
            return ResultGenerator.success(str);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.error(e.getMessage());
        }
    }

    @ApiOperation(value = "固定资产导出", notes = "固定资产导出")
    @GetMapping("/export")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "assetsNo", value = "资产编号", dataType = "String"),
            @ApiImplicitParam(name = "consumeName", value = "资产名称", dataType = "String")
    })
    public Result export(
            @RequestParam(required = false) String assetsNo,
            @RequestParam(required = false) String assetsName,
            @RequestParam(required = false) Integer assetsBelongId,
            @RequestParam(required = false) String useDeptName,
            @RequestParam(required = false) Date createStartDate,
            @RequestParam(required = false) Date createEndDate,
            @RequestParam(required = false) String financeStatus,
            @RequestParam(required = false) String cardId,
            @RequestParam(required = false) String oriAssetsNo,
            @RequestParam(required = false) String bookAssetsNo,
            @RequestParam(required = false) Integer assetsCategoryId,
            @RequestParam(required = false) Integer assetsDetailCategoryId,
            @RequestParam(required = false) Date buyStartDate,
            @RequestParam(required = false) Date buyEndDate,
            @RequestParam(required = false) Date financeStartDate,
            @RequestParam(required = false) Date financeEndDate,
            @RequestParam(required = false) Integer number,
            @RequestParam(required = false) String useWay,
            @RequestParam(required = false) String deptName,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String useStatus,
            @RequestParam(required = false) String cardAddress,
            @RequestParam(required = false) String useUserName,
            @RequestParam(required = false) Date useStartDate,
            @RequestParam(required = false) Date useEndDate,
            @RequestParam(required = false) String remarks,
            @RequestParam(required = false) String depositAddress,
            @RequestParam(required = false) String assetsModel
    ) throws IOException {
        UserInfo userInfo = ThreadRequestContext.current();
        PageHelper.startPage(1, 10000);
        Condition condition = new Condition(BusAssets.class);
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtil.isEmpty(assetsNo)) {
            criteria.andEqualTo("assetsNo", assetsNo);
        }
        if (!StringUtil.isEmpty(assetsName)) {
            criteria.andLike("assetsName", "%" + assetsName + "%");
        }
        if (assetsBelongId != null) {
            criteria.andEqualTo("assetsBelongId", assetsBelongId);
        }
        if (StringUtil.isEmpty(useDeptName)) {
            if (!userInfo.getDeptIdList().contains(100)) {
                criteria.andIn("useDeptId", userInfo.getDeptIdList());
            }
        } else {
            criteria.andEqualTo("useDeptName", useDeptName);
        }
        if (assetsBelongId != null) {
            criteria.andEqualTo("assetsBelongId", assetsBelongId);
        }
        if (createStartDate != null) {
            criteria.andGreaterThan("createDate", createStartDate);
        }
        if (createEndDate != null) {
            criteria.andLessThan("createDate", createEndDate);
        }
        if (!StringUtil.isEmpty(financeStatus)) {
            criteria.andEqualTo("financeStatus", financeStatus);
        }
        if (!StringUtil.isEmpty(cardId)) {
            criteria.andEqualTo("cardId", cardId);
        }
        if (!StringUtil.isEmpty(oriAssetsNo)) {
            criteria.andEqualTo("oriAssetsNo", oriAssetsNo);
        }
        if (!StringUtil.isEmpty(bookAssetsNo)) {
            criteria.andEqualTo("bookAssetsNo", bookAssetsNo);
        }
        if (assetsCategoryId != null) {
            criteria.andEqualTo("assetsCategoryId", assetsCategoryId);
        }
        if (assetsDetailCategoryId != null) {
            criteria.andEqualTo("assetsDetailCategoryId", assetsDetailCategoryId);
        }
        if (buyStartDate != null) {
            criteria.andGreaterThan("buyDate", buyStartDate);
        }
        if (buyEndDate != null) {
            criteria.andLessThan("buyDate", buyEndDate);
        }
        if (financeStartDate != null) {
            criteria.andGreaterThan("financeDate", financeStartDate);
        }
        if (financeEndDate != null) {
            criteria.andLessThan("financeDate", financeEndDate);
        }
        if (number != null) {
            criteria.andEqualTo("number", number);
        }
        if (!StringUtil.isEmpty(useWay)) {
            criteria.andEqualTo("useWay", useWay);
        }
        if (!StringUtil.isEmpty(deptName)) {
            criteria.andEqualTo("deptName", deptName);
        }
        if (!StringUtil.isEmpty(userName)) {
            criteria.andEqualTo("userName", userName);
        }
        if (!StringUtil.isEmpty(useStatus)) {
            criteria.andEqualTo("useStatus", useStatus);
        }
        if (!StringUtil.isEmpty(cardAddress)) {
            criteria.andEqualTo("cardAddress", cardAddress);
        }
        if (!StringUtil.isEmpty(useUserName)) {
            criteria.andLike("useUserName", "%" + useUserName + "%");
        }
        if (useStartDate != null) {
            criteria.andGreaterThan("useStartTime", DateTime.now().toDate());
        }
        if (useEndDate != null) {
            criteria.andLessThan("useStartTime", DateTime.now().toDate());
        }
        if (!StringUtil.isEmpty(remarks)) {
            criteria.andLike("remarks", "%" + remarks + "%");
        }
        if (!StringUtil.isEmpty(depositAddress)) {
            criteria.andLike("depositAddress", "%" + depositAddress + "%");
        }
        if (!StringUtil.isEmpty(assetsModel)) {
            criteria.andLike("assetsModel", "%" + assetsModel + "%");
        }
        condition.orderBy("createTime").desc();
        try {
            List<BusAssets> list = busAssetsService.selectByCondition(condition);
            if (list.size() > 0) {
                ExcelUtils.ExcelExporter excelExporter = ExcelUtils.newExcelExport(BusAssets.class, listExcelMap,
                        Integer.MAX_VALUE);
                list.forEach(excelExporter::write);
                String date = DateUtil.getNow(DateEnum.UNSIGNED_DATE);
                return ResultGenerator.success(excelExporter.ossExcelFile(sysConfig.getShowUrl(), sysConfig.getFileUrl(), date + "图情公司资产和馆所资产导出清单.xlsx"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultGenerator.error();
    }

    @ApiOperation(value = "下放导出管所资产", notes = "下放导出管所资产")
    @GetMapping("/transferCompanyExport")
    @NoLogin
    public Result transferCompanyExport(HttpServletResponse response, HttpServletRequest request,
                                        @RequestParam(required = false) String ids) {
        List<BusAssets> list = busAssetsService.selectByIds(ids);
        try {
            ExcelTemplate excel = new ExcelTemplate(sysConfig.getFileUrl() + File.separator + "temp" + File.separator
                    + "librarysign.xls");
            // ExcelTemplate excel = new ExcelTemplate("D://公司资产下放签字单.xls");
            LinkedHashMap<Integer, LinkedList<String>> rows = new LinkedHashMap<>();
            // 创建第一个行区域里面填充的值，ExcelTemplate会按从左至右，
            // 从上往下的顺序，挨个填充区域里面的${}，所以创建的时候注意顺序就好
            int j = 1;
            for (BusAssets att : list) {
                LinkedList<String> row1 = new LinkedList<>();
                row1.add(String.valueOf(j));
                row1.add(att.getAssetsNo() != null ? att.getAssetsNo() : "");
                row1.add(att.getAssetsName());
                row1.add(att.getAssetsModel() != null ? att.getAssetsModel() : "");
                row1.add(att.getFinanceDate() != null ? DateUtil.parseDate(att.getFinanceDate(), DateEnum.DATE) : "");
                row1.add(att.getPrice() != null ? att.getPrice().toString() : "");
                BusAssetsAudit audit = busAssetsAuditService.getAssetsAuditById(att.getId());
                if (audit != null && att.getStatus() == 2) {
                    row1.add(audit.getReceiveDeptName() != null ? audit.getReceiveDeptName() : "");
                    row1.add(audit.getDepositAddress() != null ? audit.getDepositAddress() : "");
                    row1.add(audit.getUseUserName() != null ? audit.getUseUserName() : "");
                    row1.add(att.getUserName() != null ? att.getUserName() : "");
                } else {
                    row1.add("");
                    row1.add("");
                    row1.add("");
                    row1.add(att.getUserName() != null ? att.getUserName() : "");
                }
                rows.put(j, row1);
                j++;
            }
            excel.addRowByExist(0, 2, 2, 3, rows, true);
            String ad = UUID.randomUUID().toString();
            File file2 = new File(sysConfig.getFileUrl() + File.separator + "excel" + File.separator + ad + File.separator);
            if (file2.exists() == false) {
                file2.mkdirs();
            }
            excel.save(sysConfig.getFileUrl() + File.separator + "excel" + File.separator + ad + File.separator
                    + "馆所资产下放签字单.xls");
            return ResultGenerator.success(sysConfig.getShowUrl() + File.separator + "excel" + File.separator + ad
                    + File.separator + "馆所资产下放签字单.xls");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultGenerator.error();
    }

    @ApiOperation(value = "下放导出公司资产", notes = "下放导出公司资产")
    @GetMapping("/transferLibraryExport")
    public Result transferLibraryExport(HttpServletResponse response, HttpServletRequest request,
                                        @RequestParam(required = false) String ids) {
        List<BusAssets> list = busAssetsService.selectByIds(ids);
        try {
            //File file =new File("D://公司资产下放签字单.xls");
            // 加载导出模板
            ExcelTemplate excel = new ExcelTemplate(sysConfig.getFileUrl() + File.separator + "temp" + File.separator + "companysign.xls");
            //ExcelTemplate excel = new ExcelTemplate("D://公司资产下放签字单.xls");
            LinkedHashMap<Integer, LinkedList<String>> rows = new LinkedHashMap<>();
            // 创建第一个行区域里面填充的值，ExcelTemplate会按从左至右，
            // 从上往下的顺序，挨个填充区域里面的${}，所以创建的时候注意顺序就好
            int j = 1;
            for (BusAssets att : list) {
                LinkedList<String> row1 = new LinkedList<>();
                row1.add(String.valueOf(j));
                row1.add(att.getBookAssetsNo() != null ? att.getBookAssetsNo() : "");
                row1.add(att.getAssetsName());
                row1.add(att.getAssetsModel() != null ? att.getAssetsModel() : "");
                row1.add(att.getFinanceDate() != null ? DateUtil.parseDate(att.getFinanceDate(), DateEnum.DATE) : "");
                row1.add(att.getPrice() != null ? att.getPrice().toString() : "");
                BusAssetsAudit audit = busAssetsAuditService.getAssetsAuditById(att.getId());
                if (audit != null && att.getStatus() == 2) {
                    row1.add(audit.getReceiveDeptName() != null ? audit.getReceiveDeptName() : "");
                    row1.add(audit.getDepositAddress() != null ? audit.getDepositAddress() : "");
                    row1.add(audit.getUseUserName() != null ? audit.getUseUserName() : "");
                    row1.add(att.getUserName() != null ? att.getUserName() : "");
                } else {
                    row1.add("");
                    row1.add("");
                    row1.add("");
                    row1.add(att.getUserName() != null ? att.getUserName() : "");
                }
                rows.put(j, row1);
                j++;
            }
/**
 * 使用一个已经存在的行区域作为模板，
 * 从sheet的toRowNum行开始插入这段行区域,
 * areaValue会从左至右，从上至下的替换掉row区域
 * 中值为 ${} 的单元格的值
 *
 * @param sheetNo 需要操作的Sheet的编号
 * @param fromRowStartIndex 模板row区域的开始索引
 * @param fromRowEndIndex 模板row区域的结束索引
 * @param toRowIndex 开始插入的row索引
 * @param areaValues 替换模板row区域的${}值
 * @param delRowTemp 是否删除模板row区域
 * @return int 插入的行数量
 * @throws IOException
 * */
            excel.addRowByExist(0, 2, 2, 3, rows, true);
            String ad = UUID.randomUUID().toString();
            File file2 = new File(sysConfig.getFileUrl() + File.separator + "excel" + File.separator + ad + File.separator);
            if (file2.exists() == false) {
                file2.mkdirs();
            }
            excel.save(sysConfig.getFileUrl() + File.separator + "excel" + File.separator + ad + File.separator + "公司资产下放签字单.xls");
            return ResultGenerator.success(sysConfig.getShowUrl() + File.separator + "excel" + File.separator + ad + File.separator + "公司资产下放签字单.xls");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultGenerator.error();
    }


    @ApiOperation(value = "交接导出资产", notes = "下交接导出资产")
    @GetMapping("/handoverExport")
    @NoLogin
    public Result handoverExport(HttpServletResponse response, HttpServletRequest request,
                                 @RequestParam(required = false) String ids) {
        List<BusAssets> list = busAssetsService.selectByIds(ids);
        try {
            ExcelTemplate excel = new ExcelTemplate(sysConfig.getFileUrl() + File.separator + "temp" + File.separator + "handoversign.xlsx");
            BusAssetsAudit audit = busAssetsAuditService.getAssetsAuditById(list.get(0).getId());

            Map<String, String> fillValues = new HashMap<String, String>();
            fillValues.put("dept", list.get(0).getUseDeptName());
            fillValues.put("userName", list.get(0).getUseUserName());
            if (audit != null && audit.getReceiveDeptName() != null) {
                fillValues.put("newDept", audit.getReceiveDeptName());
            } else {
                fillValues.put("newDept", "");
            }
            if (audit != null && audit.getUseUserName() != null) {
                fillValues.put("newUserName", audit.getUseUserName());
            } else {
                fillValues.put("newUserName", "");
            }
            excel.fillVariable(fillValues);

            LinkedHashMap<Integer, LinkedList<String>> rows2 = new LinkedHashMap<>();
            int i = 1;
            int j = 1;
            for (BusAssets att : list) {
                att.setId(i);
                LinkedList<String> row1 = new LinkedList<>();
                row1.add(att.getAssetsNo() != null ? att.getAssetsNo() : "");
                row1.add(att.getAssetsName());
                row1.add(att.getAssetsModel() != null ? att.getAssetsModel() : "");
                row1.add(att.getAssetsDetailCategoryName() != null ? att.getAssetsDetailCategoryName() : "");
                row1.add(att.getPrice() != null ? att.getPrice().toString() : "");
                row1.add(att.getNumber() != null ? att.getNumber().toString() : "");
                row1.add(att.getDepositAddress() != null ? att.getDepositAddress() : "");
                row1.add(att.getUseUserName() != null ? att.getUseUserName() : "");
                row1.add(att.getUserName() != null ? att.getUserName() : "");
                row1.add(att.getRemarks());
                rows2.put(j, row1);
                i++;
                j++;
            }
            excel.addRowByExist(0, 4, 4, 5, rows2, true);
            String ad = UUID.randomUUID().toString();
            File file2 = new File(sysConfig.getFileUrl() + File.separator + "excel" + File.separator + ad + File.separator);
            if (file2.exists() == false) {
                file2.mkdirs();
            }
            excel.save(sysConfig.getFileUrl() + File.separator + "excel" + File.separator + ad + File.separator + "资产交接单.xls");
            return ResultGenerator.success(sysConfig.getShowUrl() + File.separator + "excel" + File.separator + ad
                    + File.separator + "资产交接单.xls");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultGenerator.error();
    }


    @ApiOperation(value = "管所报废导出资产", notes = "管所报废接导出资产")
    @GetMapping("/scrapExport")
    public Result scrapExport(HttpServletResponse response, HttpServletRequest request,
                              @RequestParam(required = false) String ids) {
        List<BusAssets> list = busAssetsService.selectByIds(ids);
        try {
            ExcelTemplate excel = new ExcelTemplate(sysConfig.getFileUrl() + File.separator + "temp" + File.separator
                    + "scrapsign.xls");
            Map<String, String> fillValues = new HashMap<String, String>();
            fillValues.put("dept", list.get(0).getUseDeptName() != null ? list.get(0).getUseDeptName() : "");
            excel.fillVariable(fillValues);
            LinkedHashMap<Integer, LinkedList<String>> rows = new LinkedHashMap<>();
            int j = 1;
            for (BusAssets att : list) {
                LinkedList<String> row1 = new LinkedList<>();
                row1.add(att.getAssetsNo() != null ? att.getAssetsNo() : "");
                row1.add(att.getAssetsName());
                row1.add(att.getPrice() != null ? att.getPrice().toString() : "");
                row1.add(att.getBuyDate() != null ? DateUtil.parseDate(att.getBuyDate(), DateEnum.DATE) : "");
                row1.add(att.getUseUserName() != null ? att.getUseUserName() : "");
                row1.add(att.getDepositAddress());
                rows.put(j, row1);
                j++;
            }
            excel.addRowByExist(0, 3, 3, 4, rows, true);
            String ad = UUID.randomUUID().toString();
            File file2 = new File(sysConfig.getFileUrl() + File.separator + "excel" + File.separator + ad + File.separator);
            if (file2.exists() == false) {
                file2.mkdirs();
            }
            excel.save(sysConfig.getFileUrl() + File.separator + "excel" + File.separator + ad + File.separator
                    + "馆所资产报废申报单据.xls");
            return ResultGenerator.success(sysConfig.getShowUrl() + File.separator + "excel" + File.separator + ad
                    + File.separator + "馆所资产报废申报单据.xls");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultGenerator.error();
    }

    @ApiOperation(value = "图情公司报废导出资产", notes = "图情公司报废接导出资产")
    @GetMapping("/scrapCompanyExport")
    public Result scrapCompanyExport(HttpServletResponse response, HttpServletRequest request,
                                     @RequestParam(required = false) String ids) {
        List<BusAssets> list = busAssetsService.selectByIds(ids);
        try {
            ExcelTemplate excel = new ExcelTemplate(sysConfig.getFileUrl() + File.separator + "temp" + File.separator
                    + "companyscrapsign.xls");
            Map<String, String> fillValues = new HashMap<String, String>();
            fillValues.put("dept", list.get(0).getUseDeptName() != null ? list.get(0).getUseDeptName() : "");
            excel.fillVariable(fillValues);
            LinkedHashMap<Integer, LinkedList<String>> rows = new LinkedHashMap<>();
            int j = 1;
            for (BusAssets att : list) {
                LinkedList<String> row1 = new LinkedList<>();
                row1.add(att.getAssetsNo() != null ? att.getAssetsNo() : "");
                row1.add(att.getAssetsName());
                row1.add(att.getPrice() != null ? att.getPrice().toString() : "");
                row1.add(att.getFinanceDate() != null ? DateUtil.parseDate(att.getFinanceDate(), DateEnum.DATE) : "");
                row1.add(att.getUseUserName() != null ? att.getUseUserName() : "");
                row1.add(att.getDepositAddress());
                row1.add(att.getDecrease() != null ? att.getDecrease() : "");
                row1.add(att.getExpireYear() != null ? att.getExpireYear() : "");
                row1.add(att.getNetWorth() != null ? att.getNetWorth() : "");
                rows.put(j, row1);
                j++;
            }
            excel.addRowByExist(0, 3, 3, 4, rows, true);
            String ad = UUID.randomUUID().toString();
            File file2 = new File(sysConfig.getFileUrl() + File.separator + "excel" + File.separator + ad + File.separator);
            if (file2.exists() == false) {
                file2.mkdirs();
            }
            excel.save(sysConfig.getFileUrl() + File.separator + "excel" + File.separator + ad + File.separator
                    + "图情公司报废申报单据.xls");
            return ResultGenerator.success(sysConfig.getShowUrl() + File.separator + "excel" + File.separator + ad
                    + File.separator + "图情公司报废申报单据.xls");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultGenerator.error();
    }
}

