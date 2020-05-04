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
package tools.dynamia.cms.mail.services.impl

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.MailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.core.StringParser
import tools.dynamia.cms.core.StringParsers
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.mail.MailMessage
import tools.dynamia.cms.mail.MailServiceException
import tools.dynamia.cms.mail.MailServiceListener
import tools.dynamia.cms.mail.domain.MailAccount
import tools.dynamia.cms.mail.domain.MailTemplate
import tools.dynamia.cms.mail.domain.MailingContact
import tools.dynamia.cms.mail.services.MailService
import tools.dynamia.commons.logger.LoggingService
import tools.dynamia.commons.logger.SLF4JLoggingService
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.services.CrudService
import tools.dynamia.integration.Containers

import javax.mail.MessagingException
import javax.mail.internet.MimeMessage

/**
 *
 * @author ronald
 */
@Service
@CompileStatic
class MailServiceImpl implements MailService {


    @Autowired
    private CrudService crudService
    private Map<Long, MailSenderHolder> senderCache = new HashMap<>()

    private final LoggingService logger = new SLF4JLoggingService(MailService.class)

    @Override
    @Async
    void sendAsync(String to, String subject, String content) {
        send(to, subject, content)
    }

    @Override
    void send(String to, String subject, String content) {
        send(new MailMessage(to, subject, content))
    }

    @Override
    @Async
    void sendAsync(MailMessage mailMessage) {
        send(mailMessage)
    }

    @Override
    void send(final MailMessage mailMessage) {

        MailAccount account = mailMessage.mailAccount
        if (account == null) {
            account = getPreferredEmailAccount(mailMessage.site)
        }

        if (account == null) {
            return
        }

        if (mailMessage.template != null && !mailMessage.template.enabled) {
            return
        }

        final MailAccount finalAccount = crudService.reload(account)

        try {

            if (mailMessage.template != null) {
                processTemplate(mailMessage)
            }

            JavaMailSenderImpl jmsi = (JavaMailSenderImpl) createMailSender(finalAccount)
            MimeMessage mimeMessage = jmsi.createMimeMessage()

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, finalAccount.enconding)

            helper.to = mailMessage.to
            if (!mailMessage.tos.empty) {
                helper.to = mailMessage.tosAsArray
            }
            String from = finalAccount.fromAddress
            String personal = finalAccount.name
            if (from != null && personal != null) {

                helper.setFrom(from, personal)

            }

            if (!mailMessage.bccs.empty) {
                helper.bcc = mailMessage.bccsAsArray
            }

            if (!mailMessage.ccs.empty) {
                helper.cc = mailMessage.ccsAsArray
            }

            helper.subject = mailMessage.subject
            if (mailMessage.plainText != null && mailMessage.content != null) {
                helper.setText(mailMessage.plainText, mailMessage.content)
            } else {
                helper.setText(mailMessage.content, true)
            }

            for (File archivo : (mailMessage.attachtments)) {
                helper.addAttachment(archivo.name, archivo)
            }

            fireOnMailSending(mailMessage)
            logger.info("Sending e-mail " + mailMessage)
            jmsi.send(mimeMessage)

            logger.info("Email sended succesfull!")
            fireOnMailSended(mailMessage)
        } catch (Exception me) {
            logger.error("Error sending e-mail " + mailMessage, me)
            fireOnMailSendFail(mailMessage, me)
            throw new MailServiceException("Error sending mail message " + mailMessage, me)

        }

    }

    @Override
    MailAccount getPreferredEmailAccount(Site site) {

        Site currentSite = site

        try {
            if (currentSite == null) {
                currentSite = SiteContext.get().current
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        QueryParameters params = QueryParameters.with("preferred", true)
        if (currentSite != null) {
            params.add("site", currentSite)
        }

        MailAccount account = crudService.findSingle(MailAccount.class, params)
        if (account == null) {
            logger.warn("There is not a preferred email account ")
        }
        return account
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void setPreferredEmailAccount(MailAccount account) {
        crudService.batchUpdate(MailAccount.class, "preferred", false, QueryParameters.with("site", account.site))
        crudService.updateField(account, "preferred", true)
    }

    private MailSender createMailSender(MailAccount account) {
        JavaMailSenderImpl mailSender = null

        MailSenderHolder holder = senderCache.get(account.id)

        if (holder != null) {
            mailSender = (JavaMailSenderImpl) holder.sender
            if (holder.isOld(account.timestamp)) {
                mailSender = null
                senderCache.remove(account.id)
            }
        }

        if (mailSender == null) {
            mailSender = new JavaMailSenderImpl()
            mailSender.host = account.serverAddress
            mailSender.port = account.port
            mailSender.username = account.username
            mailSender.password = account.password

            mailSender.protocol = "smtp"
            if (account.enconding != null && !account.enconding.empty) {
                mailSender.defaultEncoding = account.enconding
            }

            Properties jmp = new Properties()
            jmp.setProperty("mail.smtp.auth", String.valueOf(account.loginRequired))
            jmp.setProperty("mail.smtp.from", account.fromAddress)
            jmp.setProperty("mail.smtp.port", String.valueOf(account.port))
            jmp.setProperty("mail.smtp.starttls.enable", String.valueOf(account.useTTLS))
            jmp.setProperty("mail.smtp.host", account.serverAddress)
            jmp.setProperty("mail.from", account.fromAddress)
            jmp.setProperty("mail.personal", account.name)

            mailSender.javaMailProperties = jmp
            try {
                mailSender.testConnection()
                senderCache.put(account.id, new MailSenderHolder(account.timestamp, mailSender))
            } catch (MessagingException e) {
                throw new MailServiceException(
                        "Error creating mail sender for account " + account.name + " - " + account.site, e)
            }
        }

        return mailSender

    }

    void processTemplate(MailMessage message) {

        if (message.template == null) {
            throw new MailServiceException("$message  has no template to process")
        }

        MailTemplate template = message.template
        StringParser stringParser = StringParsers.get(template.templateEngine)
        message.subject = stringParser.parse(template.subject, message.templateModel)
        message.content = stringParser.parse(template.content, message.templateModel)
    }

    @Override
    boolean existsMailingContact(MailingContact contact) {
        return crudService.findSingle(MailingContact.class, "emailAddress", contact.emailAddress) != null
    }

    private void fireOnMailSending(MailMessage message) {
        Collection<MailServiceListener> listeners = Containers.get().findObjects(MailServiceListener.class)
        for (MailServiceListener listener : listeners) {
            try {
                listener.onMailSending(message)
            } catch (Exception e) {
                logger.error("Error firing listener OnMailSending: $listener", e)
            }
        }
    }

    private void fireOnMailSended(MailMessage message) {
        Collection<MailServiceListener> listeners = Containers.get().findObjects(MailServiceListener.class)
        for (MailServiceListener listener : listeners) {
            try {
                listener.onMailSended(message)
            } catch (Exception e) {
                logger.error("Error firing listener OnMailSended: $listener", e)
            }
        }
    }

    private void fireOnMailSendFail(MailMessage message, Throwable cause) {
        Collection<MailServiceListener> listeners = Containers.get().findObjects(MailServiceListener.class)
        for (MailServiceListener listener : listeners) {
            try {
                listener.onMailSendFail(message, cause)
            } catch (Exception e) {
                logger.error("Error firing listener OnMailSendFail: $listener", e)
            }
        }
    }

}
