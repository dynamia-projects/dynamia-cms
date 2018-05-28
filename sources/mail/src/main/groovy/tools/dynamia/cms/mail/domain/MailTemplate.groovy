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
