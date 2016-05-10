package com.sw.plugins.cooperate.provider.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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

import com.sw.core.controller.BaseController;
import com.sw.core.controller.CommonModelAndView;
import com.sw.core.exception.DetailException;
import com.sw.plugins.config.dictionary.entity.DictionaryItem;
import com.sw.plugins.config.dictionary.service.DictionaryItemService;
import com.sw.plugins.cooperate.provider.entity.Provider;
import com.sw.plugins.cooperate.provider.entity.ProviderUser;
import com.sw.plugins.cooperate.provider.service.ProviderService;
import com.sw.plugins.cooperate.provider.service.ProviderUserService;
import com.sw.util.Encrypt;
import com.sw.util.FTPUtil;
import com.sw.util.SystemProperty;

/**
 * 供应商管理控制器
 * 
 * @author runchao
 */
@Controller
public class ProviderController extends BaseController<Provider> {

	private static Logger logger = Logger.getLogger(ProviderController.class);
	
	@Resource
	private ProviderService providerService;
	@Resource
	private ProviderUserService providerUserService;
	@Resource
	private DictionaryItemService dictionaryItemService;
	/**
	 * 供应商查询
	 * 
	 * @param provider
	 * @param request
	 * @return
	 */
	@RequestMapping("/cooperate/provider")
	public CommonModelAndView list(Provider provider, HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, provider, messageSource);
		Map<String, Object> providerStatus = initData.getProviderStatus();
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			 map = dictionaryItemService.getKeyValue(new DictionaryItem());
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		commonModelAndView.addObject("providerStatus", providerStatus);
		//Map<String, Object> companyType = initData.getOrgType();
		commonModelAndView.addObject("companyType", map);
		return commonModelAndView;
	}
	/**
	 * 供应商用户查询
	 * 
	 * @param provider
	 * @param request
	 * @return
	 */
	@RequestMapping("/cooperate/provider/provideruser")
	public CommonModelAndView userlist(Provider provider, HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, provider, messageSource);
		Map<String, Object> providerUserStatus = initData.getProviderUserStatus();
		commonModelAndView.addObject("providerUserStatus", providerUserStatus);
		return commonModelAndView;
	}
	/**
	 * 供应商用户启用任务
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/cooperate/provider/provideruser/start")
	public CommonModelAndView start(ProviderUser providerUser,HttpServletRequest request) {
		ProviderUser updateProviderUser = new ProviderUser();
		updateProviderUser.setId(providerUser.getId());
		updateProviderUser.setStatus(1);
		String mark="false";
	    try {
	    	//更新供应商用户状态
	    	providerUserService.update(updateProviderUser);
	    	/*发送消息给供应商用户，还没处理
	    	
	    	*/
	    	mark="true";
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("mark", mark);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView", map);
		return commonModelAndView;
	}
	/**
	 * 供应商用户禁用任务
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/cooperate/provider/provideruser/stop")
	public CommonModelAndView stop(ProviderUser providerUser,HttpServletRequest request) {
		ProviderUser updateProviderUser = new ProviderUser();
		updateProviderUser.setId(providerUser.getId());
		updateProviderUser.setStatus(0);
		String mark="false";
	    try {
	    	//更新供应商用户状态
	    	providerUserService.update(updateProviderUser);
	    	/*发送消息给供应商用户，还没处理
	    	
	    	*/
	    	mark="true";
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("mark", mark);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView", map);
		return commonModelAndView;
	}
	/**
	 * 供应商列表，返回json
	 * 
	 * @param provider
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/cooperate/provider/grid")
	public CommonModelAndView json(Provider provider, HttpServletRequest request){
		Object gridJson;
		Map<String, Object> map = null;
		try {
			gridJson = providerService.getGrid(provider);
			map = (gridJson == null ? null : (Map<String, Object>)gridJson);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new CommonModelAndView("jsonView", map, provider, request);
	}
	/**
	 * 供应商用户列表，返回json
	 * 
	 * @param provider
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/cooperate/provider/usermanage/grid")
	public CommonModelAndView userjson(Provider provider, HttpServletRequest request){
		String code = provider.getC();
		Object gridJson;
		Map<String, Object> map = null;
		ProviderUser providerUser =new ProviderUser();
		providerUser.setProvidersID(provider.getId());
		providerUser.setRows(provider.getRows());
		providerUser.setPage(provider.getPage());
		try {
			gridJson = providerUserService.getGrid(providerUser);
			map = (gridJson == null ? null : (Map<String, Object>)gridJson);
			provider = providerService.getOne(provider);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		provider.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView", map, provider, request);
		commonModelAndView.addObject("provider", provider);
		return commonModelAndView;
	}
	
	/**
	 * 供应商查看操作
	 * 
	 * @param provider
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/cooperate/provider/detail")
	public CommonModelAndView detail(Provider provider, HttpServletRequest request, Map<String, Object> model){
		String code = provider.getC();
		try {
			provider = providerService.getOne(provider);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		provider.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, provider, messageSource);
		
		//Map<String, Object> companyType = initData.getOrgType();
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			 map = dictionaryItemService.getKeyValue(new DictionaryItem());
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		commonModelAndView.addObject("companyType", map);
		model.put("provider", provider);
		String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
		commonModelAndView.addObject("ftpHttpUrl",ftpHttpUrl);
		return commonModelAndView;
	}
	
	/**
	 * 供应商创建操作
	 * 
	 * @param provider
	 * @param request
	 * @return
	 */
	@RequestMapping("/cooperate/provider/create")
	public CommonModelAndView create(Provider provider, HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, provider, messageSource);
		//Map<String, Object> companyType = initData.getOrgType();
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			 map = dictionaryItemService.getKeyValue(new DictionaryItem());
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		commonModelAndView.addObject("companyType", map);
		String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
		commonModelAndView.addObject("ftpHttpUrl",ftpHttpUrl);
		return commonModelAndView;
	}
	/**
	 * 供应商用户创建
	 * 
	 * @param provider
	 * @param request
	 * @return
	 */
	@RequestMapping("/cooperate/provider/provideruser/create")
	public CommonModelAndView usercreate(ProviderUser providerUser,BindingResult result, HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, providerUser, messageSource);
		Map<String, Object> providerUserGender = initData.getProviderUserGender();
		/** -- 验证提示 -- */
		result.rejectValue("account","Pattern.providerUser.account");
		result.rejectValue("password","Pattern.providerUser.password");
		result.rejectValue("confirmPwd","Pattern.providerUser.confirmPwd");
		result.rejectValue("authorization","NotEmpty.providerUser.authorization");
		commonModelAndView.addObject("providerUserGender", providerUserGender);
		return commonModelAndView;
	}
	/**
	 * 供应商用户修改操作
	 * 
	 * @param provider
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/cooperate/provider/provideruser/modify")
	public CommonModelAndView modifyuser(ProviderUser providerUser,BindingResult result, HttpServletRequest request, Map<String,Object> model){
		String code = providerUser.getC();
		try {
			providerUser = providerUserService.getOne(providerUser);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		providerUser.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, providerUser, messageSource);
		Map<String, Object> providerUserGender = initData.getProviderUserGender();
		/** -- 验证提示 -- */
		result.rejectValue("account","Pattern.providerUser.account");
		result.rejectValue("authorization","NotEmpty.providerUser.authorization");
		commonModelAndView.addObject("providerUserGender", providerUserGender);
		commonModelAndView.addObject("providerUser", providerUser);
		model.put("providerUser", providerUser);
		return commonModelAndView;
	}
	/**
	 * 供应商用户密码重置操作
	 * 
	 * @param provider
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/cooperate/provider/provideruser/resetpassword")
	public CommonModelAndView resetpassword(ProviderUser providerUser,BindingResult result, HttpServletRequest request, Map<String,Object> model){
		
		String code = providerUser.getC();
		try {
			providerUser = providerUserService.getOne(providerUser);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		model.put("providerUser", providerUser);
		providerUser.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, providerUser, messageSource);
		result.rejectValue("newPwd","Pattern.providerUser.newPwd");
		result.rejectValue("confirmPwd","Pattern.providerUser.confirmPwd");
		return commonModelAndView;
	}
	/**
	 * 供应商用户重置密码
	 * 
	 * @param request
	 * @author liubaomin
	 * @return
	 */
	@RequestMapping("/cooperate/provider/provideruser/updatePassword")
	public CommonModelAndView updatePassword(ProviderUser providerUser,HttpServletRequest request) {
		
 		String viewName = null;
		String code = providerUser.getC();
	    try {
	    	ProviderUser updateProviderUser = new ProviderUser();
			updateProviderUser.setId(providerUser.getId());
			updateProviderUser.setPassword(Encrypt.getMD5(providerUser.getNewPwd()));
	    	//更新理财师的密码为空
	    	providerUserService.update(updateProviderUser);
	    	/*发送消息给理财师机构人员，还没处理
	    	
	    	*/
	    	viewName = this.SUCCESS;
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
			viewName = this.ERROR;
		}
		
		providerUser.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, providerUser, request, messageSource);
		return commonModelAndView;
	}
	/**
	 * 供应商用户详细操作
	 * 
	 * @param provider
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/cooperate/provider/provideruser/detail")
	public CommonModelAndView detailuser(ProviderUser providerUser, HttpServletRequest request, Map<String,Object> model){
		String code = providerUser.getC();
		try {
			providerUser = providerUserService.getOne(providerUser);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		providerUser.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, providerUser, messageSource);
		Map<String, Object> providerUserGender = initData.getProviderUserGender();
		Map<String, Object> providerUserAuthorization = initData.getProviderUserAuthorization();
		commonModelAndView.addObject("providerUserGender", providerUserGender);
		commonModelAndView.addObject("providerUserAuthorization", providerUserAuthorization);
		commonModelAndView.addObject("providerUser", providerUser);
		model.put("providerUser", providerUser);
		return commonModelAndView;
	}
	/**
	 * 供应商修改操作
	 * 
	 * @param provider 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/cooperate/provider/modify")
	public CommonModelAndView modify(Provider provider, HttpServletRequest request, Map<String,Object> model){
		String code = provider.getC();
		try {
			provider = providerService.getOne(provider);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		provider.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, provider, messageSource);
		//Map<String, Object> companyType = initData.getOrgType();
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			 map = dictionaryItemService.getKeyValue(new DictionaryItem());
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		commonModelAndView.addObject("companyType", map);
		commonModelAndView.addObject("provider", provider);
		model.put("provider", provider);
		String ftpHttpUrl = SystemProperty.getInstance("config").getProperty("ftp.http.url");
		commonModelAndView.addObject("ftpHttpUrl",ftpHttpUrl);
		return commonModelAndView;
	}
	
	/**
	 * 供应商保存操作
	 * 
	 * @param provider
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/cooperate/provider/save", method = RequestMethod.POST)
	public CommonModelAndView save(Provider provider, HttpServletRequest request) {
		String viewName = null;
		String code = provider.getC();
		try {
			if(provider.getId() == null){
				providerService.save(provider);
			}else{
				providerService.update(provider);
			}
			viewName = this.SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			viewName = this.ERROR;
		}
		provider.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, provider, request, messageSource);
		return commonModelAndView;
	}
	/**
	 * 供应商用户保存操作
	 * 
	 * @param provider
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/cooperate/provider/provideruser/saveuser", method = RequestMethod.POST)
	public CommonModelAndView saveuser(ProviderUser providerUser, HttpServletRequest request) {
		String viewName = null;
		String code = providerUser.getC();
	    try {

	    	if(providerUser.getId() == null){
	    		if (!providerUser.getPassword().equals("")){
	    			providerUser.setPassword(Encrypt.getMD5(providerUser.getPassword()));
	    		}
	    		providerUserService.save(providerUser);
			}else{
				providerUserService.update(providerUser);
			}
	    	viewName = this.SUCCESS;
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
			viewName = this.ERROR;
		}
		providerUser.setC(code);
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, providerUser, request, messageSource);
		return commonModelAndView;
	}
	
	/**
	 * 供应商状态更改
	 */
	@RequestMapping("/cooperate/provider/status")
	public CommonModelAndView status(Provider provider, HttpServletRequest request){
		String viewName = null;
		ProviderUser providerUser = new ProviderUser();
		providerUser.setProvidersID(provider.getId());
		if(provider.getStatus() == 1){
			providerUser.setStatus(1);
		}else{
			providerUser.setStatus(0);
		}
		try {
			providerService.update(provider);
			providerUserService.update(providerUser);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, provider, request, messageSource);
		return commonModelAndView;
	}
	
	/**
	 * 供应商删除操作
	 * 
	 * @param provider
	 * @param request
	 * @return
	 */
	@RequestMapping("/cooperate/provider/delete")
	public CommonModelAndView delete(Provider provider, HttpServletRequest request) {
		String viewName = null;
		try {
			if(provider.getIds() != null){
				providerService.deleteByArr(provider);
			}else if(provider.getId() != null){
				providerService.delete(provider);
			}
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, provider, request, messageSource);
		return commonModelAndView;
	}
	
	/**
	 * 保存排序号码
	 * @param provider
	 * @param request
	 * @return
	 */
	@RequestMapping("/cooperate/provider/saveSortNum")
	public CommonModelAndView saveSortNum(Provider provider, HttpServletRequest request) {
		String viewName = null;
		try {
			if(provider.getId() != null){
				providerService.update(provider);
			}
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			logger.error(e.getMessage());
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, provider, request, messageSource);
		return commonModelAndView;
	}
	
	/**
	 * 供应商Logo上传
	 * 
	 * @param provider
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	@RequestMapping("/cooperate/provider/uploadfile")
	public String uploadfile(Provider provider, HttpServletRequest request, HttpServletResponse response) {
		try {
			response.getWriter().print(providerService.upload(provider, request));
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return ERROR;
	}
	
	/**
	 * 异步验证
	 * 
	 * @param provider
	 * @param result
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cooperate/provider/valid")
	public CommonModelAndView valid(@Valid Provider provider, BindingResult result, Map<String, Object> model, HttpServletRequest request,	HttpServletResponse response) {
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		if (result.hasErrors()) {
			commonModelAndView.addBindingErrors(model, result, messageSource);
			return commonModelAndView;
		}
		return commonModelAndView;
	}

	@Override
	public String downloadfile(Provider entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(Provider entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	@RequestMapping("/cooperate/provider/provideruser/valid")
	public CommonModelAndView valid(@Valid ProviderUser entity, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		String operate = (entity.getOperate() == null || entity.getOperate().equals("")) ? "0" : entity.getOperate();
		int operateTemp = Integer.parseInt(operate);
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
		try{
			switch(operateTemp){
			    case 1:{
			    	ProviderUser tProviderUser = new ProviderUser();
			    	tProviderUser.setAccount(entity.getAccount());
					if (!entity.getAccount().isEmpty()){
						Long count =providerUserService.getCountForAccount(entity);
						if (count == 1) {
							result.rejectValue("account", "dupaccount");
						}
					}
					if (entity.getConfirmPwd()!=null&&entity.getConfirmPwd()!=""&&!entity.getConfirmPwd().equals(entity.getPassword())){
							result.rejectValue("confirmPwd", "different");
					}
					if (result.hasErrors()) {
						commonModelAndView.addBindingErrors(model, result, messageSource);
						return commonModelAndView;
					}
			    	break;
			    }
			    case 2:{
			    	ProviderUser tProviderUser = new ProviderUser();
			    	tProviderUser.setAccount(entity.getAccount());
			    	tProviderUser.setId(entity.getId());
					if (!entity.getAccount().isEmpty()){
						Long count =providerUserService.getCountForAccount(entity);
						if (count == 1) {
							result.rejectValue("account", "dupaccount");
						}
					}
					if (result.hasErrors()) {
						commonModelAndView.addBindingErrors(model, result, messageSource);
						return commonModelAndView;
					}
			    	break;
			    }
			    case 3:{
			    	ProviderUser tProviderUser = new ProviderUser();
			    	tProviderUser.setAccount(entity.getAccount());
			    	tProviderUser.setId(entity.getId());
			    	if (!entity.getConfirmPwd().equals(entity.getNewPwd())){
						result.rejectValue("confirmPwd", "differentNew");
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
	
	/**
	 * 文本编辑器上传图片
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/cooperate/provider/kindeditor/uploadimg")
	public String kindEditorUpload(HttpServletRequest request, HttpServletResponse response) {
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
		String path = SystemProperty.getInstance("config").getProperty("provider.image.path") + date;
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
			logger.error(e.getMessage());
		}
		return null;
	}
}
