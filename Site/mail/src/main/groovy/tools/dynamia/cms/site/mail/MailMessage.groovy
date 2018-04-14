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
package tools.dynamia.cms.site.mail

import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.mail.domain.MailAccount
import tools.dynamia.cms.site.mail.domain.MailTemplate

/**
 *
 * @author Mario Serrano Leones
 */
class MailMessage implements Serializable {

	private final Set<String> tos = new HashSet<>()
    private final Set<String> ccs = new HashSet<>()
    private final Set<String> bccs = new HashSet<>()
    private String to
    private String subject
    private String content
    private String plainText
    private final List<File> attachtments = new ArrayList<>()
    private MailAccount mailAccount
    private MailTemplate template
    private Map<String, Object> templateModel = new HashMap<>()
    private Site site

    MailMessage() {
	}

    MailMessage(MailTemplate template) {
		super()
        this.template = template
    }

    MailMessage(String to, String subject, String content) {
		this.to = to
        this.subject = subject
        this.content = content
    }

    Site getSite() {
		return site
    }

    void setSite(Site site) {
		this.site = site
    }

    Map<String, Object> getTemplateModel() {
		return templateModel
    }

    void setTemplateModel(Map<String, Object> templateModel) {
		this.templateModel = templateModel
    }

    MailAccount getMailAccount() {
		return mailAccount
    }

    void setMailAccount(MailAccount emailAccount) {
		this.mailAccount = emailAccount
    }

    MailTemplate getTemplate() {
		return template
    }

    void setTemplate(MailTemplate template) {
		this.template = template
    }

    String getPlainText() {
		return plainText
    }

    void setPlainText(String plainText) {
		this.plainText = plainText
    }

    void setTo(String to) {
		this.to = to
    }

    String getTo() {
		return to
    }

    void addTo(String to) {
		if (!tos.contains(to)) {
			tos.add(to)
        }
	}

    void addCc(String cc) {
		if (!ccs.contains(cc)) {
			ccs.add(cc)
        }
	}

    void addBcc(String bcc) {
		if (!bccs.contains(bcc)) {
			bccs.add(bcc)
        }
	}

    void addAttachtment(File file) {
		attachtments.add(file)
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

    Set<String> getTos() {
		return tos
    }

    String[] getTosAsArray() {
		return tos.toArray(new String[tos.size()])
    }

    Set<String> getCcs() {
		return ccs
    }

    String[] getCcsAsArray() {
		return ccs.toArray(new String[ccs.size()])
    }

    Set<String> getBccs() {
		return bccs
    }

    String[] getBccsAsArray() {
		return bccs.toArray(new String[bccs.size()])
    }

    List<File> getAttachtments() {
		return attachtments
    }

	@Override
    String toString() {
		if (!tos.empty) {
			return tos.toString() + " Subject: " + subject
        } else {
			return to + " Subject: " + subject
        }
	}
}
