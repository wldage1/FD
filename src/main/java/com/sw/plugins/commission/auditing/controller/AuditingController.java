package com.sw.plugins.commission.auditing.controller;


import java.io.File;
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
import com.sw.plugins.commission.paygrantparameter.entity.PayGrantParameter;
import com.sw.plugins.commission.paygrantparameter.service.PayGrantParameterService;
import com.sw.plugins.customer.member.entity.Member;
import com.sw.plugins.customer.member.service.MemberService;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.util.SystemProperty;

/**
 * 居间费审核控制器
 * 
 * @author Spark
 *
 */

@Controller
public class AuditingController extends BaseController<Commission> {

	private static Logger logger = Logger.getLogger(AuditingController.class);
	
	@Resource
	private AuditingService auditingService;
	
	@Resource
	private ContractService contractService;
	
	@Resource
	private MemberService memberService;
	
	@Resource
	private AdjustService adjustService;
	
	@Resource
	private PayGrantParameterService payGrantParameterService;
	
	/**
	 * 居间费审核
	 * 
	 * @param order
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/commission/auditing")
	public CommonModelAndView list(Commission entity, HttpServletRequest request, Map<String, Object> model) {
		Object obj = new CommonModelAndView().getCurrentStatus(entity, request);
		if (obj != null) {
			if (obj instanceof Commission) {
				entity = (Commission) obj;
			}
		}
		List<PayGrantParameter> shoudPayDateList = null;
		List<PayGrantParameter> orgShoudPayDateList = null;
		try {
			//判断当前登录用户是否属于居间公司，如果是则查询该居间公司数据
			Organization organization = adjustService.isCommission();
			PayGrantParameter payGrantParameter = new PayGrantParameter();
			if (organization != null && organization.getIsCommission() == 1){
				payGrantParameter.setOrgID(organization.getId());
			}
			payGrantParameter.setType((short)1);
			shoudPayDateList = payGrantParameterService.getList(payGrantParameter);			
			int i=0;
			int first=0; //上旬
			int middle=0; //中旬
			for(PayGrantParameter param : shoudPayDateList){
				String name = param.getName();
				int payDate = param.getPayDate();
				if(i==0){
					first=payDate;
					name=name+" "+"1-"+payDate;
				}else if(i==1){
					middle=payDate;
					name=name+" "+(first+1)+"-"+payDate;
				}else{					
					name=name+" "+(middle+1)+"-"+payDate;
				}
				param.setName(name);
				i++;
			}
			payGrantParameter = new PayGrantParameter();
			if (organization != null && organization.getIsCommission() == 1){
				payGrantParameter.setOrgID(organization.getId());
			}
			payGrantParameter.setType((short)2);
			orgShoudPayDateList = payGrantParameterService.getList(payGrantParameter);		
			for(PayGrantParameter param : orgShoudPayDateList){
				param.setName(String.valueOf(param.getPayDate()));
				i++;
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, entity,messageSource);
		commonModelAndView.addObject("code", entity.getC());
		model.put("commission", entity);
		model.put("shoudPayDateList", shoudPayDateList);
		model.put("orgShoudPayDateList", orgShoudPayDateList);
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
	@RequestMapping("/commission/auditing/grid")
	public CommonModelAndView json(Commission entity, HttpServletRequest request, Map<String, Object> model) {
		Object gridJson;
		Map<String, Object> map = null;
		try {
			//判断当前登录用户是否属于居间公司，如果是则查询该居间公司数据
			Organization organization = adjustService.isCommission();
			if (organization != null && organization.getIsCommission() == 1){
				entity.setOrgID(organization.getId());
			}
			if(entity.getPayStatus().toString().equals("1")){
				String payStatusCollection[] = {"1"};
				entity.setPayStatusCollection(payStatusCollection);
			}else{
				String payStatusCollection[] = {"5"};
				entity.setPayStatusCollection(payStatusCollection);
			}				
			String flag=entity.getFlag();
			String month=entity.getMonth();
			String payDate=entity.getShoudPayDate();
			if(flag.equals("1")){//个人
				if(!month.equals("")){
					if(payDate.equals("")){
						entity.setStartDate(month);
					}else{
						String first=payDate.substring(payDate.lastIndexOf(" ")+1,payDate.lastIndexOf("-"));
						String middle=payDate.substring(payDate.lastIndexOf("-")+1);
						entity.setStartDate(month+"-"+first);
						entity.setEndDate(month+"-"+middle);
					}
				}
			}else{//机构
				/** 2014-7-9 号去掉指定时间内的数据查询 */
//				if(!month.equals("")){
//					if(payDate.equals("")){
//						Calendar cal=Calendar.getInstance(); 
//						Date date=new SimpleDateFormat("yyyy-MM-dd").parse(month+"-01"); 
//						cal.setTime(date); 
//				    	int lastday=cal.getActualMaximum(Calendar.DAY_OF_MONTH); 
//						entity.setStartDate(month+"-01");
//						entity.setEndDate(month+"-"+lastday);
//					}else{
//						if(payDate.length()==1){
//							payDate="0"+payDate;
//						}
//						entity.setEndDate(month+"-"+payDate);
//					}
//				}
			}
			gridJson = auditingService.getGrid(entity);
			map = (gridJson == null ? null : (Map<String, Object>) gridJson);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, entity, request);
	}
	

	@RequestMapping("/commission/auditing/export")
	public @ResponseBody Map<String,Object> export(Commission entity,HttpServletRequest request,HttpServletResponse response){
		Organization organization = adjustService.isCommission();
		if (organization != null && organization.getIsCommission() == 1){
			entity.setOrgID(organization.getId());
		}
		String flag = request.getParameter("flag");
		String colName="";
		String colModel="";
		if(flag.equals("1")){
			colName = "订单号,理财顾问,产品名称,子产品名称,客户名称,居间费结算比例,到账金额(万元),预计应得居间费(元),核定时间,审核状态";
			colModel = "orderHistory.orderNumber,memberName,productName,subProductName,clientName,commissionProportion,payAmount,commission,checkRatifyTime,payStatus";			
		}else{
			colName = "订单号,理财顾问,机构简称,产品名称,子产品名称,客户名称,居间费结算比例,到账金额(万元),预计应得居间费(元),核定时间,审核状态";
			colModel = "orderHistory.orderNumber,memberName,orgShortName,productName,subProductName,clientName,commissionProportion,payAmount,commission,checkRatifyTime,payStatus";			
		}
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
			Map<String, Object> payStatusMap=this.initData.getPayStatus();	
			Map<String, Object> exportStatus=this.initData.getExportStatus();
			WritableWorkbook excel = null;
			response.reset();
			response.setHeader("Content-disposition", "attachment; filename = Excel" + temp + ".xls");
			response.setContentType("application/msexcel");
			excel = Workbook.createWorkbook(response.getOutputStream());
			auditingService.exportExcel(colName, colModel, excel, entity, payStatusMap,exportStatus);
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
	

	/**
	 * （一键）审核订单
	 * 
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping("/commission/auditing/auditing")
	public CommonModelAndView auditing(Commission entity,HttpServletRequest request) {
		String status;
	    try {
	    	short payStatus = entity.getPayStatus();
	    	if(payStatus == 4){
	    		auditingService.saveAuditingInfo(entity);
	    	}else{
		    	auditingService.update(entity);
	    	}
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
	 * 查看身份证复印件、网签协议和居间合同
	 * 
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping("/commission/auditing/viewerContract")
	public CommonModelAndView viewerContract(Commission entity,HttpServletRequest request) {
		String status;
		Contract contract = new Contract();
	    try {
	    	contract.setStatus(2);
	    	//ftp文件服务器地址
	    	String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
	    	//查看居间公司合同
	    	if(entity.getFlag().equals("1")){
		    	Long orgContractID = entity.getOrgContractID();
		    	if(orgContractID != null){
		    		contract.setId(orgContractID);
		    		contract = contractService.getOneContract(contract);
		    	}else if(entity.getTeamID() != null){
		    		contract.setOrgID(entity.getOrgID().intValue());
		    		contract.setTeamOrOrgID(entity.getTeamID().toString());
			    	contract = contractService.getOneContract(contract);
		    	}else{
		    		contract = null;
		    	}
	    		if(orgContractID != null||contract!=null){
	    			contract.setContractCreateTime(ftpHttpUrl);
	    			Commission commission = new Commission();
	    			commission.setId(entity.getId());
	    			commission.setOrgContractID(contract.getId());
	    			adjustService.update(commission);
	    		}
	    	}else{
	    		//网签协议
	    		contract.setId(entity.getAgreementTemplateID());
	    		contract = contractService.getOne(contract);
	    		//证件复印件
	    		Member member = new Member();
	    		member.setId(entity.getMemberID());
	    		member = memberService.getOneForDetail(member);
	    		contract.setContractCreateTime(ftpHttpUrl);
	    		contract.setContractStopTime(member.getCardImage());
	    	}
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
	
	@RequestMapping("/commission/auditing/detail")
	public CommonModelAndView adjustDetail(Commission entity, HttpServletRequest request, Map<String, Object> model){
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
		commonModelAndView.addObject("commission",entity);
		return commonModelAndView;
	}

	@Override
	public String uploadfile(Commission entity, HttpServletRequest request,
			HttpServletResponse response) {
		return null;
	}

	@Override
	public String downloadfile(Commission entity, HttpServletRequest request,
			HttpServletResponse response) {
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(Commission entity,
			HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	@Override
	public CommonModelAndView valid(Commission entity, BindingResult result,
			Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) {
		return null;
	}

}
