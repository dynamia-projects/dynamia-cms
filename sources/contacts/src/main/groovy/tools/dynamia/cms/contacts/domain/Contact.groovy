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
package tools.dynamia.cms.contacts.domain

import tools.dynamia.cms.core.domain.SiteSimpleEntity
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
