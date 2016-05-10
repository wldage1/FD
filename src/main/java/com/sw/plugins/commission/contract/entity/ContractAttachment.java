package com.sw.plugins.commission.contract.entity;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.sw.core.data.entity.RelationEntity;

public class ContractAttachment extends RelationEntity{
	
	private static final long serialVersionUID = 8439388546577637264L;
	
	//机构合同ID
    private Integer orgContractID;
    //文件名称
    private String fileName;
    //文件路径
    private String fileUrl;
    //创建时间
    private String createTime;
	public Integer getOrgContractID() {
		return orgContractID;
	}
	public void setOrgContractID(Integer orgContractID) {
		this.orgContractID = orgContractID;
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
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}