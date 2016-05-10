package com.sw.plugins.customer.client.controller;

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
import com.sw.plugins.customer.client.entity.Client;
import com.sw.plugins.customer.client.service.ClientService;

/**
 * 理财顾问客户管理控制器
 * 
 * @author runchao
 */
@Controller
public class ClientController extends BaseController<Client> {

	private static Logger logger = Logger.getLogger(ClientController.class);
	
	@Resource
	private ClientService clientService;
	
	/**
	 * 理财顾问客户查询
	 * 
	 * @param client
	 * @param request
	 * @return
	 */
	@RequestMapping("/customer/client")
	public CommonModelAndView list(Client client, HttpServletRequest request) {
		Map<String, Object> clientType = this.initData.getClientType();
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, client, messageSource);
		commonModelAndView.addObject("clientType", clientType);
		commonModelAndView.addObject("clientStatus",this.initData.getClientStatus());
		commonModelAndView.addObject("idCardTypeMap", this.initData.getMemberIDCardType());
		return commonModelAndView;
	}
	
	/**
	 * 理财顾问客户列表，返回json
	 * 
	 * @param client
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/customer/client/grid")
	public CommonModelAndView json(Client client, HttpServletRequest request){
		Object gridJson;
		Map<String, Object> map = null;
		try {
			gridJson = clientService.getGrid(client);
			map = (gridJson == null ? null : (Map<String, Object>)gridJson);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new CommonModelAndView("jsonView", map, client, request);
	}
	
	/**
	 * 理财顾问客户查看操作
	 * 
	 * @param client
	 * @param request
	 * @return
	 */
	@RequestMapping("/customer/client/detail")
	public CommonModelAndView detail(Client client, HttpServletRequest request) {
		String code = client.getC();
		try {
			client = clientService.getOne(client);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		client.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, client, messageSource);
		commonModelAndView.addObject("client", client);
		Map<String, Object> clientType = this.initData.getClientType();
		commonModelAndView.addObject("clientType", clientType);
		Map<String, Object> memberGender = this.initData.getMemberGender();
		commonModelAndView.addObject("memberGender", memberGender);
		Map<String, Object> clientMarried = this.initData.getClientMarried();
		commonModelAndView.addObject("clientMarried", clientMarried);
		commonModelAndView.addObject("memberIDCardType",this.initData.getMemberIDCardType());
		return commonModelAndView;
	}

	/**
	 * 理财顾问客户查看存续产品操作
	 * 
	 * @param client
	 * @param request
	 * @return
	 */
	@RequestMapping("/customer/client/product")
	public CommonModelAndView product(Client client, HttpServletRequest request) {
		//修改了tomcat的URIEncodin-g配置，设置为UTF-8
		/*String name = "";
		try {
			name = new String(client.getName().getBytes("ISO-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}
		client.setName(name);*/
		return new CommonModelAndView(request, client, messageSource);
	}
	
	/**
	 * 理财顾问客户存续产品列表，返回json
	 * 
	 * @param client
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/customer/client/product/grid")
	public CommonModelAndView jsonProduct(Client client, HttpServletRequest request){
		Object gridJson;
		Map<String, Object> map = null;
		try {
			gridJson = clientService.getGridProduct(client);
			map = (gridJson == null ? null : (Map<String, Object>)gridJson);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new CommonModelAndView("jsonView", map, client, request);
	}
	
	@Override
	public String uploadfile(Client entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String downloadfile(Client entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(Client entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView valid(Client entity, BindingResult result,
			Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
