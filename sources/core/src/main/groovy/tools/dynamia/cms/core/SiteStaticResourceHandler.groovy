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
