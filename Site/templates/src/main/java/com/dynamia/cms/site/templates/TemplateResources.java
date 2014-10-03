/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.templates;

import com.dynamia.cms.site.core.CMSUtil;
import com.dynamia.cms.site.core.DynamiaCMS;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.cms.site.templates.services.TemplateService;
import com.dynamia.tools.integration.Containers;

import java.io.File;
import java.nio.file.Path;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author mario
 */
public class TemplateResources {

	public static Path find(Site site, String resourceName) {
		SiteService service = Containers.get().findObject(SiteService.class);
		TemplateService tpltService = Containers.get().findObject(TemplateService.class);

		if (site == null) {
			site = service.getMainSite();
		}

		String currentTemplate = getTemplateName(site);

		if (currentTemplate == null || currentTemplate.isEmpty()) {
			currentTemplate = tpltService.getDefaultTemplate().getDirectoryName();
		}

		Path templateResource = DynamiaCMS.getTemplatesLocation().resolve(currentTemplate + File.separator + resourceName);

		return templateResource;
	}

	private static String getTemplateName(Site site) {
		String name = site.getTemplate();
		
		HttpServletRequest request = CMSUtil.getCurrentRequest();
		if (request != null) {
			String sessionTemplate = request.getParameter("setSessionTemplate");
			if (sessionTemplate != null && !sessionTemplate.isEmpty()) {
				request.getSession().setAttribute("SessionTemplate", sessionTemplate);
			}
			sessionTemplate = (String) request.getSession().getAttribute("SessionTemplate");
			if (sessionTemplate != null) {
				name = sessionTemplate;
			}
		}

		return name;
	}

}
