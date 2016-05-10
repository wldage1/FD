package com.sw.plugins.message.task.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sw.core.controller.BaseController;
import com.sw.core.controller.CommonModelAndView;
import com.sw.core.exception.DetailException;
import com.sw.plugins.cooperate.provider.entity.ProviderUser;
import com.sw.plugins.cooperate.provider.service.ProviderUserService;
import com.sw.plugins.message.task.entity.MessageReceiverConfig;
import com.sw.plugins.message.task.entity.MessageTask;
import com.sw.plugins.message.task.service.MessageTaskService;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.plugins.system.organization.service.OrganizationService;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;
import com.sw.plugins.system.user.service.UserService;

@Controller
public class MessageTaskController extends BaseController<MessageTask> {
	
	private static Logger logger = Logger.getLogger(MessageTaskController.class);

	@Resource
	private MessageTaskService messageTaskService;
	@Resource
	private UserLoginService userLoginService;
	@Resource
	private ProviderUserService providerUserService;
	@Resource
	private UserService userService;
	@Resource
	private OrganizationService organizationService;
	/**
	 * 消息任务列表
	 * @param entity
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/message/task")
	public CommonModelAndView listTask(MessageTask entity, HttpServletRequest request, Map<String, Object> model) {
		Object obj = new CommonModelAndView().getCurrentStatus(entity, request);
		if (obj != null) {
			if (obj instanceof MessageTask) {
				entity = (MessageTask) obj;
			}
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, entity,messageSource);
		commonModelAndView.addObject("code", entity.getC());
		model.put("messageTask", entity);
		return commonModelAndView;
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/message/task/grid")
	public CommonModelAndView taskGrid(MessageTask entity, HttpServletRequest request, Map<String, Object> model) {
		Object gridJson;
		Map<String, Object> map = null;
		try {
			//自定义任务
			entity.setType(Short.parseShort("2"));
			gridJson = messageTaskService.getGrid(entity);
			map = (gridJson == null ? null : (Map<String, Object>) gridJson);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, entity, request);
	}
	/**
	 * 跳转到创建任务页面
	 * @param entity
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/message/task/create")
	public CommonModelAndView create(MessageTask entity, HttpServletRequest request,Map<String,Object> model) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, entity,messageSource);
		entity.setStatus((short)2);
		entity.setSendType((short)1);
		commonModelAndView.addObject("messageTask", entity);
		model.put("messageTask", entity);
		return commonModelAndView;
	}
	
	/**
	 * 跳转到修改任务页面
	 * @param entity
	 * @param request 
	 * @param model
	 * @return
	 */
	@RequestMapping("/message/task/modify")
	public CommonModelAndView modify(MessageTask entity, HttpServletRequest request,Map<String,Object> model) {
		String code = entity.getC();
		if (entity.getId() != null) {
			try {
				entity = (MessageTask)messageTaskService.getOne(entity);
			} catch (Exception e) {
				logger.error(e.getMessage());
			} 
		}
		entity.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, entity,messageSource);
		commonModelAndView.addObject("messageTask", entity);
		commonModelAndView.addObject("c", entity.getC());
		model.put("messageTask", entity);
		return commonModelAndView;
	}
	
	/**
	 * 消息任务保存
	 * @param entity
	 * @param request
	 * @return	
	 */
	@RequestMapping(value = "/message/task/save", method = RequestMethod.POST)
	public CommonModelAndView save(MessageTask entity, HttpServletRequest request) {
		String viewName = null;
		try {
			//自定义任务
			entity.setType(Short.parseShort("2"));
			messageTaskService.saveOrUpdate(entity);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			entity.setErrorMsg(e.getMessage());
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, entity,request, messageSource);
		return commonModelAndView;
	}
	@RequestMapping(value = "/message/task/delete")
	public CommonModelAndView delete(MessageTask entity,HttpServletRequest request){
		// 视图名
		//String viewName = this.ERROR;
		Map map = new HashMap();
		try {
			if (entity != null && entity.getId() != null) {
				String nodeleteId = messageTaskService.deleteTask(entity);
				map.put("nodeleteids", nodeleteId);
				map.put("status", "success");
				//viewName = this.SUCCESS;
			} else if (entity != null && entity.getIds() != null) {
				String nodeleteIds = messageTaskService.deleteTaskByArr(entity);
				map.put("nodeleteids", nodeleteIds);
				map.put("status", "success");
				//viewName = this.SUCCESS;
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map , entity, request);
	}
	/**
	 * 跳转到自定义消息
	 * @param entity
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/message/task/config")
	public CommonModelAndView list(MessageTask entity, HttpServletRequest request, Map<String, Object> model) {
		Object obj = new CommonModelAndView().getCurrentStatus(entity, request);
		if (obj != null) {
			if (obj instanceof MessageTask) {
				entity = (MessageTask) obj;
			}
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, entity,messageSource);
		commonModelAndView.addObject("code", entity.getC());
		return commonModelAndView;
	}
	
	/**
	 * 查询消息任务信息，返回json
	 * @param entity
	 * @param request
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/message/task/config/grid")
	public CommonModelAndView json(MessageTask entity, HttpServletRequest request, Map<String, Object> model) {
		Object gridJson;
		Map<String, Object> map = null;
		try {
			//内置任务
			entity.setType(Short.parseShort("1"));
			gridJson = messageTaskService.getGrid(entity);
			map = (gridJson == null ? null : (Map<String, Object>) gridJson);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, entity, request);
	}
	
	/**
	 * 消息配置
	 * @param entity
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/message/task/config/messageConfig")
	public CommonModelAndView config(MessageTask entity, HttpServletRequest request,Map<String,Object> model) {
		String code = entity.getC();
		User user = userLoginService.getCurrLoginUser();
		ProviderUser providerUser = new ProviderUser();
		List<ProviderUser> providerUserList=null;
		List<User> userList = null;
		Organization org = new Organization();
		if (entity.getId() != null) {
			try {
				//查询用户是否为居间公司用户
				if(user.getOrgId() != null ){
					org.setId(user.getOrgId());
					org = organizationService.getOne(org);
					user.setIsCommission(org.getIsCommission());
				}
				entity.setType(Short.parseShort("1"));
				entity = (MessageTask)messageTaskService.getOne(entity);
				//居间公司用户
				if(user.getIsCommission() == 1){
					//查询所机构下的居间公司用户
					user.setTaskId(entity.getId());
					userList =  userService.getAllUsers(user);
				}else{//运营用户
					//获取供应商用户集合
					providerUser.setTaskID(entity.getId());
					providerUserList = providerUserService.getList(providerUser);
					//查询所机构下的供应商用户
					user.setTaskId(entity.getId());
					userList =  userService.getAllUsers(user);
					User userCommission = new User();
					userCommission.setTaskId(entity.getId());
					userCommission.setIsCommission(1);
					//查询所有的居间公司用户并与运营人员集合合并
					userList.addAll(userService.getAllUsers(userCommission));
				}
				
		
				
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		entity.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, entity,messageSource);
		commonModelAndView.addObject("messageTask", entity);
		model.put("messageTask", entity);
		commonModelAndView.addObject("c", entity.getC());
		commonModelAndView.addObject("user", user);
		commonModelAndView.addObject("providerUserList", providerUserList);
		commonModelAndView.addObject("userList", userList);
		return commonModelAndView;
	}
	
	/**
	 * 消息配置保存
	 * @param entity
	 * @param request
	 * @return	
	 */
	@RequestMapping(value = "/message/task/config/save", method = RequestMethod.POST)
	public CommonModelAndView saveConfig(MessageTask entity, HttpServletRequest request) {
		String viewName = null;
		//不设置接收用户
		if( entity.getIsConfigReceiver() != null && entity.getIsConfigReceiver() == 1){
			Map map = new HashMap();
			String rolesStr = request.getParameter("receiverRoles");
			String receiverUserIDsJSON = request.getParameter("receiverUserIDsJSON");
			map.put("receiverRoles", rolesStr);
			map.put("receiverUserIDsJSON", receiverUserIDsJSON);
			try {
				messageTaskService.saveMessageTaskAndConfig(entity, map);
				viewName = this.SUCCESS;
			} catch (Exception e) {
				entity.setErrorMsg(e.getMessage());
				viewName = this.ERROR;
				logger.error(e.getMessage());
			}
		}else{
			try {
				messageTaskService.update(entity);
				viewName = this.SUCCESS;
			} catch (Exception e) {
				entity.setErrorMsg(e.getMessage());
				viewName = this.ERROR;
				logger.error(e.getMessage());
			}
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, entity,request, messageSource);
		return commonModelAndView;
	}
	
	@Override
	public String uploadfile(MessageTask entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String downloadfile(MessageTask entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(MessageTask entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 气泡验证
	 */
	@Override
	@RequestMapping("/message/config/valid")
	public CommonModelAndView valid(@Valid MessageTask messageTask, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		if (result.hasErrors()) {
		    commonModelAndView.addBindingErrors(model, result, messageSource);
			return commonModelAndView;
		 }
		return commonModelAndView;
	}
	@RequestMapping("/message/task/valid")
	public CommonModelAndView taskValid(@Valid MessageTask messageTask, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		//自定义任务不需要校验模板相关内容
		if (result.hasErrors()) {
			commonModelAndView.addBindingErrors(model, result, messageSource);
			return commonModelAndView;
		}
		try {
			if(messageTaskService.uniquenessCode(messageTask)){
				result.rejectValue("code", "Duplicate");
				commonModelAndView.addBindingErrors(model, result, messageSource);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return commonModelAndView;
	}

}
