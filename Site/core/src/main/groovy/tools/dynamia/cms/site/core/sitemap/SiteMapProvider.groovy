package tools.dynamia.cms.site.core.sitemap

import tools.dynamia.cms.site.core.domain.Site

interface SiteMapProvider {

	List<SiteMapURL> get(Site site)
}
