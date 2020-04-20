/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
