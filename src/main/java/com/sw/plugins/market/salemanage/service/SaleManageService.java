package com.sw.plugins.market.salemanage.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.task.SimpleAsyncTaskExecutor;

import net.sf.json.JSONArray;

import com.sw.core.data.dbholder.CreatorContextHolder;
import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.market.order.entity.HoldingProduct;
import com.sw.plugins.market.order.entity.Order;
import com.sw.plugins.market.order.service.OrderHistoryService;
import com.sw.plugins.market.order.service.OrderHoldingProductService;
import com.sw.plugins.market.order.service.OrderService;
import com.sw.plugins.market.order.service.OrderTransactionDetailService;
import com.sw.plugins.market.salemanage.entity.SaleManage;
import com.sw.plugins.market.salemanage.entity.SaleManageMessage;
import com.sw.plugins.market.salemanage.entity.SaleManagePlot;
import com.sw.plugins.market.salemanage.service.SaleManageMessageTask.Methods;
import com.sw.plugins.message.task.entity.UserMessage;
import com.sw.plugins.product.manage.entity.Product;
import com.sw.plugins.product.manage.entity.SubProduct;
import com.sw.plugins.product.manage.service.ProductService;

public class SaleManageService extends CommonService<SaleManage> {

	@Resource
	private OrderHistoryService orderHistoryService;
	@Resource
	private OrderService orderService;
	@Resource
	private OrderTransactionDetailService orderTransactionDetailService;
	@Resource
	private OrderHoldingProductService orderHoldingProductService;
	@Resource
	private SaleManageMessageService saleManageMessageService;
	@Resource
	private SimpleAsyncTaskExecutor simpleAsyncTaskExecutor;
	@Resource
	private ProductService productService;

	final BigDecimal ZERO = BigDecimal.ZERO;

	/*------------------------------- 销售管控功能相关 --------------------------------*/

	/**
	 * 获取在途订单列表数据
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getNewOrdersGrid(SaleManage entity) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<SaleManage> resultList = this.getNewOrdersPaginatedList(entity);
		int record = 0;
		if (resultList != null) {
			//record = (long) this.getNewOrdersCount(entity);
			record = resultList.size();
		}
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}

	/**
	 * 获取小额订单管理新预约订单列表数据
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getLimitNewOrdersGrid(SaleManage saleManage) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<SaleManage> resultList = this.getOrdersWithTeam(saleManage);
		int record = 0;
		if (resultList != null) {
			int si = resultList.size();

			// 小额投资金额上限
			BigDecimal amountLimit = saleManage.getAmountLimit();
			Map<String, Integer> limitOrdersCountm = new HashMap<String, Integer>(si);
			Map<Long, Integer> rmCountc = new HashMap<Long, Integer>(si);
			Map<Long, BigDecimal> rmCounta = new HashMap<Long, BigDecimal>(si);
			Map<String, String> rmCountt = new HashMap<String, String>(si);
			// 遍历订单统计每组大额到账订单数、小额已配额订单数、大额到账明细，并保留小额订单
			for (int i = 0; i < si; i++) {
				SaleManage sm = resultList.get(i);
				Short teamType = sm.getTeamType();
				BigDecimal investAmount = sm.getInvestAmount();
				if (teamType != null) {
					Long teamId = sm.getTeamId();
					short payProgress = sm.getPayProgress();
					BigDecimal share = sm.getShare();
					BigDecimal payAmount = sm.getPayAmount();
					Long rmId = sm.getRmId();

					if (investAmount.compareTo(amountLimit) == -1) {
						// 小额已配额订单数
						if (share != null && share.compareTo(ZERO) == 1) {
							String mcKey = this.getLimitNewOrdersCountKey((short) 2, teamType, teamId);
							Integer moCount = limitOrdersCountm.get(mcKey);
							if (moCount == null) {
								moCount = 0;
							}
							moCount++;
							limitOrdersCountm.put(mcKey, moCount);
						}
					} else {
						// 大额到账订单数
						if (payProgress != (short) 0) {
							String lcKey = this.getLimitNewOrdersCountKey((short) 1, teamType, teamId);
							Integer loCount = limitOrdersCountm.get(lcKey);
							if (loCount == null) {
								loCount = 0;
							}
							loCount++;
							limitOrdersCountm.put(lcKey, loCount);
							// 理财师大额订单统计
							Integer rmCs = rmCountc.get(rmId);
							if (rmCs == null) {
								rmCs = 0;
							}
							rmCs++;
							rmCountc.put(rmId, rmCs);
							BigDecimal rmCa = rmCounta.get(rmId);
							if (rmCa == null) {
								rmCa = ZERO;
							}
							rmCa = rmCa.add(payAmount);
							rmCounta.put(rmId, rmCa);

							// 记录当前组别理财师
							if (rmCs != 0 || !(ZERO).equals(rmCa)) {
								String rmIdKey = this.getLimitNewOrdersCountKey((short) 3, teamType, teamId);
								String ids = rmCountt.get(rmIdKey);
								String newIds = getRmIds(ids, rmId);
								rmCountt.put(rmIdKey, newIds);
							}
						}
					}
				}
				// 清除大额订单非新预约订单
				Short tradeStatus = sm.getTradeStatus();
				if (tradeStatus != (short)1 || investAmount.compareTo(amountLimit) == 1 || investAmount.compareTo(amountLimit) == 0) {
					resultList.remove(i);
					si--;
					i--;
				}
			}

			// 再次遍历插入每条统计结果
			for (SaleManage sm : resultList) {
				Short teamType = sm.getTeamType();
				if (teamType == null) {
					continue;
				} else {
					Long teamId = sm.getTeamId();
					// 大额到账订单数
					String lcKey = this.getLimitNewOrdersCountKey((short) 1, teamType, teamId);
					Integer loCount = limitOrdersCountm.get(lcKey);
					if (loCount == null) {
						loCount = 0;
					}
					sm.setLargePayedCount(loCount.toString());
					// 小额已配额订单数
					String mcKey = this.getLimitNewOrdersCountKey((short) 2, teamType, teamId);
					Integer moCount = limitOrdersCountm.get(mcKey);
					if (moCount == null) {
						moCount = 0;
					}
					sm.setSmallSharedCount(moCount.toString());
					// 大额到账明细
					String rmName = sm.getRmName();
					String rmIdKey = this.getLimitNewOrdersCountKey((short) 3, teamType, teamId);
					String sid = rmCountt.get(rmIdKey);
					if (sid != null) {
						String[] ids = sid.split(",");
						StringBuffer bf = new StringBuffer();
						for (String id : ids) {
							if ("".equals(id)) {
								continue;
							}
							Long rmId = Long.parseLong(id);
							Integer rmCs = rmCountc.get(rmId);
							if (rmCs == null) {
								rmCs = 0;
							}
							BigDecimal rmCa = rmCounta.get(rmId);
							if (rmCa == null) {
								rmCa = ZERO;
							}
							rmCa = rmCa.setScale(2, BigDecimal.ROUND_DOWN);
							bf.append(rmName);
							bf.append("[");
							bf.append(rmCs);
							bf.append("/");
							bf.append(rmCa);
							bf.append("]");
							bf.append("</br>");
						}
						sm.setLargeOrderDetail(bf.toString());
					}
				}
			}
			record = resultList.size();
		}

		int pageCount = (int) Math.ceil(record / (double) saleManage.getRows());
		map.put("rows", resultList);
		map.put("page", saleManage.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}

	/**
	 * 获取统计时用到的KEY 1：大额到账订单数、2：小额已配额订单数、3：大额到账明细
	 * 
	 * @param type
	 * @param teamType
	 * @param teamId
	 * @return
	 * @throws Exception
	 */
	private String getLimitNewOrdersCountKey(int type, short teamType, Long teamId) throws Exception {
		StringBuffer bf = new StringBuffer(10);
		if (teamType == (short) 0) {
			bf.append("A-");
		} else {
			bf.append("B-");
		}
		bf.append(teamId);
		bf.append("-");
		bf.append(type);
		return bf.toString();
	}

