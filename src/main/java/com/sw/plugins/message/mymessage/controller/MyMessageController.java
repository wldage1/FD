package com.sw.plugins.message.mymessage.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sw.core.controller.BaseController;
import com.sw.core.controller.CommonModelAndView;
import com.sw.core.exception.DetailException;
import com.sw.plugins.message.task.entity.ReceiverMessage;
import com.sw.plugins.message.manage.service.ReceiverMessageService;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;

@Controller
public class MyMessageController extends BaseController<ReceiverMessage> {

	private static Logger logger = Logger.getLogger(MyMessageController.class);
	
	@Resource
	private ReceiverMessageService receiverMessageService;
	@Resource
	private UserLoginService userLoginService;
	
	/**
	 * 消息管理列表
	 * @param entity
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/message/mymessage")
	public CommonModelAndView list(ReceiverMessage entity, HttpServletRequest request, Map<String, Object> model) {
		Object obj = new CommonModelAndView().getCurrentStatus(entity, request);
		if (obj != null) {
			if (obj instanceof ReceiverMessage) {
				entity = (ReceiverMessage) obj;
			}
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, entity,messageSource);
		commonModelAndView.addObject("code", entity.getC());
		return commonModelAndView;
	}
	
	/**
	 * 查询接收到的消息信息，返回json
	 * @param entity
	 * @param request
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/message/mymessage/grid")
	public CommonModelAndView json(ReceiverMessage entity, HttpServletRequest request, Map<String, Object> model) {
		Object gridJson;
		Map<String, Object> map = null;
		try {
			User user = userLoginService.getCurrLoginUser();
			entity.setReceiverUserID(user.getId());
			gridJson = receiverMessageService.getGrid(entity);
			map = (gridJson == null ? null : (Map<String, Object>) gridJson);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, entity, request);
	}
	
	/**
	 * 消息删除
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping("/message/mymessage/delete")
	public CommonModelAndView delete(ReceiverMessage entity, HttpServletRequest request) {
		// 视图名
		String viewName = null;
		try {
			
			if (entity != null && entity.getId() != null) {
				receiverMessageService.delete(entity);
				viewName = this.SUCCESS;
			} else if (entity != null && entity.getIds() != null) {
				receiverMessageService.deleteByArr(entity);
				viewName = this.SUCCESS;
			}
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, entity, request,messageSource);
		return commonModelAndView;
	}
	
	@RequestMapping("/message/mymessage/update")
	public CommonModelAndView readMessage(ReceiverMessage entity, HttpServletRequest request) {
		// 视图名
		String viewName = null;
		try {
			entity.setIsReaded((short)1);
			entity.setReadTime("now");
			receiverMessageService.update(entity);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, entity, request,messageSource);
		return commonModelAndView;
	}
	
	@Override
	public String uploadfile(ReceiverMessage entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String downloadfile(ReceiverMessage entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(ReceiverMessage entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView valid(ReceiverMessage entity,
			BindingResult result, Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
