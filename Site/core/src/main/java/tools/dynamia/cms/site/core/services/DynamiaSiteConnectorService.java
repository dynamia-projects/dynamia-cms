package tools.dynamia.cms.site.core.services;

import tools.dynamia.cms.site.core.domain.Site;

public interface DynamiaSiteConnectorService {
    void sync(Site site);
}
