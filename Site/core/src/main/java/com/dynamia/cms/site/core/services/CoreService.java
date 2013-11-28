/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.services;

import com.dynamia.cms.site.core.domain.ModuleInstance;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.ext.Module;
import java.util.List;

/**
 *
 * @author mario
 */
public interface CoreService {

    Site getMainSite();

    List<Module> getInstalledModules();
    
    List<ModuleInstance> getEnabledModulesInstances();

    Site getSite(String key);
}
