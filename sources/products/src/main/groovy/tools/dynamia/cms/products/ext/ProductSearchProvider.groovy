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
package tools.dynamia.cms.products.ext

import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.actions.SiteActionManager
import tools.dynamia.cms.core.api.CMSExtension
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.pages.SearchForm
import tools.dynamia.cms.pages.api.SearchProvider
import tools.dynamia.cms.pages.api.SearchResult

/**
 *
 * @author Mario Serrano Leones
 */
@CMSExtension
class ProductSearchProvider implements SearchProvider {

    @Override
    SearchResult search(Site site, SearchForm form) {

        ModelAndView mv = new ModelAndView("products/productquery")

        SiteActionManager.performAction("searchProducts", mv, form.request, form.query)

        SearchResult rs = new SearchResult(mv.viewName, false)
        rs.entries.putAll(mv.model)
        return rs
    }

}
