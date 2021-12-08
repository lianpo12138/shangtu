package com.dubu.turnover.domain.entity;
import lombok.Data;
import javax.persistence.Id;

import io.swagger.annotations.ApiModelProperty;
/*
 *  @author: smart boy
 *  @date: 2021-03-13
 */
@Data
public class BusAttachments implements java.io.Serializable {
	private static final long serialVersionUID = 5454155825314635342L;
	
	@Id
	private java.lang.Integer id;
	@ApiModelProperty(value="附件名称")
	private java.lang.String attrName;
	@ApiModelProperty(value="附件url")
	private java.lang.String attrUrl;
	@ApiModelProperty(value="附件类型1固定资产2耗材入库3耗材领用")
	private java.lang.Integer attrSourceType;
	@ApiModelProperty(value="来源id")
	private java.lang.Integer attrSourceId;
	@ApiModelProperty(value="附件分类1请购单2合同审批单3合同4询价单5比价会签单6营业执照资质7签收单8报废单")
	private java.lang.String attrType;
	@ApiModelProperty(value="附件分类名称")
	private java.lang.String attTypeName;
	private java.lang.String createBy;
	private java.util.Date createTime;
	private java.lang.String updateBy;
	private java.util.Date updateTime;

}
