package com.sw.plugins.incentivefee.organizationfeemanage.entity;

import java.math.BigDecimal;

import com.sw.core.data.entity.RelationEntity;

public class IncentiveFeeDetail extends RelationEntity{

	private static final long serialVersionUID = 1L;
	//机构或团队ID
	private Long orgId;
	//0为机构，1为团队
	private Short type;
	//累计成交规模
	private BigDecimal sumInvestAmount;
	//成功系数
	private Double successRate;
	//奖励分值
	private Integer rewardScore; 
	//返利分值
	private BigDecimal rebateScore;
	//居间费总额
	private BigDecimal sumCommissionFee;
	//返利比例
	private Double rebateRate;
	//返利金额
	private BigDecimal rebateAmount;
	//发放时间
	private String payTime;
	//是否发放 0：未发放  1：已发放
	private Short isPay;
	//发放阶段时间
	private String payStageTime;
	
	private Long companyID;
	
	private Long[] teamIDs;
	
	/** ##################查询展示需要用到#################  */
	//机构简称
	private String orgShortName;
	
	public Long getOrgId() {
		return orgId;
	}
	public Short getType() {
		return type;
	}
	public BigDecimal getSumInvestAmount() {
		return sumInvestAmount;
	}
	public Double getSuccessRate() {
		return successRate;
	}
	public Integer getRewardScore() {
		return rewardScore;
	}
	public BigDecimal getRebateScore() {
		return rebateScore;
	}
	public BigDecimal getSumCommissionFee() {
		return sumCommissionFee;
	}
	public Double getRebateRate() {
		return rebateRate;
	}
	public BigDecimal getRebateAmount() {
		return rebateAmount;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public void setType(Short type) {
		this.type = type;
	}
	public void setSumInvestAmount(BigDecimal sumInvestAmount) {
		this.sumInvestAmount = sumInvestAmount;
	}
	public void setSuccessRate(Double successRate) {
		this.successRate = successRate;
	}
	public void setRewardScore(Integer rewardScore) {
		this.rewardScore = rewardScore;
	}
	public void setRebateScore(BigDecimal rebateScore) {
		this.rebateScore = rebateScore;
	}
	public void setSumCommissionFee(BigDecimal sumCommissionFee) {
		this.sumCommissionFee = sumCommissionFee;
	}
	public void setRebateRate(Double rebateRate) {
		this.rebateRate = rebateRate;
	}
	public void setRebateAmount(BigDecimal rebateAmount) {
		this.rebateAmount = rebateAmount;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getOrgShortName() {
		return orgShortName;
	}
	public void setOrgShortName(String orgShortName) {
		this.orgShortName = orgShortName;
	}
	public Short getIsPay() {
		return isPay;
	}
	public void setIsPay(Short isPay) {
		this.isPay = isPay;
	}
	public String getPayStageTime() {
		return payStageTime;
	}
	public void setPayStageTime(String payStageTime) {
		this.payStageTime = payStageTime;
	}
	public Long[] getTeamIDs() {
		return teamIDs;
	}
	public Long getCompanyID() {
		return companyID;
	}
	public void setCompanyID(Long companyID) {
		this.companyID = companyID;
	}
	public void setTeamIDs(Long[] teamIDs) {
		this.teamIDs = teamIDs;
	}
}
