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
package tools.dynamia.cms.site.mail.services.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.MailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import tools.dynamia.cms.site.core.SiteContext
import tools.dynamia.cms.site.core.StringParser
import tools.dynamia.cms.site.core.StringParsers
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.mail.MailMessage
import tools.dynamia.cms.site.mail.MailServiceException
import tools.dynamia.cms.site.mail.MailServiceListener
import tools.dynamia.cms.site.mail.domain.MailAccount
import tools.dynamia.cms.site.mail.domain.MailTemplate
import tools.dynamia.cms.site.mail.domain.MailingContact
import tools.dynamia.cms.site.mail.services.MailService
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
            listener.onMailSending(message)
        }
    }

    private void fireOnMailSended(MailMessage message) {
        Collection<MailServiceListener> listeners = Containers.get().findObjects(MailServiceListener.class)
        for (MailServiceListener listener : listeners) {
            listener.onMailSended(message)
        }
    }

    private void fireOnMailSendFail(MailMessage message, Throwable cause) {
        Collection<MailServiceListener> listeners = Containers.get().findObjects(MailServiceListener.class)
        for (MailServiceListener listener : listeners) {
            listener.onMailSendFail(message, cause)
        }
    }

}
