package com.sw.plugins.config.globalparameter.entity;

import org.hibernate.validator.constraints.NotEmpty;

import com.sw.core.data.entity.RelationEntity;

public class GlobalParameter extends RelationEntity{
	

	private static final long serialVersionUID = 1L;

	public GlobalParameter(){
		
	}
	@NotEmpty
	private String code;
	@NotEmpty
	private String name;
	@NotEmpty
	private String value;
	private String orgName;
	private Integer orgID;
	@NotEmpty
	private String[] codeItem;

	public String[] getCodeItem() {
		return codeItem;
	}
	public void setCodeItem(String[] codeItem) {
		this.codeItem = codeItem;
	}
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Integer getOrgID() {
		return orgID;
	}

	public void setOrgID(Integer orgID) {
		this.orgID = orgID;
	}

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
