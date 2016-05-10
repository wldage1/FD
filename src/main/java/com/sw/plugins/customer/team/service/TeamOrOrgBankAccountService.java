package com.sw.plugins.customer.team.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.customer.team.entity.TeamOrOrgBankAccount;

@Service
public class TeamOrOrgBankAccountService extends CommonService<TeamOrOrgBankAccount>{

	@Override
	public void save(TeamOrOrgBankAccount entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(TeamOrOrgBankAccount entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long getRecordCount(TeamOrOrgBankAccount entity) throws Exception {
		return super.getRelationDao().getCount("teamBankAccount.count", entity);
	}

	@Override
	public List<TeamOrOrgBankAccount> getList(TeamOrOrgBankAccount entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TeamOrOrgBankAccount> getPaginatedList(TeamOrOrgBankAccount entity)
			throws Exception {
		return (List<TeamOrOrgBankAccount>)super.getRelationDao().selectPaginatedList("teamBankAccount.select_paginated", entity);
	}

	@Override
	public void delete(TeamOrOrgBankAccount entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByArr(TeamOrOrgBankAccount entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TeamOrOrgBankAccount getOne(TeamOrOrgBankAccount entity) throws Exception {
		return (TeamOrOrgBankAccount)super.getRelationDao().selectOne("teamBankAccount.select_one", entity);
	}

	@Override
	//待发放居间费列表数据
	public Map<String, Object> getGrid(TeamOrOrgBankAccount entity) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		List<TeamOrOrgBankAccount> list = getPaginatedList(entity);
		for(int i = 0;i<list.size();i++){
			//给不同居间机构计算居间费总数
			entity.setTeamID(list.get(i).getTeamID());
			list.get(i).setTotalPay((Double)super.getRelationDao().selectOne("teamBankAccount.sum_dff", entity));
		}
		map.put("rows", list);
		return map;	
	}
	
	//居间费发放失败列表数据
	public Map<String, Object> getffsbGrid(TeamOrOrgBankAccount entity) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		List<TeamOrOrgBankAccount> list = getPaginatedList(entity);
		for(int i = 0;i<list.size();i++){
			//给不同居间机构计算居间费总数
			entity.setTeamID(list.get(i).getTeamID());
			list.get(i).setTotalPay((Double)super.getRelationDao().selectOne("teamBankAccount.sum_ffsb", entity));
		}
		map.put("rows", list);
		return map;	
	}
	
	@Override
	public String upload(TeamOrOrgBankAccount entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(TeamOrOrgBankAccount entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
