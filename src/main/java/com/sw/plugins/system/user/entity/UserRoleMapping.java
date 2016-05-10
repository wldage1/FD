package com.sw.plugins.system.user.entity;



import com.sw.core.data.entity.RelationEntity;

public class UserRoleMapping extends RelationEntity{
	
	private static final long serialVersionUID = 5083680498931767003L;
	private Long roleId;
    private Long userId;
    
    public UserRoleMapping(){}
    
    public UserRoleMapping(Long roleId,Long userId){
    	this.roleId = roleId;
    	this.userId = userId;
    }

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

    
}