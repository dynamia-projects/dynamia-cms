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
import tools.dynamia.domain.contraints.NotEmpty

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "ntf_email_templates")
class MailTemplate extends SimpleEntity implements SiteAware {

	@NotNull
	@OneToOne
	private Site site

    @NotEmpty(message = "Enter template name")
	private String name
    private String description
    @NotEmpty(message = "Enter subject")
	private String subject
    @Lob
	@Basic(fetch = FetchType.LAZY)
	@NotEmpty(message = "Enter template content")
	private String content
    private boolean enabled = true
    private String templateEngine

    String getTemplateEngine() {
		return templateEngine
    }

    void setTemplateEngine(String templateEngine) {
		this.templateEngine = templateEngine
    }

    boolean isEnabled() {
		return enabled
    }

    void setEnabled(boolean enabled) {
		this.enabled = enabled
    }

    Site getSite() {
		return site
    }

    void setSite(Site site) {
		this.site = site
    }

    String getName() {
		return name
    }

    void setName(String name) {
		this.name = name
    }

    String getDescription() {
		return description
    }

    void setDescription(String description) {
		this.description = description
    }

    String getSubject() {
		return subject
    }

    void setSubject(String subject) {
		this.subject = subject
    }

    String getContent() {
		return content
    }

    void setContent(String content) {
		this.content = content
    }

	@Override
    String toString() {
		return name
    }

}
