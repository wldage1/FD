package com.sw.plugins.commission.contract.service;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.commission.contract.entity.Contract;
import com.sw.plugins.commission.contract.entity.ContractAttachment;
import com.sw.plugins.commission.contract.entity.MemberAgreement;
import com.sw.plugins.commission.organizationgrant.entity.CommissionFee;
import com.sw.plugins.customer.member.entity.Member;
import com.sw.plugins.customer.organization.entity.MemberOrganization;
import com.sw.plugins.customer.team.service.TeamService;
import com.sw.util.FTPUtil;
import com.sw.util.SystemProperty;

/**
 * Service实现类 -
 */

public class ContractService extends CommonService<Contract> {
	

	@Resource
	private MemberAgreementService memberAgreementService;
	@Resource
	private ContractAttachmentService contractAttachmentService;
	/**
	 * 获取合同列表的MAP集合
	 */
	public Map<String, Object> getGrid(Contract entity) throws Exception {
		List<Contract> resultList = null;
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
	public Map<String, Object> getOrgGrid(Contract entity) throws Exception {
		List<Contract> resultList = null;
		Map<String, Object> map = new Hashtable<String, Object>();
		//分页列表
		resultList = getOrgPaginatedList(entity);
		// 记录数
		long record = this.getOrgRecordCount(entity);
		// 页数
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());

		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	public void saveOrUpdate(Contract entity) throws Exception {
		if (entity != null && entity.getId() != null) {
			// 更新合同信息
			update(entity);
		} else {
			// 保存合同信息
			save(entity);
		}
	}
	public void saveOrUpdateContract(Contract entity) throws Exception {
		if (entity != null && entity.getId() != null) {
			// 更新合同信息
			updateContract(entity);
			ContractAttachment contractAttachment = new ContractAttachment();
			contractAttachment.setOrgContractID(Integer.valueOf(entity.getId().toString()));
			contractAttachment.setFileName(entity.getFileName());
			contractAttachment.setFileUrl(entity.getFileUrl());
			contractAttachmentService.update(contractAttachment);
		} else {
			// 保存合同信息
			saveContract(entity);
			ContractAttachment contractAttachment = new ContractAttachment();
			contractAttachment.setOrgContractID(Integer.valueOf(entity.getGeneratedKey().toString()));
			contractAttachment.setFileName(entity.getFileName());
			contractAttachment.setFileUrl(entity.getFileUrl());
			contractAttachmentService.save(contractAttachment);
		}
	}
	public void updateForStart(Contract entity) throws Exception {
		if (entity != null && entity.getId() != null) {
			// 更新合同信息
			updateForStartStatus(entity);
			updateForOthers(entity);
			MemberAgreement memberAgreement =new MemberAgreement();
			memberAgreement.setStatus(2);
			memberAgreement.setOrgID(entity.getOrgID());
			//memberAgreement.setAgreementTemplateID(Integer.valueOf(entity.getId().toString()));
			memberAgreementService.update(memberAgreement);
		} 
	}
	public void updateForStop(Contract entity) throws Exception {
		if (entity != null && entity.getId() != null) {
			// 更新合同信息
			updateForStopStatus(entity);
			MemberAgreement memberAgreement =new MemberAgreement();
			memberAgreement.setStatus(2);
			memberAgreement.setOrgID(entity.getOrgID());
			//memberAgreement.setAgreementTemplateID(Integer.valueOf(entity.getId().toString()));
			memberAgreementService.update(memberAgreement);
		} 
	}
	//更改协议的状态为启动
	public void updateForStartStatus(Contract entity) throws Exception {
		super.getRelationDao().update("contract.updateForStartStatus", entity);
	}
	//更改协议的状态为暂停
	public void updateForStopStatus(Contract entity) throws Exception {
		super.getRelationDao().update("contract.updateForStopStatus", entity);
	}
	//更改合同的状态为暂停
	public void updateForContractStatus(Contract entity) throws Exception {
		super.getRelationDao().update("contract.updateForContractStatus", entity);
	}
	public void updateForOthers(Contract entity) throws Exception {
		if (entity != null && entity.getId() != null) {
			// 更新合同信息
			super.getRelationDao().update("contract.updateForOthers", entity);
		} 
	}
	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Contract entity) throws Exception {
		// TODO Auto-generated method stub
		if (entity.getId()!=null){
			MemberAgreement memberAgreement =new MemberAgreement();
			memberAgreement.setAgreementTemplateID(Integer.valueOf(entity.getId().toString()));
			memberAgreementService.delete(memberAgreement);
			super.getRelationDao().delete("contract.delete", entity);
		}
	}
	public void deleteContract(Contract entity) throws Exception {
		// TODO Auto-generated method stub
		if (entity.getId()!=null){
			ContractAttachment contractAttachment = new ContractAttachment();
			contractAttachment.setOrgContractID(Integer.valueOf(entity.getId().toString()));
			contractAttachmentService.delete(contractAttachment);
			super.getRelationDao().delete("contract.delete_contract", entity);
		}
	}

