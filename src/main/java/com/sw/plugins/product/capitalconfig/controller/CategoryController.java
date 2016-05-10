package com.sw.plugins.product.capitalconfig.controller;

import java.net.URLDecoder;
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
import org.springframework.web.bind.annotation.RequestMethod;

import com.sw.core.common.Constant;
import com.sw.core.controller.BaseController;
import com.sw.core.controller.CommonModelAndView;
import com.sw.core.exception.DetailException;
import com.sw.plugins.product.capitalconfig.entity.ProductCategory;
import com.sw.plugins.product.capitalconfig.service.ProductCategoryService;

/**
 * 产品分类控制器
 * 
 * @author baokai Created on :2013-1-21 上午10:57:22
 */
@Controller
public class CategoryController extends BaseController<ProductCategory> {

	private static Logger logger = Logger.getLogger(CategoryController.class);
	@Resource
	private ProductCategoryService productCategoryService;

	/**
	 * 产品分类列表
	 * 
	 * @author baokai Created on :2013-1-21 下午4:20:55
	 */
	@RequestMapping("/product/capitalconfig")
	public CommonModelAndView list(ProductCategory productCategory, HttpServletRequest request, Map<String, Object> model) {
		Object obj = new CommonModelAndView().getCurrentStatus(productCategory, request);
		if (obj != null) {
			if (obj instanceof ProductCategory) {
				productCategory = (ProductCategory) obj;
			}
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, productCategory, messageSource);
		commonModelAndView.addObject("code", productCategory.getC());
		model.put("category", productCategory);
		return commonModelAndView;
	}

	/**
	 * 查询产品分类信息，返回json
	 * 
	 * @author baokai Created on :2013-1-21 下午4:21:56
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/product/capitalconfig/tree")
	public CommonModelAndView json(String nodeid, HttpServletRequest request) {
		ProductCategory productCategory = new ProductCategory();
		if (nodeid != null && !nodeid.equals("")) {
			productCategory.setNodeid(nodeid);
		}
		Object obj = null;
		try {
			obj = productCategoryService.getGrid(productCategory);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		Map<String, Object> map = (obj == null ? null : (Map<String, Object>) obj);
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 跳转到创建页面
	 * 
	 * @author baokai Created on :2013-1-22 下午1:43:51
	 */
	@RequestMapping("/product/capitalconfig/create")
	public CommonModelAndView create(ProductCategory productCategory, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, productCategory, messageSource);
		commonModelAndView.addObject("productCategory", productCategory);
		commonModelAndView.addObject("c", productCategory.getC());
		return commonModelAndView;
	}

	/**
	 * 获取下拉列表树信息
	 * 
	 * @author baokai Created on :2013-1-22 下午2:17:31
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/product/capitalconfig/stree")
	public CommonModelAndView stree(String id, HttpServletRequest request) {
		ProductCategory productCategory = new ProductCategory();
		Object obj = null;
		if (id != null) {
			productCategory.setParentId(Long.valueOf(id));
		}
		try {
			obj = productCategoryService.getSelectTree(productCategory);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		Map<String, Object> map = (obj == null ? null : (Map<String, Object>) obj);
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 产品分类保存
	 * 
	 * @author baokai Created on :2013-1-22 下午3:21:33
	 */
	@RequestMapping(value = "/product/capitalconfig/save", method = RequestMethod.POST)
	public CommonModelAndView save(ProductCategory productCategory, HttpServletRequest request) {

		String viewName = null;
		try {
			productCategoryService.saveOrUpdate(productCategory);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			productCategory.setErrorMsg(e.getMessage());
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, productCategory, request, messageSource);
		return commonModelAndView;
	}

	/**
	 * 产品分类修改
	 * 
	 * @author baokai Created on :2013-1-22 下午3:21:58
	 */
	@RequestMapping("/product/capitalconfig/modify")
	public CommonModelAndView modify(ProductCategory productCategory, HttpServletRequest request) {
		String code = productCategory.getC();
		if (productCategory.getId() != null) {
			try {
				productCategory = (ProductCategory) productCategoryService.getOne(productCategory);
				ProductCategory parentPC = new ProductCategory();
				parentPC.setId(productCategory.getParentId());
				parentPC = (ProductCategory) productCategoryService.getOne(parentPC);
				productCategory.setParentName(parentPC.getName());
			} catch (Exception e) {
				logger.debug(e.getMessage());
			}
		}
		productCategory.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, productCategory, messageSource);
		commonModelAndView.addObject("productCategory", productCategory);
		return commonModelAndView;
	}

	/**
	 * 产品分类删除
	 * 
	 * @author baokai Created on :2013-1-23 上午10:13:53
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/product/capitalconfig/delete")
	public CommonModelAndView delete(ProductCategory productCategory, HttpServletRequest request) {
		// 视图名
		String viewName = null;
		try {
			String name = URLDecoder.decode(productCategory.getName(), Constant.ENCODING);
			productCategory.setName(name);
			if ((Boolean) productCategoryService.deleteCategory(productCategory)) {
				viewName = this.SUCCESS;
			} else {
				Map map = new HashMap();
				map.put(Constant.STATUS, "ERROR");
				return new CommonModelAndView("jsonView", map);
			}
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, productCategory, request, messageSource);
		return commonModelAndView;
	}
	/**
	 * 保存排序号码
	 * @param productCategory
	 * @param request
	 * @return
	 */
	@RequestMapping("/product/capitalconfig/saveSortNum")
	public CommonModelAndView saveSortNum(ProductCategory productCategory, HttpServletRequest request) {
		String viewName = null;
		try {
			productCategoryService.update(productCategory);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, productCategory, request, messageSource);
		return commonModelAndView;
	}

	@Override
	public String uploadfile(ProductCategory entity, HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	@Override
	public String downloadfile(ProductCategory entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(ProductCategory entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@RequestMapping("/product/capitalconfig/valid")
	public CommonModelAndView valid(@Valid ProductCategory entity, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		String operate = (entity.getOperate() == null || entity.getOperate().equals("")) ? "0" : entity.getOperate();
		int operateTemp = Integer.parseInt(operate);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		try {
			switch (operateTemp) {
			case 1: {
				if (entity.getName() != null) {
					ProductCategory category = new ProductCategory();
					category.setName(entity.getName());
					category.setParentId(entity.getParentId());
					long codeCount = productCategoryService.checkCode(category, request);
					if (codeCount > 0) {
						result.rejectValue("name", "exist");
					}
				}
				if (entity.getCode() != null) {
					ProductCategory category = new ProductCategory();
					category.setCode(entity.getCode());
					long codeCount = productCategoryService.checkCode(category, request);
					if (codeCount > 0) {
						result.rejectValue("code", "exist");
					}
				}
				if (result.hasErrors()) {
					commonModelAndView.addBindingErrors(model, result, messageSource);
					return commonModelAndView;
				}
				break;
			}
			case 2: {
				if (entity.getName() != null) {
					ProductCategory category = new ProductCategory();
					category.setName(entity.getName());
					category.setParentId(entity.getParentId());
					category.setId(entity.getId());
					long codeCount = productCategoryService.checkCode(category, request);
					if (codeCount > 0) {
						result.rejectValue("name", "exist");
					}
				}
				if (entity.getCode() != null) {
					ProductCategory category = new ProductCategory();
					category.setCode(entity.getCode());
					category.setId(entity.getId());
					long codeCount = productCategoryService.checkCode(category, request);
					if (codeCount > 0) {
						result.rejectValue("code", "exist");
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
