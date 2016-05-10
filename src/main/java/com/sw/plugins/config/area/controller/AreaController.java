package com.sw.plugins.config.area.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
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

import com.sw.core.common.Constant;
import com.sw.core.controller.BaseController;
import com.sw.core.controller.CommonModelAndView;
import com.sw.core.exception.DetailException;
import com.sw.plugins.config.area.entity.Area;
import com.sw.plugins.config.area.service.AreaService;

/**
 * 地区配置控制器
 */
@Controller
public class AreaController extends BaseController<Area> {

	private static Logger logger = Logger.getLogger(AreaController.class);
	@Resource
	private AreaService areaService;

	/**
	 * 地区列表
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/config/area")
	public CommonModelAndView list(Area area, HttpServletRequest request, Map<String, Object> model) throws UnsupportedEncodingException {
		Object obj = new CommonModelAndView().getCurrentStatus(area, request);
		if (obj != null) {
			if (obj instanceof Area) {
				area = (Area) obj;
			}
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, area,messageSource);
		commonModelAndView.addObject("code", area.getC());
		model.put("area", area);
		
		String parentName = area.getParentName();
		if(parentName != null){
			parentName = URLDecoder.decode(parentName, Constant.ENCODING);
			commonModelAndView.addObject("parentName", parentName);
		}
		
		if (area.getNodeid() != null && !area.getNodeid().equals("")) {
			area.setNodeid(area.getNodeid());
		}
		List<Area> areaList = new ArrayList<Area>();
		try {
			if(area.getId() != null || area.getParentId() == null){
				area.setId(null);
				area.setParentId(0L);
			}
			areaList = areaService.getTree(area);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		commonModelAndView.addObject("List",areaList);
		return commonModelAndView;
	}
	
	/**
	 * 获取上级地区
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/config/area/parent")
	public CommonModelAndView areaByParent(Area area, HttpServletRequest request, Map<String, Object> model) throws UnsupportedEncodingException {
		Object obj = new CommonModelAndView().getCurrentStatus(area, request);
		if (obj != null) {
			if (obj instanceof Area) {
				area = (Area) obj;
			}
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, area,messageSource);
		commonModelAndView.addObject("code", area.getC());
		model.put("area", area);
		List<Area> areaList = new ArrayList<Area>();
		try {
			if(area.getParentId() == null)
			area.setParentId(1L);
			areaList = (List<Area>) areaService.getListByParentId(area);
			//新加入的处理查看上级之后父id的问题
			area.setParentId(null);
			Area newarea= areaService.getOne(area);
			area.setParentId(newarea.getParentId());
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		commonModelAndView.addObject("List",areaList);
		return commonModelAndView;
	}

	@RequestMapping("/config/area/create")
	public CommonModelAndView create(Area area, BindingResult result, HttpServletRequest request)  {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, area,messageSource);
		String c = area.getC();
		if(area.getId() != null){
			try {
				area = areaService.getOne(area);
			} catch (Exception e) {
				logger.error(DetailException.expDetail(e, getClass()));
			}
		}
		
		commonModelAndView.addObject("area", area);
		commonModelAndView.addObject("c", c);
		/** -- 验证提示 -- */
		result.rejectValue("name", "Pattern.area.name");
		result.rejectValue("code", "Pattern.area.code");
		result.rejectValue("pinyin", "Pattern.area.pinyin");
		result.rejectValue("parentName", "NotEmpty.area.parentName");
		return commonModelAndView;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/config/area/stree")
	public CommonModelAndView stree(String id, HttpServletRequest request) {
		Area area = new Area();
		Object obj = null;
		if (id != null) {
			area.setParentId(new Long(id));
		}
		try {
			area.setParentName(new CommonModelAndView().getPromptMessage(messageSource, "area.tree.root", null));
			obj = areaService.getSelectTree(area);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		Map<String, Object> map = (obj == null ? null : (Map<String, Object>) obj);
		return new CommonModelAndView("jsonView", map);
	}

	@RequestMapping(value = "/config/area/save", method = RequestMethod.POST)
	public CommonModelAndView save(Area area, HttpServletRequest request) {
		// 视图名
		String viewName = null;
		try {
			areaService.saveOrUpdate(area);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			area.setErrorMsg(e.getMessage());
			viewName = this.ERROR;
			logger.error(DetailException.expDetail(e, getClass()));
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, area,request, messageSource);
		return commonModelAndView;
	}

	@RequestMapping("/config/area/modify")
	public CommonModelAndView modify(Area area, HttpServletRequest request, Map<String, Object> model) {
		String code = area.getC();
		if (area.getId() != null) {
			try {
				area = (Area) areaService.getArea(area);
			} catch (Exception e) {
				logger.debug(e.getMessage());
			}
		}
		area.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, area,messageSource);
		commonModelAndView.addObject("area", area);
		model.put("area", area);
		return commonModelAndView;
	}

