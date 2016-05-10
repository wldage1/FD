package com.sw.plugins.system.organization.entity;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.sw.core.data.entity.RelationEntity;

/**
 * @NotEmpty
 * @Size(min = 1, max = 20)
 * @NotNull
 * @NumberFormat(style = Style.NUMBER)
 * @Min(1)
 * @Max(110)
 * @NotEmpty(message = "Password must not be blank.")
 * @Size(min = 1, max = 10, message =
 *           "Password must between 1 to 10 Characters.")
 * @Pattern(regexp="(\\(\\d{3,4 \\)|\\d{3,4}-|\\s)?\\d{8}")
 */
public class Organization extends RelationEntity {

	private static final long serialVersionUID = 8439388546577637264L;

	/** --- 机构实体属性 --- */

	/** 机构代码 */
	@NotEmpty
	@Pattern(regexp = "^[0-9]{1,6}")
	private String code;
	/** 机构名称 */
	@NotEmpty
	@Pattern(regexp = "^[a-zA-Z\\u4e00-\\u9fa5]{1,50}")
	private String name;
	/** 机构电话 */
	@Pattern(regexp = "^$|(\\(\\d{3,4}\\)|\\d{3,4}-|\\s)(\\d{8}|\\d{7})")
	private String phone;
	/** 机构传真 */
	@Pattern(regexp = "^$|(\\(\\d{3,4}\\)|\\d{3,4}-|\\s)(\\d{8}|\\d{7})")
	private String fax;
	/** 邮政编码 */
	@Pattern(regexp = "^$|^[\\s\\S]{6}")
	private String postCode;
	/** 机构地址 */
	@Size(max = 250)
	private String address;
	/** 机构描述 */
	@Size(max = 250)
	private String description;
	/** 机构状态 0:n 1:y */
	private Integer status;
	/** 父主键（如果是树形结构使用此值） */
	private Long parentId;
	/** 父主键（如果是树形结构使用此值） */
	@NotEmpty
	private String parentName;
	/** 树形级别 */
	private Integer treeLevel;
	/** 树形结构描述关系（例如：,0,1,2,3,4,5, 5代表自身节点值，0-4代表其所有父级节点值） */
	private String treePath;
	/** 是否是子节点 1:是子节点 0：不是子节点 */
	private Integer isChildNode;

	/** 居间公司 **/
	/** 公司简称 */
	@Pattern(regexp = "^[a-zA-Z\\u4e00-\\u9fa5]{0,30}")
	private String shortName;
	/** 开户行 */
	@Pattern(regexp = "^$|^[a-zA-Z\u4e00-\u9fa5]{1,20}$")
	private String bankName;
	/** 银行账号 */
	@Pattern(regexp = "^$|^[0-9]{10,20}$")
	private String account;
	/** 开户人 */
	private String accountHolder;
	/** 居间协议 */
	private String treaty;
	/** 原父机构id */
	private Long oldParentId;
	private String organization;
	/** 水印 */
	private String watermark;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Integer getTreeLevel() {
		return treeLevel;
	}

	public void setTreeLevel(Integer treeLevel) {
		this.treeLevel = treeLevel;
	}

	public String getTreePath() {
		return treePath;
	}

	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}

	public Integer getIsChildNode() {
		return isChildNode;
	}

	public void setIsChildNode(Integer isChildNode) {
		this.isChildNode = isChildNode;
	}

	public Long getOldParentId() {
		return oldParentId;
	}

	public void setOldParentId(Long oldParentId) {
		this.oldParentId = oldParentId;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getWatermark() {
		return watermark;
	}

	public void setWatermark(String watermark) {
		this.watermark = watermark;
	}
	
	public String getTreaty() {
		return treaty;
	}

	public void setTreaty(String treaty) {
		this.treaty = treaty;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccountHolder() {
		return accountHolder;
	}

	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

}