package com.sw.plugins.market.order.entity;

/**
 * 清算产品
 * 
 */
public class ClearedProduct extends HoldingProduct {

	private static final long serialVersionUID = 1L;
	
	/** 清算日期 */
	private String clearedTime;

	public ClearedProduct() {
	}

	public String getClearedTime() {
		return clearedTime;
	}

	public void setClearedTime(String clearedTime) {
		this.clearedTime = clearedTime;
	}

}