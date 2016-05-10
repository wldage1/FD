/**
 * rwoms Copyright 2010 SINOWEL, Co.ltd .
 * All rights reserved.
 * Package:  com.sw.plugins.system.user.entity
 * FileName: UserOrgMapper.java
 * @version 1.0
 * @author 用户机构关系表实体
 * @created on 2012-6-12
 * @last Modified 
 * @history
 */
package com.sw.plugins.system.user.entity;

import com.sw.core.data.entity.RelationEntity;

/**
 * 用户机构关系实体
 * 
 * @author zhaofeng
 * @version 1.0 </pre> Created on :下午3:53:21 LastModified: History: </pre>
 */
public class UserOrgMapping extends RelationEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户ID
	 */
	private Long userId;
	/**
	 * 组织ID
	 */
	private Long orgId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

}
