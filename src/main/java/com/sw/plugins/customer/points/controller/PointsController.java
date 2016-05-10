package com.sw.plugins.customer.points.controller;

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
import com.sw.core.exception.DetailException;
import com.sw.plugins.customer.points.entity.Points;
import com.sw.plugins.customer.points.service.PointsService;

/**
 * 积分管理控制器，查询，新增，更新，删除等功能
 * 
 * @author Administrator
 */
@Controller
public class PointsController extends BaseController<Points> {


	private static Logger logger = Logger.getLogger(PointsController.class);

	@Resource
	private PointsService pointsService;

	/**
	 * 积分管理列表方法
	 * 
	 * @param code
	 * @param role
	 * @param request
	 * @return
	 */
	@RequestMapping("/customer/points")
	public CommonModelAndView list(Points config, HttpServletRequest request)throws Exception {
		
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, config, messageSource);
		commonModelAndView.addObject("points", config);
		return commonModelAndView;
	}

	/**
	 * 跳转积分管理页面
	 * 
	 * @param code
	 * @param role
	 * @param request
	 * @return
	 * @author
	 */
	@RequestMapping("/customer/points/create")
	public CommonModelAndView create(Points pointsconfig, BindingResult result, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, pointsconfig, messageSource);
		commonModelAndView.addObject("pointsconfig", pointsconfig);
		return commonModelAndView;
	}

	/**
	 * 跳转到积分管理修改页面
	 * 
	 * @param code
	 * @param role
	 * @param request
	 * @return
	 */
	@RequestMapping("/customer/points/modify")
	public CommonModelAndView modify(Points pointsconfig, HttpServletRequest request) {
		String code = pointsconfig.getC();
		if (pointsconfig.getId() != null) {
			try {
				pointsconfig = (Points) pointsService.getOne(pointsconfig);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		pointsconfig.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, pointsconfig, messageSource);
		commonModelAndView.addObject("pointsconfig", pointsconfig);
		return commonModelAndView;
	}

	
	/**
	 * 积分管理创建操作
	 * 
	 * @param role
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/customer/points/save", method = RequestMethod.POST)
	public CommonModelAndView save(Points config, HttpServletRequest request) {
		// 视图名
		String viewName = null;
		try {
			pointsService.saveOrUpdate(config);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, config,request, messageSource);
		return commonModelAndView;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/customer/points/grid")
	public CommonModelAndView json(Points config, HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			Object obj = pointsService.getGrid(config);
			map = obj == null ? null : (Map<String, Object>) obj;
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, config, request);
	}

	/**
	 * 积分管理删除功能，json格式
	 * 
	 * @param role
	 * @param request
	 * @return
	 */
	@RequestMapping("/customer/points/delete")
	public CommonModelAndView delete(Points user, HttpServletRequest request, Map<String, Object> model) {
		// 视图名
		String viewName = this.ERROR;
		try {
			if (user != null && user.getId() != null) {
				pointsService.delete(user);
				viewName = this.SUCCESS;
			} else if (user != null && user.getIds() != null) {
				pointsService.deleteByArr(user);
				viewName = this.SUCCESS;
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView(viewName, user,request, messageSource);
	}

	@Override
	public String uploadfile(Points entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String downloadfile(Points entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(Points entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@RequestMapping("/customer/points/valid")
	public CommonModelAndView valid(@Valid Points entity, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		String operate = (entity.getOperate() == null || entity.getOperate().equals("")) ? "0" : entity.getOperate();
		int operateTemp = Integer.parseInt(operate);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		try {
			switch (operateTemp) {
				case 1: {
					// 积分代码是否唯一
					if (entity.getCode()!= null && entity.getCode().length() > 0) {
						Long count = 0l;
						try {
							count = (Long) pointsService.checkConfigDuplication(entity);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							logger.error(DetailException.expDetail(e, getClass())); 
						}
						if (count != 0) {
							result.rejectValue("code", "Duplicate");
						}
					}
					if (result.hasErrors()) {
						commonModelAndView.addBindingErrors(model, result, messageSource);
						return commonModelAndView;
					}
					break;
				}
				case 2: {
					// 积分代码是否唯一
					if (entity.getCode()!= null && entity.getCode().length() > 0) {
						Long count = 0l;
						try {
							count = (Long) pointsService.checkConfigDuplication(entity);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							logger.error(DetailException.expDetail(e, getClass())); 
						}
						if (count != 0) {
							result.rejectValue("code", "Duplicate");
						}
					}
					if (result.hasErrors()) {
						commonModelAndView.addBindingErrors(model, result, messageSource);
						return commonModelAndView;
					}
					break;
				}
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return commonModelAndView;
	}
	
}
