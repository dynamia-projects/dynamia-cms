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

package tools.dynamia.cms.core.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.sitemap.SiteMap
import tools.dynamia.cms.core.sitemap.SiteMapProvider
import tools.dynamia.integration.Containers

@Controller
class SiteMapController {

    @RequestMapping("/sitemap.xml")
    @ResponseBody
    SiteMap getSiteMapXML() {
        Site site = SiteContext.get().current

        SiteMap map = new SiteMap()
        Containers.get().findObjects(SiteMapProvider.class).forEach { pv ->
            try {
                pv.get(site).forEach { url -> map.addUrl(url) }
            } catch (Exception e) {
                e.printStackTrace()
            }
        }

        return map

    }
}
