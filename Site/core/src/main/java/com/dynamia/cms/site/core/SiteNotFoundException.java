/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core;

/**
 *
 * @author mario
 */
public class SiteNotFoundException extends DynamiaCMSException {

    public SiteNotFoundException() {
    }

    public SiteNotFoundException(String message) {
        super(message);
    }

    public SiteNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SiteNotFoundException(Throwable cause) {
        super(cause);
    }

    public SiteNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
