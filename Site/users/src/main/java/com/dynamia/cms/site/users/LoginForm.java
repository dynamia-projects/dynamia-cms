/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.users;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.tools.domain.contraints.NotEmpty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author mario_2
 */
public class LoginForm implements Serializable {

    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    @NotNull
    private Site site;

    public LoginForm() {
    }

    public LoginForm(Site site) {
        this.site = site;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
