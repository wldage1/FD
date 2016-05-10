package com.sw.plugins.customer.member.entity;

import javax.validation.constraints.Pattern;

import com.sw.core.data.entity.RelationEntity;

/**
 * 理财师实体
 * @author liubaomin
 *
 */
public class Member extends RelationEntity{ 

	private static final long serialVersionUID = 225402170734101963L;
		
	//理财师机构ID
    private Integer teamID;
	//登录帐号
    private String account;
    //密码
    private String password;
    //姓名
    private String name;
    //昵称
    private String nickName;
    //头像
    private String portrait;
    //个性签名
    private String individualitySignature;
    //类型 1-普通理财师 2-机构管理者
    private Integer type;
    //荣誉
    private Integer level;
    //积分
    private Integer points;
    //证件类型
    private Integer iDCardType;
    //证件号码
    private String iDCard;
    //证件附件
    private String cardImage;
	//省份
    private Integer province;
    //城市
    private Integer city;
	//岗位
    private String position;
    //性别 1:男性2:女性3:其他
    private Integer gender;
    //生日
    private String birthday;
    //格式化的生日
    private String birthdayFormat;
	//移动电话
    private String mobilePhone;
	//家庭电话
    private String homePhone;
    //邮件
    private String email;
    //QQ
    private String qQ;
    //微信
    private String weiXin;
    //微博
    private String weiBo;
    //邮编
    private String postCode;
    //邮箱验证码
    private String emailValidateCode;
    //短信验证码
    private String sMValidateCode;
    //详细地址
    private String address;
    //单证邮寄地址
    private String contractAddress;
	//状态
    private Integer status;
    //删除状态
    private Integer delStatus;
    //风格
    private String style;
    //密码提示问题
    private String question;
    //密码提示答案
    private String answer;
    //注册日期
    private String registerTime;
    //最后登录IP
    private String lastLoginIP;
    //最后登录时间
    private String lastLoginTime;
    //待审核数量
    private Integer checkCount;
    //开户行
    private String bank;
    //卡号
    private String card;
    //在途订单数量
    private Integer orderCount;
	//账户名
    private String bankAccout;
    //团队升级到机构的机构ID
    private long orgID;
    
    /*理财师变更申请表实体属性*/
	
	//待审核的申请ID
    private long checkId;
    //待审核的类型
    private Integer checkType;
    //理财师的id
    private Integer checkMemberID;
    //运营方ID
    private Integer checkUserID;
    //状态
    private Integer checkStatus;
    //审核意见
    private String applicantFeedback;
    //变更时间
    private String changedTime;
    //新密码
    @Pattern(regexp = "[A-Za-z0-9]{6,16}")
    private String newPassword;
    //确认密码
    @Pattern(regexp = "[A-Za-z0-9]{6,16}")
    private String confirmPwd;
    //申请人姓名
    private String checkMemberName;
    //省名
    private String provinceName;
	//城市名
    private String cityName;
    //所属机构
    private String teamName;
    //所属机构类型
    private String orgType;
    //理财师同意升级状态
    private Integer agreeStatus;
    //理财师客户数量
    private Long clientCount;
    
