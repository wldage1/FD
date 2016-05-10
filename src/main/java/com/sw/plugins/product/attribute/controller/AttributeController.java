package com.sw.plugins.product.attribute.controller;

import java.net.URLDecoder;
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

import com.sw.core.controller.BaseController;
import com.sw.core.controller.CommonModelAndView;
import com.sw.plugins.config.dictionary.entity.Dictionary;
import com.sw.plugins.config.dictionary.service.DictionaryService;
import com.sw.plugins.config.validationrule.entity.ValidationRule;
import com.sw.plugins.config.validationrule.service.ValidationRuleService;
import com.sw.plugins.product.attribute.entity.ProductAttribute;
import com.sw.plugins.product.attribute.entity.ProductAttributeGroup;
import com.sw.plugins.product.attribute.service.ProductAttributeGroupService;
import com.sw.plugins.product.attribute.service.ProductAttributeService;
import com.sw.plugins.product.manage.entity.Product;
import com.sw.plugins.product.manage.service.ProductService;

@Controller
public class AttributeController extends BaseController<ProductAttributeGroup> {

	private static Logger logger = Logger.getLogger(AttributeController.class);

	@Resource
	private ProductAttributeGroupService productAttributeGroupService;
	@Resource
	private ProductAttributeService productAttributeService;
	@Resource
	private ValidationRuleService validationRuleService;
	@Resource
	private DictionaryService dictionaryService;
	@Resource
	private ProductService productService;

	/**
	 * 保存属性组
	 * 
	 * @author baokai Created on :2013-1-29 上午10:39:48
	 */
	@RequestMapping("/product/capitalconfig/attribute/save")
	public CommonModelAndView save(@Valid ProductAttributeGroup entity, BindingResult result, Map<String, Object> model, HttpServletRequest request) throws Exception {
		String viewName = null;
		try {
			if (entity != null) {
				// 保存相应功能
				productAttributeGroupService.saveOrUpdate(entity);
				viewName = this.SUCCESS;
			} else {
				viewName = this.ERROR;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			viewName = this.ERROR;
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, entity, request, messageSource);
		return commonModelAndView;
	}

	/**
	 * 产品属性维护
	 * 
	 * @throws Exception
	 */
	@RequestMapping("/product/capitalconfig/attribute")
	public CommonModelAndView config(ProductAttributeGroup entity, HttpServletRequest request, Map<String, Object> model) throws Exception {
		String c = entity.getC();
		Long investCategoryId = entity.getId();
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, entity, messageSource);
		// 验证规则信息
		List<ValidationRule> validationRuleList = (List<ValidationRule>) validationRuleService.getList(new ValidationRule());
		commonModelAndView.addObject("validationRuleList", validationRuleList);
		commonModelAndView.addObject("pageShowTypeList", initData.getPageShowType());
		ProductAttribute pa = new ProductAttribute();
		pa.setInvestCategoryId(entity.getId());
		// 产品属性
		List<ProductAttribute> attributeList = (List<ProductAttribute>) productAttributeService.getList(pa);
		commonModelAndView.addObject("attributeList", attributeList);
		commonModelAndView.addObject("investCategoryId", investCategoryId);
		commonModelAndView.addObject("c", c);
		// 判断当前产品分类下是否含有产品
		Product product = new Product();
		product.setInvestCategoryId(investCategoryId);
		Long record = productService.getRecordCountFromAllProduct(product);
		if (record != 0l) {
			commonModelAndView.addObject("isUse", true);
		}
		return commonModelAndView;
	}

	/**
	 * 跳转到设置数据源页面
	 * 
	 * @author baokai Created on :2013-1-22 下午1:43:51
	 */
	@RequestMapping("/product/attribute/setValue")
	public CommonModelAndView setValue(String c, String id, String type, String value, HttpServletRequest request) throws Exception {
		CommonModelAndView commonModelAndView = new CommonModelAndView("/product/attribute/setValue");
		commonModelAndView.addObject("iframeId", request.getParameter("iframeId"));
		commonModelAndView.addObject("c", c);
		commonModelAndView.addObject("id", id);
		commonModelAndView.addObject("type", type);
		// 防止乱码进行转码
		//value = CharsetUtil.changeCharset(value, "ISO8859-1", "UTF-8");
		value = URLDecoder.decode(value, "utf-8");
		commonModelAndView.addObject("value", value);
		List<Dictionary> dictionaryList = (List<Dictionary>) dictionaryService.getList(new Dictionary());
		commonModelAndView.addObject("dictionaryList", dictionaryList);
		return commonModelAndView;
	}

	@Override
	public String uploadfile(ProductAttributeGroup entity, HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	@Override
	public String downloadfile(ProductAttributeGroup entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(ProductAttributeGroup entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView valid(ProductAttributeGroup entity, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
