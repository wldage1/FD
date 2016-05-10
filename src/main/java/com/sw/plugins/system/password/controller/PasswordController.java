package com.sw.plugins.system.password.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sw.core.controller.BaseController;
import com.sw.core.controller.CommonModelAndView;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserService;
import com.sw.util.Encrypt;

/**
 * 用户密码控制器，进行用户信息的添加，修改，删除，查询等拦截控制
 * 
 * @author Administrator
 * 
 */
@Controller
public class PasswordController extends BaseController<User> {

	private static Logger logger = Logger.getLogger(PasswordController.class);

	@Resource
	private UserService userService;

	/**
	 * 自助修改密码
	 * 
	 * @param user
	 * @param request
	 * @return
	 */
	@RequestMapping("/system/password")
	public CommonModelAndView shmpwd(User user, HttpServletRequest request) {
		String code = user.getC();
		Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (object instanceof User) {
			Short isfirst=user.getIsFirstLogin(); 
			user = (User) object;
			if(user!=null){
				user.setIsFirstLogin(isfirst);
			}
		}
		user.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, user,messageSource);
		commonModelAndView.addObject("user", user);
		commonModelAndView.addObject("code", user.getC());
		return commonModelAndView;
	}

	/**
	 * 自助修改密码保存
	 * 
	 * @param user
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping("/system/password/shmpwdSave")
	public CommonModelAndView shmpwdSave(User user, HttpServletRequest request) {
		String viewName = this.ERROR;
		try {
			User tempUser = new User();
			tempUser = userService.getOne(user);
			if (tempUser != null && tempUser.getName() != null) {
				user.setName(tempUser.getName());
			}
			userService.save(user);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new CommonModelAndView(viewName, user, request,messageSource);
	}

	@Override
	public String uploadfile(User user, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String downloadfile(User user, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(User user, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@RequestMapping("/system/password/valid")
	public CommonModelAndView valid(User user, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		String operate = (user.getOperate() == null || user.getOperate().equals("")) ? "0" : user.getOperate();
		int operateTemp = Integer.parseInt(operate);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		switch(operateTemp){
		    case 1:{
		    	// 当前登录用户密码
				String currPwd = null;
				// 用户输入的原始密码
				String oldPwd = user.getPassword();
				String newPwd = user.getNewPassword();
				String confirmPwd = user.getConfirmPwd();
				// 获取登录用户信息
				Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				if (object instanceof User) {
					user.setId(((User) object).getId());
					user.setType(((User) object).getType());
					currPwd = ((User) object).getPassword();
				}
				// 原始密码不能为空
				if (oldPwd == null || oldPwd.equals("")) {
					result.rejectValue("password", "NotEmpty");
				}
				// 原始密码输入不正确
				if (result.getFieldErrors("password").size() == 0 && !currPwd.equals(Encrypt.getMD5(oldPwd))) {
					result.rejectValue("password", "Disaccord");
				}
				// 新密码不能为空
				if (newPwd == null || newPwd.equals("")) {
					result.rejectValue("newPassword", "NotEmpty");
				}
				// 确认密码不能为空
				if (confirmPwd == null || confirmPwd.equals("")) {
					result.rejectValue("confirmPwd", "NotEmpty");
				}
				// 判断新密码和确认密码是否一致
				if (result.getFieldErrors("newPassword").size() == 0 && result.getFieldErrors("confirmPwd").size() == 0 && !newPwd.equals(confirmPwd)) {
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
