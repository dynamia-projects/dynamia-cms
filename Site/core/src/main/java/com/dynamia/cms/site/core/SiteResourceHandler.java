/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.tools.integration.Containers;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

/**
 *
 * @author mario
 */
public class SiteResourceHandler extends ResourceHttpRequestHandler {

    @Override
    protected Resource getResource(HttpServletRequest request) {
        SiteService coreService = Containers.get().findObject(SiteService.class);
        Site site = coreService.getSite(request);
        if (site == null) {
            site = coreService.getMainSite();
        }

        if (site == null) {
            throw new SiteNotFoundException("Cannot load resources. Site Not found for " + request.getServerName());
        }

        String dir = site.getResourcesLocation();

        String name = request.getPathInfo();
        name = name.substring("resources/".length());
        String fileName = dir + "/" + name;

        return new FileSystemResource(fileName);
    }
}
