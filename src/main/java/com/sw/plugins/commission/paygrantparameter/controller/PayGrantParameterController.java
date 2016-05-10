package com.sw.plugins.commission.paygrantparameter.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.sw.plugins.commission.organizationgrant.controller.OrganizationgrantController;
import com.sw.plugins.commission.paygrantparameter.entity.PayGrantParameter;
import com.sw.plugins.commission.paygrantparameter.service.PayGrantParameterService;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.plugins.system.organization.service.OrganizationService;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;

@Controller
public class PayGrantParameterController extends BaseController<PayGrantParameter>{
	
	private static Logger logger = Logger.getLogger(OrganizationgrantController.class);
	
	@Resource
	private PayGrantParameterService payGrantParameterService;
	
	@Resource
	private OrganizationService organizationService;
	
	@Resource
	private UserLoginService userLoginService;
	
	@RequestMapping("/commission/paygrantparameter")
	public CommonModelAndView list(PayGrantParameter entity,HttpServletRequest request){
		Object obj = new CommonModelAndView().getCurrentStatus(entity, request);
		if (obj != null) {
			if (obj instanceof PayGrantParameter) {
				entity = (PayGrantParameter) obj;
			}
		}
		User user = userLoginService.getCurrLoginUser();
		Organization organization = user.getSelfOrg();
		Organization org = new Organization();
		if (organization != null) {
			int isCommission = organization.getIsCommission();
			if (isCommission == 1) {
				org.setId(organization.getId());
			} else {
				org.setId(null);
			}
		}
		List<Organization> orgList = new ArrayList<Organization>();
		//只展示二级居间公司列表
		org.setTreeLevel(2);
		org.setIsCommission(1);
		try {
			orgList = organizationService.getListOnLevel2(org);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, entity, messageSource);
		commonModelAndView.addObject("c", entity.getC());
		commonModelAndView.addObject("orgList", orgList);
		commonModelAndView.addObject("orgListSize",orgList.size());
		return commonModelAndView;
	}
	
	@RequestMapping("/commission/paygrantparameter/grid")
	public CommonModelAndView json(PayGrantParameter entity,HttpServletRequest request){
		Map<String,Object> map = null;
		try {
			map = payGrantParameterService.getGrid(entity);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
	
	@RequestMapping("/commission/paygrantparameter/save")
	public CommonModelAndView save(PayGrantParameter entity,HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			payGrantParameterService.saveorupdate(entity);
			map.put(this.STATUS, this.STATUS_SUCCESS);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
			map.put(this.STATUS, this.STATUS_FALSE);
		}
		return new CommonModelAndView("jsonView",map);
	}
	
	@RequestMapping("/commission/paygrantparameter/delete")
	public CommonModelAndView delete(PayGrantParameter entity,HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			payGrantParameterService.delete(entity);
			map.put(this.STATUS, this.STATUS_SUCCESS);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
			map.put(this.STATUS, this.STATUS_FALSE);
		}
		return new CommonModelAndView("jsonView",map);
	}
	
	@Override
	public String uploadfile(PayGrantParameter entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String downloadfile(PayGrantParameter entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(PayGrantParameter entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@RequestMapping("/commission/paygrantparameter/valid")
	public CommonModelAndView valid(@Valid PayGrantParameter entity,
			BindingResult result, Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) {
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		if(entity.getApprovedDeadline() == null){
			result.rejectValue("approvedDeadline", "NotEmpty");
		}
		if(entity.getPayDate() == null){
			result.rejectValue("payDate", "NotEmpty");
		}
		if(entity.getApprovedDeadline()!=null && entity.getPayDate()!=null){
			if(entity.getPayDate() < entity.getApprovedDeadline()){
				result.rejectValue("payDate", "Pattern");
			}
		}
		if (result.hasErrors()) {
			commonModelAndView.addBindingErrors(model, result, messageSource);
			return commonModelAndView;
		}				
		return commonModelAndView;
	}

}
