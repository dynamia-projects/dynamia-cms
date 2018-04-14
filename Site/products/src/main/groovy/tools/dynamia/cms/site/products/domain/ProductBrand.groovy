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
package tools.dynamia.cms.site.products.domain

import org.hibernate.annotations.BatchSize
import tools.dynamia.cms.site.core.api.SiteAware
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.products.dto.ProductBrandDTO
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
public class ProductBrand extends SimpleEntity implements SiteAware {

	@OneToOne
	@NotNull
	private Site site;

	@NotEmpty
	private String name;
	private String website;
	private String description;
	private Long externalRef;
	private String image;
	@Column(name = "brandAlias")
	private String alias;

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Long getExternalRef() {
		return externalRef;
	}

	public void setExternalRef(Long externalRef) {
		this.externalRef = externalRef;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void sync(ProductBrandDTO r) {
		description = r.getDescription();
		externalRef = r.getExternalRef();
		if (r.getImage() != null && !r.getImage().isEmpty()) {
			image = r.getImage();
		}
		name = r.getName();
		website = r.getWebsite();
	}

	@Override
	public String toString() {
		return getName();
	}

}
