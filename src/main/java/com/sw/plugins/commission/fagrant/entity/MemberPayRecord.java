package com.sw.plugins.commission.fagrant.entity;

import java.math.BigDecimal;

import com.sw.core.data.entity.RelationEntity;

/**
 * 理财师居间费支付记录表
 * 
 * @author Spark
 *
 */
public class MemberPayRecord extends RelationEntity  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5399739325963586763L;

	//预计发放时间
	private String payDate;
	//理财师ID
	private Long memberID;
	//到账总金额
	private BigDecimal actualPayAmount;
	//佣金总额
	private BigDecimal amount;
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
	//实际支付金额
	private BigDecimal payAmount;
	//居间费发放账号
	private String accountID;
	//居间费发放账户名
	private String accountName;
	//居间费发放账号开户行
	private String bankName;
	//居间费发放备注
	private String remark;
	//RM备注
	private String rmRemark;
	//支付状态 0-未支付  1-已支付  2-已收到  3-支付失败
	private Short status;
	//原状态（待发放列表为0，发放失败列表为3）
	private Short oldStatus;
	//实际发放时间
	private String payTime;
	//应发开始日期
	private String shoudPayStatrDate;
	//应发结束日期
	private String shouldPayEndDate;
	//计税时间
	private String taxAssessmentTime;
	//报税状态
	private Short bondedStauts;
	
	private String commissionProportionStr;
	//理财顾问
	private String memberName;
	//理财顾问证件号码
	private String iDCard;
	//网签协议ID
	private Long agreementTemplateID;
	//居间费记录表ID
	private Long commissionID;
	//预计开始发放时间
	private String payTimeStart;
	//预计发放结束时间
	private String payTimeEnd;
	//发放状态字符串
	private String statusStr;
	//实际开始发放时间
	private String payDateStart;
	//实际结束发放时间
	private String payDateEnd;
	//TAB页面区别标志
	private String mark;
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	//居间公司ID
	private Long orgID;
	
	private String orderNumber;
	private String clientName;
	private BigDecimal commissionProportion;
	private BigDecimal commission;
	private String auditingTime;

	public String getStatusStr() {
		return statusStr;
	}
	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public Long getMemberID() {
		return memberID;
	}
	public void setMemberID(Long memberID) {
		this.memberID = memberID;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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
	public BigDecimal getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}
	public String getAccountID() {
		return accountID;
	}
	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRmRemark() {
		return rmRemark;
	}
	public void setRmRemark(String rmRemark) {
		this.rmRemark = rmRemark;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public Short getOldStatus() {
		return oldStatus;
	}
	public void setOldStatus(Short oldStatus) {
		this.oldStatus = oldStatus;
	}
	public String getShoudPayStatrDate() {
		return shoudPayStatrDate;
	}
	public void setShoudPayStatrDate(String shoudPayStatrDate) {
		this.shoudPayStatrDate = shoudPayStatrDate;
	}
	public String getShouldPayEndDate() {
		return shouldPayEndDate;
	}
	public void setShouldPayEndDate(String shouldPayEndDate) {
		this.shouldPayEndDate = shouldPayEndDate;
	}
	public String getTaxAssessmentTime() {
		return taxAssessmentTime;
	}
	public void setTaxAssessmentTime(String taxAssessmentTime) {
		this.taxAssessmentTime = taxAssessmentTime;
	}
	public Short getBondedStauts() {
		return bondedStauts;
	}
	public void setBondedStauts(Short bondedStauts) {
		this.bondedStauts = bondedStauts;
	}
	public String getiDCard() {
		return iDCard;
	}
	public void setiDCard(String iDCard) {
		this.iDCard = iDCard;
	}
	public Long getAgreementTemplateID() {
		return agreementTemplateID;
	}
	public void setAgreementTemplateID(Long agreementTemplateID) {
		this.agreementTemplateID = agreementTemplateID;
	}
	public Long getCommissionID() {
		return commissionID;
	}
	public void setCommissionID(Long commissionID) {
		this.commissionID = commissionID;
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
	public String getPayDateStart() {
		return payDateStart;
	}
	public void setPayDateStart(String payDateStart) {
		this.payDateStart = payDateStart;
	}
	public String getPayDateEnd() {
		return payDateEnd;
	}
	public void setPayDateEnd(String payDateEnd) {
		this.payDateEnd = payDateEnd;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
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
	public String getAuditingTime() {
		return auditingTime;
	}
	public void setAuditingTime(String auditingTime) {
		this.auditingTime = auditingTime;
	}
	public Long getOrgID() {
		return orgID;
	}
	public void setOrgID(Long orgID) {
		this.orgID = orgID;
	}
	public String getCommissionProportionStr() {
		return commissionProportionStr;
	}
	public void setCommissionProportionStr(String commissionProportionStr) {
		this.commissionProportionStr = commissionProportionStr;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public BigDecimal getActualPayAmount() {
		return actualPayAmount;
	}
	public void setActualPayAmount(BigDecimal actualPayAmount) {
		this.actualPayAmount = actualPayAmount;
	}
	
}
