package com.sw.core.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sw.core.common.Constant;
import com.sw.core.data.entity.BaseEntity;
import com.sw.core.exception.DetailException;

/**
 * 后台Action类 - 管理中心基类
 */

@SuppressWarnings("rawtypes")
@Controller
public class CommonController extends BaseController {
	/** 操作成功返回,非从定向url */
	public String SUCCESS = "success";
	/** 错误页面,非从定向url */
	public String ERROR = "error";

	@RequestMapping("/")
	public CommonModelAndView defaultpage(HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView();
		commonModelAndView.setViewName("redirect:/" + this.INDEX);
		return commonModelAndView;
	}

	/**
	 * 进入首页
	 * 
	 * @return
	 */
	@RequestMapping("/index")
	public CommonModelAndView index(BaseEntity baseEntity, HttpServletRequest request) {
		return new CommonModelAndView(this.INDEX);
	}

	@RequestMapping("/success")
	public CommonModelAndView success(String promptMessage, String statusParam, HttpServletRequest request) {
		// 编码转换
		try {
			if (promptMessage != null) {
				promptMessage = URLDecoder.decode(promptMessage, Constant.ENCODING);
			}
		} catch (UnsupportedEncodingException e) {
			DetailException.expDetail(e, CommonController.class);
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(this.SUCCESS);
		// 设置提示停息
		if (promptMessage != null) {
			commonModelAndView.addObject(CommonModelAndView.PROMPT_MESSAGE, promptMessage);
		}
		if (statusParam != null) {
			commonModelAndView.addObject(CommonModelAndView.STATUS_PARAM, statusParam);
		}

		// 是二级菜单不设置跳转项
		String firstMenu = (String) request.getSession().getAttribute(LEFT);
		if (firstMenu != null) {
			if (firstMenu.contains(statusParam)) {
				commonModelAndView.addObject(CommonModelAndView.STATUS_DERECTER, "true");
			}
		}

		String coverParam = (String) request.getParameter(CommonModelAndView.COVER_PARAM);
		// 设置返回链接
		StringBuffer coc = new StringBuffer();
		coc.append("/redirector?c=").append(statusParam);
		if (coverParam != null) {
			coc.append("&back=");
			coc.append(1);
		}
		request.getSession().setAttribute(CommonModelAndView.BACK_LINK, coc.toString());

		// 设置跳转链接
		String turnCode = (String) request.getParameter(CommonModelAndView.TURN_CODE);
		if (turnCode != null) {
			coc = new StringBuffer();
			coc.append("/redirector?c=");
			coc.append(turnCode);
			if (coverParam != null) {
				coc.append("&back=");
				coc.append(coverParam);
			}
			commonModelAndView.addObject(CommonModelAndView.TURN_LINK, coc.toString());
		}

		return commonModelAndView;
	}

	@RequestMapping("/error")
	public CommonModelAndView error(String promptMessage, String statusParam, HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("base", request.getContextPath());
		CommonModelAndView commonModelAndView = new CommonModelAndView(this.ERROR);
		// 设置提示停息
		if (promptMessage != null) {
			// 编码转换
			try {
				promptMessage = URLDecoder.decode(promptMessage, Constant.ENCODING);
			} catch (UnsupportedEncodingException e) {
				DetailException.expDetail(e, CommonController.class);
			}
			commonModelAndView.addObject(CommonModelAndView.PROMPT_MESSAGE, promptMessage);
		}
		if (statusParam != null) {
			commonModelAndView.addObject(CommonModelAndView.STATUS_PARAM, statusParam);
		}
		// 是二级菜单不设置跳转项
		String firstMenu = (String) request.getSession().getAttribute(LEFT);
		if (firstMenu != null) {
			if (firstMenu.contains(statusParam)) {
				commonModelAndView.addObject(CommonModelAndView.STATUS_DERECTER, "true");
			}
		}
		return commonModelAndView;
	}

	/**
	 * 进入上传页面
	 * 
	 * @param baseEntity
	 * @param request
	 * @return
	 */
	@RequestMapping("/upload")
	public CommonModelAndView upload(String iframeId, String controllerName, String fileTypeDesc, String fileTypeExts, String queueSizeLimit, String uploadLimit, String inputIds, String imgIds, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(this.UPLOAD);
		commonModelAndView.addObject("controllerName", controllerName);
		commonModelAndView.addObject("iframeId", iframeId);
		commonModelAndView.addObject("fileTypeDesc", fileTypeDesc);
		commonModelAndView.addObject("fileTypeExts", fileTypeExts);
		commonModelAndView.addObject("queueSizeLimit", queueSizeLimit);
		commonModelAndView.addObject("uploadLimit", uploadLimit);
		commonModelAndView.addObject("inputIds", inputIds);
		commonModelAndView.addObject("imgIds", imgIds);
		return commonModelAndView;
	}

	/**
	 * 登录验证码校验
	 * 
	 * @return
	 */
	@RequestMapping("/loginVerify")
	public CommonModelAndView loginVerify(String error) {
		CommonModelAndView commonModelAndView = new CommonModelAndView();
		if (StringUtils.endsWithIgnoreCase(error, "captcha")) {
			commonModelAndView.setViewName(super.ERROR);
			commonModelAndView.addPromptMessage(messageSource, "user.captcha.error", null);
			return commonModelAndView;
		} else {
			commonModelAndView.setViewName("redirect:/" + this.MAIN);
		}
		return commonModelAndView;
	}

	/**
	 * 用户注销
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout() {
		return "redirect:/" + super.EXIT;
	}

	/**
	 * 系统退出拦截
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/exit")
	public String exit() {
		SecurityContextHolder.clearContext();
		return super.EXIT;
	}

	@Override
	public String uploadfile(BaseEntity baseEntity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String downloadfile(BaseEntity entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(BaseEntity entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView valid(BaseEntity entity, BindingResult result, Map model, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
}