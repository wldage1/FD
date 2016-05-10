package com.sw.plugins.market.order.service;

import java.math.BigDecimal;
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
import com.sw.plugins.customer.client.entity.Client;
import com.sw.plugins.customer.client.service.ClientService;
import com.sw.plugins.market.order.entity.ClearedProduct;
import com.sw.plugins.market.order.entity.HoldingProduct;
import com.sw.plugins.market.order.entity.RedeemOrder;
import com.sw.plugins.message.task.entity.SendMessage;
import com.sw.plugins.message.task.entity.UserMessage;
import com.sw.plugins.message.messageinterface.service.CommonMessageService;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;
import com.sw.util.NumericUtil;

/**
 * 赎回订单Service
 * @author liushuai
 *
 */
@Service
public class RedeemOrderService extends CommonService<RedeemOrder>{
	
	@Resource
	private OrderHoldingProductService orderHoldingProductService;
	@Resource
	private UserLoginService userLoginService;
	@Resource
	private CommonMessageService commonMessageService;
	@Resource
	private ClientService clientService;
	@Resource
	private ClearedProductService clearedProductService;
	
	
	/**
	 * 已取消订单列表集合
	 * @param redeemOrder
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getCancelGrid(RedeemOrder redeemOrder) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<RedeemOrder> resultList = getPaginatedCancelList(redeemOrder);
		Long record = getCancelRecordCount(redeemOrder);
		int pageCount = (int)Math.ceil(record/(double)redeemOrder.getRows());
		map.put("rows", resultList);
		map.put("page", redeemOrder.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	
	/**
	 * 失败订单列表集合
	 * @param redeemOrder
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getFailGrid(RedeemOrder redeemOrder) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<RedeemOrder> resultList = getPaginatedFailList(redeemOrder);
		Long record = getFailRecordCount(redeemOrder);
		int pageCount = (int)Math.ceil(record/(double)redeemOrder.getRows());
		map.put("rows", resultList);
		map.put("page", redeemOrder.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	
	/**
	 * 已取消订单统计查询
	 * @param redeemOrder
	 * @return
	 * @throws Exception
	 */
	public Long getCancelRecordCount(RedeemOrder redeemOrder) throws Exception {
		return  super.getRelationDao().getCount("redeemorder.select_cancel_redeem_count", redeemOrder);
	}

	/**
	 * 失败订单统计查询
	 * @param redeemOrder
	 * @return
	 * @throws Exception
	 */
	public Long getFailRecordCount(RedeemOrder redeemOrder) throws Exception {
		return  super.getRelationDao().getCount("redeemorder.select_fail_redeem_count", redeemOrder);
	}
	
