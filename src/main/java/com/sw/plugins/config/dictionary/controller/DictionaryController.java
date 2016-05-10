package com.sw.plugins.config.dictionary.controller;

import java.net.URLDecoder;
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
import com.sw.plugins.config.dictionary.entity.Dictionary;
import com.sw.plugins.config.dictionary.entity.DictionaryItem;
import com.sw.plugins.config.dictionary.service.DictionaryItemService;
import com.sw.plugins.config.dictionary.service.DictionaryService;

/**
 * 字典控制器，负责字典的添加，修改，删除，查询等功能
 * 
 * @author Administrator
 */
@Controller
public class DictionaryController extends BaseController<DictionaryItem> {

	private static Logger logger = Logger.getLogger(DictionaryController.class);

	@Resource
	private DictionaryItemService dictionaryItemService;
	@Resource
	private DictionaryService dictionaryService;
	
	/**
	 * 字典类型列表方法
	 * 
	 * @param dictionary
	 * @param request
	 * @return
	 * @author Administrator
	 * @version 1.0 </pre> Created on :2012-5-2 上午11:44:34 LastModified:
	 *          History: </pre>
	 */
	@RequestMapping("/config/dictionary")
	public CommonModelAndView list(Dictionary dictionary, HttpServletRequest request) {
		Object obj = new CommonModelAndView().getCurrentStatus(dictionary, request);
		if (obj != null) {
			if (obj instanceof Dictionary) {
				dictionary = (Dictionary) obj;
			}
		}
		
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, dictionary,messageSource);
		commonModelAndView.addObject("code", dictionary.getC());
		commonModelAndView.addObject("dictionary", dictionary);
		
