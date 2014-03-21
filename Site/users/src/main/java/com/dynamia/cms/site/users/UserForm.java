/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.users;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.users.domain.User;
import com.dynamia.tools.domain.contraints.NotEmpty;
import java.io.Serializable;

/**
 *
 * @author mario_2
 */
public class UserForm implements Serializable {

    
    private User data = new User();
    @NotEmpty
    private String passwordConfirm;
    @NotEmpty
    private String currentPassword;
    private Site site;

    public UserForm() {
    }

    public UserForm(Site site) {
        this.site = site;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

}
