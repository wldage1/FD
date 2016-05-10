package com.sw.plugins.product.capitalconfig.entity;

import javax.validation.constraints.Pattern;

import com.sw.core.data.entity.RelationEntity;

/**
 * 产品分类
 * @author baokai
 * Created on :2013-1-21 上午10:56:49
 */
public class ProductCategory extends RelationEntity {
	
	private static final long serialVersionUID = 7205904436776762449L;
	@Pattern(regexp="^[a-zA-Z0-9\\u4e00-\\u9fa5]+$")
	private String name;
	@Pattern(regexp="^[A-Za-z0-9]+$")
	private String code;
	private String  treePath;
	private Integer treeLevel;
	private Integer isChildNode;
	private Long parentId;
	private String parentName;
	private Long oldParentId;
	private Short SortNum;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTreePath() {
		return treePath;
	}
	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}
	public Integer getTreeLevel() {
		return treeLevel;
	}
	public void setTreeLevel(Integer treeLevel) {
		this.treeLevel = treeLevel;
	}
	public Integer getIsChildNode() {
		return isChildNode;
	}
	public void setIsChildNode(Integer isChildNode) {
		this.isChildNode = isChildNode;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public Long getOldParentId() {
		return oldParentId;
	}
	public void setOldParentId(Long oldParentId) {
		this.oldParentId = oldParentId;
	}
	public Short getSortNum() {
		return SortNum;
	}
	public void setSortNum(Short sortNum) {
		SortNum = sortNum;
	}
	
	
	
	
	
}
