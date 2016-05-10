package com.sw.plugins.activity.manage.service;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.activity.manage.entity.Activity;

/**
 * 活动Service
 * 
 * @author Erun
 */
public class ActivityService extends CommonService<Activity>{
	
	/**
	 * 获取活动集合
	 */
	@Override
	public Map<String, Object> getGrid(Activity entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Activity> resultList = getPaginatedList(entity);
		Long record = getRecordCount(entity);
		int pageCount = (int)Math.ceil(record/(double)entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Activity> getPaginatedList(Activity entity) throws Exception {
		return (List<Activity>) getRelationDao().selectList("activity.select_paginated_list", entity);
	}
	
	@Override
	public Long getRecordCount(Activity entity) throws Exception {
		return getRelationDao().getCount("activity.select_paginated_count", entity);
	}
	
	@Override
	public Activity getOne(Activity entity) throws Exception {
		return (Activity) getRelationDao().selectOne("activity.select_one", entity);
	}
	
	@Override
	public void save(Activity entity) throws Exception {
		getRelationDao().insert("activity.insert", entity);
	}
	
	@Override
	public void update(Activity entity) throws Exception {
		getRelationDao().update("activity.update", entity);
	}
	
	@Override
	public void deleteByArr(Activity entity) throws Exception {
		if(entity.getIds() != null){
			for(String id : entity.getIds()){
				entity.setId(Long.valueOf(id));
				delete(entity);
			}
		}
	}
	
	@Override
	public void delete(Activity entity) throws Exception {
		getRelationDao().delete("activity.delete", entity);
	}

	@Override
	public List<Activity> getList(Activity entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String upload(Activity entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(Activity entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
