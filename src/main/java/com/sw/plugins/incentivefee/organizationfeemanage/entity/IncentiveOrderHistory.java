package com.sw.plugins.incentivefee.organizationfeemanage.entity;

import com.sw.core.data.entity.RelationEntity;

public class IncentiveOrderHistory extends RelationEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//激励费用发放详细ID
	private Long incentiveFeeId;
	//激励费用发放详细ID集合
	private Long[] incentiveFeeIds;
	//订单号
	private String orderNumber;
	//订单号集合
	private String[] orderNumbers;
	//理财师机构ID
	private Long orgId;
	//理财师机构ID集合
	private Long[] orgIds;
	//居间费记录审核时间
	private String auditingTime;
	
	private Long companyID;
	
	private Short type;
	
	private Short isPay;
	
	public Short getIsPay(){
		return isPay;
	}
	
	public void setIsPay(Short isPay){
		this.isPay = isPay;
	}
	
	public void setType(Short type){
		this.type = type;
	}
	
	public Short getType(){
		return type;
	}
	
	public Long getIncentiveFeeId() {
		return incentiveFeeId;
	}
	public void setIncentiveFeeId(Long incentiveFeeId) {
		this.incentiveFeeId = incentiveFeeId;
	}
	public Long[] getIncentiveFeeIds() {
		return incentiveFeeIds;
	}
	public void setIncentiveFeeIds(Long[] incentiveFeeIds) {
		this.incentiveFeeIds = incentiveFeeIds;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String[] getOrderNumbers() {
		return orderNumbers;
	}
	public void setOrderNumbers(String[] orderNumbers) {
		this.orderNumbers = orderNumbers;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Long[] getOrgIds() {
		return orgIds;
	}
	public void setOrgIds(Long[] orgIds) {
		this.orgIds = orgIds;
	}
	public String getAuditingTime() {
		return auditingTime;
	}
	public void setAuditingTime(String auditingTime) {
		this.auditingTime = auditingTime;
	}
	public Long getCompanyID() {
		return companyID;
	}
	public void setCompanyID(Long companyID) {
		this.companyID = companyID;
	}
}
