package com.sw.plugins.product.comment.entity;

import com.sw.core.data.entity.RelationEntity;

/**
 * 产品评价实体类
 * 
 * @author runchao
 */
public class ProductEvaluate extends RelationEntity{

	private static final long serialVersionUID = 8426837409071667562L;

	//产品ID
	private Long productId;
	//产品名称
	private String productName;
	//产品简称
	private String productShortName;
	//产品类型
	private Short productType;
	//产品编号
	private String productNumber;
	//理财师ID
	private Long memberId;
	//理财顾问姓名
	private String memberName;
	//理财顾问昵称
	private String memberNickName;
	//评价类型
	private Integer type;
	//评价标准
	private Integer criterion;
	//评价
	private Integer evaluate;
	//评价说明
	private String description;
	//评价时间
	private String evaluateTime;
	//对应产品好评率（百分数）
	private String goodCommentRatio;
	//对应产品差评率（百分数）
	private String badCommentRatio;
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductShortName() {
		return productShortName;
	}
	public void setProductShortName(String productShortName) {
		this.productShortName = productShortName;
	}
	public Short getProductType() {
		return productType;
	}
	public void setProductType(Short productType) {
		this.productType = productType;
	}
	public String getProductNumber() {
		return productNumber;
	}
	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getMemberNickName() {
		return memberNickName;
	}
	public void setMemberNickName(String memberNickName) {
		this.memberNickName = memberNickName;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getCriterion() {
		return criterion;
	}
	public void setCriterion(Integer criterion) {
		this.criterion = criterion;
	}
	public Integer getEvaluate() {
		return evaluate;
	}
	public void setEvaluate(Integer evaluate) {
		this.evaluate = evaluate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEvaluateTime() {
		return evaluateTime;
	}
	public void setEvaluateTime(String evaluateTime) {
		this.evaluateTime = evaluateTime;
	}
	public String getGoodCommentRatio() {
		return goodCommentRatio;
	}
	public void setGoodCommentRatio(String goodCommentRatio) {
		this.goodCommentRatio = goodCommentRatio;
	}
	public String getBadCommentRatio() {
		return badCommentRatio;
	}
	public void setBadCommentRatio(String badCommentRatio) {
		this.badCommentRatio = badCommentRatio;
	}
}
