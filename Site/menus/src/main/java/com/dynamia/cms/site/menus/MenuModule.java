/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.menus;

import com.dynamia.cms.site.core.api.CMSModule;
import com.dynamia.cms.site.core.api.Module;
import com.dynamia.cms.site.core.api.ModuleContext;

/**
 *
 * @author mario
 */
@CMSModule
public class MenuModule implements Module {

    @Override
    public String getName() {
        return "Menus and MenuItems";
    }

    @Override
    public String getDescription() {
        return "Basic module for creation menubars width its items";
    }

    @Override
    public void init(ModuleContext context) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
