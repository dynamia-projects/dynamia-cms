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

package tools.dynamia.cms.products.admin.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.products.domain.ProductsSiteConfig
import tools.dynamia.cms.products.services.ProductsSyncService
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.ui.UIMessages

@InstallAction
class SyncProductCategoriesAction extends AbstractCrudAction {

    @Autowired
    private ProductsSyncService service

    SyncProductCategoriesAction() {
        name = "Sync Cats"
        applicableClass = ProductsSiteConfig
    }

    @Override
    void actionPerformed(CrudActionEvent evt) {
        def config = evt.data as ProductsSiteConfig
        if (config) {
            def categories = service.synchronizeCategories(config)
            service.syncCategoriesDetails(config, categories)
            UIMessages.showMessage("Categories synced successfully")
        }
    }
}
