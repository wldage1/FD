package com.sw.plugins.message.task.entity;

import com.sw.core.data.entity.RelationEntity;

/**
 * @author Spark
 *
 */
public class ReceiverMessage extends RelationEntity {

	private static final long serialVersionUID = 5724243962232591962L;

	/* 接收者类型  1-运营方 2-供应商 3-理财师 4-居间公司*/
	private Short receiverUserType;
	/* 接收者ID */
	private Long receiverUserID;
	/* 关联对象类型  1-订单 2-机构申请单 3-产品 4-资讯*/
	private Short type;
	/* 关联对象ID */
	private Long sourceID;
	/* 标题 */
	private String title;
	/* 是否读取  0-否 1-是*/
	private Short isReaded;
	/* 发送者类型  1-运营方 2-供应商 3-理财师 4-居间公司*/
	private Short sendUserType;
	/* 发送者 */ 
	private Long sendUserID;
	/* 有效期 */
	private String validTime;
	/* 读取时间 */
	private String readTime;
	/* 发送消息ID */
	private Long sendMessageID;
	/* 发送方式 */
	private Short sendWay;
	/* 发送者 */
	private String sendUserName;
	/* 接收者 */
	private String receiverName;
	
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
	public Short getType() {
		return type;
	}
	public void setType(Short type) {
		this.type = type;
	}
	public Long getSourceID() {
		return sourceID;
	}
	public void setSourceID(Long sourceID) {
		this.sourceID = sourceID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Short getIsReaded() {
		return isReaded;
	}
	public void setIsReaded(Short isReaded) {
		this.isReaded = isReaded;
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
	public String getValidTime() {
		return validTime;
	}
	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}
	public String getReadTime() {
		return readTime;
	}
	public void setReadTime(String readTime) {
		this.readTime = readTime;
	}
	public Long getSendMessageID() {
		return sendMessageID;
	}
	public void setSendMessageID(Long sendMessageID) {
		this.sendMessageID = sendMessageID;
	}
	public Short getSendWay() {
		return sendWay;
	}
	public void setSendWay(Short sendWay) {
		this.sendWay = sendWay;
	}
	public String getSendUserName() {
		return sendUserName;
	}
	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	
}
