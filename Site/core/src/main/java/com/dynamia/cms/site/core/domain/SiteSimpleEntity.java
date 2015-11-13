package com.dynamia.cms.site.core.domain;

import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.dynamia.cms.site.core.api.SiteAware;

import tools.dynamia.domain.SimpleEntity;

@MappedSuperclass
public abstract class SiteSimpleEntity extends SimpleEntity implements SiteAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8298102526377692771L;
	@OneToOne
	@NotNull
	private Site site;

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

}
