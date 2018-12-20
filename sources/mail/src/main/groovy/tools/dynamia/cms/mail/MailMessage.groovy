/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
package tools.dynamia.cms.mail

import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.mail.domain.MailAccount
import tools.dynamia.cms.mail.domain.MailTemplate

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
