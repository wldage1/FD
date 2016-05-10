package com.sw.core.interceptor;

import java.util.Enumeration;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sw.core.cache.PluginsConfigCache;
import com.sw.core.cache.SystemSettingCache;
import com.sw.core.common.Constant;
import com.sw.core.controller.CommonModelAndView;
import com.sw.core.data.dao.IDao;
import com.sw.core.data.dbholder.CreatorContextHolder;
import com.sw.core.data.dbholder.DatabaseTypeContextHolder;
import com.sw.core.data.dbholder.DatasourceTypeContextHolder;
import com.sw.core.data.entity.Authorization;
import com.sw.core.data.entity.BaseLog;
import com.sw.core.data.entity.BaseUser;
import com.sw.core.data.entity.RelationEntity;
import com.sw.core.data.entity.Setting;

public class CommonInterceptor implements HandlerInterceptor {

	private static Logger logger = Logger.getLogger(CommonInterceptor.class);

	@Resource
	private IDao<BaseLog> relationDao;


	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object obj, ModelAndView modelAndView) throws Exception {
		// 设置web root
		request.getSession().setAttribute(Constant.BASE,request.getContextPath());
		// 设置系统样式
		request.getSession().setAttribute(Constant.SKIN,Constant.STYLE);
		//将WebRoot和系统样式 存放到Setting中
		Setting setting = SystemSettingCache.getCache(SystemSettingCache.SYSTEM_SETTING_KEY);
		setting.setBase(request.getContextPath()) ;
		setting.setSkin(Constant.STYLE) ;
		SystemSettingCache.putAuthCache(setting) ;
		
		// 操作代码
		String contro;
		if(modelAndView == null){
			return;
		}
		Map<String,Object> viewMap = modelAndView.getModelMap();
		if(modelAndView != null && viewMap != null){
			contro = (String) modelAndView.getModelMap().get(CommonModelAndView.STATUS_PARAM);
			if(contro != null){
				Authorization authorization = PluginsConfigCache.getAuthCache(contro);
				//验证是否需要记录日志
				if (authorization.getLogOrNot() != null 
						&& ("true".equals(authorization.getLogOrNot()) 
						|| "1".equals(authorization.getLogOrNot()))) {
					Object currentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					//验证用户是否登录
					if (!currentUser.equals("anonymousUser")){
						BaseLog baseLog = new BaseLog();
						BaseUser bu = (BaseUser) currentUser;
						if (bu.getContent() != null){
							baseLog.setUserId((Long)bu.getId());
							baseLog.setAccessIp(bu.getAccessIp());
							baseLog.setUserAccount(bu.getUsername());
							baseLog.setUserName(bu.getUsername());
							baseLog.setContent(bu.getContent());
							try {
								//写入日志
								relationDao.insert("log.insert", baseLog);
								bu.setContent(null);
							} catch (Exception e) {
								logger.debug(e.getMessage());
							}
						}
					}
				}
			}
		}
		//清除上下文中存储的数据源，数据库，操作，创建者信息
		//DatabaseOperateContextHolder.clear();
		DatasourceTypeContextHolder.clear();
		DatabaseTypeContextHolder.clear();
		CreatorContextHolder.clear();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object obj) throws Exception {
		//控制器访问信息输出，config.properties配置文件中控制是否输出
		Setting setting = SystemSettingCache.getCache(SystemSettingCache.SYSTEM_SETTING_KEY);
		if (setting.isDebug()){
			logger.debug("==>RequestURI:"+request.getRequestURI());
			Enumeration enumer = request.getParameterNames();
			while(enumer.hasMoreElements()){
				Object pname = enumer.nextElement();
				if (pname != null){
					String pvalue = request.getParameter(pname.toString());
			        logger.debug("==>Prameter:" + pname.toString() + "=" + pvalue);
				}
			}
		}	
		if (SecurityContextHolder.getContext().getAuthentication() != null){
			//判断是否登录
			Object currentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (!currentUser.equals("anonymousUser")){
				String c = request.getParameter("c");
				if (c != null && !c.equals("")){
					//设置创建用户
					RelationEntity be = (RelationEntity) currentUser;
					CreatorContextHolder.setCreatorContext(String.valueOf(be.getId()));
				}			
			}
		}
		return true;
	}
	public IDao<BaseLog> getRelationDao() {
		return relationDao;
	}
	public void setRelationDao(IDao<BaseLog> relationDao) {
		this.relationDao = relationDao;
	}
}
