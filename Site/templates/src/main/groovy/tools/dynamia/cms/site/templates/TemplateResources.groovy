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
package tools.dynamia.cms.site.templates;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.servlet.http.HttpServletRequest;

import tools.dynamia.cms.site.core.CMSUtil;
import tools.dynamia.cms.site.core.DynamiaCMS;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.core.services.SiteService;
import tools.dynamia.cms.site.templates.services.TemplateService;

import tools.dynamia.integration.Containers;

/**
 *
 * @author Mario Serrano Leones
 */
public class TemplateResources {

	public static Path find(Site site, String resourceName) {
		SiteService service = Containers.get().findObject(SiteService.class);
		TemplateService tpltService = Containers.get().findObject(TemplateService.class);

		if (site == null) {
			site = service.getMainSite();
		}

		String currentTemplate = getTemplateName(site);

		if (currentTemplate == null || currentTemplate.isEmpty()) {
			currentTemplate = tpltService.getDefaultTemplate().getDirectoryName();
		}

		// find first in local site template resources
		Path templateResource = DynamiaCMS.getSitesResourceLocation(site)
				.resolve("templates" + File.separator + currentTemplate + File.separator + resourceName);

		//if not found find in global template resources
		if (!Files.exists(templateResource)) {
			templateResource = DynamiaCMS.getTemplatesLocation()
					.resolve(currentTemplate + File.separator + resourceName);
		}

		return templateResource;
	}

	public static String getTemplateName(Site site) {
		String name = site.getTemplate();

		HttpServletRequest request = CMSUtil.getCurrentRequest();
		if (request != null) {
			String sessionTemplate = request.getParameter("setSessionTemplate");
			if (sessionTemplate != null && !sessionTemplate.isEmpty()) {
				request.getSession().setAttribute("SessionTemplate", sessionTemplate);
			}
			sessionTemplate = (String) request.getSession().getAttribute("SessionTemplate");
			if (sessionTemplate != null) {
				name = sessionTemplate;
			}
		}

		return name;
	}

}
