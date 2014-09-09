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
public class MailServiceException extends RuntimeException {

	public MailServiceException() {
	}

	public MailServiceException(String message) {
		super(message);
	}

	public MailServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public MailServiceException(Throwable cause) {
		super(cause);
	}

	public MailServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
