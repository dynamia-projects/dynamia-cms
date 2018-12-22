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

package tools.dynamia.cms.blog.domain

import tools.dynamia.cms.core.Aliasable
import tools.dynamia.cms.core.domain.ContentAuthor
import tools.dynamia.cms.core.domain.SiteBaseEntity
import tools.dynamia.domain.contraints.NotEmpty

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "blg_blogs")
class Blog extends SiteBaseEntity implements Aliasable {

    @NotEmpty
    String alias
    @OneToOne
    ContentAuthor mainAuthor
    boolean  commentsEnabled = true
    String disqusSite
    @Column(length = 500)
    @NotEmpty
    String title
    @Column(length = 1000)
    String description

    @Column(length = 4000)
    String tags

    long postCount
    long subcribersCount
    long categoriesCount
    long commentsCount

    @Override
    String toString() {
        return name
    }

    @Override
    String aliasSource() {
        return title
    }
}
