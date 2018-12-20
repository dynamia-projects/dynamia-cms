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

import org.hibernate.annotations.BatchSize
import tools.dynamia.cms.core.api.SiteAware
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.domain.SimpleEntity
import tools.dynamia.domain.contraints.NotEmpty

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "prd_brands")
@BatchSize(size = 50)
class ProductBrand extends SimpleEntity implements SiteAware {

	@OneToOne
	@NotNull
	private Site site

    @NotEmpty
	private String name
    private String website
    private String description
    private Long externalRef
    private String image
    @Column(name = "brandAlias")
	private String alias

    String getAlias() {
		return alias
    }

    void setAlias(String alias) {
		this.alias = alias
    }

    Site getSite() {
		return site
    }

    void setSite(Site site) {
		this.site = site
    }

    String getImage() {
		return image
    }

    void setImage(String image) {
		this.image = image
    }

    Long getExternalRef() {
		return externalRef
    }

    void setExternalRef(Long externalRef) {
		this.externalRef = externalRef
    }

    String getName() {
		return name
    }

    void setName(String name) {
		this.name = name
    }

    String getWebsite() {
		return website
    }

    void setWebsite(String website) {
		this.website = website
    }

    String getDescription() {
		return description
    }

    void setDescription(String description) {
		this.description = description
    }

    void sync(tools.dynamia.cms.products.dto.ProductBrandDTO r) {
		description = r.description
        externalRef = r.externalRef
        if (r.image != null && !r.image.empty) {
			image = r.image
        }
		name = r.name
        website = r.website
    }

	@Override
    String toString() {
		return name
    }

}
