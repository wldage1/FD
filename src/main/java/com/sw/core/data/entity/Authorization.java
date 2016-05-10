package com.sw.core.data.entity;

import java.util.List;

public class Authorization extends RelationEntity {

	private static final long serialVersionUID = -8667400765027701089L;

	/** ------- 权限实体属性 ---------- */
	private String code;
	private String parentCode;
	private String treeLevel;
	private String relationPath;
	private Integer isChildNode ;
	private String indexCode;
	private String name;
	/** 是否是动态创建功能 1 是 0 不是 */
	private Integer type;
	private String description;
	private String controller;
	/** jsp 页面名称，只有二级功能指定有效 */
	private String pageName;
	private String dataSource;
	/** 前置图标 */
	private String icon;
	/** 是否记录日志 */
	private String logOrNot;
	/** 是否设置帮助信息在上下稳重 */
	private String helpOrNot;
	/** 是否设置导航 */
	private String navigateOrNot;
	/** 是否设置权限在上下文中 */
	private String setAuthOrNot;
	/** 是否记录当前状态 */
	private String setStatusOrNot;
	/** 是否需要功能授权 */
	private String setAuthorityOrNot;

	private String authorization;
	private List<String> codes;

	public Authorization() {
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getTreeLevel() {
		return treeLevel;
	}

	public void setTreeLevel(String treeLevel) {
		this.treeLevel = treeLevel;
	}

	public String getRelationPath() {
		return relationPath;
	}

	public void setRelationPath(String relationPath) {
		this.relationPath = relationPath;
	}

	public String getIndexCode() {
		return indexCode;
	}

	public void setIndexCode(String indexCode) {
		this.indexCode = indexCode;
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

	public String getController() {
		return controller;
	}

	public void setController(String controller) {
		this.controller = controller;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getLogOrNot() {
		return logOrNot;
	}

	public void setLogOrNot(String logOrNot) {
		this.logOrNot = logOrNot;
	}

	public String getNavigateOrNot() {
		return navigateOrNot;
	}

	public void setNavigateOrNot(String navigateOrNot) {
		this.navigateOrNot = navigateOrNot;
	}

	public String getSetAuthOrNot() {
		return setAuthOrNot;
	}

	public void setSetAuthOrNot(String setAuthOrNot) {
		this.setAuthOrNot = setAuthOrNot;
	}

	public String getSetStatusOrNot() {
		return setStatusOrNot;
	}

	public void setSetStatusOrNot(String setStatusOrNot) {
		this.setStatusOrNot = setStatusOrNot;
	}

	public String getAuthorization() {
		return authorization;
	}

	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}

	public List<String> getCodes() {
		return codes;
	}

	public void setCodes(List<String> codes) {
		this.codes = codes;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHelpOrNot() {
		return helpOrNot;
	}

	public void setHelpOrNot(String helpOrNot) {
		this.helpOrNot = helpOrNot;
	}

	public String getSetAuthorityOrNot() {
		return setAuthorityOrNot;
	}

	public void setSetAuthorityOrNot(String setAuthorityOrNot) {
		this.setAuthorityOrNot = setAuthorityOrNot;
	}

	public Integer getIsChildNode() {
		return isChildNode;
	}

	public void setIsChildNode(Integer isChildNode) {
		this.isChildNode = isChildNode;
	}

}
