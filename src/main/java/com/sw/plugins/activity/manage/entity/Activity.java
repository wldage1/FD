package com.sw.plugins.activity.manage.entity;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.sw.core.data.entity.RelationEntity;

/**
 * 活动实体
 * 
 * @author Erun
 */
public class Activity extends RelationEntity{

	private static final long serialVersionUID = -3650472166859923345L;
	
	//活动名称
	@Size(min=1, max=50)
	private String name;
	//活动类型
	private Integer type;
	//活动状态
	private Integer status;
	//活动开始时间
	@NotEmpty
	private String startTime;
	//活动结束时间
	@NotEmpty
	private String endTime;
	//活动描述
	private String description;
	//活动网页地址
	private String url;
	//活动模板名称
	private String TempletName;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTempletName() {
		return TempletName;
	}
	public void setTempletName(String templetName) {
		TempletName = templetName;
	}
	
}
