/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.site.pages

import tools.dynamia.cms.site.core.api.AdminModule
import tools.dynamia.cms.site.core.api.AdminModuleOption
import tools.dynamia.cms.site.core.api.CMSModule
import tools.dynamia.cms.site.core.domain.ContentAuthor
import tools.dynamia.cms.site.pages.domain.Page
import tools.dynamia.cms.site.pages.domain.PageCategory

/**
 *
 * @author Mario Serrano Leones
 */
@CMSModule
class ContentAdminModule implements AdminModule {

    @Override
    String getGroup() {
        return "Content"
    }

    @Override
    String getName() {
        return "Content"
    }

    @Override
    String getImage() {
        return null
    }

    @Override
    AdminModuleOption[] getOptions() {
        return [
                new AdminModuleOption("authors", "Authors", ContentAuthor.class),
                new AdminModuleOption("pages", "Pages", Page.class, true, true, "edit", true),
                new AdminModuleOption("categories", "Pages Categories", PageCategory.class)

        ]
    }

}