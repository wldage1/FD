package com.sw.plugins.market.order.entity;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.sw.core.data.entity.RelationEntity;
import com.sw.plugins.cooperate.provider.entity.Provider;
import com.sw.plugins.customer.member.entity.Member;
import com.sw.plugins.customer.team.entity.Team;
import com.sw.plugins.product.manage.entity.Product;
import com.sw.plugins.product.manage.entity.SubProduct;
import com.sw.plugins.system.organization.entity.Organization;

public class Order extends RelationEntity {

	private static final long serialVersionUID = 1L;
	/** 订单号 **/
	private String orderNumber;
	/** 居间公司ID **/
	private Long orgID;
	/** 供应商ID **/
	private Long providerID;
	/** 产品ID **/
	private Long productID;
	/** 子产品ID **/
	private Long subProductID;
	/** 机构ID **/
	private Long teamID;
	/** 理财顾问ID **/
	private Long memberID;
	/** 客户类别 1-个人 2-企业 **/
	private Short clientType;
	/** 客户名称 **/
	@NotEmpty
	private String clientName;
	/** 证件类型 1-身份证 **/
	private Long iDCardType;
	/** 证件号码 **/
	private String iDCard;
	/** 开户行客户名称 **/
	private String bankClientName;
	/** 开户行 **/
	private String bankName;
	/** 开户行卡号 **/
	private String bankNumber;
	/** 交易类型 1-认购 2-新申购 3-追加申购 只有阳光私募才可以追加 **/
	private Short tradeType;
	/** 投资金额 **/
	@NotNull
	private BigDecimal investAmount;
	/** 产品期限（月） **/
	private Integer deadline;
	/** 预期收益率 **/
	@NotEmpty
	private String profitRatio;
	/** 居间费结算比例 **/
//	private Float commissionPercent;
	/** 预计居间费(分) **/
//	private Double expectCommission;
	/** 居间费(分) **/
//	private Double commission;
	/** 营业税(分) **/
//	private Double salesTax;
	/** 城建税(分) **/
//	private Double constructionTax;
	/** 教育费附加(分) **/
//	private Double educationalSurtax;
	/** 地方教育费附加(分) **/
//	private Double localEducationalCost;
	/** 个人所得税(分) **/
//	private Double personalIncomeTax;
	/** 预约时间 **/
	@NotEmpty
	private String orderTime;
	/** 订单核对时间 */
	private String checkTime;
	/** 预计打款日期 **/
	private String commissionPayTime;
	/** 打款止期 **/
	private String payMoneyStopTime;
	/** 已分配份额(分) **/
	private BigDecimal share;
	/** 分配份额时间 **/
	private String shareTime;
	/** 到账金额(分) **/
	private BigDecimal payAmount;
	/** 到账日期 **/
	private String payTime;
	/** 认购净值 **/
	@NotNull
	private BigDecimal netValue;
	/** 成交份额(分) **/
	private BigDecimal assignedShare;
	/**
	 * 交易状态 1-预约提交 2-份额确认 3-单证归集 4-资金到帐 5-单证归集&资金到帐 6.产品封帐冻结 7-成功订单 8-产品未成立被取消
	 * 9-理财顾问取消预约 10-平台方取消预约
	 **/
	/**
	 * 二次修改 1-预约提交 2-份额确认 3-单证归集 4-资金到帐 5-单证归集&资金到帐 6.超期未打款 7-成功订单 8-产品未成立被取消
	 * 9-理财顾问取消预约 10-平台方取消预约 11-弃用 12.额度重分配中
	 **/
	private Short tradeStatus;
	/** 资金状态 0-未打款 1-部分到账 2-全部到账 3-超额打款 投资人打款状态 **/
	private Short payProgress;
	/** 单证状态 0-未归集 1-已归集 **/
	private Short docStatus;
	/** 单证状态 0-未寄出 1-已寄出 2-已收到 **/
	private Short documentStatus;
	/** 居间费支付状态 0-未发放 1-在途居间费 2-已发放居间费 3-延后发放居间费 4-不发放居间费 5-发放居间费失败 **/
	/** 二次修改 0-未核定 1-已核定 2-在途 3-已复核 4-延迟发放 5-拒付 6-汇款失败 7-已收 **/
//	private Short payStatus;
	/** 合同编号 **/
	private String contractNumber;
	/** 存续产品ID **/
	private Long holdingProductID;
	/** 备注 **/
	private String remark;
	/** 配给状态 0-未配给 1-已配给 2-同意配给 3-不同意配给 **/
	private Short pushStatus;
	/** 配给额度(分) **/
	private BigDecimal pushShare;
	/** 配给时间 **/
	private String pushTime;
	/** 居间费发放日期 **/
//	private String commissionGrantDate;
	/** 居间费发放账号 **/
//	private String commissionGrantAccount;
	/** 居间费发放账户名 **/
//	private String commissionGrantUserName;
	/** 居间费发放账号开户行 **/
//	private String commissionGrantBankName;
	/** 居间费发放备注 **/
//	private String commissionGrantRemark;
	/** RM备注 */
	private String rMRemark;
	//激励费用是否发放 0或空为未发放，1为已发放
	private Short isPay;
	/** 理财顾问组织名称 **/
	private String teamOrOrgName;
	/** 居间费总额 */
//	private double commissionSum;
	/** 爽约原因 */
	private Short breakReason; 
	
