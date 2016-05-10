package com.sw.plugins.market.order.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import jxl.write.WritableWorkbook;

import org.springframework.stereotype.Service;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.customer.client.entity.Client;
import com.sw.plugins.customer.client.service.ClientService;
import com.sw.plugins.market.bankwater.entity.PayConfirmFromProvider;
import com.sw.plugins.market.bankwater.service.BankWaterService;
import com.sw.plugins.market.order.entity.HoldingProduct;
import com.sw.plugins.market.order.entity.Order;
import com.sw.plugins.message.task.entity.SendMessage;
import com.sw.plugins.message.task.entity.UserMessage;
import com.sw.plugins.message.messageinterface.service.CommonMessageService;
import com.sw.plugins.product.manage.entity.Product;
import com.sw.plugins.product.manage.entity.ProductProfit;
import com.sw.plugins.product.manage.entity.SubProduct;
import com.sw.plugins.product.manage.service.ProductProfitService;
import com.sw.plugins.product.manage.service.ProductService;
import com.sw.plugins.product.manage.service.SubProductService;
import com.sw.plugins.system.user.entity.User;
import com.sw.plugins.system.user.service.UserLoginService;
import com.sw.util.ExportExcel;
import com.sw.util.NumericUtil;

/**
 * 在途订单Service
 * @author liushuai
 *
 */
@Service
public class OrderService extends CommonService<Order>{
	
	@Resource
	private OrderHistoryService orderHistoryService;
	@Resource
	private OrderTransactionDetailService orderTransactionDetailService;
	@Resource
	private OrderHoldingProductService orderHoldingProductService;
	@Resource
	private ProductService productService;
	@Resource
	private ProductProfitService productProfitService;
	@Resource
	private UserLoginService userLoginService;
	@Resource
	private CommonMessageService commonMessageService;
	@Resource
	private ClientService clientService;
	@Resource
	private BankWaterService bankWaterService;
	@Resource
	private SubProductService subProductService;
	
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

	@Override
	public void save(Order entity) throws Exception {
		super.getRelationDao().insert("order.insert", entity);
	}

	@Override
	public void update(Order entity) throws Exception {
		super.getRelationDao().update("order.update", entity);
	}