	/**
	 * 获取统计时用到的KEY 1：大额到账订单数、2：小额已配额订单数、3：大额到账明细
	 * 
	 * @param type
	 * @param teamType
	 * @param teamId
	 * @return
	 * @throws Exception
	 */
	private String getRmIds(String ids, Long rmId) throws Exception {
		StringBuffer bf = new StringBuffer(10);
		if (ids != null) {
			bf.append(ids);
		}
		bf.append(",");
		bf.append(rmId);
		return bf.toString();
	}

	/**
	 * 获取在途订单
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<SaleManage> getNewOrdersPaginatedList(SaleManage entity) throws Exception {
		return (List<SaleManage>) super.getRelationDao().selectList("saleManage.select_new_order", entity);
	}

	public Long getNewOrdersCount(SaleManage order) throws Exception {
		return (long) super.getRelationDao().getCount("saleManage.count_new_order", order);
	}

	/**
	 * 获取已配额订单列表数据
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getSharedOrdersGrid(SaleManage entity) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<SaleManage> resultList = this.getSharedOrdersPaginatedList(entity);
		int record = 0;
		Long totalShared = 0L;
		if (resultList != null) {
			//record = (long) this.getSharedOrdersCount(entity);
			record = resultList.size();
			totalShared = (long) this.getSharedOrdersAmount(entity);
		}
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		Map<String, Long> userMap = new HashMap<String, Long>();
		userMap.put("totalshared", totalShared);
		map.put("userdata", userMap);
		return map;
	}

	/**
	 * 获取已配额订单
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<SaleManage> getSharedOrdersPaginatedList(SaleManage entity) throws Exception {
		return (List<SaleManage>) super.getRelationDao().selectList("saleManage.select_shared_order", entity);
	}

	public Long getSharedOrdersCount(SaleManage order) throws Exception {
		return (long) super.getRelationDao().getCount("saleManage.count_shared_order", order);
	}

	public Long getSharedOrdersAmount(SaleManage order) throws Exception {
		return (long) super.getRelationDao().getCount("saleManage.count_shared_order_amount", order);
	}

	/**
	 * 获取已到账订单列表数据
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getPayedOrdersGrid(SaleManage entity) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<SaleManage> resultList = this.getPayedOrdersPaginatedList(entity);
		int record = 0;
		Long totalPayAmount = 0L;
		if (resultList != null) {
			//record = (long) this.getPayedOrdersCount(entity);
			record = resultList.size();
			totalPayAmount = (long) this.getPayedOrdersAmount(entity);
		}
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		Map<String, Long> userMap = new HashMap<String, Long>();
		userMap.put("totalpayed", totalPayAmount);
		map.put("userdata", userMap);
		return map;
	}

	/**
	 * 获取已到账订单
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<SaleManage> getPayedOrdersPaginatedList(SaleManage entity) throws Exception {
		return (List<SaleManage>) super.getRelationDao().selectList("saleManage.select_payed_order", entity);
	}

	public Long getPayedOrdersCount(SaleManage order) throws Exception {
		return (long) super.getRelationDao().getCount("saleManage.count_payed_order", order);
	}

	public Long getPayedOrdersAmount(SaleManage order) throws Exception {
		return (long) super.getRelationDao().getCount("saleManage.count_payed_order_amount", order);
	}

	/**
	 * 更新配额和交易状态
	 * 
	 * @param saleManage
	 * @throws Exception
	 */
	public void updateShareAndTradeStatus(SaleManage saleManage, Short tradeStatus) throws Exception {
		Long orderId = saleManage.getOrderId();
		BigDecimal share = saleManage.getShare();
		short opType = (short) 1;
		if (orderId != null) {
			// 变更订单信息
			saleManage.setTradeStatus(tradeStatus);
			super.getRelationDao().update("saleManage.update_share_and_tradestatus", saleManage);
			// 记录操作流水
			Order to = new Order();
			to.setId(orderId);
			Order order = orderService.getOne(to);
			if(order != null){
				orderTransactionDetailService.save(order);
			}
			if (share == null || share.equals(ZERO)) {
				opType = (short) 0;
			}
			// 通知
			SaleManageMessage saleManageMessage = new SaleManageMessage();
			saleManageMessage.setOrderId(orderId);
			saleManageMessage.setShare(share);
			saleManageMessage.setCreatorUserId(CreatorContextHolder.getCreatorContext());

			// saleManageMessageService.handleShare(saleManageMessage, opType);
			simpleAsyncTaskExecutor.submit(new SaleManageMessageTask(saleManageMessageService, Methods.HANDLESHARE, saleManageMessage, opType));
		}
	}

	/**
	 * 更新配额
	 * 
	 * @param saleManage
	 * @param msgType
	 *            2: 修改配额1:分配额度 0：取消额度
	 * @throws Exception
	 */
	public void updateShare(SaleManage saleManage, short msgType) throws Exception {
		Long orderId = saleManage.getOrderId();
		if (orderId != null) {
			// 订单处理类型
			short processType = saleManage.getProcessType();
			BigDecimal share = saleManage.getShare();

			// 处理到账订单
			if (processType == 1) {
				/**
				 * -- 暂时停用 SaleManage sm = (SaleManage)
				 * super.getRelationDao().selectOne
				 * ("saleManage.select_trade_info", saleManage); if (sm != null)
				 * { BigDecimal payAmount = sm.getPayAmount(); payAmount =
				 * payAmount.divide(new BigDecimal(1000000)); //
				 * 分配额度等于到账金额则修改订单为全款到账状态 if (payAmount.compareTo(share) == 0) {
				 * Short tradeStatus = sm.getTradeStatus(); // 资金全部到账
				 * saleManage.setPayProgress((short) 3);
				 * saleManage.setTradeStatus(tradeStatus); // 未归集单证 if
				 * (tradeStatus.equals((short) 2)) {
				 * saleManage.setTradeStatus((short) 4); } // 已归集单证 else if
				 * (tradeStatus.equals((short) 3)) {
				 * saleManage.setTradeStatus((short) 5); }
				 * super.getRelationDao().update(
				 * "saleManage.update_share_tradestatus_and_payprogress",
				 * saleManage); }else{
				 * super.getRelationDao().update("saleManage.update_share",
				 * saleManage); }
				 * 
				 * }
				 **/
			}
			// 处理未到账订单
			else {
				super.getRelationDao().update("saleManage.update_share", saleManage);
				// 记录操作流水
				Order to = new Order();
				to.setId(orderId);
				Order order = orderService.getOne(to);
				if(order != null){
					orderTransactionDetailService.save(order);
				}
			}
			// 通知
			SaleManageMessage saleManageMessage = new SaleManageMessage();
			saleManageMessage.setOrderId(orderId);
			saleManageMessage.setShare(share);
			saleManageMessage.setCreatorUserId(CreatorContextHolder.getCreatorContext());
			// saleManageMessageService.handleShare(saleManageMessage, msgType);
			simpleAsyncTaskExecutor.submit(new SaleManageMessageTask(saleManageMessageService, Methods.HANDLESHARE, saleManageMessage, msgType));
		}
	}

	/**
	 * 更新主产品销售状态
	 * 
	 * @param saleManage
	 * @throws Exception
	 */
	public void updateProductSellStatus(SaleManage saleManage, Short sellStatus) throws Exception {

		// 产品成立
		if (sellStatus.equals((short) 5)) {
			this.updateProductToFounded(saleManage);
		}
		// 发行失败
		else if (sellStatus.equals((short) 4)) {
			this.updateProductToUnFounded(saleManage);
		}
		// 产品封账
		else if (sellStatus.equals((short) 9)) {
			this.updateProductToClosed(saleManage);
		}
		// 取消投放
		else if (sellStatus.equals((short) 8)) {
			this.updateProductToCanceled(saleManage);
		}
		// 仅更新产品状态
		else if (saleManage.getProductId() != null) {
			saleManage.setSellStatus(sellStatus);
			super.getRelationDao().update("saleManage.update_product_sellstatus", saleManage);
		}
	}

