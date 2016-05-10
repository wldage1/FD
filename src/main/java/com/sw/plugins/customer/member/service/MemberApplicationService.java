package com.sw.plugins.customer.member.service;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.customer.member.entity.MemberApplication;

/**
 * Service实现类 -
 */

public class MemberApplicationService extends CommonService<MemberApplication> {

	@Override
	public void delete(MemberApplication entity) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteByArr(MemberApplication entity) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Object download(MemberApplication entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, Object> getGrid(MemberApplication entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		return map;
	}

	@Override
	public List<MemberApplication> getList(MemberApplication entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MemberApplication getOne(MemberApplication entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MemberApplication> getPaginatedList(MemberApplication entity) throws Exception {
		return (List<MemberApplication>) super.getRelationDao().selectList("MemberApplication.select_paginated", entity);
	}

	@Override
	public Long getRecordCount(MemberApplication entity) throws Exception {
		return null;
	}

	@Override
	public void save(MemberApplication entity) throws Exception {

	}

	// 理财师机构变更申请变更申请更改
	@Override
	public void update(MemberApplication entity) throws Exception {
		super.getRelationDao().update("memberApplication.update", entity);
	}

	@Override
	public String upload(MemberApplication entity, HttpServletRequest request) throws Exception {
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {

	}

	public Map<String, Object> getApQuitMemberGrid(MemberApplication entity) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<MemberApplication> resultList = this.getApQuitMemberPaginatedList(entity);
		Long record = 0l;
		if (resultList != null) {
			record = (long) this.getApQuitMemberCount(entity);
		}
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}

	@SuppressWarnings("unchecked")
	public List<MemberApplication> getApQuitMemberPaginatedList(MemberApplication entity) throws Exception {
		return (List<MemberApplication>) super.getRelationDao().selectList("memberApplication.select_quit_org_member", entity);
	}

	public Long getApQuitMemberCount(MemberApplication ma) throws Exception {
		return (long) super.getRelationDao().getCount("memberApplication.count_quit_org_member", ma);
	}

	public void updateApQuitMemberStatus(MemberApplication ma) throws Exception {
		super.getRelationDao().update("memberApplication.update_member_quit_org", ma);
	}

}