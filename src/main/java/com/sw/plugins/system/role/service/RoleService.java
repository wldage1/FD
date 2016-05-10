package com.sw.plugins.system.role.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import com.sw.core.data.entity.Authorization;
import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.plugins.system.role.entity.Role;
import com.sw.plugins.system.role.entity.RoleAuthMapping;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.entity.UserRoleMapping;
import com.sw.plugins.system.user.service.UserLoginService;
import com.sw.plugins.system.user.service.UserRoleMappingService;
import com.sw.plugins.system.user.service.UserService;

/**
 * Service实现类 - Service实现类基类
 */

/**
 * 类简要说明
 * 
 * @author Administrator
 * @version 1.0 </pre> Created on :下午02:30:26 LastModified: History: </pre>
 */
public class RoleService extends CommonService<Role> {

	@Resource
	private RoleAuthMappingService roleAuthMappingService;
	@Resource
	private UserRoleMappingService userRoleMappingService;
	@Resource
	private UserService userService;
	@Resource
	private UserLoginService userLoginService;

	/**
	 * 获取角色集合
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getGrid(Role role) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Role> roleList = new ArrayList<Role>();
		Long count = 0L;

		// 获取当前登录用户信息
		User currUser = userLoginService.getCurrLoginUser();
		if (currUser == null) {
			return map;
		}

		// 普通使用者加载二级机构数据权限内的角色
		if (currUser.getType() == 1) {
			List<Organization> secendLevelOrgList = userService.getUserSecendOrgList(currUser);
			StringBuffer bf = new StringBuffer();
			if (secendLevelOrgList != null) {
				for (Organization org : secendLevelOrgList) {
					bf.append(org.getId());
					bf.append(",");
				}
			}
			if (bf.length() > 0) {
				role.setOrgIds(bf.toString().substring(0, bf.length() - 1));
			}
		}
		// 系统级超级管理员加载所有角色
		roleList = (List<Role>) super.getRelationDao().selectList("role.select_role_of_org_paginated", role);
		count = super.getRelationDao().getCount("role.count_role_of_org", role);
		
		// 记录数
		long record = count;
		// 页数
		int pageCount = (int) Math.ceil(record / (double) role.getRows());

		map.put("rows", roleList);
		map.put("page", role.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}

	/**
	 * 获取角色权限JSON
	 * 
	 * @param role
	 * @return
	 * @throws Exception
	 */
	public String getRoleAuthorizationJson(Role role) throws Exception {
		List<Map<String, Object>> list = this.getAuthTreeList(role);
		if (list == null) {
			list = new ArrayList<Map<String, Object>>();
		}
		String json = JSONArray.fromObject(list).toString();
		list = null;
		return json;
	}

