/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.templates.thymeleaf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.resourceresolver.IResourceResolver;

import com.dynamia.cms.site.core.DynamiaCMS;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.cms.site.templates.TemplateResources;

import tools.dynamia.integration.Containers;

/**
 *
 * @author mario
 */
public class ThymeleafTemplateResourceResolver implements IResourceResolver {

    @Override
    public String getName() {
        return ThymeleafTemplateResourceResolver.class.getName();
    }

    @Override
    public InputStream getResourceAsStream(TemplateProcessingParameters tpp, String name) {

        String siteKey = (String) tpp.getContext().getVariables().get("siteKey");
        if (siteKey == null) {
            siteKey = "main";
        }

        return loadFromFile(siteKey, name);

    }

    private InputStream loadFromFile(String siteKey, String name) {

        SiteService coreService = Containers.get().findObject(SiteService.class);
        Site site = coreService.getSite(siteKey);

        //Find first in root template folder
        Path file = TemplateResources.find(site, name);
        
         //find view in site folders
        if (Files.notExists(file)) {
            Path siteHome = DynamiaCMS.getSitesResourceLocation(site);
            for (String loc : DynamiaCMS.getRelativeLocations()) {
                file = siteHome.resolve(loc + File.separator + name);
                if (Files.exists(file)) {
                    break;
                }
            }
        }

        //find view in templates sub folders
        if (Files.notExists(file)) {
            for (String loc : DynamiaCMS.getRelativeLocations()) {
                file = TemplateResources.find(site, loc + File.separator + name);
                if (Files.exists(file)) {
                    break;
                }
            }
        }

        //find in home default folders
        if (Files.notExists(file)) {
            Path home = DynamiaCMS.getHomePath();
            for (String loc : DynamiaCMS.getRelativeLocations()) {
                file = home.resolve(loc + File.separator + name);
                if (Files.exists(file)) {
                    break;
                }
            }
        }
        try {
            return Files.newInputStream(file);
        } catch (IOException e) {
            return null;
        }
    }

}
