package tools.dynamia.cms.site.pages.ext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import tools.dynamia.cms.site.core.CMSUtil;
import tools.dynamia.cms.site.core.api.CMSExtension;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.core.sitemap.SiteMapProvider;
import tools.dynamia.cms.site.core.sitemap.SiteMapURL;
import tools.dynamia.cms.site.pages.domain.Page;
import tools.dynamia.cms.site.pages.services.PageService;

@CMSExtension
public class PageSiteMapProvider implements SiteMapProvider {

	@Autowired
	private PageService service;

	@Override
	public List<SiteMapURL> get(Site site) {

		List<SiteMapURL> urls = new ArrayList<>();

		urls.addAll(service.getPages(site).stream().filter(p -> p.isPublished()).map(p -> createURL(site, p))
				.collect(Collectors.toList()));

		service.getPagesCategories(site).forEach(p -> {
			if (p.getAlias() != null && !p.getAlias().isEmpty()) {
				SiteMapURL url = new SiteMapURL(CMSUtil.getSiteURL(site, "category/" + p.getAlias()));
				url.setName(p.getName());
				url.setDescription(p.getDescription());
				urls.add(url);
			}
		});

		return urls;
	}

	private SiteMapURL createURL(Site site, Page p) {
		SiteMapURL url = new SiteMapURL(CMSUtil.getSiteURL(site, p.getAlias()), p.getLastUpdate());
		url.setName(p.getTitle());
		url.setImageURL(p.getImageURL());
		url.setDescription(p.getSummary());
		if (p.getCategory() != null) {
			url.setCategory(p.getCategory().getName());
		}
		return url;
	}

}
