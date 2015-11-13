package com.dynamia.cms.site.contacts.domain;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.dynamia.cms.site.core.domain.SiteSimpleEntity;

import tools.dynamia.domain.contraints.Email;
import tools.dynamia.domain.contraints.NotEmpty;

@Entity
@Table(name = "cts_contacts")
public class Contact extends SiteSimpleEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5862561944292945368L;

	@OneToOne
	@NotNull(message = "Select category")
	private ContactCategory category;

	@NotEmpty(message = "Enter contact name")
	private String name;
	private String phoneNumber;
	private String mobileNumber;
	private String address;
	private String city;
	private String zipCode;
	@NotNull
	@NotEmpty(message = "Enter contact email")
	@Email(message = "Enter valid email address")
	private String email;
	private boolean active = true;

	public ContactCategory getCategory() {
		return category;
	}

	public void setCategory(ContactCategory category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Override
	public String toString() {
		return name;
	}

}
