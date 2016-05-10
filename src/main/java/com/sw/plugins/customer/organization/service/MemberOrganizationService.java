package com.sw.plugins.customer.organization.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import jxl.write.WritableWorkbook;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.customer.member.entity.Member;
import com.sw.plugins.customer.member.entity.MemberApplication;
import com.sw.plugins.customer.member.entity.MemberTeamOrOrgHistory;
import com.sw.plugins.customer.member.service.MemberApplicationService;
import com.sw.plugins.customer.member.service.MemberService;
import com.sw.plugins.customer.member.service.MemberTeamOrOrgHistoryService;
import com.sw.plugins.customer.organization.entity.MemberOrganization;
import com.sw.plugins.customer.team.entity.TeamOrOrgApplication;
import com.sw.plugins.customer.team.service.TeamOrOrgApplicationService;
import com.sw.plugins.message.messageinterface.service.CommonMessageService;
import com.sw.plugins.message.task.entity.SendMessage;
import com.sw.plugins.message.task.entity.UserMessage;
import com.sw.plugins.system.user.entity.User;
import com.sw.util.ExportExcel;
import com.sw.util.FTPUtil;
import com.sw.util.SystemProperty;

/**
 * Service实现类 -
 */

public class MemberOrganizationService extends CommonService<MemberOrganization> {
	@Resource
	private MemberService memberService;
	@Resource
	private TeamOrOrgApplicationService teamOrOrgApplicationService;
	@Resource
	private MemberTeamOrOrgHistoryService memberTeamOrOrgHistoryService;
	@Resource
	private MemberApplicationService memberApplicationService; 
	@Resource
	private CommonMessageService commonMessageService;
	//获取所有机构
	@SuppressWarnings("unchecked")
	public List<MemberOrganization> getAllMemberOrganization(MemberOrganization entity) throws Exception {
		return (List<MemberOrganization>) super.getRelationDao().selectList("memberOrganization.select_organization", entity);
	}
	//获取所有机构 合同管理用到的
	@SuppressWarnings("unchecked")
	public List<MemberOrganization> getAllMemberOrganizationContract(MemberOrganization entity) throws Exception {
		return (List<MemberOrganization>) super.getRelationDao().selectList("memberOrganization.select_organization_contract", entity);
	}
	
	//查询机构帐号信息
	public MemberOrganization getBankInfo(MemberOrganization entity)
			throws Exception {
		return (MemberOrganization) super.getRelationDao().selectOne("memberOrganization.select_one", entity);
	}
	
