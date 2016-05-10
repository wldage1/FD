package com.sw.plugins.market.salemanage.entity;

import java.math.BigDecimal;
import java.util.List;

import com.sw.core.data.entity.RelationEntity;

/**
 * 
 * 表单处理BEAN
 * 
 * @author Administrator
 * 
 */
public class SaleManage extends RelationEntity {

	private static final long serialVersionUID = 3617498320458867625L;

	/* ----------------------- 列表字段 --------------------- */
	/** 订单号 */
	private String orderNumber;
	/** RM昵称 */
	private String rmShortName;
	/** RMID */
	private Long rmId;
	/** RM姓名 */
	private String rmName;
	/** RM信用值 */
	private Integer rmLevel;
	/** 机构,团队简称 */
	private String teamShortName;
	/** 机构团队类型0:机构1：团队 */
	private Short teamType;
	/** 机构团队ID */
	private Long teamId;
	/** 客户名称 */
	private String clientName;
	/** 客户证件号码 */
	private String iDCard;
	/** 预约时间 */
	private String orderTime;
	/** 预计打款时间 */
	private String commissionPayTime;
	/** 预约金额 */
	private BigDecimal investAmount;
	/** 预分配额度 */
	private BigDecimal preShared;
	/** 分配额度时间 */
	private String shareTime;
	/** 到账时间 */
	private String payTime;
	/** 到账金额 */
	private BigDecimal payAmount;
	/** 产品ID */
	private Long productId;
	/** 子产品ID */
	private Long subProductId;
	/** 订单ID */
	private Long orderId;
	/** 分配份额 */
	private BigDecimal share;
	/** 配给份额 */
	private BigDecimal pushShare;
	/** 订单交易状态 */
	private Short tradeStatus;
	/** 订单配给状态 */
	private Short pushStatus;
	/** 订单资金状态 */
	private Short payProgress;
	/** 单证归集状态 */
	private Short docStatus;
	/* -----------------------// 列表字段 --------------------- */

	/** 产品名称 */
	private String productName;
	/** 产品交易状态 */
	private Short sellStatus;
	/** 子产品是否可打款 */
	private Short isRemittance;
	/** 子产品是否可收藏 */
	private Short isFavorites;
	/** 子产品是否可预约 */
	private Short isOrder;
	/** 子产品是否可预约可打款 */
	private Short isOrderAndRemittance;
	/** 统计数 */
	private int count;
	/** 收藏时间 */
	private String favoritesTime;
	/** 小额订单人数限制 */
	private Integer clientLimit;
	/** 小额订单金额限制 */
	private BigDecimal amountLimit;
	/** 当前产品状态 */
	private Short currStatus;
	/** 是否共享额度0不共享，1共享 */
	private short isTotalShare;
	/** 是否共享小额投资人数限制 */
	private short isTotalLowAmountClientCount;
	/** 小额订单处理类型1:已到账订单 2：已配额未到账订单 0：新配额订单 */
	private short processType;
	/** 退款金额 */
	private BigDecimal reFund;
	/** 产品成立日期 **/
	private String foundDate;
	/** 产品止期 **/
	private String stopDate;
	/** 是否可追加 */
	private Short isRedeem;
	/** 是否可赎回 */
	private Short isAdd;
	/** 已到账大额订单数量 */
	private String largePayedCount;
	/** 已配额小额订单数量 */
	private String smallSharedCount;
	/** 大额到账明细 */
	private String largeOrderDetail;

	private SaleManagePlot saleManagePlot;
	private List<SaleManage> saleManageList;

