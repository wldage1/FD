package com.sw.core.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.sw.core.cache.PluginsConfigCache;
import com.sw.core.common.Constant;
import com.sw.core.data.entity.Authorization;
import com.sw.core.data.entity.BaseUser;
import com.sw.core.data.entity.RelationEntity;
import com.sw.core.exception.DetailException;
import com.sw.plugins.system.user.entity.User;

public class CommonModelAndView extends ModelAndView {

	/** 提示信息常量 */
	public static String PROMPT_MESSAGE = "promptMessage";
	/** 是否显示导航 */
	public final String NAVIGATEORNOT = "navigateOrNot";
	/** 导航信息变量 */
	public static String NAVIGATION = "navigation";
	/** 系统帮助信息 */
	public static String HELP_INFO = "helpInfo";
	/** 子全新列表变量 */
	public static String SUB_PERMISSION_LIST = "subPermissionList";
	/** 系统状态参数存储变量 */
	public static String STATUS_PARAM = "statusParam";
	/** 页面跳转参数 */
	public static String TURN_CODE = "turnCode";
	public static String COVER_PARAM = "coverParam";
	public static String TURN_LINK = "turnLink";
	public static String BACK_CODE = "backCode";
	public static String BACK_LINK = "backLink";
	/** 系统跳转状态 */
	public static String STATUS_DERECTER = "directer";
	/** 提示信息 */
	private String promptMessage;
	/** 提示状态 */
	private String status;

	public CommonModelAndView() {
		super();
	}

	public CommonModelAndView(String viewName) {
		super(viewName);
	}

	public CommonModelAndView(View view) {
		super(view);
	}

	public CommonModelAndView(String viewName, Map<String, ?> model) {
		super(viewName, model);
	}

	public CommonModelAndView(View view, Map<String, ?> model) {
		super(view, model);
	}

	public CommonModelAndView(String viewName, String modelName, Object modelObject) {
		super(viewName, modelName, modelObject);
	}

	public CommonModelAndView(View view, String modelName, Object modelObject) {
		super(view, modelName, modelObject);
	}

	// 异步请求，记录当前状态
	public CommonModelAndView(String viewName, Map<String, ?> model, RelationEntity entity, HttpServletRequest request) {
		super(viewName, model);
		// 记录当前操作状态，列表默认都记录
		Authorization authorization = PluginsConfigCache.getAuthCache(entity.getC());
		// 是否记录当前状态
		if (authorization.getSetStatusOrNot() != null && "true".equals(authorization.getSetStatusOrNot())) {
			this.addCurrentStatus(entity, request);
		}
	}

	/**
	 * 此构造封装方法： 1、配置视图(获取 "*plugin-config.xml" 里的 "controller" 配置)；
	 * 2、是否加载导航条(根据"*plugin.config.xml" 里 配置的"isNav")； 3、是否添加子权限信息和子权限信息列表到上下文中；
	 * 4、设置上下文控制器url，在目标页面中可以获取到，并将当时请求中的参数记录到session中，状态回退时能模拟当前状态；
	 * 
	 * @param request
	 * @param entity
	 */
	public CommonModelAndView(HttpServletRequest request, RelationEntity entity, DelegatingMessageSource messageSource) {
		// 视图
		Authorization authorization = PluginsConfigCache.getAuthCache(entity.getC());
		String controller = authorization.getController();
		String pageName = authorization.getPageName();
		if (pageName == null) {
			super.setViewName(controller);
		} else {
			super.setViewName(controller + "/" + pageName);
		}
		// 是否加载导航
		if (authorization.getNavigateOrNot() != null && "true".equals(authorization.getNavigateOrNot())) {
			this.addNavigation(entity, request);
			this.addObject(NAVIGATEORNOT, 0);
		} else {
			// 不显示导航
			this.addObject(NAVIGATEORNOT, 1);
		}
		// 是否加载帮助信息
		if (authorization.getHelpOrNot() != null && "true".equals(authorization.getHelpOrNot())) {
			this.addHelpInfo(messageSource, entity);
		}

		// 加载页面权限信息
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (obj instanceof User) {
			User user = (User) obj;
			this.addAllObjects(user.getUserAuthorization());
		}

		// 创建返回链接并保存当前参数
		generateBackLink(entity, request);

		this.addObject(CommonModelAndView.STATUS_PARAM, entity.getC());
	}

