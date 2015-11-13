/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.pages;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.dynamia.cms.site.core.DynamiaCMSException;

/**
 *
 * @author mario
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "page not found in site")
public class PageNotFoundException extends DynamiaCMSException {

    private String pageAlias;
    private String siteKey;

    public PageNotFoundException() {
    }

    public PageNotFoundException(String pageAlias, String siteKey) {
        this.pageAlias = pageAlias;
        this.siteKey = siteKey;
    }

    public PageNotFoundException(String pageAlias, String siteKey, String message) {
        super(message);
        this.pageAlias = pageAlias;
        this.siteKey = siteKey;
    }

    public PageNotFoundException(String message) {
        super(message);
    }

    public PageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PageNotFoundException(Throwable cause) {
        super(cause);
    }

    public PageNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getPageAlias() {
        return pageAlias;
    }

    public String getSiteKey() {
        return siteKey;
    }

}
