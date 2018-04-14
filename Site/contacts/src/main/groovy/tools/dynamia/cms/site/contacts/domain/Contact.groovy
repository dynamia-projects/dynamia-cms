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
package tools.dynamia.cms.site.contacts.domain

import tools.dynamia.cms.site.core.domain.SiteSimpleEntity
import tools.dynamia.domain.contraints.Email
import tools.dynamia.domain.contraints.NotEmpty

import javax.persistence.Entity
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "cts_contacts")
class Contact extends SiteSimpleEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5862561944292945368L

    @OneToOne
	@NotNull(message = "Select category")
	private ContactCategory category

    @NotEmpty(message = "Enter contact name")
	private String name
    private String phoneNumber
    private String mobileNumber
    private String address
    private String city
    private String zipCode
    @NotNull
	@NotEmpty(message = "Enter contact email")
	@Email(message = "Enter valid email address")
	private String email
    private boolean active = true

    ContactCategory getCategory() {
		return category
    }

    void setCategory(ContactCategory category) {
		this.category = category
    }

    String getName() {
		return name
    }

    void setName(String name) {
		this.name = name
    }

    String getPhoneNumber() {
		return phoneNumber
    }

    void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber
    }

    String getMobileNumber() {
		return mobileNumber
    }

    void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber
    }

    String getEmail() {
		return email
    }

    void setEmail(String email) {
		this.email = email
    }

    boolean isActive() {
		return active
    }

    void setActive(boolean active) {
		this.active = active
    }

    String getAddress() {
		return address
    }

    void setAddress(String address) {
		this.address = address
    }

    String getCity() {
		return city
    }

    void setCity(String city) {
		this.city = city
    }

    String getZipCode() {
		return zipCode
    }

    void setZipCode(String zipCode) {
		this.zipCode = zipCode
    }

	@Override
    String toString() {
		return name
    }

}
