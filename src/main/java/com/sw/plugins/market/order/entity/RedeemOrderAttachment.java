package com.sw.plugins.market.order.entity;

import com.sw.core.data.entity.RelationEntity;

public class RedeemOrderAttachment extends RelationEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RedeemOrderAttachment(){
		
	}
	
	
	private String title;
	
	private String fileName;
	
	private String fileUrl;
	
	private String uploadTime;
	
	private Short audited;
	
	private String orderNumber;
	
	public String getOrderNumber(){
		return orderNumber;
	}
	
	public void setOrderNumber(String orderNumber){
		this.orderNumber = orderNumber;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public Short getAudited() {
		return audited;
	}

	public void setAudited(Short audited) {
		this.audited = audited;
	}
}
