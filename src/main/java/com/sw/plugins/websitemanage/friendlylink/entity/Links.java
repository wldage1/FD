package com.sw.plugins.websitemanage.friendlylink.entity;

import org.hibernate.validator.constraints.NotEmpty;

import com.sw.core.data.entity.RelationEntity;

/**
 * 友情链接Entity
 * 
 * @author Flora
 */
public class Links extends RelationEntity {

	private static final long serialVersionUID = -4315334295473042713L;

	/** 链接名称 */
	@NotEmpty
	private String name;

	/** 链接地址 */
	@NotEmpty
	private String url;

	/** 上传图片路径 */
	@NotEmpty
	private String logo;

	private String picName;

	public String getPicName() {
		return picName;
	}

	public void setPicName(String picName) {
		this.picName = picName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

}
