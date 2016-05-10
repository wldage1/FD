package com.sw.plugins.incentivefee.incentive.entity;

import com.sw.core.data.entity.RelationEntity;

//活跃度系数，阶段参数配置表
public class StageParameter extends RelationEntity{

	private static final long serialVersionUID = 1L;
	
	private String code;
	
	private String value;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
