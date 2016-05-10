package com.sw.plugins.commission.adjust.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import com.sw.plugins.commission.adjust.entity.Commission;
import com.sw.plugins.commission.adjust.service.AdjustService;
import com.sw.plugins.commission.contract.entity.Contract;
import com.sw.plugins.commission.contract.service.ContractService;
import com.sw.plugins.commission.paygrantparameter.entity.PayGrantParameter;
import com.sw.plugins.commission.paygrantparameter.service.PayGrantParameterService;
import com.sw.plugins.customer.member.entity.Member;
import com.sw.plugins.customer.member.service.MemberService;
import com.sw.plugins.market.order.entity.Order;
import com.sw.plugins.market.order.service.OrderHistoryService;
import com.sw.plugins.product.manage.entity.Product;
import com.sw.plugins.product.manage.entity.ProductCommissionRatio;
import com.sw.plugins.product.manage.service.ProductCommissionRatioService;
import com.sw.plugins.product.manage.service.ProductService;
import com.sw.plugins.system.organization.entity.Organization;

/**
 * 核算居间费控制器
 * 
 * @author Spark
 *
 */
@Controller
public class AdjustController extends BaseController<Commission> {
 
	private static Logger logger = Logger.getLogger(AdjustController.class);
	
	@Resource
	private ProductService productService;
	
	@Resource
	private AdjustService adjustService;
	
	@Resource
	private OrderHistoryService orderHistoryService;
	
	@Resource
	private ContractService contractService;
	
	@Resource
	private ProductCommissionRatioService productCommissionRatioService;
	
	@Resource
	private MemberService memberService;
	
	@Resource
	private PayGrantParameterService payGrantParameterService;
	
