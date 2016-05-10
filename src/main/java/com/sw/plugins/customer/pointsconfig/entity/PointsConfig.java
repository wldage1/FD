package com.sw.plugins.customer.pointsconfig.entity;



import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.sw.core.data.entity.RelationEntity;


/**
 * 积分配置
 * @author wanghui
 *
 */
public class PointsConfig extends RelationEntity{ 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1777921715228681153L;
	
	//积分代码
	@Pattern(regexp="^[a-zA-Z0-9_\\u4e00-\\u9fa5]{1,30}")
	@NotEmpty
    private String code;
    //积分
	@Pattern(regexp="^[0-9]{1,11}")
	@NotEmpty
    private String points;
	//描述
    private String description;
    public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    

	
}
