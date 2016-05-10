package com.sw.plugins.market.docmanage.entity;

import java.math.BigDecimal;

import com.sw.core.data.entity.RelationEntity;

/**
 * 单证实体类
 * 
 * @author runchao
 * 
 */
public class Document extends RelationEntity{

	private static final long serialVersionUID = 1513034035386196673L;
	
	//订单号
	private String orderNumber;
	//平台寄出未用印单证
	private Short isPlatformSentNotusing;
	//平台寄出未用印单证时间
	private String platformSentNotusingTime;
	//网站收到未用印单证
	private Short isNetReceivedNotusing;
	//网站收到未用印单证时间
	private String netReceivedNotusingTime;
	//网站寄出签字未用印单证
	private Short isNetSentSignNotusing;
	//网站寄出签字未用印单证时间
	private String netSentSignNotusingTime;
	//平台收到签字未用印单证
	private Short isPlatformReceivedNotusing;
	//平台收到签字未用印单证时间
	private String platformReceivedNotusingTime;
	//平台寄出签字用印单证
	private Short isPlatformSentSignUsing;
	//平台寄出签字用印单证时间
	private String platformSentSignUsingTime;
	//网站收到签字用印单证
	private Short isNetreceivedSignUsing;
	//网站收到签字用印单证时间
	private String netreceivedSignUsingTime;
	//平台寄出用印单证
	private Short isPlatformSentUsing;
	//平台寄出用印单证时间
	private String platformSentUsingTime;
	//网站收到用印单证
	private Short isNetReceivedUsing;
	//网站收到用印单证时间
	private String netReceivedUsingTime;
	//网站寄出签字用印单证
	private Short isNetSentSignUsing;
	//网站寄出签字用印单证时间
	private String netSentSignUsingTime;
	//平台收到签字用印单证
	private Short isPlatformReceivedSignUsing;
	//平台收到签字用印单证时间
	private String platformReceivedSignUsingTime;
	// 1查询TC_Order表 2查询TC_OrderHistory表
	private Short tableType;
	/*
	 * 认购单证归集流水
	 */
	//操作人
	private Long operatorId;
	//接收人
	private String receivorName;
	//接收人手机
	private String receivorMobilePhone;
	//接收人座机
	private String receivorOfficePhone;
	//接收人地址
	private String receivorAddress;
	//接收人邮编
	private String receivorCode;
	//操作时间
	private String operateTime;
	
	/*
	 * 附加属性
	 */
	//居间公司ID
	private Long orgId;
	//产品ID
	private Long productId;
	//产品简称
	private String productShortName;
	//发行机构简称
	private String providerShortName;
	//客户名称
	private String clientName;
	//理财顾问ID
	private Long memberId;
	//理财顾问姓名
	private String memberName;
	//理财顾问昵称
	private String memberNickName;
	//投资金额
	private BigDecimal investAmount;
	//合同编号
	private String contractNumber;
	//单证状态
	private Short docStatus;
	//合同类型
	private Short type;
	//订单ID
	private Long orderId;
	//单证附件标题
	private String attTitle;
	//单证附件文件名称
	private String attFileName;
	//单证附件文件地址
	private String attFileUrl;
	
	private String memAddress;
	
	private String memPostcode;
	//产品合同前缀
	private String contractPrefix;
	
	private String name;
	
	private String phone;
	
	private String telphone;
	
	private String postCode;
	
	private Long contactInformationID;
	
