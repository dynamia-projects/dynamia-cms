/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.users;

import org.springframework.security.core.AuthenticationException;

/**
 *
 * @author mario_2
 */
public class SiteNotFoundException extends AuthenticationException {

    public SiteNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

}
