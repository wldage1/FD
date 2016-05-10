package com.sw.plugins.config.websitesetting.entity;

import com.sw.core.data.entity.RelationEntity;

public class WebsiteSetting extends RelationEntity{
	
	private static final long serialVersionUID = 1L;

	public WebsiteSetting(){
		
	}
	
	private String code;
	
	private String name;
	
	private String value;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
