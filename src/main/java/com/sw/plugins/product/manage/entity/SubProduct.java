package com.sw.plugins.product.manage.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.sw.core.data.entity.RelationEntity;

public class SubProduct extends RelationEntity {

	private static final long serialVersionUID = -4451028678041631924L;

	/** 产品ID */
	private Long productId;
	/** 子产品类型 */
	private String type;
	/** 子产品期限(月) */
	private Integer deadline;
	/** 最小募集额度(元) */
	private BigDecimal minTotalShare;
	/** 最大募集额度(元) */
	private BigDecimal maxTotalShare;
	/** 预期收益类型 1:浮动 2 固定 */
	private Short profitType;
	/** ProfitType为1 时自定义收益率 */
	private String profitRatioDesc;
	/** 销售系数 */
	private BigDecimal salesFactor;
	/** 小额投资人数量上限 */
	private Integer maxLowAmountClientCount;
	/** 小额投资人数上限预警 */
	private Integer warningLowAmountClientCount;
	/** 预警额度(元) */
	private BigDecimal warningShare;
	/** 是否允许打款 */
	private Short isRemittance;
	/** 是否可预约 */
	private Short isOrder;
	/** 是否可收藏 */
	private Short isFavorites;
	/** 佣金支付方式1-打款即支付 2-预约即支付 3-资金到账即支付 */
	private Short payWay;
	/** 认购起点 */
	private BigDecimal beginningShare;

	/** 其它参数 **/
	private Short isTotalShare;
	private String productName;
	private Short productType;
	private Short productSaleStatus;
	private ProductProfit productProfit;
	private List<ProductProfit> productProfitList;
	private List<ProductNetValue> productNetValueList;
	private Short isTotalLowAmountClientCount;

	public SubProduct() {
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getDeadline() {
		return deadline;
	}

	public void setDeadline(Integer deadline) {
		this.deadline = deadline;
	}

	public BigDecimal getMinTotalShare() {
		return minTotalShare;
	}

	public void setMinTotalShare(BigDecimal minTotalShare) {
		this.minTotalShare = minTotalShare;
	}

	public BigDecimal getMaxTotalShare() {
		return maxTotalShare;
	}

	public void setMaxTotalShare(BigDecimal maxTotalShare) {
		this.maxTotalShare = maxTotalShare;
	}

	public BigDecimal getWarningShare() {
		return warningShare;
	}

	public void setWarningShare(BigDecimal warningShare) {
		this.warningShare = warningShare;
	}

	public Short getIsOrder() {
		return isOrder;
	}

	public void setIsOrder(Short isOrder) {
		this.isOrder = isOrder;
	}

	public Short getIsFavorites() {
		return isFavorites;
	}

	public void setIsFavorites(Short isFavorites) {
		this.isFavorites = isFavorites;
	}

	public Short getProductType() {
		return productType;
	}

	public void setProductType(Short productType) {
		this.productType = productType;
	}

	public ProductProfit getProductProfit() {
		return productProfit;
	}

	public void setProductProfit(ProductProfit productProfit) {
		this.productProfit = productProfit;
	}

	public List<ProductProfit> getProductProfitList() {
		return productProfitList;
	}

	public void setProductProfitList(List<ProductProfit> productProfitList) {
		this.productProfitList = productProfitList;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public List<ProductNetValue> getProductNetValueList() {
		return productNetValueList;
	}

	public void setProductNetValueList(List<ProductNetValue> productNetValueList) {
		this.productNetValueList = productNetValueList;
	}

	public Short getIsRemittance() {
		return isRemittance;
	}

	public void setIsRemittance(Short isRemittance) {
		this.isRemittance = isRemittance;
	}

	public Short getPayWay() {
		return payWay;
	}

	public void setPayWay(Short payWay) {
		this.payWay = payWay;
	}

	public Integer getMaxLowAmountClientCount() {
		return maxLowAmountClientCount;
	}

	public void setMaxLowAmountClientCount(Integer maxLowAmountClientCount) {
		this.maxLowAmountClientCount = maxLowAmountClientCount;
	}

	public Integer getWarningLowAmountClientCount() {
		return warningLowAmountClientCount;
	}

	public void setWarningLowAmountClientCount(Integer warningLowAmountClientCount) {
		this.warningLowAmountClientCount = warningLowAmountClientCount;
	}

	public Short getProfitType() {
		return profitType;
	}

	public void setProfitType(Short profitType) {
		this.profitType = profitType;
	}

	public String getProfitRatioDesc() {
		return profitRatioDesc;
	}

	public void setProfitRatioDesc(String profitRatioDesc) {
		this.profitRatioDesc = profitRatioDesc;
	}

	public BigDecimal getSalesFactor() {
		return salesFactor;
	}

	public void setSalesFactor(BigDecimal salesFactor) {
		this.salesFactor = salesFactor;
	}

	public Short getIsTotalShare() {
		return isTotalShare;
	}

	public void setIsTotalShare(Short isTotalShare) {
		this.isTotalShare = isTotalShare;
	}

	public Short getIsTotalLowAmountClientCount() {
		return isTotalLowAmountClientCount;
	}

	public void setIsTotalLowAmountClientCount(Short isTotalLowAmountClientCount) {
		this.isTotalLowAmountClientCount = isTotalLowAmountClientCount;
	}

	public void setBeginningShare(BigDecimal beginningShare) {
		this.beginningShare = beginningShare;
	}

	public BigDecimal getBeginningShare() {
		return beginningShare;
	}

	public Short getProductSaleStatus() {
		return productSaleStatus;
	}

	public void setProductSaleStatus(Short productSaleStatus) {
		this.productSaleStatus = productSaleStatus;
	}

}
