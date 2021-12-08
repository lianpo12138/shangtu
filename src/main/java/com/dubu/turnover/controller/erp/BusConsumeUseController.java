package com.dubu.turnover.controller.erp;

import com.dubu.turnover.domain.entity.*;
import com.dubu.turnover.mapper.BusConsumeProjectMapper;
import com.dubu.turnover.mapper.BusConsumeUseMapper;
import com.dubu.turnover.service.BusAssetsService;
import com.dubu.turnover.service.BusAttachmentsService;
import com.dubu.turnover.service.BusConsumeStoreService;
import com.dubu.turnover.service.BusConsumeUseService;
import com.dubu.turnover.service.BusTaskAuditService;
import com.dubu.turnover.service.BusTaskRelationService;
import com.dubu.turnover.service.BusTaskService;
import com.dubu.turnover.service.SysAdminService;
import com.dubu.turnover.service.SysDictionaryService;
import com.dubu.turnover.utils.DateUtil;
import com.dubu.turnover.utils.ExcelTemplate;
import com.dubu.turnover.utils.ExcelUtils;
import com.dubu.turnover.utils.WordUtils;
import com.dubu.turnover.vo.OptConsumeNumberVo;
import com.dubu.turnover.vo.OptConsumeUseVo;
import com.dubu.turnover.vo.TableForm;
import com.dubu.turnover.domain.UserInfo;
import com.dubu.turnover.annotation.DateEnum;
import com.dubu.turnover.annotation.NoLogin;
import com.dubu.turnover.component.ThreadRequestContext;
import com.dubu.turnover.component.config.SysConfig;
import com.dubu.turnover.component.redis.RedisClient;
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
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
@RequestMapping("/erp/busconsumeuse")
@Api(value = "耗材领用", tags = "耗材领用")
public class BusConsumeUseController {

    @Resource
    private BusConsumeUseService busConsumeUseService;

    @Resource
    private BusAttachmentsService busAttachmentsService;

    @Resource
    private BusTaskAuditService busTaskAuditService;

    @Resource
    private SysDictionaryService sysDictionaryService;

    @Resource
    private BusConsumeStoreService busConsumeStoreService;

    @Autowired
    SysConfig sysConfig;

    @Autowired
    private RedisClient redisClient;

    @Resource
    private BusAssetsService busAssetsService;

    @Resource
    private BusTaskRelationService busTaskRelationService;

    @Resource
    private BusTaskService busTaskService;

    @Resource
    private SysAdminService sysAdminService;

    @Autowired
    private BusConsumeProjectMapper busConsumeProjectMapper;


    @InitBinder
    public void initDateFormate(WebDataBinder dataBinder) {
        dataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
    }

//    @GetMapping("/test")
////    public Result search(@RequestParam(required = false) int[] fund) {
////		Condition condition = new Condition(BusConsumeProject.class);
////		if (fund != null) {
////			Example.Criteria criteria = condition.createCriteria();
////			for (int i : fund) {
////				criteria.orEqualTo("fundId",i);
////			}
////		}
////        List<BusConsumeProject> busConsumeProjects = busConsumeProjectMapper.selectByCondition(condition);
////		List<Integer> collect = busConsumeProjects.stream().map(BusConsumeProject::getId).collect(Collectors.toList());
////
////		Result result = new Result();
////		result.setData(collect);
////		return result;
////    }
////    @GetMapping("/hello")
////    public Result hello(@RequestParam List<Integer> ids) {
////		Condition condition1 = new Condition(BusConsumeProject.class);
////		Example.Criteria criteria1 = condition1.createCriteria();
////		criteria1.andIn("fundId", ids);
////		List<BusConsumeProject> busConsumeProjects = busConsumeProjectMapper.selectByCondition(condition1);
////		Result result = new Result();
////		result.setData(busConsumeProjects);
////		return result;
////    }

