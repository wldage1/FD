package com.sw.plugins.customer.member.entity;



import com.sw.core.data.entity.RelationEntity;


/**
 * 理财师变更申请表实体
 * @author liubaomin
 *
 */
public class MemberChangeApplication extends RelationEntity{ 

	private static final long serialVersionUID = -4390790818154113813L;
	/**
	 * 
	 */
		
    //待审核的类型
    private Integer type;
	//理财师的id
    private Integer memberID;
    //运营方ID
    private Integer userID;
    //状态
    private Integer status;
    //审核意见
    private String applicantFeedback;
    //变更时间
    private String changedTime;
   //理财师姓名
    private String name;
    //理财师姓名
    private String nickName;
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getMemberID() {
		return memberID;
	}
	public void setMemberID(Integer memberID) {
		this.memberID = memberID;
	}
	public Integer getUserID() {
		return userID;
	}
	public void setUserID(Integer userID) {
		this.userID = userID;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
}

    