	/**
	 * 居间费核算
	 * 
	 * @param order
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/commission/adjust")
	public CommonModelAndView list(Commission entity, HttpServletRequest request, Map<String, Object> model) {
		Object obj = new CommonModelAndView().getCurrentStatus(entity, request);
		if (obj != null) {
			if (obj instanceof Commission) {
				entity = (Commission) obj;
			}
		}
		List<Product> productList = null;
		try {
			Product product = new Product();
			//判断当前登录用户是否属于居间公司，如果是则查询该居间公司数据
			Organization organization = adjustService.isCommission();
			if (organization != null){
				product.setOrgId(organization.getId());
				//已成立
				product.setSellStatus((short) 5);
				//居间费状态
//				short payStatus = 0;
//				if(entity != null && entity.getPayStatus() != null){
//					payStatus = entity.getPayStatus();
//				}
//				product.setStatus(payStatus);
				productList= productService.getCommissionProduct(product);
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, entity,messageSource);
		commonModelAndView.addObject("code", entity.getC());
		model.put("commission", entity);
		model.put("productList", productList);
		return commonModelAndView;
	}
	
	/**
	 * 订单列表
	 * 
	 * @param entity
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	@RequestMapping("/commission/adjust/grid")
	public CommonModelAndView json(Commission entity, HttpServletRequest request) {
		Object gridJson;
		Map<String, Object> map = null;
		try {
			//判断当前登录用户是否属于居间公司，如果是则查询该居间公司数据
			Organization organization = adjustService.isCommission();
			PayGrantParameter payGrantParameter = new PayGrantParameter();
			if(organization != null){
				payGrantParameter.setOrgID(organization.getId());
				payGrantParameter.setType((short) 1);
				//个人居间费参数
				List<PayGrantParameter> payGrantParameterList = payGrantParameterService.getList(payGrantParameter);
				//机构居间费参数
//				payGrantParameter.setType((short) 2);
//				List<PayGrantParameter> payOrgGrantParameterList = payGrantParameterService.getList(payGrantParameter);
				//判断是否设置个人居间费的上中下旬和机构的核定截止日期和发放日期
				if(payGrantParameterList != null){
					Calendar  now  =  Calendar.getInstance();
			    	int year = now.get(Calendar.YEAR);
			    	int month = now.get(Calendar.MONTH)  +  1;
			    	int date = now.get(Calendar.DAY_OF_MONTH);
			    	//核算截止日期
					String predictPayTime;
					if (payGrantParameterList.size()>0){
						for(PayGrantParameter payParameter :  payGrantParameterList){
							if(date >= payParameter.getApprovedDeadline()&&date<=payParameter.getPayDate()){
								date=payParameter.getApprovedDeadline();
								break;
							}
						}
					}
					predictPayTime = year + "-" + month + "-" + date;
					//查询需要核算的订单
					Order order = new Order();
					//设置个人核定截止日期
					order.setModifyTime(predictPayTime);
//					//设置机构核定截止日期
//					order.setShareTime("");
//					if (payOrgGrantParameterList.size()>0){
//						for(PayGrantParameter payParameter :  payOrgGrantParameterList){
//							if(date >= payParameter.getApprovedDeadline()&&date<=payParameter.getPayDate()){
//								order.setShareTime(year + "-" + month + "-" + payParameter.getApprovedDeadline());
//								break;
//							}
//						}
//					}
					order.setProductID(entity.getProductID());
					order.setTradeTypeCollection(entity.getTradeTypeCollection());
					order.setMaxShareThreshold(entity.getMaxShareThreshold());
					order.setMinShareThreshold(entity.getMinShareThreshold());
					order.setEffectiveDate(entity.getEffectiveDate());
					order.setSubProductID(entity.getSubProductID());
					List<Order> orderList = orderHistoryService.getAdjustOrderList(order);
					Contract contract;
					Long agreementTemplateID, orgContractID = null, teamID;
					for(Order item : orderList){
						contract = new Contract();
						contract.setOrgID(Integer.parseInt(item.getOrgID().toString()));
						contract.setStatus(2);
						//查询网签协议ID
						contract  = contractService.getAgreementTemplateID(contract);
						agreementTemplateID = contract.getId();
						teamID = item.getTeamID();
						//按照存续阶段和子产品ID查询产品佣金率表
						ProductCommissionRatio productCommissionRatio = new ProductCommissionRatio();
						if(item.getTradeType() == 1 && item.getProductType() != 2 && item.getProductType() != 5){
							//按照子产品查询有效的阶段
							productCommissionRatio.setSubProductId(item.getSubProductID());
							//有效的阶段
							productCommissionRatio.setIsAvailable((short)1);
							productCommissionRatio.setMaxShareThreshold(item.getPayAmount());
							productCommissionRatio = productCommissionRatioService.getOneBySubproductID(productCommissionRatio);
						}else{
							//申购和新申购订单（按照子产品查询生效日期最近的记录）
							productCommissionRatio.setEffectiveDate(item.getOrderTime());
							productCommissionRatio.setSubProductId(item.getSubProductID());
							productCommissionRatio = productCommissionRatioService.getOneSubscribe(productCommissionRatio);
						}
						//循环向居间费记录表插入记录
						Commission commission = new Commission();
						commission.setOrderID(item.getId());
						commission.setMemberID(item.getMemberID());
						commission.setTeamID(item.getTeamID());
						commission.setOrgID(item.getOrgID());
						commission.setProductID(item.getProductID());
						commission.setSubProductID(item.getSubProductID());
						commission.setPayAmount(item.getPayAmount());
						commission.setAgreementTemplateID(agreementTemplateID);
						commission.setExportStatus("0");
						commission.setPayStatus((short)0);
						if(productCommissionRatio.getHoldlingPhase() != null){
							commission.setHoldlingPhase(productCommissionRatio.getHoldlingPhase());
						}
						BigDecimal serviceChargeCoefficient = productCommissionRatio.getServiceChargeCoefficient();
						//如果理财师属于机构则设置机构服务费系数，否则设置个人服务费系数
						if(teamID != null){
							//判断理财师是否属于机构
							Member member = new Member();
							member.setId(teamID);
							member = memberService.memberIsOrg(member);
							if(member != null){
								// 如果理财师属于机构则服务费系数为个人服务费系数+机构服务费系数
								serviceChargeCoefficient = serviceChargeCoefficient.add(productCommissionRatio.getOrgServiceChargeCoefficient());
								//查询居间机构合同ID
								contract.setTeamOrOrgID(member.getTeamID().toString());
								contract  = contractService.getOrgContractID(contract);
								if(contract != null){
									orgContractID = Long.parseLong(contract.getContractOrgID().toString());
									commission.setOrgContractID(orgContractID);
								}
							}
						}
						commission.setServiceChargeCoefficient(serviceChargeCoefficient);
						commission.setHoldlingPhase(productCommissionRatio.getHoldlingPhase());
						commission.setIssuanceCostRate(productCommissionRatio.getIssuanceCostRate());
						//计算最终佣金比例
						commission.setTimeCoefficient(productCommissionRatio.getTimeCoefficient());
						BigDecimal issuanceCostRate = productCommissionRatio.getIssuanceCostRate();
						BigDecimal timeCoefficient = productCommissionRatio.getTimeCoefficient();
						BigDecimal incentiveFeeRate = productCommissionRatio.getIncentiveFeeRate();
						
						BigDecimal commissionProportion = issuanceCostRate.multiply(serviceChargeCoefficient).multiply(timeCoefficient).add(incentiveFeeRate);
						commission.setCommissionProportion(commissionProportion);
						BigDecimal commissionAmount = commissionProportion.multiply(item.getPayAmount()).divide(BigDecimal.valueOf(100));
						commission.setCommission(commissionAmount);
						commission.setIncentiveFeeRate(productCommissionRatio.getIncentiveFeeRate());
						adjustService.save(commission);
					}
				}
				
				//重新核算核定未通过对数据
				Commission commiss = new Commission();
				commiss.setProductID(entity.getProductID());
				commiss.setPayStatus((short)2);
				List<Commission> orderIdList = adjustService.getOrderIdList(commiss);
				for(int i=0; i<orderIdList.size(); i++){
					commiss = new Commission();
					commiss.setId(orderIdList.get(i).getId());
					adjustService.againAdjustCommission(commiss);
				}
				if (organization != null && organization.getIsCommission() == 1){
					entity.setOrgID(organization.getId());
				}
				//设置居间费状态集合
				if(entity.getTradeTypeCollection().length != 3){
					String payStatusCollection[] = {"0", "1", "2" , "6"};
					entity.setPayStatusCollection(payStatusCollection);
				}else{
					String payStatusCollection[] = {"6"};
					entity.setPayStatusCollection(payStatusCollection);
				}
				gridJson = adjustService.getGrid(entity);
				map = (gridJson == null ? null : (Map<String, Object>) gridJson);
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 产品收益权和存续阶段列表
	 * 
	 * @param entity
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/commission/adjust/holdlingPhase")
	public CommonModelAndView holdlingPhase(Commission entity,HttpServletRequest request) {
		Object gridJson = null;
		Map<String, Object> map = null;
	    try {
			//判断是否选择产品
			if(entity.getProductID() != null || entity.getTradeTypeCollection().length == 2){
				//判断当前登录用户是否属于居间公司，如果是则查询该居间公司数据
				Organization organization = adjustService.isCommission();
				if (organization != null){
					entity.setOrgID(organization.getId());
					gridJson = adjustService.holdlingPhase(entity);
				}
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
	    map = (gridJson == null ? null : (Map<String, Object>) gridJson);
		return new CommonModelAndView("jsonView", map, entity, request);
	}
	
	/**
	 * 不发放居间费
	 * 
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping("/commission/adjust/nonPayment")
	public CommonModelAndView nonPayment(Commission entity,HttpServletRequest request) {
		String status;
	    try {
	    	//判断当前登录用户是否属于居间公司，如果是则查询该居间公司数据
			Organization organization = adjustService.isCommission();
			if (organization != null && organization.getIsCommission() == 1){
				entity.setOrgID(organization.getId());
			}
	    	entity.setPayStatus((short)6);
	    	adjustService.update(entity);
	    	status = "success";
		} catch (Exception e) {
			status = "error";
			logger.error(DetailException.expDetail(e, getClass()));
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("status", status);
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 重新核算居间费
	 * 
	 * @param entity
	 * @param request
	 * @return
	 */
	/*@RequestMapping("/commission/adjust/againAdjust")
	public CommonModelAndView againAdjust(Commission entity,HttpServletRequest request) {
		String status;
	    try {
	    	adjustService.againAdjustCommission(entity);
	    	status = "success";
		} catch (Exception e) {
			status = "error";;
			logger.error(DetailException.expDetail(e, getClass()));
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("status", status);
		return new CommonModelAndView("jsonView", map);
	}*/
	
