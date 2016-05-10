package com.sw.plugins.config.dictionary.entity;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.sw.core.data.entity.RelationEntity;


/**
 * 数据字典总体分类
 * @author Administrator
 *
 */
public class Dictionary extends RelationEntity{
	
    private static final long serialVersionUID = 1L;  
    
    //字典名称
    @Pattern(regexp="^[\u4e00-\u9fa5]{1,20}")
    private String name;
    //标识字典类型的固定值
    @Pattern(regexp="^[A-Za-z]+$")
    private String code;
    //描述
    @Size(max=100)
    private String description;

    public Dictionary(){  
    }  

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
