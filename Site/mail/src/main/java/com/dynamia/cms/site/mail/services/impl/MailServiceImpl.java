/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.mail.services.impl;

import java.io.File;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dynamia.cms.site.mail.MailMessage;
import com.dynamia.cms.site.mail.MailServiceException;
import com.dynamia.cms.site.mail.MailServiceListener;
import com.dynamia.cms.site.mail.domain.MailAccount;
import com.dynamia.cms.site.mail.domain.MailTemplate;
import com.dynamia.cms.site.mail.domain.MailingContact;
import com.dynamia.cms.site.mail.services.MailService;

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

	private VelocityEngine velocityEngine = new VelocityEngine();

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

		final MailAccount finalAccount = account;

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					if (mailMessage.getTemplate() != null) {
						processTemplate(mailMessage);
					}

					JavaMailSenderImpl jmsi = (JavaMailSenderImpl) createMailSender(finalAccount);
					MimeMessage mimeMessage = jmsi.createMimeMessage();

					MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
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
		});
		thread.start();

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
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
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

		return mailSender;

	}

	public void processTemplate(MailMessage message) {
		if (velocityEngine == null) {
			throw new MailServiceException("There is not a VelocityEngine configured to process any template");
		}

		if (message.getTemplate() == null) {
			throw new MailServiceException(message + " has no template to process");
		}

		StringWriter contentWriter = new StringWriter();
		StringWriter subjectWriter = new StringWriter();
		MailTemplate template = message.getTemplate();
		VelocityContext context = new VelocityContext();
		if (message.getTemplateModel() != null) {
			for (Entry<String, Object> entry : message.getTemplateModel().entrySet()) {
				context.put(entry.getKey(), entry.getValue());
			}
		}

		velocityEngine.evaluate(context, subjectWriter, "log", template.getSubject());
		velocityEngine.evaluate(context, contentWriter, "log", template.getContent());
		message.setSubject(subjectWriter.toString());
		message.setContent(contentWriter.toString());
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
}
