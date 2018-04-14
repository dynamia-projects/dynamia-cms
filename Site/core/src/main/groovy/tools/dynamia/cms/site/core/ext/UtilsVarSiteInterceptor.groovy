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
package tools.dynamia.cms.site.core.ext

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.site.core.CMSModules
import tools.dynamia.cms.site.core.CMSUtil
import tools.dynamia.cms.site.core.api.CMSExtension
import tools.dynamia.cms.site.core.api.SiteRequestInterceptorAdapter
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.core.domain.SiteParameter
import tools.dynamia.cms.site.core.services.SiteService
import tools.dynamia.cms.site.core.services.impl.ModulesService

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
