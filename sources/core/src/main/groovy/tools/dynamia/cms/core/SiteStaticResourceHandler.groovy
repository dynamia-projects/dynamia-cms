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
package tools.dynamia.cms.core

import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler
import tools.dynamia.integration.Containers

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 *
 * @author Mario Serrano Leones
 */
class SiteStaticResourceHandler extends ResourceHttpRequestHandler {

	@Override
    void handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("response", response)
        super.handleRequest(request, response)
    }

	@Override
	protected Resource getResource(HttpServletRequest request) throws IOException {
		HttpServletResponse response = (HttpServletResponse) request.getAttribute("response")
        tools.dynamia.cms.core.services.SiteService coreService = Containers.get().findObject(tools.dynamia.cms.core.services.SiteService.class)
        tools.dynamia.cms.core.domain.Site site = coreService.getSite(request)
        if (site == null) {
			site = coreService.mainSite
        }

		if (site == null) {
			throw new SiteNotFoundException("Cannot load resources. Site Not found for " + request.serverName)
        }

		Path resource = Paths.get(request.requestURI.replaceFirst("/static/", ""))
        Path staticDir = DynamiaCMS.getSitesStaticResourceLocation(site)
        Path absoluteResource = staticDir.resolve(resource)
        if (Files.isDirectory(absoluteResource)) {
			if (!request.requestURI.endsWith("/")) {
				try {
					response.sendRedirect(request.requestURI + "/")
                } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace()
                }
			}

			absoluteResource = absoluteResource.resolve("index.html")

        }
		File file = absoluteResource.toFile()
        if (!file.exists()) {
			return null
        }
		return new FileSystemResource(file)
    }

}
