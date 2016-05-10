package com.sw.plugins.market.order.entity;


import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.sw.core.data.entity.RelationEntity;

public class OrderProof extends RelationEntity{
	private static final long serialVersionUID = 1L;
	/** 订单号 **/
	private String orderNumber;
	/** 理财顾问ID   **/
	private Long memberID;
	/** 产品ID **/
	private Long productID;
	/** 打款金额 **/
	@NotNull
	private BigDecimal proofAmount;
	/** 打款时间 **/
	private String proofTime;
	/** 处理状态 0-未处理 1-已处理 **/
	private Short proofStatus;
	/** 凭证附件 **/
	@NotEmpty
	private String proofFileUrl;
	/** 备注 **/
	private String proofRemark;
	/** 流水号 **/
	private String serialNumber;
	/** 确认状态 0-拒绝1-确认*/
	private Short comfirm;
	/** 拒绝原因 */
	private String remark;
	
	//外加属性 
	private Order order;
	//产品简称
	private String productShortName;
	//理财顾问姓名
	private String memberName;
	//确认打款时间
	private String affirmProofTime;
	//确认打款金额
	private String affirmProofAmount;
	
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public Long getMemberID() {
		return memberID;
	}
	public void setMemberID(Long memberID) {
		this.memberID = memberID;
	}
	public BigDecimal getProofAmount() {
		return proofAmount;
	}
	public void setProofAmount(BigDecimal proofAmount) {
		this.proofAmount = proofAmount;
	}
	public String getProofTime() {
		return proofTime;
	}
	public void setProofTime(String proofTime) {
		this.proofTime = proofTime;
	}
	public Short getProofStatus() {
		return proofStatus;
	}
	public void setProofStatus(Short proofStatus) {
		this.proofStatus = proofStatus;
	}
	public String getProofFileUrl() {
		return proofFileUrl;
	}
	public void setProofFileUrl(String proofFileUrl) {
		this.proofFileUrl = proofFileUrl;
	}
	public String getProofRemark() {
		return proofRemark;
	}
	public void setProofRemark(String proofRemark) {
		this.proofRemark = proofRemark;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public Long getProductID() {
		return productID;
	}
	public void setProductID(Long productID) {
		this.productID = productID;
	}
	public String getProductShortName() {
		return productShortName;
	}
	public void setProductShortName(String productShortName) {
		this.productShortName = productShortName;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getAffirmProofTime() {
		return affirmProofTime;
	}
	public void setAffirmProofTime(String affirmProofTime) {
		this.affirmProofTime = affirmProofTime;
	}
	public String getAffirmProofAmount() {
		return affirmProofAmount;
	}
	public void setAffirmProofAmount(String affirmProofAmount) {
		this.affirmProofAmount = affirmProofAmount;
	}
	public Short getComfirm() {
		return comfirm;
	}
	public void setComfirm(Short comfirm) {
		this.comfirm = comfirm;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
