package com.sw.plugins.system.user.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import com.sw.core.common.Constant;
import com.sw.core.data.entity.Authorization;
import com.sw.core.exception.DetailException;
import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.system.authorization.service.AuthorizationService;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.plugins.system.organization.service.OrganizationService;
import com.sw.plugins.system.role.entity.Role;
import com.sw.plugins.system.role.service.RoleService;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.entity.UserOrgMapping;
import com.sw.plugins.system.user.entity.UserPositionMapping;
import com.sw.plugins.system.user.entity.UserRoleMapping;
import com.sw.util.Encrypt;

public class UserService extends CommonService<User> {

	private static Logger logger = Logger.getLogger(UserService.class);
	@Resource
	private UserRoleMappingService userRoleMappingService;
	@Resource
	private UserOrgMappingService userOrgMappingService;
	@Resource
	private UserPositionMappingService userPositionMappingService;
	@Resource
	private UserLoginService userLoginService;
	@Resource
	private RoleService roleService;
	@Resource
	private OrganizationService organizationService;
	@Resource
	private AuthorizationService authorizationService;

	@Override
	public void init(InitData initData) throws Exception {
		try {
			logger.debug("user info initializing");
			Map<String, Object> userMap = initData.getUser();
			String password = userMap.get("password") == null ? "" : userMap.get("password").toString();
			String account = userMap.get("account") == null ? "" : userMap.get("account").toString();
			User user = new User();
			user.setType(0);
			user.setPassword(Encrypt.getMD5(password));
			user.setUsername(account);
			user.setName(account);
			user.setUsername(account);
			user.setAccount(account);
			user.setStyle(Constant.STYLE);
			User obj = (User) getRelationDao().selectOne("user.select_sys_admin", user);
			if (obj == null) {
				getRelationDao().insert("user.insert", user);
			} else {
				user.setId(obj.getId());
				getRelationDao().update("user.update", user);
			}
			logger.debug("user info initialize finished");
		} catch (Exception e) {
			String debug = DetailException.expDetail(e, this.getClass());
			logger.debug("user info initialize fail");
			logger.debug(debug);
		}
	}

	/**
	 * 检查用户信息是否唯一
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Long checkUserDuplication(User user) throws Exception {
		return (Long) super.getRelationDao().getCount("user.count_for_user_duplication", user);
	}

	/**
	 * 根据用户岗位查询用户
	 * 
	 * @param position
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<User> getUserByPositionId(User user) throws Exception {
		return (List<User>) this.getRelationDao().selectList("user.select_user_by_positionid", user);
	}

	/**
	 * 根据用户岗位分页查询用户
	 * 
	 * @param position
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<User> getUserByPositionIdPaginated(User user) throws Exception {
		return (List<User>) this.getRelationDao().selectList("user.select_user_by_positionid_paginated", user);
	}

	/**
	 * 获取用户列表集合
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getGrid(User user) throws Exception {
		Long count = 0L;
		List<User> userList;

		// 获取当前登录用户信息
		User currUser = userLoginService.getCurrLoginUser();
		if (currUser == null) {
			return null;
		}

		// 系统级管理员加载所有用户;
		if (currUser.getType() == 0) {
			userList = (List<User>) super.getRelationDao().selectList("user.select_user_of_org_paginated", user);
			count = (Long) super.getRelationDao().getCount("user.count_user_of_org", user);
		}

		// 普通使用者仅所属机构内的用户
		else {
			Long orgId = user.getOrgId();
			if (orgId == null) {
				orgId = currUser.getOrgId();
			}
			user.setOrgId(orgId);
			userList = (List<User>) super.getRelationDao().selectList("user.select_user_of_org_paginated", user);
			count = (Long) super.getRelationDao().getCount("user.count_user_of_org", user);
		}
		// 记录数
		Long record = count;
		// 页数
		int pageCount = (int) Math.ceil(record / (double) user.getRows());
		Map<String, Object> map = new Hashtable<String, Object>();
		map.put("rows", userList);
		map.put("page", user.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}

	/**
	 * 保存或更新用户信息
	 */
	public void saveOrUpdate(User user) throws Exception {

		if (user != null && user.getNewPassword() != null) {
			user.setPassword(Encrypt.getMD5(user.getNewPassword()));
		}

		// 保存或更新用户信息
		if (user != null && user.getId() != null) {
			update(user);
		} else {
			save(user);
			user.setId(user.getGeneratedKey());
		}

		if (user.getId() != null) {
			this.saveBind(user);
			this.saveUserOrganization(user);
		}

	}