	private Long memberAccountID;
	
	private Long contactInformationID;
	
	private Short confirmPay;
	
	/****** 查询关联表 ******/
	// 产品表BEAN
	private Product product;
	// 产品供应商BEAN
	private Provider provider;
	// 理财顾问BEAN
	private Member member;
	// 理财顾问机构BEAN
	private Team team;
	// 居间机构信息BEAN
	private Organization organization;
	// 打款凭证BEAN
	private OrderProof orderProof;
	//自产品BEAN
	private SubProduct subProduct;

	/****** 状态中文转换 ******/
	// 中文交易状态
	private String tradeStatusName;
	// 中文资金状态
	private String payProgressName;
	// 中文单证状态
	private String docStatusName;
	// 中文单证邮寄状态
	private String documentStatusName;
	// 中文客户类别
	private String clientTypeName;
	// 中文证件类型
	private String iDCardTypeName;
	// 中文交易类型
	private String tradeTypeName;
	// 中文居间费支付状态
	private String payStatusName;
	// 中文配给状态
	private String pushStatusName;

	/****** 查询条件 ******/
	// (产品表)是否是销控管理员
	private Short isScManager;
	// (产品表)产品销售方
	private Short salerType;
	// 产品名称
	@NotEmpty
	private String productName;
	// 理财顾问名称
	@NotEmpty
	private String memberName;
	// 产品居间费状态
	private String commissionStatus;
	// 打款凭证 状态
	private Short ProofStatus; 
	// 导出产品订单的sql语句
	private String sqlQuerys;
	private String bank;
	private String card;
	private String bankAccout;
	//订单类型集合
	private String tradeTypeCollection[];
	//存续阶段+子产品ID
	private String queryCollection;
	//产品类型
	private Short productType;
	//存续阶段
	private BigDecimal maxShareThreshold;
	private BigDecimal minShareThreshold;
	//产品佣金率ID
	private Long productcommissionratioID;
	// 是否共享投资额度
	private Short isTotalShare;
	//成功订单表中的UpdateTime
	private String updateTime;
	//阶段时间
	private String stageTime;
	//开始时间
	private String beginTime;
	
	public String getSqlQuerys() {
		return sqlQuerys;
	}

	public void setSqlQuerys(String sqlQuerys) {
		this.sqlQuerys = sqlQuerys;
	}
	/********** 关联表需要展示的数据 ***********/
	private String memberNickName;
	//供应商简称
	private String providerShortName;
	//产品简称
	private String productShortName;
	//产品合同编号前缀
	private String proContractPrefix;
	//居间公司简称
	private String orgShortName;
	//受益权类型
	private String subProductType;
	//订单区分是否有凭证
	private String proofCount;
	//确认打款金额
	private BigDecimal affirmAmount;
	//生效日期
	private String effectiveDate;
	//存续时长(月)
	private String deadLine;
	
	/*
	 * 理财师单证邮寄地址
	 */
	//收件人
	private String contactName;
	//详细地址
	private String contactAddress;
	//手机号
	private String contactPhone;
	//固定电话号
	private String contactTelphone;
	//邮政编码
	private String contactPostCode;
	
	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getOrgShortName(){
		return orgShortName;
	}
	
	public void setOrgShortName(String orgShortName){
		this.orgShortName = orgShortName;
	}
	
	public String getProductShortName(){
		return productShortName;
	}
	
