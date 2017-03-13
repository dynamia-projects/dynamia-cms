package tools.dynamia.cms.site.products.ext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import tools.dynamia.cms.site.core.CMSUtil;
import tools.dynamia.cms.site.core.api.CMSExtension;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.core.sitemap.SiteMapFrecuency;
import tools.dynamia.cms.site.core.sitemap.SiteMapProvider;
import tools.dynamia.cms.site.core.sitemap.SiteMapURL;
import tools.dynamia.cms.site.products.domain.ProductBrand;
import tools.dynamia.cms.site.products.domain.ProductCategory;
import tools.dynamia.cms.site.products.services.ProductsService;

@CMSExtension
public class ProductBrandSiteMapProvider implements SiteMapProvider {

	@Autowired
	private ProductsService service;

	@Override
	public List<SiteMapURL> get(Site site) {
		List<SiteMapURL> urls = new ArrayList<>();

		SiteMapURL url = new SiteMapURL(CMSUtil.getSiteURL(site, "store/brands"));
		url.setName("Brands");

		urls.add(url);
		service.getBrands(site).stream().map(c -> createURL(site, c)).forEach(urls::add);

		return urls;

	}

	private SiteMapURL createURL(Site site, ProductBrand b) {
		SiteMapURL url = new SiteMapURL(CMSUtil.getSiteURL(site, String.format("store/brands/%s", b.getAlias())));				
		url.setName(b.getName());
		url.setDescription(b.getDescription());

		return url;
	}

}
