package com.sw.plugins.extension.youku.category.entity;

import javax.validation.constraints.Pattern;

import com.sw.core.data.entity.RelationEntity;

public class Category extends RelationEntity{

	private static final long serialVersionUID = 1L;
	//分类标识
	@Pattern(regexp="^[A-Za-z]+$")
	private String code;
	//分类名称
	@Pattern(regexp="^[\u4e00-\u9fa5]{1,20}")
	private String name;
	//是否关联产品
	private Short isRelate;
	//是否密码控制
	private Short isPwdControl;
	
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
	public Short getIsRelate() {
		return isRelate;
	}
	public Short getIsPwdControl() {
		return isPwdControl;
	}
	public void setIsRelate(Short isRelate) {
		this.isRelate = isRelate;
	}
	public void setIsPwdControl(Short isPwdControl) {
		this.isPwdControl = isPwdControl;
	}
}