	/**
	 * 保存用户与机构的关系
	 * 
	 * @param userID
	 * @param orgs
	 * @author zhaofeng
	 */
	public void saveUserOrganization(User user) throws Exception {
		String bindOrg = user.getOrgstr();

		// 删除原有关系
		UserOrgMapping orgMapping = new UserOrgMapping();
		orgMapping.setUserId((Long) user.getId());
		userOrgMappingService.delete(orgMapping);

		if (user.getType() != 0 && bindOrg != null && bindOrg.length() > 0) {
			String[] orgsArray = user.getOrgstr().split(",");
			for (String s : orgsArray) {
				UserOrgMapping userOrgMapping = new UserOrgMapping();
				userOrgMapping.setUserId((Long) user.getId());
				userOrgMapping.setOrgId(Long.valueOf(s));
				// 保存新关系
				userOrgMappingService.save(userOrgMapping);
			}
		}
	}

	/**
	 * 保存用户与角色 的关系
	 * 
	 * @param role
	 * @throws Exception
	 */
	public void saveBind(User user) throws Exception {

		String bind = user.getBind();

		UserRoleMapping userRoleMapping = new UserRoleMapping();
		userRoleMapping.setUserId((Long) user.getId());
		userRoleMappingService.delete(userRoleMapping);

		if (user.getType() != 0 && bind != null && bind.length() > 0) {
			String bindArr[] = bind.split(",");
			for (String tempBind : bindArr) {
				userRoleMapping = new UserRoleMapping();
				userRoleMapping.setUserId((Long) user.getId());
				userRoleMapping.setRoleId(Long.valueOf(tempBind));
				userRoleMappingService.save(userRoleMapping);
			}
		}
	}

	/**
	 * 岗位信息直接存在用户表中，可以直接调用saveUser()方法 保存用户与岗位 的关系
	 * 
	 * @param user
	 * @throws Exception
	 */
	@Deprecated
	public void saveUserPosition(User user) throws Exception {

		String spotn = user.getPosition();

		if (spotn != null && spotn.length() > 0) {
			UserPositionMapping userPositionMapping = new UserPositionMapping();
			userPositionMapping.setUserId((Long) user.getId());
			userPositionMappingService.delete(userPositionMapping);

			String spotnArr[] = spotn.split(",");
			for (String tempPotnId : spotnArr) {
				userPositionMapping = new UserPositionMapping();
				userPositionMapping.setUserId((Long) user.getId());
				userPositionMapping.setPositionId(Long.valueOf(tempPotnId));
				userPositionMappingService.save(userPositionMapping);
			}
		}
	}

	/**
	 * 获取用户角色树形JSON
	 * 
	 * @param users
	 * @return
	 * @throws Exception
	 */
	public String getUserRolesTreeJson(User user) throws Exception {
		List<Map<String, Object>> list = this.getUserRoleTreeList(user);
		if (list == null) {
			list = new ArrayList<Map<String, Object>>();
		}
		String roleJson = JSONArray.fromObject(list).toString();
		list = null;
		return roleJson;
	}

