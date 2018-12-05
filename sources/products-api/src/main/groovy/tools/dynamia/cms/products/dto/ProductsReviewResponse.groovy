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

class ProductsReviewResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1234515750141844185L
    private boolean accepted
    private List<ProductDTO> products = new ArrayList<>()

    private String email
    private String name
    private String lastName
    private String identification
    private String externalRef
    private String address
    private String phoneNumber
    private String mobileNumber
    private String city
    private String region
    private String country
    private String description
    private Date date
    private String document

    ProductsReviewResponse() {

	}

    ProductsReviewResponse(List<ProductDTO> products, String email, String name) {
		super()
        this.products = products
        this.email = email
        this.name = name
        this.accepted = true
    }

    String getDocument() {
		return document
    }

    void setDocument(String document) {
		this.document = document
    }

    Date getDate() {
		return date
    }

    void setDate(Date date) {
		this.date = date
    }

    String getDescription() {
		return description
    }

    void setDescription(String description) {
		this.description = description
    }

    boolean isAccepted() {
		return accepted
    }

    List<ProductDTO> getProducts() {
		return products
    }

    String getEmail() {
		return email
    }

    String getName() {
		return name
    }

    String getLastName() {
		return lastName
    }

    void setLastName(String lastName) {
		this.lastName = lastName
    }

    String getIdentification() {
		return identification
    }

    void setIdentification(String identification) {
		this.identification = identification
    }

    String getExternalRef() {
		return externalRef
    }

    void setExternalRef(String externalRef) {
		this.externalRef = externalRef
    }

    String getAddress() {
		return address
    }

    void setAddress(String address) {
		this.address = address
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

    String getCity() {
		return city
    }

    void setCity(String city) {
		this.city = city
    }

    String getRegion() {
		return region
    }

    void setRegion(String region) {
		this.region = region
    }

    String getCountry() {
		return country
    }

    void setCountry(String country) {
		this.country = country
    }

    static ProductsReviewResponse rejected() {
		return new ProductsReviewResponse()
    }

    static ProductsReviewResponse rejected(String description) {
		return new ProductsReviewResponse()
    }
}
