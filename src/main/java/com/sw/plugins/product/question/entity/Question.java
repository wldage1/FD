package com.sw.plugins.product.question.entity;

import com.sw.core.data.entity.RelationEntity;

public class Question extends RelationEntity{
	
	private static final long serialVersionUID = 1L;
	
	//会员ID
	private Long memberID;
	//问题
	private String question;
	//提问时间
	private String askTime;
	//是否处理
	private Integer isAudited;
	//1为网站咨询，2为产品咨询
	private Integer type;
	
	private String reply;
	
	private String replyTime;
	
	private Integer isReleased;
	
	private String memberName;
	
	private String ip;
	
	private Short solved;
	
	private String productName;
	
	private String name;
	
	private String email;
	
	private String phone;
	
	private String branch;
	
	private String nickName;
	
	//居间公司ID
	private Long orgId;
	
	public Question(){
		
	}
	
	public String getNickName(){
		return nickName;
	}
	
	public void setNickName(String nickName){
		this.nickName = nickName;
	}
	
	public String getProductName(){
		return productName;
	}
	
	public void setProductName(String productName){
		this.productName = productName;
	}
	
	public Short getSolved(){
		return solved;
	}
	
	public void setSolved(Short solved){
		this.solved = solved;
	}
	
	public String getMemberName(){
		return memberName;
	}
	
	public void setMemberName(String memberName){
		this.memberName = memberName;
	}
	
	public Long getMemberID() {
		return memberID;
	}

	public void setMemberID(Long memberID) {
		this.memberID = memberID;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAskTime() {
		return askTime;
	}

	public void setAskTime(String askTime) {
		this.askTime = askTime;
	}

	public Integer getIsAudited() {
		return isAudited;
	}

	public void setIsAudited(Integer isAudited) {
		this.isAudited = isAudited;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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
	
	public Integer getIsReleased() {
		return isReleased;
	}

	public void setIsReleased(Integer isReleased) {
		this.isReleased = isReleased;
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public Long getOrgId() {
		return orgId;
	}
	
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}
}

