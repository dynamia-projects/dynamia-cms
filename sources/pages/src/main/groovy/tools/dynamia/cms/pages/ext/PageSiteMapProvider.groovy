package tools.dynamia.cms.pages.ext

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.api.CMSExtension
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.sitemap.SiteMapProvider
import tools.dynamia.cms.core.sitemap.SiteMapURL
import tools.dynamia.cms.pages.domain.Page

@CMSExtension
class PageSiteMapProvider implements SiteMapProvider {

    @Autowired
    private tools.dynamia.cms.pages.services.PageService service

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
