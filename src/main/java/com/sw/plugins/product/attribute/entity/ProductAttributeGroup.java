package com.sw.plugins.product.attribute.entity;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.sw.core.data.entity.RelationEntity;

public class ProductAttributeGroup extends RelationEntity {

	private static final long serialVersionUID = -3499785774039219402L;
	@Pattern(regexp="^(?!_)(?!.*?_$)[a-zA-Z0-9_\\u4e00-\\u9fa5]+$")
	@Size(min = 1, max = 20)
	private String name;
	@Pattern(regexp="^\\w+$")
	@Size(min = 1, max = 30)
	private String code;
	private String attributeString;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getAttributeString() {
		return attributeString;
	}
	public void setAttributeString(String attributeString) {
		this.attributeString = attributeString;
	}
	
	
	
}
