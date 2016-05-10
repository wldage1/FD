package com.sw.plugins.customer.team.service;



import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.sw.core.exception.DetailException;
import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.customer.member.entity.Member;
import com.sw.plugins.customer.member.entity.MemberApplication;
import com.sw.plugins.customer.member.entity.MemberChangeApplication;
import com.sw.plugins.customer.member.service.MemberApplicationService;
import com.sw.plugins.customer.member.service.MemberService;
import com.sw.plugins.customer.organization.entity.MemberOrganization;
import com.sw.plugins.customer.organization.service.MemberOrganizationService;
import com.sw.plugins.customer.team.entity.Team;
import com.sw.plugins.customer.team.entity.TeamOrOrgApplication;


/**
 * Service实现类 -
 */

public class TeamOrOrgApplicationService extends CommonService<TeamOrOrgApplication> {

	@Override
	public void delete(TeamOrOrgApplication entity) throws Exception {
		// TODO Auto-generated method stub 
	}

	@Override
	public void deleteByArr(TeamOrOrgApplication entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object download(TeamOrOrgApplication entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getGrid(TeamOrOrgApplication entity) throws Exception {
		List<TeamOrOrgApplication> resultList = null;
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
	public Map<String, Object> getGridForTeam(TeamOrOrgApplication entity) throws Exception {
		List<TeamOrOrgApplication> resultList = null;
		Map<String, Object> map = new Hashtable<String, Object>();
		//分页列表
		resultList = getPaginatedListForTeam(entity);
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
	

	@Override
	public List<TeamOrOrgApplication> getList(TeamOrOrgApplication entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TeamOrOrgApplication getOne(TeamOrOrgApplication entity) throws Exception {
		// TODO Auto-generated method stub
		return (TeamOrOrgApplication) super.getRelationDao().selectOne("teamApplication.select_one", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TeamOrOrgApplication> getPaginatedList(TeamOrOrgApplication entity)
			throws Exception {
		return (List<TeamOrOrgApplication>) super.getRelationDao().selectList("teamApplication.select_paginated", entity);
	}
	@SuppressWarnings("unchecked")
	public List<TeamOrOrgApplication> getPaginatedListForTeam(TeamOrOrgApplication entity)
	throws Exception {
	return (List<TeamOrOrgApplication>) super.getRelationDao().selectList("teamApplication.select_paginated_for_team", entity);
	}


	@Override
	public Long getRecordCount(TeamOrOrgApplication entity) throws Exception {
		// TODO Auto-generated method stub
		return (Long) super.getRelationDao().selectOne("teamApplication.count", entity);
	}

	@Override
	public void save(TeamOrOrgApplication entity) throws Exception {
		// TODO Auto-generated method stub
		
	}
	//理财师机构申请更新
	@Override
	public void update(TeamOrOrgApplication entity) throws Exception {
		super.getRelationDao().update("teamApplication.update", entity);	
	}
	public void updateForCancel(TeamOrOrgApplication entity) throws Exception {
		super.getRelationDao().update("teamApplication.updateForCancel", entity);	
	}

	@Override
	public String upload(TeamOrOrgApplication entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	
}