package com.sw.plugins.market.order.service;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.market.order.entity.OrderProof;
import com.sw.util.FTPUtil;
import com.sw.util.SystemProperty;
/**
 * 打款凭证 Service
 * @author liushuai
 *
 */
@Service
public class OrderProofService extends CommonService<OrderProof>{
	
	/**
	 * 保存 默认处理状态为 0 代表未处理
	 */
	@Override
	public void save(OrderProof orderProof) throws Exception {
		orderProof.setProofStatus((short)0);
		getRelationDao().insert("orderProof.insert", orderProof);
	}
	
	@Override
	public OrderProof getOne(OrderProof entity) throws Exception {
		return (OrderProof) super.getRelationDao().selectOne("orderProof.select_one", entity);
	}

	@Override
	public Map<String, Object> getGrid(OrderProof entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<OrderProof> resultList = getPaginatedList(entity);
		Long record = getRecordCount(entity);
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}

	/**
	 * 上传打款凭证
	 */
	@Override
	public String upload(OrderProof orderProof, HttpServletRequest request) throws Exception {
		String path = SystemProperty.getInstance("config").getProperty("order.proof.path") + orderProof.getOrderNumber();
		//重置文件名
		String tempFileName = request.getParameter("Filename");
		String fileExtensionName = tempFileName.substring(tempFileName.lastIndexOf("."));
		Long newFileName = System.currentTimeMillis(); 
		//String newFileName = FTPUtil.getRandomString();
		String realFileName = newFileName + fileExtensionName;
		//FTP上传文件
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		return FTPUtil.uploadFile(multipartRequest.getFile("fileName"), path, realFileName);
	}
	
	/**
	 * 获取未匹配的 打款凭证数据
	 * @param orderProof
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getNotMapingGrid(OrderProof orderProof) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<OrderProof> resultList = (List<OrderProof>) super.getRelationDao().selectList("orderProof.select_notmaping", orderProof);
		Long record = super.getRelationDao().getCount("orderProof.select_notmaping_count", orderProof);
		int pageCount = (int) Math.ceil(record / (double) orderProof.getRows());
		map.put("rows", resultList);
		map.put("page", orderProof.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	
	@Override
	public void update(OrderProof entity) throws Exception {
		super.getRelationDao().update("orderProof.update", entity);
	}
	@Override
	public Long getRecordCount(OrderProof entity) throws Exception {
		return super.getRelationDao().getCount("orderProof.select_count", entity);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<OrderProof> getPaginatedList(OrderProof entity) throws Exception {
		return (List<OrderProof>) super.getRelationDao().selectList("orderProof.select_paginated", entity);
	}
	@Override
	public void delete(OrderProof entity) throws Exception {
	}
	@Override
	public List<OrderProof> getList(OrderProof entity) throws Exception {
		return null;
	}
	@Override
	public void deleteByArr(OrderProof entity) throws Exception {
	}
	@Override
	public Object download(OrderProof entity, HttpServletRequest request) throws Exception {
		return null;
	}
	@Override
	public void init(InitData initData) throws Exception {
	}
}
