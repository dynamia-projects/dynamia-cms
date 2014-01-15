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
public class DynamiaCMSException extends RuntimeException {

    public DynamiaCMSException() {
    }

    public DynamiaCMSException(String message) {
        super(message);
    }

    public DynamiaCMSException(String message, Throwable cause) {
        super(message, cause);
    }

    public DynamiaCMSException(Throwable cause) {
        super(cause);
    }

    public DynamiaCMSException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
