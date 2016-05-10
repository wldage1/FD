package com.sw.plugins.product.manage.entity;

import javax.validation.constraints.Size;
import javax.xml.crypto.Data;

import com.sw.core.data.entity.RelationEntity;

public class ProductApplication extends RelationEntity{
	private static final long serialVersionUID = 1L;
	/** 产品ID **/
	private Long productID;
	/** 供应商ID **/
	private Long providerUserID;
	/** 平台方ID **/
	private Long userID;
	/** 审核状态  1审核通过，0审核未通过  **/
	private Short status;
	/** 反馈信息 **/
	@Size(max=200)
	private String feedback;
	/**审核日期**/
	private Data auditTime;
	
	//额外属性  
	//产品名称
	private String name;
	//产品类型
	private Integer type;
	//产品Bean
	private Product product;
	//产品销售状态
	private Short sellStatusTemp;
	
	public Long getProductID() {
		return productID;
	}
	public void setProductID(Long productID) {
		this.productID = productID;
	}
	public Long getProviderUserID() {
		return providerUserID;
	}
	public void setProviderUserID(Long providerUserID) {
		this.providerUserID = providerUserID;
	}
	public Long getUserID() {
		return userID;
	}
	public void setUserID(Long userID) {
		this.userID = userID;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	public Data getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Data auditTime) {
		this.auditTime = auditTime;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Short getSellStatusTemp() {
		return sellStatusTemp;
	}
	public void setSellStatusTemp(Short sellStatusTemp) {
		this.sellStatusTemp = sellStatusTemp;
	}
}