	public CommonModelAndView(RelationEntity entity) {
		// 视图
		Authorization authorization = PluginsConfigCache.getAuthCache(entity.getC());
		String controller = authorization.getController();
		String pageName = authorization.getPageName();
		if (pageName == null) {
			super.setViewName(controller);
		} else {
			super.setViewName(controller + "/" + pageName);
		}
	}

	/**
	 * 此构造封装方法： 1、配置视图(接受控制层指定的viewName)；
	 * 2、设置并替换提示信息(提交页面配置的"prompt"字段名来替换相应的提示信息)； 3、记录操作状态。
	 * 
	 * @param request
	 * @param entity
	 */
	@SuppressWarnings("unchecked")
	public CommonModelAndView(String viewName, RelationEntity entity, HttpServletRequest request, DelegatingMessageSource messageSource) {
		// 读取登录用户信息
		try {
			Object currentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			this.addObject(CommonModelAndView.STATUS_PARAM, entity.getC());
			this.addObject(CommonModelAndView.TURN_CODE, entity.getTurnCode());
			this.addObject(CommonModelAndView.COVER_PARAM, entity.getCoverParam());
			this.setViewName(viewName);
			Map<String, Object> map = new Hashtable<String, Object>();
			String tempViewName = "success";
			int indx = viewName.indexOf("redirect:/");
			if (indx >= 0) {
				tempViewName = viewName.replace("redirect:/", "");
			}
			map.put(Constant.STATUS, tempViewName);
			this.addAllObjects(map);

			// 设置并替换提示信息
			String replaceMsgs[] = null;
			Map<String, String[]> pMap = request.getParameterMap();
			String prompt = entity.getPrompt();
			if (prompt != null) {
				String promptField[] = prompt.split(",");
				replaceMsgs = new String[promptField.length];
				for (int i= 0;i<promptField.length;i++) {
					String field = promptField[i];
					String[] filed = pMap.get(field);
					if(filed !=  null){
						replaceMsgs[i] = filed[0];
					}
				}
				this.addPromptMessage(messageSource, entity, replaceMsgs);
			}
			if (!currentUser.equals("anonymousUser")) {
				BaseUser bu = (BaseUser) currentUser;
				// 设置操作日志内容
				bu.setContent(this.getPromptMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成页面导航条
	 * 
	 * @param code
	 * @param request
	 */
	public void addNavigation(RelationEntity entity, HttpServletRequest request) {
		// 生成导航信息
		Authorization currAuthorization = PluginsConfigCache.getAuthCache(entity.getC());
		String path = "";
		if (currAuthorization != null) {
			path = currAuthorization.getRelationPath();
		}
		String codeArr[] = path.split(",");
		String webroot = request.getContextPath();
		StringBuffer navigation = new StringBuffer();
		int codeLength = codeArr.length;
		int ilastCode = codeArr.length - 1;
		for (int i = 0; i < codeLength; i++) {
			if (codeArr[i].length() == 0) {
				continue;
			}
			Authorization authorization = PluginsConfigCache.getAuthCache(codeArr[i]);

			if (authorization == null) {
				continue;
			}

			//String controller = authorization.getController();
			String name = authorization.getName();
			String code = authorization.getCode();
			if(authorization.getNavigateOrNot() != null && authorization.getNavigateOrNot().equals("false") ){
				continue;
			}
			String treeLevel = authorization.getTreeLevel();
			// level 1
			if (("1").equals(treeLevel)) {
				navigation.append(" : ");
				navigation.append(name);
				continue;
			}
			// level 2
			if ("2".equals(treeLevel)) {
				if (i == codeLength - 1) {
					navigation.append(" &gt; ");
					navigation.append(name);
				} else {
					navigation.append(" &gt; <a href='");
					navigation.append(webroot);
					navigation.append("/redirector?c=");
					navigation.append(code);
					navigation.append("&back=1'>");
					navigation.append(name);
					navigation.append("</a>");
				}
				continue;
			}
			// level N
			if (!"1".equals(treeLevel) || !"2".equals(treeLevel)) {
				// last end
				if (i == ilastCode) {
					navigation.append(" &gt; ");
					navigation.append(name);
				} else {

					navigation.append(" &gt; <a href='");
					navigation.append(webroot);
					navigation.append("/redirector?c=");
					navigation.append(code);
					navigation.append("&back=1'>");
					navigation.append(name);
					navigation.append("</a>");
				}
			}
		}
		request.getSession().setAttribute(NAVIGATION, navigation);
		navigation = null;
	}

	/**
	 * 将提示信息添加到上下文中
	 * 
	 * @param messageSource
	 * @param baseEntity
	 * @param replaceMsg
	 */
	public void addPromptMessage(DelegatingMessageSource messageSource, RelationEntity baseEntity, String replaceMsg[]) {
		String pmsg = this.getPromptMessage(messageSource, baseEntity, replaceMsg);
		this.setPromptMessage(pmsg);
		// 设置提示停息
		try {
			pmsg = URLEncoder.encode(pmsg, Constant.ENCODING);
		} catch (UnsupportedEncodingException e) {
			DetailException.expDetail(e, CommonModelAndView.class);
		}
		this.addObject(CommonModelAndView.PROMPT_MESSAGE, pmsg);
	}

	/**
	 * 根据属性文件key获取信息，并存放到上下文中
	 * 
	 * @param messageSource
	 * @param msgKey
	 * @param replaceMsg
	 */
	public void addPromptMessage(DelegatingMessageSource messageSource, String msgKey, String replaceMsg[]) {
		String pmsg = this.getPromptMessage(messageSource, msgKey, replaceMsg);
		// 设置提示停息
		try {
			pmsg = URLEncoder.encode(pmsg, Constant.ENCODING);
			this.setPromptMessage(pmsg);
		} catch (UnsupportedEncodingException e) {
			DetailException.expDetail(e, CommonModelAndView.class);
		}
		this.addObject(CommonModelAndView.PROMPT_MESSAGE, pmsg);
	}

	/**
	 * 
	 * @param messageSource
	 * @param baseEntity
	 */
	public void addHelpInfo(DelegatingMessageSource messageSource, RelationEntity baseEntity) {
		String pmsg = null;
		try {
			String clsname = RelationEntity.class.getName();
			if (baseEntity != null) {
				clsname = baseEntity.getClass().getName();
			}
			if (clsname != null) {
				clsname = clsname.toLowerCase();
			}
			String msdKey = clsname + "." + baseEntity.getC() + "." + "help";
			pmsg = this.getPromptMessage(messageSource, msdKey, null);
		} catch (Exception e) {
		}
		this.addObject(CommonModelAndView.HELP_INFO, pmsg);
	}

	/**
	 * 获取资源文件信息
	 * 
	 * @return
	 */
	public String getPromptMessage() {
		return promptMessage;
	}

	public void setPromptMessage(String promptMessage) {
		this.promptMessage = promptMessage;
	}

	/**
	 * 获取提示信息
	 * 
	 * @param status
	 * @param baseEntity
	 * @param messageSource
	 * @param replaceMsg
	 * @return
	 */
	public String getPromptMessage(DelegatingMessageSource messageSource, RelationEntity baseEntity, String replaceMsg[]) {
		String clsname = RelationEntity.class.getName();
		if (baseEntity != null) {
			clsname = baseEntity.getClass().getName();
		}
		if (clsname != null) {
			clsname = clsname.toLowerCase();
		}
		String viewName = this.getViewName();
		int indx = viewName.indexOf("redirect:/");
		if (indx >= 0) {
			viewName = viewName.replace("redirect:/", "");
		}
		if (baseEntity.getErrorMsg() != null) {
			if (baseEntity.getErrorMsg().toLowerCase().indexOf("duplicate") > -1 || baseEntity.getErrorMsg().toLowerCase().indexOf("unique key") > -1 || baseEntity.getErrorMsg().toLowerCase().indexOf("unique constraint") > -1) {
				viewName = "multiple";
			}
		}
		return messageSource.getMessage(clsname + "." + baseEntity.getC() + "." + viewName, replaceMsg, Locale.CHINA);
	}

	/**
	 * 直接根据属性文件key值获取信息
	 * 
	 * @param messageSource
	 * @param msgKey
	 * @param replaceMsg
	 * @return
	 */
	public String getPromptMessage(DelegatingMessageSource messageSource, String msgKey, String replaceMsg[]) {
		return messageSource.getMessage(msgKey, replaceMsg, Locale.CHINA);
	}

	/**
	 * 创建返回链接
	 * 
	 * @param baseEntity
	 * @param request
	 */
	@SuppressWarnings({ "static-access", "unchecked" })
	public void generateBackLink(RelationEntity baseEntity, HttpServletRequest request) {
		String code = baseEntity.getC();

		// 设置返回链接
		StringBuffer coc = new StringBuffer();
		Authorization auth = PluginsConfigCache.getAuthCache(code);
		String parentCode = auth.getParentCode();
		if (parentCode != null) {
			coc.append("/redirector?c=").append(parentCode).append("&back=1");
			request.getSession().setAttribute(this.BACK_LINK, coc.toString());
		}

		Map<String, String> linkParam = new HashMap<String, String>();
		// 记录操作轨迹
		Enumeration<String> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();
			String value = request.getParameter(key);
			linkParam.put(key, value);
		}
		linkParam.remove("c");
		if (linkParam.size() > 0) {
			request.getSession().setAttribute("_turn_" + code, linkParam);
		}
	}

	/**
	 * 设置上下文控制器url，在目标页面中可以获取到， 并将当时请求中的参数记录到session中，状态回退时能模拟当前状态
	 * 
	 * @param code
	 * @param baseEntity
	 */
	@SuppressWarnings("unchecked")
	public void addCurrentStatus(RelationEntity baseEntity, HttpServletRequest request) {
		String code = baseEntity.getC();
		String back = baseEntity.getBack();
		if (back == null || ("0").equals(back)) {
			Map<String,String[]> params = new HashMap<String,String[]>();
			params.putAll(request.getParameterMap());
			request.getSession().setAttribute("_turn_" + code, params);
		}
	}

	/**
	 * 获取记录的状态信息
	 * 
	 * @param baseEntity
	 * @param request
	 * @return
	 */
	public Object getCurrentStatus(RelationEntity baseEntity, HttpServletRequest request) {
		String code = baseEntity.getC();
		String back = baseEntity.getBack();
		Object obj = null;
		if (("1").equals(back)) {
			obj = request.getSession().getAttribute("_turn_" + code);
		}
		request.getSession().removeAttribute("_turn_" + code);
		return obj;
	}


	public void addBindingErrors(Map<String, Object> errors, BindingResult bindingResult, DelegatingMessageSource messageSource) {
		List<Map<String, String>> errorList = new ArrayList<Map<String, String>>();

		// 设置资源文件国际化提示
		for (FieldError error : bindingResult.getFieldErrors()) {
			Map<String, String> errorMap = new HashMap<String, String>();
			String key = error.getField();
			StringBuffer msgs = new StringBuffer();
			List<FieldError> fieldErrors = bindingResult.getFieldErrors(key);
			for (int i = 0; i < fieldErrors.size(); i++) {
				String[] codes = fieldErrors.get(i).getCodes();
				String code0 = codes[0];
				msgs.append(messageSource.getMessage(code0, error.getArguments(), Locale.CHINA));
				msgs.append("  ");
			}
			String msg = msgs.substring(0, msgs.length() - 1);
			errorMap.put("field", key);
			errorMap.put("msg", msg);
			errorList.add(errorMap);
		}

		// 自定义验证信息
		for (ObjectError error : bindingResult.getGlobalErrors()) {
			Map<String, String> errorMap = new HashMap<String, String>();
			String code = error.getCode();
			String msg = error.getDefaultMessage();
			errorMap.put("field", code);
			errorMap.put("msg", msg);
			errorList.add(errorMap);
		}

		errors.put("result", errorList);
		errorList = null;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
