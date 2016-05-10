package com.sw.plugins.market.order.entity;

import java.math.BigDecimal;

import com.sw.core.data.entity.RelationEntity;

public class TaxRate extends RelationEntity{

	private static final long serialVersionUID = 1L;
	/** 应纳所得税额度最小阀值 **/
	private BigDecimal minPersonalIncomeTax;
	/** 应纳所得税额度最大阀值 **/
	private BigDecimal maxPersonalIncomeTax;
	/** 适用税率 **/
	private BigDecimal taxRate;
	/** 速算扣除数 **/
	private BigDecimal deductCount;
	
	public BigDecimal getMinPersonalIncomeTax() {
		return minPersonalIncomeTax;
	}
	public void setMinPersonalIncomeTax(BigDecimal minPersonalIncomeTax) {
		this.minPersonalIncomeTax = minPersonalIncomeTax;
	}
	public BigDecimal getMaxPersonalIncomeTax() {
		return maxPersonalIncomeTax;
	}
	public void setMaxPersonalIncomeTax(BigDecimal maxPersonalIncomeTax) {
		this.maxPersonalIncomeTax = maxPersonalIncomeTax;
	}
	public BigDecimal getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}
	public BigDecimal getDeductCount() {
		return deductCount;
	}
	public void setDeductCount(BigDecimal deductCount) {
		this.deductCount = deductCount;
	}
}
