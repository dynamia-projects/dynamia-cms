/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.mail.services;

import com.dynamia.cms.site.mail.MailMessage;
import com.dynamia.cms.site.mail.domain.MailAccount;
import com.dynamia.cms.site.mail.domain.MailingContact;

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

}
