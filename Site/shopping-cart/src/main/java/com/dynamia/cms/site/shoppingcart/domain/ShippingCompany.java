package com.dynamia.cms.site.shoppingcart.domain;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.dynamia.cms.site.core.api.SiteAware;
import com.dynamia.cms.site.core.domain.Site;

import tools.dynamia.domain.SimpleEntity;

@Entity
@Table(name = "sc_shipping_companies")
public class ShippingCompany extends SimpleEntity implements SiteAware {

	@OneToOne
	private Site site;
	private String name;
	private String website;
	private String trackingSiteURL;
	private Long externalRef;

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

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getTrackingSiteURL() {
		return trackingSiteURL;
	}

	public void setTrackingSiteURL(String trackingSiteURL) {
		this.trackingSiteURL = trackingSiteURL;
	}

	public Long getExternalRef() {
		return externalRef;
	}

	public void setExternalRef(Long externalRef) {
		this.externalRef = externalRef;
	}

	@Override
	public String toString() {
		return getName();
	}
}
