package com.sw.plugins.message.task.entity;

import com.sw.core.data.entity.RelationEntity;

/**
 * @author Spark
 *
 */
public class MessageTemplate extends RelationEntity {

	private static final long serialVersionUID = -5670709106657301823L;

	/* 消息任务ID */
	private Long messageTaskID;
	/* 名称 */
	private String name;
	/* 模板类型 1-站内 2-短信 3-邮件 4-IM */
	private Short type;
	/* 模板状态 0-无效 1-有效 */
	private Short status;
	/* 模板内容 */
	private String content;
	//模板code
	private String code;
	/*任务名称*/
	private String taskName;
	
	public Long getMessageTaskID() {
		return messageTaskID;
	}
	public void setMessageTaskID(Long messageTaskID) {
		this.messageTaskID = messageTaskID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Short getType() {
		return type;
	}
	public void setType(Short type) {
		this.type = type;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
}
