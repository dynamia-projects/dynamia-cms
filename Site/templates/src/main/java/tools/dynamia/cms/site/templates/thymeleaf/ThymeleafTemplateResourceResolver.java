/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.site.templates.thymeleaf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.resourceresolver.IResourceResolver;

import tools.dynamia.cms.site.core.DynamiaCMS;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.core.services.SiteService;
import tools.dynamia.cms.site.templates.TemplateResources;

import tools.dynamia.integration.Containers;

/**
 *
 * @author Mario Serrano Leones
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
