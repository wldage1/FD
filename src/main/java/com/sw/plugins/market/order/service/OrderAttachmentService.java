package com.sw.plugins.market.order.service;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.market.order.entity.Order;
import com.sw.plugins.market.order.entity.OrderAttachment;
import com.sw.util.FTPUtil;
import com.sw.util.SystemProperty;

/**
 * 认购订单附件  service
 * @author liushuai
 *
 */
@Service
public class OrderAttachmentService extends CommonService<OrderAttachment>{

	@Resource
	private OrderService orderService;
	
	@Override
	public Map<String, Object> getGrid(OrderAttachment entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<OrderAttachment> resultList = getPaginatedList(entity);
		Long record = getRecordCount(entity);
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
		
	}

	@Override
	public String upload(OrderAttachment entity, HttpServletRequest request) throws Exception {
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
	 * 保存订单附件   并将销售状态改为 单证归集 /单证归集&资金到账
	 * @param orderAttachment
	 * @throws Exception
	 */
	public void saveOrderAttachment(OrderAttachment orderAttachment) throws Exception {
		/***** 保存合同号并且改变单证状态 *****/
		Order orderContract = new Order();
		orderContract.setId(orderAttachment.getId());
		orderContract.setContractNumber(orderAttachment.getContractNumber());
		orderContract.setDocumentStatus((short)2);
		Order order=orderService.getOne(orderContract);
		/*********  修改订单销售状态  ********/
		//判断订单交易状态是否为资金到账    =4为到账
		if(order.getTradeStatus().equals((short)4)){
			//订单状态设置为 单证归集并且资金到账
			orderContract.setTradeStatus((short)5);
		}else{
			//订单状态设置为 单证归集
			orderContract.setTradeStatus((short)3);
		}
		//更新订单表
		orderService.update(orderContract);
		
		//审核状态
		orderAttachment.setAudited((short)1);
		//保存单证附件表数据
		this.save(orderAttachment);
	}
	
	@Override
	public void save(OrderAttachment entity) throws Exception {
		super.getRelationDao().insert("orderAttachment.insert", entity);
	}
	@Override
	public void update(OrderAttachment entity) throws Exception {
		super.getRelationDao().update("orderAttachment.update", entity);
	}
	@Override
	public Long getRecordCount(OrderAttachment entity) throws Exception {
		return super.getRelationDao().getCount("orderAttachment.select_count", entity);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<OrderAttachment> getPaginatedList(OrderAttachment entity) throws Exception {
		return  (List<OrderAttachment>) super.getRelationDao().selectList("orderAttachment.select_paginated", entity);
	}
	@Override
	public List<OrderAttachment> getList(OrderAttachment entity) throws Exception {
		return null;
	}
	@Override
	public void delete(OrderAttachment entity) throws Exception {
	}
	@Override
	public void deleteByArr(OrderAttachment entity) throws Exception {
	}
	@Override
	public OrderAttachment getOne(OrderAttachment entity) throws Exception {
		return null;
	}
	@Override
	public Object download(OrderAttachment entity, HttpServletRequest request) throws Exception {
		return null;
	}
	@Override
	public void init(InitData initData) throws Exception {
	}
}