	/**
	 * 批量重新核算
	 * 
	 * @param entity
	 * @param request
	 * @return
	 */
	/*@RequestMapping("/commission/adjust/batchAdjust")
	public CommonModelAndView batchAgainAdjust(Commission entity,HttpServletRequest request) {
		String status;
	    try {
	    	adjustService.againAdjustCommission(entity);
	    	status = "success";
		} catch (Exception e) {
			status = "error";;
			logger.error(DetailException.expDetail(e, getClass()));
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("status", status);
		return new CommonModelAndView("jsonView", map);
	}*/
	
	/**
	 * (一键)核定
	 * 
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping("/commission/adjust/batchVouch")
	public CommonModelAndView batchVouch(Commission entity,HttpServletRequest request) {
		String status;
	    try {
	    	if(entity.getPayStatus() == 1){
	    		Date date = new Date(System.currentTimeMillis());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dateStr = sdf.format(date);
		    	entity.setCheckRatifyTime(dateStr);
	    	}
	    	adjustService.update(entity);
	    	status = "success";
		} catch (Exception e) {
			status = "error";
			logger.error(DetailException.expDetail(e, getClass()));
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("status", status);
		return new CommonModelAndView("jsonView", map);
	}
	
	@RequestMapping("/commission/adjust/detail")
	public CommonModelAndView adjustDetail(Commission entity, HttpServletRequest request, Map<String, Object> model){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, entity, messageSource);
		String c = entity.getC();
		try {
			entity = adjustService.getOne(entity);
			//客户类型
			commonModelAndView.addObject("clientType", this.initData.getClientType());
			//交易类型
			commonModelAndView.addObject("tradeType", this.initData.getTradeType().get(0));
			//打款状态
			commonModelAndView.addObject("payProgress", this.initData.getPayProgress());
			//交易状态
			commonModelAndView.addObject("tradeStatus", this.initData.getTradeStatus().get(1));
			//单证状态
			commonModelAndView.addObject("documentStatus", this.initData.getDocumentStatus());
			//配给状态
			commonModelAndView.addObject("pushStatus", this.initData.getPushStatus());
			//证件类型
			commonModelAndView.addObject("iDCardTypeMap", this.initData.getMemberIDCardType());		
			//居间费支付状态
			commonModelAndView.addObject("payStatusMap", this.initData.getPayStatus());		
			
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		entity.setC(c);
		commonModelAndView.addObject("commission",entity);
		return commonModelAndView;
	}
	
	@Override
	public String uploadfile(Commission entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String downloadfile(Commission entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(Commission entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView valid(Commission entity, BindingResult result,
			Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