		commonModelAndView.addObject("dictionaryItem", new DictionaryItem());
		return commonModelAndView;
	}
	@RequestMapping("/config/dictionary/getDicList.json")
	public CommonModelAndView dicList(Dictionary dictionary, HttpServletRequest request) {
		List<Dictionary> dslist = null;
		try {
			dslist = dictionaryService.getList(new Dictionary());
		} catch (Exception e) {
			logger.error(e);
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, dictionary,messageSource);
		commonModelAndView.addObject("dictionaryList", dslist);
		commonModelAndView.addObject("dicsize", dslist.size());
		return commonModelAndView;
	}
	
	
	/**
	 * 字典值列表方法
	 * 
	 * @param dictionaryItem
	 * @param request
	 * @return
	 * @author Administrator
	 * @version 1.0 </pre> Created on :2012-5-2 上午11:44:34 LastModified:
	 *          History: </pre>
	 */
	@RequestMapping("/config/dictionaryitem")
	public CommonModelAndView list(DictionaryItem dictionaryItem, HttpServletRequest request) {
		Object obj = new CommonModelAndView().getCurrentStatus(dictionaryItem, request);
		if (obj != null) {
			if (obj instanceof DictionaryItem) {
				dictionaryItem = (DictionaryItem) obj;
			}
		}
		List<Dictionary> dslist = null;
		try {
			dslist = dictionaryService.getList(new Dictionary());
		} catch (Exception e) {
			logger.error(e);
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, dictionaryItem,messageSource);
		commonModelAndView.addObject("code", dictionaryItem.getC());
		commonModelAndView.addObject("dictionaryItem", dictionaryItem);
		commonModelAndView.addObject("dictionaryList", dslist);
		commonModelAndView.addObject("dictionaryListSize", dslist.size());
		return commonModelAndView;
	}
	
	/**
	 * 字典类型创建操作
	 * 
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/config/dictionary/save", method = RequestMethod.POST)
	public CommonModelAndView save(@Valid Dictionary dictionary, BindingResult result, Map<String, Object> model, HttpServletRequest request) {

		CommonModelAndView commonModelAndView = null;
		// 视图名
		String viewName = null;
		commonModelAndView = new CommonModelAndView(request, dictionary,messageSource);
		try {
			if (!("").equals(dictionary.getName()) && !("").equals(dictionary.getCode())) {
				Dictionary dicSort = new Dictionary();
				dicSort.setId(dictionary.getId());
				dicSort.setName(dictionary.getName());
				Long countDupliName = (Long) dictionaryService.checkDupicate(dicSort);
				if (countDupliName != 0) {
					//result.rejectValue("name", "already.dictionary.name");
					//return commonModelAndView;
					model.put(MESSAGE,new CommonModelAndView().getPromptMessage(messageSource, "already.dictionary.name", null));
					return new CommonModelAndView("jsonView", model);
				}
				dicSort = new Dictionary();
				dicSort.setId(dictionary.getId());
				dicSort.setCode(dictionary.getCode());
				Long countDupliVaue = (Long) dictionaryService.checkDupicate(dicSort);
				if (countDupliVaue != 0) {
					//result.rejectValue("value", "already.dictionary.value");
					//return commonModelAndView;
					model.put(MESSAGE,new CommonModelAndView().getPromptMessage(messageSource, "already.dictionary.code", null));
					return new CommonModelAndView("jsonView", model);
				}
			}
			if (result.hasErrors()) {
				return commonModelAndView;
			}
			dictionaryService.saveOrUpdate(dictionary);
			viewName = this.SUCCESS;

		} catch (Exception e) {
			logger.debug(e.getMessage());
			viewName = this.ERROR;
		}
		commonModelAndView = new CommonModelAndView(viewName, dictionary, request,messageSource);
		return commonModelAndView;
	}
	
	/**
	 * 查询字典类型信息 返回json格式
	 * 
	 * @param consult
	 * @param request
	 * @return
	 * @author Administrator
	 * @version 1.0 </pre> Created on :2012-4-27 下午2:49:45 LastModified:
	 *          History: </pre>
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/config/dictionary/grid")
	public CommonModelAndView json(Dictionary dictionary, HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			Object obj = dictionaryService.getGrid( dictionary);
			map = obj == null ? null : (Map<String, Object>) obj;
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, dictionary, request);
	}
	
	/**
	 * 跳转到字典值创建页面
	 * 
	 * @param dictionaryItem
	 * @param request
	 * @return
	 * @author Administrator
	 * @version 1.0 </pre> Created on :2012-5-2 上午11:44:34 LastModified:
	 *          History: </pre>
	 */
	@RequestMapping("/config/dictionaryitem/create")
	public CommonModelAndView create(DictionaryItem dictionaryItem, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, dictionaryItem,messageSource);
		// 获取字典类型列表
		List<Dictionary> dslist = null;
		try {
			Dictionary dictionary = new Dictionary();
			dslist = dictionaryService.getList(dictionary);
			commonModelAndView.addObject("dictionaryList", dslist);
			commonModelAndView.addObject("dictionaryItem", dictionaryItem);
		} catch (Exception e) {
			DetailException.expDetail(e, DictionaryController.class);
			logger.debug(e.getMessage());
		}
		return commonModelAndView;
	}

	/**
	 * 字典值创建操作
	 * 
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/config/dictionaryitem/save", method = RequestMethod.POST)
	public CommonModelAndView save(DictionaryItem dictionaryItem, Map<String, String> model, HttpServletRequest request) {
		try {
			model.put(STATUS, "false");
			if ((Long)dictionaryItemService.checkForDupliValue(dictionaryItem) != 0 && dictionaryItem.getId() == null) {
				model.put(MESSAGE,new CommonModelAndView().getPromptMessage(messageSource, "already.dictionaryItem.itemValue", null));
				return new CommonModelAndView("jsonView", model);
			}
			if ((Long)dictionaryItemService.checkForDupliName(dictionaryItem) != 0) {
				model.put(MESSAGE,new CommonModelAndView().getPromptMessage(messageSource, "already.dictionaryItem.itemName", null));
				return new CommonModelAndView("jsonView", model);
			}
			if (dictionaryItem != null && dictionaryItem.getId() != null) {
				dictionaryItemService.update(dictionaryItem);
				model.put(STATUS, "success");
			} else {
				// 查找字典类型ID
				Dictionary dict = new Dictionary();
				dict.setCode(dictionaryItem.getDictionaryCode());
				Dictionary dictionary = dictionaryService.getOne(dict);
				if(dictionary != null){
					dictionaryItem.setDictionaryId(dictionary.getId());
					dictionaryItemService.save(dictionaryItem);
					model.put(STATUS, "success");
				}
			}
			model.put("gid", dictionaryItem.getGeneratedKey() == null ? "":dictionaryItem.getGeneratedKey().toString());
			
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", model);
	}

	/**
	 * 跳转到字典值修改页面
	 * 
	 * @param code
	 * @param consult
	 * @param request
	 * @return
	 */
	@RequestMapping("/config/dictionaryitem/modify")
	public CommonModelAndView modify(DictionaryItem dictionaryItem, HttpServletRequest request, Map<String, Object> model) {
		String code = dictionaryItem.getC();
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, dictionaryItem,messageSource);
		try {

			if (dictionaryItem.getId() != null) {
				dictionaryItem = (DictionaryItem) dictionaryItemService.getOne( dictionaryItem);
			}
			dictionaryItem.setC(code);
			model.put("dictionaryItem", dictionaryItem);

			// 取所有字典类型
			List<Dictionary> dslist = dictionaryService.getList(new Dictionary());

			commonModelAndView.addObject("dictionaryList", dslist);
		} catch (Exception e) {
			DetailException.expDetail(e, DictionaryController.class);
			logger.debug(e.getMessage());
		}
		return commonModelAndView;
	}

	/**
	 * 字典值删除功能，json格式
	 * 
	 * @param consult
	 * @param request
	 * @return
	 */
	@RequestMapping("/config/dictionaryitem/delete")
	public CommonModelAndView delete(DictionaryItem dictionaryItem, HttpServletRequest request) {
		// 视图名
		String viewName = null;
		try {
			String name = URLDecoder.decode(dictionaryItem.getItemName(), Constant.ENCODING);
			dictionaryItem.setItemName(null);
			dictionaryItemService.deleteByArr(dictionaryItem);
			dictionaryItem.setItemName(name);
			viewName = this.SUCCESS;
		} catch (Exception e) {
			viewName = this.ERROR;
			e.printStackTrace();
		}
		CommonModelAndView commonModelAndView = new CommonModelAndView(viewName, dictionaryItem,request, messageSource);
		return commonModelAndView;
	}

	/**
	 * 查询字典值信息 返回json格式
	 * 
	 * @param consult
	 * @param request
	 * @return
	 * @author Administrator
	 * @version 1.0 </pre> Created on :2012-4-27 下午2:49:45 LastModified:
	 *          History: </pre>
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/config/dictionaryitem/grid")
	public CommonModelAndView json(DictionaryItem dictionaryItem, HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			if (dictionaryItem != null && dictionaryItem.getDictionaryCode() != null) {
				Object obj = dictionaryItemService.getGrid(dictionaryItem);
				map = obj == null ? null : (Map<String, Object>) obj;
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map, dictionaryItem, request);
	}

	@Override
	public String uploadfile(DictionaryItem entity, HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
	@Override
	public String downloadfile(DictionaryItem entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public CommonModelAndView functionJsonData(DictionaryItem entity, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 数据字典验证方法
	 * 
	 * */
	@Override
	@RequestMapping("/config/dictionaryItem/valid")
	public CommonModelAndView valid(@Valid DictionaryItem entity, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
				if (result.hasErrors()) {
					commonModelAndView.addBindingErrors(model, result, messageSource);
					return commonModelAndView;
				}				
		return commonModelAndView;
	}
	/**
	 * 字典类型验证方法
	 * 
	 * */
	@RequestMapping("/config/dictionary/valid")
	public CommonModelAndView valid(@Valid Dictionary entity, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		CommonModelAndView commonModelAndView = new CommonModelAndView("jsonView");
				if (result.hasErrors()) {
					commonModelAndView.addBindingErrors(model, result, messageSource);
					return commonModelAndView;
				}				
		return commonModelAndView;
	}
}
