package com.sw.plugins.product.manage.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.sw.core.data.entity.RelationEntity;
import com.sw.plugins.cooperate.provider.entity.Provider;
import com.sw.plugins.product.attribute.entity.ProductAttributeGroup;
import com.sw.plugins.product.capitalconfig.entity.ProductCategory;

public class Product extends RelationEntity {

	private static final long serialVersionUID = -4451028678041631925L;

	/** 居间公司ID **/
	private Long orgId;
	/** 居间公司名称 **/
	private String orgName;
	/** 机构类型 **/
	private Long orgTypeId;
	/************ 产品编号 ****************/
	@NotEmpty
	@Size(min = 1, max = 100)
	private String code;
	/******** 产品名称 ******/
	@NotEmpty
	@Size(min = 1, max = 100)
	private String name;
	/** 产品简称 **/
	private String shortName;
	/** 产品图片 **/
	private String productImage;
	/** 产品类型 **/
	private Short type;
	/** 资金投向ID **/
	private Long investCategoryId;
	/** 供应商ID **/
	@NotNull
	private Long providerId;
	/** 募集开始时间 **/
	@NotEmpty
	private String raiseStartTime;
	/** 封帐日期 **/
	@NotEmpty
	private String raiseFinishTime;
	/** 预期收益类型 **/
	private Short profitType;
	/** 是否共享募集额度 **/
	private Short isTotalShare;
	/** 最小预期收益率 **/
	@NotNull
	private BigDecimal minProfitRatio;
	/** 最大预期收益率 **/
	@NotNull
	private BigDecimal maxProfitRatio;
	/** 订购起点(元) **/
	@NotEmpty
	private String beginningShare;
	/** 订购递增金额(元) **/
	@NotNull
	private BigDecimal incrementalShare;
	/** 最小募集额度(元) **/
	@NotNull
	private BigDecimal minTotalShare;
	/** 最大募集额度(元) **/
	@NotNull
	private BigDecimal maxTotalShare;
	/** 产品成立日期 **/
	private String foundDate;
	/** 产品止期 **/
	private String stopDate;
	/** 产品期限类型 **/
	private Short deadlineType;
	/** 产品期限数据库类型 1:月2:日 **/
	private Short deadlineDataType;
	/** 最小产品期限(月) **/
	@NotNull
	private Integer minDeadline;
	/** 最大产品期限(月) **/
	@NotNull
	private Integer maxDeadline;
	/** 评级 **/
	private Integer level;
	/** 排序号码 **/
	private Integer sortNumber;
	/**
	 * 产品销售状态 0-创建中 1-申请创建 2-申请未通过 3-申请通过 4-提交复核 5-复核未通过 6-复核通过
	 */
	private Short status;
	/**
	 * 产品销售状态 0-创建中 1-计划投放 2-在售 3-停止销售 4-发行失败 5-已成立 6-已成立且在售（阳光私募） 7-已清算 8-取消投放
	 * 9-产品封帐
	 * */
	private Short sellStatus;

	/**
	 * 1-全款先到先得,2-预约,3-定向推送
	 * */
	private Short sellType;

	/** 产品发布状态 0未发布，1已发布 **/
	private Short released;
	/** 产品描述 **/
	private String profile;
	/** 其他说明 **/
	private String description;
	/** 是否可收藏 **/
	private Integer isFavorites;
	/** 是否可预约 **/
	private Integer isOrder;
	/** 是否可以赎回 **/
	private Short isRedeem;
	/** 是否可以追加 **/
	private Short isAdd;
	/** 居间费状态 0-未核定 1-已核定 2-已复核 **/
	private Short commissionStatus;
	/** 账号信息 **/
	private String accountInfo;
	/** 信托 委托人数量上限 **/
	private Integer maxClientCount;
	/** 阳光私募 ,信托小额投资人数量上限 */
	private Integer maxLowAmountClientCount;
	/** 阳光私募 ,信托小额投资金额上限 */
	@NotNull
	private BigDecimal lowAmountThreshold;
	/** 小额投资人数上限预警 */
	private Integer warningLowAmountClientCount;
	/** 是否共享小额投资人数上限 */
	private Short isTotalLowAmountClientCount;
	/** 阳光私募 封闭类型 **/
	private Integer closeType;
	/** 产品好评率（百分数） */
	private String goodCommentRatio;
	/** 产品差评率（百分数） **/
	private String badCommentRatio;
	/** 是否公示 */
	private Short isNoTice;
	/** 问卷推送状态 **/
	private Short pushStatus;
	/** 合同前缀 */
	private String contractPrefix;
	/** 募集方所在区域 */
	private Short raiseArea;
	/** 是否是特供产品 **/
	private Short isSpecialProvisionProduct;
	/** 产品管理员 */
	private String pomanager;
	/** 销控管理员 */
	private String scmanager;
	/** 产品销售方 1:优财富2：非优财富*/
	private Short salerType;
	
	

