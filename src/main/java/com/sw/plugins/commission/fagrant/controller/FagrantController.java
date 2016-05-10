package com.sw.plugins.commission.fagrant.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sw.core.common.Constant;
import com.sw.core.controller.BaseController;
import com.sw.core.controller.CommonModelAndView;
import com.sw.core.exception.DetailException;
import com.sw.plugins.commission.adjust.entity.Commission;
import com.sw.plugins.commission.adjust.service.AdjustService;
import com.sw.plugins.commission.auditing.service.AuditingService;
import com.sw.plugins.commission.contract.entity.Contract;
import com.sw.plugins.commission.contract.service.ContractService;
import com.sw.plugins.commission.fagrant.entity.MemberPayRecord;
import com.sw.plugins.commission.fagrant.service.MemberPayRecordService;
import com.sw.plugins.commission.paygrantparameter.entity.PayGrantParameter;
import com.sw.plugins.customer.member.entity.Member;
import com.sw.plugins.customer.member.service.MemberService;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.util.SystemProperty;

/**
 * 理财顾问居间费发放
 * 
 * @author Spark
 *
 */
@Controller
public class FagrantController extends BaseController<Commission> {

	private static Logger logger = Logger.getLogger(FagrantController.class);
	
	@Resource
	private MemberPayRecordService memberPayRecordService;
	
	@Resource
	private ContractService contractService;
	
	@Resource
	private MemberService memberService;
	
	@Resource
	private AuditingService auditingService;
	
	@Resource
	private AdjustService adjustService;
	
