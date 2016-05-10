package com.sw.plugins.websitemanage.information.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.HtmlUtils;

import com.sw.core.controller.BaseController;
import com.sw.core.controller.CommonModelAndView;
import com.sw.plugins.websitemanage.information.entity.Information;
import com.sw.plugins.websitemanage.information.service.InformationService;
import com.sw.plugins.product.manage.entity.ProductInformation;
import com.sw.plugins.storage.entity.GridFile;
import com.sw.util.FTPUtil;
import com.sw.util.SystemProperty;

/**
 * 资讯管理控制器
 * 
 * @author runchao
 */
@Controller
public class InformationController extends BaseController<Information> {

	private static Logger logger = Logger.getLogger(InformationController.class);
	
	@Resource
	private InformationService informationService;
	
	/**
	 * 资讯列表方法
	 * 
	 * @param information
	 * @param request
	 * @return
	 */
	@RequestMapping("/websitemanage/information")
	public CommonModelAndView list(Information information, HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, information, messageSource);
		commonModelAndView.addObject("information", information);
		Map<String, Object> isOrNot = this.initData.getInformationReleased();
		commonModelAndView.addObject("isOrNot", isOrNot);
		List<Map<String, Object>> informationType = initData.getInformationType();
		commonModelAndView.addObject("informationType", informationType);
		return commonModelAndView;
	}
	
	/**
	 * 资讯类别列表，返回json
	 * 
	 * @param information
	 * @param request
	 * @return
	 */
	@RequestMapping("/websitemanage/type/grid")
	public CommonModelAndView type(Information information, HttpServletRequest request){
		Map<String, Object> map = null;
		try {
			map = informationService.getTypeGrid(information);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new CommonModelAndView("jsonView", map, information, request);
	}
	
	/**
	 * 资讯列表，返回json
	 * 
	 * @param information
	 * @param request
	 * @return
	 */
	@RequestMapping("/websitemanage/information/grid")
	public CommonModelAndView json(Information information, HttpServletRequest request){
		Map<String, Object> map = null;
		try {
			map = informationService.getGrid(information);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new CommonModelAndView("jsonView", map, information, request);
	}
	
	/**
	 * 资讯标题图片上传
	 * 
	 * @param information
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	@RequestMapping("/websitemanage/information/uploadfile")
	public String uploadfile(Information information, HttpServletRequest request, HttpServletResponse response) {
		try {
			response.getWriter().print(informationService.upload(information, request));
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return ERROR;
	}
	
	@RequestMapping("/websitemanage/information/deleteTitleImage")
	public void deleteTitleImage(Information information){
		try {
			informationService.deleteTitleImage(information);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 资讯创建操作
	 * 
	 * @param information
	 * @param request
	 * @return
	 */
	@RequestMapping("/websitemanage/information/create")
	public CommonModelAndView create(Information information, HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, information, messageSource);
		List<Map<String, Object>> informationType = initData.getInformationType();
		commonModelAndView.addObject("informationType", informationType);
		commonModelAndView.addObject("information", information);
		String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
		commonModelAndView.addObject("ftpHttpUrl",ftpHttpUrl);
		return commonModelAndView;
	}
	
	/**
	 * 资讯修改操作
	 * 
	 * @param information
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/websitemanage/information/modify")
	public CommonModelAndView modify(Information information, HttpServletRequest request, Map<String,Object> model){
		String code = information.getC();
		try {
			information = informationService.getOne(information);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		information.setC(code);
		information.setContent(HtmlUtils.htmlUnescape(information.getContent()));
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, information, messageSource);
		List<Map<String, Object>> informationType = initData.getInformationType();
		commonModelAndView.addObject("informationType", informationType);
		commonModelAndView.addObject("information", information);
		model.put("information", information);
		String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
		commonModelAndView.addObject("ftpHttpUrl",ftpHttpUrl);
		return commonModelAndView;
	}
	
	/**
	 * 资讯保存操作
	 * 
	 * @param information
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/websitemanage/information/save", method = RequestMethod.POST)
	public CommonModelAndView save(Information information, HttpServletRequest request) {
		String viewName = null;
		String code = information.getC();
		try {
			if(information.getId() == null){
				informationService.save(information);
			}else{
				informationService.update(information);
			}
			viewName = this.SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			viewName = this.ERROR;
		}
		information.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, information, request, messageSource);
		return commonModelAndView;
	}
	
	/**
	 * 资讯查看操作
	 * 
	 * @param information
	 * @param request
	 * @return
	 */
	@RequestMapping("/websitemanage/information/detail")
	public CommonModelAndView detail(Information information, HttpServletRequest request){
		String code = information.getC();
		try {
			information = informationService.getOne(information);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		information.setC(code);
		information.setContent(HtmlUtils.htmlUnescape(information.getContent()));
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, information, messageSource);
		List<Map<String, Object>> informationType = initData.getInformationType();
		commonModelAndView.addObject("informationType", informationType);
		commonModelAndView.addObject("information", information);
		String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
		commonModelAndView.addObject("ftpHttpUrl",ftpHttpUrl);
		return commonModelAndView;
	}
	
	/**
	 * 资讯关联产品操作
	 * 
	 * @param information
	 * @param request
	 * @return
	 */
	@RequestMapping("/websitemanage/information/relate")
	public CommonModelAndView relate(Information information, HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, information, messageSource);
		commonModelAndView.addObject("information", information);
		Map<String, Object> productType = this.initData.getProductType();
		commonModelAndView.addObject("productType", productType);
		ProductInformation productInformation = new ProductInformation();
		try {
			productInformation = informationService.selectRelate(information);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		commonModelAndView.addObject("productInformation", productInformation);
		return commonModelAndView;
	}
	
	/**
	 * 产品列表，返回json
	 * 
	 * @param information
	 * @param request
	 * @return
	 */
	@RequestMapping("/websitemanage/information/product/grid")
	public CommonModelAndView product(Information information, HttpServletRequest request){
		Map<String, Object> map = null;
		try {
			map = informationService.getProductGrid(information);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new CommonModelAndView("jsonView", map, information, request);
	}
	
	/**
	 * 设置资讯关联产品操作
	 * 
	 * @param information
	 * @param request
	 * @return
	 */
	@RequestMapping("/websitemanage/information/setrelate")
	public CommonModelAndView setRelate(Information information, HttpServletRequest request) {
		String viewName = null;
		try {
			informationService.setRelate(information);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, information, request, messageSource);
		return commonModelAndView;
	}
	
	/**
	 * 资讯删除操作
	 * 
	 * @param information
	 * @param request
	 * @return
	 */
	@RequestMapping("/websitemanage/information/delete")
	public CommonModelAndView delete(Information information, HttpServletRequest request) {
		String viewName = null;
		try {
			if(information.getIds() != null){
					informationService.deleteByArr(information);
			}else if(information.getId() != null){
				informationService.delete(information);
			}
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, information, request, messageSource);
		return commonModelAndView;
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
	@RequestMapping(value = "/websitemanage/information/kindeditor/uploadimg")
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
		String path = SystemProperty.getInstance("config").getProperty("information.image.path") + date;
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
	public String downloadfile(Information entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(Information entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 气泡验证
	 */
	@Override
	@RequestMapping("/websitemanage/information/valid")
	public CommonModelAndView valid(@Valid Information information, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		try {
			if (informationService.checkForTitle(information) != 0){
				result.rejectValue("title", "already");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		if (result.hasErrors()) {
			commonModelAndView.addBindingErrors(model, result, messageSource);
			return commonModelAndView;
		}
		return commonModelAndView;
	}
}