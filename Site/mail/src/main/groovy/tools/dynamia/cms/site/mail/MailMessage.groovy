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
package tools.dynamia.cms.site.mail;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.mail.domain.MailAccount;
import tools.dynamia.cms.site.mail.domain.MailTemplate;

/**
 *
 * @author Mario Serrano Leones
 */
public class MailMessage implements Serializable {

	private final Set<String> tos = new HashSet<>();
	private final Set<String> ccs = new HashSet<>();
	private final Set<String> bccs = new HashSet<>();
	private String to;
	private String subject;
	private String content;
	private String plainText;
	private final List<File> attachtments = new ArrayList<>();
	private MailAccount mailAccount;
	private MailTemplate template;
	private Map<String, Object> templateModel = new HashMap<>();
	private Site site;

	public MailMessage() {
	}

	public MailMessage(MailTemplate template) {
		super();
		this.template = template;
	}

	public MailMessage(String to, String subject, String content) {
		this.to = to;
		this.subject = subject;
		this.content = content;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Map<String, Object> getTemplateModel() {
		return templateModel;
	}

	public void setTemplateModel(Map<String, Object> templateModel) {
		this.templateModel = templateModel;
	}

	public MailAccount getMailAccount() {
		return mailAccount;
	}

	public void setMailAccount(MailAccount emailAccount) {
		this.mailAccount = emailAccount;
	}

	public MailTemplate getTemplate() {
		return template;
	}

	public void setTemplate(MailTemplate template) {
		this.template = template;
	}

	public String getPlainText() {
		return plainText;
	}

	public void setPlainText(String plainText) {
		this.plainText = plainText;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getTo() {
		return to;
	}

	public void addTo(String to) {
		if (!tos.contains(to)) {
			tos.add(to);
		}
	}

	public void addCc(String cc) {
		if (!ccs.contains(cc)) {
			ccs.add(cc);
		}
	}

	public void addBcc(String bcc) {
		if (!bccs.contains(bcc)) {
			bccs.add(bcc);
		}
	}

	public void addAttachtment(File file) {
		attachtments.add(file);
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

	public Set<String> getTos() {
		return tos;
	}

	public String[] getTosAsArray() {
		return getTos().toArray(new String[tos.size()]);
	}

	public Set<String> getCcs() {
		return ccs;
	}

	public String[] getCcsAsArray() {
		return getCcs().toArray(new String[ccs.size()]);
	}

	public Set<String> getBccs() {
		return bccs;
	}

	public String[] getBccsAsArray() {
		return getBccs().toArray(new String[bccs.size()]);
	}

	public List<File> getAttachtments() {
		return attachtments;
	}

	@Override
	public String toString() {
		if (!tos.isEmpty()) {
			return tos.toString() + " Subject: " + subject;
		} else {
			return to + " Subject: " + subject;
		}
	}
}