    /*
	 * 联系方式
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
	//是否默认
	private Short contactIsDefault;
	
	public Integer getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}
	public String getContractAddress() {
		return contractAddress;
	}
	public void setContractAddress(String contractAddress) {
		this.contractAddress = contractAddress;
	}
    public String getCard() {
		return card;
	}
	public void setCard(String card) {
		this.card = card;
	}
    public String getBirthdayFormat() {
		return birthdayFormat;
	}
	public void setBirthdayFormat(String birthdayFormat) {
		this.birthdayFormat = birthdayFormat;
	}
    public Integer getAgreeStatus() {
		return agreeStatus;
	}
	public void setAgreeStatus(Integer agreeStatus) {
		this.agreeStatus = agreeStatus;
	}
	public long getOrgID() {
		return orgID;
	}
	public void setOrgID(long orgID) {
		this.orgID = orgID;
	}
    public String getOrgType() {
		return orgType;
	}
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
    public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getConfirmPwd() {
		return confirmPwd;
	}
	public void setConfirmPwd(String confirmPwd) {
		this.confirmPwd = confirmPwd;
	}
	
    public String getCardImage() {
		return cardImage;
	}
	public void setCardImage(String cardImage) {
		this.cardImage = cardImage;
	}
    public Integer getCheckCount() {
		return checkCount;
	}
	public void setCheckCount(Integer checkCount) {
		this.checkCount = checkCount;
	}
    public String getCheckMemberName() {
		return checkMemberName;
	}
	public void setCheckMemberName(String checkMemberName) {
		this.checkMemberName = checkMemberName;
	}
	public long getCheckId() {
		return checkId;
	}
	public void setCheckId(long checkId) {
		this.checkId = checkId;
	}
	public Integer getCheckType() {
		return checkType;
	}
	public void setCheckType(Integer checkType) {
		this.checkType = checkType;
	}
	public Integer getCheckMemberID() {
		return checkMemberID;
	}
	public void setCheckMemberID(Integer checkMemberID) {
		this.checkMemberID = checkMemberID;
	}
	public Integer getCheckUserID() {
		return checkUserID;
	}
	public void setCheckUserID(Integer checkUserID) {
		this.checkUserID = checkUserID;
	}
	public Integer getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}
	public String getApplicantFeedback() {
		return applicantFeedback;
	}
	public void setApplicantFeedback(String applicantFeedback) {
		this.applicantFeedback = applicantFeedback;
	}
	public String getChangedTime() {
		return changedTime;
	}
	public void setChangedTime(String changedTime) {
		this.changedTime = changedTime;
	}
	public Integer getTeamID() {
		return teamID;
	}
	public void setTeamID(Integer teamID) {
		this.teamID = teamID;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPortrait() {
		return portrait;
	}
	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}
	public String getIndividualitySignature() {
		return individualitySignature;
	}
	public void setIndividualitySignature(String individualitySignature) {
		this.individualitySignature = individualitySignature;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getPoints() {
		return points;
	}
	public void setPoints(Integer points) {
		this.points = points;
	}
	public Integer getiDCardType() {
		return iDCardType;
	}
	public void setiDCardType(Integer iDCardType) {
		this.iDCardType = iDCardType;
	}
	public String getiDCard() {
		return iDCard;
	}
	public void setiDCard(String iDCard) {
		this.iDCard = iDCard;
	}
	public Integer getProvince() {
		return province;
	}
	public void setProvince(Integer province) {
		this.province = province;
	}
	public Integer getCity() {
		return city;
	}
	public void setCity(Integer city) {
		this.city = city;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public Integer getGender() {
		return gender;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getHomePhone() {
		return homePhone;
	}
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getqQ() {
		return qQ;
	}
	public void setqQ(String qQ) {
		this.qQ = qQ;
	}
	public String getWeiXin() {
		return weiXin;
	}
	public void setWeiXin(String weiXin) {
		this.weiXin = weiXin;
	}
	public String getWeiBo() {
		return weiBo;
	}
	public void setWeiBo(String weiBo) {
		this.weiBo = weiBo;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getEmailValidateCode() {
		return emailValidateCode;
	}
	public void setEmailValidateCode(String emailValidateCode) {
		this.emailValidateCode = emailValidateCode;
	}
	public String getsMValidateCode() {
		return sMValidateCode;
	}
	public void setsMValidateCode(String sMValidateCode) {
		this.sMValidateCode = sMValidateCode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getDelStatus() {
		return delStatus;
	}
	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}
	public String getLastLoginIP() {
		return lastLoginIP;
	}
	public void setLastLoginIP(String lastLoginIP) {
		this.lastLoginIP = lastLoginIP;
	}
	public String getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getBankAccout() {
		return bankAccout;
	}
	public void setBankAccout(String bankAccout) {
		this.bankAccout = bankAccout;
	}
	public Long getClientCount() {
		return clientCount;
	}
	public void setClientCount(Long clientCount) {
		this.clientCount = clientCount;
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
	
	public Short getContactIsDefault() {
		return contactIsDefault;
	}

	public void setContactIsDefault(Short contactIsDefault) {
		this.contactIsDefault = contactIsDefault;
	}
	
}

    
