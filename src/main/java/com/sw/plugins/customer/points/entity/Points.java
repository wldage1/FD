package com.sw.plugins.customer.points.entity;


import com.sw.core.data.entity.RelationEntity;


/**
 * 积分管理
 * @author wanghui
 *
 */
public class Points extends RelationEntity{ 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1777921715228681153L;
	
	//积分代码	
    private String code;
    //积分	
    private String points;
	//积分说明
    private String discription;
    //理财师名
    private String name;
    //理财师昵称
    private String nickName;
    
    
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	public String getDiscription() {
		return discription;
	}
	public void setDiscription(String discription) {
		this.discription = discription;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}    
}
