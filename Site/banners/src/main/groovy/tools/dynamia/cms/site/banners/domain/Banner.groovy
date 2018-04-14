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
package tools.dynamia.cms.site.banners.domain

import org.hibernate.annotations.BatchSize
import tools.dynamia.cms.site.core.Orderable
import tools.dynamia.cms.site.core.api.SiteAware
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.domain.SimpleEntity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "ban_banners")
@BatchSize(size = 10)
class Banner extends SimpleEntity implements SiteAware, Orderable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2090565396360605396L

	@OneToOne
	@NotNull
	private Site site

	private String title
	private String subtitle
	private String description
	private boolean enabled = true
	private String url = "#"
	private String imageURL
	private String alternateImageURL
	@Column(name = "bannerOrder")
	private int order
	private String buttonLabel

	@OneToOne
	@NotNull
	private BannerCategory category

	String getButtonLabel() {
		return buttonLabel
	}

	void setButtonLabel(String buttonLabel) {
		this.buttonLabel = buttonLabel
	}

	String getSubtitle() {
		return subtitle
	}

	void setSubtitle(String subtitle) {
		this.subtitle = subtitle
	}

	String getDescription() {
		return description
	}

	void setDescription(String description) {
		this.description = description
	}

	String getAlternateImageURL() {
		return alternateImageURL
	}

	void setAlternateImageURL(String alternateImageURL) {
		this.alternateImageURL = alternateImageURL
	}

	Site getSite() {
		return site
	}

	void setSite(Site site) {
		this.site = site
	}

	String getTitle() {
		return title
	}

	void setTitle(String title) {
		this.title = title
	}

	boolean isEnabled() {
		return enabled
	}

	void setEnabled(boolean enabled) {
		this.enabled = enabled
	}

	String getUrl() {
		return url
	}

	void setUrl(String url) {
		this.url = url
	}

	String getImageURL() {
		return imageURL
	}

	void setImageURL(String imageURL) {
		this.imageURL = imageURL
	}

	BannerCategory getCategory() {
		return category
	}

	void setCategory(BannerCategory category) {
		this.category = category
	}

	@Override
	int getOrder() {
		return order
	}

	@Override
	void setOrder(int order) {
		this.order = order
	}

	@Override
	String toString() {
		return title
	}


}
