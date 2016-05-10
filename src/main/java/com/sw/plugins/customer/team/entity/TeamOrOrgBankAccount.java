package com.sw.plugins.customer.team.entity;

import com.sw.core.data.entity.RelationEntity;

public class TeamOrOrgBankAccount extends RelationEntity{
	private static final long serialVersionUID = 1L;

	public TeamOrOrgBankAccount(){
		
	}
	
	private Long teamID;
	
	private String bankName;
	
	private String userName;
	
	private String account;
	
	private Short isDefault;
	
	private String teamName;
	
	private Double totalPay;
	
	public Double getTotalPay() {
		return totalPay;
	}

	public void setTotalPay(Double totalPay) {
		this.totalPay = totalPay;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public Long getTeamID() {
		return teamID;
	}

	public void setTeamID(Long teamID) {
		this.teamID = teamID;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Short getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Short isDefault) {
		this.isDefault = isDefault;
	}

}
