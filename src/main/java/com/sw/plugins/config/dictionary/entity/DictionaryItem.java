package com.sw.plugins.config.dictionary.entity;

import javax.validation.constraints.Pattern;

import com.sw.core.data.entity.RelationEntity;

/**
 * 字典表
 */
public class DictionaryItem extends RelationEntity {
	private static final long serialVersionUID = -5645914212917376676L;
	
	private Long dictionaryId;
	@Pattern(regexp = "^[\u4e00-\u9fa5]{1,20}")
	private String itemName;
	@Pattern(regexp = "^\\d{1,3}$")
	private String itemValue;
	private String dictionaryCode;

	public DictionaryItem() {
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemValue() {
		return itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	public String getDictionaryCode() {
		return dictionaryCode;
	}

	public void setDictionaryCode(String dictionaryCode) {
		this.dictionaryCode = dictionaryCode;
	}

	public Long getDictionaryId() {
		return dictionaryId;
	}

	public void setDictionaryId(Long dictionaryId) {
		this.dictionaryId = dictionaryId;
	}
	

}
