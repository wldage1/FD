package com.sw.plugins.market.order.entity;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.sw.core.data.entity.RelationEntity;
import com.sw.plugins.cooperate.provider.entity.Provider;
import com.sw.plugins.customer.member.entity.Member;
import com.sw.plugins.customer.team.entity.Team;
import com.sw.plugins.product.manage.entity.Product;
import com.sw.plugins.system.organization.entity.Organization;

public class RedeemOrder extends RelationEntity {
	private static final long serialVersionUID = 1L;

	private String temp;
	/** 存续产品ID **/
	private Long HoldingProductID;
	/** 交易状态 **/
	private Short tradeStatus;
	/** 交易状态 **/
	private String tradeTime;
	/** 订单号 **/
	private String orderNumber;
	/** 居间公司ID **/
	private Long orgID;
	/** 合同号 **/
	@NotEmpty
	private String contractNumber;
	/** 产品ID **/
	private Long productID;
	/** 子产品ID **/
	private Long subProductID;
	/** 理财师所在关系表ID **/
	private Long teamID;
	/** 理财师ID **/
	private Long memberID;
	/** 客户名称 **/
	@NotEmpty
	private String clientName;
	/** 客户类别 **/
	private Short clientType;
	/** 证件类型 **/
	private Long iDCardType;
	/** 证件号码 **/
	private String iDCard;
	/** 存续产品ID **/
	@NotNull
	private BigDecimal netValue;
	/** 赎回份额 **/
	@NotNull
	private BigDecimal share;
	/** 赎回金额 **/
	private BigDecimal amount;
	/** 开放日 **/
	private String openTime;
	/** 单证邮寄状态 **/
	private Short documentStatus;
	/** 供应商ID **/
	private Long providerID;
	/** 订单备注 **/
	private String remark;
	/** RM备注 **/
	private String rMRemark;
	/** 理财顾问组织名称 **/
	private String teamOrOrgName;

	/****** 查询关联表 ******/
	// (产品表)是否是销控管理员
	private Short isScManager;
	// (产品表)产品销售方
	private Short salerType;
	// 产品表BEAN
	private Product product;
	// 理财顾问BEAN
	private Member member;
	// 供应商BEAN
	private Provider provider;
	// 理财顾问机构BEAN
	private Team team;
	// 机构信息BEAN
	private Organization organization;

	/****** 状态中文转换 ******/
	// 中文单证邮寄状态
	private String documentStatusName;
	// 中文交易状态
	private String tradeStatusName;
	// 中文资金状态
	private String payProgressName;
	// 中文客户类别
	private String clientTypeName;
	// 中文证件类型
	private String iDCardTypeName;
	// 中文交易类型
	private String tradeTypeName;
	// 中文居间费支付状态
	private String payStatusName;
	// 中文供应商
	private String providerName;
	// 中文子产品类型
	private String subProductType;

	/****** 查询条件 ******/
	// 产品名称
	@NotEmpty
	private String productName;
	// 理财顾问名称
	private String memberName;

	private String teamName;
	// 成功 订单中的UpdateTime
	private String updateTime;

	public Long getHoldingProductID() {
		return HoldingProductID;
	}

	public void setHoldingProductID(Long holdingProductID) {
		HoldingProductID = holdingProductID;
	}

	public Short getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(Short tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Long getOrgID() {
		return orgID;
	}

	public void setOrgID(Long orgID) {
		this.orgID = orgID;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
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

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Short getClientType() {
		return clientType;
	}

	public void setClientType(Short clientType) {
		this.clientType = clientType;
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

	public BigDecimal getNetValue() {
		return netValue;
	}

	public void setNetValue(BigDecimal netValue) {
		this.netValue = netValue;
	}

	public BigDecimal getShare() {
		return share;
	}

	public void setShare(BigDecimal share) {
		this.share = share;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getOpenTime() {
		return openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	public Short getDocumentStatus() {
		return documentStatus;
	}

	public void setDocumentStatus(Short documentStatus) {
		this.documentStatus = documentStatus;
	}

	public Long getProviderID() {
		return providerID;
	}

	public void setProviderID(Long providerID) {
		this.providerID = providerID;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getrMRemark() {
		return rMRemark;
	}

	public void setrMRemark(String rMRemark) {
		this.rMRemark = rMRemark;
	}

	public String getTeamOrOrgName() {
		return teamOrOrgName;
	}

	public void setTeamOrOrgName(String teamOrOrgName) {
		this.teamOrOrgName = teamOrOrgName;
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

	public String getDocumentStatusName() {
		return documentStatusName;
	}

	public void setDocumentStatusName(String documentStatusName) {
		this.documentStatusName = documentStatusName;
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

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getSubProductType() {
		return subProductType;
	}

	public void setSubProductType(String subProductType) {
		this.subProductType = subProductType;
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

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
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
