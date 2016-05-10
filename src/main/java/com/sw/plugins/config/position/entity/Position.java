package com.sw.plugins.config.position.entity;

import com.sw.core.data.entity.RelationEntity;

public class Position extends RelationEntity {

	private static final long serialVersionUID = 1L;

	private String name;
	private String code;
	private String url;
	private String picture;

	public Position() {
	}

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

}
