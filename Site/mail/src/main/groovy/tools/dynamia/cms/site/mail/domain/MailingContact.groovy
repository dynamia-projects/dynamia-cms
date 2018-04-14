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
package tools.dynamia.cms.site.mail.domain

import tools.dynamia.cms.site.core.api.SiteAware
import tools.dynamia.cms.site.core.domain.Site
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
