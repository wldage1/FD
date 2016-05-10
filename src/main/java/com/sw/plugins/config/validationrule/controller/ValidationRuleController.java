package com.sw.plugins.config.validationrule.controller;

import java.net.URLDecoder;
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
import com.sw.plugins.config.validationrule.entity.ValidationRule;
import com.sw.plugins.config.validationrule.service.ValidationRuleService;
import com.sw.util.CommonUtil;


/**
 * 控制器，进行验证规则信息维护
 * @author liubaomin
 *
 */
@Controller  
public class ValidationRuleController extends BaseController<ValidationRule>{  
	
	private static Logger logger = Logger.getLogger(ValidationRuleController.class);
	
    @Resource
    private ValidationRuleService validationRuleService;

    /**
	 *  类简要说明:验证规则列表界面
	 *  @author liubaomin
	 *  @version 1.0
	 *  </pre>
	 *  Created on : 2013-02-26
	 *  LastModified:2013-03-13
	 *  History:
	 *  </pre>
	 */
	@RequestMapping("/config/validationrule")
    public CommonModelAndView list(ValidationRule validationRule,HttpServletRequest request,Map<String, Object> model){ 
		Object obj = new CommonModelAndView().getCurrentStatus(validationRule, request);
		if (obj != null){
			if (obj instanceof ValidationRule){
				validationRule = (ValidationRule)obj;
			}
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(request,validationRule,messageSource);
    	commonModelAndView.addObject("code", validationRule.getC());
    	model.put("validationRule", validationRule);
        return commonModelAndView;
    }
	/**
	 *  类简要说明:获取验证规则列表
	 *  @author liubaomin
	 *  @version 1.0
	 *  </pre>
	 *  Created on : 2013-02-26
	 *  LastModified:2013-03-13
	 *  History:
	 *  </pre>
	 */
	@RequestMapping("/config/validationrule/validationTesting")
	public CommonModelAndView validationTesting(String ruleContent,HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView("/config/validationrule/validation");
		//ruleContent=request.getParameter("ruleContent");
		commonModelAndView.addObject("rule", ruleContent);
		return commonModelAndView;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping("/config/validationrule/grid")
	public CommonModelAndView json(ValidationRule validationRule,String nodeid,HttpServletRequest request){
		validationRule.setName(CommonUtil.convertSearchSign(validationRule.getName()));//获取查询条件
		Map<String, Object> map = null;
		try {
			Object obj = validationRuleService.getGrid(validationRule);
			map = obj == null ? null : (Map<String, Object>) obj;
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, validationRule, request);
	}
	/**
	 *  类简要说明:创建验证规则
	 *  @author liubaomin
	 *  @version 1.0
	 *  </pre>
	 *  Created on : 2013-02-26
	 *  LastModified:2013-03-13
	 *  History:
	 *  </pre>
	 */
	@RequestMapping("/config/validationrule/create")
	public CommonModelAndView create(ValidationRule validationRule,BindingResult result, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, validationRule,messageSource);
		try {
			commonModelAndView.addObject("validationRule", validationRule);
		} catch (Exception e) {
			DetailException.expDetail(e, ValidationRuleController.class);
			logger.debug(e.getMessage());
		}
		/** -- 验证提示 -- */
		result.rejectValue("name","Pattern.validationRule.name");
		result.rejectValue("ruleContent","Size.validationRule.ruleContent");
		result.rejectValue("promptMessage","NotEmpty.validationRule.promptMessage");
		
		return commonModelAndView;
	}
	/**
	 *  类简要说明:删除验证规则
	 *  @author liubaomin
	 *  @version 1.0
	 *  </pre>
	 *  Created on : 2013-02-26
	 *  LastModified:2013-03-13
	 *  History:
	 *  </pre>
	 */
	@RequestMapping("/config/validationrule/delete")
	public CommonModelAndView delete(ValidationRule validationRule, HttpServletRequest request) {
		String viewName = null;
		try {
			String name = URLDecoder.decode(validationRule.getName(), Constant.ENCODING);
			validationRule.setName(name);
			validationRuleService.validationRuleDelete( validationRule);//调用SqlserverValidationService中的validationDelete
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, validationRule, request,messageSource);
		return commonModelAndView;
	}
	/**
	 *  类简要说明:验证规则的保存操作(包括创建与修改)
	 *  @author liubaomin
	 *  @version 1.0
	 *  </pre>
	 *  Created on : 2013-02-26
	 *  LastModified:2013-03-13
	 *  History:
	 *  </pre>
	 */
	@RequestMapping(value = "/config/validationrule/save", method = RequestMethod.POST)
	public CommonModelAndView save(ValidationRule validationRule, HttpServletRequest request) {
		String viewName = null;
		try {
			validationRuleService.saveOrUpdate(validationRule);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, validationRule,request, messageSource);
		return commonModelAndView;
		
	}
	/**
	 *  类简要说明:验证规则的修改
	 *  @author liubaomin
	 *  @version 1.0
	 *  </pre>
	 *  Created on : 2013-02-26
	 *  LastModified:2013-03-13
	 *  History:
	 *  </pre>
	 */
	@RequestMapping("/config/validationrule/modify")
	public CommonModelAndView modify(ValidationRule validationRule, HttpServletRequest request,Map<String,Object> model) {
		String code = validationRule.getC();
		if (validationRule.getId() != null) {
			try {
				validationRule = (ValidationRule)validationRuleService.getOne(validationRule);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		validationRule.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, validationRule,messageSource);
		commonModelAndView.addObject("validationRule", validationRule);
		model.put("validationRule", validationRule);
		return commonModelAndView;
	}
	@Override
	public String uploadfile(ValidationRule entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String downloadfile(ValidationRule entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public CommonModelAndView functionJsonData(ValidationRule entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	@RequestMapping("/config/validationrule/valid")
	public CommonModelAndView valid(@Valid ValidationRule entity, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		String operate = (entity.getOperate() == null || entity.getOperate().equals("")) ? "0" : entity.getOperate();
		int operateTemp = Integer.parseInt(operate);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		try{
			switch(operateTemp){
			    case 1:{
			    	ValidationRule tValidation = new ValidationRule();
					tValidation.setName(entity.getName());
					if (!entity.getName().isEmpty()){
						Long count = (Long) validationRuleService.validationRuleCount(tValidation);//判断该验证过则是否已经存在 1已经存在 0不存在
						if (count == 1) {
							result.rejectValue("name", "dupname");
						}
					}
					if (result.hasErrors()) {
						commonModelAndView.addBindingErrors(model, result, messageSource);
						return commonModelAndView;
					}
			    	break;
			    }
			    case 2:{
			    	ValidationRule tValidation = new ValidationRule();
					tValidation.setName(entity.getName());
					if (!entity.getName().isEmpty()){
						Long count = (Long) validationRuleService.validationRuleCount(tValidation);//判断该验证过则是否已经存在 1已经存在 0不存在
						if (count == 1) {
							ValidationRule dValidation = (ValidationRule)validationRuleService.validationRuleSelect(tValidation);//取出这条存在的验证规则
							if (dValidation != null && dValidation.getId().intValue() != entity.getId().intValue()) {
								result.rejectValue("name", "dupname");
							}
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