	/**
	 * 获取权限树形LIST
	 * 
	 * @param role
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getAuthTreeList(Role role) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Set<String> authSet = new HashSet<String>();
		Integer authNum = 0;

		// 获取当前登录用户信息
		User currUser = userLoginService.getCurrLoginUser();
		if (currUser == null) {
			return null;
		}

		// 当前登录用户具有的角色信息
		List<Role> roleList = userService.getUserAuthorized(currUser);

		if (roleList == null || roleList.size() == 0) {
			return null;
		}

		// 查询当前角色授权的功能权限
		List<RoleAuthMapping> roleMapperList;
		RoleAuthMapping roleAuthMapping = new RoleAuthMapping();
		roleAuthMapping.setRoleId((Long) role.getId());
		roleMapperList = roleAuthMappingService.getRoleAuthMappingList(roleAuthMapping);

		for (Role tempRole : roleList) {
			List<Authorization> authorizationList = tempRole.getAuthorizations();
			if (authorizationList == null) {
				break;
			}
			for (Authorization authorization : authorizationList) {
				authSet.add(authorization.getCode());
				if (authSet.size() > authNum) {
					authNum = authSet.size();
					Map<String, Object> maprole = new Hashtable<String, Object>();
					// 树形参数
					maprole.put("id", authorization.getCode());
					maprole.put("pId", authorization.getParentCode());
					maprole.put("name", authorization.getName());
					if (roleMapperList == null) {
						break;
					}
					// 如果存在登录用户具有的权限信息和已经授权角色的权限信息一致
					// 设置此权限为选中状态
					for (RoleAuthMapping tempAuthMapper : roleMapperList) {
						String code = tempAuthMapper.getAuthCode();
						if (code != null && code.equals(authorization.getCode())) {
							// 设置为选中
							maprole.put("checked", true);
							break;
						}
					}
					list.add(maprole);
				}
			}
		}
		return list;
	}

	/**
	 * 保存角色权限信息
	 * 
	 * @param role
	 * @throws Exception
	 */
	public void saveAuth(Role role) throws Exception {
		String auth = role.getAuth();
		if (auth != null) {
			// 获取当前登录用户信息
			User currUser = userLoginService.getCurrLoginUser();
			if (currUser != null) {

				// 当前登录用户具有的角色信息
				List<Role> roleList = userService.getUserAuthorized(currUser);

				String authArr[] = auth.split(",");

				// 清空当前角色功能权限
				RoleAuthMapping roleAuthMapping = new RoleAuthMapping();
				roleAuthMapping.setRoleId((Long) role.getId());
				roleAuthMappingService.delete(roleAuthMapping);

				for (String tempAuth : authArr) {
					if (roleList != null && roleList.size() > 0) {
						for (Role tempRole : roleList) {
							List<Authorization> currplist = tempRole.getAuthorizations();
							for (Authorization authorization : currplist) {
								if (tempAuth != null && tempAuth.equals(authorization.getCode())) {
									roleAuthMapping.setRoleId((Long) role.getId());
									roleAuthMapping.setAuthCode(authorization.getCode());
									roleAuthMappingService.save(roleAuthMapping);
									break;
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 保存或更改角色信息
	 * 
	 * @param role
	 * @throws Exception
	 */
	public void saveOrUpdate(Role role) throws Exception {
		if (role != null && role.getId() != null) {
			// 更新角色
			update(role);
		} else {
			// 保存角色
			role.setOrgId(role.getOrgId() == null ? 0L : role.getOrgId());
			save(role);
		}
	}

	public Role getRoleAuth(Role entity) throws Exception {
		return (Role) super.getRelationDao().selectOne("role.select_role_auth", entity);
	}

	@SuppressWarnings("unchecked")
	public List<Role> getRoleOfOrgList(Role entity) throws Exception {
		return (List<Role>) super.getRelationDao().selectList("role.select_role_of_org", entity);
	}

	public List<Role> getRoleOfOrgsourceList(Role entity) throws Exception {
		return getRoleOfOrgList(entity);
	}

	@Override
	public void init(InitData initData) throws Exception {

	}

	@Override
	public String upload(Role entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(Role entity, HttpServletRequest request) throws Exception {
		return null;
	}

	@Override
	public void save(Role entity) throws Exception {
		super.getRelationDao().insert("role.insert", entity);
	}

	@Override
	public void update(Role entity) throws Exception {
		super.getRelationDao().update("role.update", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> getList(Role entity) throws Exception {
		return (List<Role>) super.getRelationDao().selectList("role.select", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> getPaginatedList(Role entity) throws Exception {
		return (List<Role>) super.getRelationDao().selectList("role.select", entity);
	}

	@Override
	public void delete(Role entity) throws Exception {
		// 清空权限
		RoleAuthMapping rom = new RoleAuthMapping();
		rom.setRoleId(entity.getId());
		roleAuthMappingService.delete(rom);

		// 清空用户绑定的角色
		UserRoleMapping userRoleMapping = new UserRoleMapping();
		userRoleMapping.setRoleId(entity.getId());
		userRoleMappingService.delete(userRoleMapping);

		super.getRelationDao().delete("role.delete", entity);
	}

	@Override
	public void deleteByArr(Role entity) throws Exception {
		if (entity != null && entity.getIds() != null) {
			for (String id : entity.getIds()) {
				entity.setId(Long.valueOf(id));
				delete(entity);
			}
		}
	}

	@Override
	public Role getOne(Role entity) throws Exception {
		return (Role) super.getRelationDao().selectOne("role.select", entity);
	}

	@Override
	public Long getRecordCount(Role entity) throws Exception {
		return (Long) super.getRelationDao().selectOne("role.count", entity);
	}

}