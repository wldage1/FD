package com.sw.plugins.message.send.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sw.core.controller.BaseController;
import com.sw.core.controller.CommonModelAndView;
import com.sw.core.exception.DetailException;
import com.sw.plugins.config.area.entity.Area;
import com.sw.plugins.config.area.service.AreaService;
import com.sw.plugins.customer.member.entity.Member;
import com.sw.plugins.customer.member.service.MemberService;
import com.sw.plugins.customer.organization.entity.MemberOrganization;
import com.sw.plugins.customer.organization.service.MemberOrganizationService;
import com.sw.plugins.customer.team.entity.Team;
import com.sw.plugins.customer.team.service.TeamService;
import com.sw.plugins.message.task.entity.MessageTask;
import com.sw.plugins.message.task.entity.SendMessage;
import com.sw.plugins.message.task.service.MessageTaskService;
import com.sw.plugins.message.messageinterface.service.CommonMessageService;
import com.sw.plugins.message.send.service.SendMessageService;

@Controller
public class SendMessageController extends BaseController<SendMessage> {

	private static Logger logger = Logger.getLogger(SendMessageController.class);
	
	@Resource
	private MessageTaskService messageTaskService;
	
	@Resource
	private SendMessageService sendMessageService;
	
	@Resource
	private MemberService memberService;
	
	@Resource
	private TeamService teamService;
	
	@Resource
	private MemberOrganizationService memberOrganizationService;
	
	@Resource
	private AreaService areaService;
	