	/**
	 * 设置产品成立
	 * 
	 * 订单状态为'单证归集&资金到账[tradestatus:5]'状态下的订单写入历史表
	 * 
	 * @param saleManage
	 * @throws Exception
	 */
	public void updateProductToFounded(SaleManage saleManage) throws Exception {

		// 更新产品止期和成产状态
		saleManage.setSellStatus((short) 5);
		super.getRelationDao().update("saleManage.update_product_sellstatus_founddate_and_stopdate", saleManage);

		Long productId = saleManage.getProductId();
		if (productId == null) {
			return;
		}

		// 发送消息-准备数据
		SaleManageMessage saleManageMessage = new SaleManageMessage();
		saleManageMessage.setProductId(productId);
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		List<UserMessage> receiveList = saleManageMessageService.getMemberOrdered(saleManageMessage);
		if (receiveList != null && receiveList.size() > 0) {
			saleManageMessage = saleManageMessageService.getProductElements(saleManageMessage);
			parameterMap.put("pj", saleManageMessage);
		}

		// 根据产品ID 获取产品类型
		Product product = new Product();
		product.setId(productId);
		product = productService.getOne(product);

		Order order = new Order();
		order.setProductID(productId);
		List<Order> orderList = orderService.getList(order);
		// 处理订单
		if (orderList != null) {
			for (Order torder : orderList) {
				// 1记录订单历史
				Short tradeStatus = torder.getTradeStatus();
				Short payProgress = torder.getPayProgress();
				if (tradeStatus != null && payProgress != null && tradeStatus.equals((short) 4) && payProgress.equals((short) 2)) {
					// 设置为成功订单状态
					torder.setTradeStatus((short) 7);
					// 设置存续产品数据
					HoldingProduct holdingProduct = new HoldingProduct();
					holdingProduct.setContractNumber(torder.getContractNumber());
					holdingProduct.setOrgID(torder.getOrgID());
					holdingProduct.setProductID(torder.getProductID());
					holdingProduct.setSubProductID(torder.getSubProductID());
					holdingProduct.setTeamID(torder.getTeamID());
					holdingProduct.setMemberID(torder.getMemberID());
					// 私募产品 需要将持有份额单位进行转换
					if (product != null && product.getType() != null && product.getType().equals((short) 2)) {
						holdingProduct.setShare(torder.getShare().multiply(BigDecimal.valueOf(10000)));
					} else {
						holdingProduct.setShare(torder.getShare().multiply(BigDecimal.valueOf(1000000)));
					}
					holdingProduct.setInvestAmount(torder.getInvestAmount());
					holdingProduct.setDeadline(torder.getDeadline());
					holdingProduct.setClientType(torder.getClientType());
					holdingProduct.setClientName(torder.getClientName());
					holdingProduct.setiDCardType(torder.getiDCardType());
					holdingProduct.setiDCard(torder.getiDCard());
					holdingProduct.setPurchaseTime(torder.getOrderTime());
					holdingProduct.setLastTradeTime(torder.getOrderTime());
					holdingProduct.setRemark(torder.getRemark());
					// 成功订单产品记录插入'存续产品表'
					orderHoldingProductService.save(holdingProduct);
				} else {
					// 设置失败订单状态
					torder.setTradeStatus((short) 13);
					// 处理爽约订单 1:配给未完成2：全款到账撤消
					Short pushStatus = torder.getPushStatus();
					if(pushStatus == (short)2 && (tradeStatus == (short)9 || tradeStatus == (short)11)){
						torder.setBreakReason((short)1);
					}
					// 2：全款到账撤消
					else if(payProgress == (short)2 && (tradeStatus == (short)9 || tradeStatus == (short)11)){
						torder.setBreakReason((short)2);
					}
				}
				// 设置产品核对时间(信托类产品为产品成立日期)
				String fundDate = product.getFoundDate();
				torder.setCheckTime(fundDate);
				// 记录订单历史
				orderHistoryService.save(torder);
				// 2记录操作流水
				orderTransactionDetailService.save(torder);
				// 3删除原始订单
				orderService.delete(torder);
			}
		}

		// 发送消息
		// saleManageMessageService.saveProductStatus(parameterMap,receiveList,
		// (short) 2);
		SaleManageMessage ms = new SaleManageMessage();
		ms.setCreatorUserId(CreatorContextHolder.getCreatorContext());
		simpleAsyncTaskExecutor.submit(new SaleManageMessageTask(saleManageMessageService, Methods.SAVEPRODUCTSTATUS, ms, parameterMap, receiveList, (short) 2));

	}

	/**
	 * 设置产品发行失败
	 * 
	 * 产品发行失败[sellstatus:4]同时变更订单状态为‘产品未成立被取消[tradestatus:8]’
	 * 
	 * @param saleManage
	 * @throws Exception
	 */
	public void updateProductToUnFounded(SaleManage saleManage) throws Exception {

		// 更新产品销售状态为发行失败
		saleManage.setSellStatus((short) 4);
		super.getRelationDao().update("saleManage.update_product_sellstatus", saleManage);

		Long productId = saleManage.getProductId();
		if (productId == null) {
			return;
		}

		// 发送消息-准备数据
		SaleManageMessage saleManageMessage = new SaleManageMessage();
		saleManageMessage.setProductId(productId);
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		List<UserMessage> receiveList = saleManageMessageService.getMemberOrdered(saleManageMessage);
		if (receiveList != null && receiveList.size() > 0) {
			saleManageMessage = saleManageMessageService.getProductElements(saleManageMessage);
			parameterMap.put("pj", saleManageMessage);
		}

		Order order = new Order();
		order.setProductID(productId);
		List<Order> orderList = orderService.getList(order);
		// 处理订单
		for (Order torder : orderList) {
			// 1记录订单历史
			torder.setTradeStatus((short) 8);
			orderHistoryService.save(torder);
			// 2记录操作流水
			orderTransactionDetailService.save(torder);
			// 3删除原始订单
			orderService.delete(torder);
		}
		// 发送消息
		// saleManageMessageService.saveProductStatus(parameterMap,receiveList,
		// (short) 3);
		SaleManageMessage ms = new SaleManageMessage();
		ms.setCreatorUserId(CreatorContextHolder.getCreatorContext());
		simpleAsyncTaskExecutor.submit(new SaleManageMessageTask(saleManageMessageService, Methods.SAVEPRODUCTSTATUS, ms, parameterMap, receiveList, (short) 3));
	}

	/**
	 * 设置产品发行取消
	 * 
	 * 产品发行失败[sellstatus:8]同时变更订单状态为‘产品未成立被取消[tradestatus:8]’
	 * 
	 * @param saleManage
	 * @throws Exception
	 */
	public void updateProductToCanceled(SaleManage saleManage) throws Exception {
		Long productId = saleManage.getProductId();
		if (productId == null) {
			return;
		}

		// 发送消息-准备数据
		SaleManageMessage saleManageMessage = new SaleManageMessage();
		saleManageMessage.setProductId(productId);
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		List<UserMessage> receiveList = saleManageMessageService.getMemberOrdered(saleManageMessage);
		if (receiveList != null && receiveList.size() > 0) {
			saleManageMessage = saleManageMessageService.getProductElements(saleManageMessage);
			parameterMap.put("pj", saleManageMessage);
		}

		// 更新产品销售状态为发行失败
		saleManage.setSellStatus((short) 8);
		super.getRelationDao().update("saleManage.update_product_sellstatus", saleManage);

		Order order = new Order();
		order.setProductID(productId);
		List<Order> orderList = orderService.getList(order);
		// 处理订单
		for (Order torder : orderList) {
			// 1记录订单历史
			torder.setTradeStatus((short) 8);
			orderHistoryService.save(torder);
			// 2记录操作流水
			orderTransactionDetailService.save(torder);
			// 3删除原始订单
			orderService.delete(torder);
		}

		// 发送消息
		// saleManageMessageService.saveProductStatus(parameterMap,receiveList,
		// (short) 4);
		SaleManageMessage ms = new SaleManageMessage();
		ms.setCreatorUserId(CreatorContextHolder.getCreatorContext());
		simpleAsyncTaskExecutor.submit(new SaleManageMessageTask(saleManageMessageService, Methods.SAVEPRODUCTSTATUS, ms, parameterMap, receiveList, (short) 4));

	}

