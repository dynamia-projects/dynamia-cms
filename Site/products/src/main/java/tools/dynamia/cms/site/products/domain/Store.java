/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.site.products.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import tools.dynamia.cms.site.core.api.SiteAware;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.pages.domain.Page;
import tools.dynamia.cms.site.products.dto.StoreDTO;

import tools.dynamia.domain.SimpleEntity;
import tools.dynamia.domain.contraints.NotEmpty;
import tools.dynamia.domain.util.ContactInfo;

/**
 *
 * @author Mario Serrano Leones
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

	@OneToMany(mappedBy = "store", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<StoreContact> contacts = new ArrayList<>();

	public List<StoreContact> getContacts() {
		return contacts;
	}

	public void setContacts(List<StoreContact> contacts) {
		this.contacts = contacts;
	}

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

	@Override
	public String toString() {
		return name;
	}

}
