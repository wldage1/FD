package com.sw.plugins.customer.points.service;


import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.customer.points.entity.Points;

/**
 * Service实现类 -
 */

public class PointsService extends CommonService<Points> {
	
	//积分管理查询
	public Map<String, Object> getGrid(Points entity) throws Exception {
		List<Points> resultList = null;
		Map<String, Object> map = new Hashtable<String, Object>();
		//分页列表
		resultList = getPaginatedList(entity);
		// 记录数
		long record = this.getRecordCount(entity);
		// 页数
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());

		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	//删除积分管理
	@Override
	public void delete(Points entity) throws Exception {
		super.getRelationDao().delete("points.delete", entity);		
	}
	//批量删除积分管理
	@Override
	public void deleteByArr(Points entity) throws Exception {
		if (entity != null && entity.getIds() != null) {
			for (String id : entity.getIds()) {
				entity.setId(Long.valueOf(id));
				delete(entity);
			}
		}
	}
    //积分管理的分页查询
	@SuppressWarnings("unchecked")
	@Override
	public List<Points> getPaginatedList(Points entity) throws Exception {
		return (List<Points>) super.getRelationDao().selectList("points.select_paginated", entity);
	}
	//积分管理的统计
	@Override
	public Long getRecordCount(Points entity) throws Exception {
		return (Long) super.getRelationDao().selectOne("points.count", entity);
	}
	//积分管理的保存
	@Override
	public void save(Points entity) throws Exception {
		super.getRelationDao().insert("points.insert", entity);	
	}
    //积分管理更新
	@Override
	public void update(Points entity) throws Exception {
		super.getRelationDao().update("points.update", entity);	
	}

	/**
	 * 保存或更改积分管理信息
	 * 
	 * @param Points
	 * @throws Exception
	 */
	public void saveOrUpdate(Points entity) throws Exception {
		if (entity != null && entity.getId() != null) {
			// 更新积分管理
			update(entity);
		} else {
			// 保存积分管理
			save(entity);
		}
	}
	/**
	 * 积分代码是否唯一
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Long checkConfigDuplication(Points entity) throws Exception {
		return (Long) super.getRelationDao().getCount("points.count_for_config_duplication", entity);
	}
	@Override
	public String upload(Points entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object download(Points entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<Points> getList(Points entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Points getOne(Points entity) throws Exception {
		return (Points) super.getRelationDao().selectOne("points.select", entity);
	}
}