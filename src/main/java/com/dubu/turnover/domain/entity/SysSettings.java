package com.dubu.turnover.domain.entity;
import lombok.Data;
import javax.persistence.Id;
/*
 *  @author: smart boy
 *  @date: 2019-04-02
 */
@Data
public class SysSettings implements java.io.Serializable {
	private static final long serialVersionUID = 5454155825314635342L;
	
	@Id
	private java.lang.Integer id;
	private java.lang.Integer switchIndex;
	private java.lang.Integer switchQuotation;
	private java.lang.Integer switchAttention;
	private java.lang.Integer switchQtAttention;
	private java.lang.Integer switchWaxing;
	private java.lang.Integer swithDgWaxing;
	private java.lang.String iosVersion;
	private java.lang.String androidVersion;
	private java.lang.Integer iosUpdate;
	private java.lang.Integer androidUpdate;
	private java.lang.String androidDesc;
	private java.lang.String iosDesc;
	private java.lang.String iosUpdateUrl;

}