	/**
	 * 设置产品封账
	 * 
	 * 产品封账 停止所有子产品预约
	 * 
	 * @param saleManage
	 * @throws Exception
	 */
	public void updateProductToClosed(SaleManage saleManage) throws Exception {
		Long productId = saleManage.getProductId();
		if (productId == null) {
			return;
		}

		// 发送消息-准备数据
		SaleManageMessage saleManageMessage = new SaleManageMessage();
		saleManageMessage.setProductId(productId);
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		List<UserMessage> receiveList = saleManageMessageService.getMemberOrdered(saleManageMessage);
		if (receiveList != null && receiveList.size() > 0) {
			saleManageMessage = saleManageMessageService.getProductElements(saleManageMessage);
			parameterMap.put("pj", saleManageMessage);
		}

		// 关闭预约
		saleManage.setIsTotalShare((short) 1);// 操作所有子产品
		this.updateProductIsOrder(saleManage, (short) 0);
		// 更新产品销售状态为封并设置封闭日期
		saleManage.setSellStatus((short) 9);
		super.getRelationDao().update("saleManage.update_product_sellstatus_and_raisefinishtime", saleManage);
		// 发送消息
		// saleManageMessageService.saveProductStatus(parameterMap,receiveList,
		// (short) 1);
		SaleManageMessage ms = new SaleManageMessage();
		ms.setCreatorUserId(CreatorContextHolder.getCreatorContext());
		simpleAsyncTaskExecutor.submit(new SaleManageMessageTask(saleManageMessageService, Methods.SAVEPRODUCTSTATUS, ms, parameterMap, receiveList, (short) 1));
	}

	/**
	 * 更新产品是否可打款状态（同时变更产品销售状态为'在售--2'）预约状态为停止预约[isOrder:0]
	 * 
	 * @param saleManage
	 * @throws Exception
	 */
	public void updateProductIsRemittance(SaleManage saleManage, Short isRemittance) throws Exception {
		saleManage.setIsRemittance(isRemittance);
		super.getRelationDao().update("saleManage.update_product_is_remittance", saleManage);
		short opType = 0;
		if (isRemittance.equals((short) 1)) {
			saleManage.setSellStatus((short) 2);
			super.getRelationDao().update("saleManage.update_product_sellstatus", saleManage);
			opType = 1;
		}
		// 发送消息
		SaleManageMessage saleManageMessage = new SaleManageMessage();
		saleManageMessage.setProductId(saleManage.getProductId());
		saleManageMessage.setSubProductId(saleManage.getSubProductId());
		saleManageMessage.setCreatorUserId(CreatorContextHolder.getCreatorContext());
		// saleManageMessageService.handleRemittanceStatus(saleManageMessage,
		// opType);
		simpleAsyncTaskExecutor.submit(new SaleManageMessageTask(saleManageMessageService, Methods.HANDLEREMITTANCESTATUS, saleManageMessage, opType));
	}

	/**
	 * 更新产品是否可收藏状态
	 * 
	 * @param saleManage
	 * @throws Exception
	 */
	public void updateProductIsFavorites(SaleManage saleManage, Short isFavorites) throws Exception {
		saleManage.setIsFavorites(isFavorites);
		super.getRelationDao().update("saleManage.update_product_is_favorites", saleManage);
	}

	/**
	 * 更新产品是否可预约状态
	 * 
	 * @param saleManage
	 * @throws Exception
	 */
	public void updateProductIsOrder(SaleManage saleManage, Short isOrder) throws Exception {
		saleManage.setIsOrder(isOrder);
		super.getRelationDao().update("saleManage.update_product_is_order", saleManage);
		if (isOrder.equals((short) 1)) {
			SaleManageMessage saleManageMessage = new SaleManageMessage();
			saleManageMessage.setProductId(saleManage.getProductId());
			saleManageMessage.setSubProductId(saleManage.getSubProductId());
			saleManageMessage.setCreatorUserId(CreatorContextHolder.getCreatorContext());
			// saleManageMessageService.handleOrderStatus(saleManageMessage);
			simpleAsyncTaskExecutor.submit(new SaleManageMessageTask(saleManageMessageService, Methods.HANDLEORDERSTATUS, saleManageMessage));
		}
	}

	/**
	 * 更新产品是否可打款可预约状态（同时变更产品销售状态为'成立且在售--6')
	 * 
	 * @param saleManage
	 * @throws Exception
	 */
	public void updateOrderAndRemittance(SaleManage saleManage, Short isOrderAndRemittance) throws Exception {
		Long productId = saleManage.getProductId();
		if (productId == null) {
			return;
		}
		// 变更打款和预约状态
		super.getRelationDao().update("saleManage.update_product_is_order_and_remittance", saleManage);

		// 关闭时产品状态为成立，且关闭追加和赎回
		if (isOrderAndRemittance.equals((short) 0)) {
			// 同时变更追加和赎回状态
			saleManage.setSellStatus((short) 5);
			saleManage.setIsRedeem((short) 0);
			saleManage.setIsAdd((short) 0);
			super.getRelationDao().update("saleManage.update_product_sellstatus_isredeem_and_isadd", saleManage);
		}
		// 开放时产品状态为成立且在售
		else {
			// 设置产品状态
			saleManage.setSellStatus((short) 6);
			super.getRelationDao().update("saleManage.update_product_sellstatus", saleManage);
		}
	}

	/**
	 * 分配份额
	 * 
	 * @param saleManage
	 * @throws Exception
	 */
	public void distributeAllShare(SaleManage saleManage) throws Exception {
		List<SaleManage> orderList = saleManage.getSaleManageList();
		SaleManage ts;
		Short processType;
		Long id;
		if (orderList != null) {
			for (int i = 0; i < orderList.size(); i++) {
				id = orderList.get(i).getOrderId();
				if (id != null) {
					processType = orderList.get(i).getProcessType();
					ts = orderList.get(i);
					BigDecimal share = ts.getShare();
					BigDecimal reFund = ts.getReFund();
					ts.setProcessType(processType);
					// 处理已配额未到账订单,修改分配额度，并发送通知
					if (processType == 2) {
						updateShare(ts, (short) 1);
					}
					// 处理已到账订单,修改分配额度，如有退款提交退款申请，并发送通知
					else if (processType == 1) {
						// 停用已到账订单修改额度功能
						// updateShare(ts, (short) 2);
						// 退款通知
						if (reFund != null) {
							SaleManageMessage saleManageMessage = new SaleManageMessage();
							saleManageMessage.setOrderId(id);
							// saleManageMessageService.handleFund(saleManageMessage,
							// (short) 0);
							saleManageMessage.setCreatorUserId(CreatorContextHolder.getCreatorContext());
							simpleAsyncTaskExecutor.submit(new SaleManageMessageTask(saleManageMessageService, Methods.HANDLEFUND, saleManageMessage, (short) 0));
						}
					}
					// 处理新配额订单
					else if (processType == 0) {
						if (share == null || share.equals(BigDecimal.ZERO)) {
							continue;
						}
						// 订单交易状态置为'份额确认'
						updateShareAndTradeStatus(ts, (short) 2);
					}
				}
			}
		}
	}

