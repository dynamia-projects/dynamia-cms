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
package tools.dynamia.cms.site.mail.services;

import org.springframework.scheduling.annotation.Async;

import tools.dynamia.cms.site.mail.MailMessage;
import tools.dynamia.cms.site.mail.domain.MailAccount;
import tools.dynamia.cms.site.mail.domain.MailingContact;

/**
 *
 * @author ronald
 */
public interface MailService {

	public void send(MailMessage message);

	public void send(String to, String subject, String content);

	public void setPreferredEmailAccount(MailAccount account);

	public MailAccount getPreferredEmailAccount();

	public boolean existsMailingContact(MailingContact contact);

	void sendAsync(MailMessage mailMessage);

	void sendAsync(String to, String subject, String content);

}
