package com.sw.plugins.market.print.controller;

import java.util.ArrayList;
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
import com.sw.plugins.product.manage.entity.Product;
import com.sw.plugins.product.manage.service.ProductService;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;

@Controller
public class PrintController extends BaseController<Order>{
	private static Logger logger = Logger.getLogger(PrintController.class);
	
	@Resource
	private OrderService orderServie;
	@Resource
	private ProductService productService;
	@Resource
	private UserLoginService userLoginService;
	
	/**
	 * 列表初始化
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/print")
	public CommonModelAndView list(Order order,HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, order, messageSource);
		List<Product> productList =null;
		try {
			User user = userLoginService.getCurrLoginUser();
			Organization organization = user.getSelfOrg();
			Product product=new Product();
			if (organization != null) {
				int isCommission = organization.getIsCommission();
				if (isCommission == 1) {
					product.setOrgId(organization.getId());
				} else {
					product.setOrgId(null);
				}
			}
			productList = productService.getSallingProduct(product);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		commonModelAndView.addObject("productList",productList);
		return commonModelAndView;
	}
	
	/**
	 * 认购订单的额度确认完成订单信息
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/print/orderjson")
	public CommonModelAndView orderJson(Order order,HttpServletRequest request){
		Map<String,Object> map = null;
		try {
			User user = userLoginService.getCurrLoginUser();
			Organization organization = user.getSelfOrg();
			if (organization != null) {
				int isCommission = organization.getIsCommission();
				if (isCommission == 1) {
					order.setOrgID(organization.getId());
				} else {
					order.setOrgID(null);
				}
			}
			map = orderServie.getOrderTradeStatusList(order);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		CommonModelAndView commonModelAndView = new  CommonModelAndView("jsonView", map, order, request);
		return commonModelAndView;
	}
	
	@RequestMapping("/market/print/printview")
	public CommonModelAndView reprintview(Order order,HttpServletRequest request){
		String code = order.getC();
		List<Order> list = new ArrayList<Order>();
		try {
			 if(order.getIds()!=null){
				list = orderServie.getArrOverOrders(order);
			}else if(order.getId()!=null){
				order =  orderServie.getOneOverOrder(order);
				if(order!=null){
					list.add(order);
				}
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		order.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, order, messageSource);
		commonModelAndView.addObject("orders", list);
		return commonModelAndView;
	}
	
	@Override
	public String uploadfile(Order entity, HttpServletRequest request,HttpServletResponse response) {
		return null;
	}
	@Override
	public String downloadfile(Order entity, HttpServletRequest request,HttpServletResponse response) {
		return null;
	}
	@Override
	public CommonModelAndView functionJsonData(Order entity,HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
	@Override
	public CommonModelAndView valid(Order entity, BindingResult result,Map<String, Object> model, HttpServletRequest request,HttpServletResponse response) {
		return null;
	}
}
