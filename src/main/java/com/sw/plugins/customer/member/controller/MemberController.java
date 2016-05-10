package com.sw.plugins.customer.member.controller;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import jxl.Workbook;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sw.core.common.Constant;
import com.sw.core.controller.BaseController;
import com.sw.core.controller.CommonModelAndView;
import com.sw.core.exception.DetailException;
import com.sw.plugins.customer.client.entity.Client;
import com.sw.plugins.customer.client.service.ClientService;
import com.sw.plugins.customer.member.entity.Member;
import com.sw.plugins.customer.member.entity.MemberChangeApplication;
import com.sw.plugins.customer.member.service.MemberChangeApplicationService;
import com.sw.plugins.customer.member.service.MemberService;
import com.sw.plugins.market.order.service.OrderService;
import com.sw.plugins.message.messageinterface.service.CommonMessageService;
import com.sw.plugins.message.task.entity.SendMessage;
import com.sw.plugins.message.task.entity.UserMessage;
import com.sw.plugins.system.user.entity.User;
import com.sw.util.Encrypt;
import com.sw.util.SystemProperty;


/**
 * 理财师控制器，查询，审核，注销，删除等功能
 * 
 * @author Administrator
 */
@Controller
public class MemberController extends BaseController<Member> {

	private static Logger logger = Logger.getLogger(MemberController.class);
	@Resource
	private OrderService orderService;
	@Resource
	private MemberService memberService;
	@Resource
	private ClientService clientService;
	@Resource
	private MemberChangeApplicationService memberChangeApplicationService;
	@Resource
	private CommonMessageService commonMessageService;

