package com.sw.plugins.product.manage.entity;

import com.sw.core.data.entity.RelationEntity;

public class ProductSeckill extends RelationEntity {
	private static final long serialVersionUID = 1L;
	
	/** 产品ID **/
	private Long productId;
	/** 排序  **/
	private Short sortNumber;
	/** 开始时间  **/
	private String startTime;
	/** 结束时间  **/
	private String finishTime;
	/** 状态  **/
	private Short status;
	
	
	/** 其它参数 */
	private String productName;
	
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Short getSortNumber() {
		return sortNumber;
	}
	public void setSortNumber(Short sortNumber) {
		this.sortNumber = sortNumber;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
}
