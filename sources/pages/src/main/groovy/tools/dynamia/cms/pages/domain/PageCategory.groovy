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
package tools.dynamia.cms.pages.domain

import org.hibernate.annotations.BatchSize
import tools.dynamia.cms.core.Aliasable
import tools.dynamia.cms.core.api.SiteAware
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.domain.SimpleEntity
import tools.dynamia.domain.contraints.NotEmpty

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToOne
import javax.persistence.Table

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "pg_categories")
@BatchSize(size = 10)
class PageCategory extends SimpleEntity implements SiteAware, Aliasable {

	public static final int DEFAULT_PAGINATION_SIZE = 20

    @NotEmpty(message = "Enter category name")
	private String name
    private String description
    @OneToOne
	private Site site
    private boolean indexableByDates
    @Column(name = "catAlias")
	private String alias
    private boolean hidden
    private int paginationSize = DEFAULT_PAGINATION_SIZE

    int getPaginationSize() {
		if (paginationSize <= 0) {
			paginationSize = DEFAULT_PAGINATION_SIZE
        }
		return paginationSize
    }

    void setPaginationSize(int paginationSize) {
		this.paginationSize = paginationSize
    }

    boolean isHidden() {
		return hidden
    }

    void setHidden(boolean hidden) {
		this.hidden = hidden
    }

    String getAlias() {
		return alias
    }

    void setAlias(String alias) {
		this.alias = alias
    }

    boolean isIndexableByDates() {
		return indexableByDates
    }

    void setIndexableByDates(boolean indexableByDates) {
		this.indexableByDates = indexableByDates
    }

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

    String getDescription() {
		return description
    }

    void setDescription(String description) {
		this.description = description
    }

	@Override
    String toString() {
		return name
    }

	@Override
    String aliasSource() {
		return name
    }

}
