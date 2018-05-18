package tools.dynamia.cms.core.sitemap

import tools.dynamia.cms.core.domain.Site

interface SiteMapProvider {

	List<SiteMapURL> get(Site site)
}
