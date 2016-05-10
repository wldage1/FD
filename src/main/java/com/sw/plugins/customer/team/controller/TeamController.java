package com.sw.plugins.customer.team.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sw.core.controller.BaseController;
import com.sw.core.controller.CommonModelAndView;
import com.sw.core.exception.DetailException;
import com.sw.plugins.customer.member.entity.Member;
import com.sw.plugins.customer.member.entity.MemberApplication;
import com.sw.plugins.customer.member.service.MemberApplicationService;
import com.sw.plugins.customer.member.service.MemberService;
import com.sw.plugins.customer.organization.entity.MemberOrganization;
import com.sw.plugins.customer.organization.service.MemberOrganizationService;
import com.sw.plugins.customer.team.entity.Team;
import com.sw.plugins.customer.team.entity.TeamOrOrgApplication;
import com.sw.plugins.customer.team.service.TeamOrOrgApplicationService;
import com.sw.plugins.customer.team.service.TeamService;
import com.sw.util.CommonUtil;
import com.sw.util.SystemProperty;

/**
 * 理财师团队控制器，查询，审核，注销，删除等功能
 * 
 * @author Administrator
 */
@Controller
public class TeamController extends BaseController<Team> {

	private static Logger logger = Logger.getLogger(TeamController.class);

	@Resource
	private TeamService teamService;
	@Resource
	private TeamOrOrgApplicationService teamOrOrgApplicationService;
	@Resource
	private MemberService memberService;
	@Resource
	private MemberOrganizationService memberOrganizationService;
	@Resource
	private MemberApplicationService memberApplicationService; 
	/**
	 * 理财师团队列表方法
	 * @param team
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/team")
	public CommonModelAndView list(Team team, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, team,messageSource);
		Map<String, Object> teamOrOrgStatus = initData.getTeamOrOrgStatus();
		commonModelAndView.addObject("teamOrOrgStatus", teamOrOrgStatus);
		commonModelAndView.addObject("code", team.getC());
		return commonModelAndView;
	}
	/**
	 * 理财师列表方法
	 * @param team
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/team/teammember")
	public CommonModelAndView listMember(Team team, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, team,messageSource);
		//获取理财师类型
		Map<String, Object> memberType = initData.getMemberType();
		//获取理财师的状态
		Map<String, Object> memberStatus = initData.getMemberStatus();
		commonModelAndView.addObject("memberType", memberType);
		commonModelAndView.addObject("memberStatus", memberStatus);
		commonModelAndView.addObject("code", team.getC());
		return commonModelAndView;
	}
	/**
	 * 查询理财师团队信息 返回json格式
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/team/grid")
	public CommonModelAndView json(Team team, String nodeid, HttpServletRequest request) {
		team.setName(CommonUtil.convertSearchSign(team.getName()));//获取查询条件
		Map<String, Object> map = null;
		try {
			map = teamService.getGrid(team);
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
	@RequestMapping("/customer/teammember/grid")
	public CommonModelAndView memberJson(Team team, String nodeid, HttpServletRequest request) {
		Map<String, Object> map = null;
		Member member =new Member();
		if (team.getId()!=null){
			member.setTeamID(Integer.valueOf(team.getId().toString()));
		}
		member.setRows(team.getRows());
		member.setPage(team.getPage());
		try {
			map = memberService.getGridForTeammember(member);
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
	@RequestMapping("/customer/team/checklist")
	public CommonModelAndView checklist(Team team, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, team,messageSource);
		Map<String, Object> teamOrOrgApplicationType = initData.getTeamOrOrgApplicationType();
		Map<String, Object> teamOrOrgApplicationStatus = initData.getTeamOrOrgApplicationStatus();
		commonModelAndView.addObject("teamOrOrgApplicationType", teamOrOrgApplicationType);
		commonModelAndView.addObject("teamOrOrgApplicationStatus", teamOrOrgApplicationStatus);
		commonModelAndView.addObject("code", team.getC());
		return commonModelAndView;
	}
	@RequestMapping("/customer/team/checklist/grid")
	public CommonModelAndView checklistJson(Team team, String nodeid, HttpServletRequest request) {
		Map<String, Object> map = null;
		TeamOrOrgApplication teamOrOrgApplication =new TeamOrOrgApplication();
		if (team.getId()!=null){
			teamOrOrgApplication.setTeamID(Integer.valueOf(team.getId().toString()));
		}
		teamOrOrgApplication.setRows(team.getRows());
		try {
			map = teamOrOrgApplicationService.getGridForTeam(teamOrOrgApplication);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
	/**
	 * 理财师详细查看操作
	 * 
	 * @param client
	 * @param request
	 * @return
	 */
	@RequestMapping("/customer/team/teammember/memberdetail")
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
	 * 理财师团队暂停任务
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/team/suspendtask")
	public CommonModelAndView updateSuspendTeam(Team team,HttpServletRequest request) {
		String mark="false";
		try {
			teamService.updateSuspendTeam(team);
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
	 * 理财师团队恢复任务
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/team/recoverytask")
	public CommonModelAndView updateRecoveryTeam(Team team,HttpServletRequest request) {

		String mark="false";
		try {
			teamService.updateRecoveryTeam(team);
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
	 * 理财师团队审核信息查询
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/team/check")
	public CommonModelAndView check(Team team,BindingResult result,Map<String, Object> model, HttpServletRequest request) throws Exception {
		
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, team,messageSource);
		try {
			//查询团队创建申请
			team.setCheckType(1);
			Team createTeam=teamService.getOne(team);
			//查询团队管理人变更申请
			team.setCheckType(2);
			Team changeTeam=teamService.getOne(team);
			//查询团队年检申请
			team.setCheckType(3);
			Team annualTeam=teamService.getOne(team);
			//查询团队注销申请
			team.setCheckType(4);
			Team cancelTeam=teamService.getOne(team);
			commonModelAndView.addObject("createTeam", createTeam);
			commonModelAndView.addObject("changeTeam", changeTeam);
			commonModelAndView.addObject("annualTeam", annualTeam);
			commonModelAndView.addObject("cancelTeam", cancelTeam);
		} catch (Exception e) {
			DetailException.expDetail(e, TeamController.class);
			logger.debug(e.getMessage());
		}
		/** -- 验证提示 -- */
		return commonModelAndView;
	}
	/**
	 * 理财师团队申请审核
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/customer/team/saveTeamCheck")
	public CommonModelAndView saveTeamCheck(Team team,HttpServletRequest request){
		String mark="false";
		try {
			teamService.saveApplyAgree(team);
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
	 * 理财师团队申请退回
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/team/backTeamCheck")
	public CommonModelAndView savebackTeamCheck(Team team,HttpServletRequest request) {
		String mark="false";
		
		try {
			teamService.saveApplyBack(team);
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
	 * 团队认证审核信息查询
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/team/checklist/createcheck")
	public CommonModelAndView createcheck(Team team,BindingResult result,Map<String, Object> model, HttpServletRequest request) throws Exception {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, team,messageSource);
		try {
			//查询团队创建申请
			team.setCheckType(1);
			Team createTeam=teamService.getOne(team);
			commonModelAndView.addObject("createTeam", createTeam);
			model.put("createTeam", createTeam);
		} catch (Exception e) {
			DetailException.expDetail(e, TeamController.class);
			logger.debug(e.getMessage());
		}
		return commonModelAndView;
	}
	/**
	 * 团队认证审核信息详细
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/team/checklist/createdetail")
	public CommonModelAndView createdetail(Team team,BindingResult result,Map<String, Object> model, HttpServletRequest request) throws Exception {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, team,messageSource);
		try {
			//查询团队创建申请
			team.setCheckType(1);
			Team createTeam=teamService.getOne(team);
			commonModelAndView.addObject("createTeam", createTeam);
			model.put("createTeam", createTeam);
		} catch (Exception e) {
			DetailException.expDetail(e, TeamController.class);
			logger.debug(e.getMessage());
		}
		return commonModelAndView;
	}
	/**
	 * 团队变更审核信息详细
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/team/checklist/changedetail")
	public CommonModelAndView changedetail(Team team,BindingResult result,Map<String, Object> model, HttpServletRequest request) throws Exception {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, team,messageSource);
		try {
			//查询团队创建申请
			team.setCheckType(2);
			Team changeTeam=teamService.getOne(team);
			commonModelAndView.addObject("changeTeam", changeTeam);
		} catch (Exception e) {
			DetailException.expDetail(e, TeamController.class);
			logger.debug(e.getMessage());
		}
		return commonModelAndView;
	}
	/**
	 * 团队升级信息查询
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/team/checklist/promotecheck")
	public CommonModelAndView promotecheck(Team team,BindingResult result,Map<String, Object> model, HttpServletRequest request) throws Exception {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, team,messageSource);
		String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
		commonModelAndView.addObject("ftpHttpUrl",ftpHttpUrl);
		try {
			//查询团队升级申请
			team.setCheckType(5);
			Team promoteTeam=teamService.getOne(team);
			MemberOrganization memberOrganization =new MemberOrganization();
			TeamOrOrgApplication teamOrOrgApplication =new TeamOrOrgApplication();
			teamOrOrgApplication.setId(team.getId());
			TeamOrOrgApplication teamorgApplication = teamOrOrgApplicationService.getOne(teamOrOrgApplication);
			memberOrganization.setHistoryTeamID(teamorgApplication.getTeamID().intValue());
			MemberOrganization promoteOrganization =memberOrganizationService.getOneForPromote(memberOrganization);
			commonModelAndView.addObject("promoteTeam", promoteTeam);
			commonModelAndView.addObject("promoteOrganization", promoteOrganization);
			model.put("promoteTeam", promoteTeam);
			model.put("promoteOrganization", promoteOrganization);
		} catch (Exception e) {
			DetailException.expDetail(e, TeamController.class);
			logger.debug(e.getMessage());
		}
		return commonModelAndView;
	}
	/**
	 * 团队升级审核信息详细
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/team/checklist/promotedetail")
	public CommonModelAndView annualdetail(Team team,BindingResult result,Map<String, Object> model, HttpServletRequest request) throws Exception {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, team,messageSource);
		String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
		commonModelAndView.addObject("ftpHttpUrl",ftpHttpUrl);
		try {
			//查询团队升级申请
			team.setCheckType(5);
			Team promoteTeam=teamService.getOne(team);
			MemberOrganization memberOrganization =new MemberOrganization();
			TeamOrOrgApplication teamOrOrgApplication =new TeamOrOrgApplication();
			teamOrOrgApplication.setId(team.getId());
			TeamOrOrgApplication teamorgApplication = teamOrOrgApplicationService.getOne(teamOrOrgApplication);
			memberOrganization.setHistoryTeamID(teamorgApplication.getTeamID().intValue());
			MemberOrganization promoteOrganization =memberOrganizationService.getOneForPromote(memberOrganization);
			commonModelAndView.addObject("promoteTeam", promoteTeam);
			commonModelAndView.addObject("promoteOrganization", promoteOrganization);
			model.put("promoteTeam", promoteTeam);
			model.put("promoteOrganization", promoteOrganization);
		} catch (Exception e) {
			DetailException.expDetail(e, TeamController.class);
			logger.debug(e.getMessage());
		}
		return commonModelAndView;
	}
	/**
	 * 团队注销审核信息查询
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/team/checklist/cancelcheck")
	public CommonModelAndView cancelcheck(Team team,BindingResult result,Map<String, Object> model, HttpServletRequest request) throws Exception {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, team,messageSource);
		try {
			//查询团队创建申请
			team.setCheckType(4);
			Team cancelTeam=teamService.getOne(team);
			commonModelAndView.addObject("cancelTeam", cancelTeam);
			model.put("cancelTeam", cancelTeam);
		} catch (Exception e) {
			DetailException.expDetail(e, TeamController.class);
			logger.debug(e.getMessage());
		}
		return commonModelAndView;
	}
	/**
	 * 团队注销审核信息查询
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/team/checklist/canceldetail")
	public CommonModelAndView canceldetail(Team team,BindingResult result,Map<String, Object> model, HttpServletRequest request) throws Exception {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, team,messageSource);
		try {
			//查询团队创建申请
			team.setCheckType(4);
			Team cancelTeam=teamService.getOne(team);
			commonModelAndView.addObject("cancelTeam", cancelTeam);
		} catch (Exception e) {
			DetailException.expDetail(e, TeamController.class);
			logger.debug(e.getMessage());
		}
		return commonModelAndView;
	}
	/**
	 * 团队变更审核信息审核
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/team/checklist/changecheck")
	public CommonModelAndView changecheck(Team team,BindingResult result,Map<String, Object> model, HttpServletRequest request) throws Exception {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, team,messageSource);
		try {
			//查询团队变更申请
			team.setCheckType(2);
			Team changeTeam=teamService.getOne(team);
			commonModelAndView.addObject("changeTeam", changeTeam);
		} catch (Exception e) {
			DetailException.expDetail(e, TeamController.class);
			logger.debug(e.getMessage());
		}
		return commonModelAndView;
	}
	/**
	 * 团队注销
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/customer/team/cancel")
	public CommonModelAndView updateCancel(Team team,HttpServletRequest request) {
		String mark="false";
		try {
			teamService.updateCancel(team);
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
	public String downloadfile(Team entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(Team entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String uploadfile(Team entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView valid(Team entity, BindingResult result,
			Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}


	@RequestMapping("/customer/team/mobilephone.json")
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
	

	@RequestMapping("/customer/team/homephone.json")
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
	
	@RequestMapping("/customer/team/account.json")
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
	
	@RequestMapping("/customer/team/idcard.json")
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
