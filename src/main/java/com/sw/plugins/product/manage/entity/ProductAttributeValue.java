package com.sw.plugins.product.manage.entity;

import com.sw.core.data.entity.RelationEntity;

public class ProductAttributeValue extends RelationEntity {

	/**
	 * 产品属性值
	 * @author baokai
	 * Created on :2013-3-20 下午6:29:22
	 */
	private static final long serialVersionUID = 8723611759305163753L;
	private Long productId;
	private Long attributeId;
	private String value;
	
	private ProductAttribute productAttribute;
	
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Long getAttributeId() {
		return attributeId;
	}
	public void setAttributeId(Long attributeId) {
		this.attributeId = attributeId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public ProductAttribute getProductAttribute() {
		return productAttribute;
	}
	public void setProductAttribute(ProductAttribute productAttribute) {
		this.productAttribute = productAttribute;
	}
}
