package com.sw.plugins.market.salemanage.entity;

import java.math.BigDecimal;

import com.sw.core.data.entity.RelationEntity;

public class SaleManagePlot extends RelationEntity {

	private static final long serialVersionUID = -5206146620360686341L;

	private Long productId;
	private Long subProductId;
	
	/* --------------------销售管控参数 ----------------*/
	
	/* -- 产品规模 -- */
	/** 产品最大募集规模 */
	BigDecimal productScaleMax;
	/** 产品最小募集规模 */
	BigDecimal productScaleMin;
	/* -- 到账金额 -- */
	/** 到账总金额 */
	BigDecimal orderTotalPayed;
	/** 锁定到账金额 */
	BigDecimal orderTotalCorrectPayed;
	/** 配额已到账 */
	BigDecimal orderTotalPayedShared;
	/** 配额 未到账 */
	BigDecimal orderTotalUnPayedShared;
	/** 到账未配额 */
	BigDecimal orderTotalPayedUnshared;
	/* -- 订单总额 -- */
	/** 订单总金额 */
	BigDecimal orderTotal;
	/** 已配额订单金额 */
	BigDecimal orderTotalShared;
	/* -- 订单总数 -- */
	/** 订单总份数 */
	long orderTotalCount;
	/** 已锁定到账订单数 */
	long orderTotalCorrectPayedCount;
	/** 已配额订单总数 */
	long orderTotalSharedCount;
	/** 已到账订单总数 */
	long orderTotalPayedCount;
	/* -- 小额订单数 -- */
	/** 小额订单总金额 */
	BigDecimal orderTotalLimit;
	/** 小额订单已配额已到账金额 */
	BigDecimal orderTotalLimitPayedShared;
	/** 小额订单已配额未到账金额 */
	BigDecimal orderTotalLimitUnPayedShared;
	/** 小额订单总数 */
	long orderTotalLimitCount;
	/** 小额订单已配额数 */
	long orderTotalLimitSharedCount;
	/** 小额订单已到账数 */
	long orderTotalLimitPayedCount;
	/** 小额订单已配额未到账数 */
	long orderTotalLimitUnPayedSharedCount;
	/** 小额订单投资金额限制  */
	BigDecimal lowAmountThreshold;
	/** 小额订单投资人数限制  */
	int maxLowAmountClientCount;
	/* -- 单证归集数 -- */
	/** 单证归集总数 */
	long orderTotalCollectCount;
	/** 锁定到账订单归集数 */
	long orderTotalCorrectCollectCount;
	/* -- 每日到账情况 -- */
	String orderDaysAmountListJSON;
	
	/* --------------------额度配给参数 ----------------*/
	/** 锁定到账小额订单数 */
	long orderTotallimitCorrectCount;
	/** 总配给额度 */
	BigDecimal orderTotalPushShare;
	/** 配给接受额度 */
	BigDecimal orderTotalAcceptPushShare;
	
	
	
	public SaleManagePlot(){}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getSubProductId() {
		return subProductId;
	}

	public void setSubProductId(Long subProductId) {
		this.subProductId = subProductId;
	}

	public BigDecimal getProductScaleMax() {
		return productScaleMax;
	}

	public void setProductScaleMax(BigDecimal productScaleMax) {
		this.productScaleMax = productScaleMax;
	}

	public BigDecimal getProductScaleMin() {
		return productScaleMin;
	}

	public void setProductScaleMin(BigDecimal productScaleMin) {
		this.productScaleMin = productScaleMin;
	}

	public BigDecimal getOrderTotalPayed() {
		return orderTotalPayed;
	}

	public void setOrderTotalPayed(BigDecimal orderTotalPayed) {
		this.orderTotalPayed = orderTotalPayed;
	}

	public BigDecimal getOrderTotalCorrectPayed() {
		return orderTotalCorrectPayed;
	}

	public void setOrderTotalCorrectPayed(BigDecimal orderTotalCorrectPayed) {
		this.orderTotalCorrectPayed = orderTotalCorrectPayed;
	}

	public BigDecimal getOrderTotalPayedShared() {
		return orderTotalPayedShared;
	}

	public void setOrderTotalPayedShared(BigDecimal orderTotalPayedShared) {
		this.orderTotalPayedShared = orderTotalPayedShared;
	}

	public BigDecimal getOrderTotalUnPayedShared() {
		return orderTotalUnPayedShared;
	}

	public void setOrderTotalUnPayedShared(BigDecimal orderTotalUnPayedShared) {
		this.orderTotalUnPayedShared = orderTotalUnPayedShared;
	}

	public BigDecimal getOrderTotalPayedUnshared() {
		return orderTotalPayedUnshared;
	}

	public void setOrderTotalPayedUnshared(BigDecimal orderTotalPayedUnshared) {
		this.orderTotalPayedUnshared = orderTotalPayedUnshared;
	}

	public BigDecimal getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(BigDecimal orderTotal) {
		this.orderTotal = orderTotal;
	}

	public BigDecimal getOrderTotalShared() {
		return orderTotalShared;
	}

	public void setOrderTotalShared(BigDecimal orderTotalShared) {
		this.orderTotalShared = orderTotalShared;
	}

