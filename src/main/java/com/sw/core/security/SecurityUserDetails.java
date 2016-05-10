package com.sw.core.security;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.sw.core.data.entity.BaseUser;
import com.sw.core.service.CommonUserLoginService;

public class SecurityUserDetails implements UserDetailsService{
	
	private CommonUserLoginService commonUserLoginService;
	/**
	 *用户登录验证
	 */
	public BaseUser loadUserByUsername(String userName){
		if (commonUserLoginService != null){
			return commonUserLoginService.userLogin(userName);
		}
		else{
			return new BaseUser();
		}
	}
	
	public CommonUserLoginService getCommonUserLoginService() {
		return commonUserLoginService;
	}
	public void setCommonUserLoginService(CommonUserLoginService commonUserLoginService) {
		this.commonUserLoginService = commonUserLoginService;
	}
	
}