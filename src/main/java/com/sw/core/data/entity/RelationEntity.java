package com.sw.core.data.entity;


public class RelationEntity extends BaseEntity {

	private static final long serialVersionUID = -6896154521253993228L;

	/** 主键 */
	private Long id;
	/** 主键串 */
	private String ids[];
	/** 唯一标识串 */
	private Long generatedKey;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String[] getIds() {
		return ids;
	}
	public void setIds(String[] ids) {
		this.ids = ids;
	}
	public Long getGeneratedKey() {
		return generatedKey;
	}
	public void setGeneratedKey(Long generatedKey) {
		this.generatedKey = generatedKey;
	}

	

}