	/**
	 * 主产品图表统计
	 * 
	 * @param plot
	 * @return
	 * @throws Exception
	 */
	public SaleManagePlot productStatisticsPlot(SaleManage saleManage) throws Exception {
		SaleManagePlot plot = new SaleManagePlot();

		/* -- 订单总额 -- */
		BigDecimal orderTotal = ZERO;
		BigDecimal orderTotalShared = ZERO;
		/* -- 到账金额 -- */
		BigDecimal orderTotalPayed = ZERO;
		BigDecimal orderTotalCorrectPayed = ZERO;
		/* -- 订单总数 -- */
		long orderTotalCount = 0;
		long orderTotalSharedCount = 0;
		long orderTotalPayedCount = 0;
		/* -- 小额总额 -- */
		BigDecimal orderTotalLimit = ZERO;
		/* -- 单证归集数 -- */
		BigDecimal orderTotalLimitPayedShared = ZERO;
		BigDecimal orderTotalLimitUnPayedShared = ZERO;
		long orderTotalCorrectPayedCount = 0;
		long orderTotalCorrectCollectCount = 0;

		// 小额投资金额限制 如果不限额则不统计小额投资金额总计
		BigDecimal investLimitAmount = saleManage.getAmountLimit();
		if (investLimitAmount.equals(new BigDecimal(-1))) {
			orderTotalLimit = new BigDecimal(-1);
		}

		List<SaleManage> listProductOrders = this.getPruductOrders(saleManage);
		if (listProductOrders != null) {
			for (int i = 0; i < listProductOrders.size(); i++) {
				SaleManage sm = listProductOrders.get(i);
				BigDecimal investAmount = sm.getInvestAmount();
				if (investAmount == null) {
					investAmount = ZERO;
				}
				BigDecimal share = sm.getShare();
				if (share == null) {
					share = ZERO;
				}
				BigDecimal payAmount = sm.getPayAmount();
				if (payAmount == null) {
					payAmount = ZERO;
				}
				Short docStatus = sm.getDocStatus();
				if (docStatus == null) {
					docStatus = (short) -1;
				}
				// 计算订单总金额
				orderTotal = orderTotal.add(investAmount);
				// 计算订单总配额
				orderTotalShared = orderTotalShared.add(share);
				// 计算订单到账金额
				orderTotalPayed = orderTotalPayed.add(payAmount);
				// 计算到账情况
				if (payAmount.compareTo(ZERO) == 1) {
					orderTotalPayedCount++;
					int co = payAmount.compareTo(investAmount);
					if (co == 1 || co == 0) {
						orderTotalCorrectPayed = orderTotalCorrectPayed.add(investAmount);
					}
				}
				// 已配额订单总数
				if (share.compareTo(ZERO) == 1) {
					orderTotalSharedCount++;
				}
				// 统计已锁定单证归集情况
				if (payAmount.equals(investAmount)) {
					orderTotalCorrectPayedCount++;
					if (docStatus.equals((short) 1)) {
						orderTotalCorrectCollectCount++;
					}
				}
				// 统计小额订单情况
				int co = investAmount.compareTo(investLimitAmount);
				if (co == -1) {
					orderTotalLimit = orderTotalLimit.add(investAmount);
					if (share.compareTo(ZERO) == 1) {
						if (payAmount.compareTo(ZERO) == 1) {
							orderTotalLimitPayedShared = orderTotalLimitPayedShared.add(payAmount);
						} else {
							orderTotalLimitUnPayedShared = orderTotalLimitUnPayedShared.add(share);
						}
					}
				}
			}
			/* -- 订单总数 -- */
			orderTotalCount = listProductOrders.size();
			/* -- 每日到账情况 -- */
			List<Map<String, Object>> tsmList = this.getProductOrderAmount(saleManage);
			plot.setOrderDaysAmountListJSON(JSONArray.fromObject(tsmList).toString());

			plot.setOrderTotal(orderTotal);
			plot.setOrderTotalShared(orderTotalShared);

			plot.setOrderTotalPayed(orderTotalPayed);
			plot.setOrderTotalCorrectPayed(orderTotalCorrectPayed);

			plot.setOrderTotalCount(orderTotalCount);
			plot.setOrderTotalSharedCount(orderTotalSharedCount);
			plot.setOrderTotalPayedCount(orderTotalPayedCount);

			plot.setOrderTotalLimit(orderTotalLimit);
			plot.setOrderTotalLimitPayedShared(orderTotalLimitPayedShared);
			plot.setOrderTotalLimitUnPayedShared(orderTotalLimitUnPayedShared);

			plot.setOrderTotalCorrectPayedCount(orderTotalCorrectPayedCount);
			plot.setOrderTotalCorrectCollectCount(orderTotalCorrectCollectCount);

		}
		return plot;
	}

	/**
	 * 子产品图表统计
	 * 
	 * @param plot
	 * @return
	 * @throws Exception
	 */
	public SaleManagePlot subProductStatisticsPlot(SaleManage saleManage) throws Exception {
		SaleManagePlot plot = new SaleManagePlot();

		/* -- 订单总额 -- */
		BigDecimal orderTotal = ZERO;
		BigDecimal orderTotalShared = ZERO;
		/* -- 到账金额 -- */
		BigDecimal orderTotalPayed = ZERO;
		BigDecimal orderTotalCorrectPayed = ZERO;
		/* -- 订单总数 -- */
		long orderTotalCount = 0;
		long orderTotalSharedCount = 0;
		long orderTotalPayedCount = 0;
		/* -- 小额总额 -- */
		BigDecimal orderTotalLimit = ZERO;
		BigDecimal orderTotalLimitPayedShared = ZERO;
		BigDecimal orderTotalLimitUnPayedShared = ZERO;
		/* -- 单证归集数 -- */
		long orderTotalCorrectPayedCount = 0;
		long orderTotalCorrectCollectCount = 0;

		// 小额投资金额限制 如果不限额则不统计小额投资金额总计
		BigDecimal investLimitAmount = saleManage.getAmountLimit();
		if (investLimitAmount.equals(new BigDecimal(-1))) {
			orderTotalLimit = new BigDecimal(-1);
		}

		List<SaleManage> listProductOrders = this.getSubPruductOrders(saleManage);
		if (listProductOrders != null) {
			for (int i = 0; i < listProductOrders.size(); i++) {
				SaleManage sm = listProductOrders.get(i);
				BigDecimal investAmount = sm.getInvestAmount();
				if (investAmount == null) {
					investAmount = ZERO;
				}
				BigDecimal share = sm.getShare();
				if (share == null) {
					share = ZERO;
				}
				BigDecimal payAmount = sm.getPayAmount();
				if (payAmount == null) {
					payAmount = ZERO;
				}
				Short docStatus = sm.getDocStatus();
				if (docStatus == null) {
					docStatus = (short) -1;
				}
				// 计算订单总金额
				orderTotal = orderTotal.add(investAmount);
				// 计算订单总配额
				orderTotalShared = orderTotalShared.add(share);
				// 计算订单到账金额
				orderTotalPayed = orderTotalPayed.add(payAmount);
				// 计算到账情况
				if (payAmount.compareTo(ZERO) == 1) {
					orderTotalPayedCount++;
					int co = payAmount.compareTo(investAmount);
					if (co == 1 || co == 0) {
						orderTotalCorrectPayed = orderTotalCorrectPayed.add(investAmount);
					}
				}
				// 已配额订单总数
				if (share.compareTo(ZERO) == 1) {
					orderTotalSharedCount++;
				}
				// 统计已锁定单证归集情况
				if (payAmount.equals(investAmount)) {
					orderTotalCorrectPayedCount++;
					if (docStatus.equals((short) 1)) {
						orderTotalCorrectCollectCount++;
					}
				}
				// 统计小额订单情况
				int co = investAmount.compareTo(investLimitAmount);
				if (co == -1) {
					orderTotalLimit = orderTotalLimit.add(investAmount);
					if (share.compareTo(ZERO) == 1) {
						if (payAmount.compareTo(ZERO) == 1) {
							orderTotalLimitPayedShared = orderTotalLimitPayedShared.add(payAmount);
						} else {
							orderTotalLimitUnPayedShared = orderTotalLimitUnPayedShared.add(share);
						}
					}
				}
			}
			/* -- 订单总数 -- */
			orderTotalCount = listProductOrders.size();
			/* -- 每日到账情况 -- */
			List<Map<String, Object>> tsmList = this.getSubProductOrderAmount(saleManage);
			plot.setOrderDaysAmountListJSON(JSONArray.fromObject(tsmList).toString());

			plot.setOrderTotal(orderTotal);
			plot.setOrderTotalShared(orderTotalShared);

			plot.setOrderTotalPayed(orderTotalPayed);
			plot.setOrderTotalCorrectPayed(orderTotalCorrectPayed);

			plot.setOrderTotalCount(orderTotalCount);
			plot.setOrderTotalSharedCount(orderTotalSharedCount);
			plot.setOrderTotalPayedCount(orderTotalPayedCount);

			plot.setOrderTotalLimit(orderTotalLimit);
			plot.setOrderTotalLimitPayedShared(orderTotalLimitPayedShared);
			plot.setOrderTotalLimitUnPayedShared(orderTotalLimitUnPayedShared);

			plot.setOrderTotalCorrectPayedCount(orderTotalCorrectPayedCount);
			plot.setOrderTotalCorrectCollectCount(orderTotalCorrectCollectCount);

		}
		return plot;
	}

