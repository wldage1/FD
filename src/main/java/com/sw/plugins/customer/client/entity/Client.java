package com.sw.plugins.customer.client.entity;

import com.sw.core.data.entity.RelationEntity;

/**
 * 理财顾问客户实体
 * 
 * @author runchao
 */
public class Client extends RelationEntity{

	private static final long serialVersionUID = 7664503791060247771L;

	//理财顾问ID
	private Long memberId;
	//理财顾问姓名
	private String memberName;
	//理财顾问昵称
	private String memberNickName;
	//客户类别
	private Integer type;
	//客户名称
	private String name;
	//性别
	private Long sex;
	//生日
	private String birthday;
	//格式化的生日
	private String formatBirthday;
	//证件类型
	private Long idCardType;
	//证件号码
	private String idCard;
	//证件有效期
	private String idCardValid;
	//格式化的证件有效期
	private String formatIdCardValid;
	
	//信仰
	private String belief;
	//国籍
	private String nationality;
	//手机号码
	private String mobilePhone;
	//电话号码
	private String phone;
	//省份
	private String province;
	//城市
	private String city;
	//行业
	private String sourceIndustry;
	//婚姻状况
	private Long married;
	//职业
	private Long occupation;
	//教育程度
	private String education;
	//职位
	private String memberPosition;
	//工作状态
	private String workStatus;
	//月收入
	private Float income;
	//收入范围
	private String incomeRangeID;
	//兴趣爱好
	private String hobby;
	//电子邮箱
	private String email;
	//家庭地址
	private String homeAddress;
	//联系地址
	private String contactAddress;
	//邮编
	private String postCode;
	//风险偏好
	private String ventureTrend;
	//所在公司
	private String company;
	
	//公司行业
	private String companyIndustry;
	//意向产品
	private String intentionProduct;
	//目前投资情况
	private String investmentSituation;
	//创建时间
	private String createTime;
	//更新时间
	private String modifyTime;
	//创建者
	private Long creatorUserID;
	//状态
	private Integer status;
	//是否开放给机构
	private Short isOpenToOrg;
	
	public String getFormatBirthday() {
		return formatBirthday;
	}
	public void setFormatBirthday(String formatBirthday) {
		this.formatBirthday = formatBirthday;
	}
	public String getFormatIdCardValid() {
		return formatIdCardValid;
	}
	public void setFormatIdCardValid(String formatIdCardValid) {
		this.formatIdCardValid = formatIdCardValid;
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getSex() {
		return sex;
	}
	public void setSex(Long sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public Long getIdCardType() {
		return idCardType;
	}
	public void setIdCardType(Long idCardType) {
		this.idCardType = idCardType;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getIdCardValid() {
		return idCardValid;
	}
	public void setIdCardValid(String idCardValid) {
		this.idCardValid = idCardValid;
	}
	public String getBelief() {
		return belief;
	}
	public void setBelief(String belief) {
		this.belief = belief;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getSourceIndustry() {
		return sourceIndustry;
	}
	public void setSourceIndustry(String sourceIndustry) {
		this.sourceIndustry = sourceIndustry;
	}
	public Long getMarried() {
		return married;
	}
	public void setMarried(Long married) {
		this.married = married;
	}
	public Long getOccupation() {
		return occupation;
	}
	public void setOccupation(Long occupation) {
		this.occupation = occupation;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getMemberPosition() {
		return memberPosition;
	}
	public void setMemberPosition(String memberPosition) {
		this.memberPosition = memberPosition;
	}
	public String getWorkStatus() {
		return workStatus;
	}
	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}
	public Float getIncome() {
		return income;
	}
	public void setIncome(Float income) {
		this.income = income;
	}
	public String getIncomeRangeID() {
		return incomeRangeID;
	}
	public void setIncomeRangeID(String incomeRangeID) {
		this.incomeRangeID = incomeRangeID;
	}
	public String getHobby() {
		return hobby;
	}
	public void setHobby(String hobby) {
		this.hobby = hobby;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	public String getContactAddress() {
		return contactAddress;
	}
	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getVentureTrend() {
		return ventureTrend;
	}
	public void setVentureTrend(String ventureTrend) {
		this.ventureTrend = ventureTrend;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCompanyIndustry() {
		return companyIndustry;
	}
	public void setCompanyIndustry(String companyIndustry) {
		this.companyIndustry = companyIndustry;
	}
	public String getIntentionProduct() {
		return intentionProduct;
	}
	public void setIntentionProduct(String intentionProduct) {
		this.intentionProduct = intentionProduct;
	}
	public String getInvestmentSituation() {
		return investmentSituation;
	}
	public void setInvestmentSituation(String investmentSituation) {
		this.investmentSituation = investmentSituation;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public Long getCreatorUserID() {
		return creatorUserID;
	}
	public void setCreatorUserID(Long creatorUserID) {
		this.creatorUserID = creatorUserID;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Short getIsOpenToOrg() {
		return isOpenToOrg;
	}
	public void setIsOpenToOrg(Short isOpenToOrg) {
		this.isOpenToOrg = isOpenToOrg;
	}
}
