package com.sw.plugins.commission.contract.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.sw.core.controller.BaseController;
import com.sw.core.controller.CommonModelAndView;
import com.sw.core.exception.DetailException;
import com.sw.plugins.commission.contract.entity.Contract;
import com.sw.plugins.commission.contract.service.ContractService;
import com.sw.plugins.commission.organizationgrant.entity.CommissionFee;
import com.sw.plugins.cooperate.provider.entity.Provider;
import com.sw.plugins.customer.member.entity.Member;
import com.sw.plugins.customer.organization.entity.MemberOrganization;
import com.sw.plugins.customer.organization.service.MemberOrganizationService;
import com.sw.plugins.storage.entity.GridFile;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.plugins.system.organization.service.OrganizationService;
import com.sw.plugins.system.user.entity.User;
import com.sw.util.FTPUtil;
import com.sw.util.SystemProperty;

/**
 * 协议管理控制器，负责协议与合同的添加，修改，启用等功能拦截处理
 * 
 * @author Administrator
 * 
 */
@Controller
public class ContractController extends BaseController<Contract> {

	private static Logger logger = Logger.getLogger(ContractController.class);
	@Resource
	private ContractService contractService;
	@Resource
	private MemberOrganizationService memberOrganizationService;
	@Resource
	private OrganizationService organizationService;
	/**
	 * 跳转到协议列表列表页面
	 * 
	 * @param code
	 * @param organization
	 * @param request
	 * @return
	 */
	@RequestMapping("/commission/contract")
	public CommonModelAndView list(Contract contract, HttpServletRequest request) {
		User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Organization organization = new Organization();
		if (currentUser.getOrgId()!=null){
			organization.setId(currentUser.getOrgId());
			try {
				organization = organizationService.getOne(organization);
				if (organization!=null&&organization.getIsCommission()==1){
					contract.setOrgID(Integer.valueOf(currentUser.getOrgId().toString()));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, contract, messageSource);
		Map<String, Object> agreementStatus = initData.getAgreementStatus();
		commonModelAndView.addObject("agreementStatus", agreementStatus);
		Map<String, Object> contractStatus = initData.getContractStatus();
		commonModelAndView.addObject("contractStatus", contractStatus);
		Map<String, Object> contractType = initData.getContractType();
		commonModelAndView.addObject("contractType", contractType);
		commonModelAndView.addObject("c", contract.getC());
		commonModelAndView.addObject("ftpUrl", SystemProperty.getInstance("config").getProperty("ftp.http.url"));
		return commonModelAndView;
	}
	/**
	 * 查询协议列表信息 返回json格式
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/commission/contract/grid")
	public CommonModelAndView contractlistJson(Contract contract,HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			map = contractService.getGrid(contract);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
	/**
	 * 查询协议列表信息 返回json格式
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/commission/orgcontract/grid")
	public CommonModelAndView orgContractlistJson(Contract contract,HttpServletRequest request) {
		Map<String, Object> map = null;
		if (contract.getId()!=null){
		}
		try {
			map = contractService.getOrgGrid(contract);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
	/**
	 * 协议创建操作
	 * 
	 * @param provider
	 * @param request
	 * @return
	 */
	@RequestMapping("/commission/contract/create")
	public CommonModelAndView create(Contract contract, HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, contract, messageSource);
		return commonModelAndView;
	}
	/**
	 * 合同创建操作
	 * 
	 * @param provider
	 * @param request
	 * @return
	 */
	@RequestMapping("/commission/contract/contractcreate")
	public CommonModelAndView contractcreate(Contract contract, HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, contract, messageSource);
		Map<String, Object> contractType = initData.getContractType();
		List<MemberOrganization> orgList=null;
		try {
			MemberOrganization memberOrganization = new MemberOrganization();
			orgList= memberOrganizationService.getAllMemberOrganizationContract(memberOrganization);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		commonModelAndView.addObject("contractType", contractType);
		commonModelAndView.addObject("orgList", orgList);
		return commonModelAndView;
	}
	/**
	 * 跳转到协议修改页面
	 * 
	 * @param code
	 * @param organization
	 * @param request
	 * @return
	 */
	@RequestMapping("/commission/contract/modify")
	public CommonModelAndView modify(Contract contract,Map<String, Object> model, HttpServletRequest request) {
		String code = contract.getC();
		Contract con = null;
		if (contract.getId() != null) {
			try {
				con = contractService.getOne(contract);
				if(con != null){
					con.setC(code);
				}
			} catch (Exception e) {
				logger.debug(e.getMessage());
			}
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, contract, messageSource);
		commonModelAndView.addObject("contract", con);
		model.put("contract", con);
		return commonModelAndView;
	}
	/**
	 * 跳转到合同修改页面
	 * 
	 * @param code
	 * @param organization
	 * @param request
	 * @return
	 */
	@RequestMapping("/commission/contract/contractmodify")
	public CommonModelAndView contractmodify(Contract contract,Map<String, Object> model, HttpServletRequest request) {
		String code = contract.getC();
		Contract con = null;
		Map<String, Object> contractType = initData.getContractType();
		List<MemberOrganization> orgList=null;
		if (contract.getId() != null) {
			try {
				MemberOrganization memberOrganization = new MemberOrganization();
				orgList= memberOrganizationService.getAllMemberOrganizationContract(memberOrganization);
				con = contractService.getOneContract(contract);
				if(con != null){
					con.setC(code);
				}
			} catch (Exception e) {
				logger.debug(e.getMessage());
			}
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, contract, messageSource);
		commonModelAndView.addObject("contract", con);
		commonModelAndView.addObject("contractType", contractType);
		commonModelAndView.addObject("orgList", orgList);
		commonModelAndView.addObject("ftpUrl", SystemProperty.getInstance("config").getProperty("ftp.http.url"));
		model.put("contract", con);
		return commonModelAndView;
	}
	/**
	 * 跳转到协议详细页面
	 * 
	 * @param code
	 * @param organization
	 * @param request
	 * @return
	 */
	@RequestMapping("/commission/contract/detail")
	public CommonModelAndView detail(Contract contract,Map<String, Object> model, HttpServletRequest request) {
		String code = contract.getC();
		Contract con = null;
		if (contract.getId() != null) {
			try {
				con = contractService.getOne(contract);
				if(con != null){
					con.setC(code);
				}
			} catch (Exception e) {
				logger.debug(e.getMessage());
			}
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, contract, messageSource);
		commonModelAndView.addObject("contract", con);
		model.put("contract", con);
		return commonModelAndView;
	}
	/**
	 * 协议创建修改操作
	 * 
	 * @param organization
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/commission/contract/save", method = RequestMethod.POST)
	public CommonModelAndView save(Contract contract,HttpServletRequest request) {
		// 视图名
		String viewName = null;
		try {
			contractService.saveOrUpdate(contract);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			contract.setErrorMsg(e.getMessage());
			viewName = this.ERROR;
			logger.error(DetailException.expDetail(e, getClass()));
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, contract,request, messageSource);
		return commonModelAndView;
	}
	/**
	 * 合同创建修改操作
	 * 
	 * @param organization
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/commission/contract/saveContract", method = RequestMethod.POST)
	public CommonModelAndView saveContract(Contract contract,HttpServletRequest request) {
		// 视图名
		String viewName = null;
		try {
			contractService.saveOrUpdateContract(contract);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			contract.setErrorMsg(e.getMessage());
			viewName = this.ERROR;
			logger.error(DetailException.expDetail(e, getClass()));
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, contract,request, messageSource);
		return commonModelAndView;
	}
	/**
	 * 协议启动
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/commission/contract/start")
	public CommonModelAndView contractstart(Contract contract,HttpServletRequest request) {
		String mark="false";
	    try {
	    	//更新理财师状态
	    	contractService.updateForStart(contract);
	    	/*发送消息给理财师机构人员，还没处理
	    	
	    	*/
	    	mark="true";
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("mark", mark);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView", map);
		return commonModelAndView;
	}
	/**
	 * 协议停用
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/commission/contract/stop")
	public CommonModelAndView contractstop(Contract contract,HttpServletRequest request) {
		String mark="false";
	    try {
	    	//更新理财师状态
	    	contractService.updateForStop(contract);
	    	/*发送消息给理财师机构人员，还没处理
	    	
	    	*/
	    	mark="true";
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("mark", mark);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView", map);
		return commonModelAndView;
	}
	/**
	 * 合同停用
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/commission/contract/stopContractStatus")
	public CommonModelAndView stopContractStatus(Contract contract,HttpServletRequest request) {
		String mark="false";
	    try {
	    	//更新理财师状态
	    	contractService.updateForContractStatus(contract);
	    	/*发送消息给理财师机构人员，还没处理
	    	
	    	*/
	    	mark="true";
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("mark", mark);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView", map);
		return commonModelAndView;
	}
	/**
	 * 协议的删除操作
	 * 
	 * @param member
	 * @param result
	 * @return
	 */
	@RequestMapping("/commission/contract/delete")
	public CommonModelAndView delete(Contract contract, HttpServletRequest request) {
		String viewName = null;
		try {
			contractService.delete(contract);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, contract,request, messageSource);
		return commonModelAndView;
	}
	/**
	 * 合同的删除操作
	 * 
	 * @param member
	 * @param result
	 * @return
	 */
	@RequestMapping("/commission/contract/contractdelete")
	public CommonModelAndView deleteContract(Contract contract, HttpServletRequest request) {
		String viewName = null;
		try {
			contractService.deleteContract(contract);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, contract,request, messageSource);
		return commonModelAndView;
	}
	@Override
	@RequestMapping("/commission/contract/valid")
	public CommonModelAndView valid(@Valid Contract contract, BindingResult result, Map<String, Object> model, HttpServletRequest request,	HttpServletResponse response) {
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		//判断标题是否存在
		if (!contract.getTitle().isEmpty()) {
			Contract newcontract = new  Contract();
			newcontract.setTitle(contract.getTitle());
			newcontract.setId(contract.getId());
			Long count = null;
			try {
				count = (Long) contractService.countForTitle(newcontract);
				if (count > 0) {
					result.rejectValue("title", "dupTitle");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			
		}
		if (result.hasErrors()) {
			commonModelAndView.addBindingErrors(model, result, messageSource);
			return commonModelAndView;
		}
		return commonModelAndView;
	}
	@RequestMapping("/commission/orgcontract/valid")
	public CommonModelAndView validorg(@Valid Contract contract, BindingResult result, Map<String, Object> model, HttpServletRequest request,	HttpServletResponse response) {
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		//判断标题是否存在
		if (!contract.getContractName().isEmpty()) {
			Contract newcontract = new  Contract();
			newcontract.setContractName(contract.getContractName());
			newcontract.setId(contract.getId());
			Long count = null;
			try {
				count = (Long) contractService.countForContractName(newcontract);
				if (count > 0) {
					result.rejectValue("contractName", "dupContractName");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			
		}
		//判断该机构的合同是否创建
		if (contract.getTeamOrOrgID()!=null&&!contract.getTeamOrOrgID().isEmpty()) {
			Contract newcontract = new  Contract();
			newcontract.setTeamOrOrgID(contract.getTeamOrOrgID());
			newcontract.setContractOrgID(contract.getContractOrgID());
			newcontract.setId(contract.getId());
			Long count = null;
			try {
				count = (Long) contractService.countForContract(newcontract);
				if (count > 0) {
					result.rejectValue("teamOrOrgID", "dupContractOrg");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			
		}
		if (result.hasErrors()) {
			commonModelAndView.addBindingErrors(model, result, messageSource);
			return commonModelAndView;
		}
		return commonModelAndView;
	}
	
	@Override
	public String downloadfile(Contract entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	//下载合同附件
	@RequestMapping("/commission/contract/download")
	public CommonModelAndView dowmload(Contract entity,HttpServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			entity = contractService.getFileUrl(entity);
			if(entity!=null){
				map.put("url", entity.getFileUrl());
				return new CommonModelAndView("jsonView",map);
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return null;
	}
	@Override
	public CommonModelAndView functionJsonData(Contract entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	@RequestMapping("/commission/orgcontract/uploadfile")
	public String uploadfile(Contract entity, HttpServletRequest request, HttpServletResponse response) {
		try {
			response.getWriter().print(contractService.upload(entity,request));
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return ERROR;
	}
	/**
	 * 文本编辑器上传图片
	 * 
	 * @param entity
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/commission/contract/kindeditor/uploadimg")
	public String kindEditorUpload(Contract entity, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		String tempFileName = request.getParameter("localUrl");
		String fileExtensionName = tempFileName.substring(tempFileName.lastIndexOf("."));
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", ".gif,.jpg,.jpeg,.png,.bmp");
		if (!Arrays.<String> asList(extMap.get("image").split(",")).contains(fileExtensionName)) {
			response.getWriter().println("上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get("image") + "格式。");
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String date = format.format(new Date());
		String path = SystemProperty.getInstance("config").getProperty("agreement.image.path") + date;
		// 重置文件名
		String newFileName = FTPUtil.getRandomString();
		String realFileName = newFileName + fileExtensionName;
		// FTP上传文件
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("imgFile");
		try {
			FTPUtil.uploadFile(file, path, realFileName);
			response.getWriter().print("{\"error\":0,\"url\":\"" + SystemProperty.getInstance("config").getProperty("ftp.http.url") + path + "/" + realFileName + "\"}");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	
}
