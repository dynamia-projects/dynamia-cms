/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package tools.dynamia.cms.templates

import org.thymeleaf.templateresource.ITemplateResource
import tools.dynamia.cms.core.DynamiaCMS
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.services.SiteService
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
        this.templatePath = findTemplate(siteKey, templateName)
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
				siteKey = SiteContext.get().current.key
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
        for (String loc : (DynamiaCMS.relativeLocations)) {
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
			for (String loc : (DynamiaCMS.relativeLocations)) {
				file = TemplateResources.find(site, loc + File.separator + name)
                if (Files.exists(file)) {
					break
                }
			}
		}

		// find in home default folders
		if (Files.notExists(file)) {
			Path home = DynamiaCMS.homePath
            for (String loc : (DynamiaCMS.relativeLocations)) {
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
		return StringUtils.removeFilenameExtension(templatePath.fileName.toString())
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
			path = path.parent
        }

		return new SiteTemplateResource(templateName, enconding, path.resolve(relativeLocation))
    }

    Path getTemplatePath() {
		return templatePath
    }

}
