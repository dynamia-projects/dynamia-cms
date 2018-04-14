/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.site.templates

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.site.core.SiteInitializer
import tools.dynamia.cms.site.core.api.CMSExtension
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.templates.services.TemplateService

/**
 *
 * @author mario
 */
@CMSExtension
public class DefaultTemplateSiteInitializer implements SiteInitializer {

    @Autowired
    private TemplateService service;

    @Override
    public void init(Site site) {
        if (site.getTemplate() == null) {
            site.setTemplate(service.getDefaultTemplate().getName());
        }
    }

    @Override
    public void postInit(Site site) {

    }

}
