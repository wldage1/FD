package com.sw.plugins.market.order.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.market.order.entity.Order;
import com.sw.plugins.market.order.entity.OrderAcountAffirmDetail;
import com.sw.plugins.market.order.entity.OrderProof;
import com.sw.plugins.message.messageinterface.service.CommonMessageService;
import com.sw.plugins.message.task.entity.SendMessage;
import com.sw.plugins.message.task.entity.UserMessage;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;
/**
 * 打款单证确认 Service
 * @author liushuai
 *
 */
@Service
public class OrderAcountAffirmDetailService extends CommonService<OrderAcountAffirmDetail>{
	@Resource
	private OrderService orderService;
	@Resource
	private OrderProofService orderProofService;
	@Resource
	private UserLoginService userLoginService;
	@Resource
	private CommonMessageService commonMessageService;
	
	/**
	 * 打款凭证 金额确认
	 * @param orderAcountAffirmDetail
	 * @throws Exception
	 */
	public void updateProofAndSaveAcount(OrderAcountAffirmDetail orderAcountAffirmDetail) throws Exception {
		User user=userLoginService.getCurrLoginUser();
		Order order=new Order();
		order.setOrderNumber(orderAcountAffirmDetail.getOrderNumber());
		order=orderService.getOneByNum(order);
		//金额确认  实现金额追加
		BigDecimal d=order.getPayAmount()==null?BigDecimal.ZERO:order.getPayAmount();
		order.setPayAmount(d.add(orderAcountAffirmDetail.getAffirmProofAmount()));
		//修改到账日期 --将到账日期修改为 最最后的凭证日期
		if(order.getPayTime()!=null && !order.getPayTime().equals("")){
			DateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			if(df.parse(order.getPayTime()).compareTo(df.parse(orderAcountAffirmDetail.getAffirmProofTime()))==-1){
				order.setPayTime(orderAcountAffirmDetail.getAffirmProofTime());
			}
		}else{
			order.setPayTime(orderAcountAffirmDetail.getAffirmProofTime());
		}
		//修改订单中的 资金到账状态/已到账金额/到账日期/（订单交易状态）
		orderService.updatePayProgress(order);
		
		//保持流水数据
		this.saveAcountAffirmDetail(orderAcountAffirmDetail);
		
		OrderProof orderProof=new OrderProof();
		orderProof.setId(orderAcountAffirmDetail.getProofID());
		orderProof.setProofAmount(orderAcountAffirmDetail.getAffirmProofAmount());
		orderProof.setProofTime(orderAcountAffirmDetail.getAffirmProofTime());
		orderProof.setProofStatus((short) 1);
		orderProof.setSerialNumber(orderAcountAffirmDetail.getSerialNumber());
		orderProof.setComfirm(orderAcountAffirmDetail.getComfirm());
		OrderProof proofTemp=orderProofService.getOne(orderProof);
		//修改打款凭证数据
		orderProofService.update(orderProof);
		//如果 确认的凭证金额 与 原有金额不等时 需要-发送消息
		if(proofTemp.getProofAmount().compareTo(orderAcountAffirmDetail.getAffirmProofAmount())!=0){
			try {
				Map<String, Object> parameterMap = new HashMap<String, Object>();
				parameterMap.put("parameter_orderNumber", order.getOrderNumber());
				parameterMap.put("parameter_orderID", order.getId());
				List<UserMessage> receiver = new ArrayList<UserMessage>();
				UserMessage us = new UserMessage();
				us.setUserID(order.getMemberID());
				us.setUserType((short)3);
				us.setUserName(order.getMember().getName());
				receiver.add(us);
				this.getSendMessage(parameterMap, receiver, "1", "sys_manage_order_proof_affirm", user.getId(), Short.valueOf("1"),Short.valueOf("1"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 保存资金确认流水记录
	 * @param orderAcountAffirmDetail
	 * @throws Exception 
	 */
	public void saveAcountAffirmDetail(OrderAcountAffirmDetail orderAcountAffirmDetail) throws Exception {
		User users=userLoginService.getCurrLoginUser();
		orderAcountAffirmDetail.setCreatorUserId(users.getId().toString());
		this.save(orderAcountAffirmDetail);
	}

	/**
	 * 订单到账金额修改  流水保存
	 * @param orderAcountAffirmDetail
	 * @throws Exception 
	 */
	public void saveAcountAndUpdateOrder(OrderAcountAffirmDetail orderAcountAffirmDetail) throws Exception {
		User user=userLoginService.getCurrLoginUser();
		Order order=new Order();
		order.setOrderNumber(orderAcountAffirmDetail.getOrderNumber());
		order.setPayAmount(orderAcountAffirmDetail.getAffirmProofAmount());
		order.setPayTime(orderAcountAffirmDetail.getAffirmProofTime());
		//修改订单中的 资金到账状态/已到账金额/到账日期/（订单交易状态）
		orderService.updatePayProgress(order);
		//保存资金确认流水记录
		this.saveAcountAffirmDetail(orderAcountAffirmDetail);
		
		order=orderService.getOneByNum(order);
		//修改凭证金额时-发送消息
		try {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put("parameter_orderNumber", order.getOrderNumber());
			parameterMap.put("parameter_orderID", order.getId());
			List<UserMessage> receiver = new ArrayList<UserMessage>();
			UserMessage us = new UserMessage();
			us.setUserID(order.getMemberID());
			us.setUserType((short)3);
			us.setUserName(order.getMember().getName());
			receiver.add(us);
			this.getSendMessage(parameterMap, receiver, "1", "sys_manage_order_proof_update", user.getId(), Short.valueOf("1"),Short.valueOf("1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 银行流水 金额确认
	 * @param orderAcountAffirmDetail
	 * @throws Exception
	 */
	public void updateOrderAndSaveAcount(OrderAcountAffirmDetail orderAcountAffirmDetail) throws Exception {
		Order order=new Order();
		order.setOrderNumber(orderAcountAffirmDetail.getOrderNumber());
		order=orderService.getOneByNum(order);
		//金额确认  实现金额追加
		BigDecimal d=order.getPayAmount()==null?BigDecimal.ZERO:order.getPayAmount();
		order.setPayAmount(d.add(orderAcountAffirmDetail.getAffirmProofAmount()));
		//修改到账日期
		order.setPayTime(orderAcountAffirmDetail.getAffirmProofTime());
		//修改订单中的 资金到账状态/已到账金额/到账日期/（订单交易状态）
		orderService.updatePayProgress(order);
		
		//保持流水数据
		this.saveAcountAffirmDetail(orderAcountAffirmDetail);
	}
	
	@Override
	public Map<String, Object> getGrid(OrderAcountAffirmDetail entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<OrderAcountAffirmDetail> resultList = getPaginatedList(entity);
		Long record = getRecordCount(entity);
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	
	/**
	 * 发送消息基础方法
	 * @param parameterMap   模板替换内容
	 * @param receiver		   接受者list
	 * @param sendway		   发送方式
	 * @param templatecode	   模板code
	 * @param senderID		   发送者id
	 * @param sendertype	   发送者类型
	 * @param relationtype	   关联类型
	 * @return
	 * @throws Exception
	 */
	private void getSendMessage(Map<String,Object> parameterMap,List<UserMessage> receiver, String sendway,String templatecode,Long senderID,Short sendertype,Short relationtype) throws Exception {
		SendMessage sm = new SendMessage();
		//接收者List
		sm.setUserList(receiver);
		//设置发送方式
		sm.setSendWayStr(sendway);
		//设置模板参数
		sm.setTemplateParameters(parameterMap);
		//设置模板code
		sm.setTemplateCode(templatecode);
		//设置发送者ID
		sm.setSendUserID(Long.valueOf(senderID));
		//设置发送者类型
		sm.setSendUserType(sendertype);
		//设置关联对象类型
		sm.setRelationType(relationtype);
		
		commonMessageService.send(sm);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OrderAcountAffirmDetail> getPaginatedList(OrderAcountAffirmDetail entity) throws Exception {
		return (List<OrderAcountAffirmDetail>) super.getRelationDao().selectList("orderAcountAffirmDetail.select_paginated", entity);
	}
	@Override
	public void save(OrderAcountAffirmDetail entity) throws Exception {
		super.getRelationDao().insert("orderAcountAffirmDetail.insert", entity);
	}
	@Override
	public Long getRecordCount(OrderAcountAffirmDetail entity) throws Exception {
		return  super.getRelationDao().getCount("orderAcountAffirmDetail.select_count", entity);
	}
	@Override
	public List<OrderAcountAffirmDetail> getList(OrderAcountAffirmDetail entity) throws Exception {
		return null;
	}
	@Override
	public void update(OrderAcountAffirmDetail entity) throws Exception {
	}
	@Override
	public void delete(OrderAcountAffirmDetail entity) throws Exception {
	}
	@Override
	public void deleteByArr(OrderAcountAffirmDetail entity) throws Exception {
	}
	@Override
	public OrderAcountAffirmDetail getOne(OrderAcountAffirmDetail entity) throws Exception {
		return null;
	}
	@Override
	public String upload(OrderAcountAffirmDetail entity, HttpServletRequest request) throws Exception {
		return null;
	}
	@Override
	public Object download(OrderAcountAffirmDetail entity, HttpServletRequest request) throws Exception {
		return null;
	}
	@Override
	public void init(InitData initData) throws Exception {
	}
}
