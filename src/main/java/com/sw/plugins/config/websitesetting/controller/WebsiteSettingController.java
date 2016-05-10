package com.sw.plugins.config.websitesetting.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.HtmlUtils;

import com.sw.core.controller.BaseController;
import com.sw.core.controller.CommonModelAndView;
import com.sw.core.exception.DetailException;
import com.sw.plugins.config.websitesetting.entity.WebsiteSetting;
import com.sw.plugins.config.websitesetting.service.WebsiteSettingService;
import com.sw.plugins.storage.entity.GridFile;
import com.sw.util.FTPUtil;
import com.sw.util.SystemProperty;

@Controller
public class WebsiteSettingController extends BaseController<WebsiteSetting>{
	private static Logger logger = Logger.getLogger(WebsiteSettingController.class);
	
	@Resource
	private WebsiteSettingService websiteSettingService;
	
	@RequestMapping("/config/websitesetting")
	public CommonModelAndView list(WebsiteSetting website,HttpServletRequest request,Map<String,Object> model) throws Exception{
		WebsiteSetting logo = new WebsiteSetting();
		logo.setCode("Logo");
		logo = websiteSettingService.getOne(logo);
		WebsiteSetting logo2 = new WebsiteSetting();
		logo2.setCode("Logo2");
		logo2 = websiteSettingService.getOne(logo2);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request,website,messageSource);
		commonModelAndView.addObject("c",website.getC());
		commonModelAndView.addObject("ftpUrl", SystemProperty.getInstance("config").getProperty("ftp.http.url"));
		commonModelAndView.addObject("logo",logo);
		commonModelAndView.addObject("Logo2", logo2);
		//添加前台所操作属性id
		WebsiteSetting status = new WebsiteSetting();
		status.setCode("Status");
		status = websiteSettingService.getOne(status);
		commonModelAndView.addObject("status_id", status.getId());
		WebsiteSetting smtpPwd = new WebsiteSetting();
		smtpPwd.setCode("SMTPPassword");
		smtpPwd = websiteSettingService.getOne(smtpPwd);
		commonModelAndView.addObject("smtpPwd_id", smtpPwd.getId());
		WebsiteSetting aboutUs = new WebsiteSetting();
		aboutUs.setCode("AboutUs");
		aboutUs = websiteSettingService.getOne(aboutUs);
		commonModelAndView.addObject("aboutUs_id", aboutUs.getId());
		WebsiteSetting contactUs = new WebsiteSetting();
		contactUs.setCode("ContactUs");
		contactUs = websiteSettingService.getOne(contactUs);
		commonModelAndView.addObject("contactUs_id", contactUs.getId());
		WebsiteSetting declare = new WebsiteSetting();
		declare.setCode("Declare");
		declare = websiteSettingService.getOne(declare);
		commonModelAndView.addObject("declare_id", declare.getId());
		WebsiteSetting pmrs = new WebsiteSetting();
		pmrs.setCode("ParameterMemberRegisterState");
		pmrs = websiteSettingService.getOne(pmrs);
		commonModelAndView.addObject("pmrs_id", pmrs.getId());
		return commonModelAndView;
	}
	
	/**
	 * 查询网站基本信息方法
	 * @param website
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/config/websitesetting/grid")
	public CommonModelAndView json(WebsiteSetting website,HttpServletRequest request,Map<String,Object> model){
		Map<String,Object> map = null;
		try {
			map = websiteSettingService.getGrid(website);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView",map,website,request);
		return commonModelAndView;
	}
	
	/**
	 * 查询SMTP服务器基本信息方法
	 * @param website
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/config/websitesetting/SMTPgrid")
	public CommonModelAndView SMTPjson(WebsiteSetting website,HttpServletRequest request,Map<String,Object> model){
		Map<String,Object> map = null;
		try {
			map = websiteSettingService.getSMTPGrid(website);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView",map,website,request);
		return commonModelAndView;
	}
	
	/**
	 * 查询网站前台信息方法
	 * @param website
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/config/websitesetting/webInfogrid")
	public CommonModelAndView webInfojson(WebsiteSetting website,HttpServletRequest request,Map<String,Object> model){
		Map<String,Object> map = null;
		try {
			map = websiteSettingService.getWebInfoGrid(website);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView",map,website,request);
		return commonModelAndView;
	}
	
	@RequestMapping("/config/websitesetting/webLogogrid")
	public CommonModelAndView webLogojson(WebsiteSetting website,HttpServletRequest request,Map<String,Object> model){
		Map<String,Object> map = null;
		try {
			map = websiteSettingService.getWebLogoGrid(website);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView",map,website,request);
		return commonModelAndView;
	}
	
	
	@RequestMapping("/config/websitesetting/save")
	public CommonModelAndView save(WebsiteSetting website,HttpServletRequest request,Map<String,Object> model){
		String viewName = null;
		website.setValue(HtmlUtils.htmlEscape(website.getValue()));
		if(website.getId()!=null){
			try {
				websiteSettingService.update(website);
				viewName = this.SUCCESS;
			} catch (Exception e) {
				viewName = this.ERROR;
				logger.error(DetailException.expDetail(e, getClass()));
			}
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName,website, request,messageSource);
		return commonModelAndView;
	}
	
	/**
	 * 跳转网站信息修改页面
	 * @param website
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/config/websitesetting/webModify")
	public CommonModelAndView modify(WebsiteSetting website,HttpServletRequest request,Map<String,Object> model){
		String code = website.getC();
		if(website.getId()!=null){
			try {
				website = websiteSettingService.getOne(website);
			} catch (Exception e) {
				logger.error(DetailException.expDetail(e, getClass()));
			}
		}
		website.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request,website,messageSource);
		commonModelAndView.addObject("c",code);
		commonModelAndView.addObject("website",website);
		return commonModelAndView;
	}
	
	/**
	 * 跳转网站logo修改页面
	 * @param website
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/config/websitesetting/logoModify")
	public CommonModelAndView logoModify(WebsiteSetting website,HttpServletRequest request,Map<String,Object> model){
		String code = website.getC();
		if(website.getId()!=null){
			try {
				website = websiteSettingService.getOne(website);
			} catch (Exception e) {
				logger.error(DetailException.expDetail(e, getClass()));
			}
		}
		website.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request,website,messageSource);
		commonModelAndView.addObject("c",code);
		commonModelAndView.addObject("website",website);
		commonModelAndView.addObject("ftpUrl", SystemProperty.getInstance("config").getProperty("ftp.http.url"));
		return commonModelAndView;
	}
	
	/**
	 * 网站Logo信息保存
	 * @param website
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/config/websitesetting/saveLogo")
	public CommonModelAndView saveLogo(WebsiteSetting website,HttpServletRequest request,Map<String,Object> model){
		String viewName = null;
		try {
			websiteSettingService.saveLogo(website);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
			viewName = this.ERROR;
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName,website,request,messageSource);
		return commonModelAndView;
	}
	
	@Override
	@RequestMapping("/config/websitesetting/uploadfile")
	public String uploadfile(WebsiteSetting entity, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			response.getWriter().print(websiteSettingService.uploadfile(entity, request));
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
	@RequestMapping(value = "/config/websitesetting/kindeditor/uploadimg")
	public String kindEditorUpload(GridFile entity, HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		String tempFileName = request.getParameter("localUrl");
		String fileExtensionName = tempFileName.substring(tempFileName.lastIndexOf("."));
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", ".gif,.jpg,.jpeg,.png,.bmp");
		if (!Arrays.<String> asList(extMap.get("image").split(",")).contains(fileExtensionName)) {
			try {
				response.getWriter().println("上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get("image") + "格式。");
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String date = format.format(new Date());
		String path = SystemProperty.getInstance("config").getProperty("website.setting.image.path") + date;
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

	@Override
	public String downloadfile(WebsiteSetting entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(WebsiteSetting entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView valid(WebsiteSetting entity,
			BindingResult result, Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
