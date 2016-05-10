package com.sw.plugins.extension.youku.videomanager.entity;

import com.sw.core.data.entity.RelationEntity;

public class VideoBasic extends RelationEntity{

	private static final long serialVersionUID = 1L;
	
	private String fileName;
	
	private String title;
	
	private String link;
	
	private String thumbnail;
	
	private String duration;
	
	private String description;
	
	private Short status;
	
	private String publishTime;
	
	private Short publishOrNot;
	
	private String publicType;
	
	private String copyrightType;
	
	private Long categoryId;
	
	private Long userId;
	
	private Long orgId;
	
	private String videoId;
	
	private Long productId;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public Short getPublishOrNot() {
		return publishOrNot;
	}

	public void setPublishOrNot(Short publishOrNot) {
		this.publishOrNot = publishOrNot;
	}

	public String getPublicType() {
		return publicType;
	}

	public void setPublicType(String publicType) {
		this.publicType = publicType;
	}

	public String getCopyrightType() {
		return copyrightType;
	}

	public void setCopyrightType(String copyrightType) {
		this.copyrightType = copyrightType;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getVideoId() {
		return videoId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	
	
}
