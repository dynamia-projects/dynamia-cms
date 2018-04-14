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
package tools.dynamia.cms.site.templates

import org.thymeleaf.templateresource.ITemplateResource
import tools.dynamia.cms.site.core.DynamiaCMS
import tools.dynamia.cms.site.core.SiteContext
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.core.services.SiteService
import tools.dynamia.commons.StringUtils
import tools.dynamia.integration.Containers

import java.nio.file.Files
import java.nio.file.Path

/**
 *
 * @author Mario Serrano Leones
 */
class SiteTemplateResource implements ITemplateResource, Serializable {

	private String templateName
    private String enconding
    private Path templatePath

    SiteTemplateResource(String templateName, String enconding) {
		super()
        this.templateName = templateName
        this.enconding = enconding
        this.templatePath = findTemplate(getSiteKey(), templateName)
    }

    SiteTemplateResource(String templateName, String enconding, Path templatePath) {
		super()
        this.templateName = templateName
        this.enconding = enconding
        this.templatePath = templatePath
    }

    String getSiteKey() {

		String siteKey = null
        if (siteKey == null) {
			try {
				siteKey = SiteContext.get().getCurrent().getKey()
            } catch (Exception e) {

				e.printStackTrace()
            }
		}

		if (siteKey == null) {
			siteKey = "main"
        }

		return siteKey

    }

	private Path findTemplate(String siteKey, String name) {

		SiteService coreService = Containers.get().findObject(SiteService.class)
        Site site = coreService.getSite(siteKey)

        Path file = null

        // find view in site folders
		Path siteHome = DynamiaCMS.getSitesResourceLocation(site)
        for (String loc : DynamiaCMS.getRelativeLocations()) {
			file = siteHome.resolve(loc + File.separator + name)
            if (Files.exists(file)) {
				break
            }
		}

		// find view in site local templates
		if (!Files.exists(file)) {
			String loc = TemplateResources.getTemplateName(site)
            if (loc != null) {
				loc = DynamiaCMS.TEMPLATES + File.separator + loc
                file = siteHome.resolve(loc + File.separator + name)
            }
		}

		// find view in templates sub folders
		if (Files.notExists(file)) {
			for (String loc : DynamiaCMS.getRelativeLocations()) {
				file = TemplateResources.find(site, loc + File.separator + name)
                if (Files.exists(file)) {
					break
                }
			}
		}

		// find in home default folders
		if (Files.notExists(file)) {
			Path home = DynamiaCMS.getHomePath()
            for (String loc : DynamiaCMS.getRelativeLocations()) {
				file = home.resolve(loc + File.separator + name)
                if (Files.exists(file)) {
					break
                }
			}

		}

		// Find last in root template folder
		if (Files.notExists(file)) {
			file = TemplateResources.find(site, name)
        }

		return file
    }

	@Override
    String getDescription() {
		return templatePath.toString()
    }

	@Override
    String getBaseName() {
		return StringUtils.removeFilenameExtension(templatePath.getFileName().toString())
    }

	@Override
    boolean exists() {
		return Files.exists(templatePath)
    }

	@Override
    Reader reader() throws IOException {
		InputStream inputStream = Files.newInputStream(templatePath)
        if (enconding != null) {
			return new BufferedReader(new InputStreamReader(inputStream, enconding))
        } else {
			return new BufferedReader(new InputStreamReader(inputStream))
        }
	}

	@Override
    ITemplateResource relative(String relativeLocation) {
		Path path = templatePath
        if (!Files.isDirectory(path)) {
			path = path.getParent()
        }

		return new SiteTemplateResource(templateName, enconding, path.resolve(relativeLocation))
    }

    Path getTemplatePath() {
		return templatePath
    }

}
