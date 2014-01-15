/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.services.impl;

import com.dynamia.cms.site.core.domain.ModuleInstance;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.domain.SiteDomain;
import com.dynamia.cms.site.core.api.Module;
import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.tools.domain.services.CrudService;
import com.dynamia.tools.integration.Containers;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author mario
 */
@Service
public class SiteServiceImpl implements SiteService {

    @Autowired
    private CrudService crudService;

    @Override
    public Site getMainSite() {
        return crudService.findSingle(Site.class, "key", "main");
    }

    /**
     *
     * @param key
     * @return
     */
    @Override
    public Site getSite(String key) {
        return crudService.findSingle(Site.class, "key", key);
    }

    @Override
    public Site getSiteByDomain(String domainName) {
        SiteDomain domain = crudService.findSingle(SiteDomain.class, "name", domainName);

        return domain != null ? domain.getSite() : null;
    }

    @Override
    public Site getSite(HttpServletRequest request) {
        return getSiteByDomain(request.getServerName());
    }

    @Override
    public List<Module> getInstalledModules() {
        List<Module> modules = new ArrayList<>();
        modules.addAll(Containers.get().findObjects(Module.class));
        return modules;
    }

    @Override
    public List<ModuleInstance> getEnabledModulesInstances() {
        return crudService.find(ModuleInstance.class, "enabled", true);
    }

    @PostConstruct
    private void initMainSite() {
        if (getMainSite() == null) {

        }
    }

}
