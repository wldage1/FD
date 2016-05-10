package com.sw.plugins.product.comment.entity;

import com.sw.core.data.entity.RelationEntity;

/**
 * 评价记录实体类
 * 
 * @author runchao
 */
public class EvaluateComplaint extends RelationEntity{

	private static final long serialVersionUID = 5215606050203748995L;

	//评价ID
	private Long evaluateId;
	//供应商ID
	private Long providerId;
	//理财师ID
	private Long memberId;
	//处理状态
	private Integer status;
	//评价说明
	private String description;
	//评价指标
	private Integer evaluateQuota;
	//反馈意见
	private String feedback;
	public Long getEvaluateId() {
		return evaluateId;
	}
	public void setEvaluateId(Long evaluateId) {
		this.evaluateId = evaluateId;
	}
	public Long getProviderId() {
		return providerId;
	}
	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getEvaluateQuota() {
		return evaluateQuota;
	}
	public void setEvaluateQuota(Integer evaluateQuota) {
		this.evaluateQuota = evaluateQuota;
	}
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
}
