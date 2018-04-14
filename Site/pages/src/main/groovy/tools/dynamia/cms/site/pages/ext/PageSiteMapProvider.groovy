package tools.dynamia.cms.site.pages.ext

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.site.core.CMSUtil
import tools.dynamia.cms.site.core.api.CMSExtension
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.core.sitemap.SiteMapProvider
import tools.dynamia.cms.site.core.sitemap.SiteMapURL
import tools.dynamia.cms.site.pages.domain.Page
import tools.dynamia.cms.site.pages.services.PageService

@CMSExtension
class PageSiteMapProvider implements SiteMapProvider {

    @Autowired
    private PageService service

    @Override
    List<SiteMapURL> get(Site site) {

        List<SiteMapURL> urls = new ArrayList<>()

        service.getPages(site).stream().filter { p -> p.published }.map { p -> createURL(site, p) }.forEach {
            urls << it
        }

        service.getPagesCategories(site).forEach { p ->
            if (p.alias != null && !p.alias.empty) {
                SiteMapURL url = new SiteMapURL(CMSUtil.getSiteURL(site, "category/" + p.alias))
                url.name = p.name
                url.description = p.description
                urls.add(url)
            }
        }

        return urls
    }

    private SiteMapURL createURL(Site site, Page p) {
        SiteMapURL url = new SiteMapURL(CMSUtil.getSiteURL(site, p.alias), p.lastUpdate)
        url.name = p.title
        url.imageURL = p.imageURL
        url.description = p.summary
        if (p.category != null) {
            url.category = p.category.name
        }
        return url
    }

}