	public SaleManage() {
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getRmShortName() {
		return rmShortName;
	}

	public void setRmShortName(String rmShortName) {
		this.rmShortName = rmShortName;
	}

	public String getRmName() {
		return rmName;
	}

	public void setRmName(String rmName) {
		this.rmName = rmName;
	}

	public Integer getRmLevel() {
		return rmLevel;
	}

	public void setRmLevel(Integer rmLevel) {
		this.rmLevel = rmLevel;
	}

	public String getTeamShortName() {
		return teamShortName;
	}

	public void setTeamShortName(String teamShortName) {
		this.teamShortName = teamShortName;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getCommissionPayTime() {
		return commissionPayTime;
	}

	public void setCommissionPayTime(String commissionPayTime) {
		this.commissionPayTime = commissionPayTime;
	}

	public BigDecimal getInvestAmount() {
		return investAmount;
	}

	public void setInvestAmount(BigDecimal investAmount) {
		this.investAmount = investAmount;
	}

	public BigDecimal getPreShared() {
		return preShared;
	}

	public void setPreShared(BigDecimal preShared) {
		this.preShared = preShared;
	}

	public String getShareTime() {
		return shareTime;
	}

	public void setShareTime(String shareTime) {
		this.shareTime = shareTime;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public BigDecimal getShare() {
		return share;
	}

	public void setShare(BigDecimal share) {
		this.share = share;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public List<SaleManage> getSaleManageList() {
		return saleManageList;
	}

	public void setSaleManageList(List<SaleManage> saleManageList) {
		this.saleManageList = saleManageList;
	}

	public Short getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(Short tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public Long getSubProductId() {
		return subProductId;
	}

	public void setSubProductId(Long subProductId) {
		this.subProductId = subProductId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getFavoritesTime() {
		return favoritesTime;
	}

	public void setFavoritesTime(String favoritesTime) {
		this.favoritesTime = favoritesTime;
	}

	public short getIsTotalShare() {
		return isTotalShare;
	}

	public void setIsTotalShare(short isTotalShare) {
		this.isTotalShare = isTotalShare;
	}

	public Short getSellStatus() {
		return sellStatus;
	}

	public void setSellStatus(Short sellStatus) {
		this.sellStatus = sellStatus;
	}

	public Short getIsRemittance() {
		return isRemittance;
	}

	public void setIsRemittance(Short isRemittance) {
		this.isRemittance = isRemittance;
	}

	public Short getIsFavorites() {
		return isFavorites;
	}

	public void setIsFavorites(Short isFavorites) {
		this.isFavorites = isFavorites;
	}

	public Short getIsOrder() {
		return isOrder;
	}

	public void setIsOrder(Short isOrder) {
		this.isOrder = isOrder;
	}

	public Integer getClientLimit() {
		return clientLimit;
	}

	public void setClientLimit(Integer clientLimit) {
		this.clientLimit = clientLimit;
	}

	public BigDecimal getAmountLimit() {
		return amountLimit;
	}

	public void setAmountLimit(BigDecimal amountLimit) {
		this.amountLimit = amountLimit;
	}

	public Short getCurrStatus() {
		return currStatus;
	}

	public void setCurrStatus(Short currStatus) {
		this.currStatus = currStatus;
	}

	public short getProcessType() {
		return processType;
	}

	public void setProcessType(short processType) {
		this.processType = processType;
	}

	public BigDecimal getReFund() {
		return reFund;
	}

	public void setReFund(BigDecimal reFund) {
		this.reFund = reFund;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getFoundDate() {
		return foundDate;
	}

	public void setFoundDate(String foundDate) {
		this.foundDate = foundDate;
	}

	public String getStopDate() {
		return stopDate;
	}

	public void setStopDate(String stopDate) {
		this.stopDate = stopDate;
	}

	public String getiDCard() {
		return iDCard;
	}

	public void setiDCard(String iDCard) {
		this.iDCard = iDCard;
	}

	public SaleManagePlot getSaleManagePlot() {
		return saleManagePlot;
	}

	public void setSaleManagePlot(SaleManagePlot saleManagePlot) {
		this.saleManagePlot = saleManagePlot;
	}

	public short getIsTotalLowAmountClientCount() {
		return isTotalLowAmountClientCount;
	}

	public void setIsTotalLowAmountClientCount(short isTotalLowAmountClientCount) {
		this.isTotalLowAmountClientCount = isTotalLowAmountClientCount;
	}

	public BigDecimal getPushShare() {
		return pushShare;
	}

	public void setPushShare(BigDecimal pushShare) {
		this.pushShare = pushShare;
	}

	public Short getPushStatus() {
		return pushStatus;
	}

	public void setPushStatus(Short pushStatus) {
		this.pushStatus = pushStatus;
	}

	public Short getIsOrderAndRemittance() {
		return isOrderAndRemittance;
	}

	public void setIsOrderAndRemittance(Short isOrderAndRemittance) {
		this.isOrderAndRemittance = isOrderAndRemittance;
	}

	public Short getPayProgress() {
		return payProgress;
	}

	public void setPayProgress(Short payProgress) {
		this.payProgress = payProgress;
	}

	public Short getIsRedeem() {
		return isRedeem;
	}

	public void setIsRedeem(Short isRedeem) {
		this.isRedeem = isRedeem;
	}

	public Short getIsAdd() {
		return isAdd;
	}

	public void setIsAdd(Short isAdd) {
		this.isAdd = isAdd;
	}

	public Short getDocStatus() {
		return docStatus;
	}

	public void setDocStatus(Short docStatus) {
		this.docStatus = docStatus;
	}

	public Short getTeamType() {
		return teamType;
	}

	public void setTeamType(Short teamType) {
		this.teamType = teamType;
	}

	public String getLargePayedCount() {
		return largePayedCount;
	}

	public void setLargePayedCount(String largePayedCount) {
		this.largePayedCount = largePayedCount;
	}

	public String getSmallSharedCount() {
		return smallSharedCount;
	}

	public void setSmallSharedCount(String smallSharedCount) {
		this.smallSharedCount = smallSharedCount;
	}

	public String getLargeOrderDetail() {
		return largeOrderDetail;
	}

	public void setLargeOrderDetail(String largeOrderDetail) {
		this.largeOrderDetail = largeOrderDetail;
	}

	public Long getTeamId() {
		return teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

	public Long getRmId() {
		return rmId;
	}

	public void setRmId(Long rmId) {
		this.rmId = rmId;
	}


}