	/****** 其它参数 ******/
	/** 产品类型名称 */
	private String typeName;
	/** 资金投向名称 */
	private String investCateName;
	/** 产品附加属性值 */
	private List<ProductAttributeValue> productAttributeValueList;
	/** 子产品 */
	private SubProduct subProduct;
	/** 子产品ID */
	private Long subProductId;
	/** 产品资料 */
	private List<ProductAttachment> productAttachmentList;
	/** 子产品LIST */
	private List<SubProduct> subProductList;
	/** 产品秒杀 */
	private ProductSeckill productSeckill;
	/** 新订单预约数 */
	private Integer newOrderCount;

	private Short IsRemittance;
	// 创建人姓名
	private String createUserName;

	private ProductCategory productCategory;
	private ProductAttributeGroup productAttributeGroup;
	private Provider provider;

	private String providerShortName;
	
	/** 是否是产品管理员 */
	private Short isPoManager;
	/** 是否是销控管理员 */
	private Short isScManager;

	/******** 属性值 ******/
	private String attributeValue;
	private Long attributeId;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public Long getInvestCategoryId() {
		return investCategoryId;
	}

	public void setInvestCategoryId(Long investCategoryId) {
		this.investCategoryId = investCategoryId;
	}

	public Long getProviderId() {
		return providerId;
	}

	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}

	public String getRaiseStartTime() {
		return raiseStartTime;
	}

	public void setRaiseStartTime(String raiseStartTime) {
		this.raiseStartTime = raiseStartTime;
	}

	public String getRaiseFinishTime() {
		return raiseFinishTime;
	}

	public void setRaiseFinishTime(String raiseFinishTime) {
		this.raiseFinishTime = raiseFinishTime;
	}

	public Short getProfitType() {
		return profitType;
	}

	public void setProfitType(Short profitType) {
		this.profitType = profitType;
	}

	public String getBeginningShare() {
		return beginningShare;
	}

	public void setBeginningShare(String beginningShare) {
		this.beginningShare = beginningShare;
	}

	public BigDecimal getIncrementalShare() {
		return incrementalShare;
	}

	public void setIncrementalShare(BigDecimal incrementalShare) {
		this.incrementalShare = incrementalShare;
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

	public String getFoundDate() {
		return foundDate;
	}

	public void setFoundDate(String foundDate) {
		this.foundDate = foundDate;
	}

	public String getStopDate() {
		return stopDate;
	}

	public void setStopDate(String stopDate) {
		this.stopDate = stopDate;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getSortNumber() {
		return sortNumber;
	}

	public void setSortNumber(Integer sortNumber) {
		this.sortNumber = sortNumber;
	}

	public Short getSellStatus() {
		return sellStatus;
	}

	public void setSellStatus(Short sellStatus) {
		this.sellStatus = sellStatus;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public Short getReleased() {
		return released;
	}

	public void setReleased(Short released) {
		this.released = released;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getIsFavorites() {
		return isFavorites;
	}

	public void setIsFavorites(Integer isFavorites) {
		this.isFavorites = isFavorites;
	}

	public ProductCategory getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	public ProductAttributeGroup getProductAttributeGroup() {
		return productAttributeGroup;
	}

	public void setProductAttributeGroup(ProductAttributeGroup productAttributeGroup) {
		this.productAttributeGroup = productAttributeGroup;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public List<ProductAttributeValue> getProductAttributeValueList() {
		return productAttributeValueList;
	}

	public void setProductAttributeValueList(List<ProductAttributeValue> productAttributeValueList) {
		this.productAttributeValueList = productAttributeValueList;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public String getInvestCateName() {
		return investCateName;
	}

	public void setInvestCateName(String investCateName) {
		this.investCateName = investCateName;
	}

	public Long getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(Long attributeId) {
		this.attributeId = attributeId;
	}

	public Integer getMaxClientCount() {
		return maxClientCount;
	}

	public void setMaxClientCount(Integer maxClientCount) {
		this.maxClientCount = maxClientCount;
	}

	public Integer getMaxLowAmountClientCount() {
		return maxLowAmountClientCount;
	}

	public void setMaxLowAmountClientCount(Integer maxLowAmountClientCount) {
		this.maxLowAmountClientCount = maxLowAmountClientCount;
	}

	public BigDecimal getLowAmountThreshold() {
		return lowAmountThreshold;
	}

	public void setLowAmountThreshold(BigDecimal lowAmountThreshold) {
		this.lowAmountThreshold = lowAmountThreshold;
	}

	public Integer getCloseType() {
		return closeType;
	}

	public void setCloseType(Integer closeType) {
		this.closeType = closeType;
	}

	public String getGoodCommentRatio() {
		return goodCommentRatio;
	}

	public void setGoodCommentRatio(String goodCommentRatio) {
		this.goodCommentRatio = goodCommentRatio;
	}

	public String getBadCommentRatio() {
		return badCommentRatio;
	}

	public void setBadCommentRatio(String badCommentRatio) {
		this.badCommentRatio = badCommentRatio;
	}

	public Integer getIsOrder() {
		return isOrder;
	}

	public void setIsOrder(Integer isOrder) {
		this.isOrder = isOrder;
	}

	public SubProduct getSubProduct() {
		return subProduct;
	}

	public void setSubProduct(SubProduct subProduct) {
		this.subProduct = subProduct;
	}

	public List<ProductAttachment> getProductAttachmentList() {
		return productAttachmentList;
	}

	public void setProductAttachmentList(List<ProductAttachment> productAttachmentList) {
		this.productAttachmentList = productAttachmentList;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Short getSellType() {
		return sellType;
	}

	public void setSellType(Short sellType) {
		this.sellType = sellType;
	}

	public Short getDeadlineType() {
		return deadlineType;
	}

	public void setDeadlineType(Short deadlineType) {
		this.deadlineType = deadlineType;
	}

	public Integer getMinDeadline() {
		return minDeadline;
	}

	public void setMinDeadline(Integer minDeadline) {
		this.minDeadline = minDeadline;
	}

	public Integer getMaxDeadline() {
		return maxDeadline;
	}

	public void setMaxDeadline(Integer maxDeadline) {
		this.maxDeadline = maxDeadline;
	}

	public BigDecimal getMinProfitRatio() {
		return minProfitRatio;
	}

	public void setMinProfitRatio(BigDecimal minProfitRatio) {
		this.minProfitRatio = minProfitRatio;
	}

	public BigDecimal getMaxProfitRatio() {
		return maxProfitRatio;
	}

	public void setMaxProfitRatio(BigDecimal maxProfitRatio) {
		this.maxProfitRatio = maxProfitRatio;
	}

	public Short getIsTotalShare() {
		return isTotalShare;
	}

	public void setIsTotalShare(Short isTotalShare) {
		this.isTotalShare = isTotalShare;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public List<SubProduct> getSubProductList() {
		return subProductList;
	}

	public void setSubProductList(List<SubProduct> subProductList) {
		this.subProductList = subProductList;
	}

	public Short getIsRedeem() {
		return isRedeem;
	}

	public void setIsRedeem(Short isRedeem) {
		this.isRedeem = isRedeem;
	}

	public Short getIsAdd() {
		return isAdd;
	}

	public void setIsAdd(Short isAdd) {
		this.isAdd = isAdd;
	}

	public ProductSeckill getProductSeckill() {
		return productSeckill;
	}

	public void setProductSeckill(ProductSeckill productSeckill) {
		this.productSeckill = productSeckill;
	}

	public Long getOrgTypeId() {
		return orgTypeId;
	}

	public void setOrgTypeId(Long orgTypeId) {
		this.orgTypeId = orgTypeId;
	}

	public Short getCommissionStatus() {
		return commissionStatus;
	}

	public void setCommissionStatus(Short commissionStatus) {
		this.commissionStatus = commissionStatus;
	}

	public String getAccountInfo() {
		return accountInfo;
	}

	public void setAccountInfo(String accountInfo) {
		this.accountInfo = accountInfo;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Integer getWarningLowAmountClientCount() {
		return warningLowAmountClientCount;
	}

	public void setWarningLowAmountClientCount(Integer warningLowAmountClientCount) {
		this.warningLowAmountClientCount = warningLowAmountClientCount;
	}

	public Short getIsTotalLowAmountClientCount() {
		return isTotalLowAmountClientCount;
	}

	public void setIsTotalLowAmountClientCount(Short isTotalLowAmountClientCount) {
		this.isTotalLowAmountClientCount = isTotalLowAmountClientCount;
	}

	public Short getIsNoTice() {
		return isNoTice;
	}

	public void setIsNoTice(Short isNoTice) {
		this.isNoTice = isNoTice;
	}

	public Short getPushStatus() {
		return pushStatus;
	}

	public void setPushStatus(Short pushStatus) {
		this.pushStatus = pushStatus;
	}

	public Long getSubProductId() {
		return subProductId;
	}

	public void setSubProductId(Long subProductId) {
		this.subProductId = subProductId;
	}

	public Short getIsRemittance() {
		return IsRemittance;
	}

	public void setIsRemittance(Short isRemittance) {
		IsRemittance = isRemittance;
	}

	public String getProviderShortName() {
		return providerShortName;
	}

	public void setProviderShortName(String providerShortName) {
		this.providerShortName = providerShortName;
	}

	public Short getDeadlineDataType() {
		return deadlineDataType;
	}

	public void setDeadlineDataType(Short deadlineDataType) {
		this.deadlineDataType = deadlineDataType;
	}

	public String getContractPrefix() {
		return contractPrefix;
	}

	public void setContractPrefix(String contractPrefix) {
		this.contractPrefix = contractPrefix;
	}

	public Integer getNewOrderCount() {
		return newOrderCount;
	}

	public void setNewOrderCount(Integer newOrderCount) {
		this.newOrderCount = newOrderCount;
	}

	public Short getRaiseArea() {
		return raiseArea;
	}

	public void setRaiseArea(Short raiseArea) {
		this.raiseArea = raiseArea;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public Short getIsSpecialProvisionProduct() {
		return isSpecialProvisionProduct;
	}

	public void setIsSpecialProvisionProduct(Short isSpecialProvisionProduct) {
		this.isSpecialProvisionProduct = isSpecialProvisionProduct;
	}

	public String getPomanager() {
		return pomanager;
	}

	public void setPomanager(String pomanager) {
		this.pomanager = pomanager;
	}

	public String getScmanager() {
		return scmanager;
	}

	public void setScmanager(String scmanager) {
		this.scmanager = scmanager;
	}

	public Short getSalerType() {
		return salerType;
	}

	public void setSalerType(Short salerType) {
		this.salerType = salerType;
	}

	public Short getIsPoManager() {
		return isPoManager;
	}

	public void setIsPoManager(Short isPoManager) {
		this.isPoManager = isPoManager;
	}

	public Short getIsScManager() {
		return isScManager;
	}

	public void setIsScManager(Short isScManager) {
		this.isScManager = isScManager;
	}

}
