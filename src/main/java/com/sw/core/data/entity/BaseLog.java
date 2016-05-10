package com.sw.core.data.entity;


public class BaseLog extends RelationEntity {
	private static final long serialVersionUID = 3528409663709225035L;
	/**用户id*/
    private Long userId;	
    /**用户账号*/
    private String userAccount;
    /**用户名称*/
    private String userName;
    /**操作时间*/
    private String operationTime;
    /**操作内容*/
    private String content;
    /**访问ip*/
    private String accessIp;
    
    private Integer type;


    public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getOperationTime() {
		return operationTime;
	}
	public void setOperationTime(String operationTime) {
		this.operationTime = operationTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAccessIp() {
		return accessIp;
	}
	public void setAccessIp(String accessIp) {
		this.accessIp = accessIp;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}

    
    
}