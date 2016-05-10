package com.sw.plugins.system.role.entity;

import java.util.List;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.sw.core.data.entity.Authorization;
import com.sw.core.data.entity.RelationEntity;

public class Role extends RelationEntity {
	private static final long serialVersionUID = -8552065215066165229L;
	
	/** --角色实体属性 -- */
	
	/** 所属机构 **/
	private Long orgId;
	/** 角色名称 */
	@NotEmpty
	@Pattern(regexp="^[a-zA-Z\\u4e00-\\u9fa5]{1,20}")
	private String name;
	/** 角色描述 */
	private String description;
	/** 角色类型 0：超级管理员角色 1：普通角色 */
	private Integer type = 1;
	/** 角色状态 0: 弃用 1:启用*/
	private Integer status;
	
	/** --- 参数 --- */
	private String role;
	/** 权限字符串 */
	private String auth;
	/** 所属机构路径 **/
	private String orgTreePath;
	/** 所属机构名称 */
	private String orgName;
	/** 角色绑定的权限信息 */
	private List<Authorization> authorizations;
	private String authorizationstr;
	/** 所属机构串 */
	private String orgIds;

	public Role() {
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getOrgTreePath() {
		return orgTreePath;
	}

	public void setOrgTreePath(String orgTreePath) {
		this.orgTreePath = orgTreePath;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public List<Authorization> getAuthorizations() {
		return authorizations;
	}

	public void setAuthorizations(List<Authorization> authorizations) {
		this.authorizations = authorizations;
	}

	public String getAuthorizationstr() {
		return authorizationstr;
	}

	public void setAuthorizationstr(String authorizationstr) {
		this.authorizationstr = authorizationstr;
	}

	public String getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(String orgIds) {
		this.orgIds = orgIds;
	}
	
}