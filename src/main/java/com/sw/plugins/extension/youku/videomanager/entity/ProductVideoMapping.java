package com.sw.plugins.extension.youku.videomanager.entity;

import com.sw.core.data.entity.RelationEntity;

public class ProductVideoMapping extends RelationEntity{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long productId;
	
	private Long videoBasicId;

	public Long getProductId() {
		return productId;
	}

	public Long getVideoBasicId() {
		return videoBasicId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public void setVideoBasicId(Long videoBasicId) {
		this.videoBasicId = videoBasicId;
	}

	
}
