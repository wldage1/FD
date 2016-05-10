package com.sw.plugins.market.docmanage.controller;

import java.util.HashMap;
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
import com.sw.core.exception.DetailException;
import com.sw.plugins.market.docmanage.entity.Document;
import com.sw.plugins.market.docmanage.service.DocumentService;

/**
 * 单证控制器
 * 
 * @author runchao
 *
 */
@Controller
public class DocumentController extends BaseController<Document>{
	
	private static Logger logger = Logger.getLogger(DocumentController.class);
	
	@Resource
	private DocumentService documentService;

	/**
	 * 单证列表方法
	 * 
	 * @param document
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/docmanage")
	public CommonModelAndView list(Document document, HttpServletRequest request){
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, document, messageSource);
		commonModelAndView.addObject("document", document);
		return commonModelAndView;
	}
	
	/**
	 * 在途订单单证查询，返回json
	 * 
	 * @param document
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/docmanage/grid")
	public CommonModelAndView json(Document document, HttpServletRequest request){
		Map<String, Object> map = null;
		try {
			map = documentService.getGrid(document);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new CommonModelAndView("jsonView", map, document, request);
	}
	
	/**
	 * 成功订单单证查询，返回json
	 * 
	 * @param document
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/docmanage/gridSuccess")
	public CommonModelAndView jsonSuccess(Document document, HttpServletRequest request){
		Map<String, Object> map = null;
		try {
			map = documentService.getGridSuccess(document);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new CommonModelAndView("jsonView", map, document, request);
	}
	
	/**
	 * 打印封面数据 
	 * @param tableType,orderNumber
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/docmanage/printview")
	public CommonModelAndView printContent(Document document,HttpServletRequest request){
		String c = document.getC();
		try {
			document = documentService.getWhichOne(document);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		document.setC(c);
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, document, messageSource);
		commonModelAndView.addObject("document", document);
		return commonModelAndView;
	}
	
	/**
	 * 寄出在途订单单证
	 * 
	 * @param document
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/docmanage/send")
	public CommonModelAndView send(Document document,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			documentService.send(document);
			map.put("status", "success");
		} catch (Exception e) {
			map.put("status", "error");
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 寄出成功订单单证
	 * 
	 * @param document
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/docmanage/sendSuccess")
	public CommonModelAndView sendSuccess(Document document,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			documentService.sendSuccess(document);
			map.put("status", "success");
		} catch (Exception e) {
			map.put("status", "error");
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 合同编号重复验证
	 * 
	 * @param document
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/docmanage/checkContractNum")
	public CommonModelAndView checkContractNum(Document document,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "error");
		try {
			Long count = documentService.checkContractNum(document);
			if(count == 0){
				map.put("status", "success");
			}
		} catch (Exception e) {
			map.put("status", "error");
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 收到签字单证或寄出签字用印单证
	 * 
	 * @param document
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/docmanage/receiveOrSend")
	public CommonModelAndView receiveOrSend(Document document,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			documentService.receiveOrSend(document);
			map.put("status", "success");
		} catch (Exception e) {
			map.put("status", "error");
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 收到在途订单签字用印单证
	 * 
	 * @param document
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/docmanage/receiveAdvance")
	public CommonModelAndView receiveAdvance(Document document,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			documentService.receiveAdvance(document);
			map.put("status", "success");
		} catch (Exception e) {
			map.put("status", "error");
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 收到成功订单签字用印单证
	 * 
	 * @param document
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/docmanage/receiveSuccessAdvance")
	public CommonModelAndView receiveSuccessAdvance(Document document,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			documentService.receiveSuccessAdvance(document);
			map.put("status", "success");
		} catch (Exception e) {
			map.put("status", "error");
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 上传单证附件页
	 * 
	 * @param document
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/docmanage/uploaddoc")
	public CommonModelAndView uploaddoc(Document document, HttpServletRequest request) {
		CommonModelAndView commonModelAndView = new CommonModelAndView(request, document, messageSource);
		commonModelAndView.addObject("document", document);
		commonModelAndView.addObject("iframeID",request.getParameter("iframeID"));
		return commonModelAndView;
	}
	
	/**
	 * 上传单证附件
	 * 
	 * @param document
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/market/docmanage/uploadDocAtt")
	public String uploadDocAtt(Document document,HttpServletRequest request, HttpServletResponse response) {
		try {
			response.getWriter().print(documentService.upload(document, request));
			return null;
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return ERROR;
	}
	
	
	/**
	 * 附件标题重复验证
	 * 
	 * @param document
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/docmanage/checkAttTitle")
	public CommonModelAndView checkAttTitle(Document document,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "error");
		try {
			Long count = documentService.checkAttTitle(document);
			if(count == 0){
				map.put("status", "success");
			}
		} catch (Exception e) {
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
	
	/**
	 * 保存单证附件
	 * 
	 * @param document
	 * @param request
	 * @return
	 */
	@RequestMapping("/market/docmanage/saveDocAtt")
	public CommonModelAndView saveDocAtt(Document document, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			documentService.saveDocAtt(document);
			map.put("status", "success");
		} catch (Exception e) {
			map.put("status", "error");
			logger.error(DetailException.expDetail(e, getClass()));
		}
		return new CommonModelAndView("jsonView", map);
	}
	
	@Override
	public String uploadfile(Document entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String downloadfile(Document entity, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView functionJsonData(Document entity,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonModelAndView valid(Document entity, BindingResult result,
			Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
