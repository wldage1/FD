package com.sw.plugins.message.task.entity;


public class AppSender {

	private static final long serialVersionUID = 1L;

	private String serviceID;// 服务ID标识
	private String smContent;// 短信内容
	private String sendTarget;// 发送目标
	private String priority;// 优先级
	private String recompleteTimeBegin;// 发送开始日期
	private String recompleteTimeEnd;// 发送结束日期
	private String recompleteHourBegin;// 发送发始时间
	private String recompleteHourEnd;// 发送结束时间
	private String rodeBy;// 路由

	public AppSender() {
	}

	public String getServiceID() {
		return serviceID;
	}

	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}

	public String getSmContent() {
		return smContent;
	}

	public void setSmContent(String smContent) {
		this.smContent = smContent;
	}

	public String getSendTarget() {
		return sendTarget;
	}

	public void setSendTarget(String sendTarget) {
		this.sendTarget = sendTarget;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getRecompleteTimeBegin() {
		return recompleteTimeBegin;
	}

	public void setRecompleteTimeBegin(String recompleteTimeBegin) {
		this.recompleteTimeBegin = recompleteTimeBegin;
	}

	public String getRecompleteTimeEnd() {
		return recompleteTimeEnd;
	}

	public void setRecompleteTimeEnd(String recompleteTimeEnd) {
		this.recompleteTimeEnd = recompleteTimeEnd;
	}

	public String getRecompleteHourBegin() {
		return recompleteHourBegin;
	}

	public void setRecompleteHourBegin(String recompleteHourBegin) {
		this.recompleteHourBegin = recompleteHourBegin;
	}

	public String getRecompleteHourEnd() {
		return recompleteHourEnd;
	}

	public void setRecompleteHourEnd(String recompleteHourEnd) {
		this.recompleteHourEnd = recompleteHourEnd;
	}

	public String getRodeBy() {
		return rodeBy;
	}

	public void setRodeBy(String rodeBy) {
		this.rodeBy = rodeBy;
	}

}
