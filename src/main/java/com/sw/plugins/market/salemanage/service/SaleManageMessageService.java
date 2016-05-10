package com.sw.plugins.market.salemanage.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.sw.core.exception.DetailException;
import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.market.salemanage.entity.SaleManageMessage;
import com.sw.plugins.message.messageinterface.service.CommonMessageService;
import com.sw.plugins.message.task.entity.SendMessage;
import com.sw.plugins.message.task.entity.UserMessage;

public class SaleManageMessageService extends CommonService<SaleManageMessage> {

	private static Logger logger = Logger.getLogger(SaleManageMessageService.class);

	@Resource
	private CommonMessageService commonMessageService;

	private final String PJKEY = "pj";

	/**
	 * 开启预约消息 接收者为：所有收藏了该产品的RM
	 * 
	 * @param salaManage
	 * @throws Exception
	 */
	public void handleOrderStatus(SaleManageMessage saleManageMessage) {
		try {
			String userId = saleManageMessage.getCreatorUserId();
			// 设置发送方式和对应的模板key:sendWay value:templateCode
			Map<String, String> routerMap = new HashMap<String, String>();
			routerMap.put("1", "sys_manage_market_kqyy");
			routerMap.put("2", "sms_manage_market_kqyy");
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			List<UserMessage> receiveList = this.getMemberFavorities(saleManageMessage);
			if (receiveList != null && receiveList.size() > 0) {
				saleManageMessage = this.getProductAndSubProductElements(saleManageMessage);
				parameterMap.put(PJKEY, saleManageMessage);
				this.messageSender(receiveList, parameterMap, routerMap, userId,0);
			}
		} catch (Exception e) {
			logger.error("SALEMANAGE-MSG-EXCEPTION:" + DetailException.expDetail(e, getClass()));
		}
	}

	/**
	 * 开启、关闭打款消息 接收者为：所有预约了该产品的RM
	 * 
	 * @param saleManageMessage
	 * @param opType
	 *            1:开启 0: 关闭
	 * @throws Exception
	 */
	public void handleRemittanceStatus(SaleManageMessage saleManageMessage, short opType) {
		try {
			String userId = saleManageMessage.getCreatorUserId();
			// 设置发送方式和对应的模板key:sendWay value:templateCode
			Map<String, String> routerMap = new HashMap<String, String>();
			if (opType == (short) 1) {
				routerMap.put("1", "sys_manage_market_kqdk");
				routerMap.put("2", "sms_manage_market_kqdk");
			} else {
				routerMap.put("1", "sys_manage_market_gbdk");
				routerMap.put("2", "sms_manage_market_gbdk");
			}
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			List<UserMessage> receiveList = this.getMemberOrdered(saleManageMessage);
			if (receiveList != null && receiveList.size() > 0) {
				saleManageMessage = this.getProductAndSubProductElements(saleManageMessage);
				parameterMap.put(PJKEY, saleManageMessage);
				this.messageSender(receiveList, parameterMap, routerMap, userId,0);
			}
		} catch (Exception e) {
			logger.error("SALEMANAGE-MSG-EXCEPTION:" + DetailException.expDetail(e, getClass()));
		}
	}

	/**
	 * 产品状态变更消息
	 * 
	 * @param saleManageMessage
	 * @param opType
	 *            1：产品封账 2：产品成立 3:发行失败 4：取消投放
	 * @throws Exception
	 */
	public void saveProductStatus(SaleManageMessage saleManageMessage, Map<String, Object> parameterMap, List<UserMessage> receiveList, short opType) {
		try {
			String userId = saleManageMessage.getCreatorUserId();
			// 设置发送方式和对应的模板key:sendWay value:templateCode
			Map<String, String> routerMap = new HashMap<String, String>();
			if (opType == (short) 1) {
				routerMap.put("1", "sys_manage_market_cpfz");
				routerMap.put("2", "sms_manage_market_cpfz");
			} else if (opType == (short) 2) {
				routerMap.put("1", "sys_manage_market_cpcl");
				routerMap.put("2", "sms_manage_market_cpcl");
			} else if (opType == (short) 3) {
				routerMap.put("1", "sys_manage_market_fxsb");
				routerMap.put("2", "sms_manage_market_fxsb");
			} else if (opType == (short) 4) {
				routerMap.put("1", "sys_manage_market_qxtf");
				routerMap.put("2", "sms_manage_market_qxtf");
			}
			if (receiveList != null && receiveList.size() > 0) {
				this.messageSender(receiveList, parameterMap, routerMap, userId,0);
			}
		} catch (Exception e) {
			logger.error("SALEMANAGE-MSG-EXCEPTION:" + DetailException.expDetail(e, getClass()));
		}
	}

	/**
	 * 额度分配消息
	 * 
	 * @param saleManageMessage
	 * @param opType
	 *            2: 修改配额1:分配额度 0：取消额度
	 * @throws Exception
	 */
	public void handleShare(SaleManageMessage saleManageMessage, short opType) {
		try {
			String userId = saleManageMessage.getCreatorUserId();
			// 设置发送方式和对应的模板key:sendWay value:templateCode
			Map<String, String> routerMap = new HashMap<String, String>();
			if (opType == (short) 1) {
				routerMap.put("1", "sys_manage_market_edfp");
				routerMap.put("2", "sms_manage_market_edfp");
			} else if (opType == (short) 2) {
				routerMap.put("1", "sys_manage_market_xgpe");
				routerMap.put("2", "sms_manage_market_xgpe");
			} else {
				routerMap.put("1", "sys_manage_market_qxpe");
				routerMap.put("2", "sms_manage_market_qxpe");
			}
			BigDecimal share = saleManageMessage.getShare();
			saleManageMessage = this.getOrderElements(saleManageMessage);
			if (saleManageMessage != null) {
				saleManageMessage.setShare(share);
				Map<String, Object> parameterMap = new HashMap<String, Object>();
				UserMessage um = this.getOrdersMember(saleManageMessage);
				if (um != null) {
					List<UserMessage> receiveList = new ArrayList<UserMessage>();
					receiveList.add(um);
					parameterMap.put(PJKEY, saleManageMessage);
					this.messageSender(receiveList, parameterMap, routerMap, userId,0);
				}
			}
		} catch (Exception e) {
			logger.error("SALEMANAGE-MSG-EXCEPTION:" + DetailException.expDetail(e, getClass()));
		}
	}

