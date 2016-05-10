package com.sw.plugins.websitemanage.information.entity;

import javax.validation.constraints.Size;

import com.sw.core.data.entity.RelationEntity;

/**
 * 资讯实体类
 * 
 * @author runchao
 */
public class Information extends RelationEntity{

	private static final long serialVersionUID = -5047541489328361576L;
	
	//居间公司ID
	private Long orgId;
	//类别
	private Integer type;
	//标题
	@Size(min=1, max=40)
	private String title;
	//标题1
	private String shorterTitle;
	//标题2
	private String shortestTitle;
	//标题图片
	private String titleImage;
	//关键字
	private String keyword;
	//摘要
	private String summary;
	//来源
	private String source;
	//作者
	private String author;
	//内容
	@Size(min=1)
	private String content;
	//是否免费
	private Integer freed;
	//链接地址
	private String url;
	//是否发布
	private Integer Released;
	//关联产品ID
	private Long productId;
	//关联产品类型
	private Short productType;
	//关联产品名称
	private String productName;
	
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getShorterTitle() {
		return shorterTitle;
	}
	public void setShorterTitle(String shorterTitle) {
		this.shorterTitle = shorterTitle;
	}
	public String getShortestTitle() {
		return shortestTitle;
	}
	public void setShortestTitle(String shortestTitle) {
		this.shortestTitle = shortestTitle;
	}
	public String getTitleImage() {
		return titleImage;
	}
	public void setTitleImage(String titleImage) {
		this.titleImage = titleImage;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getFreed() {
		return freed;
	}
	public void setFreed(Integer freed) {
		this.freed = freed;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getReleased() {
		return Released;
	}
	public void setReleased(Integer released) {
		Released = released;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Short getProductType() {
		return productType;
	}
	public void setProductType(Short productType) {
		this.productType = productType;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}

}
