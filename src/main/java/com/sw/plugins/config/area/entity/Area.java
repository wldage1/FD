package com.sw.plugins.config.area.entity;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.sw.core.data.entity.RelationEntity;
 

public class Area extends RelationEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6375047774072973223L;
	@Pattern(regexp = "[A-Za-z\\u4e00-\\u9fa5]{1,20}")
	//地区名称
	private String name;
	/**是否是子节点 1:是子节点  0：不是子节点*/
	private Long isChildNode;
	//树级别
	private Long treeLevel;
	//树路径
	private String treePath;
	//上级ID
	private Long parentId;
	//上级地区名称
	@NotEmpty
	private String parentName;
	//地区代码
	@Pattern(regexp = "[0-9]{6,6}")
	private String code;
	//英文或者拼音首字母简写
	@Pattern(regexp = "[A-Za-z]{1,20}")
	private String pinyin;
	private Long oldParentId;
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getTreePath() {
		return treePath;
	}
	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public Long getIsChildNode() {
		return isChildNode;
	}
	public void setIsChildNode(Long isChildNode) {
		this.isChildNode = isChildNode;
	}
	public Long getTreeLevel() {
		return treeLevel;
	}
	public void setTreeLevel(Long treeLevel) {
		this.treeLevel = treeLevel;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Long getOldParentId() {
		return oldParentId;
	}
	public void setOldParentId(Long oldParentId) {
		this.oldParentId = oldParentId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}