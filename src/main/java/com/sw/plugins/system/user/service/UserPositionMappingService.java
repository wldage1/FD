package com.sw.plugins.system.user.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sw.core.service.CommonService;
import com.sw.plugins.system.user.entity.UserPositionMapping;

public class UserPositionMappingService extends CommonService<UserPositionMapping> {

	@SuppressWarnings("unchecked")
	public List<UserPositionMapping> getUserPositionMappingList(UserPositionMapping entity) throws Exception {
		return (List<UserPositionMapping>) super.getRelationDao().selectList("userPositionMapping.select_user_position_mapping", entity);
	}

	@Override
	public void init(com.sw.core.initialize.InitData initData) throws Exception {

	}

	@Override
	public Map<String, Object> getGrid(UserPositionMapping entity) throws Exception {
		return null;
	}

	@Override
	public String upload(UserPositionMapping entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(UserPositionMapping entity, HttpServletRequest request) throws Exception {
		return null;
	}

	@Override
	public void save(UserPositionMapping entity) throws Exception {
		super.getRelationDao().insert("userPositionMapping.insert", entity);
	}

	@Override
	public void update(UserPositionMapping entity) throws Exception {
		super.getRelationDao().update("userPositionMapping.update", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserPositionMapping> getList(UserPositionMapping entity) throws Exception {
		return (List<UserPositionMapping>) super.getRelationDao().selectList("userPositionMapping.select", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserPositionMapping> getPaginatedList(UserPositionMapping entity) throws Exception {
		return (List<UserPositionMapping>) super.getRelationDao().selectList("userPositionMapping.select", entity);
	}

	@Override
	public void delete(UserPositionMapping entity) throws Exception {
		super.getRelationDao().delete("userPositionMapping.delete", entity);
	}

	@Override
	public void deleteByArr(UserPositionMapping entity) throws Exception {
		if (entity != null && entity.getIds() != null) {
			for (String id : entity.getIds()) {
				entity.setId(Long.valueOf(id));
				delete(entity);
			}
		}
	}

	@Override
	public UserPositionMapping getOne(UserPositionMapping entity) throws Exception {
		return (UserPositionMapping) super.getRelationDao().selectOne("userPositionMapping.select", entity);
	}

	@Override
	public Long getRecordCount(UserPositionMapping entity) throws Exception {
		return (Long) super.getRelationDao().selectOne("userPositionMapping.count", entity);
	}

}
