package com.sw.plugins.customer.pointsconfig.service;


import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.customer.pointsconfig.entity.PointsConfig;

/**
 * Service实现类 -
 */

public class PointsConfigService extends CommonService<PointsConfig> {
	
	//积分配置查询
	public Map<String, Object> getGrid(PointsConfig entity) throws Exception {
		List<PointsConfig> resultList = null;
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
	//删除积分配置
	@Override
	public void delete(PointsConfig entity) throws Exception {
		super.getRelationDao().delete("config.delete", entity);		
	}
	//批量删除积分配置
	@Override
	public void deleteByArr(PointsConfig entity) throws Exception {
		if (entity != null && entity.getIds() != null) {
			for (String id : entity.getIds()) {
				entity.setId(Long.valueOf(id));
				delete(entity);
			}
		}
	}
    //积分配置的分页查询
	@SuppressWarnings("unchecked")
	@Override
	public List<PointsConfig> getPaginatedList(PointsConfig entity) throws Exception {
		return (List<PointsConfig>) super.getRelationDao().selectList("config.select_paginated", entity);
	}
	//积分配置的统计
	@Override
	public Long getRecordCount(PointsConfig entity) throws Exception {
		return (Long) super.getRelationDao().selectOne("config.count", entity);
	}
	//积分配置的保存
	@Override
	public void save(PointsConfig entity) throws Exception {
		super.getRelationDao().insert("config.insert", entity);	
	}
    //积分配置更新
	@Override
	public void update(PointsConfig entity) throws Exception {
		super.getRelationDao().update("config.update", entity);	
	}

	/**
	 * 保存或更改积分配置信息
	 * 
	 * @param pointsConfig
	 * @throws Exception
	 */
	public void saveOrUpdate(PointsConfig entity) throws Exception {
		if (entity != null && entity.getId() != null) {
			// 更新积分配置
			update(entity);
		} else {
			// 保存积分配置
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
	public Long checkConfigDuplication(PointsConfig entity) throws Exception {
		return (Long) super.getRelationDao().getCount("config.count_for_config_duplication", entity);
	}
	@Override
	public String upload(PointsConfig entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object download(PointsConfig entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<PointsConfig> getList(PointsConfig entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PointsConfig getOne(PointsConfig entity) throws Exception {
		return (PointsConfig) super.getRelationDao().selectOne("config.select", entity);
	}
}