	/**
	 * 已取消订单分页查询
	 * @param redeemOrder
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<RedeemOrder> getPaginatedCancelList(RedeemOrder redeemOrder) throws Exception {
		return (List<RedeemOrder>) super.getRelationDao().selectList("redeemorder.select_cancel_redeem_paginated", redeemOrder);
	}
	
	/**
	 * 失败订单分页查询
	 * @param redeemOrder
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<RedeemOrder> getPaginatedFailList(RedeemOrder redeemOrder) throws Exception {
		return (List<RedeemOrder>) super.getRelationDao().selectList("redeemorder.select_fail_redeem_paginated", redeemOrder);
	}

	@Override
	public void save(RedeemOrder entity) throws Exception {
		super.getRelationDao().insert("redeemorder.insert", entity);
	}
	
	public void saveTransaction(RedeemOrder entity)throws Exception{
		super.getRelationDao().insert("redeemorder.insert_transaction",entity);
	}
	
	public void saveHistory(RedeemOrder entity)throws Exception{
		super.getRelationDao().insert("redeemorder.insert_history", entity);
	}
	
	@Override
	public void update(RedeemOrder entity) throws Exception {
		super.getRelationDao().update("redeemorder.update", entity);
	}

	public void updateTrade(RedeemOrder entity) throws Exception{
		super.getRelationDao().update("redeemorder.update_trade", entity);
	}
	public void updateRedeemOrderHistory(RedeemOrder order) throws Exception{
		super.getRelationDao().update("redeemorder.update_history", order);
	}
	public void updateRedeemOrder(RedeemOrder order) throws Exception{
		super.getRelationDao().update("redeemorder.update_redeemorder", order);
	}
	
	/**
	 * 赎回订单撤销操作
	 * @param entity
	 * @throws Exception
	 */
	public void updateRepealRedeemOrder(RedeemOrder entity)throws Exception{
		entity.setTradeStatus((short)8);
		//修改销售状态
		this.updateTrade(entity);
		//存流水表
		entity = this.getOne(entity);
		this.saveTransaction(entity);
		//撤销赎回订单发送消息
		try{
			repealRedeemMessage(entity);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据订单号 规则获取订单号
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public String updateRedeemOrderCode(RedeemOrder order) throws Exception{
		//会员ID
		Long clientID=0l;
		//订单ID
		Long orderID=order.getId();
		//供应商ID
		Long providerID=order.getProviderID();
		//产品ID
		Long productID=order.getProductID();
		
		Client client=new Client();
		client.setMemberId(order.getMemberID());
		client.setType(Integer.valueOf(order.getClientType()));
		//代表个人
		if(order.getClientType().equals((short)1)){
			client.setIdCard(order.getiDCard());
			client.setIdCardType(order.getiDCardType());
		}else{
			client.setIdCard(order.getiDCard());
		}
		//验证
		Client clientTemp=clientService.checkMemberClient(client);
		clientID=clientTemp.getId();
		
		//号段1-供应商32进制编号 
		String provoderCode=NumericUtil.formatStr(NumericUtil.numericToString(providerID.intValue(), 32), 3);
		//号段2-产品32位编号 
		String productCode=NumericUtil.formatStr(NumericUtil.numericToString(productID.intValue(), 32), 4);
		//号段3-会员32位编号 
		String clientCode=NumericUtil.formatStr(NumericUtil.numericToString(clientID.intValue(), 32), 4);
		//号段4-订单32位编号 
		String orderCode=NumericUtil.formatStr(NumericUtil.numericToString(orderID.intValue(), 32), 4);
		//订单号
		String orderNumber=provoderCode+productCode+clientCode+"2"+orderCode;
		
		return orderNumber;
	}
	
	/**
	 * 赎回订单修改或创建保存操作
	 * @param entity
	 * @throws Exception
	 */
	public void saveorupdate(RedeemOrder entity)throws Exception{
		if(entity.getId()!=null){
			if(entity.getTradeStatus() == 9){
				HoldingProduct product = new HoldingProduct();
				product.setId(entity.getHoldingProductID());
				product = orderHoldingProductService.getOne(product);
				//插入清算产品所用
				HoldingProduct cleart=product;
				BigDecimal investAmount = product.getInvestAmount().subtract(entity.getAmount());
				BigDecimal share = product.getShare().subtract(entity.getShare());
				product.setInvestAmount(investAmount);
				product.setShare(share);
				//如果 代表全部赎回
				if(share.compareTo(BigDecimal.valueOf(0))==0 || share.compareTo(BigDecimal.valueOf(0))==-1){
					//删除存续产品
					orderHoldingProductService.delete(product);
					//插入到已清算产品
					ClearedProduct clearedProduct=new ClearedProduct();
					clearedProduct.setContractNumber(cleart.getContractNumber());
					clearedProduct.setOrgID(cleart.getOrgID());
					clearedProduct.setProductID(cleart.getProductID());
					clearedProduct.setSubProductID(cleart.getSubProductID());
					clearedProduct.setTeamID(cleart.getTeamID());
					clearedProduct.setMemberID(cleart.getMemberID());
					clearedProduct.setShare(cleart.getShare());
					clearedProduct.setInvestAmount(cleart.getInvestAmount());
					clearedProduct.setDeadline(cleart.getDeadline());
					clearedProduct.setClientType(cleart.getClientType());
					clearedProduct.setClientName(cleart.getClientName());
					clearedProduct.setiDCardType(cleart.getiDCardType());
					clearedProduct.setiDCard(cleart.getiDCard());
					clearedProduct.setPurchaseTime(cleart.getPurchaseTime());
					clearedProduct.setLastTradeTime(cleart.getLastTradeTime());
					clearedProduct.setRemark(cleart.getRemark());
					clearedProductService.saveByRedeem(clearedProduct);
				}else{
					orderHoldingProductService.update(product);
				}
				//删赎回订单表
				this.delete(entity);
				//保存历史订单
				this.saveHistory(entity);
				//获取最新历史订单数据
				entity.setId(entity.getGeneratedKey());
				//获取最新历史数据
				entity = this.getOneHistory(entity);
				//保存流水数据
				this.saveTransaction(entity);
				//赎回订单成功时-发送消息
				try{
					redeemSuccessMessage(entity);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				//修改赎回订单
				this.update(entity);
				//获取最新订单数据
				entity = this.getOne(entity);
				//保存流水数据
				this.saveTransaction(entity);
				//赎回订单修改时-发送消息
				try{
					redeemUpdateMessage(entity);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else{
			if(entity.getTradeStatus() == 9){
				HoldingProduct product = new HoldingProduct();
				product.setId(entity.getHoldingProductID());
				product = orderHoldingProductService.getOne(product);
				//插入清算产品所用
				HoldingProduct cleart=product;
				BigDecimal investAmount = product.getInvestAmount().subtract(entity.getAmount());
				BigDecimal share = product.getShare().subtract(entity.getShare());
				product.setInvestAmount(investAmount);
				product.setShare(share);
				//如果 代表全部赎回
				if(share.compareTo(BigDecimal.valueOf(0))==0 || share.compareTo(BigDecimal.valueOf(0))==-1){
					//删除存续产品
					orderHoldingProductService.delete(product);
					//插入到已清算产品
					ClearedProduct clearedProduct=new ClearedProduct();
					clearedProduct.setContractNumber(cleart.getContractNumber());
					clearedProduct.setOrgID(cleart.getOrgID());
					clearedProduct.setProductID(cleart.getProductID());
					clearedProduct.setSubProductID(cleart.getSubProductID());
					clearedProduct.setTeamID(cleart.getTeamID());
					clearedProduct.setMemberID(cleart.getMemberID());
					clearedProduct.setShare(cleart.getShare());
					clearedProduct.setInvestAmount(cleart.getInvestAmount());
					clearedProduct.setDeadline(cleart.getDeadline());
					clearedProduct.setClientType(cleart.getClientType());
					clearedProduct.setClientName(cleart.getClientName());
					clearedProduct.setiDCardType(cleart.getiDCardType());
					clearedProduct.setiDCard(cleart.getiDCard());
					clearedProduct.setPurchaseTime(cleart.getPurchaseTime());
					clearedProduct.setLastTradeTime(cleart.getLastTradeTime());
					clearedProduct.setRemark(cleart.getRemark());
					clearedProductService.saveByRedeem(clearedProduct);
				}else{
					//更新存续产品数据  投资金额和持有份额
					orderHoldingProductService.update(product);
				}
				
				//保存到历史订单表
				this.saveHistory(entity);
				//获取最新历史订单数据
				entity.setId(entity.getGeneratedKey());
				//获取最新的历史表数据
				entity=this.getOneHistory(entity);
				//生成订单号
				String orderNumber=updateRedeemOrderCode(entity);
				entity.setOrderNumber(orderNumber);
				entity.setId(entity.getId());
				//更新订单号
				this.updateRedeemOrderHistory(entity);
				//保存流水数据
				this.saveTransaction(entity);
				//赎回订单成功时-发送消息
				try{
					redeemSuccessMessage(entity);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				//补录操作保存
				this.save(entity);
				entity.setId(entity.getGeneratedKey());
				//查询最新数据
				entity=this.getOne(entity);
				//生成订单号
				String orderNumber=updateRedeemOrderCode(entity);
				entity.setOrderNumber(orderNumber);
				entity.setId(entity.getId());
				//更新订单号
				this.updateRedeemOrder(entity);
				//存交易流水表
				this.saveTransaction(entity);
				//赎回订单补录时-发送消息
				try{
					redeemAddMessage(entity);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 赎回订单成功订单按钮操作
	 * @param entity
	 * @throws Exception
	 */
	public void updateRedeemSuccess(RedeemOrder entity)throws Exception{
		this.updateTrade(entity);
		entity = this.getOne(entity);
		RedeemOrder redeemOrderTemp=new RedeemOrder();
		redeemOrderTemp=entity;
		//存流水表
		this.saveTransaction(entity);
		//存历史赎回订单表
		this.saveHistory(entity);
		//修改存续产品持有金额
		HoldingProduct holdingProduct = new HoldingProduct();
		holdingProduct.setId(entity.getHoldingProductID());
		holdingProduct = orderHoldingProductService.getOne(holdingProduct);
		//插入清算产品所用
		HoldingProduct cleart=new HoldingProduct();
		cleart=holdingProduct;
		BigDecimal share =  holdingProduct.getShare().subtract(entity.getShare());
		BigDecimal investAmount = holdingProduct.getInvestAmount().subtract(entity.getAmount());
		//如果 代表全部赎回
		if(share.compareTo(BigDecimal.valueOf(0))==0 || share.compareTo(BigDecimal.valueOf(0))==-1){
			//删除存续产品
			orderHoldingProductService.delete(holdingProduct);
			//插入到已清算产品
			ClearedProduct clearedProduct=new ClearedProduct();
			clearedProduct.setContractNumber(cleart.getContractNumber());
			clearedProduct.setOrgID(cleart.getOrgID());
			clearedProduct.setProductID(cleart.getProductID());
			clearedProduct.setSubProductID(cleart.getSubProductID());
			clearedProduct.setTeamID(cleart.getTeamID());
			clearedProduct.setMemberID(cleart.getMemberID());
			clearedProduct.setShare(cleart.getShare());
			clearedProduct.setInvestAmount(cleart.getInvestAmount().multiply(BigDecimal.valueOf(1000000)));
			clearedProduct.setDeadline(cleart.getDeadline());
			clearedProduct.setClientType(cleart.getClientType());
			clearedProduct.setClientName(cleart.getClientName());
			clearedProduct.setiDCardType(cleart.getiDCardType());
			clearedProduct.setiDCard(cleart.getiDCard());
			clearedProduct.setPurchaseTime(cleart.getPurchaseTime());
			clearedProduct.setLastTradeTime(cleart.getLastTradeTime());
			clearedProduct.setRemark(cleart.getRemark());
			clearedProductService.saveByRedeem(clearedProduct);
		}else{
			holdingProduct.setShare(share);
			if(investAmount.compareTo(BigDecimal.valueOf(0))==-1){
				investAmount=BigDecimal.valueOf(0);
			}
			holdingProduct.setInvestAmount(investAmount);
			orderHoldingProductService.update(holdingProduct);
		}
		
		//删赎回订单表
		this.delete(entity);
		//赎回完成发送消息
		try{
			redeemSuccessMessage(redeemOrderTemp);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 赎回订单失败订单操作
	 * @param entity
	 * @throws Exception
	 */
	public void updateRedeemFailed(RedeemOrder entity)throws Exception{
		this.updateTrade(entity);
		entity = this.getOne(entity);
		this.saveHistory(entity);
		this.saveTransaction(entity);
		this.delete(entity);
		//赎回订单失败时-发送消息
		try{
			redeemFailMessage(entity);
		}catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	@Override
	public Long getRecordCount(RedeemOrder entity) throws Exception {
		return super.getRelationDao().getCount("redeemorder.select_count", entity);
	}

	@Override
	public List<RedeemOrder> getList(RedeemOrder entity) throws Exception {
		return null;
	}
	@Override
	public void deleteByArr(RedeemOrder entity) throws Exception {
	}
	@Override
	public String upload(RedeemOrder entity, HttpServletRequest request) throws Exception {
		return null;
	}
	@Override
	public Object download(RedeemOrder entity, HttpServletRequest request) throws Exception {
		return null;
	}
	@Override
	public void init(InitData initData) throws Exception {
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RedeemOrder> getPaginatedList(RedeemOrder entity) throws Exception {
		return (List<RedeemOrder>)super.getRelationDao().selectList("redeemorder.select_paginated", entity);
	}

	@Override
	public void delete(RedeemOrder entity) throws Exception {
		super.getRelationDao().delete("redeemorder.delete", entity);
	}

	@Override
	public RedeemOrder getOne(RedeemOrder entity) throws Exception {
		return (RedeemOrder)super.getRelationDao().selectOne("redeemorder.select_one", entity);
	}
	
	@Override
	public Map<String, Object> getGrid(RedeemOrder entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<RedeemOrder> resultList = getPaginatedList(entity);
		for(RedeemOrder order : resultList){
			order.setTradeStatusName(this.getInitData().getTradeStatus().get(1).get(order.getTradeStatus().toString()).toString());
			order.setDocumentStatusName(this.getInitData().getDocumentStatus().get(order.getDocumentStatus().toString()).toString());
		}
		Long record = getRecordCount(entity);
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	
	/**
	 * 查询成功赎回订单
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getSucceedGrid(RedeemOrder entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		@SuppressWarnings("unchecked")
		List<RedeemOrder> resultList = (ArrayList<RedeemOrder>) super.getRelationDao().selectList("redeemorder.select_succeed_paginated", entity);
		Long record = super.getRelationDao().getCount("redeemorder.select_succeed_count", entity);
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}

	/**
	 * 查询赎回成功订单 BY ID
	 * @param redeemOrder
	 * @return
	 * @throws Exception
	 */
	public RedeemOrder getOneHistory(RedeemOrder redeemOrder) throws Exception {
		return (RedeemOrder)super.getRelationDao().selectOne("redeemorder.select_one_history", redeemOrder);
	}
	
	/**
	 * 成功赎回发送消息
	 * 
	 * @param order
	 * @throws Exception 
	 */
	public void redeemSuccessMessage(RedeemOrder order) throws Exception{
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("parameter_productName", order.getProductName());
		parameterMap.put("parameter_orderNumber", order.getOrderNumber());
		SendMessage sendMessage = new SendMessage();
		UserMessage us = new UserMessage();
		us.setUserID(order.getMemberID());
		us.setUserType((short)3);
		us.setUserName(order.getMemberName());
		List<UserMessage> list = new ArrayList<UserMessage>();
		list.add(us);
		sendMessage.setUserList(list);
		//设置发送方式
		sendMessage.setSendWayStr("1");
		//设置模板参数
		sendMessage.setTemplateParameters(parameterMap);
		//设置发送者ID
		User user = userLoginService.getCurrLoginUser();
		sendMessage.setSendUserID(user.getId());
		//设置发送者类型
		sendMessage.setSendUserType((short)1);
		//设置模板code,发送消息
		sendMessage.setTemplateCode("sys_manage_order_shwc");
		commonMessageService.send(sendMessage);
		sendMessage.setTemplateCode("sms_manage_order_shwc");
		sendMessage.setSendWayStr("2");
		commonMessageService.send(sendMessage);
	}
	
	/**
	 * 失败赎回发送消息
	 * @param order
	 * @throws Exception 
	 */
	public void redeemFailMessage(RedeemOrder order) throws Exception{
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("parameter_orderNumber", order.getOrderNumber());
		SendMessage sendMessage = new SendMessage();
		UserMessage us = new UserMessage();
		us.setUserID(order.getMemberID());
		us.setUserType((short)3);
		us.setUserName(order.getMemberName());
		List<UserMessage> list = new ArrayList<UserMessage>();
		list.add(us);
		sendMessage.setUserList(list);
		//设置发送方式
		sendMessage.setSendWayStr("1");
		//设置模板参数
		sendMessage.setTemplateParameters(parameterMap);
		//设置发送者ID
		User user = userLoginService.getCurrLoginUser();
		sendMessage.setSendUserID(user.getId());
		//设置发送者类型
		sendMessage.setSendUserType((short)1);
		//设置模板code,发送消息
		sendMessage.setTemplateCode("sys_manage_order_fail");
		commonMessageService.send(sendMessage);
	}
	
	/**
	 * 修改赎回发送消息
	 * @param order
	 * @throws Exception 
	 */
	public void redeemUpdateMessage(RedeemOrder order) throws Exception{
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("parameter_orderNumber", order.getOrderNumber());
		SendMessage sendMessage = new SendMessage();
		UserMessage us = new UserMessage();
		us.setUserID(order.getMemberID());
		us.setUserType((short)3);
		us.setUserName(order.getMemberName());
		List<UserMessage> list = new ArrayList<UserMessage>();
		list.add(us);
		sendMessage.setUserList(list);
		//设置发送方式
		sendMessage.setSendWayStr("1");
		//设置模板参数
		sendMessage.setTemplateParameters(parameterMap);
		//设置发送者ID
		User user = userLoginService.getCurrLoginUser();
		sendMessage.setSendUserID(user.getId());
		//设置发送者类型
		sendMessage.setSendUserType((short)1);
		//设置模板code,发送消息
		sendMessage.setTemplateCode("sys_manage_order_xgdd");
		commonMessageService.send(sendMessage);
		sendMessage.setTemplateCode("sms_manage_order_xgdd");
		sendMessage.setSendWayStr("2");
		commonMessageService.send(sendMessage);
	}
	
	/**
	 * 补录赎回发送消息
	 * @param order
	 * @throws Exception 
	 */
	public void redeemAddMessage(RedeemOrder order) throws Exception{
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("parameter_orderNumber", order.getOrderNumber());
		SendMessage sendMessage = new SendMessage();
		UserMessage us = new UserMessage();
		us.setUserID(order.getMemberID());
		us.setUserType((short)3);
		us.setUserName(order.getMemberName());
		List<UserMessage> list = new ArrayList<UserMessage>();
		list.add(us);
		sendMessage.setUserList(list);
		//设置发送方式
		sendMessage.setSendWayStr("1");
		//设置模板参数
		sendMessage.setTemplateParameters(parameterMap);
		//设置发送者ID
		User user = userLoginService.getCurrLoginUser();
		sendMessage.setSendUserID(user.getId());
		//设置发送者类型
		sendMessage.setSendUserType((short)1);
		//设置模板code,发送消息
		sendMessage.setTemplateCode("sys_manage_order_bldd");
		commonMessageService.send(sendMessage);
	}
	
	/**
	 * 撤销赎回订单发送消息
	 * 
	 * @param order
	 */
	public void repealRedeemMessage(RedeemOrder order){
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("parameter_orderNumber", order.getOrderNumber());
		SendMessage sendMessage = new SendMessage();
		UserMessage us = new UserMessage();
		us.setUserID(order.getMemberID());
		us.setUserType((short)3);
		us.setUserName(order.getMemberName());
		List<UserMessage> list = new ArrayList<UserMessage>();
		list.add(us);
		sendMessage.setUserList(list);
		//设置发送方式
		sendMessage.setSendWayStr("1");
		//设置模板参数
		sendMessage.setTemplateParameters(parameterMap);
		//设置发送者ID
		User user = userLoginService.getCurrLoginUser();
		sendMessage.setSendUserID(user.getId());
		//设置发送者类型
		sendMessage.setSendUserType((short)1);
		//设置模板code，发送消息
		sendMessage.setTemplateCode("sys_manage_order_xzcxdd");
		commonMessageService.send(sendMessage);
		sendMessage.setTemplateCode("sms_manage_order_xzcxdd");
		sendMessage.setSendWayStr("2");
		commonMessageService.send(sendMessage);
	}
}
