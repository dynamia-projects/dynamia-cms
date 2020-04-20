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
package tools.dynamia.cms.core.domain

import tools.dynamia.cms.core.Aliasable
import tools.dynamia.cms.core.api.URIable
import tools.dynamia.domain.contraints.Email
import tools.dynamia.domain.contraints.NotEmpty

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "cr_authors")
class ContentAuthor extends SiteSimpleEntity implements URIable, Aliasable {

    String alias
    @NotEmpty(message = "Enter first name")
    String firstName
    @NotEmpty(message = "Enter first name")
    String lastName
    @NotEmpty(message = "Enter email address")
    @Email(message = "Enter valid email")
    String email
    String twitter
    String googlePlus
    String facebook
    String instagram
    String linkedin
    String tumblr
    String snapchat
    String pinterest
    String vk
    String flickr
    String vine
    String meetup
    String website
    String photo

    @Column(length = 1000)
    String bio


    @Override
    String toString() {
        return String.format("%s %s", firstName, lastName)
    }

    @Override
    String toURI() {
        return "authors/$alias"
    }

    @Override
    String aliasSource() {
        return "$firstName $lastName"
    }
}


