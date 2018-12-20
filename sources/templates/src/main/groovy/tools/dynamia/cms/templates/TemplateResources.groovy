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

import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.DynamiaCMS
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.services.SiteService
import tools.dynamia.cms.templates.services.TemplateService
import tools.dynamia.integration.Containers

import javax.servlet.http.HttpServletRequest
import java.nio.file.Files
import java.nio.file.Path

/**
 *
 * @author Mario Serrano Leones
 */
class TemplateResources {

	static Path find(Site site, String resourceName) {
		SiteService service = Containers.get().findObject(SiteService.class)
        TemplateService tpltService = Containers.get().findObject(TemplateService.class)

        if (site == null) {
			site = service.mainSite
        }

		String currentTemplate = getTemplateName(site)

        if (currentTemplate == null || currentTemplate.empty) {
			currentTemplate = tpltService.defaultTemplate.directoryName
        }

		// find first in local site template resources
		Path templateResource = DynamiaCMS.getSitesResourceLocation(site)
				.resolve("templates" + File.separator + currentTemplate + File.separator + resourceName)

        //if not found find in global template resources
		if (!Files.exists(templateResource)) {
			templateResource = DynamiaCMS.templatesLocation
					.resolve(currentTemplate + File.separator + resourceName)
        }

		return templateResource
    }

    static String getTemplateName(Site site) {
		String name = site.template

        HttpServletRequest request = CMSUtil.currentRequest
        if (request != null) {
			String sessionTemplate = request.getParameter("setSessionTemplate")
            if (sessionTemplate != null && !sessionTemplate.empty) {
				request.session.setAttribute("SessionTemplate", sessionTemplate)
            }
			sessionTemplate = (String) request.session.getAttribute("SessionTemplate")
            if (sessionTemplate != null) {
				name = sessionTemplate
            }
		}

		return name
    }

}
