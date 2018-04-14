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
package tools.dynamia.cms.site.pages.domain;

import tools.dynamia.cms.site.core.Aliasable;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import tools.dynamia.cms.site.core.api.SiteAware;
import tools.dynamia.cms.site.core.domain.Site;
import javax.persistence.Column;

import tools.dynamia.domain.SimpleEntity;
import tools.dynamia.domain.contraints.NotEmpty;

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "pg_categories")
@BatchSize(size = 10)
public class PageCategory extends SimpleEntity implements SiteAware, Aliasable {

	public static final int DEFAULT_PAGINATION_SIZE = 20;

	@NotEmpty(message = "Enter category name")
	private String name;
	private String description;
	@OneToOne
	private Site site;
	private boolean indexableByDates;
	@Column(name = "catAlias")
	private String alias;
	private boolean hidden;
	private int paginationSize = DEFAULT_PAGINATION_SIZE;

	public int getPaginationSize() {
		if (paginationSize <= 0) {
			paginationSize = DEFAULT_PAGINATION_SIZE;
		}
		return paginationSize;
	}

	public void setPaginationSize(int paginationSize) {
		this.paginationSize = paginationSize;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public boolean isIndexableByDates() {
		return indexableByDates;
	}

	public void setIndexableByDates(boolean indexableByDates) {
		this.indexableByDates = indexableByDates;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public String aliasSource() {
		return getName();
	}

}