	/**
	 * 获取理财师机构的MAP集合
	 */
	public Map<String, Object> getGrid(MemberOrganization entity) throws Exception {
		List<MemberOrganization> resultList = null;
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

    //理财师机构的分页查询
	@SuppressWarnings("unchecked")
	@Override
	public List<MemberOrganization> getPaginatedList(MemberOrganization entity) throws Exception {
		return (List<MemberOrganization>) super.getRelationDao().selectList("memberOrganization.select_paginated", entity);
	}
	//理财师机构的统计
	@Override
	public Long getRecordCount(MemberOrganization entity) throws Exception {
		return (Long) super.getRelationDao().selectOne("memberOrganization.count", entity);
	}
	public Long memberOrganizationCount(MemberOrganization entity) throws Exception {
		return (Long) super.getRelationDao().selectOne("memberOrganization.memberOrganizationCount", entity);
	}
	
	@SuppressWarnings("unchecked")
	public List<Long> getOrgIds(MemberOrganization entity) throws Exception {
		return (List<Long>)super.getRelationDao().selectList("memberOrganization.select_orgId", entity);
	}
	
	@Override
	public void save(MemberOrganization entity) throws Exception {
		
	}
	@Override
	public MemberOrganization getOne(MemberOrganization entity)
			throws Exception {
		return (MemberOrganization) super.getRelationDao().selectOne("memberOrganization.select_for_check", entity);
	}
	public MemberOrganization getOneForUpdate(MemberOrganization entity)
			throws Exception {
		return (MemberOrganization) super.getRelationDao().selectOne("memberOrganization.select_for_update", entity);
	}
	public MemberOrganization getOneForPromote(MemberOrganization entity)
		throws Exception {
		return (MemberOrganization) super.getRelationDao().selectOne("memberOrganization.select_for_promote", entity);
	}
    //理财师机构更新
	@Override
	public void update(MemberOrganization entity) throws Exception {
		super.getRelationDao().update("memberOrganization.update", entity);	
	}
	//更新机构的晋升时间
	public void update_for_promotionTtime(MemberOrganization entity) throws Exception {
		super.getRelationDao().update("memberOrganization.update_for_promotionTtime", entity);	
	}
	public void updateforstatus(MemberOrganization entity) throws Exception {
		super.getRelationDao().update("memberOrganization.update_for_status", entity);	
	}
	/**
	 * 机构营业执照上传
	 * 
	 * @param provider
	 * @param request
	 * @return
	 */
	@Override
	public String upload(MemberOrganization entity, HttpServletRequest request) throws Exception {
		MultipartFile file = ((MultipartHttpServletRequest)request).getFile("Filedata");
		String path = "";
		//重置文件名
		///////
		path = SystemProperty.getInstance("config").getProperty("team.licence.card.path")+"licenceImage" ;
		//重置文件名//时间戳
		Long newFileName = System.currentTimeMillis(); 
		String realFileName = newFileName  + ".jpg";
		/////////
		return FTPUtil.uploadFile(file, path, realFileName);
	}
	/**
	 * 修改理财师机构信息，获取FTP上传路径名
	 * 
	 * @param provider
	 * @return 
	 */
	public String getPathName(MemberOrganization entity) throws Exception{
		return String.valueOf(getRelationDao().getCount("provider.selectMaxId", entity)+1);
	}
	
	/**
	 * 获取激励费用发放详细集合
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MemberOrganization> getIncentiveList(MemberOrganization entity) throws Exception {
		return (List<MemberOrganization>)super.getRelationDao().selectList("memberOrganization.select_incentive", entity);
	}
	
	@Override
	public void init(InitData initData) throws Exception {
		
	}

	@Override
	public void delete(MemberOrganization entity) throws Exception {
		
	}
	public void delete_for_organization(MemberOrganization entity) throws Exception {
		super.getRelationDao().delete("memberOrganization.delete_for_organization", entity);
	}
	public void delete_for_teamToOrg(MemberOrganization entity) throws Exception {
		super.getRelationDao().delete("memberOrganization.delete_for_teamToOrg", entity);
	}

	@Override
	public void deleteByArr(MemberOrganization entity) throws Exception {
		
	}
	public void updateCancel(MemberOrganization memberOrganization) throws Exception {
			//更新理财师状态为启用
	    	Member member3 = new Member();
	    	member3.setTeamID(Integer.valueOf(memberOrganization.getId().toString()));
	    	member3.setStatus(2);
	    	memberService.updateStatus(member3);
	    	/////////////////
	    	MemberOrganization updateMemberOrganization = new MemberOrganization();
			updateMemberOrganization.setId(memberOrganization.getId());
			updateMemberOrganization.setStatus(3);
	    	//更新机构状态
	    	updateforstatus(updateMemberOrganization);
	    	Member member2 = new Member();
	    	if (memberOrganization.getId()!=null){
	    		member2.setTeamID(Integer.valueOf(memberOrganization.getId().toString()));
	    	}
	    	List<Member> memberList = memberService.selectteamOrOrgmember(member2);
	    	//清空该机构下的理财师的teamid,解除所有理财师与该机构的绑定关系
	    	Member member = new Member();
	    	if (memberOrganization.getId()!=null){
	    		member.setTeamID(Integer.valueOf(memberOrganization.getId().toString()));
	    	}
	    	memberService.updateTeamId(member);
	    	//更新理财师申请机构表状态
	    	MemberApplication MemberApplication = new MemberApplication();
	    	MemberApplication.setTeamID(Integer.valueOf(memberOrganization.getId().toString()));
	    	memberApplicationService.update(MemberApplication);
	    	//更新其他的申请中的为不通过
	    	TeamOrOrgApplication teamOrOrgApplication = new TeamOrOrgApplication();
	    	teamOrOrgApplication.setStatus(2);
	    	teamOrOrgApplication.setTeamID(Integer.valueOf(memberOrganization.getId().toString()));
	    	teamOrOrgApplicationService.updateForCancel(teamOrOrgApplication);
	    	//更新理财师组织经历表状态
	    	MemberTeamOrOrgHistory memberTeamOrOrgHistory = new MemberTeamOrOrgHistory();
	    	memberTeamOrOrgHistory.setTeamOrOrgID(Integer.valueOf(memberOrganization.getId().toString()));
	    	memberTeamOrOrgHistory.setStatus(5);
	    	memberTeamOrOrgHistoryService.update(memberTeamOrOrgHistory);
	    	//消息测试 begin
			Map parameterMap = new HashMap();
			SendMessage sendMessage = new SendMessage();
			UserMessage us = new UserMessage();
			List<UserMessage> list = new ArrayList();
			User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			sendMessage = new SendMessage();
			for(Member mem:memberList){
				us = new UserMessage();
				us.setUserID(mem.getId());
				us.setUserType((short)3);
				list.add(us);
			}
			sendMessage.setUserList(list);
			//设置发送方式
			sendMessage.setSendWayStr("1");
			//设置模板参数
			sendMessage.setTemplateParameters(parameterMap);
			//设置模板code
			sendMessage.setTemplateCode("sys_manage_organization_htzjzxjg");
			//设置发送者ID
			if (currentUser.getId()!=null){
				sendMessage.setSendUserID(currentUser.getId());
			}
			//设置发送者类型
			sendMessage.setSendUserType((short)1);
			//发送消息
			try {
				commonMessageService.send(sendMessage);
			} catch (Exception e) {
				
			}
		    //消息测试end
	    	
	}
	public void savebackOrganizationCheck(MemberOrganization memberOrganization) throws Exception {
		MemberOrganization updateMemberOrganization = new MemberOrganization();
		MemberOrganization updateorgidentity = new MemberOrganization();
		TeamOrOrgApplication teamOrOrgApplication =  new TeamOrOrgApplication();
		Member member2 = new Member();
		List<Member> memberList=null;
		Map parameterMap = new HashMap();
		SendMessage sendMessage = new SendMessage();
		UserMessage us = new UserMessage();
		List<UserMessage> list = new ArrayList();
		User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		switch (memberOrganization.getCheckType())
		{
		case 1://机构创建申请退回
				updateorgidentity.setId(memberOrganization.getId());
				updateorgidentity.setIdentity(memberOrganization.getIdentity());
				this.update(updateorgidentity);
		    	//更新理财师机构状态
				updateMemberOrganization.setId(memberOrganization.getId());
				updateMemberOrganization.setStatus(4); 
				updateforstatus(updateMemberOrganization);
		    	//更新机构申请表状态
		    	
		    	teamOrOrgApplication.setId(memberOrganization.getCheckId());
		    	teamOrOrgApplication.setStatus(2);
		    	teamOrOrgApplication.setApplicantFeedback(memberOrganization.getApplicantFeedback());
		    	teamOrOrgApplicationService.update(teamOrOrgApplication);
		    	//更新理财师组织经历表状态
		    	MemberTeamOrOrgHistory memberTeamOrOrgHistory = new MemberTeamOrOrgHistory();
		    	memberTeamOrOrgHistory.setTeamOrOrgID(Integer.valueOf(memberOrganization.getId().toString()));
		    	memberTeamOrOrgHistory.setStatus(3);
		    	memberTeamOrOrgHistoryService.updateForStatus(memberTeamOrOrgHistory);
		    	//消息测试 begin
			    parameterMap = new HashMap();
			    parameterMap.put("re", memberOrganization.getApplicantFeedback());
				sendMessage = new SendMessage();
				us = new UserMessage();
				us.setUserID((long)memberOrganization.getApplicationMember());
				us.setUserType((short)3);
				list = new ArrayList();
				list.add(us);
				sendMessage.setUserList(list);
				//设置发送方式
				sendMessage.setSendWayStr("1");
				//设置模板参数
				sendMessage.setTemplateParameters(parameterMap);
				//设置模板code
				sendMessage.setTemplateCode("sys_manage_organization_jgcjbtg");
				//设置发送者ID
				
				if (currentUser.getId()!=null){
					sendMessage.setSendUserID(currentUser.getId());
				}
				//设置发送者类型
				sendMessage.setSendUserType((short)1);
				//发送消息
				try {
					commonMessageService.send(sendMessage);
				} catch (Exception e) {
					
				}
			    //消息测试end
			break;
		case 2://机构管理人变更申请退回
		    	//更新机构申请表状态
		    	teamOrOrgApplication.setId(memberOrganization.getCheckId());
		    	teamOrOrgApplication.setStatus(2);
		    	teamOrOrgApplication.setApplicantFeedback(memberOrganization.getApplicantFeedback());
		    	teamOrOrgApplicationService.update(teamOrOrgApplication);
		    	//消息测试 begin
			    parameterMap = new HashMap();
				sendMessage = new SendMessage();
				us = new UserMessage();
				us.setUserID((long)memberOrganization.getApplicationMember());
				us.setUserType((short)3);
				list = new ArrayList();
				list.add(us);
				sendMessage.setUserList(list);
				//设置发送方式
				sendMessage.setSendWayStr("1");
				//设置模板参数
				sendMessage.setTemplateParameters(parameterMap);
				//设置模板code
				sendMessage.setTemplateCode("sys_manage_organization_jgglrbgbtg");
				//设置发送者ID
				
				if (currentUser.getId()!=null){
					sendMessage.setSendUserID(currentUser.getId());
				}
				//设置发送者类型
				sendMessage.setSendUserType((short)1);
				//发送消息
				try {
					commonMessageService.send(sendMessage);
				} catch (Exception e) {
					
				}
			    //消息测试end
			break;
		case 3://机构年检申请退回
		    	//更新机构申请表状态
		    	teamOrOrgApplication.setId(memberOrganization.getCheckId());
		    	teamOrOrgApplication.setStatus(2);
		    	teamOrOrgApplication.setApplicantFeedback(memberOrganization.getApplicantFeedback());
		    	teamOrOrgApplicationService.update(teamOrOrgApplication);
		    	//更新理财师机构状态为暂停
		    	updateMemberOrganization.setId(memberOrganization.getId());
		    	updateMemberOrganization.setStatus(2); 
		    	updateforstatus(updateMemberOrganization);
		    	//更新理财师状态为暂停
		    	Member member = new Member();
		    	member.setTeamID(Integer.valueOf(memberOrganization.getId().toString()));
		    	member.setStatus(0);
		    	memberService.updateAnnualBack(member);
		    	//消息测试 begin
			    parameterMap = new HashMap();
				sendMessage = new SendMessage();
				us = new UserMessage();
				us.setUserID((long)memberOrganization.getApplicationMember());
				us.setUserType((short)3);
				list = new ArrayList();
				list.add(us);
				sendMessage.setUserList(list);
				//设置发送方式
				sendMessage.setSendWayStr("1");
				//设置模板参数
				sendMessage.setTemplateParameters(parameterMap);
				//设置模板code
				sendMessage.setTemplateCode("sys_manage_organization_jgnjbtg");
				//设置发送者ID
				
				if (currentUser.getId()!=null){
					sendMessage.setSendUserID(currentUser.getId());
				}
				//设置发送者类型
				sendMessage.setSendUserType((short)1);
				//发送消息
				try {
					commonMessageService.send(sendMessage);
				} catch (Exception e) {
					
				}
			    //消息测试end
			break;
		case 4://机构注销申请退回
				//是否需要进行注销前置条件查询？
				try {
		    	//更新机构申请表状态
		    	teamOrOrgApplication.setId(memberOrganization.getCheckId());
		    	teamOrOrgApplication.setStatus(2);
		    	teamOrOrgApplication.setApplicantFeedback(memberOrganization.getApplicantFeedback());
		    	teamOrOrgApplicationService.update(teamOrOrgApplication);
		    	//消息测试 begin
			    parameterMap = new HashMap();
				sendMessage = new SendMessage();
				us = new UserMessage();
				us.setUserID((long)memberOrganization.getApplicationMember());
				us.setUserType((short)3);
				list = new ArrayList();
				list.add(us);
				sendMessage.setUserList(list);
				//设置发送方式
				sendMessage.setSendWayStr("1");
				//设置模板参数
				sendMessage.setTemplateParameters(parameterMap);
				//设置模板code
				sendMessage.setTemplateCode("sys_manage_organization_jgzxbtg");
				//设置发送者ID
				
				if (currentUser.getId()!=null){
					sendMessage.setSendUserID(currentUser.getId());
				}
				//设置发送者类型
				sendMessage.setSendUserType((short)1);
				//发送消息
				
					commonMessageService.send(sendMessage);
				} catch (Exception e) {
					
				}
			    //消息测试end
			break;
		default:break;
		}
    	
	}
	public void saveOrganizationCheck(MemberOrganization memberOrganization) throws Exception {
		MemberOrganization updateMemberOrganization = new MemberOrganization();
		MemberOrganization updateorgidentity = new MemberOrganization();
		TeamOrOrgApplication teamOrOrgApplication =  new TeamOrOrgApplication();
		Member member2 = new Member();
		List<Member> memberList=null;
		Map parameterMap = new HashMap();
		SendMessage sendMessage = new SendMessage();
		UserMessage us = new UserMessage();
		List<UserMessage> list = new ArrayList();
		User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		switch (memberOrganization.getCheckType())
		{
		case 1://机构创建申请
				updateorgidentity.setId(memberOrganization.getId());
				updateorgidentity.setIdentity(memberOrganization.getIdentity());
				this.update(updateorgidentity);
		    	//更新理财师机构状态
				updateMemberOrganization.setId(memberOrganization.getId());
				updateMemberOrganization.setStatus(1); 
				updateMemberOrganization.setIdentity(memberOrganization.getIdentity());
				updateforstatus(updateMemberOrganization);
		    	//更新机构申请表状态
		    	teamOrOrgApplication.setId(memberOrganization.getCheckId());
		    	teamOrOrgApplication.setStatus(1);
		    	teamOrOrgApplication.setApplicantFeedback(memberOrganization.getApplicantFeedback());
		    	teamOrOrgApplicationService.update(teamOrOrgApplication);
		    	//更新理财师组织经历表状态
		    	MemberTeamOrOrgHistory memberTeamOrOrgHistory = new MemberTeamOrOrgHistory();
		    	memberTeamOrOrgHistory.setTeamOrOrgID(Integer.valueOf(memberOrganization.getId().toString()));
		    	memberTeamOrOrgHistory.setStatus(2);
		    	memberTeamOrOrgHistoryService.updateForStatus(memberTeamOrOrgHistory);
		    	//消息测试 begin
			    parameterMap = new HashMap();
				sendMessage = new SendMessage();
				us = new UserMessage();
				us.setUserID((long)memberOrganization.getApplicationMember());
				us.setUserType((short)3);
				list = new ArrayList();
				list.add(us);
				sendMessage.setUserList(list);
				//设置发送方式
				sendMessage.setSendWayStr("1");
				//设置模板参数
				sendMessage.setTemplateParameters(parameterMap);
				//设置模板code
				sendMessage.setTemplateCode("sys_manage_organization_jgcjtg");
				//设置发送者ID
				
				if (currentUser.getId()!=null){
					sendMessage.setSendUserID(currentUser.getId());
				}
				//设置发送者类型
				sendMessage.setSendUserType((short)1);
				//发送消息
				try {
					commonMessageService.send(sendMessage);
				} catch (Exception e) {
					
				}
			    //消息测试end
			break;
		case 2://机构管理人变更申请
		    	//更新理财师状态 
		    	Member member = new Member();
		    	member.setId(Long.valueOf(memberOrganization.getMemberID()));
		    	member.setType(2);
		    	//更新申请人为机构管理者
		    	memberService.update(member);
		    	member = new Member();
		    	member.setId(Long.valueOf(memberOrganization.getApplicationMember()));
		    	member.setType(1);
		    	memberService.update(member);
		    	//更新机构申请表状态
		    	
		    	teamOrOrgApplication.setId(memberOrganization.getCheckId());
		    	teamOrOrgApplication.setStatus(1);
		    	teamOrOrgApplication.setApplicantFeedback(memberOrganization.getApplicantFeedback());
		    	teamOrOrgApplicationService.update(teamOrOrgApplication);
		    	//消息测试 begin
		    	member2 = new Member();
		    	if (memberOrganization.getId()!=null){
		    		member2.setTeamID(Integer.valueOf(memberOrganization.getId().toString()));
		    	}
		    	memberList = memberService.selectteamOrOrgmember(member2);
			    parameterMap = new HashMap();
			    list = new ArrayList();
				sendMessage = new SendMessage();
				for(Member mem:memberList){
					us = new UserMessage();
					us.setUserID(mem.getId());
					us.setUserType((short)3);
					list.add(us);
				}
				sendMessage.setUserList(list);
				//设置发送方式
				sendMessage.setSendWayStr("1");
				//设置模板参数
				sendMessage.setTemplateParameters(parameterMap);
				//设置模板code
				sendMessage.setTemplateCode("sys_manage_organization_jgglrbgtg");
				//设置发送者ID
				if (currentUser.getId()!=null){
					sendMessage.setSendUserID(currentUser.getId());
				}
				//设置发送者类型
				sendMessage.setSendUserType((short)1);
				//发送消息
				try {
					commonMessageService.send(sendMessage);
				} catch (Exception e) {
					
				}
			    //消息测试end
			break;
		case 3://机构年检申请
		    	//更新理财师机构状态   
				updateMemberOrganization.setId(memberOrganization.getId());
				updateMemberOrganization.setLicenceCode(memberOrganization.getCheckLicenceCode());
				updateMemberOrganization.setLicenceExpireTime(memberOrganization.getCheckLicenceExpireTime());
				updateMemberOrganization.setLicenceImage(memberOrganization.getCheckLicenceImage());
				updateMemberOrganization.setStatus(1);//启用
				update(updateMemberOrganization);
				updateforstatus(updateMemberOrganization);
		    	//更新理财师状态为启用
		    	Member member1 = new Member();
		    	member1.setTeamID(Integer.valueOf(memberOrganization.getId().toString()));
		    	member1.setStatus(2);
		    	memberService.updateStatus(member1);
		    	//更新机构申请表状态
		    	teamOrOrgApplication.setId(memberOrganization.getCheckId());
		    	teamOrOrgApplication.setStatus(1);
		    	teamOrOrgApplication.setApplicantFeedback(memberOrganization.getApplicantFeedback());
		    	teamOrOrgApplicationService.update(teamOrOrgApplication);
		    	//消息测试 begin
			    parameterMap = new HashMap();
				sendMessage = new SendMessage();
				us = new UserMessage();
				us.setUserID((long)memberOrganization.getApplicationMember());
				us.setUserType((short)3);
				list = new ArrayList();
				list.add(us);
				sendMessage.setUserList(list);
				//设置发送方式
				sendMessage.setSendWayStr("1");
				//设置模板参数
				sendMessage.setTemplateParameters(parameterMap);
				//设置模板code
				sendMessage.setTemplateCode("sys_manage_organization_jgnjtg");
				//设置发送者ID
				
				if (currentUser.getId()!=null){
					sendMessage.setSendUserID(currentUser.getId());
				}
				//设置发送者类型
				sendMessage.setSendUserType((short)1);
				//发送消息
				try {
					commonMessageService.send(sendMessage);
				} catch (Exception e) {
					
				}
			    //消息测试end
			break;
		case 4:
				//是否需要进行注销前置条件查询？
				//更新理财师状态为启用
		    	Member member3 = new Member();
		    	member3.setTeamID(Integer.valueOf(memberOrganization.getId().toString()));
		    	member3.setStatus(2);
		    	memberService.updateStatus(member3);
		    	//更新理财师机构状态
				updateMemberOrganization.setId(memberOrganization.getId());
				updateMemberOrganization.setStatus(3); 
				updateforstatus(updateMemberOrganization);
				member2 = new Member();
		    	if (memberOrganization.getId()!=null){
		    		member2.setTeamID(Integer.valueOf(memberOrganization.getId().toString()));
		    	}
		    	memberList = memberService.selectteamOrOrgmember(member2);
		    	//更新理财师状态为个人状态,已完成目前注销方便测试
		    	Member member11 = new Member();
		    	member11.setTeamID(Integer.valueOf(memberOrganization.getId().toString()));
		    	memberService.updateTeamId(member11);
		    	//更新机构申请表状态
		    	teamOrOrgApplication.setId(memberOrganization.getCheckId());
		    	teamOrOrgApplication.setStatus(1);
		    	teamOrOrgApplication.setApplicantFeedback(memberOrganization.getApplicantFeedback());
		    	teamOrOrgApplicationService.update(teamOrOrgApplication);
		    	//更新其他的申请中的为不通过
		    	teamOrOrgApplication = new TeamOrOrgApplication();
		    	teamOrOrgApplication.setStatus(2);
		    	teamOrOrgApplication.setTeamID(Integer.valueOf(memberOrganization.getId().toString()));
		    	teamOrOrgApplicationService.updateForCancel(teamOrOrgApplication);
		    	//更新理财师申请机构表状态
		    	MemberApplication MemberApplication = new MemberApplication();
		    	MemberApplication.setTeamID(Integer.valueOf(memberOrganization.getId().toString()));
		    	memberApplicationService.update(MemberApplication);
		    	//更新理财师组织经历表状态
		    	memberTeamOrOrgHistory = new MemberTeamOrOrgHistory();
		    	memberTeamOrOrgHistory.setTeamOrOrgID(Integer.valueOf(memberOrganization.getId().toString()));
		    	memberTeamOrOrgHistory.setStatus(5);
		    	memberTeamOrOrgHistoryService.update(memberTeamOrOrgHistory);
		    	//////////////
		    	//消息测试 begin
		    	
			    parameterMap = new HashMap();
			    list = new ArrayList();
				sendMessage = new SendMessage();
				for(Member mem:memberList){
					us = new UserMessage();
					us.setUserID(mem.getId());
					us.setUserType((short)3);
					list.add(us);
				}
				sendMessage.setUserList(list);
				//设置发送方式
				sendMessage.setSendWayStr("1");
				//设置模板参数
				sendMessage.setTemplateParameters(parameterMap);
				//设置模板code
				sendMessage.setTemplateCode("sys_manage_organization_jgzxtg");
				//设置发送者ID
				if (currentUser.getId()!=null){
					sendMessage.setSendUserID(currentUser.getId());
				}
				//设置发送者类型
				sendMessage.setSendUserType((short)1);
				//发送消息
				try {
					commonMessageService.send(sendMessage);
				} catch (Exception e) {
					
				}
			    //消息测试end
			break;
		default:break;
		}
	}
	public void updateRecoveryOrganization(MemberOrganization memberOrganization) throws Exception {
	    	MemberOrganization updateMemberOrganization = new MemberOrganization();
			updateMemberOrganization.setId(memberOrganization.getId());
			updateMemberOrganization.setStatus(1); 
	    	//更新理财师机构状态
	    	updateforstatus(updateMemberOrganization);
	    	//更新理财师机构人员的状态
	    	Member member =new Member();
	    	member.setTeamID(Integer.valueOf(memberOrganization.getId().toString()));
	    	member.setStatus(2);
	    	memberService.updateStatus(member); 
	    	//消息测试 begin
			Map parameterMap = new HashMap();
			SendMessage sendMessage = new SendMessage();
			UserMessage us = new UserMessage();
			List<UserMessage> list = new ArrayList();
			User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<Member> memberList = memberService.selectteamOrOrgmember(member);
			sendMessage = new SendMessage();
			for(Member mem:memberList){
				us = new UserMessage();
				us.setUserID(mem.getId());
				us.setUserType((short)3);
				list.add(us);
			}
			sendMessage.setUserList(list);
			//设置发送方式
			sendMessage.setSendWayStr("1");
			//设置模板参数
			sendMessage.setTemplateParameters(parameterMap);
			//设置模板code
			sendMessage.setTemplateCode("sys_manage_organization_jghfyw");
			//设置发送者ID
			if (currentUser.getId()!=null){
				sendMessage.setSendUserID(currentUser.getId());
			}
			//设置发送者类型
			sendMessage.setSendUserType((short)1);
			//发送消息
			try {
				commonMessageService.send(sendMessage);
			} catch (Exception e) {
				
			}
		    //消息测试end
    	
	}
	public void updateSuspendOrganization(MemberOrganization memberOrganization) throws Exception {
	    	MemberOrganization updatememberOrganization = new MemberOrganization();
			updatememberOrganization.setId(memberOrganization.getId());
			updatememberOrganization.setStatus(2);
	    	//更新理财师机构状态
	    	updateforstatus(updatememberOrganization);
	    	//更新理财师机构人员的状态
	    	Member member =new Member();
	    	member.setTeamID(Integer.valueOf(memberOrganization.getId().toString()));
	    	member.setStatus(0);
	    	memberService.updateStatusForOrg(member);
	    	//消息测试 begin
			Map parameterMap = new HashMap();
			SendMessage sendMessage = new SendMessage();
			UserMessage us = new UserMessage();
			List<UserMessage> list = new ArrayList();
			User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<Member> memberList = memberService.selectteamOrOrgmember(member);
			sendMessage = new SendMessage();
			for(Member mem:memberList){
				us = new UserMessage();
				us.setUserID(mem.getId());
				us.setUserType((short)3);
				list.add(us);
			}
			sendMessage.setUserList(list);
			//设置发送方式
			sendMessage.setSendWayStr("1");
			//设置模板参数
			sendMessage.setTemplateParameters(parameterMap);
			//设置模板code
			sendMessage.setTemplateCode("sys_manage_organization_jgztyw");
			//设置发送者ID
			if (currentUser.getId()!=null){
				sendMessage.setSendUserID(currentUser.getId());
			}
			//设置发送者类型
			sendMessage.setSendUserType((short)1);
			//发送消息
			try {
				commonMessageService.send(sendMessage);
			} catch (Exception e) {
				
			}
		    //消息测试end
	
	}
	
	@SuppressWarnings("unchecked")
	public WritableWorkbook exportExcel(MemberOrganization entity,String colName, String colModel, WritableWorkbook excel) throws Exception{
		List<Map<String,Object>> objList = (List<Map<String,Object>>)super.getRelationDao().selectList("memberOrganization.select_for_export", entity);
		if (objList != null) {
			for (Map<String, Object> one : objList) {
				one.put("name",one.get("name"));
				one.put("shortName",one.get("shortName"));
				one.put("memberName",one.get("memberName"));
				one.put("memberPhone",one.get("memberPhone"));
				one.put("memberEmail",one.get("memberEmail"));
				one.put("checkCount",Integer.parseInt(one.get("checkCount").toString())>0 ? "待审":"已审核");
			}
		}
		String[] colNames = colName.split(",");
		String[] colModels = colModel.split(",");
		
		String excelName = "理财师机构列表";
		int colNameNum = colNames.length ;
		int colModelNum = colModels.length;
		ExportExcel exportExcel = new ExportExcel();
		excel = exportExcel.createExcel(excelName, colNames, colNameNum, colModels, colModelNum, objList, excel);
		return excel;
	}
	
	@Override
	public Object download(MemberOrganization entity, HttpServletRequest request)
			throws Exception {
		return null;
	}

	@Override
	public List<MemberOrganization> getList(MemberOrganization entity)
			throws Exception {
		return null;
	}

	

	
}