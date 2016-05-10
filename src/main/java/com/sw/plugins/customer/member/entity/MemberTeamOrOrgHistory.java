package com.sw.plugins.customer.member.entity;



import com.sw.core.data.entity.RelationEntity;


/**
 * 理财师组织经理表实体
 * @author liubaomin
 *
 */
public class MemberTeamOrOrgHistory extends RelationEntity{ 

	private static final long serialVersionUID = -4390790818154113813L;
	/**
	 * 
	 */
		
    //理财师的类型
    private Integer type;

	//组织的id
    private Integer teamOrOrgID;
    //理财师ID
    private Integer memberID;
    //状态
    private Integer status;
    //加入时间
    private String joinTime;
    //退出时间
    private String quitTime;
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getTeamOrOrgID() {
		return teamOrOrgID;
	}
	public void setTeamOrOrgID(Integer teamOrOrgID) {
		this.teamOrOrgID = teamOrOrgID;
	}
	public Integer getMemberID() {
		return memberID;
	}
	public void setMemberID(Integer memberID) {
		this.memberID = memberID;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getJoinTime() {
		return joinTime;
	}
	public void setJoinTime(String joinTime) {
		this.joinTime = joinTime;
	}
	public String getQuitTime() {
		return quitTime;
	}
	public void setQuitTime(String quitTime) {
		this.quitTime = quitTime;
	}
}

    
