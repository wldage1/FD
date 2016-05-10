package com.sw.plugins.websitemanage.consult.controller;

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
import com.sw.plugins.product.question.entity.Question;
import com.sw.plugins.websitemanage.consult.service.ConsultService;
;


@Controller
public class ConsultController extends BaseController<Question>{
	
	private static Logger logger = Logger.getLogger(ConsultController.class);
	
	@Resource
	private ConsultService consultService;
	
	
	@RequestMapping("/websitemanage/consult")
	public CommonModelAndView list(Question question,HttpServletRequest request){
		Object obj = new CommonModelAndView().getCurrentStatus(question, request);
		if(obj!=null){
			if(obj instanceof Question){
				question = (Question)obj;
			}
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(request,question,messageSource);
		commonModelAndView.addObject("question",question);
		return commonModelAndView;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/websitemanage/consult/grid")
	public CommonModelAndView json(Question question,HttpServletRequest request){
		Object gridJson = null ;
		Map<String,Object> map = null;
		try {
			gridJson = consultService.getGrid(question);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		map = (gridJson == null ? null : (Map<String,Object>) gridJson);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView",map,question,request);
		return commonModelAndView;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/websitemanage/consult/disposedgrid")
	public CommonModelAndView disposedjson(Question question,HttpServletRequest request){
		Object gridJson = null;
		Map<String,Object> map = null;
		try {
			gridJson = consultService.getDisposedGrid(question);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		map=(gridJson == null ? null :(Map<String,Object>) gridJson);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView", map,question,request);
		return commonModelAndView;
	}
	
	//处理操作 传问题ID
	@RequestMapping("/websitemanage/consult/handle")
	public CommonModelAndView handle(Question question,HttpServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
		if(question.getId()!=null){
			try {
				consultService.update(question);
				map.put(this.STATUS,this.STATUS_SUCCESS);
			} catch (Exception e) {
				map.put(this.STATUS,this.STATUS_FALSE);
				logger.error(DetailException.expDetail(e, getClass()));
			}
		}
		return new CommonModelAndView("jsonView", map);
	}
	
	@Override
	public String uploadfile(Question entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String downloadfile(Question entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(Question entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView valid(Question entity, BindingResult result,
			Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
