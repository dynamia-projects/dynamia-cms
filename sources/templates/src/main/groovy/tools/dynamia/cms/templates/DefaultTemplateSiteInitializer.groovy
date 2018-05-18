/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.templates

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.core.SiteInitializer
import tools.dynamia.cms.core.api.CMSExtension
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.templates.services.TemplateService

/**
 *
 * @author mario
 */
@CMSExtension
class DefaultTemplateSiteInitializer implements SiteInitializer {

    @Autowired
    private TemplateService service

    @Override
    void init(Site site) {
        if (site.template == null) {
            site.template = service.defaultTemplate.name
        }
    }

    @Override
    void postInit(Site site) {

    }

}
