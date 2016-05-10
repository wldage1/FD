package com.sw.plugins.market.fail.controller;

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
import com.sw.plugins.market.order.service.OrderHistoryService;
import com.sw.plugins.market.order.service.RedeemOrderService;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;

/**
 * 失败订单管理控制器
 * 
 * @author runchao
 */
@Controller
public class FailController extends BaseController<Order> {

	private static Logger logger = Logger.getLogger(FailController.class);
	
	@Resource
	private RedeemOrderService redeemOrderService;
	@Resource
	private OrderHistoryService orderHistoryService;
	@Resource
	private UserLoginService userLoginService;
	
	/**
	 * 失败订单列表查询
	 * 
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/fail")
	public CommonModelAndView list(Order order, HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, order, messageSource);
		return commonModelAndView;
	}
	
	/**
	 * 分页查询 认购/申购/赎回 失败订单
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/fail/grid")
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
				order.setIds(new String[]{"8","13"});
				map = orderHistoryService.getFailGrid(order);
			}else{
				redeemOrder.setTradeStatus((short)10);
				redeemOrder.setProductName(order.getProductName());
				redeemOrder.setMemberName(order.getMemberName());
				redeemOrder.setRows(order.getRows());
				redeemOrder.setPage(order.getPage());
				redeemOrder.setStart(order.getStart());
				redeemOrder.setOffset(order.getOffset());
				map = redeemOrderService.getFailGrid(redeemOrder);
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, order, request);
	}
	
	/**
	 * 失败的认购、申购订单查看详细
	 * 
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/fail/detail")
	public CommonModelAndView detail(Order order, HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, order, messageSource);
		try {
			order = orderHistoryService.getOne(order);
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
	 * 失败的赎回订单查看详细
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/fail/detailRedeem")
	public CommonModelAndView detailRedeem(RedeemOrder redeemOrder, HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, redeemOrder, messageSource);
		try {
			redeemOrder = redeemOrderService.getOneHistory(redeemOrder);
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
	
	@Override
	public String uploadfile(Order entity, HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
	@Override
	public String downloadfile(Order entity, HttpServletRequest request, HttpServletResponse response) {
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
