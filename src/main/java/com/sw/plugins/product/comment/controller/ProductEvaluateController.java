package com.sw.plugins.product.comment.controller;

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
import com.sw.plugins.product.comment.entity.ProductEvaluate;
import com.sw.plugins.product.comment.service.ProductEvaluateService;

/**
 * 产品评价控制器
 * 
 * @author runchao
 */
@Controller
public class ProductEvaluateController extends BaseController<ProductEvaluate>{
	
	private static Logger logger = Logger.getLogger(ProductEvaluateController.class);
	
	@Resource
	private ProductEvaluateService productEvaluateService;
	
	/**
	 * 产品评价主页
	 * 
	 * @param productEvaluate
	 * @param request
	 * @return
	 */
	@RequestMapping("/product/comment")
	public CommonModelAndView list(ProductEvaluate productEvaluate, HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, productEvaluate, messageSource);
		commonModelAndView.addObject("productEvaluate", productEvaluate);
		commonModelAndView.addObject("productType", this.initData.getProductType());
		commonModelAndView.addObject("productPushStatus", this.initData.getProductPushStatus());
		return commonModelAndView;
	}
	
	/**
	 * 产品评价列表查询，返回json
	 * 
	 * @param productEvaluate
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/product/comment/grid")
	public CommonModelAndView json(ProductEvaluate productEvaluate, HttpServletRequest request){
		Object gridJson;
		Map<String, Object> map = null;
		try {
			gridJson = productEvaluateService.getGrid(productEvaluate);
			map = (Map<String, Object>)gridJson;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new CommonModelAndView("jsonView", map, productEvaluate, request);
	}
	
	/**
	 * 产品评价详细
	 * 
	 * @param productEvaluate
	 * @param request
	 * @return
	 */
	@RequestMapping("/product/comment/evaluate")
	public CommonModelAndView evaluate(ProductEvaluate productEvaluate, HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, productEvaluate, messageSource);
		commonModelAndView.addObject("productEvaluate", productEvaluate);
		commonModelAndView.addObject("productEvaluateQuota", this.initData.getProductEvaluate());
		return commonModelAndView;
	}
	
	/**
	 * 产品评价详细列表查询，返回json
	 * 
	 * @param productEvaluate
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/product/comment/evaluate/grid")
	public CommonModelAndView jsonEvaluate(ProductEvaluate productEvaluate, HttpServletRequest request){
		Object gridJson;
		Map<String, Object> map = null;
		try {
			gridJson = productEvaluateService.getGridEvaluate(productEvaluate);
			map = (Map<String, Object>)gridJson;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new CommonModelAndView("jsonView", map, productEvaluate, request);
	}
	
	/**
	 * 推送问卷
	 * 
	 * @param productEvaluate
	 * @param request
	 * @return
	 */
	@RequestMapping("/product/comment/push")
	public CommonModelAndView push(ProductEvaluate productEvaluate, HttpServletRequest request){
		String viewName = null;
		try {
			productEvaluateService.push(productEvaluate);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, productEvaluate, request, messageSource);
		return commonModelAndView;
	}

	@Override
	public String uploadfile(ProductEvaluate entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String downloadfile(ProductEvaluate entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(ProductEvaluate entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView valid(ProductEvaluate entity,
			BindingResult result, Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
