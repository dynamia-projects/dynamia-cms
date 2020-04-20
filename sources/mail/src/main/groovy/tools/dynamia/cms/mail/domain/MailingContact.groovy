/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package tools.dynamia.cms.mail.domain

import tools.dynamia.cms.core.api.SiteAware
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.domain.SimpleEntity
import tools.dynamia.domain.contraints.Email
import tools.dynamia.domain.contraints.NotEmpty

import javax.persistence.Entity
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "ntf_mailing_contacts")
class MailingContact extends SimpleEntity implements SiteAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5354256227689328543L
    @OneToOne
	@NotNull
	private Site site
    private String firstName
    private String lastName
    private String address
    private String city
    @NotNull
	@NotEmpty
	@Email
	private String emailAddress
    private String alternateEmailAddress

    Site getSite() {
		return site
    }

    void setSite(Site site) {
		this.site = site
    }

    String getFirstName() {
		return firstName
    }

    void setFirstName(String firstName) {
		this.firstName = firstName
    }

    String getLastName() {
		return lastName
    }

    void setLastName(String lastName) {
		this.lastName = lastName
    }

    String getAddress() {
		return address
    }

    void setAddress(String address) {
		this.address = address
    }

    String getCity() {
		return city
    }

    void setCity(String city) {
		this.city = city
    }

    String getEmailAddress() {
		return emailAddress
    }

    void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress
    }

    String getAlternateEmailAddress() {
		return alternateEmailAddress
    }

    void setAlternateEmailAddress(String alternateEmailAddress) {
		this.alternateEmailAddress = alternateEmailAddress
    }

	@Override
    String toString() {
		return emailAddress
    }

}
