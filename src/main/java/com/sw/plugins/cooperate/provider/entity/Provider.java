package com.sw.plugins.cooperate.provider.entity;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.sw.core.data.entity.RelationEntity;

/**
 * 供应商实体
 * 
 * @author runchao
 */
public class Provider extends RelationEntity{

	private static final long serialVersionUID = -5162777907882557479L;

	//名称
	@Size(min=1, max=30)
	//@Pattern(regexp="^[a-zA-Z\\u4e00-\\u9fa5]+$")
	private String name;
	//简称
	@Size(min=1, max=10)
	//@Pattern(regexp="^[a-zA-Z\\u4e00-\\u9fa5]+$")
	private String shortName;
	//Logo
	@NotEmpty
	private String logo;
	//简介
	private String discription;
	//机构类型
	private Integer companyType;
	//状态
	private Integer status;
	//删除状态
	private Integer delStatus;
	//成立日期
	private String createDate;
	//主要股东
	private String substantialShareholder;
	//注册资本
	private String registeredCapital;
	//最新财年管理资产规模
	private String latestAssetSize;
	//公司注册地
	private String companiesRegistryAddress;
	//荣誉奖项
	private String awards;
	 //待审核数量
    private Integer productCount;
    //排序 大数优先
    private Integer sortNum;
    
	public Integer getProductCount() {
		return productCount;
	}
	public void setProductCount(Integer productCount) {
		this.productCount = productCount;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getSubstantialShareholder() {
		return substantialShareholder;
	}
	public void setSubstantialShareholder(String substantialShareholder) {
		this.substantialShareholder = substantialShareholder;
	}
	public String getRegisteredCapital() {
		return registeredCapital;
	}
	public void setRegisteredCapital(String registeredCapital) {
		this.registeredCapital = registeredCapital;
	}
	public String getLatestAssetSize() {
		return latestAssetSize;
	}
	public void setLatestAssetSize(String latestAssetSize) {
		this.latestAssetSize = latestAssetSize;
	}
	public String getCompaniesRegistryAddress() {
		return companiesRegistryAddress;
	}
	public void setCompaniesRegistryAddress(String companiesRegistryAddress) {
		this.companiesRegistryAddress = companiesRegistryAddress;
	}
	public String getAwards() {
		return awards;
	}
	public void setAwards(String awards) {
		this.awards = awards;
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
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getDiscription() {
		return discription;
	}
	public void setDiscription(String discription) {
		this.discription = discription;
	}
	public Integer getCompanyType() {
		return companyType;
	}
	public void setCompanyType(Integer companyType) {
		this.companyType = companyType;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getDelStatus() {
		return delStatus;
	}
	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}
	public Integer getSortNum() {
		return sortNum;
	}
	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}
}
