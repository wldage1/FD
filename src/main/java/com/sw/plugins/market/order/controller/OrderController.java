package com.sw.plugins.market.order.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import jxl.Workbook;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sw.core.controller.BaseController;
import com.sw.core.controller.CommonModelAndView;
import com.sw.core.exception.DetailException;
import com.sw.plugins.customer.member.entity.Member;
import com.sw.plugins.customer.member.service.MemberService;
import com.sw.plugins.market.order.entity.HoldingProduct;
import com.sw.plugins.market.order.entity.Order;
import com.sw.plugins.market.order.entity.OrderAcountAffirmDetail;
import com.sw.plugins.market.order.entity.OrderAttachment;
import com.sw.plugins.market.order.entity.OrderProof;
import com.sw.plugins.market.order.entity.RedeemOrder;
import com.sw.plugins.market.order.entity.RedeemOrderAttachment;
import com.sw.plugins.market.order.service.OrderAcountAffirmDetailService;
import com.sw.plugins.market.order.service.OrderAttachmentService;
import com.sw.plugins.market.order.service.OrderHistoryService;
import com.sw.plugins.market.order.service.OrderHoldingProductService;
import com.sw.plugins.market.order.service.OrderProofService;
import com.sw.plugins.market.order.service.OrderService;
import com.sw.plugins.market.order.service.RedeemOrderAttachmentService;
import com.sw.plugins.market.order.service.RedeemOrderService;
import com.sw.plugins.product.manage.entity.ProductProfit;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;
import com.sw.util.SystemProperty;

@Controller
public class OrderController extends BaseController<Order> {

	private static Logger logger = Logger.getLogger(OrderController.class);

	@Resource
	private OrderService orderService;
	@Resource
	private RedeemOrderService redeemOrderService;
	@Resource
	private OrderAttachmentService orderAttachmentService;
	@Resource
	private RedeemOrderAttachmentService redeemOrderAttachmentService;
	@Resource
	private MemberService memberService;
	@Resource
	private OrderProofService orderProofService;
	@Resource
	private OrderAcountAffirmDetailService orderAcountAffirmDetailService;
	@Resource
	private OrderHoldingProductService holdingProductService;
	@Resource
	private OrderHistoryService orderHistoryService;
	@Resource
	private UserLoginService userLoginService;

