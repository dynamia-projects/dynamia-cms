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
package tools.dynamia.cms.products.admin.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.ActionGroup
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.products.domain.ProductsSiteConfig
import tools.dynamia.cms.products.services.ProductsSynchronizer
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages

/**
 *
 * @author Mario Serrano Leones
 */
@InstallAction
class SyncProductsAction extends AbstractCrudAction {

    @Autowired
    private ProductsSynchronizer synchronizer

    SyncProductsAction() {
        name = "Sync All Products"
        group = ActionGroup.get("products")
        image = "sync"
        menuSupported = true
    }

    @Override
    void actionPerformed(CrudActionEvent evt) {
        final ProductsSiteConfig cfg = (ProductsSiteConfig) evt.data
        if (cfg != null) {
            UIMessages.showQuestion("This action make take several minutes", {
                long s = System.currentTimeMillis()
                synchronizer.synchronize(cfg)

                long e = System.currentTimeMillis()
                long t = e - s

                UIMessages.showMessage("Synchronization completed in " + t + "ms sucessfully!")
            })

        } else {
            UIMessages.showMessage("Select product site configuration to sync.", MessageType.WARNING)
        }

    }

    @Override
    CrudState[] getApplicableStates() {
        return CrudState.get(CrudState.READ)
    }

    @Override
    ApplicableClass[] getApplicableClasses() {
        return ApplicableClass.get(ProductsSiteConfig.class)
    }

}
