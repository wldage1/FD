package com.sw.plugins.product.manage.entity;

import com.sw.core.data.entity.RelationEntity;

public class ProductInformation extends RelationEntity{
	private static final long serialVersionUID = 1L;
	/** 产品ID **/
	private Long productID;
	/** 资讯  **/
	private Long informationID;
	
	//资讯IDS数组
	private String infromatinIds;
	
	public Long getProductID() {
		return productID;
	}
	public void setProductID(Long productID) {
		this.productID = productID;
	}
	public Long getInformationID() {
		return informationID;
	}
	public void setInformationID(Long informationID) {
		this.informationID = informationID;
	}
	public String getInfromatinIds() {
		return infromatinIds;
	}
	public void setInfromatinIds(String infromatinIds) {
		this.infromatinIds = infromatinIds;
	}
}
