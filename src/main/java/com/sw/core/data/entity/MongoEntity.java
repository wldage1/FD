package com.sw.core.data.entity;


public class MongoEntity extends BaseEntity{

	private static final long serialVersionUID = 2093354974074612179L;

	/** 主键 */
	private String id;
	/** 主键串 */
	private String ids[];
	/** 唯一标识串 */
	private String generatedKey;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public String getGeneratedKey() {
		return generatedKey;
	}

	public void setGeneratedKey(String generatedKey) {
		this.generatedKey = generatedKey;
	}

}