	public void setProductShortName(String productShortName){
		this.productShortName = productShortName;
	}
	
	public String getProContractPrefix() {
		return proContractPrefix;
	}

	public void setProContractPrefix(String proContractPrefix) {
		this.proContractPrefix = proContractPrefix;
	}
	
	public String getProviderShortName(){
		return providerShortName;
	}
	
	public void setProviderShortName(String providerShortName){
		this.providerShortName = providerShortName;
	}
	
	public String getMemberNickName() {
		return memberNickName;
	}

	public void setMemberNickName(String memberNickName) {
		this.memberNickName = memberNickName;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setIrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Long getOrgID() {
		return orgID;
	}

	public void setOrgID(Long orgID) {
		this.orgID = orgID;
	}

	public Long getProviderID() {
		return providerID;
	}

	public void setProviderID(Long providerID) {
		this.providerID = providerID;
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

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public void setSubProductID(Long subProductID) {
		this.subProductID = subProductID;
	}

	public Long getTeamID() {
		return teamID;
	}

	public void setTeamID(Long teamID) {
		this.teamID = teamID;
	}

	public Long getMemberID() {
		return memberID;
	}

	public void setMemberID(Long memberID) {
		this.memberID = memberID;
	}

	public Short getClientType() {
		return clientType;
	}

	public void setClientType(Short clientType) {
		this.clientType = clientType;
	}

	public String getClientName() {
		return clientName;
	}

	public String getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(String deadLine) {
		this.deadLine = deadLine;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Long getiDCardType() {
		return iDCardType;
	}

	public void setiDCardType(Long iDCardType) {
		this.iDCardType = iDCardType;
	}

	public String getiDCard() {
		return iDCard;
	}

	public void setiDCard(String iDCard) {
		this.iDCard = iDCard;
	}

	public Short getTradeType() {
		return tradeType;
	}

	public void setTradeType(Short tradeType) {
		this.tradeType = tradeType;
	}

	public BigDecimal getInvestAmount() {
		return investAmount;
	}

	public void setInvestAmount(BigDecimal investAmount) {
		this.investAmount = investAmount;
	}

	public Integer getDeadline() {
		return deadline;
	}

	public void setDeadline(Integer deadline) {
		this.deadline = deadline;
	}

	public String getProfitRatio() {
		return profitRatio;
	}

	public void setProfitRatio(String profitRatio) {
		this.profitRatio = profitRatio;
	}

//	public Float getCommissionPercent() {
//		return commissionPercent;
//	}
//
//	public void setCommissionPercent(Float commissionPercent) {
//		this.commissionPercent = commissionPercent;
//	}
//
//	public Double getExpectCommission() {
//		return expectCommission;
//	}
//
//	public void setExpectCommission(Double expectCommission) {
//		this.expectCommission = expectCommission;
//	}
//
//	public Double getCommission() {
//		return commission;
//	}
//
//	public void setCommission(Double commission) {
//		this.commission = commission;
//	}
//
//	public Double getSalesTax() {
//		return salesTax;
//	}
//
//	public void setSalesTax(Double salesTax) {
//		this.salesTax = salesTax;
//	}
//
//	public Double getConstructionTax() {
//		return constructionTax;
//	}
//
//	public void setConstructionTax(Double constructionTax) {
//		this.constructionTax = constructionTax;
//	}
//
//	public Double getEducationalSurtax() {
//		return educationalSurtax;
//	}
//
//	public void setEducationalSurtax(Double educationalSurtax) {
//		this.educationalSurtax = educationalSurtax;
//	}
//
//	public Double getLocalEducationalCost() {
//		return localEducationalCost;
//	}
//
//	public void setLocalEducationalCost(Double localEducationalCost) {
//		this.localEducationalCost = localEducationalCost;
//	}
//
//	public Double getPersonalIncomeTax() {
//		return personalIncomeTax;
//	}
//
//	public void setPersonalIncomeTax(Double personalIncomeTax) {
//		this.personalIncomeTax = personalIncomeTax;
//	}

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

	public String getPayMoneyStopTime() {
		return payMoneyStopTime;
	}

	public void setPayMoneyStopTime(String payMoneyStopTime) {
		this.payMoneyStopTime = payMoneyStopTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public BigDecimal getShare() {
		return share;
	}

	public void setShare(BigDecimal share) {
		this.share = share;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public String getPayTime() {
		return payTime;
	}

	public BigDecimal getNetValue() {
		return netValue;
	}

	public void setNetValue(BigDecimal netValue) {
		this.netValue = netValue;
	}

	public BigDecimal getAssignedShare() {
		return assignedShare;
	}

	public void setAssignedShare(BigDecimal assignedShare) {
		this.assignedShare = assignedShare;
	}

	public Short getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(Short tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public Short getPayProgress() {
		return payProgress;
	}

	public void setPayProgress(Short payProgress) {
		this.payProgress = payProgress;
	}

	public Short getDocStatus() {
		return docStatus;
	}

	public void setDocStatus(Short docStatus) {
		this.docStatus = docStatus;
	}
	
	public Short getDocumentStatus() {
		return documentStatus;
	}

	public void setDocumentStatus(Short documentStatus) {
		this.documentStatus = documentStatus;
	}

//	public Short getPayStatus() {
//		return payStatus;
//	}
//
//	public void setPayStatus(Short payStatus) {
//		this.payStatus = payStatus;
//	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public Long getHoldingProductID() {
		return holdingProductID;
	}

	public void setHoldingProductID(Long holdingProductID) {
		this.holdingProductID = holdingProductID;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getTradeStatusName() {
		return tradeStatusName;
	}

	public void setTradeStatusName(String tradeStatusName) {
		this.tradeStatusName = tradeStatusName;
	}

	public String getPayProgressName() {
		return payProgressName;
	}

	public void setPayProgressName(String payProgressName) {
		this.payProgressName = payProgressName;
	}

	public String getDocStatusName() {
		return docStatusName;
	}

	public void setDocStatusName(String docStatusName) {
		this.docStatusName = docStatusName;
	}
	
	public String getDocumentStatusName() {
		return documentStatusName;
	}

	public void setDocumentStatusName(String documentStatusName) {
		this.documentStatusName = documentStatusName;
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

	public String getTradeTypeName() {
		return tradeTypeName;
	}

	public void setTradeTypeName(String tradeTypeName) {
		this.tradeTypeName = tradeTypeName;
	}

	public String getPayStatusName() {
		return payStatusName;
	}

	public void setPayStatusName(String payStatusName) {
		this.payStatusName = payStatusName;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public Short getPushStatus() {
		return pushStatus;
	}

	public void setPushStatus(Short pushStatus) {
		this.pushStatus = pushStatus;
	}

	public BigDecimal getPushShare() {
		return pushShare;
	}

	public void setPushShare(BigDecimal pushShare) {
		this.pushShare = pushShare;
	}

	public String getPushTime() {
		return pushTime;
	}

	public void setPushTime(String pushTime) {
		this.pushTime = pushTime;
	}

	public String getPushStatusName() {
		return pushStatusName;
	}

	public void setPushStatusName(String pushStatusName) {
		this.pushStatusName = pushStatusName;
	}

	public String getCommissionStatus() {
		return commissionStatus;
	}

	public void setCommissionStatus(String commissionStatus) {
		this.commissionStatus = commissionStatus;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getBankAccout() {
		return bankAccout;
	}

	public void setBankAccout(String bankAccout) {
		this.bankAccout = bankAccout;
	}

//	public String getCommissionGrantDate() {
//		return commissionGrantDate;
//	}
//
//	public void setCommissionGrantDate(String commissionGrantDate) {
//		this.commissionGrantDate = commissionGrantDate;
//	}
//
//	public String getCommissionGrantAccount() {
//		return commissionGrantAccount;
//	}
//
//	public void setCommissionGrantAccount(String commissionGrantAccount) {
//		this.commissionGrantAccount = commissionGrantAccount;
//	}
//
//	public String getCommissionGrantUserName() {
//		return commissionGrantUserName;
//	}
//
//	public void setCommissionGrantUserName(String commissionGrantUserName) {
//		this.commissionGrantUserName = commissionGrantUserName;
//	}
//
//	public String getCommissionGrantBankName() {
//		return commissionGrantBankName;
//	}
//
//	public void setCommissionGrantBankName(String commissionGrantBankName) {
//		this.commissionGrantBankName = commissionGrantBankName;
//	}
//
//	public String getCommissionGrantRemark() {
//		return commissionGrantRemark;
//	}
//
//	public void setCommissionGrantRemark(String commissionGrantRemark) {
//		this.commissionGrantRemark = commissionGrantRemark;
//	}

	public String getShareTime() {
		return shareTime;
	}

	public void setShareTime(String shareTime) {
		this.shareTime = shareTime;
	}

	public String getrMRemark() {
		return rMRemark;
	}

	public void setrMRemark(String rMRemark) {
		this.rMRemark = rMRemark;
	}
	
	public Short getIsPay() {
		return isPay;
	}

	public void setIsPay(Short isPay) {
		this.isPay = isPay;
	}
	public String getTeamOrOrgName() {
		return teamOrOrgName;
	}

	public void setTeamOrOrgName(String teamOrOrgName) {
		this.teamOrOrgName = teamOrOrgName;
	}

//	public double getCommissionSum() {
//		return commissionSum;
//	}
//
//	public void setCommissionSum(double commissionSum) {
//		this.commissionSum = commissionSum;
//	}

	public OrderProof getOrderProof() {
		return orderProof;
	}

	public void setOrderProof(OrderProof orderProof) {
		this.orderProof = orderProof;
	}

	public Short getProofStatus() {
		return ProofStatus;
	}

	public void setProofStatus(Short proofStatus) {
		ProofStatus = proofStatus;
	}

	public SubProduct getSubProduct() {
		return subProduct;
	}

	public void setSubProduct(SubProduct subProduct) {
		this.subProduct = subProduct;
	}

	public String[] getTradeTypeCollection() {
		return tradeTypeCollection;
	}

	public void setTradeTypeCollection(String[] tradeTypeCollection) {
		this.tradeTypeCollection = tradeTypeCollection;
	}

	public String getQueryCollection() {
		return queryCollection;
	}

	public void setQueryCollection(String queryCollection) {
		this.queryCollection = queryCollection;
	}

	public String getSubProductType() {
		return subProductType;
	}

	public void setSubProductType(String subProductType) {
		this.subProductType = subProductType;
	}

	public String getProofCount() {
		return proofCount;
	}

	public void setProofCount(String proofCount) {
		this.proofCount = proofCount;
	}

	public Long getProductcommissionratioID() {
		return productcommissionratioID;
	}

	public void setProductcommissionratioID(Long productcommissionratioID) {
		this.productcommissionratioID = productcommissionratioID;
	}

	public Short getIsTotalShare() {
		return isTotalShare;
	}

	public void setIsTotalShare(Short isTotalShare) {
		this.isTotalShare = isTotalShare;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public Short getProductType() {
		return productType;
	}

	public void setProductType(Short productType) {
		this.productType = productType;
	}

	public BigDecimal getAffirmAmount() {
		return affirmAmount;
	}

	public void setAffirmAmount(BigDecimal affirmAmount) {
		this.affirmAmount = affirmAmount;
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

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public String getStageTime() {
		return stageTime;
	}

	public void setStageTime(String stageTime) {
		this.stageTime = stageTime;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankNumber() {
		return bankNumber;
	}

	public void setBankNumber(String bankNumber) {
		this.bankNumber = bankNumber;
	}

	public String getBankClientName() {
		return bankClientName;
	}

	public void setBankClientName(String bankClientName) {
		this.bankClientName = bankClientName;
	}

	public Short getBreakReason() {
		return breakReason;
	}

	public void setBreakReason(Short breakReason) {
		this.breakReason = breakReason;
	}

	public Long getMemberAccountID() {
		return memberAccountID;
	}

	public Long getContactInformationID() {
		return contactInformationID;
	}

	public void setMemberAccountID(Long memberAccountID) {
		this.memberAccountID = memberAccountID;
	}

	public void setContactInformationID(Long contactInformationID) {
		this.contactInformationID = contactInformationID;
	}

	public Short getConfirmPay() {
		return confirmPay;
	}

	public void setConfirmPay(Short confirmPay) {
		this.confirmPay = confirmPay;
	}
	
	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactTelphone() {
		return contactTelphone;
	}

	public void setContactTelphone(String contactTelphone) {
		this.contactTelphone = contactTelphone;
	}

	public String getContactPostCode() {
		return contactPostCode;
	}

	public void setContactPostCode(String contactPostCode) {
		this.contactPostCode = contactPostCode;
	}

	public Short getIsScManager() {
		return isScManager;
	}

	public void setIsScManager(Short isScManager) {
		this.isScManager = isScManager;
	}

	public Short getSalerType() {
		return salerType;
	}

	public void setSalerType(Short salerType) {
		this.salerType = salerType;
	}
	
}