	public long getOrderTotalCount() {
		return orderTotalCount;
	}

	public void setOrderTotalCount(long orderTotalCount) {
		this.orderTotalCount = orderTotalCount;
	}

	public long getOrderTotalCorrectPayedCount() {
		return orderTotalCorrectPayedCount;
	}

	public void setOrderTotalCorrectPayedCount(long orderTotalCorrectPayedCount) {
		this.orderTotalCorrectPayedCount = orderTotalCorrectPayedCount;
	}

	public long getOrderTotalSharedCount() {
		return orderTotalSharedCount;
	}

	public void setOrderTotalSharedCount(long orderTotalSharedCount) {
		this.orderTotalSharedCount = orderTotalSharedCount;
	}

	public long getOrderTotalPayedCount() {
		return orderTotalPayedCount;
	}

	public void setOrderTotalPayedCount(long orderTotalPayedCount) {
		this.orderTotalPayedCount = orderTotalPayedCount;
	}

	public BigDecimal getOrderTotalLimit() {
		return orderTotalLimit;
	}

	public void setOrderTotalLimit(BigDecimal orderTotalLimit) {
		this.orderTotalLimit = orderTotalLimit;
	}

	public BigDecimal getOrderTotalLimitPayedShared() {
		return orderTotalLimitPayedShared;
	}

	public void setOrderTotalLimitPayedShared(BigDecimal orderTotalLimitPayedShared) {
		this.orderTotalLimitPayedShared = orderTotalLimitPayedShared;
	}

	public BigDecimal getOrderTotalLimitUnPayedShared() {
		return orderTotalLimitUnPayedShared;
	}

	public void setOrderTotalLimitUnPayedShared(BigDecimal orderTotalLimitUnPayedShared) {
		this.orderTotalLimitUnPayedShared = orderTotalLimitUnPayedShared;
	}

	public long getOrderTotalLimitCount() {
		return orderTotalLimitCount;
	}

	public void setOrderTotalLimitCount(long orderTotalLimitCount) {
		this.orderTotalLimitCount = orderTotalLimitCount;
	}

	public long getOrderTotalLimitSharedCount() {
		return orderTotalLimitSharedCount;
	}

	public void setOrderTotalLimitSharedCount(long orderTotalLimitSharedCount) {
		this.orderTotalLimitSharedCount = orderTotalLimitSharedCount;
	}

	public long getOrderTotalLimitPayedCount() {
		return orderTotalLimitPayedCount;
	}

	public void setOrderTotalLimitPayedCount(long orderTotalLimitPayedCount) {
		this.orderTotalLimitPayedCount = orderTotalLimitPayedCount;
	}

	public long getOrderTotalLimitUnPayedSharedCount() {
		return orderTotalLimitUnPayedSharedCount;
	}

	public void setOrderTotalLimitUnPayedSharedCount(long orderTotalLimitUnPayedSharedCount) {
		this.orderTotalLimitUnPayedSharedCount = orderTotalLimitUnPayedSharedCount;
	}

	public long getOrderTotalCollectCount() {
		return orderTotalCollectCount;
	}

	public void setOrderTotalCollectCount(long orderTotalCollectCount) {
		this.orderTotalCollectCount = orderTotalCollectCount;
	}

	public long getOrderTotalCorrectCollectCount() {
		return orderTotalCorrectCollectCount;
	}

	public void setOrderTotalCorrectCollectCount(long orderTotalCorrectCollectCount) {
		this.orderTotalCorrectCollectCount = orderTotalCorrectCollectCount;
	}

	public String getOrderDaysAmountListJSON() {
		return orderDaysAmountListJSON;
	}

	public void setOrderDaysAmountListJSON(String orderDaysAmountListJSON) {
		this.orderDaysAmountListJSON = orderDaysAmountListJSON;
	}

	public BigDecimal getLowAmountThreshold() {
		return lowAmountThreshold;
	}

	public void setLowAmountThreshold(BigDecimal lowAmountThreshold) {
		this.lowAmountThreshold = lowAmountThreshold;
	}

	public int getMaxLowAmountClientCount() {
		return maxLowAmountClientCount;
	}

	public void setMaxLowAmountClientCount(int maxLowAmountClientCount) {
		this.maxLowAmountClientCount = maxLowAmountClientCount;
	}

	public long getOrderTotallimitCorrectCount() {
		return orderTotallimitCorrectCount;
	}

	public void setOrderTotallimitCorrectCount(long orderTotallimitCorrectCount) {
		this.orderTotallimitCorrectCount = orderTotallimitCorrectCount;
	}

	public BigDecimal getOrderTotalPushShare() {
		return orderTotalPushShare;
	}

	public void setOrderTotalPushShare(BigDecimal orderTotalPushShare) {
		this.orderTotalPushShare = orderTotalPushShare;
	}

	public BigDecimal getOrderTotalAcceptPushShare() {
		return orderTotalAcceptPushShare;
	}

	public void setOrderTotalAcceptPushShare(BigDecimal orderTotalAcceptPushShare) {
		this.orderTotalAcceptPushShare = orderTotalAcceptPushShare;
	}

}
