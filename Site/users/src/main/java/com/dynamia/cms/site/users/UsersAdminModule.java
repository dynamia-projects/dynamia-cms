/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.users;

import com.dynamia.cms.site.core.api.AdminModule;
import com.dynamia.cms.site.core.api.AdminModuleOption;
import com.dynamia.cms.site.core.api.CMSModule;
import com.dynamia.cms.site.users.domain.User;

/**
 *
 * @author mario
 */
@CMSModule
public class UsersAdminModule implements AdminModule {

    @Override
    public String getGroup() {
        return "sites";
    }

    @Override
    public String getName() {
        return "General";
    }

    @Override
    public AdminModuleOption[] getOptions() {
        return new AdminModuleOption[]{
            new AdminModuleOption("users", "Users", User.class)
        };
    }

}
