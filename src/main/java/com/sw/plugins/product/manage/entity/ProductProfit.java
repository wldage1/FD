package com.sw.plugins.product.manage.entity;

import java.math.BigDecimal;

import com.sw.core.data.entity.RelationEntity;

public class ProductProfit extends RelationEntity {

	private static final long serialVersionUID = 1L;

	/** 子产品ID */
	private Long subProductId;
	/** 最大额度阀值 */
	private BigDecimal maxShareThreshold;
	/** 最小额度阀值 */
	private BigDecimal minShareThreshold;
	/** 预期收益率 */
	private String profitRatio;
	/** 生效日期 */
	private String effectiveDate;
	private BigDecimal investAmount;
	
	//产品类型
	private Short productType;

	public ProductProfit() {
	}

	public Long getSubProductId() {
		return subProductId;
	}

	public void setSubProductId(Long subProductId) {
		this.subProductId = subProductId;
	}

	public BigDecimal getMaxShareThreshold() {
		return maxShareThreshold;
	}

	public void setMaxShareThreshold(BigDecimal maxShareThreshold) {
		this.maxShareThreshold = maxShareThreshold;
	}

	public BigDecimal getMinShareThreshold() {
		return minShareThreshold;
	}

	public void setMinShareThreshold(BigDecimal minShareThreshold) {
		this.minShareThreshold = minShareThreshold;
	}

	public String getProfitRatio() {
		return profitRatio;
	}

	public void setProfitRatio(String profitRatio) {
		this.profitRatio = profitRatio;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public BigDecimal getInvestAmount() {
		return investAmount;
	}

	public void setInvestAmount(BigDecimal investAmount) {
		this.investAmount = investAmount;
	}

	public Short getProductType() {
		return productType;
	}

	public void setProductType(Short productType) {
		this.productType = productType;
	}
	
}
