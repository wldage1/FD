package com.sw.plugins.system.authorization.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sw.core.cache.PluginsConfigCache;
import com.sw.core.common.Constant;
import com.sw.core.controller.BaseController;
import com.sw.core.controller.CommonModelAndView;
import com.sw.core.data.entity.Authorization;
import com.sw.core.data.entity.RelationEntity;
import com.sw.core.exception.DetailException;
import com.sw.plugins.system.authorization.service.AuthorizationService;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserService;

@Controller
public class AuthorizationController extends BaseController<Authorization> {

	private static Logger logger = Logger.getLogger(AuthorizationController.class);

	@Resource
	private AuthorizationService authorizationService;
	@Resource
	private UserService userService;
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/redirector")
	public CommonModelAndView redirector(Authorization authorization, HttpServletRequest request, Map<String, Object> model) {
		String viewName = "redirect:";
		try {
			String code = authorization.getC();
			if (code != null) {
				Authorization pauth = PluginsConfigCache.getAuthCache(code);
				if (pauth != null) {
					viewName += pauth.getController();
					// 是否还原参数
					String back = authorization.getBack();
					if ("1".equals(back)) {
						Map<String, String> pm = (Map<String, String>) request.getSession().getAttribute("_turn_" + code);
						if (pm != null) {
							model.putAll(pm);
							request.getSession().removeAttribute("_turn_" + code);
						}
					}else{
						model.putAll(request.getParameterMap());
					}
					model.put("c", code);
					model.put("params", authorization.getParams());
				}
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView(viewName, model);
	}

	/**
	 * 左侧菜单
	 * 
	 * @return
	 */
	@RequestMapping("/left")
	public CommonModelAndView left(String c, Authorization authorization, HttpServletRequest request) {
		CommonModelAndView CommonModelAndView = new CommonModelAndView(super.LEFT);
		try {
			authorization.setSetAuthorityOrNot("true");
			authorization.setTreeLevel("1");
			// 查询权限信息
			List<Authorization> pmsTreeLevelList1 = (List<Authorization>) authorizationService.getList(authorization);
			authorization.setSetAuthorityOrNot("true");
			authorization.setTreeLevel("2");
			// 查询权限信息
			List<Authorization> pmsTreeLevelList2 = (List<Authorization>) authorizationService.getList(authorization);
			// 二级菜单放入SESSION
			StringBuffer mustr = new StringBuffer();
			for (Authorization auth : pmsTreeLevelList2) {
				mustr.append(auth.getCode());
				mustr.append(",");
			}
			request.getSession().setAttribute(LEFT, mustr.toString());
			CommonModelAndView.addObject("pmsLevelList1", pmsTreeLevelList1);
			CommonModelAndView.addObject("pmsLevelList2", pmsTreeLevelList2);
			CommonModelAndView.addObject("code", c);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		// 加登录验证
		return CommonModelAndView;
	}

	/**
	 * 进入主页面
	 * 
	 * @return
	 */
	/**
	 * 方法简要说明
	 * 
	 * @author yuanfu Created on :2013-5-23 下午03:19:00
	 */
	@RequestMapping("/main")
	public CommonModelAndView main(Authorization authorization, HttpServletRequest request, HttpServletResponse response) {
		CommonModelAndView commonModelAndView = null;
		// 获取当前用户信息
		Object currentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!currentUser.equals("anonymousUser")) {
			// 查询权限信息（系统）
			List<?> pmsTreeLevelList1 = null;
			List<?> ucmList = null;
			try {
				if (currentUser instanceof User) {
					User tempUser = (User) currentUser;
			/*		if (tempUser.getPosition() != null && tempUser.getPosition().indexOf(Constant.POSITION_TSR) > -1) {
						// 获取坐席中心功能缓存对象
						Authorization authorizationCache = PluginsConfigCache.getAuthCache("9");
						String viewName = "redirect:";
						if (authorizationCache != null) {
							viewName += authorizationCache.getController() + "?c=9";
						} else {
							viewName += "/seatcenter?c=9";
						}
						// 如果当前用户只拥有一个业务种类，则直接跳转
						commonModelAndView = new CommonModelAndView(viewName);
					} else {*/
						commonModelAndView = new CommonModelAndView(super.MAIN);
						// 设置level为1，代表系统级别
						authorization.setTreeLevel("1");
						pmsTreeLevelList1 = (List<?>) authorizationService.getList(authorization);
						// 查询权限信息
						if (currentUser instanceof User) {
							String style = ((User) currentUser).getStyle();
							String accessIp = request.getRemoteAddr();
							((User) currentUser).setAccessIp(accessIp);
							if (style == null || style.equals("")) {
								style = Constant.STYLE;
							}
							commonModelAndView.addObject("user", tempUser.getUsername());
							commonModelAndView.addObject("style", style);
						}
						commonModelAndView.addObject("ucmList", ucmList);
						commonModelAndView.addObject("pmsTreeLevelList1", pmsTreeLevelList1);
					//}
						if(tempUser!=null && (tempUser.getIsFirstLogin()==null || !tempUser.getIsFirstLogin().equals((short)1))){
							User user = (User) userService.getOne(tempUser);
							if(user!=null){
								if(user.getIsFirstLogin()==null || !user.getIsFirstLogin().equals((short)1)){
									commonModelAndView.addObject("isFirstLogin", true);
								}
							}
						}
				}
			} catch (Exception e) {
				logger.error(DetailException.expDetail(e, getClass()));
			}
			// 设置应用绝对路径
			Constant.APPLICATIONPATH = request.getSession().getServletContext().getRealPath("/").replace("\\", "/");
		}
		// 如果为空跳转到首页
		if (commonModelAndView == null) {
			commonModelAndView = new CommonModelAndView(super.INDEX);
		}
		return commonModelAndView;
	}

	@RequestMapping("/main2")
	public CommonModelAndView main2(Authorization authorization, HttpServletRequest request) {
		// 设置level为0，代表系统级别
		authorization.setTreeLevel("0");
		// 查询权限信息（系统）
		List<?> pmslist = null;
		try {
			pmslist = (List<?>) authorizationService.getList(authorization);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		// 加登录验证
		return new CommonModelAndView("main2", "pmslist", pmslist);
	}

	/**
	 * 通过业务代码获得该权限所对应的控制器，并跳转到该控制器
	 * 
	 * @param code
	 *            业务代码
	 * @param request
	 * @return
	 * @author
	 */
	@RequestMapping("/content")
	public CommonModelAndView content(String c, HttpServletRequest request) {
		String viewName = "redirect:";
		boolean isFirst=false;
		try {
			if (c != null) {
				User tempUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				if(tempUser!=null && (tempUser.getIsFirstLogin()==null || !tempUser.getIsFirstLogin().equals((short)1))){
					User user = (User) userService.getOne(tempUser);
					if(user!=null){
						if(user.getIsFirstLogin()==null || !user.getIsFirstLogin().equals((short)1)){
							c="805";
							isFirst=true;
						}
					}
				}
				Authorization currAuthorization = PluginsConfigCache.getAuthCache(c);
				String controllerStr = currAuthorization.getController();
				int indx = controllerStr.indexOf("?");
				if (indx > -1) {
					viewName += controllerStr + "&c=" + c;
				} else {
					viewName += controllerStr + "?c=" + c;
				}
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		ModelMap model=new ModelMap();
		if(isFirst){
			model.put("isFirstLogin",0);
		}
		
		CommonModelAndView CommonModelAndView = new CommonModelAndView(viewName,model);
		
		return CommonModelAndView;
	}

	/**
	 * 进入欢迎页面
	 * 
	 * @return
	 */
	@RequestMapping("/welcome")
	public CommonModelAndView welcome(RelationEntity baseEntity, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(this.WELCOME);
		try {
			Authorization authorization = new Authorization();
			authorization.setSetAuthorityOrNot("true");
			authorization.setTreeLevel("1");
			// 读取一级权限信息
			List<?> authlist1 = (List<?>) authorizationService.getList(authorization);
			commonModelAndView.addObject("authlist1", authlist1);
			// 读取二级权限信息
			authorization.setTreeLevel("2");
			List<?> authlist2 = (List<?>) authorizationService.getList(authorization);
			commonModelAndView.addObject("authlist2", authlist2);
			// 读取三级权限信息
			authorization.setTreeLevel("3");
			List<?> authlist3 = (List<?>) authorizationService.getList(authorization);
			commonModelAndView.addObject("authlist3", authlist3);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}

		// 浏览器版本
		String bversion = request.getHeader("User-Agent");
		commonModelAndView.addObject("bversion", bversion);
		ServletContext context = request.getSession().getServletContext();

		// servlet版本信息
		String serverInfo = context.getServerInfo();
		commonModelAndView.addObject("serverInfo", serverInfo);
		// servlet版本
		int majorVersion = context.getMajorVersion();
		int minorVersion = context.getMinorVersion();
		String servletVersion = new StringBuffer().append(majorVersion).append('.').append(minorVersion).toString();
		commonModelAndView.addObject("servletVersion", servletVersion);
		// 虚拟机名称
		String vmName = "";
		// 虚拟机提供商
		String vmVendor = "";
		// 虚拟机版本
		String vmVersion = "";
		// 虚拟机版本
		String runtimeName = "";
		// 虚拟机版本
		String runtimeVersion = "";
		// 操作系统名称
		String osName = "";
		// 操作系统版本
		String osVersion = "";
		// cup
		String cpu = "";
		try {
			vmName = System.getProperty("java.vm.name", "");
			vmVendor = System.getProperty("java.vm.vendor", "");
			vmVersion = System.getProperty("java.vm.version", "");
			runtimeName = System.getProperty("java.runtime.name", "");
			runtimeVersion = System.getProperty("java.runtime.version", "");
			osName = System.getProperty("os.name", "");
			osVersion = System.getProperty("os.version", "");
			cpu = System.getProperty("sun.cpu.isalist", "");
		} catch (Exception ex) {
		}
		commonModelAndView.addObject("vmName", vmName);
		commonModelAndView.addObject("vmVendor", vmVendor);
		commonModelAndView.addObject("vmVersion", vmVersion);
		commonModelAndView.addObject("osName", osName);
		commonModelAndView.addObject("runtimeName", runtimeName);
		commonModelAndView.addObject("runtimeVersion", runtimeVersion);
		commonModelAndView.addObject("osVersion", osVersion);
		commonModelAndView.addObject("cpu", cpu);
		Runtime runtime = Runtime.getRuntime();
		long totalMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();
		long totalKB = totalMemory / 1024;
		long freeKB = freeMemory / 1024;
		commonModelAndView.addObject("totalkb", totalKB);
		commonModelAndView.addObject("freekb", freeKB);
		return commonModelAndView;
	}

	@RequestMapping("/system")
	public CommonModelAndView system(RelationEntity baseEntity, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(baseEntity);
		String view = (String) commonModelAndView.getCurrentStatus(baseEntity, request);
		return new CommonModelAndView(view);
	}

	@Override
	public String uploadfile(Authorization entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String downloadfile(Authorization entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(Authorization entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView valid(Authorization entity, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}