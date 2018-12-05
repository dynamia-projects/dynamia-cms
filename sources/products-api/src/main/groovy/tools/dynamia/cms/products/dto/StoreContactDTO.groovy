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
