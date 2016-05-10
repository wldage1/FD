package com.sw.plugins.commission.organizationgrant.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
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
import com.sw.plugins.commission.organizationgrant.entity.TeamOrOrgPayDetail;
import com.sw.plugins.commission.organizationgrant.entity.TeamOrOrgPayRecord;
import com.sw.plugins.commission.organizationgrant.service.CommissionFeeService;
import com.sw.plugins.commission.organizationgrant.service.TeamOrOrgPayDetailService;
import com.sw.plugins.commission.organizationgrant.service.TeamOrOrgPayRecordService;
import com.sw.plugins.customer.member.entity.Member;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.util.CharsetUtil;
import com.sw.util.FTPUtil;
import com.sw.util.SystemProperty;

@Controller
public class OrganizationgrantController  extends BaseController<Commission>{
	
	private static Logger logger = Logger.getLogger(OrganizationgrantController.class);
	@Resource
	private CommissionFeeService commissionFeeService;
	
	@Resource
	private TeamOrOrgPayRecordService teamOrOrgPayRecordService;
	
	@Resource
	private TeamOrOrgPayDetailService teamOrOrgPayDetailService;
	
	@Resource
	private AuditingService auditingService;
	
	@Resource
	private AdjustService adjustService;
	
	@Resource
	private ContractService contractService;
	