    @ApiOperation(value = "耗材领用记录", notes = "耗材领用记录")
    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "page", dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "size", dataType = "Integer"),
            @ApiImplicitParam(name = "consumeName", value = "资产名称", dataType = "String"),
            @ApiImplicitParam(name = "consumeNo", value = "资产名称", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态", dataType = "String"),
            @ApiImplicitParam(name = "orderBy", value = "排序字段", dataType = "String"),
            @ApiImplicitParam(name = "asc", value = "排序方式:asc和desc", dataType = "String"),
            @ApiImplicitParam(name = "createStartDate", value = "录入开始时间", dataType = "Date"),
            @ApiImplicitParam(name = "createEndDate", value = "录入结束时间", dataType = "Date")
    })
    public Result list(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize,
                       @RequestParam(required = false) String consumeName,
                       @RequestParam(required = false) String consumeNo,
                       @RequestParam(required = false) String status,
                       @RequestParam(required = false) Date createStartDate,
                       @RequestParam(required = false) Date createEndDate,
                       @RequestParam(required = false) List<Integer> fund,
                       @RequestParam(required = false) String orderBy,
                       @RequestParam(required = false) String asc) {
        UserInfo userInfo = ThreadRequestContext.current();

        Condition condition = new Condition(BusConsumeUse.class);
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(consumeName)) {
            criteria.andLike("consumeName", "%" + consumeName + "%");
        }
        if (!StringUtils.isEmpty(consumeNo)) {
            criteria.andEqualTo("consumeNo", consumeNo);
        }
        if (status != null) {
            List<Integer> list = Arrays.stream(status.split(",")).map(s -> Integer.valueOf(s.trim())).collect(Collectors.toList());
            criteria.andIn("status", list);
        }
        if (createStartDate != null) {
            criteria.andGreaterThan("createTime", createStartDate);
        }
        if (createEndDate != null) {
            criteria.andLessThan("createTime", DateUtil.getDateEnd(createEndDate));
        }
        if (!userInfo.getDeptIdList().contains(100)) {
            criteria.andIn("deptId", userInfo.getDeptIdList());
        }
        if (userInfo.getDeptIdList().contains(26) || userInfo.getDeptIdList().contains(37)) {
            criteria.andEqualTo("useUserId", userInfo.getId().intValue());
        }
        if (fund!=null&&!fund.isEmpty()) {
            Condition condition1 = new Condition(BusConsumeProject.class);
            Example.Criteria criteria1 = condition1.createCriteria();
            criteria1.andIn("fundId", fund);
            List<BusConsumeProject> projects = busConsumeProjectMapper.selectByCondition(condition1);
            List<Integer> ids = projects.stream().map(BusConsumeProject::getId).collect(Collectors.toList());
            if (ids.size() == 0) {
                ids.add(0);
            }

            criteria.andIn("projectId", ids);
        }
        if (!StringUtil.isEmpty(orderBy)) {
            if ("asc".equals(asc)) {
                condition.orderBy(orderBy).asc();
            } else {
                condition.orderBy(orderBy).desc();
            }
        } else {
            condition.orderBy("createTime").desc();
        }
        Page<Object> objects = PageHelper.startPage(page, pageSize);
        List<BusConsumeUse> list = busConsumeUseService.selectByCondition(condition);
        PageInfo<BusConsumeUse> pageInfo = new PageInfo<>(list);
        return ResultGenerator.success(pageInfo);
    }

    @ApiOperation(value = "耗材领用", notes = "耗材领用")
    @GetMapping("/useList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "page", dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "size", dataType = "Integer"),
            @ApiImplicitParam(name = "consumeNo", value = "资产编号", dataType = "String"),
            @ApiImplicitParam(name = "consumeName", value = "资产名称", dataType = "String"),
            @ApiImplicitParam(name = "orderBy", value = "排序字段", dataType = "String"),
            @ApiImplicitParam(name = "asc", value = "排序方式:asc和desc", dataType = "String")
    })
    public Result useList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size,
                          @RequestParam(required = false) String consumeNo,
                          @RequestParam(required = false) String consumeName,
                          @RequestParam(required = false) String orderBy,
                          @RequestParam(required = false) String asc) {
        UserInfo userInfo = ThreadRequestContext.current();
        PageHelper.startPage(page, size);
        Condition condition = new Condition(BusConsumeStore.class);
        if (!StringUtils.isEmpty(consumeNo)) {
            condition.createCriteria().andEqualTo("consumeNo", consumeNo);
        }
        if (!StringUtils.isEmpty(consumeName)) {
            condition.createCriteria().andLike("consumeName", "%" + consumeName + "%");
        }
        if (!userInfo.getDeptIdList().contains(100)) {
            condition.createCriteria().andIn("deptId", userInfo.getDeptIdList());
        }
        if (!StringUtil.isEmpty(orderBy)) {
            if ("asc".equals(asc)) {
                condition.orderBy(orderBy).asc();
            } else {
                condition.orderBy(orderBy).desc();
            }
        } else {
            condition.orderBy("createTime").desc();
        }
        List<BusConsumeStore> list = busConsumeStoreService.selectByCondition(condition);
        PageInfo<BusConsumeStore> pageInfo = new PageInfo<>(list);
        return ResultGenerator.success(pageInfo);
    }

    @ApiOperation(value = "耗材新增领用", notes = "耗材新增领用")
    @PostMapping("/add")
    public Result add(@RequestBody OptConsumeUseVo optConsumeUseVo) {
        try {
            UserInfo userInfo = ThreadRequestContext.current();
            Integer isWenxin = 0;
            List<String> ids = new ArrayList<String>();
            for (OptConsumeNumberVo vo : optConsumeUseVo.getNumList()) {
                BusConsumeStore store = busConsumeStoreService.selectById(vo.getId());
                if ("1".equals(store.getIsAccept()) && userInfo.getDeptIdList().get(0) == 8) {
                    isWenxin = 1;
                }
                if (store.getApplyNumber() + vo.getNumber() > store.getNumber()) {
                    return ResultGenerator.error("领用数量加冻结数量不能超过库存数量");
                }
                ids.add(store.getIsAccept());
            }
            if (optConsumeUseVo.getAssetsNo() != null &&
                    !"".equals(optConsumeUseVo.getAssetsNo())) {
                Condition condition = new Condition(BusAssets.class);
                condition.createCriteria().andEqualTo("assetsNo", optConsumeUseVo.getAssetsNo());
                Integer count = busAssetsService.selectCountByCondition(condition);
                if (count == 0) {
                    return ResultGenerator.error("该资产编号在系统中不存在,不能领用");
                }
            }
//    		if(optConsumeUseVo.getAssetsNo()!=null){
//            	Condition condition = new Condition(BusConsumeUse.class);
//        		condition.createCriteria().andEqualTo("assetsNo", optConsumeUseVo.getAssetsNo());
//        		Integer count  = busConsumeUseService.selectCountByCondition(condition);
//        		if(count>0){
//        		    return ResultGenerator.error("该资产编号已经领用,不能领用");
//        		}	
//    		}
            try {
                boolean lock = redisClient.getLock("task:use", 2000, 2000, 30 * 1000);
                if (!lock) {
                    // redis 原子性，保证只有一个线程在执行
                    return ResultGenerator.error("该资产正在操作,你稍候在试");
                }
                redisClient.put("task:use", "lock");
                busConsumeUseService.saveConsume(optConsumeUseVo, userInfo, isWenxin, userInfo.getDeptIdList().get(0));

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 结束之后，删除key
                redisClient.releaseLock("task:use");
            }
            return ResultGenerator.success();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultGenerator.error();
    }


    @ApiOperation(value = "耗材领用详情", notes = "耗材领用详情")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Integer id) {
        Map<String, Object> map = new HashMap<String, Object>();
        BusConsumeUse consumeUse = busConsumeUseService.selectById(id);
        List<BusTaskAudit> auditList = busTaskAuditService.selectByTask(id, 2);
        List<BusAttachments> attList = busAttachmentsService.getAttachmentsList(id, 1);
        map.put("consumeUse", consumeUse);
        map.put("auditList", auditList);
        map.put("attList", attList);
        return ResultGenerator.success(map);
    }


    @ApiOperation(value = "耗材领用获取需要设置的附件", notes = "耗材领用获取需要设置的附件")
    @GetMapping("/getAttachments")
    public Result getAttachments() {
        Condition condition = new Condition(SysDictionary.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("dictType", "CONSUMEUSE");
        List<SysDictionary> list = sysDictionaryService.selectByCondition(condition);
        return ResultGenerator.success(list);
    }

    @ApiOperation(value = "耗材报表", notes = "耗材报表")
    @GetMapping("/report")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "page", dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "size", dataType = "Integer"),
            @ApiImplicitParam(name = "consumeNo", value = "资产编号", dataType = "String"),
            @ApiImplicitParam(name = "consumeName", value = "资产名称", dataType = "String"),
            @ApiImplicitParam(name = "userName", value = "用户名称", dataType = "String"),
            @ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "Date"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", dataType = "Date"),
            @ApiImplicitParam(name = "orderBy", value = "排序字段", dataType = "String"),
            @ApiImplicitParam(name = "asc", value = "排序方式:asc和desc", dataType = "String")
    })
    public Result report(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size,
                         @RequestParam(required = false) String consumeNo,
                         @RequestParam(required = false) String consumeName,
                         @RequestParam(required = false) String userName,
                         @RequestParam(required = false) Date startDate,
                         @RequestParam(required = false) Date endDate,
                         @RequestParam(required = false) Integer deptId,
                         @RequestParam(required = false) String orderBy,
                         @RequestParam(required = false) String asc) {
        UserInfo userInfo = ThreadRequestContext.current();
        PageHelper.startPage(page, size);
        Condition condition = new Condition(BusConsumeUse.class);
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(consumeName)) {
            criteria.andLike("consumeName", "%" + consumeName + "%");
        }
        if (!StringUtils.isEmpty(userName)) {
            criteria.andLike("userName", "%" + userName + "%");
        }
        if (!StringUtils.isEmpty(consumeNo)) {
            criteria.andEqualTo("consumeNo", consumeNo);
        }
        if (startDate != null) {
            criteria.andGreaterThan("createTime", startDate);
        }
        if (endDate != null) {
            criteria.andLessThan("createTime", DateUtil.getDateEnd(endDate));
        }
        if (deptId != null) {
            criteria.andEqualTo("deptId", deptId);
        } else {
            if (!userInfo.getDeptIdList().contains(100)) {
                condition.createCriteria().andIn("deptId", userInfo.getDeptIdList());
            }
        }
        if (!StringUtil.isEmpty(orderBy)) {
            if ("asc".equals(asc)) {
                condition.orderBy(orderBy).asc();
            } else {
                condition.orderBy(orderBy).desc();
            }
        } else {
            condition.orderBy("createTime").desc();
        }
        List<BusConsumeUse> list = busConsumeUseService.selectByCondition(condition);
        PageInfo<BusConsumeUse> pageInfo = new PageInfo<>(list);
        return ResultGenerator.success(pageInfo);
    }

    @ApiOperation(value = "耗材报表导出", notes = "耗材报表导出")
    @GetMapping("/export")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "consumeNo", value = "资产编号", dataType = "String"),
            @ApiImplicitParam(name = "consumeName", value = "资产名称", dataType = "String"),
            @ApiImplicitParam(name = "userName", value = "用户名称", dataType = "String"),
            @ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "Date"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", dataType = "Date")
    })
    public Result export(
            @RequestParam(required = false) String consumeNo,
            @RequestParam(required = false) String consumeName,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) Date startDate,
            @RequestParam(required = false) Date endDate,
            @RequestParam(required = false) Integer deptId) throws IOException {
        UserInfo userInfo = ThreadRequestContext.current();
        PageHelper.startPage(1, 10000);
        Condition condition = new Condition(BusConsumeUse.class);
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(consumeName)) {
            criteria.andLike("consumeName", "%" + consumeName + "%");
        }
        if (!StringUtils.isEmpty(userName)) {
            criteria.andLike("userName", "%" + userName + "%");
        }
        if (!StringUtils.isEmpty(consumeNo)) {
            criteria.andEqualTo("consumeNo", consumeNo);
        }
        if (startDate != null) {
            criteria.andGreaterThanOrEqualTo("createTime", DateTime.now().toDate());
        }
        if (endDate != null) {
            criteria.andLessThanOrEqualTo("createTime", DateTime.now().toDate());
        }
        if (deptId != null) {
            criteria.andEqualTo("deptId", deptId);
        } else {
            if (!userInfo.getDeptIdList().contains(100)) {
                criteria.andIn("deptId", userInfo.getDeptIdList());
            }
        }
        List<BusConsumeUse> list = busConsumeUseService.selectByCondition(condition);
        if (list.size() > 0) {
            ExcelUtils.ExcelExporter excelExporter = ExcelUtils.newExcelExport(BusConsumeUse.class, listExcelMap,
                    Integer.MAX_VALUE);
            list.forEach(excelExporter::write);
            return ResultGenerator.success(excelExporter.ossExcelFile(sysConfig.getShowUrl(), sysConfig.getFileUrl(), "报表.xlsx"));
        } else {
            return null;
        }
    }

    private static LinkedHashMap<String, String> listExcelMap = new LinkedHashMap<String, String>() {
        {
            put("projectName", "耗材对应项目");
            put("consumeName", "耗材名称");
            put("consumeModel", "耗材型号");
            //put("consumeCategoryName", "耗材分类");
            put("storeAddress", "存放地点");
            put("number", "领用耗材数量");
            put("purpose", "耗材用途");
            put("useUserName", "领用人");
            put("checkUserName", "验收人");
            put("auditUserName", "审批管理员");
        }
    };

    @ApiOperation(value = "下载领用签字单", notes = "下载领用签字单")
    @GetMapping("/useExport")
    public Result transferLibraryExport(HttpServletResponse response,
                                        HttpServletRequest request, Integer taskId) {
        UserInfo userInfo = ThreadRequestContext.current();
        SysAdmin admin = sysAdminService.selectById(userInfo.getId().intValue());
        BusTask task = busTaskService.selectById(taskId);
        List<BusTaskRelation> listRelation = busTaskRelationService.getRelationList(taskId);
        List<String> strList = listRelation.stream().map(BusTaskRelation::getTaskSourceId).map(String::valueOf).collect(Collectors.toList());
        List<BusConsumeUse> useList = busConsumeUseService.selectByIds(String.join(",", strList));
        String fileadress = "";
        if (task.getTaskType() == 5) {
            fileadress = sysConfig.getFileUrl() + File.separator + "temp" + File.separator + "use.docx";
        } else {
            fileadress = sysConfig.getFileUrl() + File.separator + "temp" + File.separator + "use2.docx";
        }
        try (XWPFDocument document = new XWPFDocument(new FileInputStream(fileadress))) {
            WordUtils wordUtils = new WordUtils();
            List<Map<String, String>> tableTextList = new ArrayList<>();
            Map<String, String> tableMap = new HashMap<>();
            tableMap.put("name", useList.get(0).getDeptName() != null ? useList.get(0).getDeptName() : "");
            tableMap.put("sexual", DateUtil.getNow(DateEnum.DATE));
            tableMap.put("address", useList.get(0).getUseUserName() != null ? useList.get(0).getUseUserName() : "");
            tableTextList.add(tableMap);
            Map<String, String> tableMap2 = new HashMap<>();
            tableTextList.add(tableMap2);
            wordUtils.changeTableText(document, tableTextList);

            List<TableForm> list = new ArrayList<>();
            TableForm tableForm = new TableForm();
            tableForm.setStartLine(2);
            for (BusConsumeUse use : useList) {
                if (task.getTaskType() == 5) {
                    tableForm.getData().add(new String[]{use.getProjectName(),
                            use.getConsumeName(), use.getConsumeType()
                            , use.getConsumeModel(), use.getNumber().toString(),
                            use.getStoreAddress()
                            , use.getPurpose()});
                } else {
                    tableForm.getData().add(new String[]{use.getProjectName(),
                            use.getConsumeName(), use.getConsumeType()
                            , use.getConsumeModel(), use.getNumber().toString(),
                            use.getStoreAddress(), use.getAssetsNo()
                            , use.getPurpose()});
                }
            }
            list.add(tableForm);
            TableForm tableForm2 = new TableForm();
            tableForm2.setStartLine(1);
            String useUserName = useList.get(0).getUseUserName();
            String checkUserName = useList.get(0).getCheckUserName() != null ? useList.get(0).getCheckUserName() : "";
            if (task.getTaskType() == 6) {
                tableForm2.getData().add(new String[]{"领用人签字:" +
                        useUserName, "验收人签字:  " + checkUserName});
                tableForm2.getData().add(new String[]{"领用日期:", "验收日期:"});
            } else {
                tableForm2.getData().add(new String[]{"领用人签字: " + useUserName
                        , ""});
                tableForm2.getData().add(new String[]{"领用日期:", ""});
            }
            List<BusTaskAudit> audit = busTaskAuditService.selectByAuditList(taskId);
            String auditUserName = "";
            String managerUserName = "";
            for (BusTaskAudit aud : audit) {
                if (aud.getProcessName().contains("文献班组负责人审核")) {
                    auditUserName = aud.getAuditUserName();
                    List<SysAdmin> t = sysAdminService.getAdminByRole(38);
                    if (t.size() == 1) {
                        managerUserName = t.get(0).getUserName();
                    }
                }
            }
            if (task.getTaskType() == 5) {
                if (task.getApplyDeptId() != 8) {
                    managerUserName = admin.getUserName();
                }
                if (task.getApplyDeptId() == 8) {
                    List<SysAdmin> t = sysAdminService.getAdminByRole(38);
                    if (t.size() == 1) {
                        managerUserName = t.get(0).getUserName();
                    }
                    auditUserName = admin.getUserName();
                }
            }
            tableForm2.getData().add(new String[]{"领用部门分管签字:  " + auditUserName,
                    "耗材管理员出库签字:  " + managerUserName});
            tableForm2.getData().add(new String[]{"日期:", "日期:"});
            list.add(tableForm2);
            wordUtils.copyHeaderInsertText(document, list);
            String ad = UUID.randomUUID().toString();
            String address = sysConfig.getFileUrl() + File.separator + "excel" + File.separator + ad + File.separator + "耗材领用记录.docx";
            //String address="d://ggg.docx";
            File file2 = new File(sysConfig.getFileUrl() + File.separator + "excel" + File.separator + ad + File.separator);
            if (file2.exists() == false) {
                file2.mkdirs();
            }
            try (FileOutputStream fileOut = new FileOutputStream(address)) {
                document.write(fileOut);
            }
            return ResultGenerator.success(sysConfig.getShowUrl() + File.separator + "excel" + File.separator + ad
                    + File.separator + "耗材领用记录.docx");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultGenerator.error();
    }

    @ApiOperation(value = "领用记录导出", notes = "领用记录导出")
    @GetMapping("/exportUse")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "consumeNo", value = "资产编号", dataType = "String"),
            @ApiImplicitParam(name = "consumeName", value = "资产名称", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态", dataType = "String"),
            @ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "Date"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", dataType = "Date")
    })
    public Result exportUse(
            @RequestParam(required = false) String consumeName,
            @RequestParam(required = false) String consumeNo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) List<Integer> fund,
            @RequestParam(required = false) Date createStartDate,
            @RequestParam(required = false) Date createEndDate
    ) throws IOException {
        UserInfo userInfo = ThreadRequestContext.current();
        PageHelper.startPage(1, 10000);
        Condition condition = new Condition(BusConsumeUse.class);
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(consumeName)) {
            criteria.andLike("consumeName", "%" + consumeName + "%");
        }
        if (status != null) {
            List<Integer> list = Arrays.stream(status.split(",")).map(s -> Integer.valueOf(s.trim())).collect(Collectors.toList());
            criteria.andIn("status", list);
        }
        if (!StringUtils.isEmpty(consumeNo)) {
            criteria.andEqualTo("consumeNo", consumeNo);
        }
        if (createStartDate != null) {
            criteria.andGreaterThanOrEqualTo("createTime", createStartDate);
        }
        if (createEndDate != null) {
            condition.createCriteria().andLessThanOrEqualTo("createTime", createEndDate);
        }
        if (fund != null && !fund.isEmpty()) {
            Condition condition1 = new Condition(BusConsumeProject.class);
            Example.Criteria criteria1 = condition1.createCriteria();
            criteria1.andIn("fundId", fund);
            List<BusConsumeProject> projects = busConsumeProjectMapper.selectByCondition(condition1);
            List<Integer> ids = projects.stream().map(BusConsumeProject::getId).collect(Collectors.toList());
            if (ids.size() == 0) {
                ids.add(0);
            }
            criteria.andIn("projectId", ids);
        }// 根据经费来源导出记录
        if (!userInfo.getDeptIdList().contains(100)) {
            criteria.andIn("deptId", userInfo.getDeptIdList());
        }
        if (userInfo.getDeptIdList().contains(26) || userInfo.getDeptIdList().contains(37)) {
            criteria.andEqualTo("useUserId", userInfo.getId().intValue());
        }
        List<BusConsumeUse> list = busConsumeUseService.selectByCondition(condition);
        if (list.size() > 0) {
            ExcelUtils.ExcelExporter excelExporter = ExcelUtils.newExcelExport(BusConsumeUse.class, listExcelUseMap,
                    Integer.MAX_VALUE);
            list.forEach(excelExporter::write);
            return ResultGenerator.success(excelExporter.ossExcelFile(sysConfig.getShowUrl(), sysConfig.getFileUrl(), "报表.xlsx"));
        } else {
            return null;
        }
    }

    private static LinkedHashMap<String, String> listExcelUseMap = new LinkedHashMap<String, String>() {
        {
            put("consumeName", "耗材、配件名称");
            put("consumeModel", "型号");
            put("consumeCategoryName", "分类");
            put("consumeType", "适用设备名称");
            put("assetsNo", "资产编号");
            put("number", "数量");
            put("useUserName", "领用人");
            put("createTime", "领用日期");
            put("remarks", "备注");
        }
    };
}

