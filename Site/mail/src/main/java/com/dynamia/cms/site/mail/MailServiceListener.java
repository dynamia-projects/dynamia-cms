/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.mail;

/**
 *
 * @author mario
 */
public interface MailServiceListener {

	public void onMailSending(MailMessage message);

	public void onMailSended(MailMessage message);

	public void onMailSendFail(MailMessage message, Throwable cause);

}
