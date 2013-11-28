/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.menus;

import com.dynamia.cms.site.core.ext.AdminModule;
import com.dynamia.cms.site.core.ext.AdminModuleOption;
import com.dynamia.cms.site.core.ext.InstallModule;
import com.dynamia.cms.site.menus.domain.Menu;

/**
 *
 * @author mario
 */
@InstallModule
public class MenuAdminModule implements AdminModule {

    @Override
    public String getGroup() {
        return "Menu";
    }

    @Override
    public String getName() {
        return "Menu";
    }

    @Override
    public AdminModuleOption[] getOptions() {
        return new AdminModuleOption[]{
            new AdminModuleOption("menus", "Menus", Menu.class)
        };
    }

}
