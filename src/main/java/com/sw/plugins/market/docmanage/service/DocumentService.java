package com.sw.plugins.market.docmanage.service;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.customer.member.entity.Member;
import com.sw.plugins.customer.member.service.MemberService;
import com.sw.plugins.market.docmanage.entity.Document;
import com.sw.plugins.market.order.entity.Order;
import com.sw.plugins.market.order.entity.OrderAttachment;
import com.sw.plugins.market.order.service.OrderAttachmentService;
import com.sw.plugins.market.order.service.OrderHistoryService;
import com.sw.plugins.market.order.service.OrderService;
import com.sw.plugins.market.order.service.OrderTransactionDetailService;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;
import com.sw.util.FTPUtil;
import com.sw.util.SystemProperty;

/**
 * 单证-service实现类
 * 
 * @author runchao
 *
 */
public class DocumentService extends CommonService<Document>{
	
	@Resource
	private UserLoginService userLoginService;
	@Resource
	private OrderService orderService;
	@Resource
	private OrderHistoryService orderHistoryService;
	@Resource
	private OrderTransactionDetailService orderTransactionDetailService;
	@Resource
	private MemberService memberService;
	@Resource
	private OrderAttachmentService orderAttachmentService;
	
	/**
	 * 获取在途单证集合
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> getGrid(Document entity) throws Exception {
		User user = userLoginService.getCurrLoginUser();
		Organization organization = user.getSelfOrg();
		if (organization != null) {
			int isCommission = organization.getIsCommission();
			if (isCommission == 1) {
				entity.setOrgId(organization.getId());
			} else {
				entity.setOrgId(null);
			}
		}
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Document> resultList = getPaginatedList(entity);
		Long record = getRecordCount(entity);
		int pageCount = (int)Math.ceil(record/(double)entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	
	/**
	 * 获取成功单证集合
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getGridSuccess(Document entity) throws Exception {
		User user = userLoginService.getCurrLoginUser();
		Organization organization = user.getSelfOrg();
		if (organization != null) {
			int isCommission = organization.getIsCommission();
			if (isCommission == 1) {
				entity.setOrgId(organization.getId());
			} else {
				entity.setOrgId(null);
			}
		}
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Document> resultList = getPaginatedListSuccess(entity);
		Long record = getRecordCountSuccess(entity);
		int pageCount = (int)Math.ceil(record/(double)entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}

	/**
	 * 寄出在途订单单证
	 * 
	 * @param document
	 * @throws Exception
	 */
	public void send(Document document) throws Exception {
		Order order = new Order();
		order.setId(document.getOrderId());
		order.setContractNumber(document.getContractNumber());
		orderService.update(order);
		order = orderService.getOne(order);
		orderTransactionDetailService.save(order);
		save(document);
		saveDetailSend(document);
	}
	
	/**
	 * 寄出成功订单单证
	 * 
	 * @param document
	 * @throws Exception
	 */
	public void sendSuccess(Document document) throws Exception {
		Order order = new Order();
		order.setId(document.getOrderId());
		order.setContractNumber(document.getContractNumber());
		orderHistoryService.updateDoc(order);
		order = orderHistoryService.getOne(order);
		orderTransactionDetailService.save(order);
		save(document);
		saveDetailSend(document);
	}
	
	/**
	 * 合同编号重复验证
	 * 
	 * @param document
	 * @return
	 * @throws Exception
	 */
	public Long checkContractNum(Document document) throws Exception{
		return getRelationDao().getCount("document.check_contract_num", document) + getRelationDao().getCount("document.check_contract_num_history", document);
	}
	
	/**
	 * 收到签字单证或寄出签字用印单证
	 * 
	 * @param document
	 * @throws Exception
	 */
	public void receiveOrSend(Document document) throws Exception {
		update(document);
		if(document.getMemberId()==null && document.getProductId()!=null){
			saveDetailReceive(document);
		}else if(document.getMemberId()!=null && document.getProductId()==null){
			saveDetailSend(document);
		}
	}

	/**
	 * 收到在途订单签字用印单证
	 * 
	 * @param document
	 * @throws Exception
	 */
	public void receiveAdvance(Document document) throws Exception {
		Order order = new Order();
		order.setId(document.getOrderId());
		order.setDocStatus((short)1);
		orderService.update(order);
		order = orderService.getOne(order);
		orderTransactionDetailService.save(order);
		update(document);
		saveDetailReceive(document);
	}
	
	/**
	 * 收到成功订单签字用印单证
	 * 
	 * @param document
	 * @throws Exception
	 */
	public void receiveSuccessAdvance(Document document) throws Exception {
		Order order = new Order();
		order.setId(document.getOrderId());
		order.setDocStatus((short)1);
		orderHistoryService.updateDoc(order);
		order = orderHistoryService.getOne(order);
		orderTransactionDetailService.save(order);
		update(document);
		saveDetailReceive(document);
	}
	
