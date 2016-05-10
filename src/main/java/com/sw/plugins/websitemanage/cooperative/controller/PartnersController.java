package com.sw.plugins.websitemanage.cooperative.controller;

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
import com.sw.plugins.websitemanage.cooperative.entity.Partners;
import com.sw.plugins.websitemanage.cooperative.service.PartnersService;
import com.sw.plugins.websitemanage.friendlylink.controller.LinksController;
import com.sw.util.SystemProperty;

/**
 * 合作伙伴控制器
 * 
 * @author tiger
 */
@Controller
public class PartnersController extends BaseController<Partners> {

	private static Logger logger = Logger.getLogger(LinksController.class);
	@Resource
	private PartnersService partnersService;

	/**
	 * 查看合作伙伴列表
	 * 
	 * @param partners
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/websitemanage/cooperative")
	public CommonModelAndView list(Partners partners, HttpServletRequest request, Map<String, Object> model) throws Exception {
		Object obj = new CommonModelAndView().getCurrentStatus(partners, request);
		if (obj != null) {
			if (obj instanceof Partners) {
				partners = (Partners) obj;
			}
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, partners, messageSource);
		commonModelAndView.addObject("links", partners);
		return commonModelAndView;
	}

	@RequestMapping("/websitemanage/cooperative/grid")
	@SuppressWarnings("unchecked")
	public CommonModelAndView json(Partners partners, HttpServletRequest request) {
		Object gridJson;
		Map<String, Object> map = null;
		try {
			gridJson = partnersService.getGrid(partners);
			map = (gridJson == null ? null : (Map<String, Object>) gridJson);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, partners, request);
	}

	/**
	 * 跳转到创建页面
	 * 
	 */
	@RequestMapping("/websitemanage/cooperative/create")
	public CommonModelAndView create(Partners partners, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, partners, messageSource);
		commonModelAndView.addObject("partners", partners);
		commonModelAndView.addObject("c", partners.getC());
		commonModelAndView.addObject("ftpUrl", SystemProperty.getInstance("config").getProperty("ftp.http.url"));
		return commonModelAndView;
	}

	/**
	 * 合作伙伴保存
	 */
	@RequestMapping(value = "/websitemanage/cooperative/save", method = RequestMethod.POST)
	public CommonModelAndView save(Partners partners, HttpServletRequest request) {
		String viewName = null;
		try {
			partnersService.saveOrUpdate(partners);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			partners.setErrorMsg(e.getMessage());
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, partners, request, messageSource);
		return commonModelAndView;
	}

	/**
	 * 合作伙伴修改
	 */
	@RequestMapping("/websitemanage/cooperative/modify")
	public CommonModelAndView modify(Partners partners, HttpServletRequest request) {
		String code = partners.getC();
		try {
			partners = partnersService.getOne(partners);
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		partners.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, partners, messageSource);
		commonModelAndView.addObject("partners", partners);
		commonModelAndView.addObject("ftpUrl", SystemProperty.getInstance("config").getProperty("ftp.http.url"));
		return commonModelAndView;
	}

	/**
	 * 合作伙伴删除
	 * 
	 * @param partners
	 * @param request
	 * @return
	 */
	@RequestMapping("/websitemanage/cooperative/delete")
	public CommonModelAndView delete(Partners partners, HttpServletRequest request) {
		// 视图名
		String viewName = null;
		try {
			partnersService.delete(partners);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			viewName = this.ERROR;
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, partners, request, messageSource);
		return commonModelAndView;
	}

	@Override
	public String downloadfile(Partners entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(Partners entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@RequestMapping("/websitemanage/cooperative/uploadfile")
	public String uploadfile(Partners entity, HttpServletRequest request, HttpServletResponse response) {
		try {
			response.getWriter().print(partnersService.uploadfile(entity, request));
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return ERROR;
	}

	@Override
	@RequestMapping("/websitemanage/cooperative/valid")
	public CommonModelAndView valid(@Valid Partners partners, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		if(partners.getName().length()>10){
			result.rejectValue("name", "size");
		}
		if (result.hasErrors()) {
			commonModelAndView.addBindingErrors(model, result, messageSource);
			return commonModelAndView;
		}
		return commonModelAndView;
	}

}
