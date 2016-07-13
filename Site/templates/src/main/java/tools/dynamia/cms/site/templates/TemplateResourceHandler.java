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

import java.nio.file.Path;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.core.services.SiteService;

import tools.dynamia.integration.Containers;

/**
 *
 * @author Mario Serrano Leones
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
