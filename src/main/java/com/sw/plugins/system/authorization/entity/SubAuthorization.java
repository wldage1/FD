package com.sw.plugins.system.authorization.entity;

import com.sw.core.data.entity.Authorization;

public class SubAuthorization extends Authorization {

	private static final long serialVersionUID = -8667400765027701089L;
	private Long businessTypeId;
	private String tempCode;
	private String tempParentCode;
	private String isConfig;

	public SubAuthorization() {
	}

	public Long getBusinessTypeId() {
		return businessTypeId;
	}

	public void setBusinessTypeId(Long businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

	public String getTempCode() {
		return tempCode;
	}

	public void setTempCode(String tempCode) {
		this.tempCode = tempCode;
	}

	public String getTempParentCode() {
		return tempParentCode;
	}

	public void setTempParentCode(String tempParentCode) {
		this.tempParentCode = tempParentCode;
	}

	public String getIsConfig() {
		return isConfig;
	}

	public void setIsConfig(String isConfig) {
		this.isConfig = isConfig;
	}

}
