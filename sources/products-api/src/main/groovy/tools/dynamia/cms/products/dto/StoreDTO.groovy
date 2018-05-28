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
package tools.dynamia.cms.products.dto
/**
 * @author Mario Serrano Leones
 */
class StoreDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 936740099546344064L
    private String name
    private Long externalRef
    private String address
    private String mobileNumber
    private String phoneNumber
    private String city
    private String country
    private String email
    private String image
    private String image2
    private String image3
    private String image4
    private String info

    String getImage2() {
        return image2
    }

    void setImage2(String image2) {
        this.image2 = image2
    }

    String getImage3() {
        return image3
    }

    void setImage3(String image3) {
        this.image3 = image3
    }

    String getImage4() {
        return image4
    }

    void setImage4(String image4) {
        this.image4 = image4
    }

    private List<StoreContactDTO> contacts = new ArrayList<>()

    List<StoreContactDTO> getContacts() {
        return contacts
    }

    void setContacts(List<StoreContactDTO> contacts) {
        this.contacts = contacts
    }

    String getImage() {
        return image
    }

    void setImage(String image) {
        this.image = image
    }

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    Long getExternalRef() {
        return externalRef
    }

    void setExternalRef(Long externalRef) {
        this.externalRef = externalRef
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

    String getInfo() {
        return info
    }

    void setInfo(String info) {
        this.info = info
    }
}
