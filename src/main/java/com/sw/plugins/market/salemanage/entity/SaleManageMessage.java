package com.sw.plugins.market.salemanage.entity;

import java.math.BigDecimal;

import com.sw.core.data.entity.RelationEntity;

public class SaleManageMessage extends RelationEntity {

	private static final long serialVersionUID = 3417498320458867625L;

	private Long memberId;// 理财师ID
	private Long productId;// 产品ID
	private Long subProductId;// 子产品ID
	private String subProductType;// 子产品受益权类型
	private Long orderId;// 订单ID
	private String productName;// 产品名称
	private String orderNumber;// 产品订单编号
	private BigDecimal share;// 分配份额

	public SaleManageMessage() {
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public BigDecimal getShare() {
		return share;
	}

	public void setShare(BigDecimal share) {
		this.share = share;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getSubProductId() {
		return subProductId;
	}

	public void setSubProductId(Long subProductId) {
		this.subProductId = subProductId;
	}

	public String getSubProductType() {
		return subProductType;
	}

	public void setSubProductType(String subProductType) {
		this.subProductType = subProductType;
	}

}