	/**
	 * 获取用户数据权限内的角色树形LIST
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getUserRoleTreeList(User user) throws Exception {

		// 是否是居间公司
		int isCommission = user.getIsCommission();

		List<Role> userRoleList = new ArrayList<Role>();
		// 获取当前登录用户机构权限内的角色
		userRoleList = this.getLoginUserOwnsRoleList();
		if (userRoleList == null) {
			return null;
		}

		List<Role> roleList = new ArrayList<Role>();

		// 过滤机构条件下的角色
		if (user.getOrgId() != null) {
			Organization org = new Organization();
			org.setIsCommission(isCommission);
			org.setId(user.getOrgId());
			Organization organization = (Organization) organizationService.getSecendLevelOrg(org);
			if (organization != null) {
				Long oId = organization.getId();
				for (Role role : userRoleList) {
					if (oId.equals(role.getOrgId())) {
						roleList.add(role);
					}
				}
			} else {
				roleList = userRoleList;
			}
		} else {
			roleList = userRoleList;
		}
		userRoleList = null;

		if (roleList.size() == 0) {
			return null;
		}

		Map<Long, Byte> roleMappingMap = new HashMap<Long, Byte>();
		if (user.getId() != null) {
			List<UserRoleMapping> roleMappingList = null;
			// 查询当前对象用户具有的角色
			UserRoleMapping roleMapping = new UserRoleMapping();
			roleMapping.setUserId((Long) user.getId());
			roleMappingList = userRoleMappingService.getUserRoleMappingList(roleMapping);
			if (roleMappingList != null) {
				for (UserRoleMapping rm : roleMappingList) {
					roleMappingMap.put(rm.getRoleId(), null);
				}
			}
			roleMappingList = null;
		}

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Role role : roleList) {
			Map<String, Object> maprole = new Hashtable<String, Object>();
			maprole.put("id", role.getId());
			maprole.put("pId", 0);
			maprole.put("name", role.getName());
			// add by lizf 是否为居间公司(消息模板配置功能中使用) begin
			maprole.put("isCommission", role.getIsCommission());
			// 区分同名角色，加上机构或居间公司名称
			maprole.put("orgName", role.getOrgName() == null ? "" : role.getOrgName());
			// end
			if (roleMappingMap.containsKey(role.getId())) {
				maprole.put("checked", true);
			}
			list.add(maprole);
		}
		roleList = null;
		return list;
	}

	/**
	 * 获取登录用户数据权限内的角色LIST
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<Role> getLoginUserOwnsRoleList() throws Exception {
		User currUser = userLoginService.getCurrLoginUser();
		List<Role> roleList = null;
		roleList = this.getUserOwnsRoleList(currUser);
		return roleList;
	}

	/**
	 * 获取用户数据权限内的角色LIST
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<Role> getUserOwnsRoleList(User user) throws Exception {
		// 普通使用者加载二级机构数据权限内的角色
		Role role = new Role();
		int isCommission = user.getIsCommission();
		role.setIsCommission(isCommission);
		if (user.getType() == 1) {
			List<Organization> secendLevelOrgList = this.getUserSecendOrgList(user);
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
		return (List<Role>) roleService.getRoleOfOrgList(role);
	}

	/**
	 * 获取用户具有的功能权限
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<Role> getUserAuthorized(User user) throws Exception {
		Role role;
		List<Role> roles = new ArrayList<Role>();
		List<Authorization> authorizationList = null;

		// 管理员加载所有系统权限
		if (user.getType() == 0) {
			role = new Role();
			Authorization allAuthorization = new Authorization();
			// allAuthorization.setSetAuthorityOrNot("true");
			authorizationList = (List<Authorization>) authorizationService.getList(allAuthorization);
			role.setAuthorizations(authorizationList);
			roles.add(role);
		}

		// 普通使用者 加载已有的功能权限
		if (user.getType() == 1) {
			UserRoleMapping roleMapping = new UserRoleMapping();
			roleMapping.setUserId((Long) user.getId());
			List<UserRoleMapping> userRole = (List<UserRoleMapping>) userRoleMappingService.getUserRoleMappingList(roleMapping);
			for (UserRoleMapping tRoleMapping : userRole) {
				// 查询角色下的功能权限
				role = new Role();
				role.setId(tRoleMapping.getRoleId());
				Role roleAuth = (Role) roleService.getRoleAuth(role);
				if (roleAuth != null) {
					authorizationList = roleAuth.getAuthorizations();
					role.setAuthorizations(authorizationList);
					roles.add(role);
				}
			}
		}
		return roles;
	}

	/**
	 * 获取用户具有的机构数据权限
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<Organization> getUserOwnsOrgList(User user) throws Exception {
		Organization organization;
		List<Organization> organizationList = null;

		// 系统级超强管理员加载系统所有机构
		if (user.getType() == 0) {
			organization = new Organization();
			return (List<Organization>) organizationService.getList(organization);
		}

		// 普通使用者加载已授权机构
		else {
			organization = new Organization();
			organization.setId(user.getId());
			organizationList = organizationService.getOrgWithUserList(organization);
		}
		return organizationList;
	}

	/**
	 * 获取登录用户所属机构及下级机构
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Organization> getLoginUserOrgAndSubOrgList(User user) throws Exception {
		User loginUser = userLoginService.getCurrLoginUser();
		User u = new User();
		u.setOrgId(loginUser.getOrgId());
		u.setIsCommission(user.getIsCommission());
		u.setSelfOrg(loginUser.getSelfOrg());
		return this.getUserOrgAndSubOrgList(u);
	}

	/**
	 * 获取用户所属机构及下级机构
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<Organization> getUserOrgAndSubOrgList(User user) throws Exception {
		// 是否是居间公司
		int isCommission = user.getIsCommission();

		Organization organization = new Organization();
		List<Organization> organizationList = null;

		// 加载所属机构及下级机构
		if (user.getOrgId() != null) {
			Organization uorg = user.getSelfOrg();
			if (uorg != null) {
				Integer isCo = uorg.getIsCommission();
				// 判断用户是否是居间公司用户
				if (isCo.equals(0) && isCommission == 1) {
					organization.setIsCommission(isCommission);
					return organizationService.getListWithoutTopOrg(organization);
				}
			}

			Organization org = new Organization();
			org.setIsCommission(isCommission);
			org.setId(user.getOrgId());
			org = (Organization) organizationService.getOneWithCoCondition(org);
			if (org != null) {
				organization.setIsCommission(isCommission);
				organization.setTreePath(org.getTreePath());
				organizationList = organizationService.getList(organization);
			}
		} else {
			organization.setIsCommission(isCommission);
			organizationList = organizationService.getList(organization);
		}
		return organizationList;
	}

	/**
	 * 获取用户所在机构及下级机构树形LIST
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getUserOrgAndSubOrgTreeList(User user) throws Exception {

		List<Organization> organizationList = null;

		// 如果没有指定某个机构结点则加载登录用户所在的机构及下级机构数据
		if (user.getOrgId() == null) {
			organizationList = this.getLoginUserOrgAndSubOrgList(user);
		} else {
			organizationList = this.getUserOrgAndSubOrgList(user);
		}

		// 合成树形数据
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Organization organization : organizationList) {
			Map<String, Object> map = new Hashtable<String, Object>();
			// 树形参数
			map.put("id", organization.getId());
			map.put("pId", organization.getParentId());
			map.put("name", organization.getName());
			map.put("open", organization.getIsChildNode() == 0 ? true : false);
			list.add(map);
		}
		// 清空LIST
		organizationList = null;
		return list;
	}

	/**
	 * 获取用户所在机构及下级机构树形JSON
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public String getUserOrgAndSubOrgTreeJson(User user) throws Exception {
		List<Map<String, Object>> list = this.getUserOrgAndSubOrgTreeList(user);
		if (list == null) {
			list = new ArrayList<Map<String, Object>>();
		}
		String orgJson = JSONArray.fromObject(list).toString();
		list = null;
		return orgJson;
	}

	/**
	 * 获取用户二级机构树形JSON
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getUserSecendOrgTreeJson(User user) throws Exception {
		List<Map<String, Object>> list = this.getUserSecendOrgTreeList(user);
		if (list == null) {
			list = new ArrayList<Map<String, Object>>();
		}
		String orgJson = JSONArray.fromObject(list).toString();
		list = null;
		return orgJson;
	}

	/**
	 * 获取用户二级机构树形LIST
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getUserSecendOrgTreeList(User user) throws Exception {
		List<Organization> orgList = this.getUserSecendOrgList(user);
		// 合成树形数据
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (orgList != null) {
			for (Organization organization : orgList) {
				Map<String, Object> map = new Hashtable<String, Object>();
				// 树形参数
				map.put("id", organization.getId());
				map.put("pId", organization.getParentId());
				map.put("name", organization.getName());
				map.put("open", organization.getIsChildNode() == 0 ? true : false);
				list.add(map);
			}
			// 清空LIST
			orgList = null;
		}
		return list;
	}

	/**
	 * 获取登录用户二级机构
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<Organization> getLoginUserSecendOrgList(User user) throws Exception {
		User loginUser = userLoginService.getCurrLoginUser();
		loginUser.setIsCommission(user.getIsCommission());
		return this.getUserSecendOrgList(loginUser);

	}

	/**
	 * 获取用户二级机构
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<Organization> getUserSecendOrgList(User user) throws Exception {
		// 是否是居间公司
		int isCommission = user.getIsCommission();

		Organization organization;
		List<Organization> organizationList = null;
		// 管理员加载所有二级机构
		if (user.getType() == 0) {
			organization = new Organization();
			organization.setIsCommission(isCommission);
			organization.setTreeLevel(2);
			organizationList = organizationService.getList(organization);
		}
		// 普通用户加载所属机构下的二级机构
		else {
			Organization uorg = user.getSelfOrg();
			if (uorg != null) {
				Integer isCo = uorg.getIsCommission();
				// 判断用户是否是居间公司用户
				if (isCo.equals(0) && isCommission == 1) {
					organization = new Organization();
					organization.setIsCommission(isCommission);
					organization.setTreeLevel(2);
					organizationList = organizationService.getList(organization);
					return organizationList;
				}
			}

			organization = new Organization();
			organization.setId(user.getOrgId());
			organization = organizationService.getOne(organization);

			Integer level = organization.getTreeLevel();

			// 加载本身
			if (level == 2) {
				organizationList = new ArrayList<Organization>();
				organizationList.add(organization);
				return organizationList;
			}

			if (level == 1) {
				organization = new Organization();
				organization.setIsCommission(isCommission);
				organization.setTreeLevel(2);
				organizationList = organizationService.getList(organization);
				return organizationList;
			}

			// 加载机构对应的二级机构
			String treePath = organization.getTreePath();
			String[] ids = treePath.substring(1, treePath.length() - 1).split(",");

			HashSet<String> hs = new HashSet<String>(level);
			for (String id : ids) {
				hs.add(id);
			}
			if (hs.size() > 0) {
				organization.setIds(hs.toArray(new String[hs.size()]));
				organizationList = organizationService.get2ndOrgByIds(organization);
			}
		}
		return organizationList;

	}

	@SuppressWarnings("unchecked")
	// 根据机构ID查询用户名，机构名
	public List<User> getUserOrgByOrgId(User entity) throws Exception {
		return (List<User>) super.getRelationDao().selectList("user.select_userName_orgName", entity);
	}

	@SuppressWarnings("unchecked")
	// 根据二级机构角色获取数据授权机构的用户
	public List<User> getUserList(User entity) throws Exception {
		return (List<User>) super.getRelationDao().selectList("user.select_userName", entity);
	}

	@Override
	public String upload(User entity, HttpServletRequest request) throws Exception {
		return null;
	}

	@Override
	public Object download(User entity, HttpServletRequest request) throws Exception {
		return null;
	}

	@Override
	public void save(User entity) throws Exception {
		if (entity != null && entity.getNewPassword() != null) {
			entity.setPassword(Encrypt.getMD5(entity.getNewPassword()));
		}
		// 保存或更新用户信息
		if (entity != null && entity.getId() != null) {
			super.getRelationDao().insert("user.update", entity);
		} else {
			super.getRelationDao().insert("user.insert", entity);
			entity.setId(entity.getGeneratedKey());
		}
	}

	@Override
	public void update(User entity) throws Exception {
		super.getRelationDao().update("user.update", entity);
	}

	/**
	 * 设置个人业务种类id，到数据库
	 * 
	 * @param entity
	 * @throws Exception
	 */
	public void updateBussinessType(User entity) throws Exception {
		super.getRelationDao().update("user.update_business_type_by_id", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getList(User entity) throws Exception {
		return (List<User>) super.getRelationDao().selectList("user.select", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getPaginatedList(User entity) throws Exception {
		return (List<User>) super.getRelationDao().selectList("user.select", entity);
	}

	@Override
	public void delete(User entity) throws Exception {
		// 清空角色权限
		UserRoleMapping rop = new UserRoleMapping();
		rop.setUserId(entity.getId());
		userRoleMappingService.delete(rop);
		// 清空数据权限
		UserOrgMapping oop = new UserOrgMapping();
		oop.setUserId(entity.getId());
		userOrgMappingService.delete(oop);
		// 清空岗位
		/*
		 * UserPositionMapping pom = new UserPositionMapping();
		 * pom.setUserId(entity.getId());
		 * userPositionMappingService.delete(pom);
		 */
		// 删除用户
		super.getRelationDao().delete("user.delete", entity);

	}

	/**
	 * 当删除MongoDB文件时同时也删除其在数据库里面的Head字段数据
	 * 
	 * @param entity
	 * @throws Exception
	 */
	public void updateHeadByid(User entity) throws Exception {
		if (entity != null && entity.getId() != null) {
			super.getRelationDao().delete("user.update_head_by_id", entity);
		}
	}

	@Override
	public void deleteByArr(User entity) throws Exception {
		if (entity != null && entity.getIds() != null) {
			for (String id : entity.getIds()) {
				entity.setId(Long.valueOf(id));
				delete(entity);
			}
		}
	}

	@Override
	public User getOne(User entity) throws Exception {
		return (User) super.getRelationDao().selectOne("user.select", entity);
	}

	@Override
	public Long getRecordCount(User entity) throws Exception {
		return (Long) super.getRelationDao().selectOne("user.count", entity);
	}

	/**
	 * 查询新增条件获取分页数据
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<User> getPaginatedListByUser(User entity) throws Exception {
		return (List<User>) super.getRelationDao().selectList("user.select_user_by_org_paginated", entity);
	}

	/**
	 * 查询新增条件获取条数
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public Long getCountbyUser(User entity) throws Exception {
		return super.getRelationDao().getCount("user.count_user_by_org", entity);
	}

	/**
	 * 查询新增条件获取机构权限内分页数据
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<User> getPaginatedListByOrgSource(User entity) throws Exception {
		return (List<User>) super.getRelationDao().selectList("user.select_user_by_orgsource_paginated", entity);
	}

	/**
	 * 查询新增条件获取机构权限内条数
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public Long getCountByOrgSource(User entity) throws Exception {
		return super.getRelationDao().getCount("user.count_user_by_orgsource", entity);
	}

	/**
	 * 根据机构ID和岗位查询用户
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<User> getUserListByOrgPosition(User entity) throws Exception {
		return (List<User>) super.getRelationDao().selectList("user.select_user_by_orgid_positionid", entity);
	}

	/**
	 * 根据机构ID和角色查询用户
	 * 
	 * @param entity
	 *            An internal error has occurred. java.lang.NullPointerException
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<User> getUserListByOrgRole(User entity) throws Exception {
		return (List<User>) super.getRelationDao().selectList("user.select_user_by_orgid_roleid", entity);
	}

	/**
	 * 查询所有用户
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 * @author sean
	 * @version 1.0 </pre> Created on :2013-10-29 下午5:23:42 LastModified:
	 *          History: </pre>
	 */
	@SuppressWarnings("unchecked")
	public List<User> getAllUsers(User entity) throws Exception {
		return (List<User>) super.getRelationDao().selectList("user.select_all_users", entity);
	}
}