package com.sw.plugins.system.role.controller;

import java.net.URLDecoder;
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

import com.sw.core.common.Constant;
import com.sw.core.controller.BaseController;
import com.sw.core.controller.CommonModelAndView;
import com.sw.core.exception.DetailException;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.plugins.system.organization.service.OrganizationService;
import com.sw.plugins.system.role.entity.Role;
import com.sw.plugins.system.role.service.RoleService;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserService;

/**
 * 角色管理控制器，负责角色的添加，修改，删除，查询等功能拦截处理
 * 
 * @author Administrator
 * 
 */
@Controller
public class RoleController extends BaseController<Role> {

	private static Logger logger = Logger.getLogger(RoleController.class);

	@Resource
	private RoleService roleService;
	@Resource
	private OrganizationService orgService;
	@Resource
	private UserService userService;
	/**
	 * 角色列表方法
	 * 
	 * @param code
	 * @param role
	 * @param request
	 * @return
	 */
	@RequestMapping("/system/role")
	public CommonModelAndView list(Role role, HttpServletRequest request)throws Exception {
		List<Organization> orgList = (List<Organization>)userService.getLoginUserSecendOrgList(new User());
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, role, messageSource);
		commonModelAndView.addObject("role", role);
		commonModelAndView.addObject("orgList",orgList);
		commonModelAndView.addObject("orgListSize", orgList.size());
		return commonModelAndView;
	}

	/**
	 * 跳转到角色创建页面
	 * 
	 * @param code
	 * @param role
	 * @param request
	 * @return
	 * @author
	 */
	@RequestMapping("/system/role/create")
	public CommonModelAndView create(Role role, BindingResult result, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, role, messageSource);
		Organization organization = new Organization();
		organization.setId(role.getOrgId());
		try {
			organization = orgService.getOne(organization);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		commonModelAndView.addObject("orgName",organization.getName());
		commonModelAndView.addObject("role", role);
		return commonModelAndView;
	}

	/**
	 * 角色创建操作
	 * 
	 * @param role
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/system/role/save", method = RequestMethod.POST)
	public CommonModelAndView save(Role role, HttpServletRequest request) {
		// 视图名
		String viewName = null;
		try {
			roleService.saveOrUpdate(role);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, role,request, messageSource);
		return commonModelAndView;
	}

	/**
	 * 跳转到角色修改页面
	 * 
	 * @param code
	 * @param role
	 * @param request
	 * @return
	 */
	@RequestMapping("/system/role/modify")
	public CommonModelAndView modify(Role role, HttpServletRequest request) {
		String code = role.getC();
		if (role.getId() != null) {
			try {
				role = (Role) roleService.getOne(role);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		role.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, role, messageSource);
		commonModelAndView.addObject("role", role);
		return commonModelAndView;
	}

	/**
	 * 跳转到角色授权页面
	 * 
	 * @param role
	 * @param request
	 * @return
	 */
	@RequestMapping("/system/role/funcauth")
	public CommonModelAndView auth(Role role, HttpServletRequest request) {
		String code = role.getC();
		Organization organization = new Organization();
		// String roleJson = null;
		String orgName= "";
		try {
			// 如果有状态记录，则获取记录状态为当前值
			role = (Role) roleService.getOne(role);
			// 获取机构名称
			organization.setId(role.getOrgId());
			organization = orgService.getOne(organization);
			if(organization != null){
				orgName = organization.getName();
			}
			// roleJson = (String)
			// roleService.serviceInvoker("getRoleAuthorization", role);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		role.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, role, messageSource);
		commonModelAndView.addObject("orgName", orgName);
		commonModelAndView.addObject("role", role);
		// commonModelAndView.addObject("json", roleJson);
		return commonModelAndView;
	}

	/**
	 * 获取树形json数据
	 * 
	 * @param role
	 * @param request
	 * @return
	 */
	@RequestMapping("/system/role/authtree")
	public CommonModelAndView jsonTree(Role role, HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			Object obj = roleService.getAuthTreeList(role);
			if (obj instanceof List) {
				map = new HashMap<String, Object>();
				map.put("treeJsonData", obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 角色权限保存
	 * 
	 * @param role
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/system/role/saveauth", method = RequestMethod.POST)
	public CommonModelAndView saveAuth(@Valid Role role, BindingResult result,HttpServletRequest request ,Map<String, Object> model) {
		// 视图名
		String viewName = null;
		try {
			roleService.saveAuth(role);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, role,request, messageSource);
		return commonModelAndView;
	}

	/**
	 * 角色删除功能，json格式
	 * 
	 * @param role
	 * @param request
	 * @return
	 */
	@RequestMapping("/system/role/delete")
	public CommonModelAndView delete(Role role, HttpServletRequest request) {
		// 视图名
		String viewName = this.ERROR;
		try {
			String name = URLDecoder.decode(role.getName(), Constant.ENCODING);
			role.setName(name);
			if (role != null && role.getId() != null) {
				roleService.delete(role);
				viewName = this.SUCCESS;
			} else if (role != null && role.getIds() != null) {
				roleService.deleteByArr(role);
				viewName = this.SUCCESS;
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView(viewName, role, request,messageSource);
	}

	/**
	 * 查询角色信息 返回json格式
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/system/role/grid")
	public CommonModelAndView json(Role role, HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			Object obj = roleService.getGrid(role);
			map = obj == null ? null : (Map<String, Object>) obj;
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, role, request);
	}

	@Override
	public String uploadfile(Role entity, HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	@Override
	public String downloadfile(Role entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(Role entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@RequestMapping("/system/role/valid")
	public CommonModelAndView valid(@Valid Role entity, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		String operate = (entity.getOperate() == null || entity.getOperate().equals("")) ? "0" : entity.getOperate();
		int operateTemp = Integer.parseInt(operate);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		try {
			switch (operateTemp) {
			case 1: {
				Role tRole = new Role();
				tRole.setOrgId(entity.getOrgId());
				tRole.setName(entity.getName());
				Long count = roleService.getRecordCount(tRole);
				if (entity.getOrgId() == null) {
					result.rejectValue("orgName", "NotEmpty");
				}
				if (count == 1) {
					result.rejectValue("name", "Duplicate");
				}
				if (result.hasErrors()) {
					commonModelAndView.addBindingErrors(model, result, messageSource);
					return commonModelAndView;
				}
				break;
			}
			case 2: {
				Role tRole = new Role();
				tRole.setOrgId(entity.getOrgId());
				tRole.setName(entity.getName());
				Long count = roleService.getRecordCount(tRole);
				if (count == 1) {
					Role dRole = roleService.getOne(tRole);
					if (dRole != null && !dRole.getId().equals(entity.getId())) {
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
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return commonModelAndView;
	}
}