	/**
	 * 保存流水表（寄出操作）
	 * 
	 * @param entity
	 * @throws Exception
	 */
	public void saveDetailSend(Document document) throws Exception {
		Long memberId = document.getMemberId();
		document = getOneDoc(document);
		User user = userLoginService.getCurrLoginUser();
		document.setOperatorId(user.getId());
		Order order = new Order();
		order.setOrderNumber(document.getOrderNumber());
		if(orderService.getMemberContact(order) != null){
			order = orderService.getMemberContact(order);
			document.setReceivorName(order.getContactName());
			document.setReceivorMobilePhone(order.getContactPhone());
			document.setReceivorOfficePhone(order.getContactTelphone());
			document.setReceivorAddress(order.getContactAddress());
			document.setReceivorCode(order.getContactPostCode());
		}else if(orderHistoryService.getMemberContact(order) != null){
			order = orderHistoryService.getMemberContact(order);
			document.setReceivorName(order.getContactName());
			document.setReceivorMobilePhone(order.getContactPhone());
			document.setReceivorOfficePhone(order.getContactTelphone());
			document.setReceivorAddress(order.getContactAddress());
			document.setReceivorCode(order.getContactPostCode());
		}else{
			Member member = new Member();
			member.setId(memberId);
			if(memberService.getMemberContact(member) != null){
				member = memberService.getMemberContact(member);
				document.setReceivorName(member.getContactName());
				document.setReceivorMobilePhone(member.getContactPhone());
				document.setReceivorOfficePhone(member.getContactTelphone());
				document.setReceivorAddress(member.getContactAddress());
				document.setReceivorCode(member.getContactPostCode());
			}
		}
		getRelationDao().insert("document.insert_detail", document);
	}
	
	/**
	 * 保存流水表（收到操作）
	 * 
	 * @param entity
	 * @throws Exception
	 */
	public void saveDetailReceive(Document document) throws Exception {
		Document tempDoc = getConfig(document);
		document = getOneDoc(document);
		User user = userLoginService.getCurrLoginUser();
		document.setOperatorId(user.getId());
		if(tempDoc != null){
			document.setReceivorName(tempDoc.getReceivorName());
			document.setReceivorMobilePhone(tempDoc.getReceivorMobilePhone());
			document.setReceivorOfficePhone(tempDoc.getReceivorOfficePhone());
			document.setReceivorAddress(tempDoc.getReceivorAddress());
			document.setReceivorCode(tempDoc.getReceivorCode());
		}
		getRelationDao().insert("document.insert_detail", document);
	}
	
	/**
	 * 查询产品单证配置信息
	 * 
	 * @param document
	 * @return
	 * @throws Exception
	 */
	public Document getConfig(Document document) throws Exception{
		return (Document) getRelationDao().selectOne("document.select_config", document);
	}

	/**
	 * 上传单证附件
	 * 
	 * @param entity
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	public String upload(Document entity, HttpServletRequest request) throws Exception {
		//FTP上传文件
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("Filedata");
		String path = SystemProperty.getInstance("config").getProperty("order.attachment.path");
		//重置文件名
		String tempFileName = request.getParameter("Filename");
		String fileExtensionName = tempFileName.substring(tempFileName.lastIndexOf("."));
		String realFileName = entity.getOrderNumber() + fileExtensionName;
		return FTPUtil.uploadFile(file, path, realFileName);
	}
	
	/**
	 * 附件标题重复验证
	 * 
	 * @param document
	 * @return
	 * @throws Exception
	 */
	public Long checkAttTitle(Document document) throws Exception{
		return getRelationDao().getCount("document.check_att_title", document);
	}
	
	/**
	 * 保存单证附件
	 * 
	 * @param document
	 * @throws Exception
	 */
	public void saveDocAtt(Document document) throws Exception{
		OrderAttachment orderAttachment = new OrderAttachment();
		orderAttachment.setOrderNumber(document.getOrderNumber());
		orderAttachment.setTitle(document.getAttTitle());
		orderAttachment.setFileName(document.getAttFileName());
		orderAttachment.setFileUrl(document.getAttFileUrl());
		orderAttachment.setAudited((short)1);
		orderAttachmentService.save(orderAttachment);
	}
	
	@Override
	public void save(Document entity) throws Exception {
		getRelationDao().insert("document.insert", entity);
	}
	
	@Override
	public void update(Document entity) throws Exception {
		getRelationDao().update("document.update", entity);
	}
	
	public Document getOneDoc(Document entity) throws Exception {
		return (Document)super.getRelationDao().selectOne("document.select_one", entity);
	}
	
	public Document getWhichOne(Document entity) throws Exception {
		Document document = new Document();
		document = getOne(entity);
		if(document.getContactInformationID()!=null && !document.getContactInformationID().equals("")){
			return document;
		}else{
			return getOne1(entity);
		}
	}
	
	@Override
	public Document getOne(Document entity) throws Exception {
		return (Document)super.getRelationDao().selectOne("document.select_printContent", entity);
	}

	public Document getOne1(Document entity) throws Exception {
		return (Document)super.getRelationDao().selectOne("document.select_printContent1", entity);
	}
	
	@Override
	public Long getRecordCount(Document entity) throws Exception {
		return getRelationDao().getCount("document.count", entity);
	}

	public Long getRecordCountSuccess(Document entity) throws Exception {
		return getRelationDao().getCount("document.count_success", entity);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Document> getPaginatedList(Document entity) throws Exception {
		return (List<Document>) getRelationDao().selectList("document.select_paginated_list", entity);
	}
	
	@SuppressWarnings("unchecked")
	public List<Document> getPaginatedListSuccess(Document entity) throws Exception {
		return (List<Document>) getRelationDao().selectList("document.select_paginated_list_success", entity);
	}
	
	@Override
	public List<Document> getList(Document entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void delete(Document entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByArr(Document entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object download(Document entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
