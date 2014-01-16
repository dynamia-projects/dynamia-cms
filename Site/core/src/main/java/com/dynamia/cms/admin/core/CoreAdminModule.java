/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.core;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.api.AdminModule;
import com.dynamia.cms.site.core.api.AdminModuleOption;
import com.dynamia.cms.site.core.api.CMSModule;

/**
 *
 * @author mario
 */
@CMSModule
public class CoreAdminModule implements AdminModule {

    @Override
    public String getGroup() {
        return "sites";
    }

    @Override
    public String getName() {
        return "DynamiaCMS Core";
    }

    @Override
    public AdminModuleOption[] getOptions() {
        return new AdminModuleOption[]{
            new AdminModuleOption("sites", "Sites", Site.class)
        };
    }

}
