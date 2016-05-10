package com.sw.plugins.product.attribute.entity;

import com.sw.core.data.entity.RelationEntity;

public class ProductAttribute extends RelationEntity {

	private static final long serialVersionUID = 3384838021103429828L;
	private Long investCategoryId;
	private String code;
	private String name;
	private Long validationRuleId;
	private Integer isNotNull;
	private Integer isEnabled;
	private Integer isForQuery;
	private Long pageShowType;
	private String defaultValue;
	private Integer sortNumber;
	private String productId;
	private String productName;
	private String productCode;
	private String productAttributeValue;

	public Long getInvestCategoryId() {
		return investCategoryId;
	}

	public void setInvestCategoryId(Long investCategoryId) {
		this.investCategoryId = investCategoryId;
	}

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

	public Long getValidationRuleId() {
		return validationRuleId;
	}

	public void setValidationRuleId(Long validationRuleId) {
		this.validationRuleId = validationRuleId;
	}

	public Integer getIsNotNull() {
		return isNotNull;
	}

	public void setIsNotNull(Integer isNotNull) {
		this.isNotNull = isNotNull;
	}

	public Integer getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Integer getIsForQuery() {
		return isForQuery;
	}

	public void setIsForQuery(Integer isForQuery) {
		this.isForQuery = isForQuery;
	}

	public Long getPageShowType() {
		return pageShowType;
	}

	public void setPageShowType(Long pageShowType) {
		this.pageShowType = pageShowType;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Integer getSortNumber() {
		return sortNumber;
	}

	public void setSortNumber(Integer sortNumber) {
		this.sortNumber = sortNumber;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductAttributeValue() {
		return productAttributeValue;
	}

	public void setProductAttributeValue(String productAttributeValue) {
		this.productAttributeValue = productAttributeValue;
	}

}
