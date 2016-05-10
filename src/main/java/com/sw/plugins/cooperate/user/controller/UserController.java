package com.sw.plugins.cooperate.user.controller;

import java.net.URLDecoder;
import java.util.ArrayList;
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
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;
import com.sw.plugins.system.user.service.UserService;
import com.sw.util.CommonUtil;

/**
 * 用户控制器，进行用户信息的添加，修改，删除，查询等拦截控制
 * 
 * @author Administrator
 * 
 */
@Controller("comUserController")
public class UserController extends BaseController<User> {

	private static Logger logger = Logger.getLogger(UserController.class);

	@Resource
	private UserService userService;
	@Resource
	private OrganizationService organizationService;
	@Resource
	private UserLoginService userLoginService;

	/**
	 * 用户列表方法
	 * 
	 * @param code
	 * @param role
	 * @param request
	 * @return
	 */
	@RequestMapping("/cooperate/user")
	public CommonModelAndView list(User user, HttpServletRequest request) throws Exception {
		com.sw.plugins.system.user.entity.User pUser = userLoginService.getCurrLoginUser();
		User u = new User();
		u.setIsCommission(1);
		u.setOrgId(pUser.getOrgId());
		u.setSelfOrg(pUser.getSelfOrg());
		String orgJson = (String) userService.getUserOrgAndSubOrgTreeJson(u);
		if (user.getName() != null)
			user.setName(user.getName().trim());
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, user, messageSource);
		commonModelAndView.addObject("code", user.getC());
		commonModelAndView.addObject("user", user);
		commonModelAndView.addObject("json", orgJson);
		return commonModelAndView;
	}

	/**
	 * 跳转到用户创建页面
	 * 
	 * @param code
	 * @param role
	 * @param request
	 * @return
	 * @author
	 */
	@RequestMapping("/cooperate/user/create")
	public CommonModelAndView create(User user, BindingResult result, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, user, messageSource);
		String orgTree = null;
		Map<String, Object> positionMap = null;
		Organization organization = new Organization();
		organization.setId(user.getOrgId());
		try {
			// 加载机构树
//			user.setIsCommission(1);
//			orgTree = (String) userService.getUserOrgAndSubOrgTreeJson(user);
			organization = organizationService.getOne(organization);

		} catch (Exception e) {
			logger.error(e);
		}
		commonModelAndView.addObject("orgName",organization.getName());
