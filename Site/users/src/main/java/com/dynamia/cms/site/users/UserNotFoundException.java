/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dynamia.cms.site.users;

import org.springframework.security.authentication.BadCredentialsException;

/**
 *
 * @author mario_2
 */
public class UserNotFoundException extends BadCredentialsException{

    public UserNotFoundException(String msg) {
        super(msg);
    }
    
}
