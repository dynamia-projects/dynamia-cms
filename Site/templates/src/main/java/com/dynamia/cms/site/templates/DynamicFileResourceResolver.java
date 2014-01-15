/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.templates;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.tools.domain.query.ApplicationParameters;
import com.dynamia.tools.integration.Containers;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.resourceresolver.IResourceResolver;

/**
 *
 * @author mario
 */
public class DynamicFileResourceResolver implements IResourceResolver {

    public DynamicFileResourceResolver() {
        System.out.println("Initializing DynamicFileResourceResolver ");
    }

    @Override
    public String getName() {
        return DynamicFileResourceResolver.class.getName();
    }

    @Override
    public InputStream getResourceAsStream(TemplateProcessingParameters tpp, String name) {

        String siteKey = (String) tpp.getContext().getVariables().get("siteKey");
        if (siteKey == null) {
            siteKey = "main";
        }
        if (name.startsWith("classpath:")) {
            return loadFromClasspath(siteKey, name);
        } else {
            return loadFromFile(siteKey, name);
        }

    }

    private InputStream loadFromFile(String siteKey, String name) {

        SiteService coreService = Containers.get().findObject(SiteService.class);
        Site site = coreService.getSite(siteKey);

        String currentTemplate = null;
        if (site != null) {
            currentTemplate = site.getTemplate();
        } else {
            currentTemplate = ApplicationParameters.get().getValue(TemplateJavaConfig.CURRENT_TEMPLATE, "dynamical");
        }

        String dir = ApplicationParameters.get().getValue(TemplateJavaConfig.TEMPLATES_LOCATION, "");
        File templateLocation = new File(dir + currentTemplate);
        if (!templateLocation.exists()) {
            throw new TemplateNotFoundException("Template [" + currentTemplate + "] for site [" + site + "] could not be found: " + templateLocation);
        }

        File file = new File(templateLocation, name);
        if (!file.exists()) {
            file = new File(templateLocation.getPath() + "/views", name);
        }

        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    private InputStream loadFromClasspath(String siteKey, String name) {
        name = name.substring(name.lastIndexOf("classpath:"), name.length());
        name = "/" + name.replace(".", "/");
        return DynamicFileResourceResolver.class.getResourceAsStream(name);
    }
}