	/**
	 * 理财师列表方法
	 * @param member
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/member")
	public CommonModelAndView list(Member member, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, member,messageSource);
	/*	if (member.getName()!=""&&member.getName()!=null){
			try {
				String name = member.getName();
				byte[] bs = name.getBytes("ISO-8859-1");
				for (int i=0;i<bs.length;i++){
					byte b = bs[i];
					if (b==63){
						break;
					}else if (b>0){
						continue;
					}else if (b<0){
						name= new String(bs,"UTF-8");
						break;
					}
				}
				member.setName(name);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		
		Map<String, Object> memberStatus = initData.getMemberStatus();
		commonModelAndView.addObject("memberStatus", memberStatus);
		commonModelAndView.addObject("code", member.getC());
		return commonModelAndView;
	}
	/**
	 * 理财师客户列表方法
	 * @param member
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/member/memberclient")
	public CommonModelAndView listClient(Member member, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, member,messageSource);
		Map<String, Object> clientType = initData.getClientType();
		commonModelAndView.addObject("clientType", clientType);
		commonModelAndView.addObject("code", member.getC());
		commonModelAndView.addObject("clientStatus",this.initData.getClientStatus());
		return commonModelAndView;
	}
	/**
	 * 理财师客户查看操作
	 * 
	 * @param client
	 * @param request
	 * @return
	 */
	@RequestMapping("/customer/member/memberclient/clientdetail")
	public CommonModelAndView clientdetail(Client client, HttpServletRequest request, Map<String, Object> model) {
		String code = client.getC();
		try {
			client = clientService.getOne(client);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		client.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, client, messageSource);
		Map<String, Object> clientType = this.initData.getClientType();
		commonModelAndView.addObject("clientType", clientType);
		Map<String, Object> memberGender = this.initData.getMemberGender();
		commonModelAndView.addObject("memberGender", memberGender);
		Map<String, Object> clientMarried = this.initData.getClientMarried();
		commonModelAndView.addObject("clientMarried", clientMarried);
		commonModelAndView.addObject("memberIDCardType",this.initData.getMemberIDCardType());
		model.put("client", client);
		return commonModelAndView;
	}
	/**
	 * 理财师审核列表方法
	 * @param member
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/member/checklist")
	public CommonModelAndView checklist(Member member, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, member,messageSource);
		Map<String, Object> memberChangeApplicationType = initData.getMemberChangeApplicationType();
		Map<String, Object> memberChangeApplicationStatus = initData.getMemberChangeApplicationStatus();
		commonModelAndView.addObject("memberChangeApplicationType", memberChangeApplicationType);
		commonModelAndView.addObject("memberChangeApplicationStatus", memberChangeApplicationStatus);
		commonModelAndView.addObject("code", member.getC());
		return commonModelAndView;
	}
	/**
	 * 查询理财师审核列表信息 返回json格式
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/member/checklist/grid")
	public CommonModelAndView checklistJson(Member member,HttpServletRequest request) {
		Map<String, Object> map = null;
		MemberChangeApplication memberChangeApplication =new MemberChangeApplication();
		if (member.getId()!=null){
		memberChangeApplication.setMemberID(Integer.valueOf(member.getId().toString()));
		}
		memberChangeApplication.setRows(member.getRows());
		try {
			map = memberChangeApplicationService.getGrid(memberChangeApplication);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
	/**
	 * 理财师查看操作
	 * 
	 * @param client
	 * @param request
	 * @return
	 */
	@RequestMapping("/customer/member/detail")
	public CommonModelAndView detail(Member member, HttpServletRequest request, Map<String, Object> model) {
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
	 * 查询理财师信息 返回json格式
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/member/grid")
	public CommonModelAndView json(Member member,HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			map = memberService.getGrid(member);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
	/**
	 * 理财师暂停任务
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/member/suspendtask")
	public CommonModelAndView updateSuspendMember(Member member,HttpServletRequest request) {
		Member updateMember = new Member();
		updateMember.setId(member.getId());
		updateMember.setStatus(3);
		String mark="false";
		Map parameterMap = new HashMap();
		SendMessage sendMessage = new SendMessage();
		UserMessage us = new UserMessage();
		List<UserMessage> list = new ArrayList();
		User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    try {
	    	//更新理财师状态
	    	memberService.update(updateMember);
	    	//消息测试 begin
		    parameterMap = new HashMap();
			sendMessage = new SendMessage();
			us = new UserMessage();
			us.setUserID(member.getId());
			us.setUserType((short)3);
			list = new ArrayList();
			list.add(us);
			sendMessage.setUserList(list);
			//设置发送方式
			sendMessage.setSendWayStr("1");
			//设置模板参数
			sendMessage.setTemplateParameters(parameterMap);
			//设置模板code
			sendMessage.setTemplateCode("sys_manage_customer_lcsztyw");
			//设置发送者ID
			
			if (currentUser.getId()!=null){
				sendMessage.setSendUserID(currentUser.getId());
			}
			//设置发送者类型
			sendMessage.setSendUserType((short)1);
			//发送消息
			try {
				commonMessageService.send(sendMessage);
			} catch (Exception e) {
				
			}
		    //消息测试end
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
	 * 理财师恢复任务
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/member/recoverytask")
	public CommonModelAndView updateRecoveryMember(Member member,HttpServletRequest request) {
		Member updateMember = new Member();
		updateMember.setId(member.getId());
		updateMember.setStatus(2); 
		String mark="false";
		Map parameterMap = new HashMap();
		SendMessage sendMessage = new SendMessage();
		UserMessage us = new UserMessage();
		List<UserMessage> list = new ArrayList();
		User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    try {
	    	//更新理财师机构状态
	    	memberService.update(updateMember);
	    	//更新理财师状态
	    	memberService.update(updateMember);
	    	//消息测试 begin
		    parameterMap = new HashMap();
			sendMessage = new SendMessage();
			us = new UserMessage();
			us.setUserID(member.getId());
			us.setUserType((short)3);
			list = new ArrayList();
			list.add(us);
			sendMessage.setUserList(list);
			//设置发送方式
			sendMessage.setSendWayStr("1");
			//设置模板参数
			sendMessage.setTemplateParameters(parameterMap);
			//设置模板code
			sendMessage.setTemplateCode("sys_manage_customer_lcshfyw");
			//设置发送者ID
			
			if (currentUser.getId()!=null){
				sendMessage.setSendUserID(currentUser.getId());
			}
			//设置发送者类型
			sendMessage.setSendUserType((short)1);
			//发送消息
			try {
				commonMessageService.send(sendMessage);
			} catch (Exception e) {
				
			}
		    //消息测试end
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
	 * 理财师申请审核
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/member/saveMemberCheck")
	public CommonModelAndView saveMemberCheck(Member member,HttpServletRequest request) {
		String mark="false";

		try {
			memberChangeApplicationService.saveApplyAgree(member);
			mark="true";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		//如果申请是认证申请，那么发送消息
		if (member.getCheckType()!=null&&member.getCheckType().equals(1)){
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("mark", mark);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView", map);
		return commonModelAndView;
	}
	/**
	 * 理财师申请退回
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/member/backMemberCheck")
	public CommonModelAndView savebackMemberCheck(Member member,HttpServletRequest request) {
		String mark="false";
		try {
		memberChangeApplicationService.saveApplyBack(member);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		mark="true";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("mark", mark);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView", map);
		return commonModelAndView;
	}
	
	/**
	 * 理财师重置密码跳转
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/member/resetpassword")
	public CommonModelAndView resetpassword(Member member,BindingResult result,Map<String, Object> model,HttpServletRequest request) {
		String code = member.getC();
		
		try {
			member = memberService.getOneForDetail(member);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		model.put("member", member);
		member.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, member, messageSource);
		result.rejectValue("newPassword","Pattern.member.newPassword");
		result.rejectValue("confirmPwd","Pattern.member.confirmPwd");
		return commonModelAndView;
	}
	/**
	 * 理财师重置密码
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/member/updatePassword")
	public CommonModelAndView updatePassword(Member member,HttpServletRequest request) {
		String viewName = null;
		String code = member.getC();
	    try {
	    	Member updateMember = new Member();
			updateMember.setId(member.getId());
			updateMember.setPassword(Encrypt.getMD5(member.getNewPassword()));
	    	//更新理财师的密码为空
	    	memberService.update(updateMember);
	    	/*发送消息给理财师机构人员，还没处理
	    	
	    	*/
	    	viewName = this.SUCCESS;
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
			viewName = this.ERROR;
		}
		member.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, member, request, messageSource);
		return commonModelAndView;
	}
	/**
	 * 理财师注销
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/member/cannel")
	public CommonModelAndView updateCannel(Member member,HttpServletRequest request) {
		Member updateMember = new Member();
		updateMember.setId(member.getId());
		updateMember.setStatus(4);
		String mark="false";
	    try {
	    	//更新理财师机构状态
	    	memberService.update(updateMember);
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
	 * 理财师机构认证审核信息查询
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/member/checklist/attestationcheck")
	public CommonModelAndView attestationcheck(Member member,BindingResult result,Map<String, Object> model, HttpServletRequest request) throws Exception {
		
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, member,messageSource);
		try {
			//理财师认证申请
			member.setCheckType(1);
			Member createMember=memberService.getOne(member);
			String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
			//获取理财师性别
			Map<String, Object> memberGender = initData.getMemberGender();
			commonModelAndView.addObject("ftpHttpUrl",ftpHttpUrl);
			commonModelAndView.addObject("memberGender",memberGender);
			commonModelAndView.addObject("createMember", createMember);
			model.put("createMember", createMember);
		} catch (Exception e) {
			DetailException.expDetail(e, MemberController.class);
			logger.debug(e.getMessage());
		}
		return commonModelAndView;
	}
	/**
	 * 理财师机构认证审核信息详细
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/member/checklist/attestationdetail")
	public CommonModelAndView attestationdetail(Member member,BindingResult result,Map<String, Object> model, HttpServletRequest request) throws Exception {
		
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, member,messageSource);
		try {
			//理财师认证申请
			member.setCheckType(1);
			Member createMember=memberService.getOne(member);
			String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
			Map<String, Object> memberGender = initData.getMemberGender();
			commonModelAndView.addObject("ftpHttpUrl",ftpHttpUrl);
			commonModelAndView.addObject("memberGender",memberGender);
			commonModelAndView.addObject("createMember", createMember);
		} catch (Exception e) {
			DetailException.expDetail(e, MemberController.class);
			logger.debug(e.getMessage());
		}
		return commonModelAndView;
	}
	/**
	 * 理财师机构注销审核信息查询
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/member/checklist/cancelcheck")
	public CommonModelAndView cancelcheck(Member member,BindingResult result,Map<String, Object> model, HttpServletRequest request) throws Exception {
		
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, member,messageSource);
		try {
			//理财师注销申请
			member.setCheckType(2);
			Member cancelMember=memberService.getOne(member);
			String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
			commonModelAndView.addObject("ftpHttpUrl",ftpHttpUrl);
			commonModelAndView.addObject("cancelMember", cancelMember);
		} catch (Exception e) {
			DetailException.expDetail(e, MemberController.class);
			logger.debug(e.getMessage());
		}
		return commonModelAndView;
	}
	/**
	 * 理财师机构注销审核信息详细
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/member/checklist/canceldetail")
	public CommonModelAndView canceldetail(Member member,BindingResult result,Map<String, Object> model, HttpServletRequest request) throws Exception {
		
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, member,messageSource);
		try {
			//理财师注销申请
			member.setCheckType(2);
			Member cancelMember=memberService.getOne(member);
			String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
			commonModelAndView.addObject("ftpHttpUrl",ftpHttpUrl);
			commonModelAndView.addObject("cancelMember", cancelMember);
		} catch (Exception e) {
			DetailException.expDetail(e, MemberController.class);
			logger.debug(e.getMessage());
		}
		return commonModelAndView;
	}
	/**
	 * 理财师删除操作
	 * 
	 * @param member
	 * @param result
	 * @return
	 */
	@RequestMapping("/customer/member/delete")
	public CommonModelAndView delete(Member member, HttpServletRequest request) {
		String viewName = null;
		try {
			if(member.getIds() != null){
				memberService.deleteByArr(member);
			}else if(member.getId() != null){
				memberService.delete(member);
			}
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, member,request, messageSource);
		return commonModelAndView;
	}
	
	@Override
	public String downloadfile(Member entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public CommonModelAndView functionJsonData(Member entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String uploadfile(Member entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	@RequestMapping("/customer/member/valid")
	public CommonModelAndView valid(@Valid Member entity, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		String operate = (entity.getOperate() == null || entity.getOperate().equals("")) ? "0" : entity.getOperate();
		int operateTemp = Integer.parseInt(operate);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		try{
			switch(operateTemp){
			    case 3:{
			    	if (entity.getConfirmPwd().length()>=6&&entity.getNewPassword().length()>=6&&!entity.getConfirmPwd().equals(entity.getNewPassword())){
						result.rejectValue("confirmPwd", "differentNew");
			    	}
					if (result.hasErrors()) {
						commonModelAndView.addBindingErrors(model, result, messageSource);
						return commonModelAndView;
					}
			    	break;
			    }
			    default:break;
			}
		}catch(Exception e){
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return commonModelAndView;
	}
	
	@RequestMapping("/customer/member/export")
	public @ResponseBody Map<String,Object> export(Member member,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		String colName="";
		String colModel="";
		colName = "理财顾问,登录帐号,所属组织,身份证号,手机号,状态,注册时间";
		colModel = "name,excelAccount,teamname,iDCard,excelMobilePhone,status,RegisterTime";	
		
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
			memberService.exportMember(member, colName, colModel, excel) ;
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
	
	@RequestMapping("/customer/member/mobilephone.json")
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
	

	@RequestMapping("/customer/member/homephone.json")
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
	
	@RequestMapping("/customer/member/account.json")
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
	
	@RequestMapping("/customer/member/idcard.json")
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
}
