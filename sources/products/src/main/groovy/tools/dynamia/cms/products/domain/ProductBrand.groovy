/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
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
