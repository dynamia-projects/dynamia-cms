/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
package tools.dynamia.cms.core.ext

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.CMSModules
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.api.CMSExtension
import tools.dynamia.cms.core.api.SiteRequestInterceptorAdapter
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.domain.SiteParameter
import tools.dynamia.cms.core.services.SiteService
import tools.dynamia.cms.core.services.impl.ModulesService

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 *
 * @author Mario Serrano Leones
 */
@CMSExtension
class UtilsVarSiteInterceptor extends SiteRequestInterceptorAdapter {

	@Autowired
	private SiteService service

    @Autowired
	private ModulesService modulesService

    @Override
    void afterRequest(Site site, HttpServletRequest request, HttpServletResponse response,
					  ModelAndView modelAndView) {
		if (modelAndView == null) {
			return
        }

		String requestURI = request.requestURI
        String requestURL = request.requestURL.toString()

        if (requestURI != null) {
			modelAndView.addObject("currentURI", requestURI)
        }
		if (requestURL != null) {
			modelAndView.addObject("currentURL", requestURL)
        }
		modelAndView.addObject("site", site)
        modelAndView.addObject("siteParams", createParams(site))
        modelAndView.addObject("cmsUtil", new CMSUtil(site))
        if (modelAndView.model.get("cmsModules") == null) {
			modelAndView.addObject("cmsModules", new CMSModules(site, modulesService))
        }

	}

	private Object createParams(Site site) {
		Map<String, String> map = new HashMap<>()
        try {
			for (SiteParameter p : service.getSiteParameters(site)) {
				map.put(p.name, p.value)
            }
		} catch (Exception e) {
		}
		return map
    }

}
