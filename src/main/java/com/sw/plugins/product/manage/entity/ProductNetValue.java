package com.sw.plugins.product.manage.entity;

import java.math.BigDecimal;

import com.sw.core.data.entity.RelationEntity;

public class ProductNetValue extends RelationEntity {

	private static final long serialVersionUID = 1L;

	// 产品ID
	private Long subProductId;
	// 开放日
	private String openDay;
	// 净值
	private BigDecimal netValue;

	public ProductNetValue() {
	}

	public Long getSubProductId() {
		return subProductId;
	}

	public void setSubProductId(Long subProductId) {
		this.subProductId = subProductId;
	}

	public String getOpenDay() {
		return openDay;
	}

	public void setOpenDay(String openDay) {
		this.openDay = openDay;
	}

	public BigDecimal getNetValue() {
		return netValue;
	}

	public void setNetValue(BigDecimal netValue) {
		this.netValue = netValue;
	}

}