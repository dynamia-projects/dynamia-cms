package tools.dynamia.cms.products.ext

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.api.CMSExtension
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.sitemap.SiteMapProvider
import tools.dynamia.cms.core.sitemap.SiteMapURL

@CMSExtension
class ProductBrandSiteMapProvider implements SiteMapProvider {

    @Autowired
    private tools.dynamia.cms.products.services.ProductsService service

    @Override
    List<SiteMapURL> get(Site site) {
        List<SiteMapURL> urls = new ArrayList<>()

        SiteMapURL url = new SiteMapURL(CMSUtil.getSiteURL(site, "store/brands"))
        url.name = "Brands"

        urls.add(url)
        service.getBrands(site).stream().map { c -> createURL(site, c) }.forEach { urls << it }

        return urls

    }

    private SiteMapURL createURL(Site site, tools.dynamia.cms.products.domain.ProductBrand b) {
        SiteMapURL url = new SiteMapURL(CMSUtil.getSiteURL(site, String.format("store/brands/%s", b.alias)))
        url.name = b.name
        url.description = b.description

        return url
    }

}
