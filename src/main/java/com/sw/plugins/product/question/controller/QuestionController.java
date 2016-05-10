package com.sw.plugins.product.question.controller;

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
import com.sw.plugins.product.question.entity.QuestionReply;
import com.sw.plugins.product.question.service.QuestionReplyService;
import com.sw.plugins.product.question.service.QuestionService;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;

@Controller
public class QuestionController extends BaseController<Question>{
	
	private static Logger logger = Logger.getLogger(QuestionController.class);
	
	@Resource
	private QuestionService productQuestionService;
	@Resource
	private UserLoginService userLoginService;
	@Resource
	private QuestionReplyService replyService;
	
	@RequestMapping("/product/question")
	public CommonModelAndView list(Question question, HttpServletRequest request){
		Object obj = new CommonModelAndView().getCurrentStatus(question, request);
		if(obj!=null)
		{
			if(obj instanceof Question)
			{
				question = (Question)obj;
			}
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(request,question,messageSource);
		commonModelAndView.addObject("question",question);
		return commonModelAndView;
	}
	
	//获取未处理的产品列表
	@SuppressWarnings("unchecked")
	@RequestMapping("/product/question/undisposedgrid")
	public CommonModelAndView undisposedjson(Question question,HttpServletRequest request){
		Object gridJson = null;
		Map<String,Object> map = null;
		try {
			User user = userLoginService.getCurrLoginUser();
			Organization organization = user.getSelfOrg();
			if (organization != null) {
				int isCommission = organization.getIsCommission();
				if (isCommission == 1) {
					question.setOrgId(organization.getId());
				} else {
					question.setOrgId(null);
				}
			}
			gridJson = productQuestionService.getGrid(question);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		map = (gridJson == null ? null : (Map<String,Object>) gridJson);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView",map,question,request);
		return commonModelAndView;
	}
	
	//获取已处理的产品列表
	@SuppressWarnings("unchecked")
	@RequestMapping("/product/question/disposedgrid")
	public CommonModelAndView disposedjson(Question question,HttpServletRequest request){
		Object gridJson = null ;
		Map<String,Object> map = null;
		try {
			User user = userLoginService.getCurrLoginUser();
			Organization organization = user.getSelfOrg();
			if (organization != null) {
				int isCommission = organization.getIsCommission();
				if (isCommission == 1) {
					question.setOrgId(organization.getId());
				} else {
					question.setOrgId(null);
				}
			}
			gridJson = productQuestionService.getDisposedGrid(question);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		map = (gridJson == null ? null : (Map<String,Object>) gridJson);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView",map,question,request);
		return commonModelAndView;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/product/question/pendinggrid")
	public CommonModelAndView pendingjson(Question question,HttpServletRequest request){
		Object gridJson = null ;
		Map<String,Object> map = null;
		try {
			User user = userLoginService.getCurrLoginUser();
			Organization organization = user.getSelfOrg();
			if (organization != null) {
				int isCommission = organization.getIsCommission();
				if (isCommission == 1) {
					question.setOrgId(organization.getId());
				} else {
					question.setOrgId(null);
				}
			}
			gridJson = productQuestionService.getPendingGrid(question);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		map = (gridJson == null ? null : (Map<String,Object>) gridJson);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView",map,question,request);
		return commonModelAndView;
	}
	
	
	//跳转到产品回复页面
	@RequestMapping("/product/question/reply")
	public CommonModelAndView reply(Question question,HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request,question,messageSource);
		String c = question.getC();
		try {
			question = productQuestionService.getOne(question);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		commonModelAndView.addObject("question",question);
		commonModelAndView.addObject("c",c);
		return commonModelAndView;
	}
	
	//跳转到产品咨询修改页面
	@RequestMapping("/product/question/modify")
	public CommonModelAndView modify(Question question,HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request,question,messageSource);
		String c = question.getC();
		try {
			question = productQuestionService.getOne(question);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		commonModelAndView.addObject("question",question);
		commonModelAndView.addObject("c",c);
		return commonModelAndView;
	}
	
	//保存回复信息
	@RequestMapping("/product/question/save")
	public CommonModelAndView save(Question question,HttpServletRequest request){
		String viewName = "";
		try {
			productQuestionService.saveReply(question);
			if(question.getIsReleased()!=null){
				isReleased(question,request);
			}
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(DetailException.expDetail(e, getClass()));
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName,question,request,messageSource);
		return commonModelAndView;
	}
	
	//保存回复信息
	@RequestMapping("/product/question/update")
	public CommonModelAndView update(QuestionReply question,HttpServletRequest request){
		String viewName = "";
		try {
			replyService.update(question);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(DetailException.expDetail(e, getClass()));
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName,question,request,messageSource);
		return commonModelAndView;
	}
	
	@RequestMapping("/product/question/isreleased")
	public CommonModelAndView isReleased(Question question,HttpServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
		if(question.getId()!=null){
			try {
				productQuestionService.updatePublish(question);
				map.put(this.STATUS,this.STATUS_SUCCESS);
			} catch (Exception e) {
				map.put(this.STATUS,this.STATUS_FALSE);
				logger.error(DetailException.expDetail(e, getClass()));
			}
		}
		return new CommonModelAndView("jsonView", map);
	}
	
	@RequestMapping("/product/question/repeal")
	public CommonModelAndView repeal(Question question,HttpServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
		if(question.getId()!=null){
			try {
				productQuestionService.updateIsReleased(question);
				map.put(this.STATUS,this.STATUS_SUCCESS);
			} catch (Exception e) {
				map.put(this.STATUS,this.STATUS_FALSE);
				logger.error(DetailException.expDetail(e, getClass()));
			}
		}
		return new CommonModelAndView("jsonView", map);
	}
	
	@RequestMapping("/product/question/delete")
	public CommonModelAndView delete(Question question,HttpServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
		if(question.getId()!=null){
			try {
				productQuestionService.delete(question);
				map.put(this.STATUS, this.STATUS_SUCCESS);
			} catch (Exception e) {
				map.put(this.ERROR, this.STATUS_FALSE);
				logger.error(DetailException.expDetail(e, getClass()));
			}
		}
		return new CommonModelAndView("jsonView",map);
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
