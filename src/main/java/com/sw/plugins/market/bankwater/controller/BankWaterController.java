package com.sw.plugins.market.bankwater.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.WritableWorkbook;
import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sw.core.common.Constant;
import com.sw.core.controller.BaseController;
import com.sw.core.controller.CommonModelAndView;
import com.sw.core.exception.DetailException;
import com.sw.plugins.market.bankwater.entity.PayConfirmFromProvider;
import com.sw.plugins.market.bankwater.service.BankWaterService;
import com.sw.plugins.market.order.controller.OrderController;
import com.sw.plugins.market.order.entity.Order;
import com.sw.plugins.market.order.entity.OrderProof;
import com.sw.plugins.market.order.service.OrderProofService;
import com.sw.plugins.market.order.service.OrderService;

import com.sw.plugins.product.manage.entity.Product;
import com.sw.plugins.product.manage.service.ProductService;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;
import com.sw.util.SystemProperty;

@Controller
public class BankWaterController extends BaseController<PayConfirmFromProvider>{
	
	private static Logger logger = Logger.getLogger(OrderController.class);
	
	@Resource
	private BankWaterService bankWaterService;
	@Resource
	private ProductService productService;
	@Resource
	private OrderProofService orderProofService;
	@Resource
	private UserLoginService userLoginService;
	@Resource
	private OrderService orderService;
	
