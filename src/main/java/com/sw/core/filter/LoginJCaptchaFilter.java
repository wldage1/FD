package com.sw.core.filter;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.octo.captcha.service.CaptchaService;

/**
 * 拦截器 - 后台登录验证码
 */

@Component("loginJCaptchaFilter")
public class LoginJCaptchaFilter implements Filter {

	// 后台登录验证失败跳转URL
	public static final String ADMIN_CAPTCHA_ERROR_URL = "/loginVerify?error=captcha";

	@Resource
	private CaptchaService captchaService;

	public void init(FilterConfig fConfig) throws ServletException {}

	public void destroy() {}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		boolean isCaptcha = validateCaptcha(request);
		if (isCaptcha) {
			chain.doFilter(request, response);
		} else {
			response.sendRedirect(request.getContextPath() + ADMIN_CAPTCHA_ERROR_URL);
		}
	}
	
	/**
	 * 校验验证码.
	 * 
	 * @param request  HttpServletRequest对象
	 * 
	 */
	protected boolean validateCaptcha(HttpServletRequest request) {
		try{
//			String captchaID = request.getSession().getId();
//			String challengeResponse = StringUtils.upperCase(request.getParameter(JCaptchaEngine.CAPTCHA_INPUT_NAME));
//			return captchaService.validateResponseForID(captchaID, challengeResponse);
			return true;
		}
		catch (Exception e){
			return false;
		}
	}

	public CaptchaService getCaptchaService() {
		return captchaService;
	}

	public void setCaptchaService(CaptchaService captchaService) {
		this.captchaService = captchaService;
	}

}
