/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.users;

import com.dynamia.cms.site.users.domain.User;

import tools.dynamia.domain.ValidationError;

/**
 *
 * @author mario_2
 */
public class PasswordsNotMatchException extends ValidationError {

    private User user;

    public PasswordsNotMatchException(User user) {
        this(user, "");
    }

    public PasswordsNotMatchException(User user, String message) {
        super(message);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}
