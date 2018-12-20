/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
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