	/**
	 * 居间费发放
	 * 
	 * @param entity
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/commission/organizationgrant")
	public CommonModelAndView list(TeamOrOrgPayDetail entity, HttpServletRequest request, Map<String, Object> model) {
		Object obj = new CommonModelAndView().getCurrentStatus(entity, request);
		if (obj != null) {
			if (obj instanceof TeamOrOrgPayDetail) {
				entity = (TeamOrOrgPayDetail) obj;
			}
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, entity,messageSource);
		Map<String, Object> tradeType = initData.getTradeType().get(0);
		commonModelAndView.addObject("tradeType", tradeType);
		commonModelAndView.addObject("code", entity.getC());
		commonModelAndView.addObject("tab", request.getParameter("tab"));
		model.put("teamOrOrgPayDetail", entity);
		Contract ontractc =new Contract();
		model.put("ontractc", ontractc);
		return commonModelAndView;
	}
	
	/**
	 * 查询支付机构居间费列表集合
	 * 
	 * @param entity
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/commission/organizationgrant/orgGrid")
	public CommonModelAndView orgJson(TeamOrOrgPayDetail entity, HttpServletRequest request) {
		Object gridJson;
		Map<String, Object> map = null;
		try {
			//判断当前登录用户是否属于居间公司，如果是则查询该居间公司数据
			Organization organization = adjustService.isCommission();
			if (organization != null && organization.getIsCommission() == 1){
				entity.setOrgId(organization.getId());
			}
			if(entity.getOrgShortName()!=null){
				entity.setOrgShortName(URLDecoder.decode(entity.getOrgShortName(), Constant.ENCODING));
			}
			gridJson = teamOrOrgPayDetailService.getOrgGrid(entity);
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
	@RequestMapping("/commission/organizationgrant/grid")
	public CommonModelAndView json(TeamOrOrgPayDetail entity, HttpServletRequest request) {
		Object gridJson;
		Map<String, Object> map = null;
		try {
//			if(entity.getStatus() == 10){
//				Commission commission = new Commission();
//				//获取不发放列表
//				commission.setPayStatus((short)6);
//				gridJson = auditingService.getGrid(commission);
//			}else{
				gridJson = teamOrOrgPayDetailService.getGrid(entity);
//			}
			map = (gridJson == null ? null : (Map<String, Object>) gridJson);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, entity, request);
	}
	
	/**
	 * 居间费发放操作（发放成功和发放失败）
	 * 
	 * @param entity
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/commission/organizationgrant/paySuccess")
	public CommonModelAndView payOperation(TeamOrOrgPayRecord entity,HttpServletRequest request) {
		String status;
	    try {
	    	Commission commission = new Commission();
	    	commission.setTeamID(entity.getTeamID());
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
    		teamOrOrgPayRecordService.update(entity);
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
	 * 验证查看合同
	 * 
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping("/commission/organizationgrant/download")
	public CommonModelAndView downloadChecked(Contract entity,HttpServletRequest request) {
		String status;
	    try {
	    	entity.setStatus(2);
	    	
	    	entity = contractService.getOneContract(entity);
	    	
	    	if(entity!=null && entity.getFileUrl()!=null && !entity.getFileUrl().equals("") && entity.getFileName()!=null && !entity.getFileName().equals("")){
	    		
	    		status = "have";
	    		
	    	}else{
	    		
	    		status = "nohave";
	    		
	    	}
	    	
		} catch (Exception e) {
			status = "error";
			logger.error(DetailException.expDetail(e, getClass()));
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("status", status);
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 查看合同
	 * 
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping("/commission/organizationgrant/downloadfile")
	public String download(Contract entity,HttpServletRequest request,HttpServletResponse response) {
	    try {
	    	entity.setStatus(2);
	    	//ftp服务器信息
	    	String ftp_ip = SystemProperty.getInstance("config").getProperty("ftp.ip");
	    	String ftp_port = SystemProperty.getInstance("config").getProperty("ftp.port");
	    	String ftp_username = SystemProperty.getInstance("config").getProperty("ftp.username");
	    	String ftp_password = SystemProperty.getInstance("config").getProperty("ftp.password");
	    	String contract_temp_url = request.getSession().getServletContext().getRealPath("/\\")+ SystemProperty.getInstance("config").getProperty("contract.temp.url");
	    	
	    	
	    	entity = contractService.getOneContract(entity);
	    	
	    	if(entity!=null && entity.getFileUrl()!=null && !entity.getFileUrl().equals("") && entity.getFileName()!=null && !entity.getFileName().equals("")){
	    		
	    		String folder = entity.getFileUrl().substring(0, entity.getFileUrl().lastIndexOf("/")+1);
	    		
	    		//List<InputStream> list=FTPUtil.downFile(ftp_ip, ftp_username, ftp_password, Integer.parseInt(ftp_port), "gbk", folder, entity.getFileName());
	    		contract_temp_url+=System.currentTimeMillis()+"/";
	    		
	    		boolean is = FTPUtil.downFile(ftp_ip, ftp_username, ftp_password, Integer.parseInt(ftp_port), CharsetUtil.GBK, CharsetUtil.ISO_8859_1, folder, entity.getFileName(), contract_temp_url, entity.getFileName());
	    		
	    		InputStream inStream = null;
	    		
	    		if(is){
	    			String localPath = contract_temp_url+entity.getFileName();
	    			File file=new File(localPath);
	    			inStream =new FileInputStream(file);
	    		}
	    		
	    		response.reset();

    	        response.setContentType("application/octet-stream;charset=utf-8");
    	        
	    	    String fileName = response.encodeURL(new String(entity.getFileName().getBytes(),"ISO8859_1"));//转码

	    	    response.setHeader("Content-Disposition", "attachment; filename=\""+fileName+"\"");

    	        ServletOutputStream out = response.getOutputStream();

    	        //循环取出流中的数据

    	        byte[] b = new byte[1024];

    	        int len;

    	        while((len=inStream.read(b)) >0)

    	        out.write(b,0,len);

    	        response.setStatus( response.SC_OK );

    	        response.flushBuffer();

    	        out.close();

    	        inStream.close();
    	        
    	        return null;
    	        
	    	}
	    	
		} catch (Exception e) {
			
			logger.error(DetailException.expDetail(e, getClass()));
			
		}
		
		return null;
	}
	
	/**
	 * 发票通知
	 * 
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping("/commission/organizationgrant/invoice")
	public CommonModelAndView invoice(Member entity,HttpServletRequest request) {
		String status;
	    try {
	    	Commission commission = new Commission();
	    	commission.setTeamID(new Long(entity.getTeamID()));
	    	long count= adjustService.getRecordNotDo(commission);
	    	if  (count>0){
	    		Map<String,Object> map = new HashMap<String,Object>();
	    		map.put("mark", "1");
	    		map.put("contract", entity);
	    		return new CommonModelAndView("jsonView", map);
	    	}
	    	commissionFeeService.invoice(entity);
	    	status = "success";
		} catch (Exception e) {
			status = "error";
			logger.error(DetailException.expDetail(e, getClass()));
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("status", status);
		map.put("contract", entity);
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 恢复居间费发放
	 * 
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping("/commission/organizationgrant/recover")
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
	
	@RequestMapping("/commission/organizationgrant/detail")
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
		commonModelAndView.addObject("commission",entity);
		commonModelAndView.addObject("tab",request.getParameter("tab"));
		return commonModelAndView;
	}

	@RequestMapping("/commission/organizationgrant/export")
	public @ResponseBody Map<String,Object> export(TeamOrOrgPayDetail entity,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		String colName="";
		String colModel="";
		colName = "机构简称,开户行,账户名,卡号,居间费总金额(元)";
		colModel = "title1,title2,title3,title4,payAmount";	
		if(entity.getOrgShortName()!=null){
			entity.setOrgShortName(new String(entity.getOrgShortName().getBytes("ISO-8859-1"),"UTF-8"));
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
			WritableWorkbook excel = null;
			response.reset();
			response.setHeader("Content-disposition", "attachment; filename = Excel" + temp + ".xls");
			response.setContentType("application/msexcel");
			excel = Workbook.createWorkbook(response.getOutputStream());
			teamOrOrgPayDetailService.exportExcel(colName, colModel, excel, entity);
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
	public String downloadfile(Commission entity, HttpServletRequest request,
			HttpServletResponse response) {
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

	@Override
	public String uploadfile(Commission entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
