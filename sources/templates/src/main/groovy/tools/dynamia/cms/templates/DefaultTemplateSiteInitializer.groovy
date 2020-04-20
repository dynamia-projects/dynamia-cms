/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

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
 * @author Mario Serrano Leones
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
