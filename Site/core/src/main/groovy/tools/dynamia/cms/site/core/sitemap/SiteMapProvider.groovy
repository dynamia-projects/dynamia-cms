package tools.dynamia.cms.site.core.sitemap

import tools.dynamia.cms.site.core.domain.Site

public interface SiteMapProvider {

	public List<SiteMapURL> get(Site site);
}
