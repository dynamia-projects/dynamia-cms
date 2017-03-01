package tools.dynamia.cms.site.core.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import tools.dynamia.cms.site.core.SiteContext;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.core.sitemap.SiteMap;
import tools.dynamia.cms.site.core.sitemap.SiteMapProvider;
import tools.dynamia.integration.Containers;

@Controller
public class SiteMapController {

	@RequestMapping("/sitemap.xml")
	@ResponseBody
	public SiteMap getSiteMapXML() {
		Site site = SiteContext.get().getCurrent();

		SiteMap map = new SiteMap();
		Containers.get().findObjects(SiteMapProvider.class).forEach(pv -> {
			try {
				pv.get(site).forEach(url -> map.addUrl(url));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		return map;

	}
}
