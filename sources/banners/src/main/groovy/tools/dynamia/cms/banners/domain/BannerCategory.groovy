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
package tools.dynamia.cms.banners.domain

import org.hibernate.annotations.BatchSize
import tools.dynamia.cms.core.api.SiteAware
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.domain.SimpleEntity
import tools.dynamia.domain.contraints.NotEmpty

import javax.persistence.Entity
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "ban_categories")
@BatchSize(size=10)
class BannerCategory extends SimpleEntity implements SiteAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2355822917784027951L

	@OneToOne
	@NotNull
	private Site site

	@NotEmpty
	private String name
	private String description
	private String folderImages


	String getFolderImages() {
		return folderImages
	}

	void setFolderImages(String folderImages) {
		this.folderImages = folderImages
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

}
