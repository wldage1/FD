package com.sw.plugins.commission.contract.service;


import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.commission.contract.entity.Contract;
import com.sw.plugins.commission.contract.entity.ContractAttachment;
import com.sw.plugins.commission.contract.entity.MemberAgreement;
import com.sw.plugins.customer.member.entity.Member;
import com.sw.plugins.customer.organization.entity.MemberOrganization;
import com.sw.plugins.customer.team.service.TeamService;

/**
 * Service实现类 -
 */

public class ContractAttachmentService extends CommonService<ContractAttachment> {

	@Override
	public void delete(ContractAttachment entity) throws Exception {
		// TODO Auto-generated method stub
		super.getRelationDao().delete("contractAttachment.delete", entity);
	}

	@Override
	public void deleteByArr(ContractAttachment entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object download(ContractAttachment entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getGrid(ContractAttachment entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContractAttachment> getList(ContractAttachment entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContractAttachment getOne(ContractAttachment entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContractAttachment> getPaginatedList(ContractAttachment entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getRecordCount(ContractAttachment entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(ContractAttachment entity) throws Exception {
		// TODO Auto-generated method stub
		super.getRelationDao().update("contractAttachment.insert", entity);
	}

	@Override
	public void update(ContractAttachment entity) throws Exception {
		// TODO Auto-generated method stub
		super.getRelationDao().update("contractAttachment.update", entity);
	}

	@Override
	public String upload(ContractAttachment entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}
	


	
	
}