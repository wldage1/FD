package com.sw.plugins.market.pushshare.controller;

import java.util.HashMap;
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
import com.sw.plugins.market.order.entity.Order;
import com.sw.plugins.market.order.service.OrderService;
import com.sw.plugins.market.salemanage.entity.SaleManagePlot;
import com.sw.plugins.market.salemanage.service.SaleManageService;
import com.sw.plugins.product.manage.entity.Product;
import com.sw.plugins.product.manage.entity.SubProduct;
import com.sw.plugins.product.manage.service.ProductService;
import com.sw.plugins.product.manage.service.SubProductService;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;

@Controller
public class PushShareController extends BaseController<Order> {

	private static Logger logger = Logger.getLogger(PushShareController.class);

	@Resource
	private OrderService orderService;
	@Resource
	private ProductService productService;
	@Resource
	private SubProductService subProductService;
	@Resource
	private SaleManageService saleManageService;
	@Resource
	private UserLoginService userLoginService;

	/**
	 * 配给订单页面
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping("market/productlist/pushshare")
	public CommonModelAndView pushList(Product product, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, product, messageSource);
		String c = product.getC();
		Long productId = product.getId();

		Product prd = null;
		SubProduct activeSubProduct = new SubProduct();
		List<SubProduct> subProductList = null;
		SaleManagePlot saleManagePlot = new SaleManagePlot();

		try {
			// 获取产品数据
			Product tprd = new Product();
			tprd.setId(productId);
			prd = productService.getOne(tprd);
			if (prd != null) {
				// 获取该产品下的未停止打款的子产品
				SubProduct tspd = new SubProduct();
				tspd.setProductId(productId);
				subProductList = subProductService.getSubProductList1(tspd);
				if (subProductList.size() != 0) {
					// 前台是否传递特定的子产品
					Long subProductId = product.getSubProductId();
					if (subProductId != null) {
						for (SubProduct tsp : subProductList) {
							Long spId = tsp.getId();
							if (spId.equals(subProductId)) {
								activeSubProduct = tsp;
							}
						}
					}
					// 否则默认第一个子产品
					else {
						activeSubProduct = subProductList.get(0);
					}
					// 获取产品概览统计数据
					saleManagePlot = saleManageService.getPushSharePlot(prd, activeSubProduct);
				}
			} else {
				prd = product;
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		commonModelAndView.addObject("product", prd);
		commonModelAndView.addObject("productID",productId);
		commonModelAndView.addObject("subProduct", activeSubProduct);
		commonModelAndView.addObject("subProductList", subProductList);
		commonModelAndView.addObject("plot", saleManagePlot);
		commonModelAndView.addObject("c", c);
		return commonModelAndView;
	}

	/**
	 * 配给产品列表页面
	 * 
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping("market/productlist")
	public CommonModelAndView productList(Product product, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, product, messageSource);
		commonModelAndView.addObject("productTypeMap", this.initData.getProductType());
		commonModelAndView.addObject("c", product.getC());
		commonModelAndView.addObject("product", product);
		return commonModelAndView;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("market/productlist/grid")
	public CommonModelAndView productGrid(Product product, HttpServletRequest request) {
		Object gridJson;
		Map<String, Object> map = null;
		try {
			User user = userLoginService.getCurrLoginUser();
			Organization uorg = user.getSelfOrg();
			if (uorg != null) {
				Integer isCo = uorg.getIsCommission();
				if (isCo.equals(1)) {
					product.setOrgId(uorg.getId());
				}
			}
			gridJson = productService.getSalingProductGrid(product);
			map = (gridJson == null ? null : (Map<String, Object>) gridJson);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, product, request);
	}


	/**
	 * 未配给列表
	 * 
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("market/pushshare/unpushshare")
	public CommonModelAndView unPushShareGrid(Order order, HttpServletRequest request){
		Map<String, Object> map = null;
		try {
			map = orderService.getUnPushShareGrid(order);
			if (map == null) {
				map = new HashMap<String, Object>();
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, order, request);
	}

	/**
	 * 已配给列表
	 * 
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("market/pushshare/pushshare")
	public CommonModelAndView pushShareGrid(Order order, HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			map = orderService.getPushShareGrid(order);
			if (map == null) {
				map = new HashMap<String, Object>();
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, order, request);
	}

	/**
	 * 未配给列表确认按钮操作
	 * 
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping("market/pushshare/saveshare")
	public CommonModelAndView saveShare(Order entity, HttpServletRequest request) {
		String view = null;
		try {
			orderService.updateShareByPushShare(entity);
			view = this.SUCCESS;
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
			view = this.ERROR;
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(view, entity, request, messageSource);
		return commonModelAndView;
	}

	/**
	 * 同意操作
	 * 
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping("market/pushshare/comfirmshare")
	public CommonModelAndView confirmShare(Order entity, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			orderService.confirmShare(entity);
			map.put(this.STATUS, this.STATUS_SUCCESS);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
			map.put(this.STATUS, this.STATUS_FALSE);
		}
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 拒绝操作
	 * 
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping("market/pushshare/refuse")
	public CommonModelAndView refuse(Order entity, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			orderService.refuse(entity);
			map.put(this.STATUS, this.STATUS_SUCCESS);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
			map.put(this.STATUS, this.STATUS_FALSE);
		}
		return new CommonModelAndView("jsonView", map);
	}

	@Override
	public String uploadfile(Order entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String downloadfile(Order entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(Order entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView valid(Order entity, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
