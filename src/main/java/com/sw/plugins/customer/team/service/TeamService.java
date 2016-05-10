package com.sw.plugins.customer.team.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;

import com.sw.core.exception.DetailException;
import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.customer.member.entity.Member;
import com.sw.plugins.customer.member.entity.MemberApplication;
import com.sw.plugins.customer.member.entity.MemberTeamOrOrgHistory;
import com.sw.plugins.customer.member.service.MemberApplicationService;
import com.sw.plugins.customer.member.service.MemberService;
import com.sw.plugins.customer.member.service.MemberTeamOrOrgHistoryService;
import com.sw.plugins.customer.organization.entity.MemberOrganization;
import com.sw.plugins.customer.organization.service.MemberOrganizationService;
import com.sw.plugins.customer.team.entity.Team;
import com.sw.plugins.customer.team.entity.TeamOrOrgApplication;
import com.sw.plugins.message.messageinterface.service.CommonMessageService;
import com.sw.plugins.message.task.entity.SendMessage;
import com.sw.plugins.message.task.entity.UserMessage;
import com.sw.plugins.system.user.entity.User;

/**
 * Service实现类 -
 */

public class TeamService extends CommonService<Team> {
	/**
	 * 获取理财师团队的MAP集合
	 */
	@Resource
	private MemberService memberService;
	@Resource
	private TeamOrOrgApplicationService teamOrOrgApplicationService;
	@Resource
	private MemberTeamOrOrgHistoryService memberTeamOrOrgHistoryService;
	@Resource
	private MemberOrganizationService memberOrganizationService;
	@Resource
	private MemberApplicationService memberApplicationService; 
	@Resource
	private CommonMessageService commonMessageService;
	public Map<String, Object> getGrid(Team entity) throws Exception {
		List<Team> resultList = null;
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
	public void updateSuspendTeam(Team team) throws Exception {
		// TODO Auto-generated method stub
		Team updateTeam = new Team();
		updateTeam.setId(team.getId());
		updateTeam.setStatus(2);
    	//更新理财师团队状态
    	this.updateforstatus(updateTeam);
    	//更新理财师团队人员的状态
    	Member member =new Member();
    	member.setTeamID(Integer.valueOf(team.getId().toString()));
    	member.setStatus(0);
    	memberService.updateStatusForOrg(member);
    	/*发送消息给理财师团队人员，还没处理
    	
    	*/
	}
	public void updateRecoveryTeam(Team team) throws Exception {
		// TODO Auto-generated method stub
		Team updateTeam = new Team();
		updateTeam.setId(team.getId());
		updateTeam.setStatus(1);
    	//更新理财师团队状态
    	this.updateforstatus(updateTeam);
    	//更新理财师团队人员的状态
    	Member member =new Member();
    	member.setTeamID(Integer.valueOf(team.getId().toString()));
    	member.setStatus(2);
    	memberService.updateStatus(member); 
    	/*发送消息给理财师团队人员，还没处理
    	
    	*/
	}
	public void updateCancel(Team team) throws Exception {
		Team updateTeam = new Team();
		updateTeam.setId(team.getId());
		updateTeam.setStatus(3);
    	//更新团队状态
    	this.updateforstatus(updateTeam);
    	Member member2 = new Member();
    	if (team.getId()!=null){
    		member2.setTeamID(Integer.valueOf(team.getId().toString()));
    	}
    	List<Member> memberList = memberService.selectteamOrOrgmember(member2);
    	Member member = new Member();
    	if (team.getId()!=null){
    		member.setTeamID(Integer.valueOf(team.getId().toString()));
    	}
    	memberService.updateTeamId(member);
    	//更新理财师申请机构表状态
    	MemberApplication MemberApplication = new MemberApplication();
    	MemberApplication.setTeamID(Integer.valueOf(team.getId().toString()));
    	memberApplicationService.update(MemberApplication);
    	//更新其他的申请中的为不通过
    	TeamOrOrgApplication teamOrOrgApplication = new TeamOrOrgApplication();
    	teamOrOrgApplication.setStatus(2);
    	teamOrOrgApplication.setTeamID(Integer.valueOf(team.getId().toString()));
    	teamOrOrgApplicationService.updateForCancel(teamOrOrgApplication);
    	//更新理财师组织经历表状态
    	MemberTeamOrOrgHistory memberTeamOrOrgHistory = new MemberTeamOrOrgHistory();
    	memberTeamOrOrgHistory.setTeamOrOrgID(Integer.valueOf(team.getId().toString()));
    	memberTeamOrOrgHistory.setStatus(5);
    	memberTeamOrOrgHistoryService.update(memberTeamOrOrgHistory);
    	//消息测试 begin
		SendMessage sendMessage = new SendMessage();
		User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Map parameterMap = new HashMap();
    	List<UserMessage> list = new ArrayList();
		sendMessage = new SendMessage();
		for(Member mem:memberList){
			UserMessage us = new UserMessage();
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
		sendMessage.setTemplateCode("sys_manage_team_htzjzxtd");
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
	public void saveApplyAgree(Team team) throws Exception {
		Team updateTeam = new Team();
		TeamOrOrgApplication teamOrOrgApplication =  new TeamOrOrgApplication();
		Member member2 = new Member();
		List<Member> memberList=null;
		Map parameterMap = new HashMap();
		SendMessage sendMessage = new SendMessage();
		UserMessage us = new UserMessage();
		List<UserMessage> list = new ArrayList();
		User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		switch (team.getCheckType())
		{
		case 1://团队创建申请
		    	//更新理财师团队状态
		    	
				updateTeam.setId(team.getId());
				updateTeam.setStatus(1); 
		    	updateforstatus(updateTeam);
		    	//更新团队申请表状态
		    	
		    	teamOrOrgApplication.setId(team.getCheckId());
		    	teamOrOrgApplication.setStatus(1);
		    	teamOrOrgApplication.setApplicantFeedback(team.getApplicantFeedback());
		    	teamOrOrgApplicationService.update(teamOrOrgApplication);
		    	//更新理财师组织经历表状态
		    	MemberTeamOrOrgHistory memberTeamOrOrgHistory = new MemberTeamOrOrgHistory();
		    	memberTeamOrOrgHistory.setTeamOrOrgID(Integer.valueOf(team.getId().toString()));
		    	memberTeamOrOrgHistory.setStatus(2);
		    	memberTeamOrOrgHistoryService.updateForStatus(memberTeamOrOrgHistory);
		    	//消息测试 begin
			    parameterMap = new HashMap();
				sendMessage = new SendMessage();
				us = new UserMessage();
				us.setUserID((long)team.getApplicationMember());
				us.setUserType((short)3);
				list = new ArrayList();
				list.add(us);
				sendMessage.setUserList(list);
				//设置发送方式
				sendMessage.setSendWayStr("1");
				//设置模板参数
				sendMessage.setTemplateParameters(parameterMap);
				//设置模板code
				sendMessage.setTemplateCode("sys_manage_team_tdcjtg");
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
		case 2://团队管理人变更申请
		    	//更新理财师状态 
		    	Member member = new Member();
		    	member.setId(Long.valueOf(team.getMemberID()));
		    	member.setType(2);
		    	//更新申请人为团队管理者
		    	memberService.update(member);
		    	member = new Member();
		    	member.setId(Long.valueOf(team.getApplicationMember()));
		    	member.setType(1);
		    	memberService.update(member);
		    	//更新团队申请表状态
		    	
		    	teamOrOrgApplication.setId(team.getCheckId());
		    	teamOrOrgApplication.setStatus(1);
		    	teamOrOrgApplication.setApplicantFeedback(team.getApplicantFeedback());
		    	teamOrOrgApplicationService.update(teamOrOrgApplication);
		    	//消息测试 begin
		    	member2 = new Member();
		    	if (team.getId()!=null){
		    		member2.setTeamID(Integer.valueOf(team.getId().toString()));
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
				sendMessage.setTemplateCode("sys_manage_team_tdglrbgtg");
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
		case 5://团队升级申请
			//更改机构表晋升时间
			MemberOrganization memberOrganization =new MemberOrganization();
			memberOrganization.setHistoryTeamID(team.getId().intValue());
			memberOrganizationService.update_for_promotionTtime(memberOrganization);
			MemberOrganization newOrganization = new MemberOrganization();
			newOrganization.setId(team.getrOrgId().longValue());
			newOrganization.setStatus(1);
			memberOrganizationService.updateforstatus(newOrganization);
			//更新团队下面的理财师到机构下
			MemberOrganization promoteOrganization =memberOrganizationService.getOneForPromote(memberOrganization);
			Member pmember = new Member();
			pmember.setTeamID(promoteOrganization.getHistoryTeamID());
			pmember.setOrgID(promoteOrganization.getCheckId());
			memberService.updateTeamToOrg(pmember);
			//更新团队的状态为不启用
			Team newteam =new Team();
			newteam.setId(team.getId());
			update_for_isAvailable(newteam);
			//更新团队申请表状态
	    	teamOrOrgApplication.setId(team.getCheckId());
	    	teamOrOrgApplication.setStatus(1);
	    	teamOrOrgApplication.setApplicantFeedback(team.getApplicantFeedback());
	    	teamOrOrgApplicationService.update(teamOrOrgApplication);
	    	member2 = new Member();
	    	if (team.getId()!=null){
	    		member2.setTeamID((int)pmember.getOrgID());
	    	}
	    	memberList = memberService.selectteamOrOrgmember(member2);
	    	//如果团队升级通过，那么团队下所有人原团队记录变成离职
	    	memberTeamOrOrgHistory = new MemberTeamOrOrgHistory();
	    	memberTeamOrOrgHistory.setTeamOrOrgID(Integer.valueOf(team.getId().toString()));
	    	memberTeamOrOrgHistory.setStatus(5);
	    	memberTeamOrOrgHistoryService.update(memberTeamOrOrgHistory);
	    	//如果团队升级通过，那么团度下面所有人插入在职记录
	    	for(Member mem:memberList){
	    	    memberTeamOrOrgHistory = new MemberTeamOrOrgHistory();
	    		memberTeamOrOrgHistory.setTeamOrOrgID(Integer.valueOf(team.getrOrgId().toString()));
		    	memberTeamOrOrgHistory.setStatus(2);
		    	memberTeamOrOrgHistory.setType(1);
		    	if (mem.getType()==2){
		    		memberTeamOrOrgHistory.setType(2);
		    	}
		    	memberTeamOrOrgHistory.setMemberID(Integer.valueOf(mem.getId().toString()));
		    	memberTeamOrOrgHistoryService.save(memberTeamOrOrgHistory);
			}
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
			sendMessage.setTemplateCode("sys_manage_team_tdsjtg");
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
			/*
			 * 关于团队的其他信息的平移还没有做
			 */
			
			
	    	
			break;
		case 4:
				//是否需要进行注销前置条件查询？
				member2 = new Member();
		    	if (team.getId()!=null){
		    		member2.setTeamID(Integer.valueOf(team.getId().toString()));
		    	}
				memberList = memberService.selectteamOrOrgmember(member2);
		    	//更新理财师团队状态
				updateTeam.setId(team.getId());
				updateTeam.setStatus(3); 
		    	updateforstatus(updateTeam);
		    	//更新理财师状态为个人状态,已完成目前注销方便测试
		    	Member member1 = new Member();
		    	member1.setTeamID(Integer.valueOf(team.getId().toString()));
		    	memberService.updateTeamId(member1);
		    	//更新团队申请表状态
		    	teamOrOrgApplication.setId(team.getCheckId());
		    	teamOrOrgApplication.setStatus(1);
		    	teamOrOrgApplication.setApplicantFeedback(team.getApplicantFeedback());
		    	teamOrOrgApplicationService.update(teamOrOrgApplication);
		    	//更新其他的申请中的为不通过
		    	teamOrOrgApplication = new TeamOrOrgApplication();
		    	teamOrOrgApplication.setStatus(2);
		    	teamOrOrgApplication.setTeamID(Integer.valueOf(team.getId().toString()));
		    	teamOrOrgApplicationService.updateForCancel(teamOrOrgApplication);
		    	//更新理财师申请机构表状态
		    	MemberApplication MemberApplication = new MemberApplication();
		    	MemberApplication.setTeamID(Integer.valueOf(team.getId().toString()));
		    	memberApplicationService.update(MemberApplication);
		    	//更新理财师组织经历表状态
		    	memberTeamOrOrgHistory = new MemberTeamOrOrgHistory();
		    	memberTeamOrOrgHistory.setTeamOrOrgID(Integer.valueOf(team.getId().toString()));
		    	memberTeamOrOrgHistory.setStatus(5);
		    	memberTeamOrOrgHistoryService.update(memberTeamOrOrgHistory);
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
				sendMessage.setTemplateCode("sys_manage_team_tdzxtg");
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
	public void saveApplyBack(Team team) throws Exception {
		Team updateTeam = new Team();
		TeamOrOrgApplication teamOrOrgApplication =  new TeamOrOrgApplication();
		Map parameterMap = new HashMap();
		SendMessage sendMessage = new SendMessage();
		UserMessage us = new UserMessage();
		List<UserMessage> list = new ArrayList();
		User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		switch (team.getCheckType())
		{
		case 1://团队创建申请退回
		    	//更新理财师团队状态
				updateTeam.setId(team.getId());
				updateTeam.setStatus(4); 
		    	updateforstatus(updateTeam);
		    	//更新团队申请表状态
		    	teamOrOrgApplication.setId(team.getCheckId());
		    	teamOrOrgApplication.setStatus(2);
		    	teamOrOrgApplication.setApplicantFeedback(team.getApplicantFeedback());
		    	teamOrOrgApplicationService.update(teamOrOrgApplication);
		    	//更新理财师组织经历表状态
		    	MemberTeamOrOrgHistory memberTeamOrOrgHistory = new MemberTeamOrOrgHistory();
		    	memberTeamOrOrgHistory.setTeamOrOrgID(Integer.valueOf(team.getId().toString()));
		    	memberTeamOrOrgHistory.setStatus(3);
		    	memberTeamOrOrgHistoryService.updateForStatus(memberTeamOrOrgHistory);
		    	//消息测试 begin
			    parameterMap = new HashMap();
				sendMessage = new SendMessage();
				us = new UserMessage();
				us.setUserID((long)team.getApplicationMember());
				us.setUserType((short)3);
				list = new ArrayList();
				list.add(us);
				sendMessage.setUserList(list);
				//设置发送方式
				sendMessage.setSendWayStr("1");
				//设置模板参数
				sendMessage.setTemplateParameters(parameterMap);
				//设置模板code
				sendMessage.setTemplateCode("sys_manage_team_tdcjbtg");
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
		case 2://团队管理人变更申请退回
		    	//更新团队申请表状态
		    	teamOrOrgApplication.setId(team.getCheckId());
		    	teamOrOrgApplication.setStatus(2);
		    	teamOrOrgApplication.setApplicantFeedback(team.getApplicantFeedback());
		    	teamOrOrgApplicationService.update(teamOrOrgApplication);
		    	//消息测试 begin
			    parameterMap = new HashMap();
				sendMessage = new SendMessage();
				us = new UserMessage();
				us.setUserID((long)team.getApplicationMember());
				us.setUserType((short)3);
				list = new ArrayList();
				list.add(us);
				sendMessage.setUserList(list);
				//设置发送方式
				sendMessage.setSendWayStr("1");
				//设置模板参数
				sendMessage.setTemplateParameters(parameterMap);
				//设置模板code
				sendMessage.setTemplateCode("sys_manage_team_tdglrbgbtg");
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
		case 5://团队升级申请退回
	    		//更新理财师同意升级的状态到原始状态
	    		Member member = new Member();
		    	member.setTeamID(Integer.valueOf(team.getId().toString()));
		    	member.setAgreeStatus(0);
		    	memberService.update(member);
		    	//删除要升级的机构信息
		    	MemberOrganization promoteOrganization = new MemberOrganization();
		    	promoteOrganization.setId(team.getOrgId().longValue());
		    	memberOrganizationService.delete_for_organization(promoteOrganization);
		    	promoteOrganization = new MemberOrganization();
		    	promoteOrganization.setId(team.getrOrgId().longValue());
		    	memberOrganizationService.delete_for_teamToOrg(promoteOrganization);
	    		//更新团队申请表状态
	    		teamOrOrgApplication.setId(team.getCheckId());
		    	teamOrOrgApplication.setStatus(2);
		    	teamOrOrgApplication.setApplicantFeedback(team.getApplicantFeedback());
		    	teamOrOrgApplicationService.update(teamOrOrgApplication);
		    	//消息测试 begin
			    parameterMap = new HashMap();
				sendMessage = new SendMessage();
				us = new UserMessage();
				us.setUserID((long)team.getApplicationMember());
				us.setUserType((short)3);
				list = new ArrayList();
				list.add(us);
				sendMessage.setUserList(list);
				//设置发送方式
				sendMessage.setSendWayStr("1");
				//设置模板参数
				sendMessage.setTemplateParameters(parameterMap);
				//设置模板code
				sendMessage.setTemplateCode("sys_manage_team_tdsjbtg");
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
		case 4://团队注销申请退回
				//是否需要进行注销前置条件查询？
				
		    	//更新团队申请表状态
		    	teamOrOrgApplication.setId(team.getCheckId());
		    	teamOrOrgApplication.setStatus(2);
		    	teamOrOrgApplication.setApplicantFeedback(team.getApplicantFeedback());
		    	teamOrOrgApplicationService.update(teamOrOrgApplication);
		    	//消息测试 begin
			    parameterMap = new HashMap();
				sendMessage = new SendMessage();
				us = new UserMessage();
				us.setUserID((long)team.getApplicationMember());
				us.setUserType((short)3);
				list = new ArrayList();
				list.add(us);
				sendMessage.setUserList(list);
				//设置发送方式
				sendMessage.setSendWayStr("1");
				//设置模板参数
				sendMessage.setTemplateParameters(parameterMap);
				//设置模板code
				sendMessage.setTemplateCode("sys_manage_team_tdzxbtg");
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
	//获取所有团队
	@SuppressWarnings("unchecked")
	public List<Team> getAllTeam(Team entity) throws Exception {
		return (List<Team>) super.getRelationDao().selectList("team.select_team", entity);
	}
	
	@SuppressWarnings("unchecked")
	public List<Long> getTeamIds(Team entity) throws Exception {
		return (List<Long>)super.getRelationDao().selectList("team.select_teamId", entity);
	}
	
	@Override
	public void delete(Team entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByArr(Team entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object download(Team entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Team> getList(Team entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Team getOne(Team entity) throws Exception {
		return (Team) super.getRelationDao().selectOne("team.select_for_check", entity);
	}
    //理财师团队的分页查询
	@SuppressWarnings("unchecked")
	@Override
	public List<Team> getPaginatedList(Team entity) throws Exception {
		return (List<Team>) super.getRelationDao().selectList("team.select_paginated", entity);
	}
	//理财师团队的统计
	@Override
	public Long getRecordCount(Team entity) throws Exception {
		return (Long) super.getRelationDao().selectOne("team.count", entity);
	}

	@Override
	public void save(Team entity) throws Exception {
		
	}
    //理财师团队更新
	@Override
	public void update(Team entity) throws Exception {
		super.getRelationDao().update("team.update", entity);	
	}
	public void updateforstatus(Team entity) throws Exception {
		super.getRelationDao().update("team.update_for_status", entity);	
	}
	public void update_for_isAvailable(Team entity) throws Exception {
		super.getRelationDao().update("team.update_for_isAvailable", entity);	
	}

	@Override
	public String upload(Team entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}

	
}