	@RequestMapping("/config/area/delete")
	public CommonModelAndView delete(Area area, HttpServletRequest request) {
		String viewName = null;
		try {
			Long parentArarCount = areaService.getParentAreaCount(area);
			if(parentArarCount > 0){
				viewName = this.ERROR;
			}else{
				String name = URLDecoder.decode(area.getName(), Constant.ENCODING);
				area.setName(name);
				areaService.delete(area);
				viewName = this.SUCCESS;
			}
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, area,request, messageSource);
		return commonModelAndView;
	}

	@Override
	public String uploadfile(Area area, HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	@Override
	public String downloadfile(Area entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(Area entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@RequestMapping("/config/area/valid")
	public CommonModelAndView valid(@Valid Area entity, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		String operate = (entity.getOperate() == null || entity.getOperate().equals("")) ? "0" : entity.getOperate();
		int operateTemp = Integer.parseInt(operate);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		try{
			switch(operateTemp){
			    case 1:{
			    	/* 支持修改上级的时候用上 */
					/*
					 * if (area.getId() != null) { area.setId((Long)area.getId()); Long
					 * id = (Long)area.getId(); Long pid = area.getParentId(); if
					 * ((id).equals(pid)) { result.rejectValue("parentName",
					 * "organization.noself"); return new CommonModelAndView(request,
					 * area); } }
					 */
					/* 支持修改上级的时候用上 */
					// 验证地区名称在同一地区下是否唯一
			    	if (!entity.getName().isEmpty()) {
						if (entity.getParentId() == null) {
							entity.setParentId(0l);
						}
						Long coName = (Long) areaService.getCountByParentId(entity);
						if (coName != null && coName != 0) {
							result.rejectValue("name", "dupliname");
						}
					}

					if (!entity.getCode().isEmpty()) {
						Long coCode = (Long) areaService.getCount(entity);
						if (coCode != null && coCode != 0) {
							result.rejectValue("code", "duplicode");
						}
					}
					if (result.hasErrors()) {
						commonModelAndView.addBindingErrors(model, result, messageSource);
						return commonModelAndView;
					}
			    	break;
			    }
			    case 2:{
			    	if (!entity.getName().isEmpty()) {
						if (entity.getParentId() == null) {
							entity.setParentId(0l);
						}
						Long coName = (Long) areaService.getCountByParentId(entity);
						if (coName != null && coName != 0) {
							result.rejectValue("name", "dupliname");
						}
					}

					if (!entity.getCode().isEmpty()) {
						Long coCode = (Long) areaService.getCount(entity);
						if (coCode != null && coCode != 0) {
							result.rejectValue("code", "duplicode");
						}
					}
					if (result.hasErrors()) {
						commonModelAndView.addBindingErrors(model, result, messageSource);
						return commonModelAndView;
					}
			    	break;
			    }
			}
		}catch(Exception e){
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return commonModelAndView;
	}

}
