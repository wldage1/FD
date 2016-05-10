package com.sw.plugins.product.promotion.entity;

import java.util.List;

import com.sw.core.data.entity.RelationEntity;

public class Promotion extends RelationEntity {

	private static final long serialVersionUID = 1L;

	private Long positionId;
	private Long productId;
	private Long positionMappingId;
	private String productName;
	private String providerShortName;
	private Integer positionOrder;

	private List<ProductPositionMapping> positionMappingList;
	private ProductPositionMapping positonMapping;
	
	//查询条件  登陆人的机构ID
	private Long OrgID;
	//产品销售状态
	private Short sellStatus;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getPositionMappingId() {
		return positionMappingId;
	}

	public void setPositionMappingId(Long positionMappingId) {
		this.positionMappingId = positionMappingId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProviderShortName() {
		return providerShortName;
	}
	
	public void setProviderShortName(String providerShortName) {
		this.providerShortName = providerShortName;
	}
	
	public Integer getPositionOrder() {
		return positionOrder;
	}

	public void setPositionOrder(Integer positionOrder) {
		this.positionOrder = positionOrder;
	}

	public Long getPositionId() {
		return positionId;
	}

	public void setPositionId(Long positionId) {
		this.positionId = positionId;
	}

	public ProductPositionMapping getPositonMapping() {
		return positonMapping;
	}

	public void setPositonMapping(ProductPositionMapping positonMapping) {
		this.positonMapping = positonMapping;
	}

	public List<ProductPositionMapping> getPositionMappingList() {
		return positionMappingList;
	}

	public void setPositionMappingList(List<ProductPositionMapping> positionMappingList) {
		this.positionMappingList = positionMappingList;
	}

	public Long getOrgID() {
		return OrgID;
	}

	public void setOrgID(Long orgID) {
		OrgID = orgID;
	}

	public Short getSellStatus() {
		return sellStatus;
	}

	public void setSellStatus(Short sellStatus) {
		this.sellStatus = sellStatus;
	}

}
