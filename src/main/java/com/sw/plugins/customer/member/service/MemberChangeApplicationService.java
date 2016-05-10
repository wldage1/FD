package com.sw.plugins.customer.member.service;


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
import com.sw.plugins.customer.member.entity.MemberChangeApplication;

import com.sw.plugins.message.messageinterface.service.CommonMessageService;
import com.sw.plugins.message.task.entity.SendMessage;
import com.sw.plugins.message.task.entity.UserMessage;
import com.sw.plugins.system.user.entity.User;

/**
 * Service实现类 -
 */

public class MemberChangeApplicationService extends CommonService<MemberChangeApplication> {
	@Resource
	private MemberService memberService;
	@Resource
	private CommonMessageService commonMessageService;
	@Override
	public void delete(MemberChangeApplication entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByArr(MemberChangeApplication entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object download(MemberChangeApplication entity,
			HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获取理财师的MAP集合
	 */
	public Map<String, Object> getGrid(MemberChangeApplication entity) throws Exception {
		List<MemberChangeApplication> resultList = null;
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
	public void saveApplyBack(Member member) throws Exception {
		Member updateMember = new Member();
		MemberChangeApplication memberChangeApplication =  new MemberChangeApplication();
		Map<String,Object> parameterMap = new HashMap<String,Object>();
		SendMessage sendMessage = new SendMessage();
		UserMessage us = new UserMessage();
		List<UserMessage> list = new ArrayList<UserMessage>();
		User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		switch (member.getCheckType())
		{
			case 1://理财师认证申请退回
			    	//更新理财师状态
					updateMember.setId(member.getId());
					updateMember.setStatus(5); 
					memberService.update(updateMember);
			    	//更新理财师变更申请表状态
					String fb = member.getApplicantFeedback();
					memberChangeApplication.setId(member.getCheckId());
					memberChangeApplication.setStatus(2);
					memberChangeApplication.setApplicantFeedback(fb);
					update(memberChangeApplication);
					//消息测试 begin
				    parameterMap = new HashMap<String,Object>();
				    parameterMap.put("re",fb);
					sendMessage = new SendMessage();
					us = new UserMessage();
					us.setUserID(member.getId());
					us.setUserType((short)3);
					list = new ArrayList<UserMessage>();
					list.add(us);
					sendMessage.setUserList(list);
					//设置发送方式
					sendMessage.setSendWayStr("1");
					//设置模板参数
					sendMessage.setTemplateParameters(parameterMap);
					//设置模板code
					sendMessage.setTemplateCode("sys_manage_customer_rzbtg");
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
			case 2://理财师注销申请退回
			    	//更新理财师变更申请表状态
			    	memberChangeApplication.setId(member.getCheckId());
			    	memberChangeApplication.setStatus(2);
			    	memberChangeApplication.setApplicantFeedback(member.getApplicantFeedback());
			    	update(memberChangeApplication);
					//消息测试 begin
				    parameterMap = new HashMap();
					sendMessage = new SendMessage();
					us = new UserMessage();
					us.setUserID(member.getId());
					us.setUserType((short)3);
					list = new ArrayList();
					list.add(us);
					sendMessage.setUserList(list);
					//设置发送方式 
					sendMessage.setSendWayStr("1");
					//设置模板参数
					sendMessage.setTemplateParameters(parameterMap);
					//设置模板code 
					sendMessage.setTemplateCode("sys_manage_customer_lcszxbtg");
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
	public void saveApplyAgree(Member member) throws Exception {
		Member updateMember = new Member();
		MemberChangeApplication memberChangeApplication =  new MemberChangeApplication();
		Map parameterMap = new HashMap();
		SendMessage sendMessage = new SendMessage();
		UserMessage us = new UserMessage();
		List<UserMessage> list = new ArrayList();
		User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		switch (member.getCheckType())
		{
		case 1://理财师认证申请
		    	//更新理财师状态
				updateMember.setId(member.getId());
				updateMember.setStatus(2); 
				memberService.update(updateMember);
		    	//更新理财师变更申请表状态
		    	
				memberChangeApplication.setId(member.getCheckId());
				memberChangeApplication.setStatus(1);
				memberChangeApplication.setApplicantFeedback(member.getApplicantFeedback());
				update(memberChangeApplication);
				//消息测试 begin
				parameterMap = new HashMap();
				sendMessage = new SendMessage();
				us = new UserMessage();
				us.setUserID(member.getId());
				us.setUserType((short)3);
				list = new ArrayList();
				list.add(us);
				sendMessage.setUserList(list);
				//设置发送方式
				sendMessage.setSendWayStr("1");
				//设置模板参数
				sendMessage.setTemplateParameters(parameterMap);
				//设置模板code
				sendMessage.setTemplateCode("sys_manage_customer_rztg");
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
		case 2://理财师注销申请
		    	//更新理财师状态 
		        updateMember = new Member();
		        updateMember.setId(Long.valueOf(member.getId()));
		        updateMember.setStatus(4);
		        memberService.update(updateMember);
		    	//更新理财师变更申请表状态
		    	
		    	memberChangeApplication.setId(member.getCheckId());
		    	memberChangeApplication.setStatus(1);
		    	memberChangeApplication.setApplicantFeedback(member.getApplicantFeedback());
		    	update(memberChangeApplication);
		    	//更新理财师变更申请表其他申请为不通过状态
		    	
		    	memberChangeApplication.setMemberID(Integer.valueOf(member.getId().toString()));
		    	memberChangeApplication.setStatus(2);
		    	updateForCan(memberChangeApplication);
		    	
			break;
		default:break;
		}
	}
	@Override
	public List<MemberChangeApplication> getList(MemberChangeApplication entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MemberChangeApplication getOne(MemberChangeApplication entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MemberChangeApplication> getPaginatedList(
			MemberChangeApplication entity) throws Exception {
		return (List<MemberChangeApplication>) super.getRelationDao().selectList("memberChangeApplication.select_paginated", entity);
	}

	@Override
	public Long getRecordCount(MemberChangeApplication entity) throws Exception {
		return (Long) super.getRelationDao().selectOne("memberChangeApplication.count", entity);
	}

	@Override
	public void save(MemberChangeApplication entity) throws Exception {
		
	}
    //理财师变更申请更改
	@Override
	public void update(MemberChangeApplication entity) throws Exception {
		super.getRelationDao().update("memberChangeApplication.update", entity);
	}
	public void updateForCan(MemberChangeApplication entity) throws Exception {
		super.getRelationDao().update("memberChangeApplication.updateForCan", entity);
	}
	@Override
	public String upload(MemberChangeApplication entity,
			HttpServletRequest request) throws Exception {
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		
	}
		
}