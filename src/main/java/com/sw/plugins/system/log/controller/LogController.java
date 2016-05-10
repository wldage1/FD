package com.sw.plugins.system.log.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sw.core.controller.BaseController;
import com.sw.core.controller.CommonModelAndView;
import com.sw.core.exception.DetailException;
import com.sw.plugins.system.log.entity.Log;
import com.sw.plugins.system.log.service.LogService;

/**
 * 控制器，进行日志信息维护
 * 
 * @author Administrator
 * 
 */
@Controller
public class LogController extends BaseController<Log> {

	private static Logger logger = Logger.getLogger(LogController.class);

	@Resource
	private LogService logService;

	/**
	 * 日志列表方法
	 * 
	 * @param code
	 * @param role
	 * @param request
	 * @return
	 */
	@RequestMapping("/system/log")
	public CommonModelAndView list(Log log, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, log,messageSource);
		commonModelAndView.addObject("code", log.getC());
		commonModelAndView.addObject("log", log);
		return commonModelAndView;
	}

	/**
	 * 日志删除功能，json格式
	 * 
	 * @param role
	 * @param request
	 * @return
	 */
	@RequestMapping("/system/log/delete")
	public CommonModelAndView delete(Log log, HttpServletRequest request) {
		// 视图名
		String viewName = null;
		try {
			if (log != null && log.getId() != null) {
				logService.delete(log);
			} else if (log != null && log.getIds() != null) {
				logService.deleteByArr(log);
			}
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, log, request,messageSource);
		return commonModelAndView;
	}

	/**
	 * 查询日志信息 返回json格式
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/system/log/grid")
	public CommonModelAndView json(Log log, String nodeid, HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			map = logService.getGrid(log);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}

	@Override
	public String uploadfile(Log entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String downloadfile(Log entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(Log entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView valid(@Valid Log entity, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
}
