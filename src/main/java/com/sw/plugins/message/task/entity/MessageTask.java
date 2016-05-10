package com.sw.plugins.message.task.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.sw.core.data.entity.RelationEntity;

/**
 * @author Spark
 *
 * 消息任务表
 */
public class MessageTask extends RelationEntity {

	private static final long serialVersionUID = 4131836690101306435L;
	
	/* 名称 */
	@Pattern(regexp = "^[a-zA-Z0-9_\u4e00-\u9fa5]{1,100}$")
	private String name;
	/* 代码 */
	@Pattern(regexp = "^[a-zA-Z0-9_]{1,50}$")
	private String code;
	/* 任务类型 1-内置任务 2-自定义任务 */
	private Short type;
	/* 发送类型 1-立即发送 2-定时发送 */
	private Short sendType;
	/* 状态 1-未启用 2-启用 3-暂停  */
	private Short status;
	/* 发送频率 1-按天 2-按周 3-按月 4-按年  */
	private Short sendFrequency;
	/* 月份  */
	private Long quartzMonth;
	/* 周  */
	private Long quartzWeek;
	/* 天  */
	private Long quartzDay;
	/* 时间点  */
	private Long quartzHour;
	/* 表达式  */
	private String quartzExpression;
	/* 是否配置接收人员 */
	private Short isConfigReceiver;
	
	//页面显示
	@Size(min=1,max=100)
	private String templateName;
	@NotNull
	private String templateType;
	private String templateStatus;
	private Long systemTemplateId;
	private Long noteTemplateId;
	private Long emailTemplateId;
	private Long imTemplateId;
	private String systemTemplateCode;
	private String noteTemplateCode;
	private String emailTemplateCode;
	private String imTemplateCode;
	
	private Short systemTemplateStatus;
	private Short noteTemplateStatus;
	private Short emailTemplateStatus;
	private Short imTemplateStatus;
	/*@Size(min=1)*/
	private String systemTemplateContent;
	/*@Size(min=1)*/
	private String noteTemplateContent;
	/*@Size(min=1)*/ 
	private String emailTemplateContent;
	/*@Size(min=1)*/
	private String imTemplateContent;
	/** 接收者角色*/
	private String receiverRoles;
	/* 接收用户id ,json格式*/
	private String receiverUserIDsJSON;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Short getType() {
		return type;
	}
	public void setType(Short type) {
		this.type = type;
	}
	public Short getSendType() {
		return sendType;
	}
	public void setSendType(Short sendType) {
		this.sendType = sendType;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	public Short getSendFrequency() {
		return sendFrequency;
	}
	public void setSendFrequency(Short sendFrequency) {
		this.sendFrequency = sendFrequency;
	}
	public Long getQuartzMonth() {
		return quartzMonth;
	}
	public void setQuartzMonth(Long quartzMonth) {
		this.quartzMonth = quartzMonth;
	}
	public Long getQuartzWeek() {
		return quartzWeek;
	}
	public void setQuartzWeek(Long quartzWeek) {
		this.quartzWeek = quartzWeek;
	}
	public Long getQuartzDay() {
		return quartzDay;
	}
	public void setQuartzDay(Long quartzDay) {
		this.quartzDay = quartzDay;
	}
	public Long getQuartzHour() {
		return quartzHour;
	}
	public void setQuartzHour(Long quartzHour) {
		this.quartzHour = quartzHour;
	}
	public String getQuartzExpression() {
		return quartzExpression;
	}
	public void setQuartzExpression(String quartzExpression) {
		this.quartzExpression = quartzExpression;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getTemplateType() {
		return templateType;
	}
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
	public Short getSystemTemplateStatus() {
		return systemTemplateStatus;
	}
	public void setSystemTemplateStatus(Short systemTemplateStatus) {
		this.systemTemplateStatus = systemTemplateStatus;
	}
	public Short getNoteTemplateStatus() {
		return noteTemplateStatus;
	}
	public void setNoteTemplateStatus(Short noteTemplateStatus) {
		this.noteTemplateStatus = noteTemplateStatus;
	}
	public Short getEmailTemplateStatus() {
		return emailTemplateStatus;
	}
	public void setEmailTemplateStatus(Short emailTemplateStatus) {
		this.emailTemplateStatus = emailTemplateStatus;
	}
	public Short getImTemplateStatus() {
		return imTemplateStatus;
	}
	public void setImTemplateStatus(Short imTemplateStatus) {
		this.imTemplateStatus = imTemplateStatus;
	}
	public String getSystemTemplateContent() {
		return systemTemplateContent;
	}
	public void setSystemTemplateContent(String systemTemplateContent) {
		this.systemTemplateContent = systemTemplateContent;
	}
	public String getNoteTemplateContent() {
		return noteTemplateContent;
	}
	public void setNoteTemplateContent(String noteTemplateContent) {
		this.noteTemplateContent = noteTemplateContent;
	}
	public String getEmailTemplateContent() {
		return emailTemplateContent;
	}
	public void setEmailTemplateContent(String emailTemplateContent) {
		this.emailTemplateContent = emailTemplateContent;
	}
	public String getImTemplateContent() {
		return imTemplateContent;
	}
	public void setImTemplateContent(String imTemplateContent) {
		this.imTemplateContent = imTemplateContent;
	}
	public String getTemplateStatus() {
		return templateStatus;
	}
	public void setTemplateStatus(String templateStatus) {
		this.templateStatus = templateStatus;
	}
	public Long getSystemTemplateId() {
		return systemTemplateId;
	}
	public void setSystemTemplateId(Long systemTemplateId) {
		this.systemTemplateId = systemTemplateId;
	}
	public Long getNoteTemplateId() {
		return noteTemplateId;
	}
	public void setNoteTemplateId(Long noteTemplateId) {
		this.noteTemplateId = noteTemplateId;
	}
	public Long getEmailTemplateId() {
		return emailTemplateId;
	}
	public void setEmailTemplateId(Long emailTemplateId) {
		this.emailTemplateId = emailTemplateId;
	}
	public Long getImTemplateId() {
		return imTemplateId;
	}
	public void setImTemplateId(Long imTemplateId) {
		this.imTemplateId = imTemplateId;
	}
	public String getSystemTemplateCode() {
		return systemTemplateCode;
	}
	public void setSystemTemplateCode(String systemTemplateCode) {
		this.systemTemplateCode = systemTemplateCode;
	}
	public String getNoteTemplateCode() {
		return noteTemplateCode;
	}
	public void setNoteTemplateCode(String noteTemplateCode) {
		this.noteTemplateCode = noteTemplateCode;
	}
	public String getEmailTemplateCode() {
		return emailTemplateCode;
	}
	public void setEmailTemplateCode(String emailTemplateCode) {
		this.emailTemplateCode = emailTemplateCode;
	}
	public String getImTemplateCode() {
		return imTemplateCode;
	}
	public void setImTemplateCode(String imTemplateCode) {
		this.imTemplateCode = imTemplateCode;
	}
	public Short getIsConfigReceiver() {
		return isConfigReceiver;
	}
	public void setIsConfigReceiver(Short isConfigReceiver) {
		this.isConfigReceiver = isConfigReceiver;
	}
	public String getReceiverRoles() {
		return receiverRoles;
	}
	public void setReceiverRoles(String receiverRoles) {
		this.receiverRoles = receiverRoles;
	}
	public String getReceiverUserIDsJSON() {
		return receiverUserIDsJSON;
	}
	public void setReceiverUserIDsJSON(String receiverUserIDsJSON) {
		this.receiverUserIDsJSON = receiverUserIDsJSON;
	}
}
