package com.sw.plugins.message.task.entity;

import java.util.List;
import java.util.Map;

import com.sw.core.data.entity.RelationEntity;

/**
 * @author Spark
 *
 */
public class SendMessage extends RelationEntity {

	private static final long serialVersionUID = -7229066957378607903L;

	/* 消息任务ID */
	private Long messageTaskID;
	/* 发送方式  1-站内 2-短信 3-邮件 4-IM(发送消息必填项)*/
	private Short sendWay;
	/* 发送者类型 (发送消息必填项)*/
	private Short sendUserType;
	/* 发送者ID (发送消息必填项)*/
	private Long sendUserID;
	/* 消息内容 */
	private String content;
	/* 发送状态 0-未发送 1-已发送 2-发送失败 */
	private Short status;
	/* 发送时间 */
	private String sendTime;
	/* 有效期 */
	private String validTime;
	/* 接收者类型  1-运营方 2-供应商 3-理财师 4-居间公司*/
	private Short receiverUserType;
	/* 接收者*/
	private Long receiverUserID;
	
	private String taseName;
	private String systemMessage;
	private String noteMessage;
	private String emailMessage;
	private String imMessage;
	
	//理财师姓名
	private String memberName;
	//理财师昵称
	private String nickName;
	//理财师性别
	private Short gender;
	//用户信息(发送消息必填项)
	private List<UserMessage> userList;
	//模板参数
	@SuppressWarnings("rawtypes")
	private Map templateParameters;
	//模板code(发送消息必填项)
	private String templateCode;
	//发送方式集合1-站内2-短信3-邮件4-IM 多个用逗号分隔(发送消息必填项)
	private String sendWayStr;
	/* 关联对象类型  1-订单 2-机构申请单 3-产品 4-资讯(发送消息必填项)*/
	private Short relationType;
	
	public Long getMessageTaskID() {
		return messageTaskID;
	}
	public void setMessageTaskID(Long messageTaskID) {
		this.messageTaskID = messageTaskID;
	}
	public Short getSendWay() {
		return sendWay;
	}
	public void setSendWay(Short sendWay) {
		this.sendWay = sendWay;
	}
	public Short getSendUserType() {
		return sendUserType;
	}
	public void setSendUserType(Short sendUserType) {
		this.sendUserType = sendUserType;
	}
	public Long getSendUserID() {
		return sendUserID;
	}
	public void setSendUserID(Long sendUserID) {
		this.sendUserID = sendUserID;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getValidTime() {
		return validTime;
	}
	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}
	public Short getReceiverUserType() {
		return receiverUserType;
	}
	public void setReceiverUserType(Short receiverUserType) {
		this.receiverUserType = receiverUserType;
	}
	public Long getReceiverUserID() {
		return receiverUserID;
	}
	public void setReceiverUserID(Long receiverUserID) {
		this.receiverUserID = receiverUserID;
	}
	public String getSystemMessage() {
		return systemMessage;
	}
	public void setSystemMessage(String systemMessage) {
		this.systemMessage = systemMessage;
	}
	public String getNoteMessage() {
		return noteMessage;
	}
	public void setNoteMessage(String noteMessage) {
		this.noteMessage = noteMessage;
	}
	public String getEmailMessage() {
		return emailMessage;
	}
	public void setEmailMessage(String emailMessage) {
		this.emailMessage = emailMessage;
	}
	public String getImMessage() {
		return imMessage;
	}
	public void setImMessage(String imMessage) {
		this.imMessage = imMessage;
	}
	public String getTaseName() {
		return taseName;
	}
	public void setTaseName(String taseName) {
		this.taseName = taseName;
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
	public Short getGender() {
		return gender;
	}
	public void setGender(Short gender) {
		this.gender = gender;
	}
	public List<UserMessage> getUserList() {
		return userList;
	}
	public void setUserList(List<UserMessage> userList) {
		this.userList = userList;
	}
	@SuppressWarnings("rawtypes")
	public Map getTemplateParameters() {
		return templateParameters;
	}
	@SuppressWarnings("rawtypes")
	public void setTemplateParameters(Map templateParameters) {
		this.templateParameters = templateParameters;
	}
	public String getTemplateCode() {
		return templateCode;
	}
	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}
	public String getSendWayStr() {
		return sendWayStr;
	}
	public void setSendWayStr(String sendWayStr) {
		this.sendWayStr = sendWayStr;
	}
	public Short getRelationType() {
		return relationType;
	}
	public void setRelationType(Short relationType) {
		this.relationType = relationType;
	}
}
