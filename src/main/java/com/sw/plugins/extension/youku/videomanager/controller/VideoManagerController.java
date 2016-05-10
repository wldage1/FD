package com.sw.plugins.extension.youku.videomanager.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.sw.plugins.config.globalparameter.entity.GlobalParameter;
import com.sw.plugins.config.globalparameter.service.GlobalParameterService;
import com.sw.plugins.extension.youku.category.entity.Category;
import com.sw.plugins.extension.youku.category.service.CategoryService;
import com.sw.plugins.extension.youku.videomanager.entity.VideoBasic;
import com.sw.plugins.extension.youku.videomanager.service.VideoBasicService;
import com.sw.plugins.product.manage.entity.Product;
import com.sw.plugins.product.manage.service.ProductService;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;

@Controller
public class VideoManagerController extends BaseController<VideoBasic> {

	private static Logger logger = Logger.getLogger(VideoManagerController.class);

	@Resource
	private VideoBasicService videoService;

	@Resource
	private CategoryService categoryService;

	@Resource
	private GlobalParameterService globalService;
	
	@Resource
	private UserLoginService userLoginService;
	
	@Resource
	private ProductService productService;

	@RequestMapping("/extension/youku_videomanager")
	public CommonModelAndView list(Category entity, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, entity, messageSource);
		List<Category> list = null;
		GlobalParameter gp = new GlobalParameter();
		String freshToken = "";
		String accessToken = "";
		try {
			list = categoryService.getList(new Category());
			gp.setCode("freshToken");
			gp = globalService.getSysOne(gp);
			if(gp != null){
				freshToken = gp.getValue();
			}
			gp.setCode("accessToken");
			gp = globalService.getSysOne(gp);
			if(gp != null){
				accessToken = gp.getValue();
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		commonModelAndView.addObject("categoryList", list);
		commonModelAndView.addObject("c", entity.getC());
		commonModelAndView.addObject("categoryListSize", list.size());
		commonModelAndView.addObject("freshToken", freshToken);
		commonModelAndView.addObject("accessToken", accessToken);
		return commonModelAndView;
	}

	@RequestMapping("/extension/youku_upload")
	public CommonModelAndView upload(VideoBasic videoBasic, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, videoBasic, messageSource);
		List<Category> list = null;
		GlobalParameter gp = new GlobalParameter();
		String password = "";
		String accessToken = "";
		try {
			list = categoryService.getList(new Category());
			gp.setCode("youkuPassword");
			gp = globalService.getSysOne(gp);
			if(gp != null){
				password = gp.getValue();
			}
			gp.setCode("accessToken");
			gp = globalService.getSysOne(gp);
			if(gp != null){
				accessToken = gp.getValue();
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		commonModelAndView.addObject("c", videoBasic.getC());
		commonModelAndView.addObject("categoryList", list);
		commonModelAndView.addObject("password", password);
		commonModelAndView.addObject("accessToken", accessToken);
		return commonModelAndView;
	}

	@RequestMapping("/extension/youku_videomanager/grid")
	public CommonModelAndView json(VideoBasic videoBasic, HttpServletRequest request) {
		videoBasic.setOrgId(userLoginService.getCurrLoginUser().getSelfOrg().getId());
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = videoService.getGrid(videoBasic);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, videoBasic, request);
	}

	@RequestMapping("/extension/youku_videomanager/save")
	public CommonModelAndView save(VideoBasic videoBasic, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if(videoBasic.getId() == null){
				videoBasic.setPublicType(request.getParameter("public_type"));
				videoBasic.setCopyrightType(request.getParameter("copyright_type"));
				User user = userLoginService.getCurrLoginUser();
				Organization org = user.getSelfOrg();
				if(org!=null){
					if(org.getIsCommission()==1){
						videoBasic.setOrgId(org.getId());
						videoService.save(videoBasic);
					}
				}
			}else{
				videoService.update(videoBasic);
			}
			map.put(this.STATUS, this.STATUS_SUCCESS);
		} catch (Exception e) {
			map.put(this.STATUS, this.STATUS_FALSE);
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}

	@RequestMapping("/extension/youku_videomanager/delete")
	public CommonModelAndView delete(VideoBasic videoBasic, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			videoService.delete(videoBasic);
			map.put(this.STATUS, this.STATUS_SUCCESS);
		} catch (Exception e) {
			map.put(this.STATUS, this.STATUS_FALSE);
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}

	@RequestMapping("/extension/youku_videomanager/updateStatus")
	public CommonModelAndView updateStatus(VideoBasic videoBasic, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			videoService.update(videoBasic);
			map.put(this.STATUS, this.STATUS_SUCCESS);
		} catch (Exception e) {
			map.put(this.STATUS, this.STATUS_FALSE);
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}

	@RequestMapping("/extension/youku_videomanager/detail")
	public CommonModelAndView detail(VideoBasic videoBasic, HttpServletRequest request) {
		String c = videoBasic.getC();
		try {
			videoBasic = videoService.getOne(videoBasic);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		videoBasic.setC(c);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, videoBasic, messageSource);
		commonModelAndView.addObject("videoBasic", videoBasic);
		return commonModelAndView;
	}

	@RequestMapping("/extension/youku_upload/upload_file")
	public void upload(VideoBasic entity, HttpServletRequest request, HttpServletResponse response) {
		try {
			String filename = videoService.upload(entity, request);
			response.getWriter().print("{\"url\":\"" + filename + "\"}");
			response.getWriter().close();
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
	}
	
	@RequestMapping("/extension/youku_videomanager/freshToken")
	public CommonModelAndView freshToken(GlobalParameter gp, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(this.STATUS, this.STATUS_FALSE);
		try {
			String code = gp.getCode();
			if(code != null){
				String codes[] = code.split(",");
				String value = gp.getValue();
				String values[] = value.split(",");
				for(int i=0;i<codes.length;i++){
					gp.setCode(codes[i]);
					gp.setValue(values[i]);
					globalService.updateByCode(gp);
				}
			}
			map.put(this.STATUS, this.STATUS_SUCCESS);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
	
	@RequestMapping("/extension/youku_upload/product")
	public CommonModelAndView productList(Product product, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String,Object>();
		List<Product> prdList = null;
		try {
			User user = userLoginService.getCurrLoginUser();
			Organization organization = user.getSelfOrg();
			if (organization != null) {
				int isCommission = organization.getIsCommission();
				if (isCommission == 1) {
					product.setOrgId(organization.getId());
					//产品限制条件 1计划投放 ，2在售，6成立且在售 已发布
					prdList = productService.getUploadProductList(product);
				} 
			}
			if(prdList == null){
				prdList = new ArrayList<Product>();
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		map.put("prdList", prdList);
		return new CommonModelAndView("jsonView", map, product, request);
	}
	

	@Override
	public String downloadfile(VideoBasic entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(VideoBasic entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView valid(VideoBasic entity, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String uploadfile(VideoBasic entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
