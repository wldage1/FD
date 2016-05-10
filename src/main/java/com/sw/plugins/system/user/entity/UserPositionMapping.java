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
 * 用户岗位关系实体
 * 
 * @author zhaofeng
 * @version 1.0 </pre> Created on :下午3:53:21 LastModified: History: </pre>
 */
public class UserPositionMapping extends RelationEntity {

	private static final long serialVersionUID = -7137135952943134806L;

	/**
	 * 用户ID
	 */
	private Long userId;
	/**
	 * 岗位ID
	 */
	private Long positionId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getPositionId() {
		return positionId;
	}

	public void setPositionId(Long positionId) {
		this.positionId = positionId;
	}

}
