/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.templates;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.tools.integration.Containers;
import java.nio.file.Path;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

/**
 *
 * @author mario
 */
public class TemplateResourceHandler extends ResourceHttpRequestHandler {

    @Override
    protected Resource getResource(HttpServletRequest request) {
        SiteService service = Containers.get().findObject(SiteService.class);

        Site site = service.getSite(request);

        Path templateResource = TemplateResources.find(site, request.getPathInfo());
        return new FileSystemResource(templateResource.toFile());
    }
}
