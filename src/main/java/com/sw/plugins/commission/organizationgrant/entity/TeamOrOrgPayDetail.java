package com.sw.plugins.commission.organizationgrant.entity;

import java.math.BigDecimal;

import com.sw.core.data.entity.RelationEntity;

public class TeamOrOrgPayDetail extends RelationEntity{

	private static final long serialVersionUID = -6901168177388324528L;
	
	//居间费ID
	private Long commissionID;
	//支付金额
	private BigDecimal payAmount;
	//机构居间费支付记录ID
	private Long teamPayID;
	
	private String title1;
	private String title2;
	private String title3;
	private String title4;
	//机构ID
	private Long orgId;
	//组织ID
	private Long teamOrOrgId;
	//机构居间费支付ID
	private Long teamOrOrgPayRecordId;
	private String productName;
	private String memberName;
	private BigDecimal commission;
	private short tradeType;
	private String orderNumber;
	private String contractNumber;
	private String clientName;
	private BigDecimal commissionProportion;
	private BigDecimal payAmount2;
	//支付状态
	private short status;
	
	private String payTimeStart;
	private String payTimeEnd;
	private String orgName;
	private String orgShortName;
	private String payDataStart;
	private String payDataEnd;
	private String orgShortNameNew;
	private String auditingTime;
	private String commissionProportionStr;
	//发放状态字符串
	private String statusStr;
	
	private Long companyID;
	
	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getCommissionProportionStr() {
		return commissionProportionStr;
	}

	public void setCommissionProportionStr(String commissionProportionStr) {
		this.commissionProportionStr = commissionProportionStr;
	}

	public String getOrgShortNameNew() {
		return orgShortNameNew;
	}

	public void setOrgShortNameNew(String orgShortNameNew) {
		this.orgShortNameNew = orgShortNameNew;
	}

	public String getPayDataStart() {
		return payDataStart;
	}

	public void setPayDataStart(String payDataStart) {
		this.payDataStart = payDataStart;
	}

	public String getPayDataEnd() {
		return payDataEnd;
	}

	public void setPayDataEnd(String payDataEnd) {
		this.payDataEnd = payDataEnd;
	}

	public String getOrgShortName() {
		return orgShortName;
	}

	public void setOrgShortName(String orgShortName) {
		this.orgShortName = orgShortName;
	}

	public Long getTeamPayID(){
		return teamPayID;
	}
	
	public void setTeamPayID(Long teamPayID){
		this.teamPayID = teamPayID;
	}
	
	public Long getCommissionID() {
		return commissionID;
	}

	public void setCommissionID(Long commissionID) {
		this.commissionID = commissionID;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public String getTitle1() {
		return title1;
	}

	public void setTitle1(String title1) {
		this.title1 = title1;
	}

	public String getTitle2() {
		return title2;
	}

	public void setTitle2(String title2) {
		this.title2 = title2;
	}

	public String getTitle3() {
		return title3;
	}

	public void setTitle3(String title3) {
		this.title3 = title3;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getTeamOrOrgPayRecordId() {
		return teamOrOrgPayRecordId;
	}

	public void setTeamOrOrgPayRecordId(Long teamOrOrgPayRecordId) {
		this.teamOrOrgPayRecordId = teamOrOrgPayRecordId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public BigDecimal getCommission() {
		return commission;
	}

	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}

	public short getTradeType() {
		return tradeType;
	}

	public void setTradeType(short tradeType) {
		this.tradeType = tradeType;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public BigDecimal getCommissionProportion() {
		return commissionProportion;
	}

	public void setCommissionProportion(BigDecimal commissionProportion) {
		this.commissionProportion = commissionProportion;
	}

	public BigDecimal getPayAmount2() {
		return payAmount2;
	}

	public void setPayAmount2(BigDecimal payAmount2) {
		this.payAmount2 = payAmount2;
	}

	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	public String getTitle4() {
		return title4;
	}

	public void setTitle4(String title4) {
		this.title4 = title4;
	}

	public Long getTeamOrOrgId() {
		return teamOrOrgId;
	}

	public void setTeamOrOrgId(Long teamOrOrgId) {
		this.teamOrOrgId = teamOrOrgId;
	}


	public String getPayTimeStart() {
		return payTimeStart;
	}

	public void setPayTimeStart(String payTimeStart) {
		this.payTimeStart = payTimeStart;
	}

	public String getPayTimeEnd() {
		return payTimeEnd;
	}

	public void setPayTimeEnd(String payTimeEnd) {
		this.payTimeEnd = payTimeEnd;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
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
