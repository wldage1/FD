package com.sw.core.service;

import javax.annotation.Resource;

import com.sw.core.data.entity.BaseUser;
import com.sw.core.security.SecurityUserDetails;

public abstract class CommonUserLoginService {
	
	@Resource
	private SecurityUserDetails securityUserDetails;

	public CommonUserLoginService(){
	}

	public abstract BaseUser userLogin(String userName);

	public SecurityUserDetails getSecurityUserDetails() {
		return securityUserDetails;
	}

	@Resource
	public void setSecurityUserDetails(SecurityUserDetails securityUserDetails) {
		this.securityUserDetails = securityUserDetails;
		if (securityUserDetails != null) {
			securityUserDetails.setCommonUserLoginService(this);
		}		
	}
	
	
}
