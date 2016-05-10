package com.sw.plugins.extension.youku.category.controller;

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
import com.sw.plugins.extension.youku.category.entity.Category;
import com.sw.plugins.extension.youku.category.service.CategoryService;

@Controller
public class VideoCategoryController extends BaseController<Category>{
	
	private static Logger logger = Logger.getLogger(VideoCategoryController.class);
	
	@Resource
	private CategoryService categoryService;
	
	@RequestMapping("/extension/youku_category")
	public CommonModelAndView list(Category entity,HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request,entity, messageSource);
		return commonModelAndView;
	}
	
	@RequestMapping("/extension/youku_category/grid")
	public CommonModelAndView json(Category entity,HttpServletRequest request){
		Map<String,Object> map = null;
		try {
			map = categoryService.getGrid(entity);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, entity, request);
	}
	
	@RequestMapping("/extension/youku_category/create")
	public CommonModelAndView create(Category entity,HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request,entity, messageSource);
		return commonModelAndView;
	}
	
	@RequestMapping("/extension/youku_category/modify")
	public CommonModelAndView modify(Category entity,HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request,entity, messageSource);
		try {
			entity = categoryService.getOne(entity);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		commonModelAndView.addObject("category",entity);
		return commonModelAndView;
	}
	
	@RequestMapping("/extension/youku_category/save")
	public CommonModelAndView save(@Valid Category entity,BindingResult result,HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			if(entity.getId()!=null){
				Long countNameByID = categoryService.getCountNameByID(entity);
				if(countNameByID!=0){
					map.put(MESSAGE,new CommonModelAndView().getPromptMessage(messageSource, "already.category.name", null));
					return new CommonModelAndView("jsonView",map);
				}
				Long countCodeByID = categoryService.getCountCodeByID(entity);
				if(countCodeByID!=0){
					map.put(MESSAGE,new CommonModelAndView().getPromptMessage(messageSource, "already.category.code", null));
					return new CommonModelAndView("jsonView",map);
				}
			}else{
				//验证视频分类名称是否唯一
				Long countName = categoryService.getNameCount(entity);
				if(countName !=0 ){
					map.put(MESSAGE,new CommonModelAndView().getPromptMessage(messageSource, "already.category.name", null));
					return new CommonModelAndView("jsonView",map);
				}
				//验证视频分类标识是否唯一
				Long countCode = categoryService.getCodeCount(entity);
				if(countCode !=0 ){
					map.put(MESSAGE,new CommonModelAndView().getPromptMessage(messageSource, "already.category.code", null));
					return new CommonModelAndView("jsonView",map);
				}
			}
		} catch (Exception e1) {
			logger.error(DetailException.expDetail(e1, getClass()));
		}
		if (result.hasErrors()) {
			return new CommonModelAndView(request, entity,messageSource);
		}
		try {
			categoryService.saveorupdate(entity);
			map.put(this.STATUS, this.STATUS_SUCCESS);
		} catch (Exception e) {
			map.put(this.STATUS, this.STATUS_FALSE);
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView",map);
	}
	
	@RequestMapping("/extension/youku_category/delete")
	public CommonModelAndView delete(Category entity,HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			categoryService.delete(entity);
			map.put(this.STATUS, this.STATUS_SUCCESS);
		} catch (Exception e) {
			map.put(this.STATUS, this.STATUS_FALSE);
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView",map);
	}
	
	@Override
	public String uploadfile(Category entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String downloadfile(Category entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(Category entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@RequestMapping("/extension/youku_category/valid")
	public CommonModelAndView valid(@Valid Category entity, BindingResult result,
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
