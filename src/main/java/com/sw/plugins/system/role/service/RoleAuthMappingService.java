package com.sw.plugins.system.role.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.system.role.entity.RoleAuthMapping;

public class RoleAuthMappingService extends CommonService<RoleAuthMapping> {

	@SuppressWarnings("unchecked")
	public List<RoleAuthMapping> getRoleAuthMappingList(RoleAuthMapping entity) throws Exception {
		return (List<RoleAuthMapping>) super.getRelationDao().selectList("roleAuthMapping.select_role_auth_mapper", entity);
	}

	@Override
	public void init(InitData initData) throws Exception {

	}

	@Override
	public Map<String, Object> getGrid(RoleAuthMapping entity) throws Exception {
		return null;
	}

	@Override
	public String upload(RoleAuthMapping entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(RoleAuthMapping entity, HttpServletRequest request) throws Exception {
		return null;
	}

	@Override
	public void save(RoleAuthMapping entity) throws Exception {
		super.getRelationDao().insert("roleAuthMapping.insert", entity);
	}

	@Override
	public void update(RoleAuthMapping entity) throws Exception {
		super.getRelationDao().update("roleAuthMapping.update", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RoleAuthMapping> getList(RoleAuthMapping entity) throws Exception {
		return (List<RoleAuthMapping>) super.getRelationDao().selectList("roleAuthMapping.select", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RoleAuthMapping> getPaginatedList(RoleAuthMapping entity) throws Exception {
		return (List<RoleAuthMapping>) super.getRelationDao().selectList("roleAuthMapping.select", entity);
	}

	@Override
	public void deleteByArr(RoleAuthMapping entity) throws Exception {
		if (entity != null && entity.getIds() != null) {
			for (String id : entity.getIds()) {
				entity.setId(Long.valueOf(id));
				delete(entity);
			}
		}
	}

	@Override
	public RoleAuthMapping getOne(RoleAuthMapping entity) throws Exception {
		return (RoleAuthMapping) super.getRelationDao().selectOne("roleAuthMapping.select", entity);
	}

	@Override
	public Long getRecordCount(RoleAuthMapping entity) throws Exception {
		return (Long) super.getRelationDao().selectOne("roleAuthMapping.count", entity);
	}

	@Override
	public void delete(RoleAuthMapping entity) throws Exception {
		super.getRelationDao().delete("roleAuthMapping.delete", entity);

	}
}