	/**
	 * 主产品额度配给统计
	 * 
	 * @param plot
	 * @return
	 * @throws Exception
	 */
	public SaleManagePlot productStatisticsForPushShare(SaleManage saleManage) throws Exception {
		SaleManagePlot plot = new SaleManagePlot();

		/* -- 到账金额 -- */
		BigDecimal orderTotalPayed = ZERO;
		BigDecimal orderTotalCorrectPayed = ZERO;
		/* -- 配给额度 -- */
		BigDecimal orderTotalPushShare = ZERO;
		BigDecimal orderTotalAcceptPushShare = ZERO;
		/* -- 小额订单 -- */
		int maxLowAmountClientCount = 0;
		long orderTotallimitCorrectCount = 0;
		/* -- 订单 -- */
		long orderTotalLimitPayedCount = 0;

		// 小额投资金额限制
		// BigDecimal investLimitAmount = saleManage.getAmountLimit();

		List<SaleManage> listProductOrders = this.getPruductOrders(saleManage);
		if (listProductOrders != null) {
			for (int i = 0; i < listProductOrders.size(); i++) {

				SaleManage sm = listProductOrders.get(i);
				BigDecimal investAmount = sm.getInvestAmount();
				if (investAmount == null) {
					investAmount = ZERO;
				}
				BigDecimal pushShare = sm.getPushShare();
				if (pushShare == null) {
					pushShare = ZERO;
				}
				BigDecimal payAmount = sm.getPayAmount();
				if (payAmount == null) {
					payAmount = ZERO;
				}
				Short pushStatus = sm.getPushStatus();
				if (pushStatus == null) {
					pushStatus = (short) -1;
				}

				// 计算订单到账金额
				orderTotalPayed = orderTotalPayed.add(payAmount);
				// 计算锁定到账情况
				if (payAmount.compareTo(ZERO) == 1) {
					int co = payAmount.compareTo(investAmount);
					if (co == 1 || co == 0) {
						orderTotalCorrectPayed = orderTotalCorrectPayed.add(investAmount);
						if (co == 0) {
							orderTotallimitCorrectCount++;
						}
					}
				}
				// 配给总额度
				if (pushStatus != null && !pushStatus.equals((short) 4)) {
					orderTotalPushShare = orderTotalPushShare.add(pushShare);
				}
				// 配给接受额度
				if (pushStatus != null && pushStatus.equals((short) 2)) {
					orderTotalAcceptPushShare = orderTotalAcceptPushShare.add(pushShare);
				}

				// 到账订单数
				if (payAmount.compareTo(ZERO) == 1) {
					orderTotalLimitPayedCount++;
				}

				// 统计小额订单情况
				// int co = investAmount.compareTo(investLimitAmount);
				// if (co == -1) {
				// int col = payAmount.compareTo(investAmount);
				// if (col == 1 || col == 0) {
				// orderTotallimitCorrectCount++;
				// }
				// }
			}
		}

		plot.setOrderTotalPayed(orderTotalPayed);
		plot.setOrderTotalCorrectPayed(orderTotalCorrectPayed);
		plot.setOrderTotalPushShare(orderTotalPushShare);
		plot.setOrderTotalAcceptPushShare(orderTotalAcceptPushShare);

		plot.setMaxLowAmountClientCount(maxLowAmountClientCount);
		plot.setOrderTotallimitCorrectCount(orderTotallimitCorrectCount);
		plot.setOrderTotalLimitPayedCount(orderTotalLimitPayedCount);

		return plot;
	}

	/**
	 * 子产品额度配给统计
	 * 
	 * @param plot
	 * @return
	 * @throws Exception
	 */
	public SaleManagePlot subProductStatisticsForPushShare(SaleManage saleManage) throws Exception {
		SaleManagePlot plot = new SaleManagePlot();

		/* -- 到账金额 -- */
		BigDecimal orderTotalPayed = ZERO;
		BigDecimal orderTotalCorrectPayed = ZERO;
		/* -- 配给额度 -- */
		BigDecimal orderTotalPushShare = ZERO;
		BigDecimal orderTotalAcceptPushShare = ZERO;
		/* -- 小额订单 -- */
		int maxLowAmountClientCount = 0;
		long orderTotallimitCorrectCount = 0;
		/* -- 订单 -- */
		long orderTotalLimitPayedCount = 0;

		// 小额投资金额限制
		// BigDecimal investLimitAmount = saleManage.getAmountLimit();

		List<SaleManage> listSubProductOrders = this.getSubPruductOrders(saleManage);
		if (listSubProductOrders != null) {
			for (int i = 0; i < listSubProductOrders.size(); i++) {
				SaleManage sm = listSubProductOrders.get(i);
				BigDecimal investAmount = sm.getInvestAmount();
				if (investAmount == null) {
					investAmount = ZERO;
				}
				BigDecimal pushShare = sm.getPushShare();
				if (pushShare == null) {
					pushShare = ZERO;
				}
				BigDecimal payAmount = sm.getPayAmount();
				if (payAmount == null) {
					payAmount = ZERO;
				}
				Short pushStatus = sm.getPushStatus();
				if (pushStatus == null) {
					pushStatus = (short) -1;
				}

				// 计算订单到账金额
				orderTotalPayed = orderTotalPayed.add(payAmount);
				// 计算锁定到账情况
				if (payAmount.compareTo(ZERO) == 1) {
					int co = payAmount.compareTo(investAmount);
					if (co == 1 || co == 0) {
						orderTotalCorrectPayed = orderTotalCorrectPayed.add(investAmount);
						if (co == 0) {
							orderTotallimitCorrectCount++;
						}
					}
				}
				// 配给总额度
				if (pushStatus != null && !pushStatus.equals((short) 4)) {
					orderTotalPushShare = orderTotalPushShare.add(pushShare);
				}
				// 配给接受额度
				if (pushStatus != null && pushStatus.equals((short) 2)) {
					orderTotalAcceptPushShare = orderTotalAcceptPushShare.add(pushShare);
				}

				// 到账订单数
				if (payAmount.compareTo(ZERO) == 1) {
					orderTotalLimitPayedCount++;
				}

				// 统计小额订单情况
				// int co = investAmount.compareTo(investLimitAmount);
				// if (co == -1) {
				// int col = payAmount.compareTo(investAmount);
				// if (col == 1 || col == 0) {
				// orderTotallimitCorrectCount++;
				// }
				// }
			}

			plot.setOrderTotalPayed(orderTotalPayed);
			plot.setOrderTotalCorrectPayed(orderTotalCorrectPayed);
			plot.setOrderTotalPushShare(orderTotalPushShare);
			plot.setOrderTotalAcceptPushShare(orderTotalAcceptPushShare);

			plot.setMaxLowAmountClientCount(maxLowAmountClientCount);
			plot.setOrderTotallimitCorrectCount(orderTotallimitCorrectCount);
			plot.setOrderTotalLimitPayedCount(orderTotalLimitPayedCount);

		}
		return plot;
	}

