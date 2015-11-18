package com.dynamia.cms.admin.core.zk.controllers;

import java.nio.file.Path;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.core.CMSUtil;
import com.dynamia.cms.site.core.DynamiaCMS;
import com.dynamia.cms.site.core.SiteContext;
import com.dynamia.cms.site.core.domain.Site;

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
