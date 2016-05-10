package com.sw.plugins.product.manage.entity;

import java.util.List;

import com.sw.core.data.entity.RelationEntity;

public class ProductOrgMapping extends RelationEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long productID;
	
	private Long orgID;
	
	private Long[] orgIDs;
	
	private Short type;// 1:非嘉华机构 2：个人3：嘉华
	
	private String name;
	
	private List<ProductOrgMapping> mappingList;
	
	public Long getProductID() {
		return productID;
	}

	public Long getOrgID() {
		return orgID;
	}

	public void setProductID(Long productID) {
		this.productID = productID;
	}

	public void setOrgID(Long orgID) {
		this.orgID = orgID;
	}

	public Long[] getOrgIDs() {
		return orgIDs;
	}

	public void setOrgIDs(Long[] orgIDs) {
		this.orgIDs = orgIDs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public List<ProductOrgMapping> getMappingList() {
		return mappingList;
	}

	public void setMappingList(List<ProductOrgMapping> mappingList) {
		this.mappingList = mappingList;
	}


	
}