	/**
	 * 产品概览图表统计
	 * 
	 * @param product
	 * @param activeSubProduct
	 * @return
	 * @throws Exception
	 */
	public SaleManagePlot getOverviewPlot(Product product, SubProduct activeSubProduct) throws Exception {
		SaleManagePlot plot = new SaleManagePlot();
		SaleManage psm;
		// 最大募集额度
		BigDecimal maxTotalShare = ZERO;
		// 最小募集额度
		BigDecimal minTotalShare = ZERO;
		// 产品是否共享额度
		short isTotalShare = product.getIsTotalShare();
		// 小额投资金额上限
		BigDecimal amountLimit = product.getLowAmountThreshold();

		// 共享
		if (isTotalShare == (short) 1) {
			// 确定产品规模
			maxTotalShare = product.getMaxTotalShare();
			minTotalShare = product.getMinTotalShare();
			// 按主产品统计
			psm = new SaleManage();
			psm.setAmountLimit(amountLimit);
			psm.setProductId(product.getId());
			plot = this.productStatisticsPlot(psm);
		}
		// 不共享
		else {
			// 确定产品规模
			maxTotalShare = activeSubProduct.getMaxTotalShare();
			minTotalShare = activeSubProduct.getMinTotalShare();
			// 按特定子产品统计
			Long subProductId = activeSubProduct.getId();
			psm = new SaleManage();
			psm.setAmountLimit(amountLimit);
			psm.setSubProductId(subProductId);
			plot = this.subProductStatisticsPlot(psm);
		}

		// 设置产品规模
		plot.setProductScaleMax(maxTotalShare);
		plot.setProductScaleMin(minTotalShare);
		return plot;
	}

	/**
	 * 额度配给统计
	 * 
	 * @param product
	 * @param activeSubProduct
	 * @return
	 * @throws Exception
	 */
	public SaleManagePlot getPushSharePlot(Product product, SubProduct activeSubProduct) throws Exception {
		SaleManagePlot plot = new SaleManagePlot();
		SaleManage psm;
		// 最大募集额度
		BigDecimal maxTotalShare = ZERO;
		// 产品是否共享额度
		short isTotalShare = product.getIsTotalShare();
		// 产品是否共享小额度投资人数
		short isTotalLowAmountClientCount = product.getIsTotalLowAmountClientCount();
		// 小额投资金额上限
		BigDecimal amountLimit = product.getLowAmountThreshold();
		// 小额订单投资人数限制
		int maxLowAmountClientCount = 0;

		// 共享
		if (isTotalShare == (short) 1) {
			// 确定产品规模
			maxTotalShare = product.getMaxTotalShare();
			// 按主产品统计
			psm = new SaleManage();
			psm.setAmountLimit(amountLimit);
			psm.setProductId(product.getId());
			plot = this.productStatisticsForPushShare(psm);
		}
		// 不共享
		else {
			// 确定产品规模
			maxTotalShare = activeSubProduct.getMaxTotalShare();
			// 按特定子产品统计
			Long subProductId = activeSubProduct.getId();
			psm = new SaleManage();
			psm.setAmountLimit(amountLimit);
			psm.setSubProductId(subProductId);
			plot = this.subProductStatisticsForPushShare(psm);
		}

		// 判断是否共享小额投资人数限制
		if (isTotalLowAmountClientCount == (short) 1) {
			maxLowAmountClientCount = product.getMaxLowAmountClientCount();
		} else {
			maxLowAmountClientCount = activeSubProduct.getMaxLowAmountClientCount();
		}

		// 设置产品规模
		plot.setProductScaleMax(maxTotalShare);
		// 设置小额投资人数上限
		plot.setMaxLowAmountClientCount(maxLowAmountClientCount);
		return plot;
	}

	/**
	 * 
	 * 设置当前销售状态 [页面状态]1-开启预约 2-开启打款 3-停止打款 4-产品封账 5-产品成立 6-发行失败 7-取消投放 8-是否可收藏
	 * 
	 * @param product
	 * @param subProduct
	 * @return
	 * @throws Exception
	 */
	public SaleManage getCurrSaleStatus(Product product, SubProduct activeSubProduct) throws Exception {
		SaleManage sm = new SaleManage();
		short currStatus = 0;
		// 获取主产品销售状态
		Short saleStatus = product.getSellStatus();
		// 获取子产品打款状态
		Short isRemittance = activeSubProduct.getIsRemittance();
		// 获取子产品预约状态
		Short isOrder = activeSubProduct.getIsOrder();
		sm.setIsOrder(isOrder);
		// 获取子产品收藏状态
		Short isFavorites = activeSubProduct.getIsFavorites();
		sm.setIsFavorites(isFavorites);

		// 产品销售状态为'计划投放'
		if (saleStatus.equals((short) 1)) {
			if (isOrder.equals((short) 1)) {
				currStatus = (short) 1;
			} else {
				currStatus = (short) 0;
			}
		}
		// 产品销售状态为'在售'
		else if (saleStatus.equals((short) 2)) {
			if (isRemittance != null && isRemittance.equals((short) 1)) {
				currStatus = (short) 2;
			} else {
				currStatus = (short) 3;
			}
		}
		// 产品销售状态为'产品封账'
		else if (saleStatus.equals((short) 9)) {
			currStatus = (short) 4;
		}
		// 产品销售状态为'已成立或已成立且在售'
		else if (saleStatus.equals((short) 5) || saleStatus.equals((short) 6)) {
			currStatus = (short) 5;
		}
		// 产品销售状态为 '发行失败'
		else if (saleStatus.equals((short) 4)) {
			currStatus = (short) 6;
		}
		// 产品销售状态为取消投放
		else if (saleStatus.equals((short) 8)) {
			currStatus = (short) 7;
		}
		sm.setCurrStatus(currStatus);
		return sm;
	}

