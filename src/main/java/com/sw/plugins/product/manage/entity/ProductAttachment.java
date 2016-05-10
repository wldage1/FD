package com.sw.plugins.product.manage.entity;

import com.sw.core.data.entity.RelationEntity;

public class ProductAttachment extends RelationEntity {
	private static final long serialVersionUID = -613026525972338773L;

	private long productId;
	private String iDInNoSql;
	private String attachTitle;
	private String fileName;
	private Integer size;
	private String fileUrl;
	private Short downloadPermission;
	private Short audited;
	private Short isConvert;
	private Short status;
	

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getiDInNoSql() {
		return iDInNoSql;
	}

	public void setiDInNoSql(String iDInNoSql) {
		this.iDInNoSql = iDInNoSql;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getAttachTitle() {
		return attachTitle;
	}

	public void setAttachTitle(String attachTitle) {
		this.attachTitle = attachTitle;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public Short getAudited() {
		return audited;
	}

	public void setAudited(Short audited) {
		this.audited = audited;
	}

	public Short getDownloadPermission() {
		return downloadPermission;
	}

	public void setDownloadPermission(Short downloadPermission) {
		this.downloadPermission = downloadPermission;
	}

	public Short getIsConvert() {
		return isConvert;
	}

	public void setIsConvert(Short isConvert) {
		this.isConvert = isConvert;
	}
	
}
