package com.sw.plugins.customer.memberquitorg.controller;

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
import com.sw.plugins.customer.member.entity.Member;
import com.sw.plugins.customer.member.entity.MemberApplication;
import com.sw.plugins.customer.member.service.MemberApplicationService;

/**
 * 理财师控制器，查询，审核，注销，删除等功能
 * 
 * @author Administrator
 */
@Controller
public class MemberQuitOrgController extends BaseController<Member> {

	private static Logger logger = Logger.getLogger(MemberQuitOrgController.class);
	@Resource
	private MemberApplicationService memberApplicationService;

	/**
	 * 获取申请退出机构会员列表
	 * 
	 * @param ma
	 * @param request
	 * @return
	 */
	@RequestMapping("/customer/memberquitorg")
	public CommonModelAndView listMemberQuit(Member member, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, member, messageSource);
		commonModelAndView.addObject("c", member.getC());
		return commonModelAndView;
	}

	/**
	 * 获取申请退出机构会员
	 * 
	 * @param ma
	 * @param request
	 * @return
	 */
	@RequestMapping("/customer/memberquitorg/grid")
	public CommonModelAndView memberQuitjson(MemberApplication ma, HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			map = memberApplicationService.getApQuitMemberGrid(ma);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 确认退出机构或团队
	 * 
	 * @param ma
	 * @param request
	 * @return
	 */
	@RequestMapping("/customer/memberquitorg/quit")
	public CommonModelAndView updateMemberQuitStatus(MemberApplication ma, HttpServletRequest request) {
		String viewName = null;
		try {
			memberApplicationService.updateApQuitMemberStatus(ma);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, ma, request, messageSource);
		return commonModelAndView;
	}

	@Override
	public String uploadfile(Member entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String downloadfile(Member entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(Member entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView valid(Member entity, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
