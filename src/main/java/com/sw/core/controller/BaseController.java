package com.sw.core.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import com.sw.core.data.entity.BaseEntity;
import com.sw.core.initialize.InitData;

/**
 * 控制器基类
 */
@Controller  
public abstract class BaseController<T extends BaseEntity>{
	
	/**操作跳转*/
	public String REDIRECT = "redirect:/";
	/**操作成功返回*/
	public String SUCCESS = "redirect:/success";
    /**错误页面*/
	public String ERROR = "redirect:/error";
    /**失败页面*/
	public String FAIL = "fail";	
	/**首页跳转*/
	public String INDEX = "index";  
	/**系统设置*/
	public String SETTING = "setting"; 
    /**主操作页面跳转*/
	public String MAIN = "main";
    /**主操作页面左侧菜单*/
	public String LEFT = "left";    
    /**欢迎页面*/
	public String WELCOME = "welcome";   
	/**下载页面*/
	public String UPLOAD = "upload";   
    /**退出系统*/
	public String LOGOUT = "logout"; 
    /**退出系统*/
	public String EXIT = "exit"; 
	/**json返回状态*/
	public String STATUS = "status";
	/**json返回状态 成功*/
	public String STATUS_SUCCESS = "success";
	/**json返回状态 失败*/
	public String STATUS_FALSE = "false";
	/**json返回字段*/
	public String FIELD = "field";
	/**json返回信息*/
	public String MESSAGE = "message";
    @Resource
	public DelegatingMessageSource messageSource;
    @Resource
    public InitData initData;
    
    /**
     * 上传文件
     * @param baseEntity
     * @param request
     * @return
     */
    public abstract String uploadfile(T entity,HttpServletRequest request,HttpServletResponse response);
    
    /**
     * 下载文件
     * @param entity
     * @param request
     * @param response
     * @return
     */
    public abstract String downloadfile(T entity,HttpServletRequest request,HttpServletResponse response);
    
    /**
     * 获取各个功能的标准json数据，提供给所有可能调用的功能界面
     * 调用方式采用AJAX方式
     * @param entity
     * @param request
     * @param response
     * @return
     */
    public abstract CommonModelAndView functionJsonData(T entity,HttpServletRequest request,HttpServletResponse response);

    /**
     * 系统标准验证抽象方法
     * 页面验证逻辑都在此方法中实现，并通过AJAX异步方式进行验证
     * @param entity
     * @param result
     * @param model
     * @param request
     * @param response
     * @return
     */
    public abstract CommonModelAndView valid(T entity, BindingResult result, Map<String, Object> model, HttpServletRequest request,HttpServletResponse response);
}