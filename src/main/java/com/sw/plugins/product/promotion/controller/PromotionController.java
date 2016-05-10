package com.sw.plugins.product.promotion.controller;

import java.util.List;
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
import com.sw.plugins.config.position.entity.Position;
import com.sw.plugins.config.position.service.PositionService;
import com.sw.plugins.product.manage.entity.Product;
import com.sw.plugins.product.manage.entity.ProductSeckill;
import com.sw.plugins.product.manage.service.ProductSeckillService;
import com.sw.plugins.product.manage.service.ProductService;
import com.sw.plugins.product.promotion.entity.ProductPositionMapping;
import com.sw.plugins.product.promotion.entity.Promotion;
import com.sw.plugins.product.promotion.service.ProductPositionMappingService;
import com.sw.plugins.product.promotion.service.PromotionService;

@Controller
public class PromotionController extends BaseController<Promotion> {

	private static Logger logger = Logger.getLogger(PromotionController.class);

	@Resource
	private PromotionService promotionService;
	@Resource
	private PositionService positionService;
	@Resource
	private ProductService productService;
	@Resource
	private ProductSeckillService secKillService;
	@Resource
	private ProductPositionMappingService positionMappingService;

	/**
	 * @param promotion
	 * @param request
	 *            positionId
	 * @return
	 */
	@RequestMapping("/product/promotion")
	public CommonModelAndView list(Promotion promotion, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, promotion, messageSource);
		List<Position> positionList = null;
		try {
			positionList = positionService.getList(new Position());
		} catch (Exception e) {
			logger.error(e);
		}
		commonModelAndView.addObject("positionList", positionList);
		commonModelAndView.addObject("positionListSize", positionList == null ? 0 : positionList.size());
		commonModelAndView.addObject("promotion", promotion);
		commonModelAndView.addObject("sellStatusList",this.initData.getSellStatus());
		return commonModelAndView;
	}

	@RequestMapping("/product/promotion/grid")
	public CommonModelAndView json(Promotion promotion, HttpServletRequest request) {
		Map<String, Object> JSONMap = null;
		try {
			JSONMap = promotionService.getGrid(promotion);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", JSONMap, promotion, request);
	}

	/**
	 * @param promotion
	 * @param request
	 *            List<productMapping>
	 * @return
	 */
	@RequestMapping("/product/promotion/updateorder")
	public CommonModelAndView savePositionMapping(ProductPositionMapping mapping, HttpServletRequest request) {
		String viewName = this.ERROR;
		try {
			positionMappingService.saveOrUpdate(mapping);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new CommonModelAndView(viewName, mapping, request, messageSource);
	}

	@RequestMapping("/product/promotion/deletemapping")
	public CommonModelAndView deletePositionMapping(ProductPositionMapping mapping, HttpServletRequest request) {
		String viewName = this.ERROR;
		try {
			if (mapping.getId() != null) {
				positionMappingService.delete(mapping);
				viewName = this.SUCCESS;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, mapping, request, messageSource);
		return commonModelAndView;
	}

	@RequestMapping("/product/promotion/seckill")
	public CommonModelAndView secKill(Promotion promotion, HttpServletRequest request, Map<String, Object> model) {
		String code = promotion.getC();
		Product product = null;
		ProductSeckill secKill = null;
		try {
			Long productId = promotion.getProductId();
			if (productId != null) {
				Product pt = new Product();
				pt.setId(productId);
				product = productService.getProductNameById(pt);
				if (product == null) {
					product = new Product();
				}
				ProductSeckill psecKill = new ProductSeckill();
				psecKill.setProductId(productId);
				secKill = secKillService.getOne(psecKill);
				if (secKill == null) {
					secKill = new ProductSeckill();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		product.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, promotion, messageSource);
		commonModelAndView.addObject("product", product);
		model.put("productSecKill", secKill);
		commonModelAndView.addAllObjects(model);
		return commonModelAndView;
	}

	@RequestMapping("/product/promotion/saveseckill")
	public CommonModelAndView saveProductSeckill(ProductSeckill productSeckill, HttpServletRequest request) {
		String view = "";
		try {
			secKillService.saveorupdate(productSeckill);
			view = this.SUCCESS;
		} catch (Exception e) {
			view = this.ERROR;
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView(view, productSeckill, request, messageSource);
	}

	@Override
	public String uploadfile(Promotion entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String downloadfile(Promotion entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(Promotion entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView valid(Promotion entity, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