	/**
	 * 产品额度分配统计
	 * 
	 * @param plot
	 * @return
	 * @throws Exception
	 */
	public SaleManagePlot productDistributePlot(SaleManage saleManage) throws Exception {

		// 产品是否共享额度
		short isTotalShare = saleManage.getIsTotalShare();
		// 小额投资金额上限
		BigDecimal amountLimit = saleManage.getAmountLimit();
		// 是否共享小额投资人数限制
		short isTotalLowAmountClientCount = saleManage.getIsTotalLowAmountClientCount();
		// 子产品ID
		Long subProductId = saleManage.getSubProductId();

		SaleManagePlot plot = new SaleManagePlot();
		// 到账金额
		BigDecimal orderTotalPayed = ZERO;
		// 已配额未到账
		BigDecimal orderTotalUnPayedShared = ZERO;
		// 小额到账订单数
		long orderTotalLimitPayedCount = 0;
		// 小额已配额未到账订单数
		long orderTotalLimitUnPayedSharedCount = 0;

		List<SaleManage> listProductOrders = this.getPruductOrders(saleManage);
		if (listProductOrders != null) {
			for (int i = 0; i < listProductOrders.size(); i++) {
				SaleManage sm = listProductOrders.get(i);
				Long tempPubProductId = sm.getSubProductId();
				BigDecimal share = sm.getShare();

				// 过滤未配额订单
				if (share == null || share.equals(ZERO)) {
					continue;
				}
				// 投资金额
				BigDecimal investAmount = sm.getInvestAmount();
				if (investAmount == null) {
					investAmount = ZERO;
				}
				// 支付金额
				BigDecimal payAmount = sm.getPayAmount();
				if (payAmount == null) {
					payAmount = ZERO;
				}
				// 是否共享额度
				if (isTotalShare == 1) {
					// 配额已到账
					orderTotalPayed = orderTotalPayed.add(payAmount);
					// 配额未到账
					BigDecimal sp = share.subtract(payAmount);
					if (sp.compareTo(ZERO) == 1) {
						orderTotalUnPayedShared = orderTotalUnPayedShared.add(sp);
					}
				}
				// 否则按子产品统计
				else {
					if (tempPubProductId.equals(subProductId)) {
						// 配额已到账
						orderTotalPayed = orderTotalPayed.add(payAmount);
						// 配额未到账
						BigDecimal sp = share.subtract(payAmount);
						if (sp.compareTo(ZERO) == 1) {
							orderTotalUnPayedShared = orderTotalUnPayedShared.add(sp);
						}
					}
				}
				// 判断是否共享小额投资人数限制
				int co = investAmount.compareTo(amountLimit);
				if (co == -1) {
					if (isTotalLowAmountClientCount == (short) 1) {
						if (payAmount.compareTo(ZERO) == 1) {
							// 小额到账订单数
							orderTotalLimitPayedCount++;
						} else {
							orderTotalLimitUnPayedSharedCount++;
						}
					} else {
						if (tempPubProductId.equals(subProductId)) {
							if (payAmount.compareTo(ZERO) == 1) {
								// 小额到账订单数
								orderTotalLimitPayedCount++;
							} else {
								orderTotalLimitUnPayedSharedCount++;
							}
						}
					}
				}
			}
			plot.setOrderTotalPayed(orderTotalPayed);
			plot.setOrderTotalUnPayedShared(orderTotalUnPayedShared);
			plot.setOrderTotalLimitPayedCount(orderTotalLimitPayedCount);
			plot.setOrderTotalLimitUnPayedSharedCount(orderTotalLimitUnPayedSharedCount);
		}
		return plot;
	}

	/**
	 * 获取产品额度分配统计数据
	 * 
	 * @param saleManage
	 * @return
	 * @throws Exception
	 */
	public SaleManagePlot getDistributePlot(Product product, SubProduct activeSubProduct) throws Exception {
		SaleManagePlot plot = new SaleManagePlot();
		SaleManage psm;
		// 最大募集额度
		BigDecimal maxTotalShare = ZERO;
		// 最小募集额度
		BigDecimal minTotalShare = ZERO;
		// 产品是否共享额度
		short isTotalShare = product.getIsTotalShare();
		// 小额投资金额上限
		BigDecimal amountLimit = product.getLowAmountThreshold();
		// 是否共享小额投资人数限制
		short isTotalLowAmountClientCount = product.getIsTotalLowAmountClientCount();
		// 子产品ID
		Long subProductId = activeSubProduct.getId();
		// 小额投资人数限制
		Integer clientLimit = 0;

		// 额度分配统计数据
		psm = new SaleManage();
		psm.setProductId(product.getId());
		psm.setSubProductId(subProductId);
		psm.setIsTotalShare(isTotalShare);
		psm.setIsTotalLowAmountClientCount(isTotalLowAmountClientCount);
		psm.setAmountLimit(amountLimit);
		plot = this.productDistributePlot(psm);

		// 共享
		if (isTotalShare == (short) 1) {
			// 确定产品规模
			maxTotalShare = product.getMaxTotalShare();
			minTotalShare = product.getMinTotalShare();
		}
		// 不共享
		else {
			// 确定产品规模
			maxTotalShare = activeSubProduct.getMaxTotalShare();
			minTotalShare = activeSubProduct.getMinTotalShare();
		}

		// 设置产品规模
		plot.setProductScaleMax(maxTotalShare);
		plot.setProductScaleMin(minTotalShare);

		// 是共享则按主产品设置
		if (isTotalLowAmountClientCount == (short) 1) {
			clientLimit = product.getMaxLowAmountClientCount();
		}
		// 否则按子产品设置
		else {
			clientLimit = activeSubProduct.getMaxLowAmountClientCount();
		}
		// 设置小额投资金额限制
		plot.setLowAmountThreshold(amountLimit);
		// 设置额度分配功能小额度投资人数限制
		plot.setMaxLowAmountClientCount(clientLimit);
		return plot;
	}

	/**
	 * 获取主产品订单
	 * 
	 * @param saleManage
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<SaleManage> getPruductOrders(SaleManage saleManage) throws Exception {
		return (List<SaleManage>) super.getRelationDao().selectList("saleManage.select_product_orders", saleManage);
	}

	@SuppressWarnings("unchecked")
	public List<SaleManage> getProductOrdersByPushShare(SaleManage saleManage) throws Exception {
		return (List<SaleManage>) super.getRelationDao().selectList("saleManage.select_product_orders_by_pushshare", saleManage);
	}

	/**
	 * 获取子产品订单
	 * 
	 * @param saleManage
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<SaleManage> getSubPruductOrders(SaleManage saleManage) throws Exception {
		return (List<SaleManage>) super.getRelationDao().selectList("saleManage.select_sub_product_orders", saleManage);
	}

	@SuppressWarnings("unchecked")
	public List<SaleManage> getSubPruductOrdersByPushShare(SaleManage saleManage) throws Exception {
		return (List<SaleManage>) super.getRelationDao().selectList("saleManage.select_sub_product_orders_by_pushshare", saleManage);
	}

	/**
	 * 主产品已配额订单数量按天统计
	 * 
	 * @param saleManage
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Integer>> countProductOrdersShared(SaleManage saleManage) throws Exception {
		return (List<Map<String, Integer>>) super.getRelationDao().selectList("saleManage.count_product_orders_shared", saleManage);
	}

	/**
	 * 子产品已配额订单数量按天统计
	 * 
	 * @param saleManage
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Integer>> countSubProductOrdersShared(SaleManage saleManage) throws Exception {
		return (List<Map<String, Integer>>) super.getRelationDao().selectList("saleManage.count_sub_product_orders_shared", saleManage);
	}

	/**
	 * 查询主产品订单每日统计量
	 * 
	 * @param saleManage
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getProductOrderAmount(SaleManage saleManage) throws Exception {
		return (List<Map<String, Object>>) super.getRelationDao().selectList("saleManage.select_product_orders_day_count", saleManage);
	}

	/**
	 * 查询子产品订单每日统计量
	 * 
	 * @param saleManage
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSubProductOrderAmount(SaleManage saleManage) throws Exception {
		return (List<Map<String, Object>>) super.getRelationDao().selectList("saleManage.select_sub_product_orders_day_count", saleManage);
	}

	public Long getProductFundCondition(SaleManage saleManage) throws Exception {
		return (long) super.getRelationDao().getCount("saleManage.count_product_fund_condition", saleManage);
	}

	/**
	 * 获取预约订单2
	 * 
	 * @param saleManage
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<SaleManage> getOrdersWithTeam(SaleManage saleManage) throws Exception {
		return (List<SaleManage>) super.getRelationDao().selectList("saleManage.select_order_with_team", saleManage);
	}

	@Override
	public void save(SaleManage entity) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(SaleManage entity) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Long getRecordCount(SaleManage entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SaleManage> getList(SaleManage entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SaleManage> getPaginatedList(SaleManage entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(SaleManage entity) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteByArr(SaleManage entity) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public SaleManage getOne(SaleManage entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getGrid(SaleManage entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String upload(SaleManage entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(SaleManage entity, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(InitData initData) throws Exception {
		// TODO Auto-generated method stub

	}

	/*-------------------------------// 销售管控功能相关 --------------------------------*/

}
