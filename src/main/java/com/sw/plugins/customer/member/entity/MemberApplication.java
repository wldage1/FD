package com.sw.plugins.customer.member.entity;

import com.sw.core.data.entity.RelationEntity;

/**
 * 理财师变更申请表实体
 * 
 * @author liubaomin
 * 
 */
public class MemberApplication extends RelationEntity {

	private static final long serialVersionUID = -4390790818154113813L;
	/**
	 * 
	 */

	// 组织ID
	private Integer teamID;
	// 状态
	private Integer status;

	/** -- 退组仲裁列表 -- */
	// 理财师名称
	private String memberName;
	// 理财师简称
	private String nickName;
	// 机构或团队名称
	private String teamName;
	// 申请日期
	private String applicationTime;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getTeamID() {
		return teamID;
	}

	public void setTeamID(Integer teamID) {
		this.teamID = teamID;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getApplicationTime() {
		return applicationTime;
	}

	public void setApplicationTime(String applicationTime) {
		this.applicationTime = applicationTime;
	}

}
