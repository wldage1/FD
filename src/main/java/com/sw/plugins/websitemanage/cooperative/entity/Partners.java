package com.sw.plugins.websitemanage.cooperative.entity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.sw.core.data.entity.RelationEntity;

/**
 * 合作伙伴Entity
 * @author Flora
 */
public class Partners extends RelationEntity{

	private static final long serialVersionUID = -4315334295473042713L;
	
	/**名称*/
	@NotEmpty
	private String name;
	
	/**链接地址*/
	@NotEmpty
	private String url;
	
	/**上传图片路径*/
	@NotEmpty
	private String logo;
	
	private String picName;
	
	public String getPicName(){
		return picName;
	}
	
	public void setPicName(String picName){
		this.picName = picName;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	
}
