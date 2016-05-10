package com.sw.plugins.config.globalparameter.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sw.core.controller.BaseController;
import com.sw.core.controller.CommonModelAndView;
import com.sw.core.exception.DetailException;
import com.sw.plugins.commission.contract.entity.Contract;
import com.sw.plugins.config.dictionary.entity.Dictionary;
import com.sw.plugins.config.dictionary.entity.DictionaryItem;
import com.sw.plugins.config.dictionary.service.DictionaryItemService;
import com.sw.plugins.config.dictionary.service.DictionaryService;
import com.sw.plugins.config.globalparameter.entity.GlobalParameter;
import com.sw.plugins.config.globalparameter.service.GlobalParameterService;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.plugins.system.organization.service.OrganizationService;

/**
 * 全局参数配置控制器
 * @author Administrator
 *
 */
@Controller
public class GlobalParameterController extends BaseController<GlobalParameter> {
	
	@Resource
	private GlobalParameterService globalParameterService;
	@Resource
	private OrganizationService organizationService;
	@Resource
	private DictionaryService dictionaryService;
	@Resource
	private DictionaryItemService dictionaryItemService;
	
	/**
	 * 跳转全局参数配置页面
	 * @param globalparameter
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/config/globalparameter")
	public CommonModelAndView list(GlobalParameter globalparameter,HttpServletRequest request,Map<String,Object> model) throws Exception{
		Object obj = new CommonModelAndView().getCurrentStatus(globalparameter, request);
		if(obj!=null)
		{
			if(obj instanceof GlobalParameter)
			{
				globalparameter = (GlobalParameter)obj;
			}
		}
		Organization organization = new Organization();
		//获取机构树形
		String orgJson = globalParameterService.getOrgTree(organization);
		//获取居间公司树形
		String comJson = globalParameterService.getComTree(organization);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request,globalparameter,messageSource);
		commonModelAndView.addObject("id",globalparameter.getId());
		commonModelAndView.addObject("globalparameter", globalparameter);
		commonModelAndView.addObject("json", orgJson);
		commonModelAndView.addObject("comJson", comJson);
		return commonModelAndView;	
	}
	/**
	 * 居间公司参数创建修改操作
	 * 
	 * @param organization
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/config/globalparameter/saveCom", method = RequestMethod.POST)
	public CommonModelAndView saveCom(GlobalParameter globalparameter,HttpServletRequest request) {
		// 视图名
		String viewName = null;
		try {
			globalParameterService.saveOrUpdateCom(globalparameter);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			globalparameter.setErrorMsg(e.getMessage());
			viewName = this.ERROR;
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, globalparameter,request, messageSource);
		return commonModelAndView;
	}
	/**
	 * 保存操作
	 * @param globalparameter
	 * @param request
	 * @return
	 */
	@RequestMapping("/config/globalparameter/save.json")
	public CommonModelAndView save(GlobalParameter globalparameter,HttpServletRequest request,Map<String, Object> model){
		String viewName = null;
		//验证参数值非空
		if(globalparameter.getValue()==null || globalparameter.equals("")){
			model.put(MESSAGE, new CommonModelAndView().getPromptMessage(messageSource,"globalparameter.value.notnull" , null));
			return new CommonModelAndView("jsonView",model);
		}
		
		//满足条件为修改
		if(globalparameter.getId()!=null){
			try {
				//修改操作
				globalParameterService.update(globalparameter);
				viewName = this.SUCCESS;
			} catch (Exception e) {
				viewName = this.ERROR;
				e.printStackTrace();
			}		
		}else{
			try {
				Long count = globalParameterService.getRecordCount(globalparameter);
				//判断是否存在相同机构下的相同参数
				if(count == 1){
					model.put(MESSAGE, new CommonModelAndView().getPromptMessage(messageSource, "globalparameter.NoRepeat", null));
					return new CommonModelAndView("jsonView",model);
				}else{
						try {
							//新增操作
							globalParameterService.save(globalparameter);
							viewName = this.SUCCESS;
						} catch (Exception e) {
							viewName = this.ERROR;
							e.printStackTrace();
						}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName,globalparameter,request,messageSource);
		return commonModelAndView;		
	}
	
	/**
	 * 获取参数信息列表
	 * @param globalparameter
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/config/globalparameter/grid")
	public CommonModelAndView json(GlobalParameter globalparameter,HttpServletRequest request)throws Exception{
		Map<String,Object> map = null;
		Object gridJson;
		gridJson = globalParameterService.getGrid(globalparameter);
		map = (gridJson == null ? null : (Map<String, Object>) gridJson);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView",map,globalparameter,request);
		return commonModelAndView;
	}
	/**
	 * 居间公司参数创建操作
	 * 
	 * @param provider
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/config/globalparameter/createcom")
	public CommonModelAndView create(GlobalParameter globalparameter,Map<String, Object> model, HttpServletRequest request) throws Exception{
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, globalparameter, messageSource);
		Organization organization = new Organization();
		if (globalparameter.getOrgID()!=null){
			organization.setId((long)globalparameter.getOrgID());
			try {
				organization = organizationService.getOne(organization);
				if (organization!=null&&organization.getIsCommission()==1){
					globalparameter.setOrgName(organization.getName());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		model.put("globalparameter", globalparameter);
		return commonModelAndView;
	}
	/**
	 * 居间公司参数修改页面
	 * 
	 * @param code
	 * @param organization
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/config/globalparameter/modifycom")
	public CommonModelAndView modifycom(GlobalParameter globalparameter,Map<String, Object> model, HttpServletRequest request) throws Exception {
		String code = globalparameter.getC();
		if (globalparameter.getId()!=null){
			globalparameter= globalParameterService.getOneCom(globalparameter);
			if(globalparameter != null){
				globalparameter.setC(code);
			}
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, globalparameter, messageSource);
		model.put("globalparameter", globalparameter);
		commonModelAndView.addObject("globalparameter",globalparameter);
		return commonModelAndView;
	}

	/**
	 * 获取参数信息列表
	 * @param globalparameter
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/config/globalparameterCom/grid")
	public CommonModelAndView jsonCom(GlobalParameter globalparameter,HttpServletRequest request)throws Exception{
		Map<String,Object> map = null;
		Object gridJson;
		gridJson = globalParameterService.getGridCom(globalparameter);
		map = (gridJson == null ? null : (Map<String, Object>) gridJson);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView",map,globalparameter,request);
		return commonModelAndView;
	}
	
	/**
	 * 删除参数操作
	 * @param globalparameter
	 * @param request
	 * @return
	 */
	@RequestMapping("/config/globalparameter/delete")
	public CommonModelAndView delete(GlobalParameter globalparameter,HttpServletRequest request){
		String viewName = null;
		try {
			globalParameterService.delete(globalparameter);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			e.printStackTrace();
		}
	CommonModelAndView commonModelAndView = new CommonModelAndView(viewName,globalparameter,request,messageSource);
	return commonModelAndView;
	}
	
	@Override
	public String uploadfile(GlobalParameter entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String downloadfile(GlobalParameter entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(GlobalParameter entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	@RequestMapping("/config/globalparameter/valid")
	public CommonModelAndView valid(@Valid GlobalParameter globalParameter, BindingResult result, Map<String, Object> model, HttpServletRequest request,	HttpServletResponse response) {
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		//判断标题是否存在
		if (!globalParameter.getCode().isEmpty()) {
			GlobalParameter gp = new  GlobalParameter();
			gp.setCode(globalParameter.getCode());
			gp.setOrgID(globalParameter.getOrgID());
			gp.setId(globalParameter.getId());
			Long count = null;
			try {
				count = (Long) globalParameterService.getForCode(gp);
				if (count > 0) {
					result.rejectValue("code", "dupCode");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		if (result.hasErrors()) {
			commonModelAndView.addBindingErrors(model, result, messageSource);
			return commonModelAndView;
		}
		return commonModelAndView;
	}
	

}
