package com.sw.plugins.product.manage.entity;

import com.sw.core.data.entity.RelationEntity;

public class ProductAttribute extends RelationEntity{
	private static final long serialVersionUID = 1L;
	/** 资金投向ID **/
	private Long investCategoryID;
	/** 字段代码 **/
	private String code;
	/** 字段名称 **/
	private String name;
	/** 验证规则ID **/
	private Long validationRuleID;
	/** 是否必填 **/
	private Integer isNotNull;
	/** 是否启用 **/
	private Integer isEnabled;
	/** 是否为检索条件 **/
	private Integer isForQuery;
	/** 页面展示类型 **/
	private Integer pageShowType;
	/** 缺省显示值 **/
	private String defaultValue;
	/** 排序序号 **/
	private Integer sortNumber;
	public Long getInvestCategoryID() {
		return investCategoryID;
	}
	public void setInvestCategoryID(Long investCategoryID) {
		this.investCategoryID = investCategoryID;
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
	public Long getValidationRuleID() {
		return validationRuleID;
	}
	public void setValidationRuleID(Long validationRuleID) {
		this.validationRuleID = validationRuleID;
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
	public Integer getPageShowType() {
		return pageShowType;
	}
	public void setPageShowType(Integer pageShowType) {
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
}
