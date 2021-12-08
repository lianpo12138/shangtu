package com.dubu.turnover.domain.entity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
/*
 *  @author: smart boy
 *  @date: 2021-03-13
 */
@Data
public class BusAssets implements java.io.Serializable {
	private static final long serialVersionUID = 5454155825314635342L;
	
	@Id
	@GeneratedValue(generator = "JDBC")
	private java.lang.Integer id;
	@ApiModelProperty(value="资产归属id")
	private java.lang.Integer assetsBelongId;
	@ApiModelProperty(value="资产归属名称")
	private java.lang.String assetsBelongName;
	@ApiModelProperty(value="管理部门id")
	private java.lang.Integer deptId;
	@ApiModelProperty(value="管理部门")
	private java.lang.String deptName;
	@ApiModelProperty(value="管理人")
	private java.lang.Integer userId;
	@ApiModelProperty(value="管理人名称")
	private java.lang.String userName;
	@ApiModelProperty(value="卡片编号")
	private java.lang.String cardId;
	@ApiModelProperty(value="资产编号")
	private java.lang.String assetsNo;
	@ApiModelProperty(value="辅助编号")
	private java.lang.String assistNo;
	@ApiModelProperty(value="图情资产编号")
	private java.lang.String bookAssetsNo;
	@ApiModelProperty(value="原始资产编号")
	private java.lang.String oriAssetsNo;
	@ApiModelProperty(value="财务日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date financeDate;	
	@ApiModelProperty(value="购买日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date buyDate;	
	@ApiModelProperty(value="资产名称")
	private java.lang.String assetsName;
	@ApiModelProperty(value="价值")
	private java.lang.String worth;
	@ApiModelProperty(value="使用开始时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date useStartTime;
	@ApiModelProperty(value="使用结束时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date useEndTime;
	@ApiModelProperty(value="使用状况")
	private java.lang.String useStatus;
	@ApiModelProperty(value="使用人id")
	private java.lang.Integer useUserId;
	@ApiModelProperty(value="使用人名称")
	private java.lang.String useUserName;
	@ApiModelProperty(value="使用部门id")
	private java.lang.Integer useDeptId;
	@ApiModelProperty(value="使用部门")
	private java.lang.String useDeptName;
	@ApiModelProperty(value="使用方向设备用途")
	private java.lang.String useWay;
	@ApiModelProperty(value="原存放地点")
	private java.lang.String oriDeposit;
	@ApiModelProperty(value="使用分类")
	private java.lang.String useCategoryId;
	@ApiModelProperty(value="使用分类名称")
	private java.lang.String useCategoryName;
	@ApiModelProperty(value="资产大类")
	private java.lang.String assetsCategoryId;
	@ApiModelProperty(value="资产大类")
	private java.lang.String assetsCategoryName;
	@ApiModelProperty(value="资产分类")
	private java.lang.String assetsDetailCategoryId;
	@ApiModelProperty(value="资产分类名称")
	private java.lang.String assetsDetailCategoryName;
	@ApiModelProperty(value="单价")
	private BigDecimal price;
	@ApiModelProperty(value="卡片路径")
	private java.lang.String cardAddress;
	@ApiModelProperty(value="登账状态")
	private java.lang.String financeStatus;

	@ApiModelProperty(value="存放地点")
	private java.lang.String depositAddress;
	@ApiModelProperty(value="数量")
	private java.lang.Integer number;
	@ApiModelProperty(value="状态1正常2下放中3交接中4申请报废5待审核6待报废7已报废8待下放9修正中")
	private java.lang.Integer status;
	@ApiModelProperty(value="是否交接过0未交接1已交接")
	private java.lang.Integer isTransfer=0;
	@ApiModelProperty(value="规格型号")
	private java.lang.String assetsModel;
	@ApiModelProperty(value="净值")
	private java.lang.String netWorth;
	@ApiModelProperty(value="减值准备")
	private java.lang.String decrease;
	@ApiModelProperty(value="役龄预龄")
	private java.lang.String expireYear;
	
	//审核专用
	@ApiModelProperty(value="审核-使用人名称")
	@Transient
	private java.lang.String auditUseUserName;
	@ApiModelProperty(value="审核-使用部门")
	@Transient
	private java.lang.String auditUseDeptName;
	@ApiModelProperty(value="审核-存放地点")
	@Transient
	private java.lang.String auditDepositAddress;
	@ApiModelProperty(value="审核-管理人")
	@Transient
	private java.lang.String auditUserName;
	
	//计量单位
	private java.lang.String unit;	
	//财政补助收入
	private BigDecimal financeIncome;
	//教科项目收入
	private BigDecimal eduIncome;
	//其他收入
	private BigDecimal otherIncome;
	//折旧年限
	private Integer depYear;
	//累计折旧
	private Integer totalDepYear;
    //净值
	private Integer cost;
	//价值类型
	private String priceType;
	//会计凭证号
	private String accountNo;
	//发票号
	private String invoiceNo;
	//资产属性
	private String accessType;
	//品牌
	private String brand;
	private String backup1;
	private String backup2;
	private String backup3;
	//供应商
	private String supplier;
	//电话
	private String phone;
    //采购方式
	private String purchaseType;
    //投入
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date useDate;
    //坐落位置
	private String location;
    //竣工日期
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date completedDate;
	//产权形式
	private String propertyType;
	//权属证明
	private String propertyProve;
	//权属证号
	private String propertyNo;
	//权属证号
	private String propertyNature;
	//权属年限	
	private String propertyYear;
	
	//设计用途
	private String designWay;	
	//建筑结构
	private String houseStructure;
	//房屋所有权人
	private String houseOwn;
	//自用面积
	private String selfArea;
	//出借面积
	private String borrowArea;
	//出租面积
	private String rentArea;
	//对外投资面积
	private String investArea;
	//其他面积
	private String otherArea;	
	//自用价值
	private String selfCost;
	//出借价值
	private String borrowCost;
	//出租价值
	private String rentCost;
	//对外投资价值
	private String investCost;
	//其他价值
	private String otherCost;
	//发证日期
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date cardDate;
	//编制情况 
	private String orgInfo;	
    //车辆用途分类 
	private String carPurpose;
    //车辆产地 
	private String carPlace;
    //车辆品牌
	private String carBrand;
    //车牌号
	private String carNo;
    //发动机号
	private String carEngineNo;
    //排气量
	private String carExhaust;
    //号牌种类
	private String carNoType;
	//行驶证注册日期
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date carRegisterDate;
	//采购合同编号
	private String contractNo;
	//地类
	private String addressType;
	//证书号
	private String cardNo;
	//生产厂家
	private String manufacturer;
	//出版社
	private String press;
	//出版日期
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date pressDate;
	//档案号
	private String archivesNo;
	//出生、栽种年份档案号
	private String birthYear;
	//纲属科
	private String outlineBelong;
	//来源地
	private String sourceAddress;
	//使用年限
	private String useYear;
	//是否待报废
	private String isScrap;
	//记账日期
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date bookDate;
	//业务状态
	private String busStatus;
	//取得日期
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date obtainDate;
	//单位
	private String company;
	//制单时间
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date voucherDate;
	//保修截至日期
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
    //二级分类
	private Integer secondCategoryId;
    //二级分类
	private String secondCategoryName;

	private String assetsType;
	
	private java.lang.String remarks;
	private java.lang.String createBy;
	private java.util.Date createTime;
	private java.lang.String updateBy;
	private java.util.Date updateTime;
	@Transient
	private List<BusAttachments> attList=new ArrayList<BusAttachments>();

}
