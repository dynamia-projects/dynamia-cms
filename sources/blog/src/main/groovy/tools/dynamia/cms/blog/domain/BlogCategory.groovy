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

package tools.dynamia.cms.blog.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import tools.dynamia.cms.blog.BlogElement
import tools.dynamia.cms.core.Aliasable
import tools.dynamia.cms.core.api.URIable
import tools.dynamia.cms.core.domain.SiteBaseEntity
import tools.dynamia.domain.contraints.NotEmpty

import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "blg_categories")
class BlogCategory extends SiteBaseEntity implements BlogElement, Aliasable, URIable {

    @ManyToOne
    @JsonIgnore
    Blog blog

    @NotEmpty
    String name
    String alias
    String description
    String language

    long postCount

    @Override
    String toString() {
        return name
    }

    @Override
    String aliasSource() {
        return name
    }

    @Override
    String toURI() {
        return "${blog.toURI()}/${alias}"
    }
}
