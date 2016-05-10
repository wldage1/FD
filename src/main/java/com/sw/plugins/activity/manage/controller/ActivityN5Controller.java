package com.sw.plugins.activity.manage.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.sw.plugins.activity.manage.entity.Activity;
import com.sw.plugins.activity.manage.entity.ActivityRecord;
import com.sw.plugins.activity.manage.service.ActivityRecordN5Service;
import com.sw.plugins.activity.manage.service.ActivityService;

/**
 * N5活动控制器
 * 
 * @author Erun
 */
@Controller
public class ActivityN5Controller extends BaseController<Activity>{
	
	private static Logger logger = Logger.getLogger(ActivityN5Controller.class);
	
	@Resource
	private ActivityService activityService;
	@Resource
	private ActivityRecordN5Service activityRecordN5Service;
	
	/**
	 * N5活动统计
	 * 
	 * @param activity
	 * @param request
	 * @return
	 */
	@RequestMapping("/activity/manage/statistics")
	public CommonModelAndView statistics(ActivityRecord activityRecord, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, activityRecord, messageSource);
		try {
			if(activityRecordN5Service.checkTime(activityRecord) == 1){
				activityRecordN5Service.countN4N5(activityRecord);
			}
			activityRecord.setActivityName(new String(activityRecord.getActivityName().getBytes("ISO-8859-1"),"utf-8"));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		commonModelAndView.addObject("activityRecord", activityRecord);
		return commonModelAndView;
	}
	
	/**
	 * 获取N5活动统计集合，返回json
	 * 
	 * @param activityRecord
	 * @param request
	 * @return
	 */
	@RequestMapping("/activity/manage/statistics/gridN5")
	public CommonModelAndView json(ActivityRecord activityRecord, HttpServletRequest request){
		Map<String, Object> map = null;
		try {
			map = activityRecordN5Service.grid(activityRecord);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new CommonModelAndView("jsonView", map, activityRecord, request);
	}
	
	/**
	 * 理财师关注微信
	 * 
	 * @param activityRecord
	 * @param request
	 * @return
	 */
	@RequestMapping("/activity/manage/statistics/follow")
	public CommonModelAndView follow(ActivityRecord activityRecord, HttpServletRequest request) {
		String viewName = null;
		try {
			activityRecord.setReserved26(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			activityRecordN5Service.update(activityRecord);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, activityRecord, request, messageSource);
		return commonModelAndView;
	}
	
	/**
	 * 理财师奖金发放
	 * 
	 * @param activityRecord
	 * @param request
	 * @return
	 */
	@RequestMapping("/activity/manage/statistics/issue")
	public CommonModelAndView issue(ActivityRecord activityRecord, HttpServletRequest request) {
		String viewName = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String issueTime = formatter.format(new Date());
		activityRecord.setReserved32(issueTime);
		if(activityRecord.getReserved36() != null){
			activityRecord.setReserved11(issueTime);
		}
		if(activityRecord.getReserved37() != null){
			activityRecord.setReserved12(issueTime);
		}
		if(activityRecord.getReserved38() != null){
			activityRecord.setReserved13(issueTime);
		}
		if(activityRecord.getReserved39() != null){
			activityRecord.setReserved14(issueTime);
		}
		if(activityRecord.getReserved40() != null){
			activityRecord.setReserved15(issueTime);
		}
		try {
			activityRecordN5Service.update(activityRecord);
			activityRecordN5Service.sendMessage(activityRecord);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, activityRecord, request, messageSource);
		return commonModelAndView;
	}
	
	@RequestMapping("/activity/manage/statistics/approve")
	public CommonModelAndView approve(ActivityRecord activityRecord, HttpServletRequest request) {
		String viewName = null;
		try {
			activityRecordN5Service.update(activityRecord);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, activityRecord, request, messageSource);
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

	@Override
	public CommonModelAndView valid(Activity entity, BindingResult result,
			Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
