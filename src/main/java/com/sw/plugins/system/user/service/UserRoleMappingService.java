package com.sw.plugins.system.user.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.system.user.entity.UserRoleMapping;

public class UserRoleMappingService extends CommonService<UserRoleMapping> {


	
	@SuppressWarnings("unchecked")
	public List<UserRoleMapping> getUserRoleMappingList(UserRoleMapping entity) throws Exception {
		return (List<UserRoleMapping>) super.getRelationDao().selectList("userRoleMapping.select_user_role_mapper", entity);
	}

	@Override
	public void init(InitData initData) throws Exception {
		
	}

	@Override
	public Map<String, Object> getGrid(UserRoleMapping entity) throws Exception {
		return null;
	}

	@Override
	public String upload(UserRoleMapping entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(UserRoleMapping entity, HttpServletRequest request) throws Exception {
		return null;
	}
	
	@Override
	public void save(UserRoleMapping entity) throws Exception {
		super.getRelationDao().insert("userRoleMapping.insert", entity);
	}

	@Override
	public void update(UserRoleMapping entity) throws Exception {
		super.getRelationDao().update("userRoleMapping.update", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserRoleMapping> getList(UserRoleMapping entity) throws Exception {
		return (List<UserRoleMapping>) super.getRelationDao().selectList("userRoleMapping.select", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserRoleMapping> getPaginatedList(UserRoleMapping entity) throws Exception {
		return (List<UserRoleMapping>) super.getRelationDao().selectList("userRoleMapping.select", entity);
	}

	@Override
	public void delete(UserRoleMapping entity) throws Exception {
		super.getRelationDao().delete("userRoleMapping.delete", entity);
	}

	@Override
	public void deleteByArr(UserRoleMapping entity) throws Exception {
		if (entity != null && entity.getIds() != null) {
			for (String id : entity.getIds()) {
				entity.setId(Long.valueOf(id));
				delete(entity);
			}
		}
	}

	@Override
	public UserRoleMapping getOne(UserRoleMapping entity) throws Exception {
		return (UserRoleMapping) super.getRelationDao().selectOne("userRoleMapping.select", entity);
	}

	@Override
	public Long getRecordCount(UserRoleMapping entity) throws Exception {
		return (Long) super.getRelationDao().selectOne("userRoleMapping.count", entity);
	}
	
}
