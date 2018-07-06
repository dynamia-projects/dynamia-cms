package tools.dynamia.cms.users.ext

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.core.SiteInitializer
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.users.services.UserService
import tools.dynamia.integration.sterotypes.Provider

@Provider
class AdminUserSiteInitializer implements SiteInitializer {

    private UserService service

    @Autowired
    AdminUserSiteInitializer(UserService service) {
        this.service = service
    }

    @Override
    void init(Site site) {

    }

    @Override
    void postInit(Site site) {
        service.checkAdminUser(site)
    }
}
