package com.sw.plugins.system.role.entity;

import com.sw.core.data.entity.RelationEntity;

public class RoleAuthMapping extends RelationEntity{
	
	private static final long serialVersionUID = -4355429053598922587L;
	private Long roleId ;
    private String authCode;
    
    public RoleAuthMapping(){}
    
    public RoleAuthMapping(Long roleId,String authCode){
    	this.roleId = roleId;
    	this.authCode = authCode;
    }

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	
}