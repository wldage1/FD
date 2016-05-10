package com.sw.plugins.commission.contract.entity;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.sw.core.data.entity.RelationEntity;

public class MemberAgreement extends RelationEntity{
	
	private static final long serialVersionUID = 8439388546577637264L;
	
	//居间机构ID
    private Integer orgID;
    private Integer agreementTemplateID;
    
	private Integer status;
    //停用时间
    private String stopTime;
    //创建时间
    private String createTime;
    //居间公司名称
    private String orgName;
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public Integer getAgreementTemplateID() {
		return agreementTemplateID;
	}
	public void setAgreementTemplateID(Integer agreementTemplateID) {
		this.agreementTemplateID = agreementTemplateID;
	}
	public Integer getOrgID() {
		return orgID;
	}
	public void setOrgID(Integer orgID) {
		this.orgID = orgID;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getStopTime() {
		return stopTime;
	}
	public void setStopTime(String stopTime) {
		this.stopTime = stopTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}