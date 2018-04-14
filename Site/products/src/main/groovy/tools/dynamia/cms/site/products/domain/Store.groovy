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
package tools.dynamia.cms.site.products.domain

import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import tools.dynamia.cms.site.core.api.SiteAware
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.pages.domain.Page
import tools.dynamia.cms.site.products.dto.StoreDTO
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
