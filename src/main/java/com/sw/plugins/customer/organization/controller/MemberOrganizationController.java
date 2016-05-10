package com.sw.plugins.customer.organization.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import jxl.Workbook;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sw.core.common.Constant;
import com.sw.core.controller.BaseController;
import com.sw.core.controller.CommonModelAndView;
import com.sw.core.exception.DetailException;
import com.sw.plugins.customer.member.entity.Member;
import com.sw.plugins.customer.member.service.MemberApplicationService;
import com.sw.plugins.customer.member.service.MemberService;
import com.sw.plugins.customer.organization.entity.MemberOrganization;
import com.sw.plugins.customer.organization.service.MemberOrganizationService;
import com.sw.plugins.customer.team.entity.TeamOrOrgApplication;
import com.sw.plugins.customer.team.service.TeamOrOrgApplicationService;
import com.sw.plugins.system.user.service.UserLoginService;
import com.sw.util.CommonUtil;
import com.sw.util.SystemProperty;

/**
 * 理财师机构控制器，查询，审核，注销，删除等功能
 * 
 * @author Administrator
 */
@Controller
public class MemberOrganizationController extends BaseController<MemberOrganization> {

	private static Logger logger = Logger.getLogger(MemberOrganizationController.class);
	@Resource
	private TeamOrOrgApplicationService teamOrOrgApplicationService;
	@Resource
	private MemberOrganizationService memberOrganizationService;
	@Resource
	private MemberService memberService;
	@Resource
	private MemberApplicationService memberApplicationService; 
	@Resource
	private UserLoginService userLoginService;
	/**
	 * 理财师机构列表方法
	 * @param organization
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/organization")
	public CommonModelAndView list(MemberOrganization memberOrganization, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, memberOrganization,messageSource);
		Map<String, Object> teamOrOrgStatus = initData.getTeamOrOrgStatus();
		commonModelAndView.addObject("teamOrOrgStatus", teamOrOrgStatus);
		commonModelAndView.addObject("code", memberOrganization.getC());
		return commonModelAndView;
	}
	/**
	 * 理财师列表方法
	 * @param organization
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/organization/organizationmember")
	public CommonModelAndView listMember(MemberOrganization memberOrganization, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, memberOrganization,messageSource);
		//获取理财师类型
		Map<String, Object> memberType = initData.getMemberType();
		//获取理财师的状态
		Map<String, Object> memberStatus = initData.getMemberStatus();
		commonModelAndView.addObject("memberType", memberType);
		commonModelAndView.addObject("memberStatus", memberStatus);
		commonModelAndView.addObject("code", memberOrganization.getC());
		return commonModelAndView;
	}
	/**
	 * 查询理财师机构信息 返回json格式
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/organization/grid")
	public CommonModelAndView json(MemberOrganization memberOrganization, String nodeid, HttpServletRequest request) {
		memberOrganization.setName(CommonUtil.convertSearchSign(memberOrganization.getName()));//获取查询条件
		Map<String, Object> map = null;
		try {
			map = memberOrganizationService.getGrid(memberOrganization);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
	/**
	 * 查询理财师信息 返回json格式
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/organizationmember/grid")
	public CommonModelAndView memberJson(MemberOrganization memberOrganization, String nodeid, HttpServletRequest request) {
		Map<String, Object> map = null;
		Member member =new Member();
		if (memberOrganization.getId()!=null){
			member.setTeamID(Integer.valueOf(memberOrganization.getId().toString()));
		}
		member.setRows(memberOrganization.getRows());
		member.setPage(memberOrganization.getPage());
		try {
			map = memberService.getGridForTeamOrOrgmember(member);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
	/**
	 * 理财师审核列表方法
	 * @param team
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/organization/checklist")
	public CommonModelAndView checklist(MemberOrganization memberOrganization, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, memberOrganization,messageSource);
		Map<String, Object> teamOrOrgApplicationType = initData.getTeamOrOrgApplicationType();
		Map<String, Object> teamOrOrgApplicationStatus = initData.getTeamOrOrgApplicationStatus();
		commonModelAndView.addObject("teamOrOrgApplicationType", teamOrOrgApplicationType);
		commonModelAndView.addObject("teamOrOrgApplicationStatus", teamOrOrgApplicationStatus);
		commonModelAndView.addObject("code", memberOrganization.getC());
		return commonModelAndView;
	}
	@RequestMapping("/customer/organization/checklist/grid")
	public CommonModelAndView checklistJson(MemberOrganization memberOrganization, String nodeid, HttpServletRequest request) {
		Map<String, Object> map = null;
		TeamOrOrgApplication teamOrOrgApplication =new TeamOrOrgApplication();
		if (memberOrganization.getId()!=null){
			teamOrOrgApplication.setTeamID(Integer.valueOf(memberOrganization.getId().toString()));
		}
		teamOrOrgApplication.setRows(memberOrganization.getRows());
		try {
			map = teamOrOrgApplicationService.getGrid(teamOrOrgApplication);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
	/**
	 * 理财师机构信息修改操作
	 * 
	 * @param provider 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("//customer/organization/update")
	public CommonModelAndView modify(MemberOrganization memberOrganization, HttpServletRequest request, Map<String,Object> model){
		String code = memberOrganization.getC();
		try {
			memberOrganization = memberOrganizationService.getOneForUpdate(memberOrganization);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		memberOrganization.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, memberOrganization, messageSource);
		commonModelAndView.addObject("memberOrganization", memberOrganization);
		model.put("memberOrganization", memberOrganization);
		String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
		commonModelAndView.addObject("ftpHttpUrl",ftpHttpUrl);
		return commonModelAndView;
	}
	/**
	 * 理财师机构营业执照上传
	 * 
	 * @param provider
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	@RequestMapping("/customer/organization/uploadfile")
	public String uploadfile(MemberOrganization memberOrganization, HttpServletRequest request, HttpServletResponse response) {
		try {
			response.getWriter().print(memberOrganizationService.upload(memberOrganization, request));
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return ERROR;
	}
	/**
	 * 理财师机构保存操作
	 * 
	 * @param provider
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/customer/organization/save", method = RequestMethod.POST)
	public CommonModelAndView save(MemberOrganization memberOrganization, HttpServletRequest request) {
		String viewName = null;
		String code = memberOrganization.getC();
		try {
			memberOrganizationService.update(memberOrganization);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			viewName = this.ERROR;
		}
		memberOrganization.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, memberOrganization, request, messageSource);
		return commonModelAndView;
	}
	/**
	 * 理财师详细查看操作
	 * 
	 * @param client
	 * @param request
	 * @return
	 */
	@RequestMapping("/customer/organization/organizationmember/memberdetail")
	public CommonModelAndView clientdetail(Member member, HttpServletRequest request, Map<String, Object> model) {
		String code = member.getC();
		try {
			member = memberService.getOneForDetail(member);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		member.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, member, messageSource);
		//获取理财师类型
		Map<String, Object> memberType = initData.getMemberType();
		//获取理财师性别
		Map<String, Object> memberGender = initData.getMemberGender();
		//获取理财师证件类型
		Map<String, Object> memberIDCardType = initData.getMemberIDCardType();
		//获取理财师的状态
		Map<String, Object> memberStatus = initData.getMemberStatus();
		commonModelAndView.addObject("memberStatus", memberStatus);
		commonModelAndView.addObject("memberType", memberType);
		commonModelAndView.addObject("memberGender", memberGender);
		commonModelAndView.addObject("memberIDCardType", memberIDCardType);
		model.put("member", member);
		return commonModelAndView;
	}
	/**
	 * 理财师机构暂停任务
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/organization/suspendtask")
	public CommonModelAndView updateSuspendOrganization(MemberOrganization memberOrganization,HttpServletRequest request) {
		
		String mark="false";
		try {
			memberOrganizationService.updateSuspendOrganization(memberOrganization);
			mark="true";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("mark", mark);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView", map);
		return commonModelAndView;
	}
	/**
	 * 理财师机构恢复任务
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/organization/recoverytask")
	public CommonModelAndView updateRecoveryOrganization(MemberOrganization memberOrganization,HttpServletRequest request) {
		
		String mark="false";
		try {
			memberOrganizationService.updateRecoveryOrganization(memberOrganization);
			mark="true";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("mark", mark);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView", map);
		return commonModelAndView;
	}
	
	/**
	 * 理财师机构申请审核
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/organization/saveOrganizationCheck")
	public CommonModelAndView saveOrganizationCheck(MemberOrganization memberOrganization,HttpServletRequest request) {
		String mark="false";
		try {
			memberOrganizationService.saveOrganizationCheck(memberOrganization);
			mark="true";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("mark", mark);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView", map);
		return commonModelAndView;
	}
	/**
	 * 理财师机构申请退回
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/organization/backOrganizationCheck")
	public CommonModelAndView savebackOrganizationCheck(MemberOrganization memberOrganization,HttpServletRequest request) {
		String mark="false";
		try {
			memberOrganizationService.savebackOrganizationCheck(memberOrganization);
			mark="true";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	    
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("mark", mark);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView", map);
		return commonModelAndView;
	}
	/**
	 * 机构认证审核信息查询
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/organization/checklist/createcheck")
	public CommonModelAndView createcheck(MemberOrganization memberOrganization,BindingResult result,Map<String, Object> model, HttpServletRequest request) throws Exception {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, memberOrganization,messageSource);
		String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
		commonModelAndView.addObject("ftpHttpUrl",ftpHttpUrl);
		try {
			//查询机构创建申请
			memberOrganization.setCheckType(1);
			MemberOrganization createMemberOrganization=memberOrganizationService.getOne(memberOrganization);
			commonModelAndView.addObject("createMemberOrganization", createMemberOrganization);
			model.put("createMemberOrganization", createMemberOrganization);
		} catch (Exception e) {
			DetailException.expDetail(e, MemberOrganizationController.class);
			logger.debug(e.getMessage());
		}
		return commonModelAndView;
	}
	/**
	 * 机构认证审核信息详细
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/organization/checklist/createdetail")
	public CommonModelAndView createdetail(MemberOrganization memberOrganization,BindingResult result,Map<String, Object> model, HttpServletRequest request) throws Exception {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, memberOrganization,messageSource);
		String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
		commonModelAndView.addObject("ftpHttpUrl",ftpHttpUrl);
		try {
			//查询机构创建申请
			memberOrganization.setCheckType(1);
			MemberOrganization createMemberOrganization=memberOrganizationService.getOne(memberOrganization);
			commonModelAndView.addObject("createMemberOrganization", createMemberOrganization);
			model.put("createMemberOrganization", createMemberOrganization);
		} catch (Exception e) {
			DetailException.expDetail(e, MemberOrganizationController.class);
			logger.debug(e.getMessage());
		}
		return commonModelAndView;
	}
	/**
	 * 机构变更审核信息详细
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/organization/checklist/changedetail")
	public CommonModelAndView changedetail(MemberOrganization memberOrganization,BindingResult result,Map<String, Object> model, HttpServletRequest request) throws Exception {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, memberOrganization,messageSource);
		String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
		commonModelAndView.addObject("ftpHttpUrl",ftpHttpUrl);
		try {
			//查询机构变更申请
			memberOrganization.setCheckType(2);
			MemberOrganization changeMemberOrganization=memberOrganizationService.getOne(memberOrganization);
			commonModelAndView.addObject("changeMemberOrganization", changeMemberOrganization);
		} catch (Exception e) {
			DetailException.expDetail(e, MemberOrganizationController.class);
			logger.debug(e.getMessage());
		}
		return commonModelAndView;
	}
	/**
	 * 机构年检审核信息查询
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/organization/checklist/annualcheck")
	public CommonModelAndView annualcheck(MemberOrganization memberOrganization,BindingResult result,Map<String, Object> model, HttpServletRequest request) throws Exception {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, memberOrganization,messageSource);
		String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
		commonModelAndView.addObject("ftpHttpUrl",ftpHttpUrl);
		try {
			//查询机构年检申请
			memberOrganization.setCheckType(3);
			MemberOrganization annualMemberOrganization=memberOrganizationService.getOne(memberOrganization);
			commonModelAndView.addObject("annualMemberOrganization", annualMemberOrganization);
			model.put("annualMemberOrganization", annualMemberOrganization);
		} catch (Exception e) {
			DetailException.expDetail(e, MemberOrganizationController.class);
			logger.debug(e.getMessage());
		}
		return commonModelAndView;
	}
	/**
	 * 机构年检审核信息详细
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/organization/checklist/annualdetail")
	public CommonModelAndView annualdetail(MemberOrganization memberOrganization,BindingResult result,Map<String, Object> model, HttpServletRequest request) throws Exception {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, memberOrganization,messageSource);
		String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
		commonModelAndView.addObject("ftpHttpUrl",ftpHttpUrl);
		try {
			//查询机构创建申请
			memberOrganization.setCheckType(3);
			MemberOrganization annualMemberOrganization=memberOrganizationService.getOne(memberOrganization);
			commonModelAndView.addObject("annualMemberOrganization", annualMemberOrganization);
		} catch (Exception e) {
			DetailException.expDetail(e, MemberOrganizationController.class);
			logger.debug(e.getMessage());
		}
		return commonModelAndView;
	}
	/**
	 * 机构注销审核信息查询
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/organization/checklist/cancelcheck")
	public CommonModelAndView cancelcheck(MemberOrganization memberOrganization,BindingResult result,Map<String, Object> model, HttpServletRequest request) throws Exception {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, memberOrganization,messageSource);
		String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
		commonModelAndView.addObject("ftpHttpUrl",ftpHttpUrl);
		try {
			
			//查询机构创建申请
			memberOrganization.setCheckType(4);
			MemberOrganization cancelMemberOrganization=memberOrganizationService.getOne(memberOrganization);
			commonModelAndView.addObject("cancelMemberOrganization", cancelMemberOrganization);
			model.put("cancelMemberOrganization", cancelMemberOrganization);
		} catch (Exception e) {
			DetailException.expDetail(e, MemberOrganizationController.class);
			logger.debug(e.getMessage());
		}
		return commonModelAndView;
	}
	/**
	 * 机构注销审核信息查询
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/organization/checklist/canceldetail")
	public CommonModelAndView canceldetail(MemberOrganization memberOrganization,BindingResult result,Map<String, Object> model, HttpServletRequest request) throws Exception {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, memberOrganization,messageSource);
		String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
		commonModelAndView.addObject("ftpHttpUrl",ftpHttpUrl);
		try {
			//查询机构创建申请
			memberOrganization.setCheckType(4);
			MemberOrganization cancelMemberOrganization=memberOrganizationService.getOne(memberOrganization);
			commonModelAndView.addObject("cancelMemberOrganization", cancelMemberOrganization);
		} catch (Exception e) {
			DetailException.expDetail(e, MemberOrganizationController.class);
			logger.debug(e.getMessage());
		}
		return commonModelAndView;
	}
	/**
	 * 机构变更审核信息审核
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/organization/checklist/changecheck")
	public CommonModelAndView changecheck(MemberOrganization memberOrganization,BindingResult result,Map<String, Object> model, HttpServletRequest request) throws Exception {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, memberOrganization,messageSource);
		String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
		commonModelAndView.addObject("ftpHttpUrl",ftpHttpUrl);
		try {
			//查询机构变更申请
			memberOrganization.setCheckType(2);
			MemberOrganization changeMemberOrganization=memberOrganizationService.getOne(memberOrganization);
			commonModelAndView.addObject("changeMemberOrganization", changeMemberOrganization);
		} catch (Exception e) {
			DetailException.expDetail(e, MemberOrganizationController.class);
			logger.debug(e.getMessage());
		}
		return commonModelAndView;
	}
	/**
	 * 机构注销
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/organization/cancel")
	public CommonModelAndView updateCancel(MemberOrganization memberOrganization,HttpServletRequest request) {
		String mark="false";
		try {
			memberOrganizationService.updateCancel(memberOrganization);
			mark="true";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("mark", mark);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView", map);
		return commonModelAndView;
	}
	@Override
	public String downloadfile(MemberOrganization entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public CommonModelAndView functionJsonData(MemberOrganization entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	@RequestMapping("/customer/organization/valid")
	public CommonModelAndView valid(@Valid MemberOrganization memberOrganization, BindingResult result, Map<String, Object> model, HttpServletRequest request,HttpServletResponse response) {
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		MemberOrganization organization = new MemberOrganization();
		//判断名称是否存在
		
		if (!memberOrganization.getName().isEmpty()) {
			organization = new  MemberOrganization();
			organization.setName(memberOrganization.getName());
			organization.setId(memberOrganization.getId());
			try {
				Long count = (Long) memberOrganizationService.memberOrganizationCount(organization);
				if (count > 0) {
					result.rejectValue("name", "dupname");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}//判断该验证过则是否已经存在 1已经存在 0不存在
			
		}
		//判断简称是否存在
		if (!memberOrganization.getShortName().isEmpty()) {
			organization = new  MemberOrganization();
			organization.setShortName(memberOrganization.getShortName());
			organization.setId(memberOrganization.getId());
			Long count = null;
			try {
				count = (Long) memberOrganizationService.memberOrganizationCount(organization);
				if (count > 0) {
					result.rejectValue("shortName", "dupshortname");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}//判断该验证过则是否已经存在 1已经存在 0不存在
			
		}
		//判断组织机构代码是否存在
		if (!memberOrganization.getLicenceCode().isEmpty()) {
			organization = new  MemberOrganization();
			organization.setLicenceCode(memberOrganization.getLicenceCode());
			organization.setId(memberOrganization.getId());
			Long count = null;
			try {
				count = (Long) memberOrganizationService.memberOrganizationCount(organization);
				if (count > 0) {
					result.rejectValue("licenceCode", "dupLicenceCode");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}//判断该验证过则是否已经存在 1已经存在 0不存在	
		}
		//判断身份证号是否存在
		if (!memberOrganization.getiDCardNumber().isEmpty()) {
			organization = new  MemberOrganization();
			organization.setiDCardNumber(memberOrganization.getiDCardNumber());
			organization.setId(memberOrganization.getId());
			Long count = null;
			try {
				count = (Long) memberOrganizationService.memberOrganizationCount(organization);
				if (count > 0) {
					result.rejectValue("iDCardNumber", "dupiDCardNumber");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}//判断该验证过则是否已经存在 1已经存在 0不存在
			
		}
		if (result.hasErrors()) {
			commonModelAndView.addBindingErrors(model, result, messageSource);
			return commonModelAndView;
		}
		return commonModelAndView;
	}

	@RequestMapping("/customer/organization/mobilephone.json")
	public @ResponseBody Map<String, Object> mobilephone(Member member, HttpServletRequest request, Map<String, Object> model) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			member = memberService.getOneForDetail(member);
			map.put("mobilePhone", member.getMobilePhone()) ;
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}

		return map;
	}
	

	@RequestMapping("/customer/organization/homephone.json")
	public @ResponseBody Map<String, Object> homephone(Member member, HttpServletRequest request, Map<String, Object> model) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			member = memberService.getOneForDetail(member);
			map.put("homePhone", member.getHomePhone()) ;
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}

		return map;
	}
	
	@RequestMapping("/customer/organization/account.json")
	public @ResponseBody Map<String, Object> account(Member member, HttpServletRequest request, Map<String, Object> model) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			member = memberService.getOneForDetail(member);
			map.put("account", member.getAccount()) ;
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}

		return map;
	}
	
	@RequestMapping("/customer/organization/idcard.json")
	public @ResponseBody Map<String, Object> idcard(Member member, HttpServletRequest request, Map<String, Object> model) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			member = memberService.getOneForDetail(member);
			map.put("idcard", member.getiDCard()) ;
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}

		return map;
	}
	
	/**
	 * 导出数据到EXCEL
	 * @param order
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/customer/organization/export")
	public @ResponseBody Map<String, Object> dataExport(MemberOrganization memOrganization,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		String colName ="机构名称,机构简称,管理人姓名,管理人电话,管理人邮箱,审核状态";
		String colModel = "name,shortName,memberName,memberPhone,memberEmail,checkCount";
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
		try {
			WritableWorkbook excel = null;
			response.reset();
			response.setHeader("Content-disposition", "attachment; filename = organization" + temp + ".xls");
			response.setContentType("application/msexcel");
			excel = Workbook.createWorkbook(response.getOutputStream());
			excel = memberOrganizationService.exportExcel(memOrganization,colName, colModel, excel);
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
	 * 设置渠道经理编号
	 * 
	 * @param memberOrganization
	 * @param request
	 * @return
	 */
	@RequestMapping("/customer/organization/channelmanager")
	public CommonModelAndView channelmanager(MemberOrganization memberOrganization,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			memberOrganizationService.update(memberOrganization);
			map.put("status", "success");
		} catch (Exception e) {
			map.put("status", "error");
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
}
