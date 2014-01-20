/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.actions;

import com.dynamia.cms.site.core.DynamiaCMSException;

/**
 *
 * @author mario
 */
class SiteActionNotFoundException extends DynamiaCMSException {

    private String actionName;

    public SiteActionNotFoundException(String actionName) {
        this.actionName = actionName;

    }

    public SiteActionNotFoundException(String actionName, String message) {
        super(message);
        this.actionName = actionName;
    }

    public SiteActionNotFoundException(String actionName, String message, Throwable cause) {
        super(message, cause);
        this.actionName = actionName;
    }

    public SiteActionNotFoundException(String actionName, Throwable cause) {
        super(cause);
        this.actionName = actionName;
    }

    public SiteActionNotFoundException(String actionName, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.actionName = actionName;
    }

    public String getActionName() {
        return actionName;
    }

}
