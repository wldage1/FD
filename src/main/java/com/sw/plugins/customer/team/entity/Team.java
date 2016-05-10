package com.sw.plugins.customer.team.entity;



import com.sw.core.data.entity.RelationEntity;


/**
 * 理财师团队实体
 * @author liubaomin
 *
 */
public class Team extends RelationEntity{ 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1777921715228681153L;
	
	//团队名称
    private String name;
    //团队简称
    private String shortName;

    //状态 1-申请中2-通过3-未通过4-暂停5-注销
    private Integer status;
    //创建者
    private Integer creatorMemberID;
    //有用的申请类型
    private Integer type;
    

	//待审核数量
    private Integer checkCount;
	//理财师团队人员数量
    private Integer memberCount;
    
    
    /*团队申请表中的属性*/
    
	//团队申请表id
    private long checkId;
    //团队申请的状态
    private Integer checkStatus;
    //理财师团队申请审核的类型
    private Integer checkType;
    
	//申请时间
    private String applicationTime;
    //申请人ID
    private Integer applicationMember;
    //申请反馈
    private String applicantFeedback;
    //变更团队管理员ID
    private Integer memberID;
    //申请人姓名
    private String applicationMemberName;
    //指定的管理者姓名
    private String memberName;
    //申请变更的营业执照代码
    private String checkLicenceCode;
    //申请变更的营业执照代码
    private String checkLicenceImage;
	//申请团队证件有效期
    private String checkLicenceExpireTime;
    //团队升级到机构的机构表ID
    private Integer orgId; 
    //团队升级到机构的机构关系表ID
    private Integer rOrgId; 
    public Integer getrOrgId() {
		return rOrgId;
	}
	public void setrOrgId(Integer rOrgId) {
		this.rOrgId = rOrgId;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
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

	//处理人ID
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
	public Team(){  
    }  
    public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
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
}
