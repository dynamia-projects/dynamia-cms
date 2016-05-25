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
package tools.dynamia.cms.admin.core.zk.controllers;

import java.nio.file.Path;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import tools.dynamia.cms.site.core.CMSUtil;
import tools.dynamia.cms.site.core.DynamiaCMS;
import tools.dynamia.cms.site.core.SiteContext;
import tools.dynamia.cms.site.core.domain.Site;

@Controller
public class FileBrowserController {

	@RequestMapping("/browse")
	public ModelAndView login(HttpServletRequest request) {

		ModelAndView mv = new ModelAndView("browse");

		try {
			Site site = SiteContext.get().getCurrent();
			String url = CMSUtil.getSiteURL(site, "resources");
			Path sitePath = DynamiaCMS.getSitesResourceLocation(site);

			mv.addObject("rootDirectory", sitePath.toFile());
			mv.addObject("baseUrl", url);
			mv.addObject("CKEditorFuncNum", request.getParameter("CKEditorFuncNum"));
			mv.addObject("CKEditor", request.getParameter("CKEditor"));
			mv.addObject("Type", request.getParameter("Type"));
			mv.addObject("url", request.getParameter("url"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mv;

	}

}
