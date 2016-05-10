package com.sw.plugins.cooperate.provider.entity;


import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.sw.core.data.entity.RelationEntity;

public class ProviderUser extends RelationEntity{

	private static final long serialVersionUID = 1L;

	
	/** 供应商ID*/
	private Long providersID;
	
	/** 用户登录帐号*/
	@Pattern(regexp = "[A-Za-z0-9]{6,16}")
	private String account;
	
	/** 用户名称*/
	@Pattern(regexp="^[a-zA-Z\\u4e00-\\u9fa5]{1,10}")
	private String name;
	
	/** 用户类型*/
	private Long type;
	
	/** 用户权限*/
	@NotEmpty
	private String authorization;
	
	/** 用户密码*/
	@NotEmpty
	private String password;
	/** 用户新密码*/
	@NotEmpty
	private String newPwd;
	
	/** 确认密码*/
	@NotEmpty
	private String confirmPwd;
	
	/** 用户性别*/
	private Long gender;
	
	/** 用户状态*/
	private Integer status;
	
	/** 生日*/
	private String birthday;
	
	/** 头像*/
	private String head;
	
	/** 个性签名*/
	private String individualitySignature;
	
	/** 用户移动电话*/
	private String mobilePhone;
	
	/** 用户办公电话*/
	private String officePhone;
	
	/** 用户邮件*/
	private String email;
	
	/** 用户描述*/
	private String description;
	
	/** 用户风格*/
	private String style;

	/** 用户性别值*/
	private String genderValue;
	/** 设置的消息接收者ID*/
	private Long receiverID;
	/*消息任务ID*/
	private Long taskID;
	public Long getProvidersID() {
		return providersID;
	}

	public void setProvidersID(Long providersID) {
		this.providersID = providersID;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public String getAuthorization() {
		return authorization;
	}

	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

	public String getConfirmPwd() {
		return confirmPwd;
	}

	public void setConfirmPwd(String confirmPwd) {
		this.confirmPwd = confirmPwd;
	}

	public Long getGender() {
		return gender;
	}

	public void setGender(Long gender) {
		this.gender = gender;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getIndividualitySignature() {
		return individualitySignature;
	}

	public void setIndividualitySignature(String individualitySignature) {
		this.individualitySignature = individualitySignature;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getOfficePhone() {
		return officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getGenderValue() {
		return genderValue;
	}

	public void setGenderValue(String genderValue) {
		this.genderValue = genderValue;
	}

	public Long getReceiverID() {
		return receiverID;
	}

	public void setReceiverID(Long receiverID) {
		this.receiverID = receiverID;
	}

	public Long getTaskID() {
		return taskID;
	}

	public void setTaskID(Long taskID) {
		this.taskID = taskID;
	}


	
}