	public String getMobilePhone() {
		return mobilePhone;
	}
	public String getHomePhone() {
		return homePhone;
	}
	public String getContact() {
		return contact;
	}
	public String getPostcode() {
		return postcode;
	}
	public String getAddress() {
		return address;
	}
	public String getTelephone() {
		return telephone;
	}
	public String getCellphone() {
		return cellphone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	private String mobilePhone;
	
	private String homePhone;
	
	private String contact;
	
	private String postcode;
	
	private String address;
	
	private String telephone;
	
	private String cellphone;
	
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public Short getIsPlatformSentNotusing() {
		return isPlatformSentNotusing;
	}
	public void setIsPlatformSentNotusing(Short isPlatformSentNotusing) {
		this.isPlatformSentNotusing = isPlatformSentNotusing;
	}
	public Short getIsNetReceivedNotusing() {
		return isNetReceivedNotusing;
	}
	public void setIsNetReceivedNotusing(Short isNetReceivedNotusing) {
		this.isNetReceivedNotusing = isNetReceivedNotusing;
	}
	public Short getIsNetSentSignNotusing() {
		return isNetSentSignNotusing;
	}
	public void setIsNetSentSignNotusing(Short isNetSentSignNotusing) {
		this.isNetSentSignNotusing = isNetSentSignNotusing;
	}
	public Short getIsPlatformReceivedNotusing() {
		return isPlatformReceivedNotusing;
	}
	public void setIsPlatformReceivedNotusing(Short isPlatformReceivedNotusing) {
		this.isPlatformReceivedNotusing = isPlatformReceivedNotusing;
	}
	public Short getIsPlatformSentSignUsing() {
		return isPlatformSentSignUsing;
	}
	public void setIsPlatformSentSignUsing(Short isPlatformSentSignUsing) {
		this.isPlatformSentSignUsing = isPlatformSentSignUsing;
	}
	public Short getIsNetreceivedSignUsing() {
		return isNetreceivedSignUsing;
	}
	public void setIsNetreceivedSignUsing(Short isNetreceivedSignUsing) {
		this.isNetreceivedSignUsing = isNetreceivedSignUsing;
	}
	public Short getIsPlatformSentUsing() {
		return isPlatformSentUsing;
	}
	public void setIsPlatformSentUsing(Short isPlatformSentUsing) {
		this.isPlatformSentUsing = isPlatformSentUsing;
	}
	public Short getIsNetReceivedUsing() {
		return isNetReceivedUsing;
	}
	public void setIsNetReceivedUsing(Short isNetReceivedUsing) {
		this.isNetReceivedUsing = isNetReceivedUsing;
	}
	public Short getIsNetSentSignUsing() {
		return isNetSentSignUsing;
	}
	public void setIsNetSentSignUsing(Short isNetSentSignUsing) {
		this.isNetSentSignUsing = isNetSentSignUsing;
	}
	public Short getIsPlatformReceivedSignUsing() {
		return isPlatformReceivedSignUsing;
	}
	public void setIsPlatformReceivedSignUsing(Short isPlatformReceivedSignUsing) {
		this.isPlatformReceivedSignUsing = isPlatformReceivedSignUsing;
	}
	/*
	 * 认购单证归集流水
	 */
	public Long getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}
	public String getReceivorName() {
		return receivorName;
	}
	public void setReceivorName(String receivorName) {
		this.receivorName = receivorName;
	}
	public String getReceivorMobilePhone() {
		return receivorMobilePhone;
	}
	public String getMemAddress() {
		return memAddress;
	}
	public String getMemPostcode() {
		return memPostcode;
	}
	public void setMemAddress(String memAddress) {
		this.memAddress = memAddress;
	}
	public void setMemPostcode(String memPostcode) {
		this.memPostcode = memPostcode;
	}
	public void setReceivorMobilePhone(String receivorMobilePhone) {
		this.receivorMobilePhone = receivorMobilePhone;
	}
	public String getReceivorOfficePhone() {
		return receivorOfficePhone;
	}
	public void setReceivorOfficePhone(String receivorOfficePhone) {
		this.receivorOfficePhone = receivorOfficePhone;
	}
	public String getReceivorAddress() {
		return receivorAddress;
	}
	public void setReceivorAddress(String receivorAddress) {
		this.receivorAddress = receivorAddress;
	}
	public String getReceivorCode() {
		return receivorCode;
	}
	public void setReceivorCode(String receivorCode) {
		this.receivorCode = receivorCode;
	}
	public String getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}
	/*
	 * 附加属性
	 */
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductShortName() {
		return productShortName;
	}
	public void setProductShortName(String productShortName) {
		this.productShortName = productShortName;
	}
	public String getProviderShortName() {
		return providerShortName;
	}
	public void setProviderShortName(String providerShortName) {
		this.providerShortName = providerShortName;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
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
	public BigDecimal getInvestAmount() {
		return investAmount;
	}
	public void setInvestAmount(BigDecimal investAmount) {
		this.investAmount = investAmount;
	}
	public String getContractNumber() {
		return contractNumber;
	}
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	public Short getDocStatus() {
		return docStatus;
	}
	public void setDocStatus(Short docStatus) {
		this.docStatus = docStatus;
	}
	public Short getType() {
		return type;
	}
	public void setType(Short type) {
		this.type = type;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getAttTitle() {
		return attTitle;
	}
	public void setAttTitle(String attTitle) {
		this.attTitle = attTitle;
	}
	public String getAttFileName() {
		return attFileName;
	}
	public void setAttFileName(String attFileName) {
		this.attFileName = attFileName;
	}
	public String getAttFileUrl() {
		return attFileUrl;
	}
	public void setAttFileUrl(String attFileUrl) {
		this.attFileUrl = attFileUrl;
	}
	public Short getTableType() {
		return tableType;
	}
	public void setTableType(Short tableType) {
		this.tableType = tableType;
	}
	public String getContractPrefix() {
		return contractPrefix;
	}
	public void setContractPrefix(String contractPrefix) {
		this.contractPrefix = contractPrefix;
	}
	public String getName() {
		return name;
	}
	public String getPhone() {
		return phone;
	}
	public String getTelphone() {
		return telphone;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public Long getContactInformationID() {
		return contactInformationID;
	}
	public void setContactInformationID(Long contactInformationID) {
		this.contactInformationID = contactInformationID;
	}
	public String getPlatformSentNotusingTime() {
		return platformSentNotusingTime;
	}
	public String getNetReceivedNotusingTime() {
		return netReceivedNotusingTime;
	}
	public String getNetSentSignNotusingTime() {
		return netSentSignNotusingTime;
	}
	public String getPlatformReceivedNotusingTime() {
		return platformReceivedNotusingTime;
	}
	public String getPlatformSentSignUsingTime() {
		return platformSentSignUsingTime;
	}
	public String getNetreceivedSignUsingTime() {
		return netreceivedSignUsingTime;
	}
	public String getPlatformSentUsingTime() {
		return platformSentUsingTime;
	}
	public String getNetReceivedUsingTime() {
		return netReceivedUsingTime;
	}
	public String getNetSentSignUsingTime() {
		return netSentSignUsingTime;
	}
	public String getPlatformReceivedSignUsingTime() {
		return platformReceivedSignUsingTime;
	}
	public void setPlatformSentNotusingTime(String platformSentNotusingTime) {
		this.platformSentNotusingTime = platformSentNotusingTime;
	}
	public void setNetReceivedNotusingTime(String netReceivedNotusingTime) {
		this.netReceivedNotusingTime = netReceivedNotusingTime;
	}
	public void setNetSentSignNotusingTime(String netSentSignNotusingTime) {
		this.netSentSignNotusingTime = netSentSignNotusingTime;
	}
	public void setPlatformReceivedNotusingTime(String platformReceivedNotusingTime) {
		this.platformReceivedNotusingTime = platformReceivedNotusingTime;
	}
	public void setPlatformSentSignUsingTime(String platformSentSignUsingTime) {
		this.platformSentSignUsingTime = platformSentSignUsingTime;
	}
	public void setNetreceivedSignUsingTime(String netreceivedSignUsingTime) {
		this.netreceivedSignUsingTime = netreceivedSignUsingTime;
	}
	public void setPlatformSentUsingTime(String platformSentUsingTime) {
		this.platformSentUsingTime = platformSentUsingTime;
	}
	public void setNetReceivedUsingTime(String netReceivedUsingTime) {
		this.netReceivedUsingTime = netReceivedUsingTime;
	}
	public void setNetSentSignUsingTime(String netSentSignUsingTime) {
		this.netSentSignUsingTime = netSentSignUsingTime;
	}
	public void setPlatformReceivedSignUsingTime(String platformReceivedSignUsingTime) {
		this.platformReceivedSignUsingTime = platformReceivedSignUsingTime;
	}
}
