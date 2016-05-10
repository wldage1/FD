package com.sw.plugins.customer.team.entity;



import com.sw.core.data.entity.RelationEntity;


/**
 * 机构申请表实体
 * @author liubaomin
 *
 */
public class TeamOrOrgApplication extends RelationEntity{ 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1777921715228681153L;
	//申请机构ID
    private Integer teamID;
	//机构申请的状态
    private Integer status;
    //理财师机构申请审核的类型
    private Integer type;
	//申请时间
    private String applicationTime;
    //申请人ID
    private Integer applicationMember;
    //申请反馈
    private String applicantFeedback;
    //变更机构管理员ID
    private Integer memberID;
    //申请变更的营业执照代码
    private String licenceCode;
    //营业执照附件
    private String licenceImage;
    //机构证件有效期
    private String licenceExpireTime;
    //处理人ID
    private Integer handleUserID;
    //机构名称
    private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getTeamID() {
		return teamID;
	}
	public void setTeamID(Integer teamID) {
		this.teamID = teamID;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
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
	public String getLicenceCode() {
		return licenceCode;
	}
	public void setLicenceCode(String licenceCode) {
		this.licenceCode = licenceCode;
	}
	public String getLicenceImage() {
		return licenceImage;
	}
	public void setLicenceImage(String licenceImage) {
		this.licenceImage = licenceImage;
	}
	public String getLicenceExpireTime() {
		return licenceExpireTime;
	}
	public void setLicenceExpireTime(String licenceExpireTime) {
		this.licenceExpireTime = licenceExpireTime;
	}
	public Integer getHandleUserID() {
		return handleUserID;
	}
	public void setHandleUserID(Integer handleUserID) {
		this.handleUserID = handleUserID;
	}
    
}