	@Override
	public Long getRecordCount(Order entity) throws Exception {
		return super.getRelationDao().getCount("order.select_count", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Order> getList(Order entity) throws Exception {
		return (List<Order>) super.getRelationDao().selectList("order.select_list", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Order> getPaginatedList(Order entity) throws Exception {
		return (List<Order>) super.getRelationDao().selectList("order.select_paginated", entity);
	}

	@Override
	public void delete(Order entity) throws Exception {
		super.getRelationDao().delete("order.delete", entity);
	}

	@Override
	public void deleteByArr(Order entity) throws Exception {
	}

	/**
	 * 通过订单编号查询订单数据
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public Order getOneByNum(Order entity) throws Exception {
		return (Order) super.getRelationDao().selectOne("order.select_one_byNum", entity);
	}
	/**
	 * 通过ID 查询数据
	 */
	@Override
	public Order getOne(Order entity) throws Exception {
		return (Order) super.getRelationDao().selectOne("order.select_one", entity);
	}
	
	/**
	 * 返回分页数据
	 */
	@Override
	public Map<String, Object> getGrid(Order entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Order> resultList = getPaginatedList(entity);
		for (Order order : resultList) {
			order.setTradeStatusName(this.getInitData().getTradeStatus().get(1).get(order.getTradeStatus().toString()).toString());
			order.setDocStatusName(this.getInitData().getDocStatus().get(order.getDocStatus().toString()).toString());
		}
		Long record = getRecordCount(entity);
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		Map<String, Object> sqlMap = new HashMap<String, Object>();
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		map.put("userdata", sqlMap);
		return map;
	}
	
	/**
	 * 导出 Excel
	 * @param c
	 * @param businessTypeID
	 * @param colName
	 * @param colModel
	 * @param excel
	 * @param entity
	 * @param tradeStatusMap
	 * @param documentStatusMap
	 * @param tradeTypeMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public WritableWorkbook exportExcel(String c, String businessTypeID, String colName, String colModel, WritableWorkbook excel,
			Order entity,Map<String, Object> tradeStatusMap,Map<String, Object> documentStatusMap,Map<String, Object> tradeTypeMap,Map<String, Object> productTypeMap) throws Exception {	
		List<Map<String, Object>> objList = (List<Map<String, Object>>) super.getRelationDao().selectList("order.select_for_export",entity);
		if (objList != null) {
			for (Map<String, Object> one : objList) {
				//TSR姓名
				one.put("orderNumber",one.get("ORDERNUMBER"));
				Set productTypeSet=productTypeMap.entrySet();
				for(Iterator iter=productTypeSet.iterator(); iter.hasNext();){
					Map.Entry entry=(Map.Entry) iter.next();
					if(entry.getKey().equals(one.get("PRODUCTTYPE")==null? "" :one.get("PRODUCTTYPE").toString())){
						String value = (String)entry.getValue();
					    one.put("productType",value);
					}
				}
				one.put("productShortName",one.get("productShortName"));
				one.put("subProductType",one.get("subProductType"));
				one.put("providerShortName",one.get("providerShortName"));
				one.put("orgShortName",one.get("orgShortName"));
				one.put("clientName",one.get("CLIENTNAME"));
				one.put("orderTime",one.get("ORDERTIME"));
				one.put("investAmount",one.get("INVESTAMOUNT"));
				one.put("commissionPayTime",one.get("COMMISSIONPAYTIME"));
				one.put("memberName",one.get("memberName"));
				one.put("mcOrgName",one.get("MCORGNAME"));
				Set set = tradeTypeMap.entrySet();
				for(Iterator iter=set.iterator(); iter.hasNext();)
				{
				   Map.Entry entry = (Map.Entry)iter.next();
				   if( entry.getKey().equals(one.get("TRADETYPE").toString())){
					   String value = (String)entry.getValue();
					   one.put("tradeType",value);
				   }
				}
				Set setTradeStatusMap = tradeStatusMap.entrySet();
				for(Iterator iter=setTradeStatusMap.iterator(); iter.hasNext();)
				{
				   Map.Entry entry = (Map.Entry)iter.next();
				   if( entry.getKey().equals(one.get("TRADESTATUS").toString())){
					   String value = (String)entry.getValue();
					   one.put("tradeStatusName",value);
				   }
				}
				Set setDocumentStatusMap = documentStatusMap.entrySet();
				for(Iterator iter=setDocumentStatusMap.iterator(); iter.hasNext();)
				{
				   Map.Entry entry = (Map.Entry)iter.next();
				   if( entry.getKey().equals(one.get("DOCSTATUS")==null ? "" :one.get("DOCSTATUS").toString())){
					   String value = (String)entry.getValue();
					   one.put("docStatusName",value);
				   }
				}
				one.put("payTime",one.get("PAYTIME"));
				one.put("payAmount",one.get("PAYAMOUNT"));
				one.put("commission",one.get("COMMISSION"));
			}
		}
		String[] colNames = colName.split(",");
		String[] colModels = colModel.split(",");
		
		String excelName = "在途订单统计";
		int colNameNum = colNames.length ;
		int colModelNum = colModels.length;
		ExportExcel exportExcel = new ExportExcel();
		excel = exportExcel.createExcel(excelName, colNames, colNameNum, colModels, colModelNum, objList, excel);
		return excel;
	}
	
	/**
	 * 已取消订单列表集合
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getCancelGrid(Order order) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Order> resultList = getPaginatedCancelList(order);
		Long record = getCancelRecordCount(order);
		int pageCount = (int) Math.ceil(record / (double) order.getRows());
		map.put("rows", resultList);
		map.put("page", order.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}

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
		int pageCount = (int) Math.ceil(record / (double) order.getRows());
		map.put("rows", resultList);
		map.put("page", order.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}

	/**
	 * 已取消订单统计查询
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public Long getCancelRecordCount(Order order) throws Exception {
		return super.getRelationDao().getCount("order.select_cancel_count", order);
	}

	/**
	 * 失败订单统计查询
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public Long getFailRecordCount(Order order) throws Exception {
		return super.getRelationDao().getCount("order.select_fail_count", order);
	}

	/**
	 * 已取消订单分页查询
	 * @param order
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Order> getPaginatedCancelList(Order order) throws Exception {
		return (List<Order>) super.getRelationDao().selectList("order.select_cancel_paginated", order);
	}

	/**
	 * 失败订单分页查询
	 * @param order
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Order> getPaginatedFailList(Order order) throws Exception {
		return (List<Order>) super.getRelationDao().selectList("order.select_fail_paginated", order);
	}
	
	/**
	 * 通过理财师和客户验证是否是 该理财师下的客户  如果不是讲自动添加到该理财师名下
	 * @param order
	 * @return 订单号
	 * @throws Exception 
	 */
	public String updateMemberClinet(Order order) throws Exception{
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
		client.setName(order.getClientName());
		client.setStatus(1);
		//代表个人
		if(order.getClientType().equals((short)1)){
			client.setIdCard(order.getiDCard());
			client.setIdCardType(order.getiDCardType());
		}else{
			client.setIdCard(order.getiDCard());
		}
		//验证
		Client clientTemp=clientService.checkMemberClient(client);
		if(clientTemp==null){
			client.setIsOpenToOrg((short)0);
			User user=userLoginService.getCurrLoginUser();
			client.setCreatorUserID(user.getId());
			clientService.save(client);
			clientID=client.getGeneratedKey();
		}else{
			//判断是否为正常客户 1为正常
			if(clientTemp.getStatus()!=1){
				clientTemp.setStatus(1);
				//将其客户状态改为正常状态
				clientService.update(clientTemp);
			}
			clientID=clientTemp.getId();
		}
		//号段1-供应商32位编号 
		String provoderCode=NumericUtil.formatStr(NumericUtil.numericToString(providerID.intValue(), 32), 3);
		//号段2-产品32位编号 
		String productCode=NumericUtil.formatStr(NumericUtil.numericToString(productID.intValue(), 32), 4);
		//号段3-会员32位编号 
		String clientCode=NumericUtil.formatStr(NumericUtil.numericToString(clientID.intValue(), 32), 4);
		//号段4-订单32位编号 
		String orderCode=NumericUtil.formatStr(NumericUtil.numericToString(orderID.intValue(), 32), 4);
		//订单号
		String orderNumber=provoderCode+productCode+clientCode+"1"+orderCode;
		
		return orderNumber;
	}

	/**
	 * 保存或修改订单
	 * @param order
	 * @throws Exception
	 */
	public void saveOrupdate(Order order) throws Exception {
		User user=userLoginService.getCurrLoginUser();
		Order orderTemp=new Order();
		if (order.getId() == null) {
			if(order.getTradeStatus().equals((short)7)){
				//操作存续产品
				this.saveOrUpdateHodingProduct(order);
				//保存成功（历史）订单表
				orderHistoryService.save(order);
				order.setId(order.getGeneratedKey());
				//获取最新历史订单数据
				order=orderHistoryService.getOne(order);
				//理财师客户操作  并且将产生订单号  返回订单号
				String orderNumber=this.updateMemberClinet(order);
				//修改订单 增加订单号
				orderTemp.setOrderNumber(orderNumber);
				orderTemp.setId(order.getId());
				order.setOrderNumber(orderNumber);
				//更新订单号 同时更新成功 日期 
				if(order.getProduct().getType().equals((short)2) || order.getProduct().getType().equals((short)5)){
					SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					orderTemp.setCheckTime(df.format(new Date()));
				}else{
					orderTemp.setCheckTime(order.getProduct().getFoundDate());
				}
				orderHistoryService.update(orderTemp);
			}else{
				//保存订单数据
				this.save(order);
				order.setId(order.getGeneratedKey());
				//获取最新订单数据
				order=this.getOne(order);
				//理财师客户操作  并且将产生订单号  返回订单号
				String orderNumber=this.updateMemberClinet(order);
				//修改订单 增加订单号
				orderTemp.setOrderNumber(orderNumber);
				orderTemp.setId(order.getId());
				order.setOrderNumber(orderNumber);
				//更新订单号
				this.update(orderTemp);
			}
			//保存流水数据
			orderTransactionDetailService.save(order);
			try{
				//补录订单发送消息
				Map<String, Object> parameterMap = new HashMap<String, Object>();
				parameterMap.put("parameter_orderNumber", order.getOrderNumber());
				List<UserMessage> receiver = new ArrayList<UserMessage>();
				UserMessage us = new UserMessage();
				us.setUserID(order.getMemberID());
				us.setUserType((short)3);
				us.setUserName(order.getMember().getName());
				receiver.add(us);
				this.getSendMessage(parameterMap, receiver, "1", "sys_manage_order_bldd", user.getId(), Short.valueOf("1"),Short.valueOf("1"));
			}catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Order temp = new Order();
			if(order.getTradeStatus().equals((short)7)){//修改为成功订单
				temp.setId(order.getId());
				temp = this.getOne(temp);
				//删除本条订单记录
				this.delete(order);
				//操作存续产品
				this.saveOrUpdateHodingProduct(order);
				//保存成功（历史）订单表
				if(temp.getProduct().getType().equals((short)2) || temp.getProduct().getType().equals((short)5)){
					SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					order.setCheckTime(df.format(new Date()));
				}else{
					order.setCheckTime(temp.getProduct().getFoundDate());
				}
				orderHistoryService.save(order);
				order.setId(order.getGeneratedKey());
			}else if(order.getTradeStatus().equals((short)13)){//修改为失败订单
				//修改订单
				this.update(order);
				temp=this.getOne(order);
				// 删除本条订单记录
				this.delete(temp);
				// 保存成功（历史）订单表
				if(temp.getProduct().getType().equals((short)2) || temp.getProduct().getType().equals((short)5)){
					SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					temp.setCheckTime(df.format(new Date()));
				}else{
					temp.setCheckTime(temp.getProduct().getFoundDate());
				}
				orderHistoryService.save(temp);
			}else{
				//修改订单
				this.update(order);
				temp=this.getOne(order);
			}
			//保存流水订单表
			orderTransactionDetailService.save(temp);
			//修改订单发送消息
			try{
				Map<String, Object> parameterMap = new HashMap<String, Object>();
				parameterMap.put("parameter_orderNumber", temp.getOrderNumber());
				List<UserMessage> receiver = new ArrayList<UserMessage>();
				UserMessage us = new UserMessage();
				us.setUserID(temp.getMemberID());
				us.setUserType((short)3);
				us.setUserName(temp.getMember().getName());
				receiver.add(us);
				this.getSendMessage(parameterMap, receiver, "1", "sys_manage_order_xgdd", user.getId(), Short.valueOf("1"),Short.valueOf("1"));
				this.getSendMessage(parameterMap, receiver, "2", "sms_manage_order_xgdd", user.getId(), Short.valueOf("1"),Short.valueOf("1"));
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 操作存续产品 接口
	 * @param order
	 * @throws Exception
	 */
	public void saveOrUpdateHodingProduct(Order order) throws Exception{
		if(order.getProductID() != null && order.getSubProductID()!=null && order.getContractNumber()!=null && !order.getContractNumber().equals("")){
			HoldingProduct holdingProduct=new HoldingProduct();
			holdingProduct.setProductID(order.getProductID());
			holdingProduct.setSubProductID(order.getSubProductID());
			holdingProduct.setContractNumber(order.getContractNumber());
			//通过子产品/产品/合同号查询存续产品是否存在
			holdingProduct=orderHoldingProductService.getOneByMidProId(holdingProduct);
			//获取产品信息  判断产品类型
			Product product=new Product();
			product.setId(order.getProductID());
			product=productService.getOne(product);
			//判断存续产品是否拥有
			if(holdingProduct==null || holdingProduct.getId()==null){
				holdingProduct=new HoldingProduct();
				//等于2 代表阳光私募 需要计算持有份额
				if(product.getType().equals((short)2)){
					//持有份额
					BigDecimal d=order.getPayAmount().multiply(BigDecimal.valueOf(10000));
					//判断订单 净值是否为空  如果为空 按1 计算
					if(order.getNetValue()!=null){
						//精度为四舍五入
						d=d.divide(order.getNetValue(),BigDecimal.ROUND_HALF_UP);
					}
					//持有份额追加
					holdingProduct.setShare(d);
				}else{
					//持有金额追加
					holdingProduct.setShare(order.getShare().multiply(BigDecimal.valueOf(1000000)));
				}
				//投资金额追加
				holdingProduct.setInvestAmount(order.getPayAmount());
				holdingProduct.setMemberID(order.getMemberID());
				holdingProduct.setProductID(order.getProductID());
				holdingProduct.setContractNumber(order.getContractNumber());
				holdingProduct.setOrgID(order.getOrgID());
				holdingProduct.setSubProductID(order.getSubProductID());
				holdingProduct.setTeamID(order.getTeamID());
				holdingProduct.setDeadline(order.getDeadline());
				holdingProduct.setClientType(order.getClientType());
				holdingProduct.setClientName(order.getClientName());
				holdingProduct.setiDCard(order.getiDCard());
				if(order.getClientType()==1){//个人客户
					holdingProduct.setiDCardType(order.getiDCardType());
				}else{
					holdingProduct.setiDCardType(null);
				}
				orderHoldingProductService.save(holdingProduct);
			}else{
				//等于2 代表阳光私募 需要计算持有份额
				if(product.getType().equals((short)2)){
					//持有份额 精度为四 舍 五入
					BigDecimal d=(order.getPayAmount().multiply(BigDecimal.valueOf(10000))).divide(order.getNetValue(),BigDecimal.ROUND_HALF_UP);
					//持有份额追加
					holdingProduct.setShare(holdingProduct.getShare().add(d));
				}
				//投资金额追加
				holdingProduct.setInvestAmount(holdingProduct.getInvestAmount().add(order.getPayAmount()));
				//修改 存续产品
				orderHoldingProductService.update(holdingProduct);
			}
		}
	}
	/**
	 * 平台确认打款收到
	 * @param entity
	 * @throws Exception
	 */
	public void updateConfirmPay(Order entity) throws Exception{
		super.getRelationDao().update("order.update_confirmPay", entity);
	}
	
	/**
	 * 平台方撤销订单
	 * @param order
	 * @throws Exception
	 */
	public void updateRepealOrder(Order order) throws Exception {
		User user=userLoginService.getCurrLoginUser();
		//平台方撤销状态 10
		order.setTradeStatus((short) 10);
		//修改订单
		this.update(order);
		//获取最新数据
		order=this.getOne(order);
		//保存流水订单表
		orderTransactionDetailService.save(order);
		//撤销订单发送消息
		try {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put("parameter_orderNumber", order.getOrderNumber());
			List<UserMessage> receiver = new ArrayList<UserMessage>();
			UserMessage us = new UserMessage();
			us.setUserID(order.getMemberID());
			us.setUserType((short)3);
			us.setUserName(order.getMember().getName());
			receiver.add(us);
			this.getSendMessage(parameterMap, receiver, "1", "sys_manage_order_xzcxdd", user.getId(), Short.valueOf("1"),Short.valueOf("1"));
			this.getSendMessage(parameterMap, receiver, "2", "sms_manage_order_xzcxdd", user.getId(), Short.valueOf("1"),Short.valueOf("1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置订单的销售状态
	 * @param order
	 * @throws Exception
	 */
	public void updateSetTradeStatus(Order order) throws Exception {
		User user=userLoginService.getCurrLoginUser();
		Order order2 = new Order();
		order2.setId(order.getId());
		if (order.getTradeStatus() != null) {
			order2.setTradeStatus(order.getTradeStatus());
			//判断是否为额度分配
			if(order2.getTradeStatus().equals((short)2)){
				//确认额度
				if(order.getShare()!=null){
					order2.setShare(order.getShare());
				}
				this.update(order2);
				
				Order order3=this.getOne(order2);
				//保存流水订单表
				orderTransactionDetailService.save(order3);
				//额度分配——消息
				try {
					Map<String, Object> parameterMap = new HashMap<String, Object>();
					parameterMap.put("parameter_orderNumber", order3.getOrderNumber());
					parameterMap.put("parameter_share", order3.getShare());
					parameterMap.put("parameter_orderID", order3.getId());
					List<UserMessage> receiver = new ArrayList<UserMessage>();
					UserMessage us = new UserMessage();
					us.setUserID(order3.getMemberID());
					us.setUserType((short)3);
					us.setUserName(order3.getMember().getName());
					receiver.add(us);
					this.getSendMessage(parameterMap, receiver, "1", "sys_manage_order_edfp", user.getId(), Short.valueOf("1"),Short.valueOf("1"));
					this.getSendMessage(parameterMap, receiver, "2", "sms_manage_order_edfp", user.getId(), Short.valueOf("1"),Short.valueOf("1"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//判断是否为单证上传/资金到账/单证上传&资金到账
			else if(order2.getTradeStatus().equals((short)4)){ //order2.getTradeStatus().equals((short)3) || order2.getTradeStatus().equals((short)5)
				this.update(order2);
				Order order3=this.getOne(order2);
				//保存流水订单表
				orderTransactionDetailService.save(order3);
			}
			//判断是否为设置成功订单/失败订单
			else if(order2.getTradeStatus().equals((short)7) || order2.getTradeStatus().equals((short)13)){
				Order order3=this.getOne(order2);
				order3.setTradeStatus(order2.getTradeStatus());
				order3.setBreakReason(order.getBreakReason());
				// 删除本条订单记录
				this.delete(order2);
				// 保存流水订单表
				orderTransactionDetailService.save(order3);
				// 保存成功（历史）订单表
				if(order3.getProduct().getType().equals((short)2) || order3.getProduct().getType().equals((short)5)){
					SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					order3.setCheckTime(df.format(new Date()));
				}else{
					order3.setCheckTime(order3.getProduct().getFoundDate());
				}
				orderHistoryService.save(order3);
				//如果成功  需要操作存续产品
				if(order2.getTradeStatus().equals((short)7)){
					this.saveOrUpdateHodingProduct(order3);
					//订单设置成功的消息
					try {
						Map<String, Object> parameterMap = new HashMap<String, Object>();
						parameterMap.put("parameter_orderNumber", order3.getOrderNumber());
						List<UserMessage> receiver = new ArrayList<UserMessage>();
						UserMessage us = new UserMessage();
						us.setUserID(order3.getMemberID());
						us.setUserType((short)3);
						us.setUserName(order3.getMember().getName());
						receiver.add(us);
						this.getSendMessage(parameterMap, receiver, "1", "sys_manage_order_success", user.getId(), Short.valueOf("1"),Short.valueOf("1"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					//订单设置失败的消息
					try {
						Map<String, Object> parameterMap = new HashMap<String, Object>();
						parameterMap.put("parameter_orderNumber", order3.getOrderNumber());
						List<UserMessage> receiver = new ArrayList<UserMessage>();
						UserMessage us = new UserMessage();
						us.setUserID(order3.getMemberID());
						us.setUserType((short)3);
						us.setUserName(order3.getMember().getName());
						receiver.add(us);
						this.getSendMessage(parameterMap, receiver, "1", "sys_manage_order_fail", user.getId(), Short.valueOf("1"),Short.valueOf("1"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * 发送消息基础方法
	 * @param parameterMap  模板替换内容
	 * @param receiver      接受者list
	 * @param sendway		发送方式
	 * @param templatecode  模板code
	 * @param senderID		发送者id
	 * @param sendertype	发送者类型
	 * @param relationtype	关联类型
	 * @return
	 * @throws Exception
	 */
	private void getSendMessage(Map<String,Object> parameterMap,List<UserMessage> receiver, String sendway,String templatecode,Long senderID,Short sendertype,Short relationtype) throws Exception {
		SendMessage sm = new SendMessage();
		//接收者LIST
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

	/**
	 * 当修改到账金额时，判断资金到账的状态是否需要改变
	 * @param order
	 * @throws Exception
	 */
	public void updatePayProgress(Order order) throws Exception{
		if(order!=null && order.getOrderNumber()!=null && !order.getOrderNumber().equals("") && order.getPayAmount()!=null){
			Order order2=this.getOneByNum(order);
			order2.setPayAmount(order.getPayAmount());
			order2.setPayTime(order.getPayTime());
			if(order2.getShare()!=null && !order2.getShare().equals("")){
				if(order2.getShare().compareTo(order.getPayAmount())==0){//判断分配金额和已到账金额差值
					order2.setPayProgress((short)2);
					//判断是否单证归集 或者 单证归集&资金到账
//					if(order2.getTradeStatus().equals((short)3) || order2.getTradeStatus().equals((short)5)){
//						order2.setTradeStatus((short) 5);
//					}else{
						order2.setTradeStatus((short) 4);
//					}
				}else if(order2.getShare().compareTo(order.getPayAmount())==1){
					order2.setPayProgress((short)1);
				}else if(order2.getShare().compareTo(order.getPayAmount())==-1){
					order2.setPayProgress((short)3);
					//判断是否单证归集
//					if(order2.getTradeStatus().equals((short)3) || order2.getTradeStatus().equals((short)5)){
//						order2.setTradeStatus((short) 5);
//					}else{
						order2.setTradeStatus((short) 4);
//					}
				}else if(order.getPayAmount().compareTo(BigDecimal.valueOf(0))==0){
					order2.setPayProgress((short)0);
				}
			}
			super.getRelationDao().update("order.update_payProgress", order2);
		}
	}
	
	/**
	 * 查询所有额度确认完成订单列表
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getOrderTradeStatusList(Order entity)throws Exception{
		Map<String,Object> map = new Hashtable<String,Object>();
		Long record = getOverOrderCount(entity);
		List<Order> list = getOverOrderList(entity);
		for (Order order : list) {
			order.setTradeTypeName(this.getInitData().getTradeType().get(0).get(order.getTradeType().toString()).toString());
			order.setTradeStatusName(this.getInitData().getTradeStatus().get(1).get(order.getTradeStatus().toString()).toString());
			order.setDocumentStatusName(this.getInitData().getDocumentStatus().get(order.getDocumentStatus().toString()).toString());
		}
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", list);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	/**
	 * 打印分页数据
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Order> getOverOrderList(Order entity) throws Exception{
		return (List<Order>)super.getRelationDao().selectList("order.select_tradestatus_2", entity);
	}
	
	public Order getOneOverOrder(Order entity) throws Exception{
		return (Order)super.getRelationDao().selectOne("order.select_tradestatus_2_one", entity);
	}
	
	public List<Order> getArrOverOrders(Order entity) throws Exception{
		List<Order> list = new ArrayList<Order>();
		if (entity != null && entity.getIds() != null) {
			for (String id : entity.getIds()) {
				entity.setId(Long.valueOf(id));
				list.add(this.getOneOverOrder(entity));
			}
		}
		return list;
	}
	
	/**
	 * 打印分页数据统计
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public Long getOverOrderCount(Order entity) throws Exception{
		return super.getRelationDao().getCount("order.select_tradestatus_count", entity);
	}
	
	/**
	 * 订单合同编号重复验证
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public Long checkForContractNumber(Order order) throws Exception{
		return getRelationDao().getCount("order.checkForContractNumber", order);
	}
	
	/**
	 * 获取认购订单中的  打款凭证信息
	 * @param order
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getHasOrNotDisposeGrid(Order order) throws Exception{
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Order> historyList =  (List<Order>) super.getRelationDao().selectList("order.selectHasOrNotDispose", order);
		Long record = super.getRelationDao().getCount("order.selectHasOrNotDispose_count", order);
		int pageCount = (int) Math.ceil(record / (double) order.getRows());
		map.put("rows", historyList);
		map.put("page", order.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	
	/**********               责任额度配给功能                                 ************/	
	
	@SuppressWarnings("unchecked")
	public List<Order> getNewPushShareList(Order entity) throws Exception{
		return (List<Order>)super.getRelationDao().selectList("order.select_newpushshare_list", entity);
	}
	
	public Long getNewPushShareCount(Order entity) throws Exception{
		return super.getRelationDao().getCount("order.select_newpushshare_count", entity);
	}
	
	/**
	 * 未责任额度配给列表
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getUnPushShareGrid(Order entity) throws Exception{
		Map<String,Object> map = new Hashtable<String,Object>();
		List<Order> pushShareList = getNewPushShareList(entity);
		Long record = getNewPushShareCount(entity);
		int pageCount = (int)Math.ceil(record / (double) entity.getRows());
		map.put("rows", pushShareList);
		map.put("page",entity.getPage());
		map.put("total", pageCount);
		map.put("records",record);
		return map;
	}
	
	/**
	 * 已责任额度配给列表
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getPushShareGrid(Order entity) throws Exception{
		Map<String,Object> map = new Hashtable<String,Object>();
		List<Order> pushShareList = (List<Order>)super.getRelationDao().selectList("order.select_pushshare_list", entity);
		Long record = super.getRelationDao().getCount("order.select_pushshare_count", entity);
		int pageCount = (int)Math.ceil(record / (double) entity.getRows());
		map.put("rows", pushShareList);
		map.put("page",entity.getPage());
		map.put("total", pageCount);
		map.put("records",record);
		return map;
	}
	
	/**
	 * 更新已分配份额
	 * @param entity
	 * @throws Exception
	 */
	public void updateShare(Order entity) throws Exception{
		super.getRelationDao().update("order.update_share", entity);
	}
	
	/**
	 * 输入配给额度点击确定操作
	 * @param entity
	 * @throws Exception
	 */
	public void updateShareByPushShare(Order entity) throws Exception{
		DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		entity.setPushTime(fmt.format(new Date()));
		entity.setPushStatus((short)1);
		//更新分配配给额度、配给状态、配给时间
		update(entity);
		//更新配给状态为待确认
		entity = getOne(entity);
		//保存流水订单表
		orderTransactionDetailService.save(entity);
		//配给额度发送消息
		try{
			pushShareMessage(entity);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 确认操作
	 * @param entity
	 * @throws Exception
	 */
	public void confirmShare(Order entity) throws Exception{
		//修改配给状态为已接受
		entity.setPushStatus((short)2);
		entity.setTradeStatus((short)2);
		update(entity);
		//取得当前id的investAmount(pushshare)的值(前台的investAmount和后台的pushshare一样)
		entity = getOne(entity);
		//确认已分配份额(将前台存的investAmount存到pushshare字段中)
		updateShare(entity);
		entity = getOne(entity);
		//保存流水订单表
		orderTransactionDetailService.save(entity);
		//退款金额
	//	BigDecimal money = entity.getPayAmount().subtract(entity.getInvestAmount());
		//退款操作
	}
	
	//拒绝操作发送消息
	public void refuse(Order order)throws Exception{
		order.setPushStatus((short)4);
		if(order.getPayProgress() == 0){
			//资金状态为未打款的则撤销
			order.setTradeStatus((short)10);
		}else{
			//否则取消中
			order.setTradeStatus((short)11);
		}
		update(order);
	}
	
	/**
	 * 查询预期收益率
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public ProductProfit countExpectProfitRatio(Order order) throws Exception{
		ProductProfit productProfit = new ProductProfit();
		productProfit.setSubProductId(order.getSubProductID());
		productProfit.setInvestAmount(order.getInvestAmount());
		return productProfitService.countExpectProfitRatio(productProfit);
	}
	
	/**
	 * 责任额度配给
	 * 
	 * @param order
	 * @throws Exception
	 */
	public void updatePushShare(Order order) throws Exception{
		//修改订单
		update(order);
		//获取订单数据 BYID
		order = getOne(order);
		//保存流水订单表
		orderTransactionDetailService.save(order);
		//配给额度发送消息
		try{
			pushShareMessage(order);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询申购有效订单  来进行流水对账
	 * @param order
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getNotMappOrderGrid(Order order) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Order> historyList =  (List<Order>) super.getRelationDao().selectList("order.getNotMappOrderGrid", order);
		Long record = super.getRelationDao().getCount("order.getNotMappOrderGrid_count", order);
		int pageCount = (int) Math.ceil(record / (double) order.getRows());
		map.put("rows", historyList);
		map.put("page", order.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	
	/**
	 * 查询申购成功的已对账订单
	 * @param order
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getHasMappOrderGrid(Order order) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<Order> historyList =  (List<Order>) super.getRelationDao().selectList("orderHistory.getHasMappOrderGrid", order);
		Long record = super.getRelationDao().getCount("orderHistory.getHasMappOrderGrid_count", order);
		int pageCount = (int) Math.ceil(record / (double) order.getRows());
		map.put("rows", historyList);
		map.put("page", order.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
	
	/**
	 * 匹配 订单打款与流水数据 并将订单设置成功
	 * @param payConfirmFromProvider
	 * @throws Exception
	 */
	public void updateOrderToHistory(PayConfirmFromProvider payConfirmFromProvider) throws Exception {
		Order order=new Order();
		order.setId(payConfirmFromProvider.getId());
		//通过ID获取订单数据
		order=this.getOne(order);
		order.setTradeStatus((short)7);
		payConfirmFromProvider.setProductId(order.getProductID());
		payConfirmFromProvider.setProviderId(order.getProviderID());
		payConfirmFromProvider.setName(order.getClientName());
		payConfirmFromProvider.setPayTime(order.getPayTime());
		payConfirmFromProvider.setMatchingStatus(1);
		payConfirmFromProvider.setOrderNumber(order.getOrderNumber());
		payConfirmFromProvider.setPayAmount(payConfirmFromProvider.getPayAmount().multiply(BigDecimal.valueOf(10000)));
		//保存供应商流水表数据
		bankWaterService.save(payConfirmFromProvider);
		//将订单设置成功
		this.updateSetTradeStatus(order);
		
	}
	
	/**
	 * 申购产品成功对账订单统计  导出 excel
	 * @param order
	 * @param colName
	 * @param colModel
	 * @param excel
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public WritableWorkbook exportsubscibeexcel(Order order, String colName,String colModel, WritableWorkbook excel) throws Exception {
		List<Map<String,Object>> objList = (List<Map<String,Object>>) super.getRelationDao().selectList("orderHistory.exportHasMappOrder",order);
		if (objList != null) {
			for (Map<String,Object> tempOrder : objList) {
				tempOrder.put("orderNumber", tempOrder.get("ORDERNUMBER"));
				tempOrder.put("contractNumber", tempOrder.get("CONTRACTNUMBER"));
				tempOrder.put("productShortName", tempOrder.get("PRODUCTSHORTNAME"));
				tempOrder.put("clientName", tempOrder.get("CLIENTNAME"));
				tempOrder.put("tradeStatusName", "成功订单");
				tempOrder.put("investAmount", tempOrder.get("INVESTAMOUNT"));
				tempOrder.put("share", tempOrder.get("SHARE"));
				tempOrder.put("payAmount", tempOrder.get("PAYAMOUNT"));
				tempOrder.put("proofCount", tempOrder.get("PROOFCOUNT").equals((long)0)?"无":"有");
				tempOrder.put("affirmAmount", tempOrder.get("AFFIRMAMOUNT"));
			}
		}
		String[] colNames = colName.split(",");
		String[] colModels = colModel.split(",");
		
		String excelName = "申购产品对账统计";
		int colNameNum = colNames.length ;
		int colModelNum = colModels.length;
		ExportExcel exportExcel = new ExportExcel();
		excel = exportExcel.createExcel(excelName, colNames, colNameNum, colModels, colModelNum, objList, excel);
		return excel;
	}
	
	/**
	 * 责任额度配给发送消息
	 * @param order
	 * @throws Exception
	 */
	public void pushShareMessage(Order order) throws Exception{
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("parameter_orderNumber", order.getOrderNumber());
		SendMessage sendMessage = new SendMessage();
		UserMessage us = new UserMessage();
		us.setUserID(order.getMemberID());
		us.setUserType((short)3);
		us.setUserName(order.getMember().getName());
		List<UserMessage> list = new ArrayList<UserMessage>();
		list.add(us);
		sendMessage.setUserList(list);
		//设置发送方式
		sendMessage.setSendWayStr("1");
		//设置模板参数
		sendMessage.setTemplateParameters(parameterMap);
		//设置模板code
		sendMessage.setTemplateCode("sys_manage_order_pjed");
		//设置发送者ID
		User user = userLoginService.getCurrLoginUser();
		sendMessage.setSendUserID(user.getId());
		//设置发送者类型
		sendMessage.setSendUserType((short)1);
		//发送消息
		commonMessageService.send(sendMessage);
	}
	
	
	/**
	 * 验证小额订单数量 
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> checkMinOrder(Order order) throws Exception {
		Map<String,Object> map=null;
		//获取订单信息
		order=this.getOne(order);
		Product product=new Product();
		product.setId(order.getProductID());
		//获取 产品数据
		product = productService.getList(product).get(0);
		
		//判断小额投资人数上限是否共享   1=共享
		if(product.getIsTotalLowAmountClientCount().equals((short)1)){
			//判断小额投资人数上限是否限制  !=-1为限制
			if(!product.getMaxLowAmountClientCount().equals(-1) && !product.getLowAmountThreshold().equals(-1)){
				//赋值给投资金额 为 小额金额
				order.setInvestAmount(product.getLowAmountThreshold());
				//根据限制的金额进行查询  小额订单的数量
				Long count=super.getRelationDao().getCount("order.select_lowamount_count", order);
				//判断 小额上限数与 小额数
				if(product.getMaxLowAmountClientCount()<=count){
					map=new HashMap<String, Object>();
					map.put("statu", "no");
				}
			}else{
				//共享不限制   无需做操作
			}
		}else{
			SubProduct subProduct=new SubProduct();
			subProduct.setId(order.getSubProductID());
			//通过子产品ID查询子产品
			subProduct = subProductService.getSubProduct(subProduct);
			//判断子产品是否限制
			if(!subProduct.getMaxLowAmountClientCount().equals((short)-1)){
				//赋值给投资金额 为 小额金额
				order.setInvestAmount(subProduct.getWarningShare());
				//根据限制的金额进行查询  小额订单的数量
				Long count=super.getRelationDao().getCount("order.select_lowamount_count", order);
				//判断 小额上限数与 小额数
				if(subProduct.getMaxLowAmountClientCount()<=count){
					map=new HashMap<String, Object>();
					map.put("statu", "no");
				}
			}
		}
		return map;
	}
	
	/**
	 * 将申购的取消订单转换为失败订单
	 * @param order
	 */
	public void updateFailDispose(Order order) throws Exception{
		if(order.getId()!=null){
			deleteOrderSaveHistory(order);
		}else if(order.getIds()!=null && order.getIds().length>0){
			for(String id:order.getIds()){
				order.setId(Long.parseLong(id));
				deleteOrderSaveHistory(order);
			}
		}
	}
	
	public void deleteOrderSaveHistory(Order order) throws Exception{
		Order order3=this.getOne(order);
		order3.setTradeStatus((short)13);
		// 删除本条订单记录
		this.delete(order);
		// 保存流水订单表
		orderTransactionDetailService.save(order3);
		// 保存成功（历史）订单表
		if(order3.getProduct().getType().equals((short)2) || order3.getProduct().getType().equals((short)5)){
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			order3.setCheckTime(df.format(new Date()));
		}else{
			order3.setCheckTime(order3.getProduct().getFoundDate());
		}
		orderHistoryService.save(order3);
	}

	/**
	 * 查询订单关联理财师单证邮寄地址
	 * 
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public Order getMemberContact(Order order) throws Exception {
		return (Order) super.getRelationDao().selectOne("order.select_member_contact", order);
	}

}
