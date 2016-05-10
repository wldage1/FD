package com.sw.plugins.market.order.entity;


import java.math.BigDecimal;

import com.sw.core.data.entity.RelationEntity;

public class OrderAcountAffirmDetail extends RelationEntity{
	private static final long serialVersionUID = 1L;
	/** 凭证ID **/
	private Long proofID;
	/** 订单号    **/
	private String orderNumber;
	/** 打款金额 **/
	private BigDecimal proofAmount;
	/** 打款时间 **/
	private String proofTime;
	/** 确认打款金额 **/
	private BigDecimal affirmProofAmount;
	/** 确认打款时间 **/
	private String affirmProofTime;
	/** 类型 0-银行流水确认 1-打款凭证确认  **/
	private Short type;
	/** 操作类型 1-金额确认 2-复核 3-修改 **/
	private Short operateType;
	/** 流水号 **/
	private String serialNumber;
	/** 确认状态 */
	private Short comfirm;
	/** 拒绝原因 */
	private String remark;
	
	//懒 加载对象
	private Order order;
	
	public Long getProofID() {
		return proofID;
	}
	public void setProofID(Long proofID) {
		this.proofID = proofID;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
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
	public Short getType() {
		return type;
	}
	public void setType(Short type) {
		this.type = type;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public Short getOperateType() {
		return operateType;
	}
	public void setOperateType(Short operateType) {
		this.operateType = operateType;
	}
	public BigDecimal getAffirmProofAmount() {
		return affirmProofAmount;
	}
	public void setAffirmProofAmount(BigDecimal affirmProofAmount) {
		this.affirmProofAmount = affirmProofAmount;
	}
	public String getAffirmProofTime() {
		return affirmProofTime;
	}
	public void setAffirmProofTime(String affirmProofTime) {
		this.affirmProofTime = affirmProofTime;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
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
