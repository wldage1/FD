package com.sw.plugins.websitemanage.friendlylink.controller;

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
import com.sw.plugins.websitemanage.friendlylink.entity.Links;
import com.sw.plugins.websitemanage.friendlylink.service.LinksService;
import com.sw.util.SystemProperty;

/**
 * 字典控制器，负责字典的添加，修改，删除，查询等功能
 * 
 * @author Administrator
 */
@Controller
public class LinksController extends BaseController<Links> {

	private static Logger logger = Logger.getLogger(LinksController.class);

	@Resource
	private LinksService linksService;

	/**
	 * 查看友情链接列表
	 * 
	 * @param links
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/websitemanage/friendlylink")
	public CommonModelAndView list(Links links, HttpServletRequest request, Map<String, Object> model) throws Exception {
		Object obj = new CommonModelAndView().getCurrentStatus(links, request);
		if (obj != null) {
			if (obj instanceof Links) {
				links = (Links) obj;
			}
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, links, messageSource);
		commonModelAndView.addObject("links", links);
		return commonModelAndView;
	}

	@RequestMapping("/websitemanage/friendlylink/grid")
	@SuppressWarnings("unchecked")
	public CommonModelAndView json(Links entity, HttpServletRequest request) {
		Object gridJson;
		Map<String, Object> map = null;
		try {
			gridJson = linksService.getGrid(entity);
			map = (gridJson == null ? null : (Map<String, Object>) gridJson);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, entity, request);
	}

	/**
	 * 跳转到创建页面
	 * 
	 */
	@RequestMapping("/websitemanage/friendlylink/create")
	public CommonModelAndView create(Links links, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, links, messageSource);
		commonModelAndView.addObject("links", links);
		commonModelAndView.addObject("c", links.getC());
		commonModelAndView.addObject("ftpUrl", SystemProperty.getInstance("config").getProperty("ftp.http.url"));
		return commonModelAndView;
	}

	/**
	 * 友情链接保存
	 */
	@RequestMapping(value = "/websitemanage/friendlylink/save", method = RequestMethod.POST)
	public CommonModelAndView save(Links links, HttpServletRequest request) {
		String viewName = null;
		try {
			linksService.saveOrUpdate(links);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			links.setErrorMsg(e.getMessage());
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, links, request, messageSource);
		return commonModelAndView;
	}

	/**
	 * 友情链接修改
	 */
	@RequestMapping("/websitemanage/friendlylink/modify")
	public CommonModelAndView modify(Links links, HttpServletRequest request) {
		String code = links.getC();
		try {
			links = linksService.getOne(links);
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		links.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, links, messageSource);
		commonModelAndView.addObject("links", links);
		commonModelAndView.addObject("ftpUrl", SystemProperty.getInstance("config").getProperty("ftp.http.url"));
		return commonModelAndView;
	}

	/**
	 * 友情链接删除
	 * 
	 * @param links
	 * @param request
	 * @return
	 */
	@RequestMapping("/websitemanage/friendlylink/delete")
	public CommonModelAndView delete(Links links, HttpServletRequest request) {
		// 视图名
		String viewName = null;
		try {
			linksService.delete(links);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			viewName = this.ERROR;
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, links, request, messageSource);
		return commonModelAndView;
	}

	@Override
	@RequestMapping("/websitemanage/friendlylink/uploadfile")
	public String uploadfile(Links entity, HttpServletRequest request, HttpServletResponse response) {
		try {
			String kk = linksService.uploadfile(entity, request);
			System.out.println(kk);
			response.getWriter().print(kk);
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return ERROR;
	}

	@Override
	public String downloadfile(Links entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(Links entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@RequestMapping("/websitemanage/friendlylink/valid")
	public CommonModelAndView valid(@Valid Links entity, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		if(entity.getName().length()>10){
			result.rejectValue("name", "size");
		}
		if (result.hasErrors()) {
			commonModelAndView.addBindingErrors(model, result, messageSource);
			return commonModelAndView;
		}
		return commonModelAndView;
	}

}
