package com.sw.plugins.system.organization.controller;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sw.core.common.Constant;
import com.sw.core.controller.BaseController;
import com.sw.core.controller.CommonModelAndView;
import com.sw.core.exception.DetailException;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.plugins.system.organization.service.OrganizationService;
import com.sw.plugins.system.user.entity.User;

/**
 * 机构管理控制器，负责机构的添加，修改，删除，查询等功能拦截处理
 * 
 * @author Administrator
 * 
 */
@Controller
public class OrganizationController extends BaseController<Organization> {

	private static Logger logger = Logger.getLogger(OrganizationController.class);

	@Resource
	private OrganizationService organizationService;

	/**
	 * 跳转到机构列表页面
	 * 
	 * @param code
	 * @param organization
	 * @param request
	 * @return
	 */
	@RequestMapping("/system/organization")
	public CommonModelAndView list(Organization organization, HttpServletRequest request) {
		User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Organization userOrg = currentUser.getSelfOrg();
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, organization, messageSource);
		commonModelAndView.addObject("c", organization.getC());
		commonModelAndView.addObject("root", userOrg == null ? 0 : userOrg.getParentId());
		return commonModelAndView;
	}

	/**
	 * 机构创建操作
	 * 
	 * @param organization
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/system/organization/save", method = RequestMethod.POST)
	public CommonModelAndView save(Organization organization,HttpServletRequest request) {
		// 视图名
		String viewName = null;
		try {
			organizationService.saveOrUpdate(organization);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			organization.setErrorMsg(e.getMessage());
			viewName = this.ERROR;
			logger.error(DetailException.expDetail(e, getClass()));
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, organization,request, messageSource);
		return commonModelAndView;
	}

	/**
	 * 跳转到机构创建页面
	 * 
	 * @param code
	 * @param organization
	 * @param request
	 * @return
	 * @author
	 */
	@RequestMapping("/system/organization/create")
	public CommonModelAndView create(Organization organization, BindingResult result, HttpServletRequest request) {

		CommonModelAndView commonModelAndView = new CommonModelAndView(request, organization, messageSource);
		commonModelAndView.addObject("organization", organization);
		commonModelAndView.addObject("c", organization.getC());
		return commonModelAndView;
	}

	/**
	 * 跳转到机构修改页面
	 * 
	 * @param code
	 * @param organization
	 * @param request
	 * @return
	 */
	@RequestMapping("/system/organization/modify")
	public CommonModelAndView modify(Organization organization, HttpServletRequest request) {
		String code = organization.getC();
		if (organization.getId() != null) {
			try {
				organization = organizationService.getOne(organization);
			} catch (Exception e) {
				logger.debug(e.getMessage());
			}
		}
		organization.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, organization, messageSource);
		commonModelAndView.addObject("organization", organization);
		return commonModelAndView;
	}

	/**
	 * 机构删除功能，json格式
	 * 
	 * @param organization
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/system/organization/delete")
	public CommonModelAndView delete(Organization organization, HttpServletRequest request) {
		// 视图名
		String viewName = null;
		try {
			String name = URLDecoder.decode(organization.getName(), Constant.ENCODING);
			organization.setName(name);
			Long obj = (Long) organizationService.checkConstraints(organization);
			if (obj > 0) {
				Map map = new HashMap();
				map.put(Constant.STATUS, "ERROR");
				return new CommonModelAndView("jsonView", map);
			} else {
				organizationService.delete(organization);
				viewName = this.SUCCESS;
			}
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, organization,request, messageSource);
		return commonModelAndView;
	}

	/**
	 * 机构列表数据
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/system/organization/grid")
	public CommonModelAndView json(String nodeid, HttpServletRequest request) {
		Organization organization = new Organization();
		if (nodeid != null) {
			organization.setNodeid(nodeid);
		}
		Object obj = null;
		try {
			obj = organizationService.getGrid(organization);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		Map<String, Object> map = (obj == null ? null : (Map<String, Object>) obj);
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 获取创建机构下拉树数据
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/system/organization/stree")
	public CommonModelAndView stree(String id, HttpServletRequest request) {
		Organization organization = new Organization();
		Object obj = null;
		if (id != null) {
			organization.setParentId(Long.valueOf(id));
		}
		try {
			obj = organizationService.getSelectTree(organization);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		Map<String, Object> map = (obj == null ? null : (Map<String, Object>) obj);
		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 获取(数据权限)下拉树数据
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping("/system/organization/sourcetree")
	public CommonModelAndView sourceStree(String id, HttpServletRequest request) {
		Organization organization = new Organization();
		Map<String, Object> map = null;
		if (id != null) {
			organization.setParentId(Long.valueOf(id));
		}
		try {
			map = organizationService.getUserOwnsOrgTreeMap(organization);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 获取(数据权限)二级机构下拉树数据
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping("/system/organization/secendleveltree")
	public CommonModelAndView secendLevelOrgStree(Organization organization, HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			map = organizationService.getSecendLevelOrgTreeMap(organization);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}

	@Override
	@RequestMapping("/system/organization/uploadfile")
	public String uploadfile(Organization organization, HttpServletRequest request, HttpServletResponse response) {
		String returnStr = null;
		try {
			returnStr = (String) organizationService.upload(organization, request);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return returnStr;
	}

	@Override
	public String downloadfile(Organization entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(Organization entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@RequestMapping("/system/organization/valid")
	public CommonModelAndView valid(@Valid Organization entity, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		String operate = (entity.getOperate() == null || entity.getOperate().equals("")) ? "0" : entity.getOperate();
		int operateTemp = Integer.parseInt(operate);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		try{
			switch(operateTemp){
			    case 1:{
					// 验证机构代码是否唯一
					if (!entity.getCode().isEmpty()) {
						Organization ccOrg = new Organization();
						ccOrg.setId(entity.getId());
						ccOrg.setCode(entity.getCode());
						Long coCode = (Long) organizationService.getCountForOrgDuplication(ccOrg);
						if (coCode != null && coCode != 0) {
							result.rejectValue("code", "Duplicate");
						}
					}
					// 验证同一机构下的名称是否唯一
					if (!entity.getName().isEmpty()) {
						Organization cnOrg = new Organization();
						cnOrg.setId(entity.getId());
						if(entity.getParentId()==null){
							cnOrg.setParentId(0l);
						}else{
							cnOrg.setParentId(entity.getParentId());
						}
						cnOrg.setName(entity.getName());
						Long coName = (Long) organizationService.getCountForOrgDuplication(cnOrg);
						if (coName != null && coName != 0) {
							result.rejectValue("name", "Duplicate");
						}
					}
					if (result.hasErrors()) {
						commonModelAndView.addBindingErrors(model, result, messageSource);
						return commonModelAndView;
					}
			    	break;
			    }
			    case 2:{
			    	// 修改时验证，父机构不能为自己
					if (entity.getId() != null) {
						entity.setId((Long) entity.getId());
						Long id = (Long) entity.getId();
						Long pid = entity.getParentId();
						if ((id).equals(pid)) {
							result.rejectValue("parentName", "NotSelf");
						}
					}
					// 验证机构代码是否唯一
					if (!entity.getCode().isEmpty()) {
						Organization ccOrg = new Organization();
						ccOrg.setId(entity.getId());
						ccOrg.setCode(entity.getCode());
						Long coCode = (Long) organizationService.getCountForOrgDuplication(ccOrg);
						if (coCode != null && coCode != 0) {
							result.rejectValue("code", "Duplicate");
						}
					}
					// 验证同一机构下的名称是否唯一
					if (!entity.getName().isEmpty()) {
						Organization cnOrg = new Organization();
						cnOrg.setId(entity.getId());
						if(entity.getParentId()==null){
							cnOrg.setParentId(0l);
						}else{
							cnOrg.setParentId(entity.getParentId());
						}
						cnOrg.setName(entity.getName());
						Long coName = (Long) organizationService.getCountForOrgDuplication(cnOrg);
						if (coName != null && coName != 0) {
							result.rejectValue("name", "Duplicate");
						}
					}
					if (result.hasErrors()) {
						commonModelAndView.addBindingErrors(model, result, messageSource);
						return commonModelAndView;
					}
			    	break;
			    }
			}
		}catch(Exception e){
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return commonModelAndView;
	}

}
