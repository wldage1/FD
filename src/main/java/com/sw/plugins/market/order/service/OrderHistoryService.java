package com.sw.plugins.market.order.service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.market.order.entity.Order;
/**
 * 成功/失败订单（历史）Service
 * @author liushuai
 *
 */
@Service
public class OrderHistoryService extends CommonService<Order>{
	
	/**
	 * 失败订单列表集合
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getFailGrid(Order order) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Order> resultList = getPaginatedFailList(order);
		Long record = getFailRecordCount(order);
		int pageCount = (int)Math.ceil(record/(double)order.getRows());
		map.put("rows", resultList);
		map.put("page", order.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	
	/**
	 * 失败订单分页查询
	 * @param order
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Order> getPaginatedFailList(Order order) throws Exception {
		return (List<Order>) super.getRelationDao().selectList("orderHistory.select_fail_paginated", order);
	}
	
	/**
	 * 失败订单统计查询
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public Long getFailRecordCount(Order order) throws Exception {
		return  super.getRelationDao().getCount("orderHistory.select_fail_count", order);
	}
	
	 /**
	 * 查询需要核算的订单
	 * @param order
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Order> getAdjustOrderList(Order order) throws Exception {
		return (List<Order>) super.getRelationDao().selectList("orderHistory.select_adjust_order", order);
	}
	
	@Override
	public Order getOne(Order entity) throws Exception {
		return (Order) super.getRelationDao().selectOne("orderHistory.select_one", entity);
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getGrid(Order entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Order> resultList = (ArrayList<Order>) super.getRelationDao().selectList("orderHistory.select_succeed_paginated", entity);
		for(Order order:resultList){
			order.setTradeStatusName(this.getInitData().getTradeStatus().get(1).get(order.getTradeStatus().toString()).toString());
			order.setDocStatusName(this.getInitData().getDocStatus().get(order.getDocStatus().toString()).toString());
		}
		Long record = super.getRelationDao().getCount("orderHistory.select_succeed_count", entity);
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	
	/**
	 * 订单合同编号重复验证
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public Long checkForContractNumber(Order order) throws Exception{
		return getRelationDao().getCount("orderHistory.checkForContractNumber", order);
	}
	
	@Override
	public void save(Order entity) throws Exception {
		super.getRelationDao().insert("orderHistory.insert", entity);
	}
	
	@Override
	public void update(Order entity) throws Exception {
		super.getRelationDao().update("orderHistory.update", entity);
	}
	
	/**
	 * 更新成功订单单证状态
	 * 
	 * @param entity
	 * @throws Exception
	 */
	public void updateDoc(Order entity) throws Exception {
		super.getRelationDao().update("orderHistory.update_doc", entity);
	}
	
	@Override
	public Long getRecordCount(Order entity) throws Exception {
		return null;
	}
	
	@Override
	public List<Order> getList(Order entity) throws Exception {
		return null;
	}
	
	public Long getSuccessOrder(Order entity)throws Exception{
		return getSuccessOrder1(entity) + getSuccessOrder2(entity);
	}
	
	public Long getFailedOrder(Order entity) throws Exception{
		return getFailedOrder1(entity) + getFailedOrder2(entity);
	}
	
	//获取某子产品的某机构的成功订单数 传子产品ID 阶段时间 机构ID 认购
	public Long getSuccessOrder1(Order entity) throws Exception{
		return (Long)super.getRelationDao().selectOne("orderHistory.select_success_byproduct_1", entity);
	}
	
	//获取某子产品的某机构的成功订单数 传子产品ID 阶段时间 机构ID 非认购
	public Long getSuccessOrder2(Order entity) throws Exception{
		return (Long)super.getRelationDao().selectOne("orderHistory.select_success_byproduct_2", entity);
	}
	//获取某子产品某机构的失败订单数 传子产品ID 阶段时间机构ID
	public Long getFailedOrder1(Order entity) throws Exception{
		return (Long)super.getRelationDao().selectOne("orderHistory.select_failed_byproduct_1", entity);
	}
	
	public Long getFailedOrder2(Order entity) throws Exception{
		return (Long)super.getRelationDao().selectOne("orderHistory.select_failed_byproduct_2", entity);
	}
	
	@Override
	public List<Order> getPaginatedList(Order entity) throws Exception {
		return null;
	}
	@Override
	public void delete(Order entity) throws Exception {
	}
	@Override
	public void deleteByArr(Order entity) throws Exception {
	}
	@Override
	public String upload(Order entity, HttpServletRequest request) throws Exception {
		return null;
	}
	@Override
	public Object download(Order entity, HttpServletRequest request) throws Exception {
		return null;
	}
	@Override
	public void init(InitData initData) throws Exception {
	}

	/**
	 * 查询订单关联理财师单证邮寄地址
	 * 
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public Order getMemberContact(Order order) throws Exception {
		return (Order) super.getRelationDao().selectOne("orderHistory.select_member_contact", order);
	}
}
