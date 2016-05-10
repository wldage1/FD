package com.sw.plugins.commission.adjust.entity;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.sw.core.data.entity.RelationEntity;
import com.sw.plugins.market.order.entity.Order;

/**
 * 居间费记录表
 * 
 * @author Spark
 *
 */
public class Commission extends RelationEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7225520225669889780L;

	//订单ID
	private Long orderID;
	//理财师ID
	private Long memberID;
	//组织ID（机构ID或团队ID）
	private Long teamID;
	//居间公司ID
	private Long orgID;
	//产品ID
	private Long productID;
	//子产品ID
	private Long subProductID;
	//到账金额
	private BigDecimal payAmount;
	//存续阶段
	private String holdlingPhase;
	//年化发行成本率
	private BigDecimal issuanceCostRate;
	//时间系数
	private BigDecimal timeCoefficient;
	//服务费系数   百分比 (根据当前理财师属于个人或机构，判断使用佣金率表中个人服务费系数或机构服务费系数)
	private BigDecimal serviceChargeCoefficient;
	//激励费用率
	private BigDecimal incentiveFeeRate;
	//居间费比例
	private BigDecimal commissionProportion;
	//居间费
	private BigDecimal commission;
	//营业税(分)
	private BigDecimal salesTax;
	//城建税(分)
	private BigDecimal constructionTax;
	//教育费附加(分)
	private BigDecimal educationalSurtax;
	//地方教育费附加(分)
	private BigDecimal localEducationalCost;
	//个人所得税(分)
	private BigDecimal personalIncomeTax;
	//已支付居间费
	private BigDecimal payCommission;
	//网签协议ID
	private Long agreementTemplateID;
	//居间机构合同ID
	private Long orgContractID;
	//居间费支付状态  0-未核定  1-核定通过  2-核定未通过  3-重新核算未核定  4-审核通过  5-审未通过  6-拒付  7-部分支付  8-全部支付
	private Short payStatus;
	//订单交易类型
	private Short tradeType;
	//订单交易类型集合
	private String tradeTypeCollection[];
	//产品名称
	private String productName;
	//机构简称
	private String orgShortName;
	//开户行
	private String bankName;
	//卡号
	private String accountID;
	//支付状态
	private Short statusTwo;
	//发行机构简称
	private String proShortName;
	//发放时间
	private String payTime;
	//月份
	private String payMonth;
	//产品类型
	private String productType;
	//发放时间
	private String shoudPayDate;
	//发放时间月
	private String month;
	//产品查询的区分标志
	private String proMark;
	//激励费用是否发放 0或空为未发放，1为已发放
	private Short isPay;

	private String status;
	private String taxAssessmentTime;
	private String orderNumber;
	private Integer deadline;
	private String stageTime;
	private String beginTime;
	
	/****** 导出excel表格sql语句 *********/
	private String sqlQuerys;
	/****** 查询关联表 ******/
	//历史订单表BEAN
	private Order orderHistory;
	 
	/* * 页面显示 * */
	//居间费总金额
	private BigDecimal commissionSum;
	//实际到帐总金额
	private BigDecimal payAmountSum;
	//收益权类型
	private String subProductName;
	//生效日期
	private String effectiveDate;
	//佣金率ID
	private Long productcommissionratioId;
	private BigDecimal maxShareThreshold;
	private BigDecimal minShareThreshold;
	//未核算订单数
	private Long notAdjustOrderNumber;
	//未核定订单数
	private Long notCheckOrderNumber;
	//已核定通过订单数
	private Long notAgreeOrderNumber;
	//已核定不通过订单数
	private Long notDisagreeOrderNumber;
	//拒发订单数
	private Long refuseOrderNumber;
	//已核算订单数
	private Long adjustOrderNumber;
	//订单总数
	private Long orderCount;
	
	//居间费状态集合
	private String payStatusCollection[];
	//存续阶段集合
	private String holdlingPhaseCollection[];
	//子产品ID集合
	private String subProductIDCollection[];
	//存续阶段+子产品ID
	private ArrayList<String> queryCollection;
	//分组标志
	private String flag;
	
	private String payTimeStart;
	private String payTimeEnd;
	//核定时间
	private String checkRatifyTime;
	//导出状态
	private String exportStatus;
	//审核时间
	private String auditingTime;
	//审核备注
	private String auditingRemark;
	
	//期限数据类型 1月2日
	private Short deadlineDataType;
	
	private Short type;
	
	public Short getType(){
		return type;
	}
	
	public void setType(Short type){
		this.type = type;
	}
	
	public Short getDeadlineDataType(){
		return deadlineDataType;
	}
	
	public void setDeadlineDataType(Short deadlineDataType){
		this.deadlineDataType = deadlineDataType;
	}
	
	public String getStageTime() {
		return stageTime;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setStageTime(String stageTime) {
		this.stageTime = stageTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	private String commissionProportionStr;
	
	//下单时间
	private String orderTime;
	
	public Integer getDeadline() {
		return deadline;
	}
	public void setDeadline(Integer deadline) {
		this.deadline = deadline;
	}
	//**************机构居间费属性*****************
	private String memberName;
	
	private String memberNickName;
	
	private String teamShortName;
	
	private String productShortName;
	
	private String clientName;
	
	private String contractNumber;
	
	private Double investAmount;
	
	private String tradeTypeName;
	
	private String fileUrl;
	
	private String tradeStatusName;

	private String clientTypeName;
	
	private String iDCardTypeName;
	
	private String documentStatusName;
	
	private Short tradeStatus;
	
	private Short documentStatus;
	
	private Short clientType;
	
	private Short iDCardType;
	
	private String iDCard;
	
	private String teamOrOrgRemark;
	
	private String teamName;
	
	private Float netValue;
	
	private String payProgress;
	
	private String startDate;
	
	private String endDate;
	
	private String orgShoudPayDate;
	
	private String PayDate;
	
	public String getProMark() {
		return proMark;
	}
	public void setProMark(String proMark) {
		this.proMark = proMark;
	}
	public String getAuditingRemark() {
		return auditingRemark;
	}
	public void setAuditingRemark(String auditingRemark) {
		this.auditingRemark = auditingRemark;
	}
	public String getTaxAssessmentTime() {
		return taxAssessmentTime;
	}
	public void setTaxAssessmentTime(String taxAssessmentTime) {
		this.taxAssessmentTime = taxAssessmentTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPayDate() {
		return PayDate;
	}
	public void setPayDate(String payDate) {
		PayDate = payDate;
	}
	public String getOrgShoudPayDate() {
		return orgShoudPayDate;
	}
	public void setOrgShoudPayDate(String orgShoudPayDate) {
		this.orgShoudPayDate = orgShoudPayDate;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getCheckRatifyTime() {
		return checkRatifyTime;
	}
	public void setCheckRatifyTime(String checkRatifyTime) {
		this.checkRatifyTime = checkRatifyTime;
	}
	public String getExportStatus() {
		return exportStatus;
	}
	public void setExportStatus(String exportStatus) {
		this.exportStatus = exportStatus;
	}
	public String getAuditingTime() {
		return auditingTime;
	}
	public void setAuditingTime(String auditingTime) {
		this.auditingTime = auditingTime;
	}
	public String getShoudPayDate() {
		return shoudPayDate;
	}
	public void setShoudPayDate(String shoudPayDate) {
		this.shoudPayDate = shoudPayDate;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public Short getIsPay() {
		return isPay;
	}
	public void setIsPay(Short isPay) {
		this.isPay = isPay;
	}
	public String getPayProgress() {
		return payProgress;
	}
	public void setPayProgress(String payProgress) {
		this.payProgress = payProgress;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public BigDecimal getSalesTax() {
		return salesTax;
	}
	public void setSalesTax(BigDecimal salesTax) {
		this.salesTax = salesTax;
	}
	public BigDecimal getConstructionTax() {
		return constructionTax;
	}
	public void setConstructionTax(BigDecimal constructionTax) {
		this.constructionTax = constructionTax;
	}
	public BigDecimal getEducationalSurtax() {
		return educationalSurtax;
	}
	public void setEducationalSurtax(BigDecimal educationalSurtax) {
		this.educationalSurtax = educationalSurtax;
	}
	public BigDecimal getLocalEducationalCost() {
		return localEducationalCost;
	}
	public void setLocalEducationalCost(BigDecimal localEducationalCost) {
		this.localEducationalCost = localEducationalCost;
	}
	public BigDecimal getPersonalIncomeTax() {
		return personalIncomeTax;
	}
	public void setPersonalIncomeTax(BigDecimal personalIncomeTax) {
		this.personalIncomeTax = personalIncomeTax;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getProShortName() {
		return proShortName;
	}
	public void setProShortName(String proShortName) {
		this.proShortName = proShortName;
	}
	public Long getNotCheckOrderNumber() {
		return notCheckOrderNumber;
	}
	public void setNotCheckOrderNumber(Long notCheckOrderNumber) {
		this.notCheckOrderNumber = notCheckOrderNumber;
	}
	public Long getNotAgreeOrderNumber() {
		return notAgreeOrderNumber;
	}
	public void setNotAgreeOrderNumber(Long notAgreeOrderNumber) {
		this.notAgreeOrderNumber = notAgreeOrderNumber;
	}
	public Long getNotDisagreeOrderNumber() {
		return notDisagreeOrderNumber;
	}
	public void setNotDisagreeOrderNumber(Long notDisagreeOrderNumber) {
		this.notDisagreeOrderNumber = notDisagreeOrderNumber;
	}
	public Long getRefuseOrderNumber() {
		return refuseOrderNumber;
	}
	public void setRefuseOrderNumber(Long refuseOrderNumber) {
		this.refuseOrderNumber = refuseOrderNumber;
	}
	public Long getOrderID() {
		return orderID;
	}
	public void setOrderID(Long orderID) {
		this.orderID = orderID;
	}
	public Long getMemberID() {
		return memberID;
	}
	public void setMemberID(Long memberID) {
		this.memberID = memberID;
	}
	public Long getTeamID() {
		return teamID;
	}
	public void setTeamID(Long teamID) {
		this.teamID = teamID;
	}
	public Long getOrgID() {
		return orgID;
	}
	public void setOrgID(Long orgID) {
		this.orgID = orgID;
	}
	public Long getProductID() {
		return productID;
	}
	public void setProductID(Long productID) {
		this.productID = productID;
	}
	public Long getSubProductID() {
		return subProductID;
	}
	public void setSubProductID(Long subProductID) {
		this.subProductID = subProductID;
	}
	public BigDecimal getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}
	public String getHoldlingPhase() {
		return holdlingPhase;
	}
	public void setHoldlingPhase(String holdlingPhase) {
		this.holdlingPhase = holdlingPhase;
	}
	public BigDecimal getIssuanceCostRate() {
		return issuanceCostRate;
	}
	public void setIssuanceCostRate(BigDecimal issuanceCostRate) {
		this.issuanceCostRate = issuanceCostRate;
	}
	public BigDecimal getTimeCoefficient() {
		return timeCoefficient;
	}
	public void setTimeCoefficient(BigDecimal timeCoefficient) {
		this.timeCoefficient = timeCoefficient;
	}
	public BigDecimal getServiceChargeCoefficient() {
		return serviceChargeCoefficient;
	}
	public void setServiceChargeCoefficient(BigDecimal serviceChargeCoefficient) {
		this.serviceChargeCoefficient = serviceChargeCoefficient;
	}
	public BigDecimal getIncentiveFeeRate() {
		return incentiveFeeRate;
	}
	public void setIncentiveFeeRate(BigDecimal incentiveFeeRate) {
		this.incentiveFeeRate = incentiveFeeRate;
	}
	public BigDecimal getCommissionProportion() {
		return commissionProportion;
	}
	public void setCommissionProportion(BigDecimal commissionProportion) {
		this.commissionProportion = commissionProportion;
	}
	public BigDecimal getCommission() {
		return commission;
	}
	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}
	public BigDecimal getPayCommission() {
		return payCommission;
	}
	public void setPayCommission(BigDecimal payCommission) {
		this.payCommission = payCommission;
	}
	public Long getAgreementTemplateID() {
		return agreementTemplateID;
	}
	public void setAgreementTemplateID(Long agreementTemplateID) {
		this.agreementTemplateID = agreementTemplateID;
	}
	public Long getOrgContractID() {
		return orgContractID;
	}
	public void setOrgContractID(Long orgContractID) {
		this.orgContractID = orgContractID;
	}
	public Short getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(Short payStatus) {
		this.payStatus = payStatus;
	}
	public Short getTradeType() {
		return tradeType;
	}
	public void setTradeType(Short tradeType) {
		this.tradeType = tradeType;
	}
	public String[] getTradeTypeCollection() {
		return tradeTypeCollection;
	}
	public void setTradeTypeCollection(String[] tradeTypeCollection) {
		this.tradeTypeCollection = tradeTypeCollection;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Order getOrderHistory() {
		return orderHistory;
	}
	public void setOrderHistory(Order orderHistory) {
		this.orderHistory = orderHistory;
	}
	public BigDecimal getCommissionSum() {
		return commissionSum;
	}
	public void setCommissionSum(BigDecimal commissionSum) {
		this.commissionSum = commissionSum;
	}
	public BigDecimal getPayAmountSum() {
		return payAmountSum;
	}
	public void setPayAmountSum(BigDecimal payAmountSum) {
		this.payAmountSum = payAmountSum;
	}
	public String getSubProductName() {
		return subProductName;
	}
	public void setSubProductName(String subProductName) {
		this.subProductName = subProductName;
	}
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public Long getProductcommissionratioId() {
		return productcommissionratioId;
	}
	public void setProductcommissionratioId(Long productcommissionratioId) {
		this.productcommissionratioId = productcommissionratioId;
	}
	public Long getNotAdjustOrderNumber() {
		return notAdjustOrderNumber;
	}
	public void setNotAdjustOrderNumber(Long notAdjustOrderNumber) {
		this.notAdjustOrderNumber = notAdjustOrderNumber;
	}
	public Long getAdjustOrderNumber() {
		return adjustOrderNumber;
	}
	public void setAdjustOrderNumber(Long adjustOrderNumber) {
		this.adjustOrderNumber = adjustOrderNumber;
	}
	public String[] getPayStatusCollection() {
		return payStatusCollection;
	}
	public void setPayStatusCollection(String[] payStatusCollection) {
		this.payStatusCollection = payStatusCollection;
	}
	public String[] getHoldlingPhaseCollection() {
		return holdlingPhaseCollection;
	}
	public void setHoldlingPhaseCollection(String[] holdlingPhaseCollection) {
		this.holdlingPhaseCollection = holdlingPhaseCollection;
	}
	public String[] getSubProductIDCollection() {
		return subProductIDCollection;
	}
	public void setSubProductIDCollection(String[] subProductIDCollection) {
		this.subProductIDCollection = subProductIDCollection;
	}
	public ArrayList<String> getQueryCollection() {
		return queryCollection;
	}
	public void setQueryCollection(ArrayList<String> queryCollection) {
		this.queryCollection = queryCollection;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getMemberNickName() {
		return memberNickName;
	}
	public void setMemberNickName(String memberNickName) {
		this.memberNickName = memberNickName;
	}
	public String getTeamShortName() {
		return teamShortName;
	}
	public void setTeamShortName(String teamShortName) {
		this.teamShortName = teamShortName;
	}
	public String getProductShortName() {
		return productShortName;
	}
	public void setProductShortName(String productShortName) {
		this.productShortName = productShortName;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getContractNumber() {
		return contractNumber;
	}
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	public Double getInvestAmount() {
		return investAmount;
	}
	public void setInvestAmount(Double investAmount) {
		this.investAmount = investAmount;
	}
	public String getTradeTypeName() {
		return tradeTypeName;
	}
	public void setTradeTypeName(String tradeTypeName) {
		this.tradeTypeName = tradeTypeName;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public String getTradeStatusName() {
		return tradeStatusName;
	}
	public void setTradeStatusName(String tradeStatusName) {
		this.tradeStatusName = tradeStatusName;
	}
	public String getClientTypeName() {
		return clientTypeName;
	}
	public void setClientTypeName(String clientTypeName) {
		this.clientTypeName = clientTypeName;
	}
	public String getiDCardTypeName() {
		return iDCardTypeName;
	}
	public void setiDCardTypeName(String iDCardTypeName) {
		this.iDCardTypeName = iDCardTypeName;
	}
	public String getDocumentStatusName() {
		return documentStatusName;
	}
	public void setDocumentStatusName(String documentStatusName) {
		this.documentStatusName = documentStatusName;
	}
	public Short getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(Short tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	public Short getDocumentStatus() {
		return documentStatus;
	}
	public void setDocumentStatus(Short documentStatus) {
		this.documentStatus = documentStatus;
	}
	public Short getClientType() {
		return clientType;
	}
	public void setClientType(Short clientType) {
		this.clientType = clientType;
	}
	public Short getiDCardType() {
		return iDCardType;
	}
	public void setiDCardType(Short iDCardType) {
		this.iDCardType = iDCardType;
	}
	public String getiDCard() {
		return iDCard;
	}
	public void setiDCard(String iDCard) {
		this.iDCard = iDCard;
	}
	public String getTeamOrOrgRemark() {
		return teamOrOrgRemark;
	}
	public void setTeamOrOrgRemark(String teamOrOrgRemark) {
		this.teamOrOrgRemark = teamOrOrgRemark;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public Float getNetValue() {
		return netValue;
	}
	public void setNetValue(Float netValue) {
		this.netValue = netValue;
	}
	public String getSqlQuerys(){
		return sqlQuerys;
	}
	public void setSqlQuerys(String sqlQuerys){
		this.sqlQuerys = sqlQuerys;
	}
	public String getOrgShortName() {
		return orgShortName;
	}
	public void setOrgShortName(String orgShortName) {
		this.orgShortName = orgShortName;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getAccountID() {
		return accountID;
	}
	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}
	public Short getStatusTwo() {
		return statusTwo;
	}
	public void setStatusTwo(Short statusTwo) {
		this.statusTwo = statusTwo;
	}
	public String getPayTimeStart() {
		return payTimeStart;
	}
	public void setPayTimeStart(String payTimeStart) {
		this.payTimeStart = payTimeStart;
	}
	public String getPayTimeEnd() {
		return payTimeEnd;
	}
	public void setPayTimeEnd(String payTimeEnd) {
		this.payTimeEnd = payTimeEnd;
	}
	public String getPayMonth() {
		return payMonth;
	}
	public void setPayMonth(String payMonth) {
		this.payMonth = payMonth;
	}
	public String getCommissionProportionStr() {
		return commissionProportionStr;
	}
	public void setCommissionProportionStr(String commissionProportionStr) {
		this.commissionProportionStr = commissionProportionStr;
	}
	public Long getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(Long orderCount) {
		this.orderCount = orderCount;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public BigDecimal getMaxShareThreshold() {
		return maxShareThreshold;
	}
	public void setMaxShareThreshold(BigDecimal maxShareThreshold) {
		this.maxShareThreshold = maxShareThreshold;
	}
	public BigDecimal getMinShareThreshold() {
		return minShareThreshold;
	}
	public void setMinShareThreshold(BigDecimal minShareThreshold) {
		this.minShareThreshold = minShareThreshold;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	
}
