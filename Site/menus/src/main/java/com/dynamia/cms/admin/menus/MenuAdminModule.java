/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.menus;

import com.dynamia.cms.site.core.api.AdminModule;
import com.dynamia.cms.site.core.api.AdminModuleOption;
import com.dynamia.cms.site.core.api.CMSModule;
import com.dynamia.cms.site.menus.domain.Menu;

/**
 *
 * @author mario
 */
@CMSModule
public class MenuAdminModule implements AdminModule {

    @Override
    public String getGroup() {
        return "Content";
    }

    @Override
    public String getName() {
        return "Site Content";
    }

    @Override
    public AdminModuleOption[] getOptions() {
        return new AdminModuleOption[]{
            new AdminModuleOption("menus", "Menus", Menu.class)
        };
    }

}