	/**
	 * 银行流水对账 初始化列表
	 * @param payConfirmFromProvider
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/bankwater")
	public CommonModelAndView bankWater(PayConfirmFromProvider payConfirmFromProvider,HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request,payConfirmFromProvider, messageSource);
		commonModelAndView.addObject("payConfirmFromProvider", payConfirmFromProvider);
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
			// 在途订单交易状态
			commonModelAndView.addObject("tradeStatusMap", this.initData.getTradeStatus().get(1));
			//查询认购类型的产品
			List<Product> offerpayList=productService.getOfferPayList(product);
			commonModelAndView.addObject("offerpayList", offerpayList);
			commonModelAndView.addObject("offerpayListJson", JSONArray.fromObject(offerpayList));
			//查询申购类型的产品
			List<Product> subpayList=productService.getSubPayList(product);
			commonModelAndView.addObject("subpayList", subpayList);
			commonModelAndView.addObject("subpayListJson", JSONArray.fromObject(subpayList));
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		
		return commonModelAndView;
	}
	
	/**
	 * 分页查询 认购或申购 产品流水的 JSON数据
	 * @param payConfirmFromProvider
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/bankwater/rgAllGrid")
	public CommonModelAndView rgAllGrid(PayConfirmFromProvider payConfirmFromProvider,HttpServletRequest request){
		Map<String, Object> map = null;
		try {
			User user = userLoginService.getCurrLoginUser();
			Organization organization = user.getSelfOrg();
			if (organization != null) {
				int isCommission = organization.getIsCommission();
				if (isCommission == 1) {
					payConfirmFromProvider.setOrgID(organization.getId());
				} else {
					payConfirmFromProvider.setOrgID(null);
				}
			}
			map = bankWaterService.getGrid(payConfirmFromProvider);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, payConfirmFromProvider, request);
	}
	
	/**
	 * 分页查询 申购 产品订单未匹配的 JSON数据
	 * @param payConfirmFromProvider
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/bankwater/getNotMappOrderGrid")
	public CommonModelAndView getNotMappOrderGrid(PayConfirmFromProvider payConfirmFromProvider,HttpServletRequest request){
		Map<String, Object> map = null;
		try {
			Order order=new Order();
			order.setProductID(payConfirmFromProvider.getProductId());
			order.setStart(payConfirmFromProvider.getStart());
			order.setRows(payConfirmFromProvider.getRows());
			order.setPage(payConfirmFromProvider.getPage());
			order.setOffset(payConfirmFromProvider.getOffset());
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
			map =orderService.getNotMappOrderGrid(order);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, payConfirmFromProvider, request);
	}
	
	/**
	 * 分页查询 申购 产品订单以匹配的 JSON数据
	 * @param payConfirmFromProvider
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/bankwater/getHasMappOrderGrid")
	public CommonModelAndView getHasMappOrderGrid(PayConfirmFromProvider payConfirmFromProvider,HttpServletRequest request){
		Map<String, Object> map = null;
		try {
			Order order=new Order();
			order.setProductID(payConfirmFromProvider.getProductId());
			order.setStart(payConfirmFromProvider.getStart());
			order.setRows(payConfirmFromProvider.getRows());
			order.setPage(payConfirmFromProvider.getPage());
			order.setOffset(payConfirmFromProvider.getOffset());
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
			map =orderService.getHasMappOrderGrid(order);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, payConfirmFromProvider, request);
	}
	
	/**
	 * 匹配订单与流水 匹配完成 订单设置成功
	 * @param payConfirmFromProvider
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/bankwater/affirmbankwater")
	public CommonModelAndView affirmbankwater(PayConfirmFromProvider payConfirmFromProvider, HttpServletRequest request){
		Map<String, Object>map = new HashMap<String, Object>();
		try {
			orderService.updateOrderToHistory(payConfirmFromProvider);
			map.put("zt", "success");
		} catch (Exception e) {
			map.put("zt", "error");
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 数据导入  初始化页面
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/bankwater/bankwaterimportor")
	public CommonModelAndView bankwaterimportor(PayConfirmFromProvider payConfirmFromProvider,HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView();
		List<Product> productList = null;
		try {
			User user = userLoginService.getCurrLoginUser();
			Organization organization = user.getSelfOrg();
			if (organization != null) {
				int isCommission = organization.getIsCommission();
				if (isCommission == 1) {
					payConfirmFromProvider.setOrgID(organization.getId());
				} else {
					payConfirmFromProvider.setOrgID(null);
				}
			}
			productList = bankWaterService.getProductList(payConfirmFromProvider);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		commonModelAndView.addObject("productList", productList);
		commonModelAndView.addObject("payConfirmFromProvider", payConfirmFromProvider);
		commonModelAndView.addObject("iframeID", request.getParameter("iframeID"));
		return commonModelAndView;
	}
	
	/**
	 * 数据导入
	 * @param payConfirmFromProvider
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/bankwater/bankwaterImport")
	public CommonModelAndView bankWaterImport(PayConfirmFromProvider payConfirmFromProvider, HttpServletRequest request){
		Map<String, Object>map = new HashMap<String, Object>();
		String fileUrl = request.getParameter("fileUrl");
		try {
			bankWaterService.bankWaterImport(payConfirmFromProvider,fileUrl);
			map.put("zt", "success");
		} catch (Exception e) {
			map.put("zt", "error");
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 流水与打款凭证 进行自动匹配初始化页面
	 * @param payConfirmFromProvider
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/bankwater/mappingbankwater")
	public CommonModelAndView intoMappingBankWaterByMotion(PayConfirmFromProvider payConfirmFromProvider,HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView();
		commonModelAndView.addObject("payConfirmFromProvider", payConfirmFromProvider);
		commonModelAndView.addObject("iframeID", request.getParameter("iframeID"));
		String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
		commonModelAndView.addObject("ftpHttpUrl", ftpHttpUrl);
		return commonModelAndView;
	}
	
	/**
	 * 流水与打款凭证 进行自动匹配 JSON 数据
	 * @param payConfirmFromProvider
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/bankwater/mappingbankwaterGrid")
	public CommonModelAndView intoMappingBankWaterGrid(PayConfirmFromProvider payConfirmFromProvider,HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map=bankWaterService.selectBankWaterList(payConfirmFromProvider);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView",map);
	}
	
	/**
	 * 流水与打款凭证 进行自动匹配
	 * @param payConfirmFromProvider
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/bankwater/mappingbankwaterUp")
	public CommonModelAndView mappingBankWaterByMotion(PayConfirmFromProvider payConfirmFromProvider,HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		boolean isTrue=true;
		try {
			bankWaterService.updateMappingBankWater(payConfirmFromProvider);
		} catch (Exception e) {
			isTrue=false;
			logger.error(DetailException.expDetail(e, getClass()));
		}
		map.put("isTrue", isTrue);
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 流水处理 初始化
	 * @param payConfirmFromProvider
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/bankwater/disposebankwater")
	public CommonModelAndView disposeBankWater(PayConfirmFromProvider payConfirmFromProvider,HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView();
		try {
			payConfirmFromProvider = bankWaterService.getOne(payConfirmFromProvider);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		commonModelAndView.addObject("payConfirmFromProvider", payConfirmFromProvider);
		commonModelAndView.addObject("iframeID", request.getParameter("iframeID"));
		String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
		commonModelAndView.addObject("ftpHttpUrl", ftpHttpUrl);
		return commonModelAndView;
	}
	
	/**
	 * 流水与打款凭证 进行手动匹配
	 * @param payConfirmFromProvider
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/bankwater/mappingbankwaterbymanual")
	public CommonModelAndView mappingBankWaterByManul(PayConfirmFromProvider payConfirmFromProvider,HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		boolean isTrue=true;
		try {
			bankWaterService.updateByManual(payConfirmFromProvider);
		} catch (Exception e) {
			isTrue=false;
			logger.error(DetailException.expDetail(e, getClass()));
		}
		map.put("isTrue", isTrue);
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 打款凭证 列表 JSON 数据
	 * @param payConfirmFromProvider
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/bankwater/orderProofGrid")
	public CommonModelAndView orderProofGrid(OrderProof orderProof,HttpServletRequest request){
		Map<String, Object> map = null;
		try {
			map = orderProofService.getNotMapingGrid(orderProof);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 获取产品对应 银行流水和打款凭证的对账详情信息
	 * @param payConfirmFromProvider
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/bankwater/getProductOrderProof")
	public CommonModelAndView getProductOrderProof(PayConfirmFromProvider payConfirmFromProvider,HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map=bankWaterService.getProductOrderProof(payConfirmFromProvider);
			map.put("status", true);
		} catch (Exception e) {
			map.put("status", false);
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 银行流水导入前上传文件
	 * @param payConfirmFromProvider
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/market/bankwater/bankwaterimport/uploadfile")
	public void uploadDocumentFile(PayConfirmFromProvider payConfirmFromProvider, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		try {
			map=bankWaterService.uploadFile(payConfirmFromProvider, request);
			map.put("status", "success");
		} catch (Exception e) {
			map.put("status", "error");
			logger.error(DetailException.expDetail(e, getClass()));
		}
		response.getWriter().write(JSONArray.fromObject(map).toString());
	}
	
	/**
	 * 导出数据到EXCEL
	 * 
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/bankwater/export")
	public @ResponseBody Map<String, Object> bankExport(PayConfirmFromProvider payConfirmFromProvider,HttpServletRequest request,HttpServletResponse response){
		String colName ="发行机构,产品简称,客户名称,打款金额(万元),打款时间,匹配状态";
		String colModel = "provider.shortName,product.shortName,name,payAmount,payTime,matchingStatus";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String temp = sdf.format(new Date());
		Calendar cal=Calendar.getInstance();
		String pathDate = Constant.DIRSPLITER+cal.get(Calendar.YEAR)+Constant.DIRSPLITER+String.valueOf(cal.get(Calendar.MONTH)+1)+Constant.DIRSPLITER+cal.get(Calendar.DATE);
		String path = Constant.APPLICATIONPATH+initData.getUploadDir()+Constant.DIRSPLITER+initData.getUploadTmpDir()+pathDate;
		File excelFile = new File(path,temp + ".xls");
		File f = excelFile.getParentFile();
		if(f!=null&&!f.exists()){ 
			f.mkdirs();
		}
		Map<String, Object> matchingStatusMap = this.initData.getMatchingStatus();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			WritableWorkbook excel = null;
			response.reset();
			response.setHeader("Content-disposition", "attachment; filename = Excel" + temp + ".xls");
			response.setContentType("application/msexcel");
			excel = Workbook.createWorkbook(response.getOutputStream());
			excel = bankWaterService.exportExcel(payConfirmFromProvider.getC(), "", colName, colModel, excel, payConfirmFromProvider,matchingStatusMap);
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
	 * 导出数据到EXCEL
	 * 
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/bankwater/exportsubscibeexcel")
	public @ResponseBody Map<String, Object> exportsubscibeexcel(PayConfirmFromProvider payConfirmFromProvider,HttpServletRequest request,HttpServletResponse response){
		String colName ="订单号,合同号,产品简称,客户名称,订单状态,投资金额(万元),已分配金额(万元),到账金额(万元),有无打款凭证,确认金额(万元)";
		String colModel = "orderNumber,contractNumber,productShortName,clientName,tradeStatusName,investAmount,share,payAmount,proofCount,affirmAmount";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String temp = sdf.format(new Date());
		Calendar cal=Calendar.getInstance();
		String pathDate = Constant.DIRSPLITER+cal.get(Calendar.YEAR)+Constant.DIRSPLITER+String.valueOf(cal.get(Calendar.MONTH)+1)+Constant.DIRSPLITER+cal.get(Calendar.DATE);
		String path = Constant.APPLICATIONPATH+initData.getUploadDir()+Constant.DIRSPLITER+initData.getUploadTmpDir()+pathDate;
		File excelFile = new File(path,temp + ".xls");
		File f = excelFile.getParentFile();
		if(f!=null&&!f.exists()){ 
			f.mkdirs();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Order order=new Order();
		order.setProductID(payConfirmFromProvider.getProductId());
		try {
			WritableWorkbook excel = null;
			response.reset();
			response.setHeader("Content-disposition", "attachment; filename = Excel" + temp + ".xls");
			response.setContentType("application/msexcel");
			excel = Workbook.createWorkbook(response.getOutputStream());
			excel = orderService.exportsubscibeexcel(order, colName, colModel, excel);
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
	
	@Override
	public String uploadfile(PayConfirmFromProvider entity,HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	@Override
	public String downloadfile(PayConfirmFromProvider entity,HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(PayConfirmFromProvider entity,HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	@Override
	public CommonModelAndView valid(PayConfirmFromProvider entity,BindingResult result, Map<String, Object> model,HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
}
