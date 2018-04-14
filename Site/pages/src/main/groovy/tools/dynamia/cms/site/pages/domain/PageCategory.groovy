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
package tools.dynamia.cms.site.pages.domain

import org.hibernate.annotations.BatchSize
import tools.dynamia.cms.site.core.Aliasable
import tools.dynamia.cms.site.core.api.SiteAware
import tools.dynamia.cms.site.core.domain.Site
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
		return getName()
    }

}
