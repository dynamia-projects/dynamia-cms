/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.users;

import com.dynamia.cms.site.users.domain.User;

/**
 *
 * @author mario
 */
public interface LoginListener {

    void onLoginSuccess(User user);
}
