package com.sw.plugins.commission.paygrantparameter.entity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.sw.core.data.entity.RelationEntity;

public class PayGrantParameter extends RelationEntity{

	private static final long serialVersionUID = 1L;
	
	@NotEmpty
	private String name;
	
	@Min(value=1)
	@Max(value=28)
	private Integer approvedDeadline;
	
	@Min(value=1)
	@Max(value=28)
	private Integer payDate;
	
	//1-理财师顾问 2-机构
	private Short type;
	
	private Long orgID;
	
	public Short getType(){
		return type;
	}
	
	public void setType(Short type){
		this.type = type;
	}
	
	public Long getOrgID(){
		return orgID;
	}
	
	public void setOrgID(Long orgID){
		this.orgID = orgID;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getApprovedDeadline() {
		return approvedDeadline;
	}

	public void setApprovedDeadline(Integer approvedDeadline) {
		this.approvedDeadline = approvedDeadline;
	}

	public Integer getPayDate() {
		return payDate;
	}

	public void setPayDate(Integer payDate) {
		this.payDate = payDate;
	}

}
