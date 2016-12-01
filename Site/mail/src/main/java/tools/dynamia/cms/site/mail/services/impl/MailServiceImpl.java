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
package tools.dynamia.cms.site.mail.services.impl;

import tools.dynamia.cms.site.core.StringParser;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.mail.MessagingException;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tools.dynamia.cms.site.mail.MailMessage;
import tools.dynamia.cms.site.mail.MailServiceException;
import tools.dynamia.cms.site.mail.MailServiceListener;
import tools.dynamia.cms.site.mail.domain.MailAccount;
import tools.dynamia.cms.site.mail.domain.MailTemplate;
import tools.dynamia.cms.site.mail.domain.MailingContact;
import tools.dynamia.cms.site.mail.services.MailService;

import tools.dynamia.commons.logger.LoggingService;
import tools.dynamia.commons.logger.SLF4JLoggingService;
import tools.dynamia.domain.query.QueryParameters;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.integration.Containers;

/**
 *
 * @author ronald
 */
@Service
public class MailServiceImpl implements MailService {

	@Autowired
	private CrudService crudService;
	private Map<Long, MailSenderHolder> senderCache = new HashMap<>();

	private final LoggingService logger = new SLF4JLoggingService(MailService.class);

	@Override
	public void send(String to, String subject, String content) {
		send(new MailMessage(to, subject, content));
	}

	@Override
	public void send(final MailMessage mailMessage) {

		MailAccount account = mailMessage.getMailAccount();
		if (account == null) {
			account = getPreferredEmailAccount();
		}

		if (account == null) {
			return;
		}

		if (mailMessage.getTemplate() != null && !mailMessage.getTemplate().isEnabled()) {
			return;
		}

		final MailAccount finalAccount = crudService.reload(account);

		try {

			if (mailMessage.getTemplate() != null) {
				processTemplate(mailMessage);
			}

			JavaMailSenderImpl jmsi = (JavaMailSenderImpl) createMailSender(finalAccount);
			MimeMessage mimeMessage = jmsi.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, finalAccount.getEnconding());

			helper.setTo(mailMessage.getTo());
			if (!mailMessage.getTos().isEmpty()) {
				helper.setTo(mailMessage.getTosAsArray());
			}
			String from = finalAccount.getFromAddress();
			String personal = finalAccount.getName();
			if (from != null && personal != null) {

				helper.setFrom(from, personal);
				
			}

			if (!mailMessage.getBccs().isEmpty()) {
				helper.setBcc(mailMessage.getBccsAsArray());
			}

			if (!mailMessage.getCcs().isEmpty()) {
				helper.setCc(mailMessage.getCcsAsArray());
			}

			helper.setSubject(mailMessage.getSubject());
			if (mailMessage.getPlainText() != null && mailMessage.getContent() != null) {
				helper.setText(mailMessage.getPlainText(), mailMessage.getContent());
			} else {
				helper.setText(mailMessage.getContent(), true);
			}

			for (File archivo : mailMessage.getAttachtments()) {
				helper.addAttachment(archivo.getName(), archivo);
			}

			fireOnMailSending(mailMessage);
			logger.info("Sending e-mail " + mailMessage);
			jmsi.send(mimeMessage);

			logger.info("Email sended succesfull!");
			fireOnMailSended(mailMessage);
		} catch (Exception me) {
			logger.error("Error sending e-mail " + mailMessage, me);
			fireOnMailSendFail(mailMessage, me);
			throw new MailServiceException("Error sending mail message " + mailMessage, me);

		}

	}

	@Override
	public MailAccount getPreferredEmailAccount() {
		MailAccount account = crudService.findSingle(MailAccount.class, "preferred", true);
		if (account == null) {
			logger.warn("There is not a preferred email account ");
		}
		return account;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void setPreferredEmailAccount(MailAccount account) {
		crudService.batchUpdate(MailAccount.class, "preferred", false, QueryParameters.with("site", account.getSite()));
		crudService.updateField(account, "preferred", true);
	}

	private MailSender createMailSender(MailAccount account) {
		JavaMailSenderImpl mailSender = null;

		MailSenderHolder holder = senderCache.get(account.getId());

		if (holder != null) {
			mailSender = (JavaMailSenderImpl) holder.getSender();
			if (holder.isOld(account.getTimestamp())) {
				mailSender = null;
				senderCache.remove(account.getId());
			}
		}

		if (mailSender == null) {
			mailSender = new JavaMailSenderImpl();
			mailSender.setHost(account.getServerAddress());
			mailSender.setPort(account.getPort());
			mailSender.setUsername(account.getUsername());
			mailSender.setPassword(account.getPassword());
			
			mailSender.setProtocol("smtp");
			if (account.getEnconding() != null && !account.getEnconding().isEmpty()) {
				mailSender.setDefaultEncoding(account.getEnconding());
			}

			Properties jmp = new Properties();
			jmp.setProperty("mail.smtp.auth", String.valueOf(account.isLoginRequired()));
			jmp.setProperty("mail.smtp.from", account.getFromAddress());
			jmp.setProperty("mail.smtp.port", String.valueOf(account.getPort()));
			jmp.setProperty("mail.smtp.starttls.enable", String.valueOf(account.isUseTTLS()));
			jmp.setProperty("mail.smtp.host", account.getServerAddress());
			jmp.setProperty("mail.from", account.getFromAddress());
			jmp.setProperty("mail.personal", account.getName());

			mailSender.setJavaMailProperties(jmp);
			try {
				mailSender.testConnection();
				senderCache.put(account.getId(), new MailSenderHolder(account.getTimestamp(), mailSender));
			} catch (MessagingException e) {
				throw new MailServiceException(
						"Error creating mail sender for account " + account.getName() + " - " + account.getSite(), e);
			}
		}

		return mailSender;

	}

	public void processTemplate(MailMessage message) {

		if (message.getTemplate() == null) {
			throw new MailServiceException(message + " has no template to process");
		}

		MailTemplate template = message.getTemplate();
		StringParser stringParser = StringParser.get(template.getTemplateEngine());
		message.setSubject(stringParser.parse(template.getSubject(), message.getTemplateModel()));
		message.setContent(stringParser.parse(template.getContent(), message.getTemplateModel()));
	}

	@Override
	public boolean existsMailingContact(MailingContact contact) {
		return crudService.findSingle(MailingContact.class, "emailAddress", contact.getEmailAddress()) != null;
	}

	private void fireOnMailSending(MailMessage message) {
		Collection<MailServiceListener> listeners = Containers.get().findObjects(MailServiceListener.class);
		for (MailServiceListener listener : listeners) {
			listener.onMailSending(message);
		}
	}

	private void fireOnMailSended(MailMessage message) {
		Collection<MailServiceListener> listeners = Containers.get().findObjects(MailServiceListener.class);
		for (MailServiceListener listener : listeners) {
			listener.onMailSended(message);
		}
	}

	private void fireOnMailSendFail(MailMessage message, Throwable cause) {
		Collection<MailServiceListener> listeners = Containers.get().findObjects(MailServiceListener.class);
		for (MailServiceListener listener : listeners) {
			listener.onMailSendFail(message, cause);
		}
	}

	class MailSenderHolder {

		private long timestamp;
		private MailSender sender;

		public MailSenderHolder(long timestamp, MailSender sender) {
			this.timestamp = timestamp;
			this.sender = sender;
		}

		public MailSender getSender() {
			return sender;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public boolean isOld(long newtimestamp) {
			return newtimestamp > timestamp;
		}

	}

}
