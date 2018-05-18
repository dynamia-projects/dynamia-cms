package tools.dynamia.cms.core.services

import tools.dynamia.cms.core.domain.Site

interface DynamiaSiteConnectorService {
    void sync(Site site)
}