	/**
	 * 初始化订单管理页面
	 * 
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order")
	public CommonModelAndView initOrder(Order order, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request,order, messageSource);
		// 在途订单交易状态
		commonModelAndView.addObject("tradeStatusMap", this.initData.getTradeStatus().get(2));
		// 在途订单赎回 订单交易状态
		commonModelAndView.addObject("tradeStatusRedeemMap", this.initData.getTradeStatus().get(4));
		// 订单单证状态
		commonModelAndView.addObject("docStatusMap", this.initData.getDocStatus());
		// 订单单证邮寄状态
		commonModelAndView.addObject("documentStatusMap", this.initData.getDocumentStatus());
		// 交易类型
		commonModelAndView.addObject("tradeTypeMap", this.initData.getTradeType().get(0));
		commonModelAndView.addObject("order", order);
		String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
		commonModelAndView.addObject("ftpHttpUrl", ftpHttpUrl);
		return commonModelAndView;
	}

	/**
	 * 追加/认购订单的列表 JSON数据
	 * 
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/grid")
	public CommonModelAndView grid(Order order, HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			User user = userLoginService.getCurrLoginUser();
			Integer type = user.getType();
			String position = user.getPosition();
			Organization organization = user.getSelfOrg();
			if (organization != null) {
				if (position != null && position.contains(",2,")) {
					Integer isCo = organization.getIsCommission();
					if (isCo.equals(1)) {
						order.setOrgID(organization.getId());
						order.setSalerType((short)2);
					}else{
						order.setSalerType((short)1);
					}
				}
			}
			if(type == 0 || (position != null && position.contains(",2,"))){
				order.setIsScManager((short)1);
			}
			map = orderService.getGrid(order);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, order, request);
	}

	/**
	 * 初始订单详细页面
	 * 
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/detail")
	public CommonModelAndView initDetail(Order order, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request,order, messageSource);
		try {
			Order order2 = orderService.getOne(order);
			order2.setC(order.getC());
			commonModelAndView.addObject("order", order2);
			// 交易状态
			commonModelAndView.addObject("tradeStatusMap", this.initData.getTradeStatus().get(1));
			// 打款状态
			commonModelAndView.addObject("payProgressMap", this.initData.getPayProgress());
			// 单证状态
			commonModelAndView.addObject("docStatusMap", this.initData.getDocStatus());
			//客户类型
			commonModelAndView.addObject("clientTypeMap", this.initData.getClientType());
			//证件类型
			commonModelAndView.addObject("iDCardTypeMap", this.initData.getMemberIDCardType());
			//交易类型
			commonModelAndView.addObject("tradeTypeMap", this.initData.getTradeType().get(0));
			//配给状态
			commonModelAndView.addObject("pushStatusMap",this.initData.getPushStatus());
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return commonModelAndView;
	}

	/**
	 * 初始订单修改页面
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/modify")
	public CommonModelAndView initModify(Order order,HttpServletRequest request, ModelMap model) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request,order, messageSource);
		try {
			Order order2 = orderService.getOne(order);
			order2.setC(order.getC());
			// 客户类型
			commonModelAndView.addObject("clientTypeMap", this.initData.getClientType());
			// 证件类型
			commonModelAndView.addObject("iDCardTypeMap", this.initData.getMemberIDCardType());
			if (order2.getTradeType().equals((short) 1)) {
				// 认购 交易类型
				commonModelAndView.addObject("tradeTypeMap", this.initData.getTradeType().get(1));
			} else {
				// 申购 交易类型
				commonModelAndView.addObject("tradeTypeMap", this.initData.getTradeType().get(2));
			}
			// 交易状态
			commonModelAndView.addObject("tradeStatusMap", this.initData.getTradeStatus().get(0));
			// 资金状态
			commonModelAndView.addObject("payProgressMap", this.initData.getPayProgress());
			// 单证状态
			commonModelAndView.addObject("documentStatusMap", this.initData.getDocumentStatus());
			// 居间费支付状态
			commonModelAndView.addObject("payStatusMap", this.initData.getPayStatus());
			// 配给状态
			commonModelAndView.addObject("pushStatusMap", this.initData.getPushStatus());

			commonModelAndView.addObject("order", order2);
			model.put("order", order2);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return commonModelAndView;
	}

	/**
	 * 保存/修改订单
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/saveorupdate")
	public CommonModelAndView saveorupdate(Order order,HttpServletRequest request) {
		String view = "";
		try {
			orderService.saveOrupdate(order);
			view = this.SUCCESS;
		} catch (Exception e) {
			view = this.ERROR;
			logger.error(DetailException.expDetail(e, getClass()));
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(view,order, request, messageSource);
		return commonModelAndView;
	}
	
	/**
	 * 查询预期收益率
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/countExpectProfitRatio")
	public CommonModelAndView countExpectProfitRatio(Order order, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			ProductProfit productProfit = orderService.countExpectProfitRatio(order);
			if(productProfit==null){
				map.put("profitRatio", 0);
			}else{
				map.put("profitRatio", productProfit.getProfitRatio());
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 额度配给
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/updatePushShare")
	public CommonModelAndView updatePushShare(Order order,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			orderService.updatePushShare(order);
			map.put("zt", "success");
		} catch (Exception e) {
			map.put("zt", "error");
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 平台确认打款收到
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/confirmPay")
	public CommonModelAndView confirmPay(Order order,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			orderService.updateConfirmPay(order);
			map.put("zt", "success");
		} catch (Exception e) {
			map.put("zt", "error");
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
	
	
	/**
	 * 撤销订单
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/repeal")
	public CommonModelAndView repealOrder(Order order,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			orderService.updateRepealOrder(order);
			map.put("zt", "success");
		} catch (Exception e) {
			map.put("zt", "error");
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 初始化 补录订单页面
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/create")
	public CommonModelAndView initCreate(Order order, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request,order, messageSource);
		try {
			// 客户类型
			commonModelAndView.addObject("clientTypeMap", this.initData.getClientType());
			// 证件类型
			commonModelAndView.addObject("iDCardTypeMap", this.initData.getMemberIDCardType());
			if (order.getTradeType().equals((short) 1)) {
				// 交易类型
				commonModelAndView.addObject("tradeTypeMap", this.initData.getTradeType().get(1));
			} else {
				// 交易类型
				commonModelAndView.addObject("tradeTypeMap", this.initData.getTradeType().get(2));
			}
			// 交易状态
			commonModelAndView.addObject("tradeStatusMap", this.initData.getTradeStatus().get(0));
			// 资金状态
			commonModelAndView.addObject("payProgressMap", this.initData.getPayProgress());
			// 单证状态
			commonModelAndView.addObject("documentStatusMap", this.initData.getDocumentStatus());
			// 居间费支付状态
			commonModelAndView.addObject("payStatusMap", this.initData.getPayStatus());
			// 配给状态
			commonModelAndView.addObject("pushStatusMap", this.initData.getPushStatus());

			commonModelAndView.addObject("order", order);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return commonModelAndView;
	}

	/**
	 * 导出数据到EXCEL
	 * @param order
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/market/order/export")
	public @ResponseBody Map<String, Object> dataExport(Order order,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		String colName ="订单号,产品类型,产品简称,受益权类型,发行机构,居间公司,客户名称,预约日期,投资金额（万元）,预计打款时间,理财顾问,所属机构,交易类型,订单状态,单证状态,到账日期,到账金额,居间费比例";
		String colModel = "orderNumber,productType,productShortName,subProductType,providerShortName,orgShortName,clientName,orderTime,investAmount,commissionPayTime,memberName,McOrgName,tradeType,tradeStatusName,docStatusName,payTime,payAmount,commission";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String temp = sdf.format(new Date());
		//Calendar cal=Calendar.getInstance();
		//String pathDate = Constant.DIRSPLITER+cal.get(Calendar.YEAR)+Constant.DIRSPLITER+String.valueOf(cal.get(Calendar.MONTH)+1)+Constant.DIRSPLITER+cal.get(Calendar.DATE);
		//String path = Constant.APPLICATIONPATH+initData.getUploadDir()+Constant.DIRSPLITER+initData.getUploadTmpDir()+pathDate;
		//File excelFile = new File(path,temp + ".xls");
		//File f = excelFile.getParentFile();
		//if(f!=null&&!f.exists()){ 
		//	f.mkdirs();
		//}
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String, Object> tradeStatusMap=this.initData.getTradeStatus().get(2);
			Map<String, Object> documentStatusMap=this.initData.getDocStatus();
			Map<String, Object> tradeTypeMap=this.initData.getTradeType().get(0);
			Map<String, Object> ProductTypeMap=this.initData.getProductType();
			WritableWorkbook excel = null;
			response.reset();
			response.setHeader("Content-disposition", "attachment; filename = Order" + temp + ".xls");
			response.setContentType("application/msexcel");
			excel = Workbook.createWorkbook(response.getOutputStream());
			
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
			
			excel = orderService.exportExcel(order.getC(), "", colName, colModel, excel, order,tradeStatusMap,documentStatusMap,tradeTypeMap,ProductTypeMap);
			excel.write();
			excel.close();
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception e) {
			map.put("status", 0);
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return map;
	}      
	
	/**
	 * 初始化理财顾问列表
	 * @param member
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/member")
	public CommonModelAndView initMember(Member member,HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView("/market/order/member");
		commonModelAndView.addObject("iframeID", request.getParameter("iframeID"));
		return commonModelAndView;
	}

	/**
	 * 初始化历史订单表
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/orderHistory")
	public CommonModelAndView initHistory(Order order,HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView("/market/order/orderHistory");
		commonModelAndView.addObject("iframeID", request.getParameter("iframeID"));
		return commonModelAndView;
	}

	/**
	 * 初始化理财顾问列表 数据
	 * @param member
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/memberGrid")
	public CommonModelAndView memberGrid(Member member,HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			map = memberService.getGridForOrdermember(member);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 存续产品列表（查询合同号）
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/historyGrid")
	public CommonModelAndView historyGrid(HoldingProduct product,HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			User user = userLoginService.getCurrLoginUser();
			Organization organization = user.getSelfOrg();
			if (organization != null) {
				int isCommission = organization.getIsCommission();
				if (isCommission == 1) {
					product.setOrgID(organization.getId());
				} else {
					product.setOrgID(null);
				}
			}	
			map = holdingProductService.getGrid(product);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 验证产品小额投资人数
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/checkMinOrder")
	public CommonModelAndView checkMinOrder(Order order,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String,Object> temp=orderService.checkMinOrder(order);
			if(temp!=null){
				if(temp.get("statu")!=null && temp.get("statu").equals("no")){
					map.put("isOrno", "no");
				}else{
					map.put("isOrno", "yes");
				}
			}else{
				map.put("isOrno", "yes");
			}
			map.put("zt", "success");
		} catch (Exception e) {
			map.put("zt", "error");
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 设置订单销售状态/打款状态/单证状态
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/setAllStatus")
	public CommonModelAndView setAllStatus(Order order,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			orderService.updateSetTradeStatus(order);
			map.put("zt", "success");
		} catch (Exception e) {
			map.put("zt", "error");
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 禁用
	 * 初始化认购订单 上传单证页面
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/detail/uploadDocument")
	public CommonModelAndView uploadDocument(Order order,HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView();
		try {
			order = orderService.getOne(order);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		commonModelAndView.addObject("order", order);
		commonModelAndView.addObject("iframeID", request.getParameter("iframeID"));
		return commonModelAndView;
	}

	/**
	 * 禁用
	 * 认购订单上传单证
	 * @param orderAttachment
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/market/order/uploadDocument/uploadfile")
	public String uploadDocumentFile(OrderAttachment orderAttachment,HttpServletRequest request, HttpServletResponse response) {
		try {
			response.getWriter().print(orderAttachmentService.upload(orderAttachment, request));
			return null;
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return ERROR;
	}

	/**
	 * 赎回订单上传页面
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/redeemDetail/redeemUploadDocument")
	public CommonModelAndView redeemUploadDocument(RedeemOrder order,HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView();
		String c = order.getC();
		try {
			order = redeemOrderService.getOne(order);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		commonModelAndView.addObject("c", c);
		commonModelAndView.addObject("redeemOrder", order);
		commonModelAndView.addObject("iframeID", request.getParameter("iframeID"));
		return commonModelAndView;
	}
	
	/**
	 * 赎回订单份额确认
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/updateShare")
	public CommonModelAndView updateShare(RedeemOrder order,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			redeemOrderService.update(order);
			map.put(this.STATUS, this.STATUS_SUCCESS);
		} catch (Exception e) {
			map.put(this.STATUS, this.STATUS_FALSE);
		}
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 查询存续产品
	 * @param product
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/selectHoldingProduct")
	public CommonModelAndView selectHoldingProduct(HoldingProduct product,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			User user = userLoginService.getCurrLoginUser();
			Organization organization = user.getSelfOrg();
			if (organization != null) {
				int isCommission = organization.getIsCommission();
				if (isCommission == 1) {
					product.setOrgID(organization.getId());
				} else {
					product.setOrgID(null);
				}
			}	
			map = holdingProductService.getGrid(product);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 获取订单对应的单证信息
	 * @param orderAttachment
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/getAttachment")
	public CommonModelAndView getAttachment(OrderAttachment orderAttachment,HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			map = orderAttachmentService.getGrid(orderAttachment);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, orderAttachment, request);
	}

	/**
	 * 禁用
	 * 保存单证上传信息    
	 * @param orderAttachment
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/saveOrderAttachment")
	public CommonModelAndView saveOrderAttachment(OrderAttachment orderAttachment, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			orderAttachmentService.saveOrderAttachment(orderAttachment);
			map.put("zt", "success");
		} catch (Exception e) {
			map.put("zt", "error");
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 保存赎回订单单证上传信息
	 * @param redeemorderAttachment
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/saveRedeemOrderAttachment")
	public CommonModelAndView saveRedeemOrderAttachment(RedeemOrderAttachment redeemorderAttachment,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			redeemOrderAttachmentService.saveRedeemOrderAttachment(redeemorderAttachment);
			map.put(this.STATUS, this.STATUS_SUCCESS);
		} catch (Exception e) {
			map.put(this.STATUS, this.STATUS_FALSE);
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 获取赎回订单列表数据
	 * @param redeemorder
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/redeemordergrid")
	public CommonModelAndView redeemorderjson(RedeemOrder redeemorder,HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			User user = userLoginService.getCurrLoginUser();
			Integer type = user.getType();
			String position = user.getPosition();
			Organization organization = user.getSelfOrg();
			if (organization != null) {
				if (position != null && position.contains(",2,")) {
					Integer isCo = organization.getIsCommission();
					if (isCo.equals(1)) {
						redeemorder.setOrgID(organization.getId());
						redeemorder.setSalerType((short)2);
					}else{
						redeemorder.setSalerType((short)1);
					}
				}
			}
			if(type == 0 || (position != null && position.contains(",2,"))){
				redeemorder.setIsScManager((short)1);
			}
			map = redeemOrderService.getGrid(redeemorder);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, redeemorder, request);
	}

	/**
	 * 跳转赎回订单修改页面
	 * @param order
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/market/order/redeemModify")
	public CommonModelAndView redeemOrderModify(RedeemOrder order,HttpServletRequest request, ModelMap model) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request,order, messageSource);
		try {
			RedeemOrder redeemOrder = redeemOrderService.getOne(order);
			redeemOrder.setC(order.getC());
			if (redeemOrder.getTradeStatus() != null && redeemOrder.getDocumentStatus()!=null) {
				redeemOrder.setTradeStatusName(this.initData.getTradeStatus().get(3).get(redeemOrder.getTradeStatus().toString()).toString());
			}
			if (redeemOrder.getDocumentStatus() != null && redeemOrder.getDocumentStatus()!=null) {
				redeemOrder.setDocumentStatusName(this.initData.getDocumentStatus().get(redeemOrder.getDocumentStatus().toString()).toString());
			}

			// 客户类型
			commonModelAndView.addObject("clientTypeMap", this.initData.getClientType());
			// 证件类型
			commonModelAndView.addObject("iDCardTypeMap", this.initData.getMemberIDCardType());
			// 交易状态
			commonModelAndView.addObject("tradeStatusMap", this.initData.getTradeStatus().get(5));
			// 资金状态
			commonModelAndView.addObject("payProgressMap", this.initData.getPayProgress());
			// 单证状态
			commonModelAndView.addObject("documentStatusMap", this.initData.getDocumentStatus());
			// 居间费支付状态
			commonModelAndView.addObject("payStatusMap", this.initData.getPayStatus());

			commonModelAndView.addObject("redeemOrder", redeemOrder);
			model.put("redeemOrder", redeemOrder);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return commonModelAndView;
	}

	/**
	 * 跳转赎回订单详细页面
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/redeemDetail")
	public CommonModelAndView redeemOrderDetail(RedeemOrder order,HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request,order, messageSource);
		try {
			String c = order.getC();
			order = redeemOrderService.getOne(order);
			order.setC(c);
			if (order.getTradeStatus()!=null && order.getTradeStatus()!=null) {
				order.setTradeStatusName(this.initData.getTradeStatus().get(3).get(order.getTradeStatus().toString()).toString());
			}
			if (order.getDocumentStatus()!=null && order.getDocumentStatus()!=null) {
				order.setDocumentStatusName(this.initData.getDocumentStatus().get(order.getDocumentStatus().toString()).toString());
			}
			if(order.getiDCardType()!=null && order.getiDCardType()!=null){
				order.setiDCardTypeName(this.initData.getMemberIDCardType().get(order.getiDCardType().toString()).toString());
			}
			if(order.getClientType()!=null && order.getiDCardType()!=null){
				order.setClientTypeName(this.initData.getClientType().get(order.getClientType().toString()).toString());
			}
			commonModelAndView.addObject("redeemOrder",order);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return commonModelAndView;
	}

	/**
	 * 赎回订单撤销操作
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/redeemRepeal")
	public CommonModelAndView repealRedeemOrder(RedeemOrder order,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (order.getId() != null) {
			try {
				redeemOrderService.updateRepealRedeemOrder(order);
				map.put(this.STATUS, this.STATUS_SUCCESS);
			} catch (Exception e) {
				map.put(this.STATUS, this.STATUS_FALSE);
			}
		}
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 保存操作
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/saveredeem")
	public CommonModelAndView saveredeem(RedeemOrder order,HttpServletRequest request) {
		String viewName = "";
		try {
			redeemOrderService.saveorupdate(order);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, order, request, messageSource);
		return commonModelAndView;
	}

	/**
	 * 赎回订单补录操作
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/redeemCreate")
	public CommonModelAndView redeemCreate(RedeemOrder order,HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request,order, messageSource);
		try {
			// 客户类型
			commonModelAndView.addObject("clientTypeMap", this.initData.getClientType());
			// 证件类型
			commonModelAndView.addObject("iDCardTypeMap", this.initData.getMemberIDCardType());
			// 交易状态
			commonModelAndView.addObject("tradeStatusMap", this.initData.getTradeStatus().get(5));
			// 资金状态
			commonModelAndView.addObject("payProgressMap", this.initData.getPayProgress());
			// 单证状态
			commonModelAndView.addObject("documentStatusMap", this.initData.getDocumentStatus());
			// 居间费支付状态
			commonModelAndView.addObject("payStatusMap", this.initData.getPayStatus());

			commonModelAndView.addObject("redeemOrder", order);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return commonModelAndView;
	}

	/**
	 * 点击成功订单按钮操作按钮-赎回成功
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/redeemSuccess")
	public CommonModelAndView redeemSuccess(RedeemOrder order,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			redeemOrderService.updateRedeemSuccess(order);
			map.put(this.STATUS, this.STATUS_SUCCESS);
		} catch (Exception e) {
			map.put(this.STATUS, this.STATUS_FALSE);
		}
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 点击失败订单操作
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/redeemFailed")
	public CommonModelAndView redeemFailed(RedeemOrder order,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			redeemOrderService.updateRedeemFailed(order);
			map.put(this.STATUS, this.STATUS_SUCCESS);
		} catch (Exception e) {
			map.put(this.STATUS, this.STATUS_FALSE);
		}
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 获取认购订单中的  打款凭证信息
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/hasOrNotDisposeGrid")
	public CommonModelAndView hasOrNotDisposeGrid(Order order,HttpServletRequest request){
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
			map = orderService.getHasOrNotDisposeGrid(order);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, order, request);
	}

	/**
	 * 初始化资金确认（打款凭证）列表
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/detail/fundConfirm")
	public CommonModelAndView intoFundConfirm(OrderProof orderProof,HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request,orderProof, messageSource);
		Order order = new Order();
		order.setOrderNumber(orderProof.getOrderNumber());
		try {
			order = orderService.getOneByNum(order);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		commonModelAndView.addObject("orderID", order.getId());
		commonModelAndView.addObject("orderProof", orderProof);
		commonModelAndView.addObject("proofStatusMap", this.initData.getProofStatus());
		String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
		commonModelAndView.addObject("ftpHttpUrl", ftpHttpUrl);
		return commonModelAndView;
	}

	/**
	 * 打款凭证 列表 Json 数据
	 * @param orderProof
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/order/detail/fundConfirmGrid")
	public CommonModelAndView fundConfirmGrid(OrderProof orderProof,HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			map = orderProofService.getGrid(orderProof);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, orderProof, request);
	}

	/**
	 * 上传凭证
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("market/order/detail/fundConfirm/uploadProof")
	public CommonModelAndView uploadProof(OrderProof orderProof,HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request,orderProof, messageSource);
		return commonModelAndView;
	}

	/**
	 * 上传打款凭证
	 * @param orderProof
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("market/order/uploadProof/uploadfile")
	public String uploadProof(OrderProof orderProof,HttpServletRequest request, HttpServletResponse response) {
		try {
			response.getWriter().print(orderProofService.upload(orderProof, request));
			return null;
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return ERROR;
	}
	
	/**
	 * 凭证上传气泡验证
	 * 
	 * @param orderProof
	 * @param result
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("market/order/uploadProof/valid")
	public CommonModelAndView uploadProofValid(@Valid OrderProof orderProof, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response){
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		if(orderProof.getProofAmount()!=null){
			if(orderProof.getProofAmount().compareTo(BigDecimal.valueOf(0))==0){
				result.rejectValue("proofAmount", "NotZero");
			}
		}
		if (orderProof.getProofTime() == null || orderProof.getProofTime().equals("")) {
			result.rejectValue("proofTime", "NotEmpty");
		}
		if (result.hasErrors()) {
			commonModelAndView.addBindingErrors(model, result, messageSource);
		}
		return commonModelAndView;
	}
	
	/**
	 * 保存订单的打款凭证
	 * @param orderProof
	 * @param request
	 * @return
	 */
	@RequestMapping("market/order/detail/fundConfirm/saveProof")
	public CommonModelAndView saveProof(OrderProof orderProof,HttpServletRequest request) {
		String viewName = null;
		String code = orderProof.getC();
		try {
			orderProofService.save(orderProof);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
			viewName = this.ERROR;
		}
		orderProof.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, orderProof, request, messageSource);
		return commonModelAndView;
	}

	/**
	 * 初始化打款凭证 处理
	 * @param orderProof
	 * @param request
	 * @return
	 */
	@RequestMapping("market/order/detail/fundConfirm/dispose")
	public CommonModelAndView intoDispose(OrderProof orderProof,HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request,orderProof, messageSource);
		try {
			orderProof = orderProofService.getOne(orderProof);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		commonModelAndView.addObject("orderProof", orderProof);
		commonModelAndView.addObject("iframeID", request.getParameter("iframeID"));
		commonModelAndView.addObject("typeMap", this.initData.getProofType());
		commonModelAndView.addObject("operateTypeMap", this.initData.getOperateType());
		String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
		commonModelAndView.addObject("ftpHttpUrl", ftpHttpUrl);
		return commonModelAndView;
	}

	/**
	 * 获取订单的 确认打款凭证记录 JSON
	 * @param orderAcountAffirmDetail
	 * @param request
	 * @return
	 */
	@RequestMapping("market/order/detail/fundConfirm/getOrderAcountAffirmDetailGrid")
	public CommonModelAndView getOrderAcountAffirmDetailGrid(OrderAcountAffirmDetail orderAcountAffirmDetail,HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			map = orderAcountAffirmDetailService.getGrid(orderAcountAffirmDetail);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 打款凭证 （金额确认）
	 * @param orderAcountAffirmDetail
	 * @param request
	 * @return
	 */
	@RequestMapping("market/order/detail/fundConfirm/dispose/addto")
	public CommonModelAndView bunkAffirm(OrderAcountAffirmDetail orderAcountAffirmDetail,HttpServletRequest request) {
		String view = "";
		try {
			orderAcountAffirmDetailService.updateProofAndSaveAcount(orderAcountAffirmDetail);
			view = "success";
		} catch (Exception e) {
			view = "error";
			logger.error(DetailException.expDetail(e, getClass()));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("zt", view);
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 打款凭证 （拒绝）
	 * @param orderAcountAffirmDetail
	 * @param request
	 * @return
	 */
	@RequestMapping("market/order/detail/fundConfirm/dispose/refuse")
	public CommonModelAndView refuse(OrderAcountAffirmDetail orderAcountAffirmDetail,HttpServletRequest request) {
		String view = "";
		try {
			OrderProof orderProof=new OrderProof();
			orderProof.setId(orderAcountAffirmDetail.getProofID());
			orderProof.setProofStatus((short) 1);
			orderProof.setComfirm(orderAcountAffirmDetail.getComfirm());
			orderProof.setRemark(orderAcountAffirmDetail.getRemark());
			orderProofService.update(orderProof);
			view = "success";
		} catch (Exception e) {
			view = "error";
			logger.error(DetailException.expDetail(e, getClass()));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("zt", view);
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 打款凭证 （复核）
	 * @param orderAcountAffirmDetail
	 * @param request
	 * @return
	 */
	@RequestMapping("market/order/detail/fundConfirm/dispose/recheck")
	public CommonModelAndView bunkRecheck(OrderAcountAffirmDetail orderAcountAffirmDetail,HttpServletRequest request) {
		String view = "";
		try {
			orderAcountAffirmDetailService.saveAcountAffirmDetail(orderAcountAffirmDetail);
			view = "success";
		} catch (Exception e) {
			view = "error";
			logger.error(DetailException.expDetail(e, getClass()));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("zt", view);
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 打款凭证 （修改）
	 * @param orderAcountAffirmDetail
	 * @param request
	 * @return
	 */
	@RequestMapping("market/order/detail/fundConfirm/dispose/modifyTwo")
	public CommonModelAndView bunkModifyTwo(OrderAcountAffirmDetail orderAcountAffirmDetail,HttpServletRequest request) {
		String view = "";
		try {
			orderAcountAffirmDetailService.saveAcountAndUpdateOrder(orderAcountAffirmDetail);
			view = "success";
		} catch (Exception e) {
			view = "error";
			logger.error(DetailException.expDetail(e, getClass()));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("zt", view);
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 初始化 银行流水确认
	 * @param orderProof
	 * @param request
	 * @return
	 */
	@RequestMapping("market/order/detail/bankStreamConfirm")
	public CommonModelAndView initBankStreamConfirm(Order order,HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request,order, messageSource);
		try {
			order = orderService.getOne(order);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		commonModelAndView.addObject("order", order);
		commonModelAndView.addObject("iframeID", request.getParameter("iframeID"));
		commonModelAndView.addObject("typeMap", this.initData.getProofType());
		commonModelAndView.addObject("operateTypeMap", this.initData.getOperateType());
		commonModelAndView.addObject("proofStatusMap", this.initData.getProofStatus());
		String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
		commonModelAndView.addObject("ftpHttpUrl", ftpHttpUrl);
		return commonModelAndView;
	}

	/**
	 * 银行流水（金额确认）
	 * @param orderAcountAffirmDetail
	 * @param request
	 * @return
	 */
	@RequestMapping("market/order/detail/bankStreamConfirm/addto")
	public CommonModelAndView bankStreamConfirm(OrderAcountAffirmDetail orderAcountAffirmDetail,HttpServletRequest request) {
		String view = "";
		try {
			orderAcountAffirmDetailService.updateOrderAndSaveAcount(orderAcountAffirmDetail);
			view = "success";
		} catch (Exception e) {
			view = "error";
			logger.error(DetailException.expDetail(e, getClass()));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("zt", view);
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 银行流水 （复核）
	 * @param orderAcountAffirmDetail
	 * @param request
	 * @return
	 */
	@RequestMapping("market/order/detail/bankStreamConfirm/recheck")
	public CommonModelAndView bankStreamConfirmRecheck(OrderAcountAffirmDetail orderAcountAffirmDetail,HttpServletRequest request) {
		String view = "";
		try {
			orderAcountAffirmDetailService.saveAcountAffirmDetail(orderAcountAffirmDetail);
			view = "success";
		} catch (Exception e) {
			view = "error";
			logger.error(DetailException.expDetail(e, getClass()));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("zt", view);
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 银行流水 （修改）
	 * @param orderAcountAffirmDetail
	 * @param request
	 * @return
	 */
	@RequestMapping("market/order/detail/bankStreamConfirm/modifyTwo")
	public CommonModelAndView bankStreamConfirmModifyTwo(OrderAcountAffirmDetail orderAcountAffirmDetail,HttpServletRequest request) {
		String view = "";
		try {
			orderAcountAffirmDetailService.saveAcountAndUpdateOrder(orderAcountAffirmDetail);
			view = "success";
		} catch (Exception e) {
			view = "error";
			logger.error(DetailException.expDetail(e, getClass()));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("zt", view);
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 赎回订单上传单证
	 * @param entity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("market/order/redeemDetail/uploadfile")
	public String uploadfile(RedeemOrderAttachment entity,HttpServletRequest request, HttpServletResponse response) {
		try {
			response.getWriter().print(redeemOrderAttachmentService.uploadfile(entity, request));
			return null;
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return ERROR;
	}
	
	/**
	 * 认购或申购 订单的异步验证
	 */
	@Override
	@RequestMapping("market/order/valid")
	public CommonModelAndView valid(@Valid Order entity, BindingResult result,Map<String, Object> model, HttpServletRequest request,HttpServletResponse response) {
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		if(entity.getClientName()!=null && !entity.getClientName().equals("")){
			if (!(Pattern.compile("^[a-zA-Z\\u4e00-\\u9fa5]{1,10}$")).matcher(entity.getClientName()).matches() && entity.getClientType()==1){
				result.rejectValue("clientName", "regex");
			}
		}
		if(entity.getiDCard() == null || entity.getiDCard().equals("")){
			result.rejectValue("iDCard", "NotEmpty");
		}else{
			if(entity.getClientType()!=null && entity.getClientType()==1 && entity.getiDCardType()==1){
				if(!(Pattern.compile("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)")).matcher(entity.getiDCard()).matches()){
					result.rejectValue("iDCard", "regex");
				}
			}
		}
		if(entity.getInvestAmount() != null){
			if(entity.getInvestAmount().equals(BigDecimal.ZERO))
				result.rejectValue("investAmount", "zero");
		}
		if(entity.getPayAmount() != null){
			if(!entity.getPayAmount().equals(BigDecimal.ZERO)){
				if(entity.getShare() == null){
					result.rejectValue("share", "exist");
				}else if(entity.getShare().equals(BigDecimal.ZERO)){
					result.rejectValue("share", "exist");
				}
			}
		}
		if(entity.getTradeStatus() == 7 && entity.getContractNumber().length() == 0){
			result.rejectValue("contractNumber", "NotNull");
		}
		if(entity.getCommissionPayTime()==null || entity.getCommissionPayTime().equals("")){
			result.rejectValue("commissionPayTime", "NotEmpty");
		}
		if(entity.getContractNumber()!=null && entity.getContractNumber()!=""){
			try {
				Long count = orderService.checkForContractNumber(entity);
				if(count > 0){
					result.rejectValue("contractNumber","already");
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		if (result.hasErrors()) {
			commonModelAndView.addBindingErrors(model, result, messageSource);
			return commonModelAndView;
		}
		return commonModelAndView;
	}
	
	/**
	 * 赎回订单 异步验证
	 * @param entity
	 * @param result
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("market/redeemorder/valid")
	public CommonModelAndView valid(@Valid RedeemOrder entity,BindingResult result, Map<String, Object> model,HttpServletRequest request, HttpServletResponse response) {
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		if(entity.getiDCard()==null || entity.getiDCard().equals("")){
			result.rejectValue("iDCard", "NotEmpty");
		}else if(entity.getClientType()==1){
			if(entity.getiDCardType()!=null && entity.getiDCardType()==1 && !(Pattern.compile("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)")).matcher(entity.getiDCard()).matches()){
				result.rejectValue("iDCard", "regex");
			}
		}
		if(entity.getTemp()==""){
			result.rejectValue("amount", "regex");
		}
		if (result.hasErrors()) {
			commonModelAndView.addBindingErrors(model, result, messageSource);
			return commonModelAndView;
		}
		return commonModelAndView;
	}
	
	/**
	 * 单证上传 气泡验证
	 * @param orderAttachment
	 * @param result
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/market/order/uploadDocument/valid")
	public CommonModelAndView validAttach(@Valid OrderAttachment orderAttachment, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		try {
			if(orderAttachment.getContractNumber().length() != 0){
				if(orderAttachment.getConfirmContractNumber()!=null && !orderAttachment.getConfirmContractNumber().equals("")){
					Order order = new Order();
					order.setProductID(orderAttachment.getProductID());
					order.setSubProductID(orderAttachment.getSubProductID());
					order.setContractNumber(orderAttachment.getContractNumber());
					if (orderService.checkForContractNumber(order)!=0 || orderHistoryService.checkForContractNumber(order)!=0){
						result.rejectValue("contractNumber", "already");
					}
					if (!orderAttachment.getConfirmContractNumber().equals(orderAttachment.getContractNumber())){
						result.rejectValue("confirmContractNumber", "dif");
					}
				}
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		if (result.hasErrors()) {
			commonModelAndView.addBindingErrors(model, result, messageSource);
			return commonModelAndView;
		}
		return commonModelAndView;
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
	public String uploadfile(Order entity, HttpServletRequest request,HttpServletResponse response) {
		return null;
	}
}