	@Override
	public void deleteByArr(Contract entity) throws Exception {
		// TODO Auto-generated method stub
		
	}
	//查询当前机构的合同下载路径
	public Contract getFileUrl(Contract entity) throws Exception{
		return (Contract) super.getRelationDao().selectOne("contract.getone_contract", entity);
	}
	@Override
	public Object download(Contract entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Contract> getList(Contract entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Contract getOneContract(Contract entity) throws Exception {
		// TODO Auto-generated method stub
		return (Contract) super.getRelationDao().selectOne("contract.getone_contract", entity);
	}
	@Override
	public Contract getOne(Contract entity) throws Exception {
		// TODO Auto-generated method stub
		return (Contract) super.getRelationDao().selectOne("contract.getone", entity);
	}

	@Override
	public List<Contract> getPaginatedList(Contract entity) throws Exception {
		// TODO Auto-generated method stub
		return (List<Contract>) super.getRelationDao().selectList("contract.select_paginated", entity);
	}

	@Override
	public Long getRecordCount(Contract entity) throws Exception {
		// TODO Auto-generated method stub
		return (Long) super.getRelationDao().selectOne("contract.count", entity);
	}
	public List<Contract> getOrgPaginatedList(Contract entity) throws Exception {
		// TODO Auto-generated method stub
		return (List<Contract>) super.getRelationDao().selectList("contract.select_paginated_contract", entity);
	}
	public Long getOrgRecordCount(Contract entity) throws Exception {
		// TODO Auto-generated method stub
		return (Long) super.getRelationDao().selectOne("contract.count_contract", entity);
	}
	public Long countForTitle(Contract entity) throws Exception {
		// TODO Auto-generated method stub
		return (Long) super.getRelationDao().selectOne("contract.countForTitle", entity);
	}
	public Long countForContractName(Contract entity) throws Exception {
		// TODO Auto-generated method stub
		return (Long) super.getRelationDao().selectOne("contract.countForContractName", entity);
	}
	//判断合同是否签订
	public Long countForContract(Contract entity) throws Exception {
		// TODO Auto-generated method stub
		return (Long) super.getRelationDao().selectOne("contract.countForContract", entity);
	}
	@Override
	public void save(Contract entity) throws Exception {
		// TODO Auto-generated method stub
		super.getRelationDao().insert("contract.insert", entity);
	}

	@Override
	public void update(Contract entity) throws Exception {
		// TODO Auto-generated method stub
		super.getRelationDao().update("contract.update", entity);
	}
	public void saveContract(Contract entity) throws Exception {
		// TODO Auto-generated method stub
		super.getRelationDao().insert("contract.insertContract", entity);
	}
	public void updateContract(Contract entity) throws Exception {
		// TODO Auto-generated method stub
		super.getRelationDao().update("contract.updateContract", entity);
	}

	@Override
	public String upload(Contract entity, HttpServletRequest request)
			throws Exception {
		MultipartFile file = ((MultipartHttpServletRequest)request).getFile("Filedata");
		String path = SystemProperty.getInstance("config").getProperty("organization.contract.path");
		//重置文件名
		String tempFileName = request.getParameter("Filename");
		String fileExtensionName = tempFileName.substring(tempFileName.lastIndexOf("."));
		String fileName = tempFileName.substring(0,tempFileName.lastIndexOf("."));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String temp = sdf.format(new Date());
		String realFileName = fileName+'_'+temp + fileExtensionName;
		return FTPUtil.uploadFile(file, path, realFileName);
	}
	
	public Contract getOrgContractID(Contract entity) throws Exception {
		return (Contract) super.getRelationDao().selectOne("contract.select_orgContractID", entity);
	}
	
	public Contract getAgreementTemplateID(Contract entity) throws Exception {
		return (Contract)super.getRelationDao().selectOne("contract.select_agreementtemplateID", entity);
	}
	
	/**
	 * 验证居间公司是否有居间协议
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> checkContractList(Contract entity) throws Exception{
		Map<String, Object> map=new Hashtable<String, Object>();
		@SuppressWarnings("unchecked")
		List<Contract> list=(ArrayList<Contract>)super.getRelationDao().selectList("contract.check_contract_list", entity);
		if(list==null || list.size()<=0){
			map.put("status", 0);
		}else{
			for(Contract contract:list){
				if(contract.getStatus().equals((int)2)){
					map.put("status", 1);
					break;
				}else{
					map.put("status", 2);
				}
			}
		}
		return map;
	}
}