//		commonModelAndView.addObject("positionMap", positionMap);
//		commonModelAndView.addObject("orgList", orgTree);
		return commonModelAndView;
	}

	/**
	 * 跳转到用户修改页面
	 * 
	 * @param code
	 * @param role
	 * @param request
	 * @return
	 */
	@RequestMapping("/cooperate/user/modify")
	public CommonModelAndView modify(User user, HttpServletRequest request) {
		String code = user.getC();
		Organization organization = new Organization();
		Map<String, Object> positionMap = null;
		String bindJson = null;
		String orgName = "";
		if (user.getId() != null) {
			try {
				user = userService.getOne(user);
				// 获取机构名称
				organization.setId(user.getOrgId());
				organization.setIsCommission(1);
				organization = organizationService.getOne(organization);
				if (organization != null) {
					orgName = organization.getName();
				}
				// 获取用户绑定的角色
				user.setIsCommission(1);
				bindJson = userService.getUserRolesTreeJson(user);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		user.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, user, messageSource);
		commonModelAndView.addObject("user", user);
		commonModelAndView.addObject("positionMap", positionMap);
		commonModelAndView.addObject("roleList", bindJson);
		if (organization != null) {
			commonModelAndView.addObject("orgName", orgName);
		}
		return commonModelAndView;
	}

	/**
	 * 用户创建操作
	 * 
	 * @param role
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cooperate/user/save", method = RequestMethod.POST)
	public CommonModelAndView save(User user, HttpServletRequest request) {
		// 视图名
		String viewName = null;
		try {
			userService.saveOrUpdate(user);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
			viewName = this.ERROR;
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, user, request, messageSource);
		return commonModelAndView;
	}

	/**
	 * 坐席页面用户设置更新个人信息
	 * 
	 * */
	@RequestMapping(value = "/cooperate/user/update", method = RequestMethod.POST)
	public CommonModelAndView update(User user, HttpServletRequest request) {
		String viewName = null;
		try {
			userService.update(user);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
			viewName = this.ERROR;
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, user, request, messageSource);
		;
		return commonModelAndView;
	}

	/**
	 * 强制修改密码跳转
	 * 
	 * @param user
	 * @param request
	 * @return
	 */
	@RequestMapping("/cooperate/user/cpwd")
	public CommonModelAndView cpwd(User user, HttpServletRequest request) {
		String code = user.getC();
		if (user.getId() != null) {
			try {
				user = userService.getOne(user);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		user.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, user, messageSource);
		commonModelAndView.addObject("user", user);
		return commonModelAndView;
	}

	/**
	 * 强制修改密码保存
	 * 
	 * @param user
	 * @param request
	 * @return
	 */
	@RequestMapping("/cooperate/user/cmpwdSave")
	public CommonModelAndView cmpwdSave(@Valid User user, BindingResult result, Map<String, Object> model, HttpServletRequest request) {
		// 视图名
		String viewName = null;
		try {
			User tempUser = new User();
			tempUser = userService.getOne(user);
			if (tempUser != null && tempUser.getName() != null) {
				user.setName(tempUser.getName());
			}
			userService.save(user);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
			viewName = this.ERROR;
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, user, request, messageSource);
		;
		return commonModelAndView;
	}

	/**
	 * 查询用户信息 返回json格式
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/cooperate/user/grid")
	public CommonModelAndView json(User user, HttpServletRequest request) {
		user.setName(CommonUtil.convertSearchSign(user.getName()));
		Object gridJson;
		Map<String, Object> map = null;
		try {
			user.setIsCommission(1);
			gridJson = userService.getGrid(user);
			map = (gridJson == null ? null : (Map<String, Object>) gridJson);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, user, request);
	}

	/**
	 * 用户删除功能，json格式
	 * 
	 * @param role
	 * @param request
	 * @return
	 */
	@RequestMapping("/cooperate/user/delete")
	public CommonModelAndView delete(User user, HttpServletRequest request, Map<String, Object> model) {
		// 视图名
		String viewName = this.ERROR;
		try {
			String name = URLDecoder.decode(user.getName(), Constant.ENCODING);
			user.setName(name);
			if (user != null && user.getId() != null) {
				userService.delete(user);
				viewName = this.SUCCESS;
			} else if (user != null && user.getIds() != null) {
				userService.deleteByArr(user);
				viewName = this.SUCCESS;
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView(viewName, user, request, messageSource);
	}

	/**
	 * 根据id删除Head信息
	 * 
	 * @param user
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/cooperate/user/updateHeadByid")
	public CommonModelAndView updateHeadByid(User user, HttpServletRequest request, Map<String, Object> model) throws Exception {
		if (user != null && user.getId() != null) {
			userService.updateHeadByid(user);
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, user, messageSource);
		return commonModelAndView;
	}

	/**
	 * 用户角色跳转
	 * 
	 * @param user
	 * @param request
	 * @return
	 */
	@RequestMapping("/cooperate/user/bind")
	public CommonModelAndView auth(User user, HttpServletRequest request) {
		String code = user.getC();
		Organization organization = new Organization();
		String orgName = "";
		try {
			if (user.getId() != null) {
				user = (User) userService.getOne(user);
				organization.setId(user.getOrgId());
				organization.setIsCommission(1);
				organization = organizationService.getOne(organization);
				if (organization != null) {
					orgName = organization.getName();
				}
			}
			user.setC(code);
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		user.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, user, messageSource);
		commonModelAndView.addObject("orgName", orgName);
		commonModelAndView.addObject("user", user);
		return commonModelAndView;
	}

	/**
	 * 用户机构权限数据JSON树
	 * 
	 * @param user
	 * @param request
	 * @return
	 */
	@RequestMapping("/cooperate/user/orgtree")
	public CommonModelAndView orgTree(User user, HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			List<Map<String, Object>> orgList = userService.getUserOrgAndSubOrgTreeList(user);
			map = new HashMap<String, Object>();
			map.put("stree", orgList == null ? new ArrayList<Map<String, Object>>() : orgList);
			orgList = null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 获取角色信息JSON树
	 * 
	 * @param user
	 * @param request
	 * @return
	 */
	@RequestMapping("/cooperate/user/roletree")
	public CommonModelAndView roleTree(User user, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			user.setIsCommission(1);
			List<Map<String, Object>> roleList = userService.getUserRoleTreeList(user);
			map.put("stree", roleList == null ? new ArrayList<Map<String, Object>>() : roleList);
			roleList = null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new CommonModelAndView("jsonView", map);
	}

	/**
	 * 保存用户角色信息
	 * 
	 * @param user
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cooperate/user/saveBind", method = RequestMethod.POST)
	public CommonModelAndView saveAuth(@Valid User user, BindingResult result, HttpServletRequest request, Map<String, Object> model) {
		// 视图名
		String viewName = null;
		try {
			User tempUser = new User();
			tempUser = userService.getOne(user);
			if (tempUser != null && tempUser.getName() != null) {
				user.setName(tempUser.getName());
			}
			userService.saveBind(user);
			viewName = this.SUCCESS;

		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, user, request, messageSource);
		;
		return commonModelAndView;
	}

	@Override
	public String uploadfile(User baseEntity, HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	@Override
	public String downloadfile(User entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(User entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@RequestMapping("/cooperate/user/valid")
	public CommonModelAndView valid(@Valid User user, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		String operate = (user.getOperate() == null || user.getOperate().equals("")) ? "0" : user.getOperate();
		int operateTemp = Integer.parseInt(operate);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		switch (operateTemp) {
		case 1: {
			User tUser;
			// 判断用户账号是否唯一
			if (user.getAccount() != null && user.getAccount().length() > 0) {
				tUser = new User();
				tUser.setId(user.getId());
				tUser.setAccount(user.getAccount());
				Long count = 0l;
				try {
					count = (Long) userService.checkUserDuplication(tUser);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (count != 0) {
					result.rejectValue("account", "Duplicate");
				}
			}

			// 新密码不能为空
			if (user.getNewPassword() == null || user.getNewPassword().equals("")) {
				result.rejectValue("newPassword", "NotEmpty");
			}
			// 确认密码不能为空
			if (user.getConfirmPwd() == null || user.getConfirmPwd().equals("")) {
				result.rejectValue("confirmPwd", "NotEmpty");
			}
			// 判断新密码和确认密码是否一致
			if (result.getFieldErrors("confirmPwd").size() == 0 && !user.getNewPassword().equals(user.getConfirmPwd())) {
				result.rejectValue("confirmPwd", "Disaccord");
			}

			// 新增时判断是否选择归属机构
			if (user.getOrgId() == null) {
				result.rejectValue("orgName", "NotEmpty");
			}
			// 判断用户工号是否唯一
			if (user.getWorkNumber() != null && user.getWorkNumber().length() > 0) {
				tUser = new User();
				tUser.setId(user.getId());
				tUser.setWorkNumber(user.getWorkNumber());
				Long count = 0l;
				try {
					count = (Long) userService.checkUserDuplication(tUser);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (count != 0) {
					result.rejectValue("workNumber", "Duplicate");
				}
			}
			if (result.hasErrors()) {
				commonModelAndView.addBindingErrors(model, result, messageSource);
				return commonModelAndView;
			}
			break;
		}
		case 2: {
			User tUser;
			// 判断用户账号是否唯一
			if (user.getAccount() != null && user.getAccount().length() > 0) {
				tUser = new User();
				tUser.setId(user.getId());
				tUser.setAccount(user.getAccount());
				Long count = 0l;
				try {
					count = (Long) userService.checkUserDuplication(tUser);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (count != 0) {
					result.rejectValue("account", "Duplicate");
				}
			}
			// 判断用户工号是否唯一
			if (user.getWorkNumber() != null && user.getWorkNumber().length() > 0) {
				tUser = new User();
				tUser.setId(user.getId());
				tUser.setWorkNumber(user.getWorkNumber());
				Long count = 0l;
				try {
					count = (Long) userService.checkUserDuplication(tUser);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (count != 0) {
					result.rejectValue("workNumber", "Duplicate");
				}
			}
			if (result.hasErrors()) {
				commonModelAndView.addBindingErrors(model, result, messageSource);
				return commonModelAndView;
			}
			break;
		}
		case 3: {
			String password = user.getNewPassword();
			String confirmPwd = user.getConfirmPwd();
			// 新密码不能为空
			if (password == null || password.equals("")) {
				result.rejectValue("newPassword", "NotEmpty");
			}
			// 确认密码不能为空
			if (confirmPwd == null || confirmPwd.equals("")) {
				result.rejectValue("confirmPwd", "NotEmpty");
			}
			// 判断新密码和确认密码是否一致
			if (!password.equals(confirmPwd)) {
				result.rejectValue("confirmPwd", "Disaccord");
			}
			if (result.hasErrors()) {
				commonModelAndView.addBindingErrors(model, result, messageSource);
				return commonModelAndView;
			}
			break;
		}
		}
		return commonModelAndView;
	}

}
