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
package tools.dynamia.cms.pages.ext

import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.api.CMSExtension
import tools.dynamia.cms.core.api.SiteRequestInterceptorAdapter
import tools.dynamia.cms.core.domain.Site

/**
 *
 * @author Mario Serrano Leones
 */
@CMSExtension
class SearchSiteInterceptor extends SiteRequestInterceptorAdapter {

	@Override
	protected void afterRequest(Site site, ModelAndView mv) {
		if (mv != null && mv.model.get("searchForm") == null) {
			mv.addObject("searchForm", new tools.dynamia.cms.pages.SearchForm())
        }
	}

}