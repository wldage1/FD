package com.sw.plugins.market.bankwater.entity;

import java.math.BigDecimal;

import com.sw.core.data.entity.RelationEntity;

/**
 * 银行流水实体
 * 
 * @author runchao
 */
public class PayConfirmFromProvider extends RelationEntity{

	private static final long serialVersionUID = 4760319934510477594L;

	//供应商ID
	private Long providerId;
	//产品ID
	private Long productId;
	//资金确认到账记录表ID
	private Long orderAcountAffirmdetailID;
	//客户名称
	private String name;
	//打款金额
	private BigDecimal payAmount;
	//打款时间
	private String payTime;
	//流水号
	private String serialNumber;
	//匹配状态
	private Integer matchingStatus;
	
	/** 外加属性 **/
	//产品简称
	private String productShortName;
	//供应商简称
	private String providerShortName;
	//数据 类型  1=认购，2=申购
	private Short dataType;
	//订单号
	private String orderNumber;
	//凭证时间
	private String proofTime;
	//凭证附近URL
	private String proofFileUrl;
	//机构id  -居间公司id
	private Long orgID;
	
	
	
	
	public Long getProviderId() {
		return providerId;
	}
	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public Integer getMatchingStatus() {
		return matchingStatus;
	}
	public void setMatchingStatus(Integer matchingStatus) {
		this.matchingStatus = matchingStatus;
	}
	public String getProductShortName() {
		return productShortName;
	}
	public void setProductShortName(String productShortName) {
		this.productShortName = productShortName;
	}
	public String getProviderShortName() {
		return providerShortName;
	}
	public void setProviderShortName(String providerShortName) {
		this.providerShortName = providerShortName;
	}
	public Short getDataType() {
		return dataType;
	}
	public void setDataType(Short dataType) {
		this.dataType = dataType;
	}
	public Long getOrderAcountAffirmdetailID() {
		return orderAcountAffirmdetailID;
	}
	public void setOrderAcountAffirmdetailID(Long orderAcountAffirmdetailID) {
		this.orderAcountAffirmdetailID = orderAcountAffirmdetailID;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getProofTime() {
		return proofTime;
	}
	public void setProofTime(String proofTime) {
		this.proofTime = proofTime;
	}
	public String getProofFileUrl() {
		return proofFileUrl;
	}
	public void setProofFileUrl(String proofFileUrl) {
		this.proofFileUrl = proofFileUrl;
	}
	public Long getOrgID() {
		return orgID;
	}
	public void setOrgID(Long orgID) {
		this.orgID = orgID;
	}
}
