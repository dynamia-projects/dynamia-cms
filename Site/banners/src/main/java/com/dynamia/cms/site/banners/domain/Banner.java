package com.dynamia.cms.site.banners.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.dynamia.cms.site.core.Orderable;
import com.dynamia.cms.site.core.api.SiteAware;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.tools.domain.SimpleEntity;

@Entity
@Table(name = "ban_banners")
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

	@OneToOne
	@NotNull
	private BannerCategory category;

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

}
