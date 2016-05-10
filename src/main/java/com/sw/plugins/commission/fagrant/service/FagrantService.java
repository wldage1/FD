package com.sw.plugins.commission.fagrant.service;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.commission.adjust.entity.Commission;

public class FagrantService extends CommonService<Commission> {

	@Override
	public void save(Commission entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Commission entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long getRecordCount(Commission entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Commission> getList(Commission entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Commission> getPaginatedList(Commission entity)
			throws Exception {
		return (List<Commission>) super.getRelationDao().selectList("memberPayDetail.select_paginated", entity);
	}

	@Override
	public void delete(Commission entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByArr(Commission entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Commission getOne(Commission entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getGrid(Commission entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Commission> resultList = getPaginatedList(entity);
		map.put("rows", resultList);
		return map;
	}

	@Override
	public String upload(Commission entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(Commission entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
