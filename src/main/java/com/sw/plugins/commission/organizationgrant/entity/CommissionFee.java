package com.sw.plugins.commission.organizationgrant.entity;

import com.sw.core.data.entity.RelationEntity;

public class CommissionFee extends RelationEntity{

	private static final long serialVersionUID = -4592968745927962169L;
	
	public CommissionFee(){
		
	}
	
	private Long orderID;
	
	private Long memberID;
	
	private Long teamID;
	
	private Long orgID;
	
	private Long productID;
	
	private Long subProductID;
	
	private Double payAmount;
	
	private String holdingPhase;
	
	private Double issuanceCostRate;
	
	private Double timeCoefficient;
	
	private Double serviceChargeCoefficient;
	
	private Double incentiveFeeRate;
	
	private Double commissionProportion;
	
	private Double commission;
	
	private Double payCommission;
	
	private Long agreementTemplateID;
	
	private Long orgContractID;
	
	private Short payStatus;	
	
	//***************关联其他表数据字段*******************
	private String memberName;
	
	private String memberNickName;
	
	private String teamShortName;
	
	private String productShortName;
	
	private String clientName;
	
	private String contractNumber;
	
	private String orderNumber;
	
	private Double investAmount;
	
	private String tradeTypeName;
	
	private Short tradeType;
	
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
	
	private String productName;
	
	public String getProductName(){
		return productName;
	}
	
	public void setProductName(String productName){
		this.productName = productName;
	}
	
	public Float getNetValue(){
		return netValue;
	}
	
	public void setNetValue(Float netValue){
		this.netValue = netValue;
	}
	
	public String getTeamOrOrgRemark(){
		return teamOrOrgRemark;
	}
	
	public void setTeamOrOrgRemark(String teamOrOrgRemark){
		this.teamOrOrgRemark = teamOrOrgRemark;
	}
	
	public String getTeamName(){
		return teamName;
	}
	
	public void setTeamName(String teamName){
		this.teamName = teamName;
	}
	
	public String getiDCard(){
		return iDCard;
	}
	
	public void setiDCard(String iDCard){
		this.iDCard = iDCard;
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

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
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

	public Double getInvestAmount() {
		return investAmount;
	}

	public void setInvestAmount(Double investAmount) {
		this.investAmount = investAmount;
	}

	public String getHoldingPhase() {
		return holdingPhase;
	}

	public void setHoldingPhase(String holdingPhase) {
		this.holdingPhase = holdingPhase;
	}

	public Double getIssuanceCostRate() {
		return issuanceCostRate;
	}

	public void setIssuanceCostRate(Double issuanceCostRate) {
		this.issuanceCostRate = issuanceCostRate;
	}

	public Double getTimeCoefficient() {
		return timeCoefficient;
	}

	public void setTimeCoefficient(Double timeCoefficient) {
		this.timeCoefficient = timeCoefficient;
	}

	public Double getServiceChargeCoefficient() {
		return serviceChargeCoefficient;
	}

	public void setServiceChargeCoefficient(Double serviceChargeCoefficient) {
		this.serviceChargeCoefficient = serviceChargeCoefficient;
	}

	public Double getIncentiveFeeRate() {
		return incentiveFeeRate;
	}

	public void setIncentiveFeeRate(Double incentiveFeeRate) {
		this.incentiveFeeRate = incentiveFeeRate;
	}

	public Double getCommissionProportion() {
		return commissionProportion;
	}

	public void setCommissionProportion(Double commissionProportion) {
		this.commissionProportion = commissionProportion;
	}

	public Double getCommission() {
		return commission;
	}

	public void setCommission(Double commission) {
		this.commission = commission;
	}

	public Double getPayCommission() {
		return payCommission;
	}

	public void setPayCommission(Double payCommission) {
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
	
	public Double getPayAmount(){
		return payAmount;
	}
	
	public void setPayAmount(Double payAmount){
		this.payAmount = payAmount;
	}
	
	public String getTradeTypeName(){
		return tradeTypeName;
	}
	
	public void setTradeTypeName(String tradeTypeName){
		this.tradeTypeName = tradeTypeName;
	}
	
	public Short getTradeType(){
		return tradeType;
	}
	
	public void setTradeType(Short tradeType){
		this.tradeType = tradeType;
	}
	
	public String getFileUrl(){
		return fileUrl;
	}
	
	public void setFileUrl(String fileUrl){
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
}
