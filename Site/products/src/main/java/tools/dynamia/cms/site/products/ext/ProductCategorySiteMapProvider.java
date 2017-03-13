package tools.dynamia.cms.site.products.ext;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import tools.dynamia.cms.site.core.CMSUtil;
import tools.dynamia.cms.site.core.api.CMSExtension;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.core.sitemap.SiteMapFrecuency;
import tools.dynamia.cms.site.core.sitemap.SiteMapProvider;
import tools.dynamia.cms.site.core.sitemap.SiteMapURL;
import tools.dynamia.cms.site.products.domain.ProductCategory;
import tools.dynamia.cms.site.products.services.ProductsService;

@CMSExtension
public class ProductCategorySiteMapProvider implements SiteMapProvider {

	@Autowired
	private ProductsService service;

	@Override
	public List<SiteMapURL> get(Site site) {

		return service.getCategories(site).stream().filter(c -> c.isActive()).map(c -> createURL(site, c))
				.collect(Collectors.toList());

	}

	private SiteMapURL createURL(Site site, ProductCategory c) {
		SiteMapURL url = new SiteMapURL(
				CMSUtil.getSiteURL(site, String.format("store/categories/%s/%s", c.getId(), c.getAlias())));
		url.setChangeFrequency(SiteMapFrecuency.daily);
		url.setPriority(SiteMapURL.HIGH);
		url.setName(c.getName());
		url.setDescription(c.getDescription());
		if (c.getParent() != null) {
			url.setCategory(c.getParent().getName());
		}

		return url;
	}

}
