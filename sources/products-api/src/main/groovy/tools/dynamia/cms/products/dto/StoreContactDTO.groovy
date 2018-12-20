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

package tools.dynamia.cms.products.dto

class StoreContactDTO implements Serializable {

	private String name
    private String lastName
    private String department
    private String jobTitle
    private String address
    private String mobileNumber
    private String phoneNumber
    private String city
    private String country
    private String email
    private String image
    private Long externalRef

    Long getExternalRef() {
		return externalRef
    }

    void setExternalRef(Long externalRef) {
		this.externalRef = externalRef
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

    String getAddress() {
		return address
    }

    void setAddress(String address) {
		this.address = address
    }

    String getMobileNumber() {
		return mobileNumber
    }

    void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber
    }

    String getPhoneNumber() {
		return phoneNumber
    }

    void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber
    }

    String getCity() {
		return city
    }

    void setCity(String city) {
		this.city = city
    }

    String getCountry() {
		return country
    }

    void setCountry(String country) {
		this.country = country
    }

    String getEmail() {
		return email
    }

    void setEmail(String email) {
		this.email = email
    }

    String getImage() {
		return image
    }

    void setImage(String image) {
		this.image = image
    }

}
