package com.dynamia.cms.site.mail.domain;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.dynamia.cms.site.core.api.SiteAware;
import com.dynamia.cms.site.core.domain.Site;

import tools.dynamia.domain.SimpleEntity;
import tools.dynamia.domain.contraints.Email;
import tools.dynamia.domain.contraints.NotEmpty;

@Entity
@Table(name = "ntf_mailing_contacts")
public class MailingContact extends SimpleEntity implements SiteAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5354256227689328543L;
	@OneToOne
	@NotNull
	private Site site;
	private String firstName;
	private String lastName;
	private String address;
	private String city;
	@NotNull
	@NotEmpty
	@Email
	private String emailAddress;
	private String alternateEmailAddress;

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getAlternateEmailAddress() {
		return alternateEmailAddress;
	}

	public void setAlternateEmailAddress(String alternateEmailAddress) {
		this.alternateEmailAddress = alternateEmailAddress;
	}

	@Override
	public String toString() {
		return getEmailAddress();
	}

}