	/**
	 * 催款、退款消息
	 * 
	 * @param saleManageMessage
	 * @param opType
	 *            1:催款 0：退款
	 * @throws Exception
	 */
	public void handleFund(SaleManageMessage saleManageMessage, short opType) {
		try {
			String userId = saleManageMessage.getCreatorUserId();
			// 设置发送方式和对应的模板key:sendWay value:templateCode
			Map<String, String> routerMap = new HashMap<String, String>();
			if (opType == (short) 1) {
				routerMap.put("1", "sys_manage_market_ck");
				routerMap.put("2", "sms_manage_market_ck");
			} else {
				routerMap.put("1", "sys_manage_market_tk");
				routerMap.put("2", "sms_manage_market_tk");
			}
			saleManageMessage = this.getOrderElements(saleManageMessage);
			if (saleManageMessage != null) {
				Map<String, Object> parameterMap = new HashMap<String, Object>();
				UserMessage um = this.getOrdersMember(saleManageMessage);
				if (um != null) {
					List<UserMessage> receiveList = new ArrayList<UserMessage>();
					receiveList.add(um);
					parameterMap.put(PJKEY, saleManageMessage);
					this.messageSender(receiveList, parameterMap, routerMap, userId,1);
				}
			}
		} catch (Exception e) {
			logger.error("SALEMANAGE-MSG-EXCEPTION:" + DetailException.expDetail(e, getClass()));
		}
	}

	/**
	 * 发送消息
	 * @param receiveList
	 * @param parameterMap
	 * @param routerMap
	 * @param userId
	 * @param configType 0: 不配置接收者1:配置接收者
	 * @throws Exception
	 */
	public void messageSender(List<UserMessage> receiveList, Map<String, Object> parameterMap, Map<String, String> routerMap, String userId,int configType) throws Exception {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setUserList(receiveList);
		// 设置模板参数
		sendMessage.setTemplateParameters(parameterMap);
		// 设置发送者ID
		Long id = Long.parseLong(userId);
		sendMessage.setSendUserID(id);
		// 设置发送者类型
		sendMessage.setSendUserType((short) 1);
		Set<Map.Entry<String, String>> entrySet = routerMap.entrySet();
		for (Iterator<Map.Entry<String, String>> it = entrySet.iterator(); it.hasNext();) {
			Map.Entry<String, String> entry = (Entry<String, String>) it.next();
			// 设置发送方式
			sendMessage.setSendWayStr((String) entry.getKey());
			// 设置模板code，发送消息
			sendMessage.setTemplateCode((String) entry.getValue());
			if(configType == 1){
				commonMessageService.sendByConfigReceiver(sendMessage);
			}else{
				commonMessageService.send(sendMessage);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<UserMessage> getMemberFavorities(SaleManageMessage saleManageMessage) throws Exception {
		return (List<UserMessage>) super.getRelationDao().selectList("saleManageMessage.select_product_favorities", saleManageMessage);
	}

	@SuppressWarnings("unchecked")
	public List<UserMessage> getMemberOrdered(SaleManageMessage saleManageMessage) throws Exception {
		return (List<UserMessage>) super.getRelationDao().selectList("saleManageMessage.select_product_ordered", saleManageMessage);
	}

	public UserMessage getOrdersMember(SaleManageMessage saleManageMessage) throws Exception {
		return (UserMessage) super.getRelationDao().selectOne("saleManageMessage.select_orders_member", saleManageMessage);
	}

	/**
	 * @param saleManageMessage
	 *            productId
	 * @return
	 * @throws Exception
	 */
	public SaleManageMessage getProductElements(SaleManageMessage saleManageMessage) throws Exception {
		return (SaleManageMessage) super.getRelationDao().selectOne("saleManageMessage.select_product_elements", saleManageMessage);
	}

	/**
	 * @param saleManageMessage
	 *            subProductId
	 * @return
	 * @throws Exception
	 */
	public SaleManageMessage getOrderElements(SaleManageMessage saleManageMessage) throws Exception {
		return (SaleManageMessage) super.getRelationDao().selectOne("saleManageMessage.select_order_elements", saleManageMessage);
	}

	/**
	 * @param saleManageMessage
	 *            subProductId
	 * @return
	 * @throws Exception
	 */
	public SaleManageMessage getProductAndSubProductElements(SaleManageMessage saleManageMessage) throws Exception {
		return (SaleManageMessage) super.getRelationDao().selectOne("saleManageMessage.select_product_and_subproduct_elements", saleManageMessage);
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(SaleManageMessage entity) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(SaleManageMessage entity) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Long getRecordCount(SaleManageMessage entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SaleManageMessage> getList(SaleManageMessage entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SaleManageMessage> getPaginatedList(SaleManageMessage entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(SaleManageMessage entity) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteByArr(SaleManageMessage entity) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public SaleManageMessage getOne(SaleManageMessage entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getGrid(SaleManageMessage entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String upload(SaleManageMessage entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(SaleManageMessage entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
