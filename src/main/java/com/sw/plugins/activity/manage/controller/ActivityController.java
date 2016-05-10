package com.sw.plugins.activity.manage.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sw.core.controller.BaseController;
import com.sw.core.controller.CommonModelAndView;
import com.sw.plugins.activity.manage.entity.Activity;
import com.sw.plugins.activity.manage.service.ActivityService;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;

/**
 * 活动控制器
 * 
 * @author Erun
 */
@Controller
public class ActivityController extends BaseController<Activity>{
	
	private static Logger logger = Logger.getLogger(ActivityController.class);

	@Resource
	private ActivityService activityService;
	@Resource
	private UserLoginService userLoginService;
	
	/**
	 * 活动中心列表方法
	 * 
	 * @param request
	 * @param activity
	 * @return
	 */
	@RequestMapping("/activity/manage")
	public CommonModelAndView list(Activity activity, HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, activity, messageSource);
		commonModelAndView.addObject("activity", activity);
		List<Map<String, Object>> activityType = this.initData.getActivityType();
		commonModelAndView.addObject("activityType", activityType);
		Map<String, Object> activityStatus = this.initData.getActivityStatus();
		commonModelAndView.addObject("activityStatus", activityStatus);
		return commonModelAndView;
	}
	
	/**
	 * 获取活动集合，返回json
	 * 
	 * @param activity
	 * @param request
	 * @return
	 */
	@RequestMapping("/activity/manage/grid")
	public CommonModelAndView json(Activity activity, HttpServletRequest request){
		Map<String, Object> map = null;
		try {
			map = activityService.getGrid(activity);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new CommonModelAndView("jsonView", map, activity, request);
	}
	
	/**
	 * 活动创建
	 * 
	 * @param activity
	 * @param request
	 * @return
	 */
	@RequestMapping("/activity/manage/create")
	public CommonModelAndView create(Activity activity, HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, activity, messageSource);
		commonModelAndView.addObject("activity", activity);
		List<Map<String, Object>> activityType = this.initData.getActivityType();
		commonModelAndView.addObject("activityType", activityType);
		return commonModelAndView;
	}
	
	/**
	 * 活动修改
	 * 
	 * @param activity
	 * @param request
	 * @return
	 */
	@RequestMapping("/activity/manage/modify")
	public CommonModelAndView modify(Activity activity, HttpServletRequest request, Map<String,Object> model){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, activity, messageSource);
		String code = activity.getC();
		try {
			activity = activityService.getOne(activity);
			activity.setC(code);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		commonModelAndView.addObject("activity", activity);
		model.put("activity", activity);
		List<Map<String, Object>> activityType = this.initData.getActivityType();
		commonModelAndView.addObject("activityType", activityType);
		return commonModelAndView;
	}
	
	/**
	 * 活动保存
	 * 
	 * @param activity
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/activity/manage/save", method = RequestMethod.POST)
	public CommonModelAndView save(Activity activity, HttpServletRequest request) {
		String viewName = null;
		String code = activity.getC();
		try {
			if(activity.getId() == null){
				User user = userLoginService.getCurrLoginUser();
				activity.setCreatorUserId(user.getId().toString());
				activityService.save(activity);
			}else{
				activityService.update(activity);
			}
			viewName = this.SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			viewName = this.ERROR;
		}
		activity.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, activity, request, messageSource);
		return commonModelAndView;
	}
	
	/**
	 * 活动删除
	 * 
	 * @param activity
	 * @param request
	 * @return
	 */
	@RequestMapping("/activity/manage/delete")
	public CommonModelAndView delete(Activity activity, HttpServletRequest request) {
		String viewName = null;
		try {
			if(activity.getIds() != null){
					activityService.deleteByArr(activity);
			}else if(activity.getId() != null){
				activityService.delete(activity);
			}
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, activity, request, messageSource);
		return commonModelAndView;
	}
	
	/**
	 * 开始或结束活动
	 * 
	 * @param activity
	 * @param request
	 * @return
	 */
	@RequestMapping("/activity/manage/switch")
	public CommonModelAndView switched(Activity activity, HttpServletRequest request) {
		String viewName = null;
		try {
			activityService.update(activity);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, activity, request, messageSource);
		return commonModelAndView;
	}
	
	@Override
	public String uploadfile(Activity entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String downloadfile(Activity entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(Activity entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 气泡验证
	 */
	@Override
	@RequestMapping("/activity/manage/valid")
	public CommonModelAndView valid(@Valid Activity entity, BindingResult result,
			Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) {
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		if (result.hasErrors()) {
			commonModelAndView.addBindingErrors(model, result, messageSource);
			return commonModelAndView;
		}
		return commonModelAndView;
	}

}
