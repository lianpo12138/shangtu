package com.dubu.turnover.domain;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
@Data
public class TreeVo  implements java.io.Serializable{
    private static final long serialVersionUID = -4778380766617313374L;
    private java.lang.Integer id;
	@ApiModelProperty(value="名称")
    private java.lang.String name;
	@ApiModelProperty(value="父节点")
    private java.lang.Integer parentId;
    private java.lang.Integer sort;
    private java.lang.String type;
    @Transient
    private List childrens = new ArrayList<>();

}
