package com.sw.plugins.system.user.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.sw.core.common.Constant;
import com.sw.core.data.entity.Authorization;
import com.sw.core.data.entity.BaseUser;
import com.sw.core.exception.DetailException;
import com.sw.core.service.CommonUserLoginService;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.plugins.system.organization.service.OrganizationService;
import com.sw.plugins.system.role.entity.Role;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.sqlserver.SqlserverUserLoginService;

public class UserLoginService extends CommonUserLoginService {

	@Resource
	private UserService userService;
	@Resource
	private OrganizationService organizationService;

	/**
	 * 用户登录验证
	 */
	@Override
	public BaseUser userLogin(String userName) {
		try {
			User user = new User();
			user.setType(null);
			user.setAccount(userName);
			// 验证用户是否存在
			user = (User) userService.getOne(user);
			if (user != null) {

				user.setUsername(userName);
				// 加载用户具有的功能权限
				user.setRoles(userService.getUserAuthorized(user));

				// 加载用户所属机构信息
				if (user.getOrgId() != null) {
					Organization org = new Organization();
					org.setId(user.getOrgId());
					Organization organization = organizationService.getOne(org);
					user.setSelfOrg(organization);
				}else{
					Organization org = new Organization();
					org.setParentId(0L);
					Organization organization = organizationService.getOne(org);
					user.setSelfOrg(organization);
				}

				// 加载用户二级机构
				user.setSecendLevelOrgs(userService.getUserSecendOrgList(user));

				// 设置权限数组
				Collection<GrantedAuthority> grantedAuthority = new ArrayList<GrantedAuthority>();
				Map<String, Authorization> userAuthorization = new HashMap<String, Authorization>();
				// 获取用户具有的权限
				List<Role> roles = user.getRoles();
				for (Role role : roles) {
					List<Authorization> authorizationList = role.getAuthorizations();
					for (Authorization authorization : authorizationList) {
						grantedAuthority.add(new GrantedAuthorityImpl(authorization.getCode()));
						userAuthorization.put(authorization.getIndexCode(), authorization);
					}
				}
				// 系统默认权限 样式表 图片 js 通用才做
				grantedAuthority.add(new GrantedAuthorityImpl(Constant.PERMIT_All));
				user.setAuthorities(grantedAuthority);
				grantedAuthority = null;
				// 设置用户权限集合
				user.setUserAuthorization(userAuthorization);
				userAuthorization = null;
				return user;

			} else {
				throw new UsernameNotFoundException("user is not exist");
			}

		} catch (Exception e) {
			DetailException.expDetail(e, SqlserverUserLoginService.class);
		}
		return new User();
	}

	/**
	 * 获取当前登录的用户信息
	 * 
	 * @return
	 */
	public User getCurrLoginUser() {
		User user = null;
		try {
			Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (obj instanceof User) {
				user = (User) obj;
			}
		} catch (Exception e) {
			return null;
		}
		return user;
	}
}