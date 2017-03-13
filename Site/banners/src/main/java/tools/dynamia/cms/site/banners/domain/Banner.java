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
package tools.dynamia.cms.site.banners.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.BatchSize;

import tools.dynamia.cms.site.core.Orderable;
import tools.dynamia.cms.site.core.api.SiteAware;
import tools.dynamia.cms.site.core.domain.Site;

import tools.dynamia.domain.SimpleEntity;

@Entity
@Table(name = "ban_banners")
@BatchSize(size = 10)
public class Banner extends SimpleEntity implements SiteAware, Orderable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2090565396360605396L;

	@OneToOne
	@NotNull
	private Site site;

	private String title;
	private String subtitle;
	private String description;
	private boolean enabled = true;
	private String url = "#";
	private String imageURL;
	private String alternateImageURL;
	@Column(name = "bannerOrder")
	private int order;
	private String buttonLabel;

	@OneToOne
	@NotNull
	private BannerCategory category;

	public String getButtonLabel() {
		return buttonLabel;
	}

	public void setButtonLabel(String buttonLabel) {
		this.buttonLabel = buttonLabel;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAlternateImageURL() {
		return alternateImageURL;
	}

	public void setAlternateImageURL(String alternateImageURL) {
		this.alternateImageURL = alternateImageURL;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public BannerCategory getCategory() {
		return category;
	}

	public void setCategory(BannerCategory category) {
		this.category = category;
	}

	@Override
	public int getOrder() {
		return order;
	}

	@Override
	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return getTitle();
	}

}
