package com.sw.plugins.product.manage.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.sw.core.data.entity.RelationEntity;

public class ProductDocConfig extends RelationEntity {

	private static final long serialVersionUID = 8240195718914733311L;

	public ProductDocConfig() {
	}

	@NotNull
	private Long productId;
	@NotNull
	private Short type;// 合同类型1：预用印2：非预用印
	@NotEmpty
	private String contact;
	@NotEmpty
	private String address;
	@Pattern(regexp = "^$|(\\(\\d{3,4}\\)|\\d{3,4}-|\\s)(\\d{8}|\\d{7})")
	private String telephone;
	@Pattern(regexp = "^$|^[\\s\\S]{11}")
	private String cellphone;
	@NotEmpty
	@Pattern(regexp = "^$|^[\\s\\S]{6}")
	private String postcode;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

}
