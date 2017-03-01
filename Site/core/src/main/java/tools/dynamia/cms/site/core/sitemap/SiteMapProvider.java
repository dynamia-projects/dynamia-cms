package tools.dynamia.cms.site.core.sitemap;

import java.util.List;

import tools.dynamia.cms.site.core.domain.Site;

public interface SiteMapProvider {

	public List<SiteMapURL> get(Site site);
}
