/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.services.impl;

import com.dynamia.cms.site.core.domain.ModuleInstance;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.domain.SiteDomain;
import com.dynamia.cms.site.core.api.Module;
import com.dynamia.cms.site.core.domain.SiteParameter;
import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.tools.commons.logger.LoggingService;
import com.dynamia.tools.commons.logger.SLF4JLoggingService;
import com.dynamia.tools.domain.services.CrudService;
import com.dynamia.tools.integration.Containers;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 *
 * @author mario
 */
@Service
public class SiteServiceImpl implements SiteService {

    @Autowired
    private CrudService crudService;

    private LoggingService logger = new SLF4JLoggingService(SiteService.class);

    @Override
    @Cacheable(value = "sites", key = "#root.methodName")
    public Site getMainSite() {
        return crudService.findSingle(Site.class, "key", "main");
    }

    /**
     *
     * @param key
     * @return
     */
    @Override
    @Cacheable("sites")
    public Site getSite(String key) {
        return crudService.findSingle(Site.class, "key", key);
    }

    @Override
    @Cacheable("sites")
    public Site getSiteByDomain(String domainName) {
        System.out.println("FINDING SITE FOR DOMAIN: " + domainName);
        SiteDomain domain = crudService.findSingle(SiteDomain.class, "name", domainName);

        return domain != null ? domain.getSite() : null;
    }

    @Override
    public Site getSite(HttpServletRequest request) {
        Site site = null;
        if (request != null) {
            site = (Site) request.getSession().getAttribute("site");
            if (site == null) {
                site = getSiteByDomain(request.getServerName());
                request.getSession().setAttribute("site", site);
            }
        }

        return site;
    }
    
    @Cacheable(value="sites", key = "'params'+#site.key")
    @Override
    public List<SiteParameter> getSiteParameters(Site site){
        site = crudService.reload(site);
        return site.getParameters();        
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

}
