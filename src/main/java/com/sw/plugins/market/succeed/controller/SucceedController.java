package com.sw.plugins.market.succeed.controller;

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
 * 成功订单管理控制器
 * 
 * @author liushuai
 */
@Controller
public class SucceedController extends BaseController<Order>{

	private static Logger logger = Logger.getLogger(SucceedController.class);

	@Resource
	private RedeemOrderService redeemOrderService;
	@Resource
	private OrderHistoryService orderHistoryService;
	@Resource
	private UserLoginService userLoginService;
	
	/**
	 * 成功订单查询
	 * 
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/succeed")
	public CommonModelAndView list(Order order, HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, order, messageSource);
		//在途订单交易状态
		commonModelAndView.addObject("tradeStatusMap",this.initData.getTradeStatus().get(0));
		//订单单证状态
		commonModelAndView.addObject("docStatusMap",this.initData.getDocStatus());
		//订单单证邮寄状态
		commonModelAndView.addObject("documentStatusMap",this.initData.getDocumentStatus());
		//交易类型
		commonModelAndView.addObject("tradeTypeMap",this.initData.getTradeType().get(0));
		return commonModelAndView;
	}
	
	/**
	 * 成功订单列表，返回json
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/succeed/grid")
	public CommonModelAndView subscribe(Order order, HttpServletRequest request){
		Map<String, Object> map = null;
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
			map = orderHistoryService.getGrid(order);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, order, request);
	}
	
	/**
	 * 获取赎回成功订单列表数据
	 * @param redeemorder
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/succeed/redeemordergrid")
	public CommonModelAndView redeemorderjson(RedeemOrder redeemorder,HttpServletRequest request){
		Map<String, Object> map = null;
		try {
			User user = userLoginService.getCurrLoginUser();
			Organization organization = user.getSelfOrg();
			if (organization != null) {
				int isCommission = organization.getIsCommission();
				if (isCommission == 1) {
					redeemorder.setOrgID(organization.getId());
				} else {
					redeemorder.setOrgID(null);
				}
			}
			map = redeemOrderService.getSucceedGrid(redeemorder);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, redeemorder, request);
	}
	
	/**
	 * 成功认购、追加订单查看详细
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/succeed/detail")
	public CommonModelAndView detail(Order order, HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, order, messageSource);
		try {
			order = orderHistoryService.getOne(order);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		commonModelAndView.addObject("order",order);
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
		commonModelAndView.addObject("iDCardTypeMap", this.initData.getMemberIDCardType());
		
		return commonModelAndView;
	}
	
	/**
	 *  成功赎回订单查看详细
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/succeed/redeemDetail")
	public CommonModelAndView detailRedeem(RedeemOrder redeemOrder, HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, redeemOrder, messageSource);
		try {
			redeemOrder = redeemOrderService.getOneHistory(redeemOrder);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		commonModelAndView.addObject("redeemOrder", redeemOrder);
		//客户类型
		commonModelAndView.addObject("clientType", this.initData.getClientType());
		//交易状态
		commonModelAndView.addObject("tradeStatus", this.initData.getTradeStatus().get(3));
		//单证邮寄状态
		commonModelAndView.addObject("documentStatus", this.initData.getDocumentStatus());
		//证件类型
		commonModelAndView.addObject("iDCardTypeMap", this.initData.getMemberIDCardType());
		
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
	public CommonModelAndView functionJsonData(Order entity, HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
	@Override
	public CommonModelAndView valid(Order entity, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
}
