package com.sw.plugins.product.promotion.entity;

import com.sw.core.data.entity.RelationEntity;

public class ProductPositionMapping extends RelationEntity {

	private static final long serialVersionUID = 1L;

	private Long productId;// 产品ID
	private Long positionId;// 位置ID
	private Integer PositionOrder;// 排序

	public ProductPositionMapping() {
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getPositionId() {
		return positionId;
	}

	public void setPositionId(Long positionId) {
		this.positionId = positionId;
	}

	public Integer getPositionOrder() {
		return PositionOrder;
	}

	public void setPositionOrder(Integer positionOrder) {
		PositionOrder = positionOrder;
	}

}