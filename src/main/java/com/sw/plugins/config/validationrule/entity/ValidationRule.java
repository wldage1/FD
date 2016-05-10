package com.sw.plugins.config.validationrule.entity;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.sw.core.data.entity.RelationEntity;


public class ValidationRule extends RelationEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -790586917769931164L;
	//验证规则名称
	@Pattern(regexp = "[A-Za-z\\u4e00-\\u9fa5]{1,20}")
	private String name;
	//验证规则内容
	@Size(min=5, max=100)
	private String ruleContent;
	//提示内容
	@NotEmpty
	private String promptMessage;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRuleContent() {
		return ruleContent;
	}
	public void setRuleContent(String ruleContent) {
		this.ruleContent = ruleContent;
	}
	public String getPromptMessage() {
		return promptMessage;
	}
	public void setPromptMessage(String promptMessage) {
		this.promptMessage = promptMessage;
	}

	
}