package com.sw.plugins.commission.contract.entity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.sw.core.data.entity.RelationEntity;

public class Contract extends RelationEntity{
	
	private static final long serialVersionUID = 8439388546577637264L;
	 /* 下面是协议管理中用到的字段 */
	//居间机构ID
    private Integer orgID;
    //标题
    @Pattern(regexp="^[a-zA-Z\\u4e00-\\u9fa5]{1,20}$")
    private String title;
    //网签协议内容
    @NotEmpty
    private String content;
    //网签协议状态
    private Integer status;
    //启用时间
    private String startTime;
    //停用时间
    private String stopTime;
    //创建时间
    private String createTime;
    //居间公司名称
    private String orgName;
    /* 下面是合同管理中用到的字段 */
    //合同中的居间公司
    private Integer contractOrgID;
    //合同中的机构
    @NotEmpty
    private String teamOrOrgID;
	//合同名称
    @Pattern(regexp="^[a-zA-Z\\u4e00-\\u9fa5]{1,20}$")
    private String contractName;
    //合同中居间公司名称
    private String contractOrgName;
    //合同中机构名称
    private String teamOrOrgName;
	//合同类型1-产品投顾合同2-长期有效合同3-伞形合同
    private Integer contractType;
    //合同描述
    @Size(max=100)
    private String contractDescription;
    //合同状态
    private Integer contractStatus;
    //合同启用时间
    private String contractStartTime;
    //合同停用时间
    private String contractStopTime;
    //合同创建时间
    private String contractCreateTime;
    //文件名称
    private String fileName;
    //文件路径
	@NotEmpty
    private String fileUrl;
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
    public String getTeamOrOrgID() {
		return teamOrOrgID;
	}
	public void setTeamOrOrgID(String teamOrOrgID) {
		this.teamOrOrgID = teamOrOrgID;
	}
    public String getContractOrgName() {
		return contractOrgName;
	}
	public void setContractOrgName(String contractOrgName) {
		this.contractOrgName = contractOrgName;
	}
	public String getTeamOrOrgName() {
		return teamOrOrgName;
	}
	public void setTeamOrOrgName(String teamOrOrgName) {
		this.teamOrOrgName = teamOrOrgName;
	}
	public Integer getContractOrgID() {
		return contractOrgID;
	}
	public void setContractOrgID(Integer contractOrgID) {
		this.contractOrgID = contractOrgID;
	}

	public String getContractName() {
		return contractName;
	}
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	public Integer getContractType() {
		return contractType;
	}
	public void setContractType(Integer contractType) {
		this.contractType = contractType;
	}
	public String getContractDescription() {
		return contractDescription;
	}
	public void setContractDescription(String contractDescription) {
		this.contractDescription = contractDescription;
	}
	public Integer getContractStatus() {
		return contractStatus;
	}
	public void setContractStatus(Integer contractStatus) {
		this.contractStatus = contractStatus;
	}
	public String getContractStartTime() {
		return contractStartTime;
	}
	public void setContractStartTime(String contractStartTime) {
		this.contractStartTime = contractStartTime;
	}
	public String getContractStopTime() {
		return contractStopTime;
	}
	public void setContractStopTime(String contractStopTime) {
		this.contractStopTime = contractStopTime;
	}
	public String getContractCreateTime() {
		return contractCreateTime;
	}
	public void setContractCreateTime(String contractCreateTime) {
		this.contractCreateTime = contractCreateTime;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public Integer getOrgID() {
		return orgID;
	}
	public void setOrgID(Integer orgID) {
		this.orgID = orgID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public String getStopTime() {
		return stopTime;
	}
	public void setStopTime(String stopTime) {
		this.stopTime = stopTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}