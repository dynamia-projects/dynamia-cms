/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.api;

import java.io.Serializable;

/**
 *
 * @author mario
 */
public class DataChangedEvent implements Serializable {

    private String token;
    private Long externalRef;

    public DataChangedEvent(String token, Long externalRef) {
        this.token = token;
        this.externalRef = externalRef;
    }

    public String getToken() {
        return token;
    }

    public Long getExternalRef() {
        return externalRef;
    }

}
