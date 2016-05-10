package com.sw.plugins.commission.organizationgrant.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sw.core.exception.DetailException;
import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.commission.adjust.entity.Commission;
import com.sw.plugins.commission.organizationgrant.entity.TeamOrOrgPayDetail;
import com.sw.plugins.commission.organizationgrant.entity.TeamOrOrgPayRecord;
import com.sw.plugins.customer.member.entity.Member;
import com.sw.plugins.customer.member.service.MemberService;
import com.sw.plugins.customer.organization.entity.MemberOrganization;
import com.sw.plugins.customer.organization.service.MemberOrganizationService;
import com.sw.plugins.message.messageinterface.service.CommonMessageService;
import com.sw.plugins.message.task.entity.SendMessage;
import com.sw.plugins.message.task.entity.UserMessage;
import com.sw.plugins.system.user.entity.User;

@Service
public class CommissionFeeService extends CommonService<Commission>{
	
private static Logger logger = Logger.getLogger(CommissionFeeService.class);
	
	@Resource
	private CommonMessageService commonMessageService;
	@Resource
	private TeamOrOrgPayDetailService teamOrOrgPayDetailService;
	
	@Resource
	private TeamOrOrgPayRecordService teamOrOrgPayRecordService;
	
	@Resource
	private MemberOrganizationService memberOrganizationService;
	
	@Resource
	private MemberService memberService;
	
	@Override
	public void save(Commission entity) throws Exception {
		
		Long orgId = entity.getTeamID();
		TeamOrOrgPayRecord teamOrOrgPayRecord = new TeamOrOrgPayRecord();
		teamOrOrgPayRecord.setSourceID(orgId);
		//支付记录表ID
		Long teamOrOrgPayRecordId;
		//判断是否已经存在当旬机构发放的记录
		teamOrOrgPayRecord = teamOrOrgPayRecordService.getOne(teamOrOrgPayRecord);
		if(teamOrOrgPayRecord != null){
			teamOrOrgPayRecordId = teamOrOrgPayRecord.getId();
			//累计到账金额
			BigDecimal payAmount = teamOrOrgPayRecord.getPayAmount().add(entity.getCommission());
			teamOrOrgPayRecord.setPayAmount(payAmount);
			teamOrOrgPayRecordService.updatePayAmount(teamOrOrgPayRecord);
		}else{
			MemberOrganization memberOrganization = new MemberOrganization();
			memberOrganization.setId(entity.getTeamID());
			//查询机构银行账号信息
			memberOrganization = memberOrganizationService.getBankInfo(memberOrganization);
			TeamOrOrgPayRecord record = new TeamOrOrgPayRecord();
			record.setSourceID(entity.getTeamID());
			record.setPayAmount(entity.getCommission());
			record.setPayAmount2(entity.getCommission());
			record.setAccountID(memberOrganization.getAccount());
			record.setAccountName(memberOrganization.getAccountHolder());
			record.setBankName(memberOrganization.getBankName());
			record.setStatus((short)0);
			//保存机构居间费支付记录表
			teamOrOrgPayRecordService.save(record);
			teamOrOrgPayRecordId = record.getGeneratedKey();
		}
		TeamOrOrgPayDetail detail = new TeamOrOrgPayDetail();
		detail.setTeamPayID(teamOrOrgPayRecordId);
		detail.setCommissionID(entity.getId());
		detail.setPayAmount(entity.getCommission());
		//保存机构居间费支付明细表
		teamOrOrgPayDetailService.save(detail);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Commission> getPaginatedList(Commission entity) throws Exception {
		return (List<Commission>) super.getRelationDao().selectList("commissionfee.select_paginated", entity);
	}
	
	@Override
	public Map<String, Object> getGrid(Commission entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Commission> resultList = getPaginatedList(entity);
		map.put("rows", resultList);
		return map;
	}
	
	@Override
	public Commission getOne(Commission entity) throws Exception {
		return (Commission)super.getRelationDao().selectOne("commissionfee.select_one", entity);
	}
	
	/**
	 * 发票通知
	 * 
	 * @param entity
	 * @throws Exception
	 */
	public void invoice(Member entity) throws Exception{
		User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		entity = memberService.memberOrgType(entity);
		SendMessage sendMessage = new SendMessage();
		//发送方式 (站内)
		sendMessage.setSendWayStr("1");
		//发送者类型(运营方)
		sendMessage.setSendUserType((short)1);
		//发送者ID
		sendMessage.setSendUserID(currentUser.getId());
		//模板替换参数
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("name", "");
		sendMessage.setTemplateParameters(userMap);
		//用户信息
		List<UserMessage> userList = new ArrayList<UserMessage>();
		UserMessage userMessage = new UserMessage();
		userMessage.setUserID(entity.getId());
		userMessage.setUserType((short)3);
		userList.add(userMessage);
		sendMessage.setUserList(userList);
		String templateCode = "sys_manage_commission_organizationgrant_fptz";
		sendMessage.setTemplateCode(templateCode);
		try{
			commonMessageService.send(sendMessage);
		}catch(Exception e){
			logger.error(DetailException.expDetail(e, getClass()));
		}
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

}
