package com.sw.plugins.incentivefee.incentive.service;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.incentivefee.incentive.entity.RebateParameter;

@Service
public class RebateParameterService extends CommonService<RebateParameter>{
	
	public void saveAll(RebateParameter entity) throws Exception{
		//清空返利表
		List<RebateParameter> rebateList = entity.getStageList();
		this.deleteAll(entity);
		if(rebateList!=null){
			for(RebateParameter rp :rebateList){
				if(rp.getLevelTag()!=null){
					this.save(rp);
				}
			}
		}
	}
	
	@Override
	public void save(RebateParameter entity) throws Exception {
		super.getRelationDao().insert("rebateParameter.insert", entity);
	}
	
	@Override
	public void update(RebateParameter entity) throws Exception {
		super.getRelationDao().update("rebateParameter.update", entity);
	}
	
	public void saveorupdate(RebateParameter entity) throws Exception{
		if(entity.getId() == null){
			this.save(entity);
		}else{
			this.update(entity);
		}
	}
	
	@Override
	public Long getRecordCount(RebateParameter entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RebateParameter> getList(RebateParameter entity)
			throws Exception {
		// TODO Auto-generated method stub
		return (List<RebateParameter>)super.getRelationDao().selectList("rebateParameter.select", entity);
	}

	@Override
	public List<RebateParameter> getPaginatedList(RebateParameter entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(RebateParameter entity) throws Exception {
		// TODO Auto-generated method stub
		super.getRelationDao().delete("rebateParameter.delete", entity);
	}
	
	public void deleteAll(RebateParameter entity) throws Exception{
		super.getRelationDao().delete("rebateParameter.deleteAll", entity);
	}
	
	@Override
	public void deleteByArr(RebateParameter entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RebateParameter getOne(RebateParameter entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getGrid(RebateParameter entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<RebateParameter> resultList = getList(entity);
		map.put("rows", resultList);
		return map;
	}

	@Override
	public String upload(RebateParameter entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(RebateParameter entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
