package com.dynamia.cms.site.banners.domain;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.dynamia.cms.site.core.api.SiteAware;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.tools.domain.SimpleEntity;
import com.dynamia.tools.domain.contraints.NotEmpty;

@Entity
@Table(name = "ban_categories")
public class BannerCategory extends SimpleEntity implements SiteAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2355822917784027951L;

	@OneToOne
	@NotNull
	private Site site;

	@NotEmpty
	private String name;
	private String description;

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
}
