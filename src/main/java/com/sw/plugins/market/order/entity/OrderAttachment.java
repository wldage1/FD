package com.sw.plugins.market.order.entity;

import java.util.List;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.sw.core.data.entity.RelationEntity;

public class OrderAttachment extends RelationEntity{
	private static final long serialVersionUID = 1L;
	/** 订单号    **/
	private String orderNumber;
	/** 附件标题  **/
	private String title;
	/** 附件名 **/
	private String fileName;
	/** 附件路径 **/
	private String fileUrl;
	/** 上传时间 **/
	private String uploadTime;
	/** 审核状态 **/
	private Short audited;
	/** 合同编号 **/
	@Size(min=1)
	private String contractNumber;
	/** 合同编号确认(气泡验证) **/
	@NotEmpty
	private String confirmContractNumber;
	/** 产品ID **/
	private Long productID;
	/** 子产品ID **/
	private Long subProductID;
	//订单附件集合
	private List<OrderAttachment> orderAttachmentList;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public String getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}
	public Short getAudited() {
		return audited;
	}
	public void setAudited(Short audited) {
		this.audited = audited;
	}
	public List<OrderAttachment> getOrderAttachmentList() {
		return orderAttachmentList;
	}
	public void setOrderAttachmentList(List<OrderAttachment> orderAttachmentList) {
		this.orderAttachmentList = orderAttachmentList;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getContractNumber() {
		return contractNumber;
	}
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	public String getConfirmContractNumber() {
		return confirmContractNumber;
	}
	public void setConfirmContractNumber(String confirmContractNumber) {
		this.confirmContractNumber = confirmContractNumber;
	}
	public Long getProductID() {
		return productID;
	}
	public void setProductID(Long productID) {
		this.productID = productID;
	}
	public Long getSubProductID() {
		return subProductID;
	}
	public void setSubProductID(Long subProductID) {
		this.subProductID = subProductID;
	}
}
