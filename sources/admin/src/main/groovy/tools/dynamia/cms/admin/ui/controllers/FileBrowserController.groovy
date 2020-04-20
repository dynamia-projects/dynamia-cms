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
package tools.dynamia.cms.admin.ui.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.DynamiaCMS
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.core.domain.Site

import javax.servlet.http.HttpServletRequest
import java.nio.file.Path

@Controller
class FileBrowserController {

	@RequestMapping("/cms-admin/browse")
    ModelAndView login(HttpServletRequest request) {

		ModelAndView mv = new ModelAndView("browse")

        try {
			Site site = SiteContext.get().current
            String url = CMSUtil.getSiteURL(site, "resources")
            Path sitePath = DynamiaCMS.getSitesResourceLocation(site)

            mv.addObject("rootDirectory", sitePath.toFile())
            mv.addObject("baseUrl", url)
            mv.addObject("CKEditorFuncNum", request.getParameter("CKEditorFuncNum"))
            mv.addObject("CKEditor", request.getParameter("CKEditor"))
            mv.addObject("Type", request.getParameter("Type"))
            mv.addObject("url", request.getParameter("url"))

        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace()
        }

		return mv

    }

}
