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
package tools.dynamia.cms.users.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import tools.dynamia.cms.core.api.SiteAware
import tools.dynamia.cms.core.domain.City
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.domain.SimpleEntity
import tools.dynamia.domain.util.ContactInfo

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "usr_users_contacts")
class UserContactInfo extends SimpleEntity implements SiteAware {

    @OneToOne
    private Site site

    @OneToOne
    @NotNull
    @JsonIgnore
    private User user
    @OneToOne
    private City city
    private ContactInfo info = new ContactInfo()
    private String name
    @Column(length = 3000)
    private String description

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    Site getSite() {
        return site
    }

    void setSite(Site site) {
        this.site = site
    }

    User getUser() {
        return user
    }

    void setUser(User user) {
        this.user = user
    }

    ContactInfo getInfo() {
        return info
    }

    void setInfo(ContactInfo info) {
        this.info = info
    }

    String getDescription() {
        return description
    }

    void setDescription(String description) {
        this.description = description
    }

    @Override
    String toString() {
        return String.format("%s (%s, %s %s) ", name, info.city, info.region, info.address)
    }

    City getCity() {
        return city
    }

    void setCity(City city) {
        this.city = city
    }
}
