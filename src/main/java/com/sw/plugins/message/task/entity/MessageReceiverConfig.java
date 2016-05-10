package com.sw.plugins.message.task.entity;

import com.sw.core.data.entity.RelationEntity;

/**
 *  消息接收者设置
 *  @author sean
 *  @version 1.0
 *  </pre>
 *  Created on :下午2:27:45
 *  LastModified:
 *  History:
 *  </pre>
 */
public class MessageReceiverConfig extends RelationEntity{
	/* 消息任务ID */
	private Long messageTaskID;
	/* 接收者ID */
	private Long receiverID;
	/* 接收者类型 1-运营方2-供应商3-理财师（暂时保留）4-居间公司5-角色*/
	private Long receiverType;
	
	public Long getReceiverID() {
		return receiverID;
	}
	public void setReceiverID(Long receiverID) {
		this.receiverID = receiverID;
	}
	public Long getReceiverType() {
		return receiverType;
	}
	public void setReceiverType(Long receiverType) {
		this.receiverType = receiverType;
	}
	public Long getMessageTaskID() {
		return messageTaskID;
	}
	public void setMessageTaskID(Long messageTaskID) {
		this.messageTaskID = messageTaskID;
	}

}
