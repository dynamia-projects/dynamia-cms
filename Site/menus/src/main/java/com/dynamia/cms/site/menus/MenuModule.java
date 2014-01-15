/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.menus;

import com.dynamia.cms.site.core.domain.ModuleInstance;
import com.dynamia.cms.site.core.api.CMSModule;
import com.dynamia.cms.site.core.api.Module;

/**
 *
 * @author mario
 */
@CMSModule
public class MenuModule implements Module {

    @Override
    public String getID() {
        return getClass().getName();
    }

    @Override
    public String getName() {
        return "Menus and MenuItems";
    }

    @Override
    public String getDescription() {
        return "Basic module for creation menubars width its items";
    }

    @Override
    public String getVersion() {
        return "0.1";
    }

    @Override
    public ModuleInstance newInstance() {

        ModuleInstance instance = new ModuleInstance();
        instance.setModuleId(getID());
        instance.setEnabled(true);
        instance.setPosition("menu");
        instance.setAlias("mainmenu");

        return instance;
    }

    @Override
    public void init() {

    }

}
