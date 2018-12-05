/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package tools.dynamia.cms.products.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import tools.dynamia.cms.core.domain.SiteSimpleEntity
import tools.dynamia.cms.products.dto.StoreContactDTO
import tools.dynamia.commons.BeanUtils
import tools.dynamia.domain.util.ContactInfo

import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "prd_store_contacts")
class StoreContact extends SiteSimpleEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 858677057279005993L

    @ManyToOne
	@JsonIgnore
	private Store store
    private String name
    private String lastName
    private String image
    private String department
    private String jobTitle
    private ContactInfo contactInfo = new ContactInfo()
    private Long externalRef

    Long getExternalRef() {
		return externalRef
    }

    void setExternalRef(Long externalRef) {
		this.externalRef = externalRef
    }

    String getImage() {
		return image
    }

    void setImage(String image) {
		this.image = image
    }

    Store getStore() {
		return store
    }

    void setStore(Store store) {
		this.store = store
    }

    String getName() {
		return name
    }

    void setName(String name) {
		this.name = name
    }

    String getLastName() {
		return lastName
    }

    void setLastName(String lastName) {
		this.lastName = lastName
    }

    String getDepartment() {
		return department
    }

    void setDepartment(String department) {
		this.department = department
    }

    String getJobTitle() {
		return jobTitle
    }

    void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle
    }

    ContactInfo getContactInfo() {
		return contactInfo
    }

    void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo
    }

    void sync(StoreContactDTO dto) {

		BeanUtils.setupBean(this, dto)
        BeanUtils.setupBean(this.contactInfo, dto)
    }

}
