package com.sw.plugins.product.watermark.controller;

import java.util.HashMap;
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
import com.sw.plugins.incentivefee.incentive.entity.StageParameter;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.plugins.system.organization.service.OrganizationService;

@Controller
public class WatermarkController extends BaseController<Organization>{
	
	private static Logger logger = Logger.getLogger(WatermarkController.class);
	
	@Resource
	private OrganizationService organizationService;
	
	@RequestMapping("/product/watermark")
	public CommonModelAndView watermark(Organization organization, HttpServletRequest request){
		return new CommonModelAndView(request, organization, messageSource);
	}
	
	@RequestMapping("/product/watermark/grid")
	public CommonModelAndView json(Organization organization, HttpServletRequest request){
		Map<String, Object> map = null;
		try {
			map = organizationService.getGridWatermark(organization);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new CommonModelAndView("jsonView", map, organization, request);
	}
	
	@RequestMapping("/product/watermark/save")
	public CommonModelAndView save(Organization organization, HttpServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
			try {
				organizationService.update(organization);
				map.put(this.STATUS, this.STATUS_SUCCESS);
			} catch (Exception e) {
				logger.error(DetailException.expDetail(e, getClass()));
				map.put(this.STATUS, this.STATUS_FALSE);
			}
		return new CommonModelAndView("jsonView",map);
	}

	@Override
	public String uploadfile(Organization entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String downloadfile(Organization entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(Organization entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView valid(Organization entity, BindingResult result,
			Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
