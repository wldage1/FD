package com.sw.plugins.system.user.entity;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.userdetails.UserDetails;

import com.sw.core.data.entity.Authorization;
import com.sw.core.data.entity.BaseUser;
import com.sw.plugins.config.globalparameter.entity.GlobalParameter;
import com.sw.plugins.system.organization.entity.Organization;
import com.sw.plugins.system.role.entity.Role;

public class User extends BaseUser implements UserDetails {

	private static final long serialVersionUID = -3070895668840290837L;

	/** --- 用户信息实体属性 --- */

	private Long orgId;
	/** 用户名 */
	@NotEmpty
	@Pattern(regexp = "^[a-zA-Z0-9_]{1,20}")
	private String account;
	/** 用户名称 */
	@NotEmpty
	@Pattern(regexp = "^[a-zA-Z\\u4e00-\\u9fa5]{1,20}")
	private String name;
	/** 管理类型 0.超级管理员 1.其他管理员 */
	private Integer type;
	private String newPassword;
	private String confirmPwd;
	private String gender;
	private String mobilePhone;
	private String officePhone;
	@NotEmpty
	@Pattern(regexp = "^[0-9]{0,10}")
	private String workNumber;// 工号
	@Pattern(regexp = "^[0-9]{0,6}")
	@NotEmpty
	private String extensionNumber; // 分机号
	@NotEmpty
	private String seatPassword; // CTI密码
	private Integer outBoundAllowed;// 是否进行外呼(以此作为用户登录后台？外呼中心) 1:是0:否
	private String email;
	private String description;
	private String status;
	private String style;
	private String individualitySignature; // 个性签名
	private String birthday;
	private String head;
	private Short isFirstLogin;

	/** 登录初化数据 */
	private Organization selfOrg; // 所属机构的信息
	private List<Organization> secendLevelOrgs;// 登录用记二级机构信息
	// private Organization secendLevelOrg;// 登录用记所属机构的二级机构信息
	private List<GlobalParameter> globalParameters; // 二级机构关联的配置参数
	private Map<String, Authorization> userAuthorization;// 登录用户拥有拥有的权限信息
	private String userOrganizatins;// 登录用户拥有的数据权限串

	/** --- 参数 --- */
	private String user;
	private Long paramRoleId;
	private List<Role> roles;
	private String rolestr;
	private String bind;
	private String position;// 用户岗们1：产品管理员2:销售管理员
	private List<Organization> organizations;
	private Long paramOrgId;
	private String orgName;
	private String orgstr;
	private String orgTreePath;
	private Long taskId;
	private Long positionCode;
	/** 坐席外乎业务id，只有一个业务直接默认，否者为坐席自己选择的业务id **/
	private Long businessTypeId;
	private String organizationCode;
	
	/** 设置的消息接收者ID*/
	private Long receiverID; 
	
	/** 已经配置的接收者ID集合,防止按照角色查询用户出现重复记录*/
	private String receiverIDs;

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public Long getPositionCode() {
		return positionCode;
	}

	public void setPositionCode(Long positionCode) {
		this.positionCode = positionCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public User() {
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	@Override
	public String getUsername() {
		return this.account;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

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

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getConfirmPwd() {
		return confirmPwd;
	}

	public void setConfirmPwd(String confirmPwd) {
		this.confirmPwd = confirmPwd;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getOfficePhone() {
		return officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	public String getWorkNumber() {
		return workNumber;
	}

	public void setWorkNumber(String workNumber) {
		this.workNumber = workNumber;
	}

	public String getExtensionNumber() {
		return extensionNumber;
	}

	public void setExtensionNumber(String extensionNumber) {
		this.extensionNumber = extensionNumber;
	}

	public String getSeatPassword() {
		return seatPassword;
	}

	public void setSeatPassword(String seatPassword) {
		this.seatPassword = seatPassword;
	}

	public Integer getOutBoundAllowed() {
		return outBoundAllowed;
	}

	public void setOutBoundAllowed(Integer outBoundAllowed) {
		this.outBoundAllowed = outBoundAllowed;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getRolestr() {
		return rolestr;
	}

	public void setRolestr(String rolestr) {
		this.rolestr = rolestr;
	}

	public String getBind() {
		return bind;
	}

	public void setBind(String bind) {
		this.bind = bind;
	}

	public List<Organization> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(List<Organization> organizations) {
		this.organizations = organizations;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgstr() {
		return orgstr;
	}

	public void setOrgstr(String orgstr) {
		this.orgstr = orgstr;
	}

	public String getOrgTreePath() {
		return orgTreePath;
	}

	public void setOrgTreePath(String orgTreePath) {
		this.orgTreePath = orgTreePath;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getIndividualitySignature() {
		return individualitySignature;
	}

	public void setIndividualitySignature(String individualitySignature) {
		this.individualitySignature = individualitySignature;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public Organization getSelfOrg() {
		return selfOrg;
	}

	public void setSelfOrg(Organization selfOrg) {
		this.selfOrg = selfOrg;
	}

	// public Organization getSecendLevelOrg() {
	// return secendLevelOrg;
	// }
	//
	// public void setSecendLevelOrg(Organization secendLevelOrg) {
	// this.secendLevelOrg = secendLevelOrg;
	// }

	public List<GlobalParameter> getGlobalParameters() {
		return globalParameters;
	}

	public void setGlobalParameters(List<GlobalParameter> globalParameters) {
		this.globalParameters = globalParameters;
	}


	public List<Organization> getSecendLevelOrgs() {
		return secendLevelOrgs;
	}

	public void setSecendLevelOrgs(List<Organization> secendLevelOrgs) {
		this.secendLevelOrgs = secendLevelOrgs;
	}

	public Long getParamOrgId() {
		return paramOrgId;
	}

	public void setParamOrgId(Long paramOrgId) {
		this.paramOrgId = paramOrgId;
	}

	public Long getParamRoleId() {
		return paramRoleId;
	}

	public void setParamRoleId(Long paramRoleId) {
		this.paramRoleId = paramRoleId;
	}

	public Map<String, Authorization> getUserAuthorization() {
		return userAuthorization;
	}

	public Long getBusinessTypeId() {
		return businessTypeId;
	}

	public void setBusinessTypeId(Long businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

	public void setUserAuthorization(Map<String, Authorization> userAuthorization) {
		this.userAuthorization = userAuthorization;
	}

	public String getUserOrganizatins() {
		return userOrganizatins;
	}

	public void setUserOrganizatins(String userOrganizatins) {
		this.userOrganizatins = userOrganizatins;
	}

	public Long getReceiverID() {
		return receiverID;
	}

	public void setReceiverID(Long receiverID) {
		this.receiverID = receiverID;
	}

	public String getReceiverIDs() {
		return receiverIDs;
	}

	public void setReceiverIDs(String receiverIDs) {
		this.receiverIDs = receiverIDs;
	}

	public Short getIsFirstLogin() {
		return isFirstLogin;
	}

	public void setIsFirstLogin(Short isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}

}