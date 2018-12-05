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

package tools.dynamia.cms.products.modules

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.core.api.AbstractModule
import tools.dynamia.cms.core.api.CMSModule
import tools.dynamia.cms.core.api.ModuleContext
import tools.dynamia.cms.products.domain.Store
import tools.dynamia.domain.services.CrudService

@CMSModule
class StoresModule extends AbstractModule {

    @Autowired
    private tools.dynamia.cms.products.services.ProductsService service

    @Autowired
    private CrudService crudService

    StoresModule() {
        super("stores", "Stores List", "products/modules/stores")
        description = "Show a stores list"
        putMetadata("author", "Mario Serrano Leones")
        putMetadata("version", "1.0")
        putMetadata("created at", "10-04-2017")
        variablesNames = "stores"

    }

    @Override
    void init(ModuleContext context) {
        List<Store> stores = service.getStores(context.site)
        try {
            stores.forEach { s -> s.contacts.size() }
        } catch (Exception e) {
        }


        context.moduleInstance.addObject("stores", stores)

    }

}
