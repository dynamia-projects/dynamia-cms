package com.dynamia.cms.site.users.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.dynamia.cms.site.core.api.SiteAware;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.tools.domain.SimpleEntity;
import com.dynamia.tools.domain.util.ContactInfo;

@Entity
@Table(name = "usr_users_contacts", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "site_id", "user_id" })
})
public class UserContactInfo extends SimpleEntity implements SiteAware {

	@OneToOne
	private Site site;

	@OneToOne
	@NotNull
	private User user;
	private ContactInfo info = new ContactInfo();
	@Column(length = 3000)
	private String description;

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ContactInfo getInfo() {
		return info;
	}

	public void setInfo(ContactInfo info) {
		this.info = info;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
