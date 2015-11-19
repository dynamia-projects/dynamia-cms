/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.domain;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.dynamia.cms.site.core.api.SiteAware;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.pages.domain.Page;
import com.dynamia.cms.site.products.dto.StoreDTO;

import tools.dynamia.domain.SimpleEntity;
import tools.dynamia.domain.contraints.NotEmpty;
import tools.dynamia.domain.util.ContactInfo;

/**
 *
 * @author mario_2
 */
@Entity
@Table(name = "prd_stores")
public class Store extends SimpleEntity implements SiteAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7246043620591262045L;
	@OneToOne
	@NotNull
	private Site site;
	@NotNull
	@NotEmpty
	private String name;
	private Long externalRef;
	private ContactInfo contactInfo = new ContactInfo();
	private String image;

	@OneToOne
	private Page page;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getExternalRef() {
		return externalRef;
	}

	public void setExternalRef(Long externalRef) {
		this.externalRef = externalRef;
	}

	public ContactInfo getContactInfo() {
		if (contactInfo == null) {
			contactInfo = new ContactInfo();
		}
		return contactInfo;
	}

	public void setContactInfo(ContactInfo contactInfo) {

		this.contactInfo = contactInfo;
	}

	public void sync(StoreDTO dto) {
		setName(dto.getName());
		getContactInfo().setAddress(dto.getAddress());
		getContactInfo().setCity(dto.getCity());
		getContactInfo().setCountry(dto.getCountry());
		getContactInfo().setEmail(dto.getEmail());
		getContactInfo().setMobileNumber(dto.getMobileNumber());
		getContactInfo().setPhoneNumber(dto.getPhoneNumber());
		setExternalRef(dto.getExternalRef());
		setImage(dto.getImage());

	}

}
