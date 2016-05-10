package com.sw.plugins.system.user.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.sw.core.service.CommonService;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.entity.UserOrgMapping;

public class UserOrgMappingService extends CommonService<UserOrgMapping> {
	@Resource
	private UserLoginService userLoginService;
	
	@SuppressWarnings("unchecked")
	public List<UserOrgMapping> getLoginUserOrgMappingList() throws Exception {
		User loginUser = userLoginService.getCurrLoginUser();
		UserOrgMapping mapping = new UserOrgMapping();
		mapping.setUserId(loginUser.getId());
		return (List<UserOrgMapping>) super.getRelationDao().selectList("userOrgMapping.select_user_org_mapper", mapping);
	}

	@SuppressWarnings("unchecked")
	public List<UserOrgMapping> getUserOrgMappingList(UserOrgMapping entity) throws Exception {
		return (List<UserOrgMapping>) super.getRelationDao().selectList("userOrgMapping.select_user_org_mapper", entity);
	}

	@Override
	public void init(com.sw.core.initialize.InitData initData) throws Exception {

	}

	@Override
	public Map<String, Object> getGrid(UserOrgMapping entity) throws Exception {
		return null;
	}

	@Override
	public String upload(UserOrgMapping entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(UserOrgMapping entity, HttpServletRequest request) throws Exception {
		return null;
	}

	@Override
	public void save(UserOrgMapping entity) throws Exception {
		super.getRelationDao().insert("userOrgMapping.insert", entity);
	}

	@Override
	public void update(UserOrgMapping entity) throws Exception {
		super.getRelationDao().update("userOrgMapping.update", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserOrgMapping> getList(UserOrgMapping entity) throws Exception {
		return (List<UserOrgMapping>) super.getRelationDao().selectList("userOrgMapping.select", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserOrgMapping> getPaginatedList(UserOrgMapping entity) throws Exception {
		return (List<UserOrgMapping>) super.getRelationDao().selectList("userOrgMapping.select", entity);
	}

	@Override
	public void delete(UserOrgMapping entity) throws Exception {
		super.getRelationDao().delete("userOrgMapping.delete", entity);
	}

	@Override
	public void deleteByArr(UserOrgMapping entity) throws Exception {
		if (entity != null && entity.getIds() != null) {
			for (String id : entity.getIds()) {
				entity.setId(Long.valueOf(id));
				delete(entity);
			}
		}
	}

	@Override
	public UserOrgMapping getOne(UserOrgMapping entity) throws Exception {
		return (UserOrgMapping) super.getRelationDao().selectOne("userOrgMapping.select", entity);
	}

	@Override
	public Long getRecordCount(UserOrgMapping entity) throws Exception {
		return (Long) super.getRelationDao().selectOne("userOrgMapping.count", entity);
	}

}
