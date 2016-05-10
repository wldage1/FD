package com.sw.plugins.market.order.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.market.order.entity.Order;
/**
 * 订单流水Service
 * @author liushuai
 *
 */
@Service
public class OrderTransactionDetailService extends CommonService<Order>{

	@Override
	public void save(Order entity) throws Exception {
		 super.getRelationDao().insert("orderTransactionDetailHistory.insert", entity);
	}
	@Override
	public void update(Order entity) throws Exception {
	}
	@Override
	public Long getRecordCount(Order entity) throws Exception {
		return null;
	}
	@Override
	public List<Order> getList(Order entity) throws Exception {
		return null;
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
	public Order getOne(Order entity) throws Exception {
		return null;
	}
	@Override
	public Map<String, Object> getGrid(Order entity) throws Exception {
		return null;
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
}