	/**
	 * 居间费发放
	 * 
	 * @param entity
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/commission/fagrant")
	public CommonModelAndView list(Commission entity, HttpServletRequest request, Map<String, Object> model) {
		Object obj = new CommonModelAndView().getCurrentStatus(entity, request);
		if (obj != null) {
			if (obj instanceof Commission) {
				entity = (Commission) obj;
			}
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, entity,messageSource);
		commonModelAndView.addObject("code", entity.getC());
		commonModelAndView.addObject("tab", request.getParameter("tab"));
		model.put("commission", entity);
		return commonModelAndView;
	}
	
	/**
	 * 订单列表
	 * 
	 * @param entity
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/commission/fagrant/grid")
	public CommonModelAndView json(MemberPayRecord entity, HttpServletRequest request) {
		Object gridJson;
		Map<String, Object> map = null;
		try {
			//判断当前登录用户是否属于居间公司，如果是则查询该居间公司数据
			Organization organization = adjustService.isCommission();
			if (organization != null && organization.getIsCommission() == 1){
				entity.setOrgID(organization.getId());
			}
			gridJson = memberPayRecordService.getGrid(entity);
			map = (gridJson == null ? null : (Map<String, Object>) gridJson);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, entity, request);
	}
	
	/**
	 * 订单列表
	 * 
	 * @param entity
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/commission/fagrant/subGrid")
	public CommonModelAndView getSubGrid(MemberPayRecord entity, HttpServletRequest request) {
		Object gridJson;
		Map<String, Object> map = null;
		try {
			//判断当前登录用户是否属于居间公司，如果是则查询该居间公司数据
			Organization organization = adjustService.isCommission();
			if (organization != null && organization.getIsCommission() == 1){
				entity.setOrgID(organization.getId());
			}
			gridJson = memberPayRecordService.getSubGrid(entity);
			map = (gridJson == null ? null : (Map<String, Object>) gridJson);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 居间费发放操作（发放成功和发放失败）
	 * 
	 * @param entity
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/commission/fagrant/paySuccess")
	public CommonModelAndView payOperation(MemberPayRecord entity,HttpServletRequest request) {
		String status;
	    try {
	    	if(entity.getStatus() == 1){
	    		Date date = new Date(System.currentTimeMillis());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				String dateStr = sdf.format(date);
	    		entity.setPayTime(dateStr);
	    	}
	    	Commission commission = new Commission();
	    	commission.setMemberID(entity.getMemberID());
	    	//判断当前登录用户是否属于居间公司，如果是则查询该居间公司数据
			Organization organization = adjustService.isCommission();
			if (organization != null && organization.getIsCommission() == 1){
				commission.setOrgID(organization.getId());
			}
	    	long count= adjustService.getRecordNotDo(commission);
	    	if  (count>0){
	    		Map<String,Object> map = new HashMap<String,Object>();
	    		map.put("mark", "1");
	    		return new CommonModelAndView("jsonView", map);
	    	}
	    	memberPayRecordService.update(entity);
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
	 * 查看证件复印件
	 * 
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping("/commission/fagrant/certificate")
	public CommonModelAndView certificate(Commission entity,HttpServletRequest request) {
		String status;
		Contract contract = new Contract();
	    try {
	    	//ftp文件服务器地址
	    	String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
	    	contract.setStatus(2);
	    	Member member = new Member();
    		member.setId(entity.getMemberID());
    		member = memberService.getOneForDetail(member);
    		contract.setContractStopTime(member.getCardImage());
    		contract.setContractCreateTime(ftpHttpUrl);
	    	status = "success";
		} catch (Exception e) {
			status = "error";
			logger.error(DetailException.expDetail(e, getClass()));
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("status", status);
		map.put("contract", contract);
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 查看网签协议
	 * 
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping("/commission/fagrant/agreement")
	public CommonModelAndView agreement(Commission entity,HttpServletRequest request) {
		String status;
		Contract contract = new Contract();
	    try {
	    	contract.setStatus(2);
	    	contract.setId(entity.getAgreementTemplateID());
    		contract = contractService.getOne(contract);
	    	status = "success";
		} catch (Exception e) {
			status = "error";
			logger.error(DetailException.expDetail(e, getClass()));
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("status", status);
		map.put("contract", contract);
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 恢复居间费发放
	 * 
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping("/commission/fagrant/recover")
	public CommonModelAndView recover(Commission entity,HttpServletRequest request) {
		String status;
	    try {
	    	auditingService.update(entity);
	    	status = "success";
		} catch (Exception e) {
			status = "error";
			logger.error(DetailException.expDetail(e, getClass()));
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("status", status);
		return new CommonModelAndView("jsonView", map);
	}
	
	@RequestMapping("/commission/fagrant/detail")
	public CommonModelAndView adjustDetail(Commission entity, HttpServletRequest request){
		String c = entity.getC();
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, entity, messageSource);
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
		commonModelAndView.addObject("tab",request.getParameter("tab"));
		commonModelAndView.addObject("commission",entity);
		return commonModelAndView;
	}

	@RequestMapping("/commission/fagrant/export")
	public @ResponseBody Map<String,Object> export(MemberPayRecord entity,HttpServletRequest request,HttpServletResponse response){
		//判断当前登录用户是否属于居间公司，如果是则查询该居间公司数据
		Organization organization = adjustService.isCommission();
		if (organization != null && organization.getIsCommission() == 1){
			entity.setOrgID(organization.getId());
		}
		String colName="";
		String colModel="";
		colName = "理财顾问,开户行,账户名,卡号,身份证号,预计发放时间,到账金额(万元),居间费,营业税(元),城建税(元),教育费附加(元),地方教育费附加(元),个人所得税(元)";
		colModel = "memberName,bankName,accountName,accountID,iDCard,payDate,payAmount,amount,salesTax,constructionTax,educationalSurtax,localEducationalCost,personalIncomeTax";			
		
		//entity.setSqlQuerys(sql);
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
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			WritableWorkbook excel = null;
			response.reset();
			response.setHeader("Content-disposition", "attachment; filename = Excel" + temp + ".xls");
			response.setContentType("application/msexcel");
			excel = Workbook.createWorkbook(response.getOutputStream());
			memberPayRecordService.exportExcel(colName, colModel, excel, entity);
			excel.write();
			excel.close();
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception e) {
			map.put("status", 0);
			e.printStackTrace();
		}
		return map;
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
