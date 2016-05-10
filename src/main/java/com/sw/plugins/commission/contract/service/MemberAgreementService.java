package com.sw.plugins.commission.contract.service;


import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.commission.contract.entity.Contract;
import com.sw.plugins.commission.contract.entity.MemberAgreement;
import com.sw.plugins.customer.member.entity.Member;
import com.sw.plugins.customer.organization.entity.MemberOrganization;

/**
 * Service实现类 -
 */

public class MemberAgreementService extends CommonService<MemberAgreement> {

	@Override
	public void delete(MemberAgreement entity) throws Exception {
		// TODO Auto-generated method stub
		super.getRelationDao().delete("memberAgreement.delete", entity);
	}

	@Override
	public void deleteByArr(MemberAgreement entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object download(MemberAgreement entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getGrid(MemberAgreement entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MemberAgreement> getList(MemberAgreement entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MemberAgreement getOne(MemberAgreement entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MemberAgreement> getPaginatedList(MemberAgreement entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getRecordCount(MemberAgreement entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(MemberAgreement entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(MemberAgreement entity) throws Exception {
		// TODO Auto-generated method stub
		super.getRelationDao().update("memberAgreement.update", entity);
	}

	@Override
	public String upload(MemberAgreement entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}
	

	
	
}