	/**
	 * 消息发送
	 * @param entity
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/message/send")
	public CommonModelAndView sendMessage(MessageTask entity, HttpServletRequest request, Map<String, Object> model) {
		String code = entity.getC();
		SendMessage sendMessage = new SendMessage();
		Member member = new Member();
		//获取所有团队，机构，省份
		List<Team> teamList = new ArrayList<Team>();
		List<MemberOrganization> organizationList = new ArrayList<MemberOrganization>();
		List<Area> provinceList = new ArrayList<Area>();
		List<MessageTask> messageTaskList = null;
		try {
			//查询自定义任务
			entity.setType((short)2); 
			entity.setStatus((short)2);
			messageTaskList  = messageTaskService.getList(entity);
			//sendMessage.setTaseName(entity.getName());
			teamList = teamService.getAllTeam(new Team());
			organizationList = memberOrganizationService.getAllMemberOrganization(new MemberOrganization());
			Area area = new Area();
			area.setTreeLevel(new Long(1));
			//查询所有省份
			provinceList = areaService.getAllArea(area);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		entity.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, entity,messageSource);
		commonModelAndView.addObject("code", entity.getC());
		commonModelAndView.addObject("sendMessage", sendMessage);
		commonModelAndView.addObject("member", member);
		commonModelAndView.addObject("teamList", teamList);
		//commonModelAndView.addObject("messageTaskID", entity.getId());
		commonModelAndView.addObject("messageTaskList", messageTaskList);
		commonModelAndView.addObject("organizationList", organizationList);
		commonModelAndView.addObject("provinceList", provinceList);
		return commonModelAndView;
	}
	
	/**
	 * 根据省份查询城市
	 * @param id
	 * @return
	 */
	@RequestMapping("/message/send/selectCity")
	public CommonModelAndView selectCity(String id){
	
		Map map = new HashMap();
		try {
			Area area = new Area();
			area.setParentId(Long.parseLong(id));
			area.setTreeLevel(new Long(2));
			List gridJson = areaService.getAllArea(area);
			map.put("cityList", gridJson);
			map.put("status", "success");
		} catch (Exception e) {
			map.put("status", "error");
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 查询用户信息，返回json
	 * @param entity
	 * @param request
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/message/send/selectMember")
	public CommonModelAndView selectMember(Member entity, HttpServletRequest request, Map<String, Object> model) {
		//Object gridJson;
		Map<String, Object> map = null;
		try {
			map = memberService.sendMessageMember(entity);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, entity, request);
	}
	
	
	/**
	 *  发送消息
	 *  @param entity
	 *  @param request
	 *  @return
	 *  @throws Exception
	 *  @author sean
	 *  @version 1.0
	 *  </pre>
	 *  Created on :2013-10-8 下午4:19:34
	 *  LastModified:
	 *  History:
	 *  </pre>
	 */
	@RequestMapping("/message/send/saveSendMessage")
	public CommonModelAndView saveSendMessage(MessageTask entity, HttpServletRequest request) throws Exception{
		Map map = new HashMap();
		String messageTaskID = request.getParameter("messageTaskID");
		String ids = request.getParameter("ids");
		String content = request.getParameter("content");
		map.put("messageTaskID", messageTaskID);
		map.put("ids", ids);
		map.put("content", content);
		sendMessageService.saveSendMessage(map);
		Map returnmap = new HashMap();
		returnmap.put("status", "success");
		return new CommonModelAndView("jsonView",returnmap);
	}
	/**
	 *  根据检索条件发送消息
	 *  @param entity
	 *  @param request
	 *  @return
	 *  @throws Exception
	 *  @author sean
	 *  @version 1.0
	 *  </pre>
	 *  Created on :2013-10-8 下午10:00:21
	 *  LastModified:
	 *  History:
	 *  </pre>
	 */
	@RequestMapping("/message/send/saveSendMessageBySearch")
	public CommonModelAndView saveSendMessageBySearch(MessageTask entity, HttpServletRequest request) throws Exception{
		Map map = new HashMap();
		String messageTaskID = request.getParameter("messageTaskID");
		String searchCondition = request.getParameter("searchCondition");
		String content = request.getParameter("content");
		map.put("messageTaskID", messageTaskID);
		map.put("searchCondition", searchCondition);
		map.put("content", content);
		sendMessageService.saveSendMessageBySearch(map);
		Map returnmap = new HashMap();
		returnmap.put("status", "success");
		return new CommonModelAndView("jsonView",returnmap);
	}
	/**
	 *  发送给所有理财师
	 *  @param entity
	 *  @param request
	 *  @return
	 *  @throws Exception
	 *  @author sean
	 *  @version 1.0
	 *  </pre>
	 *  Created on :2013-10-8 下午11:16:19
	 *  LastModified:
	 *  History:
	 *  </pre>
	 */
	@RequestMapping("/message/send/saveSendMessageAllMemer")
	public CommonModelAndView saveSendMessageAllMemer(MessageTask entity, HttpServletRequest request) throws Exception{
		Map map = new HashMap();
		String messageTaskID = request.getParameter("messageTaskID");
		String content = request.getParameter("content");
		map.put("messageTaskID", messageTaskID);
		map.put("content", content);
		sendMessageService.saveSendMessageAllMemer(map);
		Map returnmap = new HashMap();
		returnmap.put("status", "success");
		return new CommonModelAndView("jsonView",returnmap);
	}
	//查询消息详细
	@RequestMapping("/message/task/sendDetail")
	public CommonModelAndView sendDetail(SendMessage entity, HttpServletRequest request, Map<String, Object> model) {
		Object obj = new CommonModelAndView().getCurrentStatus(entity, request);
		if (obj != null) {
			if (obj instanceof SendMessage) {
				entity = (SendMessage) obj;
			}
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, entity,messageSource);
		commonModelAndView.addObject("code", entity.getC());
	 	commonModelAndView.addObject("sendMessage", entity);
	 	model.put("sendMessage", entity);
		return commonModelAndView;
	}
	
	/**
	 * 查询消息明细
	 * @param entity
	 * @param request
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/message/task/detailGrid")
	public CommonModelAndView detailGrid(SendMessage entity, HttpServletRequest request, Map<String, Object> model) {
		Map<String, Object> map = null;
		try {
			//自定义任务
			map = sendMessageService.getGrid(entity);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, entity, request);
	}
	@Override
	public String uploadfile(SendMessage entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String downloadfile(SendMessage entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(SendMessage entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView valid(SendMessage entity, BindingResult result,
			Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
