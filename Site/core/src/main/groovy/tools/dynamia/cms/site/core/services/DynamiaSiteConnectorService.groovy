package tools.dynamia.cms.site.core.services

import tools.dynamia.cms.site.core.domain.Site

interface DynamiaSiteConnectorService {
    void sync(Site site)
}
