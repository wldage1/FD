package com.sw.plugins.customer.pointsconfig.controller;

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
import com.sw.plugins.customer.pointsconfig.entity.PointsConfig;
import com.sw.plugins.customer.pointsconfig.service.PointsConfigService;

/**
 * 积分配置控制器，查询，新增，更新，删除等功能
 * 
 * @author Administrator
 */
@Controller
public class PointsConfigController extends BaseController<PointsConfig> {


	private static Logger logger = Logger.getLogger(PointsConfigController.class);

	@Resource
	private PointsConfigService configService;

	/**
	 * 积分配置列表方法
	 * 
	 * @param code
	 * @param role
	 * @param request
	 * @return
	 */
	@RequestMapping("/customer/pointsconfig")
	public CommonModelAndView list(PointsConfig config, HttpServletRequest request)throws Exception {
		
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, config, messageSource);
		commonModelAndView.addObject("config", config);
		return commonModelAndView;
	}

	/**
	 * 跳转积分配置页面
	 * 
	 * @param code
	 * @param role
	 * @param request
	 * @return
	 * @author
	 */
	@RequestMapping("/customer/pointsconfig/create")
	public CommonModelAndView create(PointsConfig pointsconfig, BindingResult result, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, pointsconfig, messageSource);
		commonModelAndView.addObject("pointsconfig", pointsconfig);
		return commonModelAndView;
	}

	/**
	 * 跳转到积分配置修改页面
	 * 
	 * @param code
	 * @param role
	 * @param request
	 * @return
	 */
	@RequestMapping("/customer/pointsconfig/modify")
	public CommonModelAndView modify(PointsConfig pointsconfig, HttpServletRequest request) {
		String code = pointsconfig.getC();
		if (pointsconfig.getId() != null) {
			try {
				pointsconfig = (PointsConfig) configService.getOne(pointsconfig);
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
	 * 积分配置创建操作
	 * 
	 * @param role
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/customer/pointsconfig/save", method = RequestMethod.POST)
	public CommonModelAndView save(PointsConfig config, HttpServletRequest request) {
		// 视图名
		String viewName = null;
		try {
			configService.saveOrUpdate(config);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, config,request, messageSource);
		return commonModelAndView;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/customer/pointsconfig/grid")
	public CommonModelAndView json(PointsConfig config, HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			Object obj = configService.getGrid(config);
			map = obj == null ? null : (Map<String, Object>) obj;
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, config, request);
	}

	/**
	 * 积分配置删除功能，json格式
	 * 
	 * @param role
	 * @param request
	 * @return
	 */
	@RequestMapping("/customer/pointsconfig/delete")
	public CommonModelAndView delete(PointsConfig user, HttpServletRequest request, Map<String, Object> model) {
		// 视图名
		String viewName = this.ERROR;
		try {
			if (user != null && user.getId() != null) {
				configService.delete(user);
				viewName = this.SUCCESS;
			} else if (user != null && user.getIds() != null) {
				configService.deleteByArr(user);
				viewName = this.SUCCESS;
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView(viewName, user,request, messageSource);
	}

	@Override
	public String uploadfile(PointsConfig entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String downloadfile(PointsConfig entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(PointsConfig entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@RequestMapping("/customer/pointsconfig/valid")
	public CommonModelAndView valid(@Valid PointsConfig entity, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
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
							count = (Long) configService.checkConfigDuplication(entity);
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
							count = (Long) configService.checkConfigDuplication(entity);
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
