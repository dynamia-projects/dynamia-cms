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

import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import tools.dynamia.cms.core.api.SiteAware
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.pages.domain.Page
import tools.dynamia.cms.products.dto.StoreDTO
import tools.dynamia.domain.SimpleEntity
import tools.dynamia.domain.contraints.NotEmpty
import tools.dynamia.domain.util.ContactInfo

import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "prd_stores")
class Store extends SimpleEntity implements SiteAware {

    /**
     *
     */
    private static final long serialVersionUID = -7246043620591262045L
    @OneToOne
    @NotNull
    private Site site
    @NotNull
    @NotEmpty
    private String name
    private Long externalRef
    private ContactInfo contactInfo = new ContactInfo()
    private String image
    private String image2
    private String image3
    private String image4
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String info

    @OneToMany(mappedBy = "store", orphanRemoval = true, cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<StoreContact> contacts = new ArrayList<>()


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

    @OneToOne
    private Page page


    List<StoreContact> getContacts() {
        return contacts
    }

    void setContacts(List<StoreContact> contacts) {
        this.contacts = contacts
    }

    String getImage() {
        return image
    }

    void setImage(String image) {
        this.image = image
    }

    Page getPage() {
        return page
    }

    void setPage(Page page) {
        this.page = page
    }

    Site getSite() {
        return site
    }

    void setSite(Site site) {
        this.site = site
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

    ContactInfo getContactInfo() {
        if (contactInfo == null) {
            contactInfo = new ContactInfo()
        }
        return contactInfo
    }

    void setContactInfo(ContactInfo contactInfo) {

        this.contactInfo = contactInfo
    }

    void sync(StoreDTO dto) {
        name = dto.name
        if (contactInfo == null) {
            contactInfo = new ContactInfo()
        }

        contactInfo.address = dto.address
        contactInfo.city = dto.city
        contactInfo.country = dto.country
        contactInfo.email = dto.email
        contactInfo.mobileNumber = dto.mobileNumber
        contactInfo.phoneNumber = dto.phoneNumber
        externalRef = dto.externalRef
        if (dto.image != null && !dto.image.empty) {
            image = dto.image
        }
        if (dto.image2 != null && !dto.image2.empty) {
            image2 = dto.image2
        }
        if (dto.image3 != null && !dto.image3.empty) {
            image3 = dto.image3
        }
        if (dto.image4 != null && !dto.image4.empty) {
            image4 = dto.image4
        }
        info = dto.info

    }

    @Override
    String toString() {
        return name
    }

    String getInfo() {
        return info
    }

    void setInfo(String info) {
        this.info = info
    }
}
