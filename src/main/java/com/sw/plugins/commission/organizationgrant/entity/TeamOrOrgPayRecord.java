package com.sw.plugins.commission.organizationgrant.entity;

import java.math.BigDecimal;

import com.sw.core.data.entity.RelationEntity;

public class TeamOrOrgPayRecord extends RelationEntity{

	private static final long serialVersionUID = -151582310742181787L;
	
	private Long companyID;
	//机构id
	private Long sourceID;
	//支付金额
	private BigDecimal payAmount;
	//实际支付金额
	private BigDecimal payAmount2;
	//账号
	private String accountID;
	//账户
	private String accountName;
	//开户行
	private String bankName;
	//发放备注
	private String remark;
	//机构备注
	private String teamOrOrgRemark;
	//支付状态
	private Short status;
	//原状态（待发放列表为0，发放失败列表为3）
	private Short oldStatus;
	//支付时间
	private String payTime;
	//支付时间
	private String payDate;
	//审核时间
	private String auditingTime;
	
	private String teamShortName;
	
	private Long teamID;
	//阶段时间
	private String stageTime;
	
	private Long subProductID;
	
	public Long getTeamID(){
		return teamID;
	}
	
	public void setTeamID(Long teamID){
		this.teamID = teamID;
	}
	
	public String getTeamShortName(){
		return teamShortName;
	}
	
	public void setTeamShortName(String teamShortName){
		this.teamShortName = teamShortName;
	}
	
	public Long getSourceID() {
		return sourceID;
	}
	public void setSourceID(Long sourceID) {
		this.sourceID = sourceID;
	}
	public BigDecimal getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}
	public BigDecimal getPayAmount2() {
		return payAmount2;
	}
	public void setPayAmount2(BigDecimal payAmount2) {
		this.payAmount2 = payAmount2;
	}
	public String getAccountID() {
		return accountID;
	}
	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTeamOrOrgRemark() {
		return teamOrOrgRemark;
	}
	public void setTeamOrOrgRemark(String teamOrOrgRemark) {
		this.teamOrOrgRemark = teamOrOrgRemark;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public Short getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(Short oldStatus) {
		this.oldStatus = oldStatus;
	}

	public String getAuditingTime() {
		return auditingTime;
	}

	public Long getSubProductID() {
		return subProductID;
	}

	public void setSubProductID(Long subProductID) {
		this.subProductID = subProductID;
	}

	public void setAuditingTime(String auditingTime) {
		this.auditingTime = auditingTime;
	}

	public String getStageTime() {
		return stageTime;
	}

	public void setStageTime(String stageTime) {
		this.stageTime = stageTime;
	}

	public Long getCompanyID() {
		return companyID;
	}

	public void setCompanyID(Long companyID) {
		this.companyID = companyID;
	}
	
}
