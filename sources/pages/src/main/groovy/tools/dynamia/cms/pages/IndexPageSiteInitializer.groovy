/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.pages

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.core.SiteInitializer
import tools.dynamia.cms.core.api.CMSExtension
import tools.dynamia.cms.core.domain.ContentAuthor
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.pages.domain.Page
import tools.dynamia.domain.services.CrudService

/**
 *
 * @author Mario Serrano Leones
 */
@CMSExtension
class IndexPageSiteInitializer implements SiteInitializer {

    @Autowired
    private CrudService crudService

    @Override
    void init(Site site) {
    }

    @Override
    void postInit(Site site) {
        Page index = new Page()
        index.alias = "index"
        index.title = site.name
        index.site = site
        ContentAuthor author = getAuthor(site)
        index.author = author

        crudService.create(index)

    }

    private ContentAuthor getAuthor(Site site) {
        ContentAuthor author = crudService.findSingle(ContentAuthor.class, "site", site)
        if (author == null) {
            author = new ContentAuthor()
            author.firstName = "Default"
            author.lastName = "Author"
            author.email = "admin@" + site.key + ".login"
            crudService.save(author)
        }
        return author
    }

}
