package tools.dynamia.cms.products.ext

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.api.CMSExtension
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.sitemap.SiteMapFrecuency
import tools.dynamia.cms.core.sitemap.SiteMapProvider
import tools.dynamia.cms.core.sitemap.SiteMapURL

@CMSExtension
class ProductCategorySiteMapProvider implements SiteMapProvider {

    @Autowired
    private tools.dynamia.cms.products.services.ProductsService service

    @Override
    List<SiteMapURL> get(Site site) {
        return service.getCategories(site).findResults { it.active ? createURL(site, it) : null }
    }

    private SiteMapURL createURL(Site site, tools.dynamia.cms.products.domain.ProductCategory c) {
        SiteMapURL url = new SiteMapURL(
                CMSUtil.getSiteURL(site, String.format("store/categories/%s/%s", c.id, c.alias)))
        url.changeFrequency = SiteMapFrecuency.daily
        url.priority = SiteMapURL.HIGH
        url.name = c.name
        url.description = c.description
        if (c.parent != null) {
            url.category = c.parent.name
        }
        return url
    }

}
