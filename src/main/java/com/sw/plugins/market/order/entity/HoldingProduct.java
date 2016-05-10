package com.sw.plugins.market.order.entity;

import java.math.BigDecimal;

import com.sw.core.data.entity.RelationEntity;
import com.sw.plugins.cooperate.provider.entity.Provider;
import com.sw.plugins.customer.member.entity.Member;
import com.sw.plugins.customer.team.entity.Team;
import com.sw.plugins.product.manage.entity.Product;
import com.sw.plugins.system.organization.entity.Organization;

/**
 * 存续产品
 * @author liushuai
 *
 */
public class HoldingProduct extends RelationEntity {

	private static final long serialVersionUID = 1L;
	/** 合同编号 **/
	private String contractNumber;
	/** 居间公司ID **/
	private Long orgID;
	/** 产品ID **/
	private Long productID;
	/** 子产品ID **/
	private Long subProductID;
	/** 机构ID **/
	private Long teamID;
	/** 理财顾问ID **/
	private Long memberID;
	/** 持有份额 **/
	private BigDecimal share;
	/** 投资金额 **/
	private BigDecimal investAmount;
	/** 产品期限（月） **/
	private Integer deadline;
	/** 客户类别 **/
	private Short clientType;
	/** 客户名称 **/
	private String clientName;
	/** 证件类型 **/
	private Long iDCardType;
	/** 证件号码 **/
	private String iDCard;
	/** 初次购买时间 **/
	private String purchaseTime;
	/** 最后交易时间 **/
	private String lastTradeTime;
	/** 备注 **/
	private String remark;
	
	private String productName;

	/** 产品 **/
	private Product product;
	/** 理财顾问 **/
	private Member member;
	/** 供应商 **/
	private Provider provider;
	/** 居间机构 **/
	private Organization organization;
	/** 理财顾问团队 */
	private Team team;
	
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
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
	public Organization getOrganization() {
		return organization;
	}
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public String getContractNumber() {
		return contractNumber;
	}
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
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
	public BigDecimal getShare() {
		return share;
	}
	public void setShare(BigDecimal share) {
		this.share = share;
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
	public Short getClientType() {
		return clientType;
	}
	public void setClientType(Short clientType) {
		this.clientType = clientType;
	}
	public String getClientName() {
		return clientName;
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
	public String getPurchaseTime() {
		return purchaseTime;
	}
	public void setPurchaseTime(String purchaseTime) {
		this.purchaseTime = purchaseTime;
	}
	public String getLastTradeTime() {
		return lastTradeTime;
	}
	public void setLastTradeTime(String lastTradeTime) {
		this.lastTradeTime = lastTradeTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
}