package com.sw.plugins.market.cancel.controller;

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
import com.sw.plugins.market.order.entity.RedeemOrder;
import com.sw.plugins.market.order.service.OrderService;
import com.sw.plugins.market.order.service.RedeemOrderService;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;

/**
 * 已取消订单管理控制器
 * 
 * @author runchao
 */
@Controller
public class CancelController extends BaseController<Order>{

	private static Logger logger = Logger.getLogger(CancelController.class);

	@Resource
	private OrderService orderService;
	@Resource
	private RedeemOrderService redeemOrderService;
	@Resource
	private UserLoginService userLoginService;
	
	/**
	 * 已取消订单查询
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/cancel")
	public CommonModelAndView list(Order order, HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, order, messageSource);
		commonModelAndView.addObject("pushStatusMap", this.initData.getPushStatus());
		return commonModelAndView;
	}
	
	/**
	 * 已取消订单列表，返回json
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/cancel/grid")
	public CommonModelAndView json(Order order, HttpServletRequest request){
		Map<String, Object> map = null;
		RedeemOrder redeemOrder = new RedeemOrder();
		try {
			User user = userLoginService.getCurrLoginUser();
			Organization organization = user.getSelfOrg();
			if (organization != null) {
				int isCommission = organization.getIsCommission();
				if (isCommission == 1) {
					order.setOrgID(organization.getId());
					redeemOrder.setOrgID(organization.getId());
				} else {
					order.setOrgID(null);
					redeemOrder.setOrgID(null);
				}
			}
			if(order.getTradeType() != null){
				order.setIds(new String[]{"8","9","10"});
				map = orderService.getCancelGrid(order);
			}else{
				redeemOrder.setIds(new String[]{"7","8"});
				redeemOrder.setProductName(order.getProductName());
				redeemOrder.setMemberName(order.getMemberName());
				redeemOrder.setRows(order.getRows());
				redeemOrder.setPage(order.getPage());
				redeemOrder.setStart(order.getStart());
				redeemOrder.setOffset(order.getOffset());
				map = redeemOrderService.getCancelGrid(redeemOrder);
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, order, request);
	}
	
	/**
	 * 已取消认购、追加订单查看详细
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/cancel/detail")
	public CommonModelAndView detail(Order order, HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, order, messageSource);
		try {
			order = orderService.getOne(order);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		commonModelAndView.addObject("order", order);
		//客户类型
		commonModelAndView.addObject("clientType", this.initData.getClientType());
		//交易类型
		commonModelAndView.addObject("tradeType", this.initData.getTradeType().get(0));
		//打款状态
		commonModelAndView.addObject("payProgress", this.initData.getPayProgress());
		//交易状态
		commonModelAndView.addObject("tradeStatus", this.initData.getTradeStatus().get(1));
		//单证状态 
		commonModelAndView.addObject("docStatus", this.initData.getDocStatus());
		//配给状态
		commonModelAndView.addObject("pushStatus", this.initData.getPushStatus());
		//证件类型
		commonModelAndView.addObject("iDCardType", this.initData.getMemberIDCardType());
		
		return commonModelAndView;
	}
	
	/**
	 * 已取消赎回订单查看详细
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/cancel/detailRedeem")
	public CommonModelAndView detailRedeem(RedeemOrder redeemOrder, HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, redeemOrder, messageSource);
		try {
			redeemOrder = redeemOrderService.getOne(redeemOrder);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		commonModelAndView.addObject("redeemOrder", redeemOrder);
		//客户类型
		commonModelAndView.addObject("clientType", this.initData.getClientType());
		//交易状态
		commonModelAndView.addObject("tradeStatus", this.initData.getTradeStatus().get(3));
		//单证状态
		commonModelAndView.addObject("documentStatus", this.initData.getDocumentStatus());
		//证件类型
		commonModelAndView.addObject("iDCardType", this.initData.getMemberIDCardType());
		
		return commonModelAndView;
	}
	
	/**
	 * 注销 已取消的订单
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/cancel/faildispose")
	public CommonModelAndView updateFailDispose(Order order,HttpServletRequest request) {
		String view = "";
		try {
			String arrid=request.getParameter("arrid");
			if(arrid!=null && !arrid.equals("")){
				order.setIds(arrid.split(","));
			}
			orderService.updateFailDispose(order);
			view = this.SUCCESS;
		} catch (Exception e) {
			view = this.ERROR;
			logger.error(DetailException.expDetail(e, getClass()));
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(view,order, request, messageSource);
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
