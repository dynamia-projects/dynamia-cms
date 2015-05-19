package com.dynamia.cms.site.mail.domain;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.dynamia.cms.site.core.api.SiteAware;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.tools.domain.SimpleEntity;
import com.dynamia.tools.domain.contraints.NotEmpty;

@Entity
@Table(name = "ntf_email_templates")
public class MailTemplate extends SimpleEntity implements SiteAware {

	@NotNull
	@OneToOne
	private Site site;

	@NotEmpty(message = "Enter template name")
	private String name;
	private String description;
	@NotEmpty(message = "Enter subject")
	private String subject;
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@NotEmpty(message = "Enter template content")
	private String content;
	private boolean enabled = true;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return getName();
	}

}
