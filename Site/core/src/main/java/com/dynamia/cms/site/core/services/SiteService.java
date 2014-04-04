/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.services;

import com.dynamia.cms.site.core.domain.ModuleInstance;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.api.Module;
import com.dynamia.cms.site.core.domain.SiteParameter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author mario
 */
public interface SiteService {

    Site getMainSite();

    List<Module> getInstalledModules();

    List<ModuleInstance> getEnabledModulesInstances();

    Site getSite(String key);

    Site getSiteByDomain(String domainName);

    Site getSite(HttpServletRequest request);

    List<SiteParameter> getSiteParameters(Site site);

    public void clearCache(Site site);

}
