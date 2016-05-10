package com.sw.plugins.product.manage.entity;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.sw.core.data.entity.RelationEntity;

public class ProductCommissionRatio extends RelationEntity {

	private static final long serialVersionUID = -4451428678041631924L;

	/** 子产品ID */
	private Long subProductId;
	/** 生效日期 */
	private String effectiveDate;
	/** 生效日期 */
	private Integer sort;
	/** 存续阶段 */
	private String holdlingPhase;
	/** 阶段时间点（第N天） */
	private Integer holdingPhaseDays;
	/** 最大投资额度阀值 */
	private BigDecimal maxShareThreshold;
	/** 最小投资额度阀值 */
	private BigDecimal minShareThreshold;
	/** 年化发行成本率 */
	@NotNull
	private BigDecimal issuanceCostRate;
	/** 时间系数 */
	@NotNull
	private BigDecimal timeCoefficient;
	/** 个人服务费系数 */
	@NotNull
	private BigDecimal serviceChargeCoefficient;
	/** 机构服务费系数 */
	@NotNull
	private BigDecimal orgServiceChargeCoefficient;
	/** 激励费用率 */
	private BigDecimal incentiveFeeRate;
	/** 是否启用 */
	private Short isAvailable;

	/** 其它参数 **/
	private String productName;
	/** 是否变更子产品封闭类型 */
	private Short alterCloseType;
	private Long productId;

	public ProductCommissionRatio() {
	}

	public Long getSubProductId() {
		return subProductId;
	}

	public void setSubProductId(Long subProductId) {
		this.subProductId = subProductId;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getHoldlingPhase() {
		return holdlingPhase;
	}

	public void setHoldlingPhase(String holdlingPhase) {
		this.holdlingPhase = holdlingPhase;
	}

	public Integer getHoldingPhaseDays() {
		return holdingPhaseDays;
	}

	public void setHoldingPhaseDays(Integer holdingPhaseDays) {
		this.holdingPhaseDays = holdingPhaseDays;
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

	public BigDecimal getIssuanceCostRate() {
		return issuanceCostRate;
	}

	public void setIssuanceCostRate(BigDecimal issuanceCostRate) {
		this.issuanceCostRate = issuanceCostRate;
	}

	public BigDecimal getTimeCoefficient() {
		return timeCoefficient;
	}

	public void setTimeCoefficient(BigDecimal timeCoefficient) {
		this.timeCoefficient = timeCoefficient;
	}

	public BigDecimal getServiceChargeCoefficient() {
		return serviceChargeCoefficient;
	}

	public void setServiceChargeCoefficient(BigDecimal serviceChargeCoefficient) {
		this.serviceChargeCoefficient = serviceChargeCoefficient;
	}

	public BigDecimal getOrgServiceChargeCoefficient() {
		return orgServiceChargeCoefficient;
	}

	public void setOrgServiceChargeCoefficient(BigDecimal orgServiceChargeCoefficient) {
		this.orgServiceChargeCoefficient = orgServiceChargeCoefficient;
	}

	public BigDecimal getIncentiveFeeRate() {
		return incentiveFeeRate;
	}

	public void setIncentiveFeeRate(BigDecimal incentiveFeeRate) {
		this.incentiveFeeRate = incentiveFeeRate;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Short getAlterCloseType() {
		return alterCloseType;
	}

	public void setAlterCloseType(Short alterCloseType) {
		this.alterCloseType = alterCloseType;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Short getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Short isAvailable) {
		this.isAvailable = isAvailable;
	}

}
