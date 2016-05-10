package com.sw.plugins.commission.paygrantparameter.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.commission.paygrantparameter.entity.PayGrantParameter;

@Service
public class PayGrantParameterService extends CommonService<PayGrantParameter>{

	@Override
	public void save(PayGrantParameter entity) throws Exception {
		super.getRelationDao().insert("paygrantparameter.insert", entity);
	}

	@Override
	public void update(PayGrantParameter entity) throws Exception {
		super.getRelationDao().update("paygrantparameter.update", entity);
	}

	@Override
	public Long getRecordCount(PayGrantParameter entity) throws Exception {
		return (Long)super.getRelationDao().selectOne("paygrantparameter.select_count", entity);
	}
	
	public void saveorupdate(PayGrantParameter entity) throws Exception {
		if(entity.getId()!=null){
			update(entity);
		}else{
			save(entity);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PayGrantParameter> getList(PayGrantParameter entity)
			throws Exception {
		return (List<PayGrantParameter>)super.getRelationDao().selectList("paygrantparameter.select_paginated", entity);
	}

	@Override
	public List<PayGrantParameter> getPaginatedList(PayGrantParameter entity)
			throws Exception {
		return null;
	}

	@Override
	public void delete(PayGrantParameter entity) throws Exception {
		super.getRelationDao().delete("paygrantparameter.delete", entity);
	}

	@Override
	public void deleteByArr(PayGrantParameter entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PayGrantParameter getOne(PayGrantParameter entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getGrid(PayGrantParameter entity)
			throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		List<PayGrantParameter> parameterList = new ArrayList<PayGrantParameter>();
		parameterList = getList(entity);
		map.put("rows", parameterList);
		return map;
	}

	@Override
	public String upload(PayGrantParameter entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(PayGrantParameter entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
