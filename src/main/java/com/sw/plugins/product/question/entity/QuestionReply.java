package com.sw.plugins.product.question.entity;

import com.sw.core.data.entity.RelationEntity;

public class QuestionReply extends RelationEntity{
	private static final long serialVersionUID = 1L;
	
	//问题ID
	private Long questionID;
	//用户类型
	private Integer userType;
	//用户ID
	private Long userID;
	//回复内容
	private String reply;
	//回复时间
	private String replyTime;
	
	public QuestionReply(){
		
	}
	
	public Long getQuestionID() {
		return questionID;
	}

	public void setQuestionID(Long questionID) {
		this.questionID = questionID;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}


}
