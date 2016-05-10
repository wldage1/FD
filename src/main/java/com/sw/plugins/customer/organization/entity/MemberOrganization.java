package com.sw.plugins.customer.organization.entity;

import java.math.BigDecimal;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.sw.core.data.entity.RelationEntity;

/**
 * 理财师机构实体
 * 
 * @author liubaomin
 * 
 */
public class MemberOrganization extends RelationEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1777921715228681153L;

	// 机构名称
	@Pattern(regexp = "^[a-zA-Z\\u4e00-\\u9fa5]{1,50}$")
	private String name;
	// 机构简称
	@Pattern(regexp = "^[a-zA-Z\\u4e00-\\u9fa5]{1,10}$")
	private String shortName;
	// 营业执照代码
	@NotEmpty
	private String licenceCode;
	// 营业执照附件
	@NotEmpty
	private String licenceImage;
	// 营业执照有效期
	@NotEmpty
	private String licenceExpireTime;
	// 法定代表
	@Pattern(regexp = "^[a-zA-Z\\u4e00-\\u9fa5]{1,6}$")
	private String representative;
	// 证件类型
	private Integer iDCardType;
	// 证件号码
	@Pattern(regexp = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)")
	private String iDCardNumber;
	// 证件有效期
	@NotEmpty
	private String iDCardExpireTime;
	// 法定代表人手机
	@Pattern(regexp = "^((\\(\\d{2,3}\\))|(\\d{3}\\-))?1[3,8,5]{1}\\d{9}$")
	private String representativePhone;
	// 开户行
	@Pattern(regexp = "^[a-zA-Z\\u4e00-\\u9fa5]{1,20}$")
	private String bankName;
	// 银行帐号
	private String account;
	// 开户人
	@Pattern(regexp = "^[a-zA-Z\\u4e00-\\u9fa5]{1,6}$")
	private String accountHolder;
	// 公司地址
	@NotEmpty
	private String companyAddress;
	// 邮编
	private String postCode;
	// 状态 1-申请中2-通过3-未通过4-暂停5-注销
	private Integer status;
	// 创建者
	private Integer creatorMemberID;
	// 历史团队ID
	private Integer historyTeamID;
	// 晋升时间
	private String promotionTtime;
	// 机构身份标识1：是嘉华销售机构2：其它销售机构
	private Short identity;

	// 有用的申请类型
	private Integer type;

	// 待审核数量
	private Integer checkCount;
	// 理财师机构人员数量
	private Integer memberCount;

	/* 机构申请表中的属性 */

	// 机构申请表id
	private long checkId;
	// 机构申请的状态
	private Integer checkStatus;
	// 理财师机构申请审核的类型
	private Integer checkType;

	// 申请时间
	private String applicationTime;
	// 申请人ID
	private Integer applicationMember;
	// 申请反馈
	private String applicantFeedback;
	// 变更机构管理员ID
	private Integer memberID;
	// 申请人姓名
	private String applicationMemberName;
	// 指定的管理者姓名
	private String memberName;
	// 管理者的昵称
	private String memberNickName;
	// 管理者的手机号
	private String memberPhone;
	// 管理着的email
	private String memberEmail;
	// 渠道经理编号
	private String channelManager;
	// 申请变更的营业执照代码
	private String checkLicenceCode;
	// 申请变更的营业执照代码
	private String checkLicenceImage;
	// 申请机构证件有效期
	private String checkLicenceExpireTime;

	/* 激励费用相关属性 */
	// 激励费用发放详细ID
	private Long incentiveFeeId;
	// 累计成交规模
	private BigDecimal sumInvestAmount;
	// 成功系数
	private Double successRate;
	// 奖励分值
	private Integer rewardScore;
	// 返利分值
	private BigDecimal rebateScore;
	// 居间费总额
	private BigDecimal sumCommissionFee;
	// 返利比例
	private Double rebateRate;
	// 返利金额
	private BigDecimal rebateAmount;
	// 是否发放 0：未发放 1：已发放
	private Short isPay;

	private String payStageTime;

	private Long companyID;

	private Long tempOrgID;
	
	public Long getTempOrgID() {
		return tempOrgID;
	}

	public void setTempOrgID(Long tempOrgID) {
		this.tempOrgID = tempOrgID;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccountHolder() {
		return accountHolder;
	}

	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}

	public String getPromotionTtime() {
		return promotionTtime;
	}

	public void setPromotionTtime(String promotionTtime) {
		this.promotionTtime = promotionTtime;
	}

	public Integer getHistoryTeamID() {
		return historyTeamID;
	}

	public void setHistoryTeamID(Integer historyTeamID) {
		this.historyTeamID = historyTeamID;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getLicenceImage() {
		return licenceImage;
	}

	public void setLicenceImage(String licenceImage) {
		this.licenceImage = licenceImage;
	}

	public String getCheckLicenceImage() {
		return checkLicenceImage;
	}

	public void setCheckLicenceImage(String checkLicenceImage) {
		this.checkLicenceImage = checkLicenceImage;
	}

	public String getApplicationMemberName() {
		return applicationMemberName;
	}

	public void setApplicationMemberName(String applicationMemberName) {
		this.applicationMemberName = applicationMemberName;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public long getCheckId() {
		return checkId;
	}

	public void setCheckId(long checkId) {
		this.checkId = checkId;
	}

	public Integer getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}

	public Integer getCheckType() {
		return checkType;
	}

	public void setCheckType(Integer checkType) {
		this.checkType = checkType;
	}

	public String getApplicationTime() {
		return applicationTime;
	}

	public void setApplicationTime(String applicationTime) {
		this.applicationTime = applicationTime;
	}

	public Integer getApplicationMember() {
		return applicationMember;
	}

	public String getPayStageTime() {
		return payStageTime;
	}

	public void setPayStageTime(String payStageTime) {
		this.payStageTime = payStageTime;
	}

	public void setApplicationMember(Integer applicationMember) {
		this.applicationMember = applicationMember;
	}

	public String getApplicantFeedback() {
		return applicantFeedback;
	}

	public void setApplicantFeedback(String applicantFeedback) {
		this.applicantFeedback = applicantFeedback;
	}

	public Integer getMemberID() {
		return memberID;
	}

	public void setMemberID(Integer memberID) {
		this.memberID = memberID;
	}

	public String getCheckLicenceCode() {
		return checkLicenceCode;
	}

	public void setCheckLicenceCode(String checkLicenceCode) {
		this.checkLicenceCode = checkLicenceCode;
	}

	public String getCheckLicenceExpireTime() {
		return checkLicenceExpireTime;
	}

	public void setCheckLicenceExpireTime(String checkLicenceExpireTime) {
		this.checkLicenceExpireTime = checkLicenceExpireTime;
	}

	public Integer getHandleUserID() {
		return HandleUserID;
	}

	public void setHandleUserID(Integer handleUserID) {
		HandleUserID = handleUserID;
	}

	// 处理人ID
	private Integer HandleUserID;

	public Integer getCheckCount() {
		return checkCount;
	}

	public void setCheckCount(Integer checkCount) {
		this.checkCount = checkCount;
	}

	public Integer getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(Integer memberCount) {
		this.memberCount = memberCount;
	}

	public Integer getCreatorMemberID() {
		return creatorMemberID;
	}

	public void setCreatorMemberID(Integer creatorMemberID) {
		this.creatorMemberID = creatorMemberID;
	}

	public MemberOrganization() {
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getLicenceCode() {
		return licenceCode;
	}

	public void setLicenceCode(String licenceCode) {
		this.licenceCode = licenceCode;
	}

	public String getLicenceExpireTime() {
		return licenceExpireTime;
	}

	public void setLicenceExpireTime(String licenceExpireTime) {
		this.licenceExpireTime = licenceExpireTime;
	}

	public String getRepresentative() {
		return representative;
	}

	public void setRepresentative(String representative) {
		this.representative = representative;
	}

	public Integer getiDCardType() {
		return iDCardType;
	}

	public void setiDCardType(Integer iDCardType) {
		this.iDCardType = iDCardType;
	}

	public String getiDCardNumber() {
		return iDCardNumber;
	}

	public void setiDCardNumber(String iDCardNumber) {
		this.iDCardNumber = iDCardNumber;
	}

	public String getiDCardExpireTime() {
		return iDCardExpireTime;
	}

	public void setiDCardExpireTime(String iDCardExpireTime) {
		this.iDCardExpireTime = iDCardExpireTime;
	}

	public String getRepresentativePhone() {
		return representativePhone;
	}

	public void setRepresentativePhone(String representativePhone) {
		this.representativePhone = representativePhone;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/* 激励费用相关属性 */
	public Long getIncentiveFeeId() {
		return incentiveFeeId;
	}

	public void setIncentiveFeeId(Long incentiveFeeId) {
		this.incentiveFeeId = incentiveFeeId;
	}

	public BigDecimal getSumInvestAmount() {
		return sumInvestAmount;
	}

	public void setSumInvestAmount(BigDecimal sumInvestAmount) {
		this.sumInvestAmount = sumInvestAmount;
	}

	public Double getSuccessRate() {
		return successRate;
	}

	public void setSuccessRate(Double successRate) {
		this.successRate = successRate;
	}

	public Integer getRewardScore() {
		return rewardScore;
	}

	public void setRewardScore(Integer rewardScore) {
		this.rewardScore = rewardScore;
	}

	public BigDecimal getRebateScore() {
		return rebateScore;
	}

	public void setRebateScore(BigDecimal rebateScore) {
		this.rebateScore = rebateScore;
	}

	public BigDecimal getSumCommissionFee() {
		return sumCommissionFee;
	}

	public void setSumCommissionFee(BigDecimal sumCommissionFee) {
		this.sumCommissionFee = sumCommissionFee;
	}

	public Double getRebateRate() {
		return rebateRate;
	}

	public void setRebateRate(Double rebateRate) {
		this.rebateRate = rebateRate;
	}

	public BigDecimal getRebateAmount() {
		return rebateAmount;
	}

	public void setRebateAmount(BigDecimal rebateAmount) {
		this.rebateAmount = rebateAmount;
	}

	public Short getIsPay() {
		return isPay;
	}

	public void setIsPay(Short isPay) {
		this.isPay = isPay;
	}

	public Long getCompanyID() {
		return companyID;
	}

	public void setCompanyID(Long companyID) {
		this.companyID = companyID;
	}

	public String getMemberNickName() {
		return memberNickName;
	}

	public void setMemberNickName(String memberNickName) {
		this.memberNickName = memberNickName;
	}

	public String getMemberPhone() {
		return memberPhone;
	}

	public void setMemberPhone(String memberPhone) {
		this.memberPhone = memberPhone;
	}

	public String getMemberEmail() {
		return memberEmail;
	}

	public void setMemberEmail(String memberEmail) {
		this.memberEmail = memberEmail;
	}

	public String getChannelManager() {
		return channelManager;
	}

	public void setChannelManager(String channelManager) {
		this.channelManager = channelManager;
	}

	public Short getIdentity() {
		return identity;
	}

	public void setIdentity(Short identity) {
		this.identity = identity;
	}

}
