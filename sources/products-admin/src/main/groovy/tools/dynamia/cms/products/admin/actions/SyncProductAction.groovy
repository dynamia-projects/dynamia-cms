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
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.products.api.ProductsDatasource
import tools.dynamia.cms.products.domain.Product
import tools.dynamia.cms.products.domain.ProductsSiteConfig
import tools.dynamia.cms.products.dto.ProductDTO
import tools.dynamia.cms.products.services.ProductsService
import tools.dynamia.cms.products.services.ProductsSyncService
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages

@InstallAction
class SyncProductAction extends AbstractCrudAction {

    @Autowired
    private ProductsService service

    @Autowired
    private ProductsSyncService syncService

    SyncProductAction() {
        name = "Sync"
        image = "refresh"
    }

    @Override
    void actionPerformed(CrudActionEvent evt) {

        Product product = (Product) evt.data
        if (product != null) {

            if (product.externalRef == null) {
                UIMessages.showMessage("Cannot sync product, external reference not found", MessageType.ERROR)
                return
            }

            ProductsSiteConfig cfg = service.getSiteConfig(SiteContext.get().current)
            if (cfg != null) {
                ProductsDatasource datasource = syncService.getDatasource(cfg)
                if (datasource != null) {
                    ProductDTO remoteProduct = datasource.getProduct(product.externalRef, cfg.parametersAsMap)
                    if (remoteProduct != null) {
                        String stage = ""
                        try {
                            stage = "general info"
                            syncService.synchronizeProduct(cfg, remoteProduct)
                            stage = "details"
                            syncService.syncProductDetails(cfg, remoteProduct)
                            stage = "stock Details"
                            syncService.syncProductStockDetails(cfg, remoteProduct)
                            stage = "image download"
                            syncService.downloadProductImages(cfg, remoteProduct)
                            UIMessages.showMessage(product + " sync successfully")

                        } catch (Exception e) {
                            UIMessages.showMessage("Error syncing product: " + stage + ": " + e.message, MessageType.ERROR)
                            e.printStackTrace()
                        }
                    } else {
                        UIMessages.showMessage("No remove product found", MessageType.WARNING)
                    }
                } else {
                    UIMessages.showMessage("Products Datasource not configured", MessageType.WARNING)
                }
            } else {
                UIMessages.showMessage("Site products configuration not found", MessageType.WARNING)
            }

        } else {
            UIMessages.showMessage("Select product first", MessageType.WARNING)
        }
    }

    @Override
    ApplicableClass[] getApplicableClasses() {
        return ApplicableClass.get(Product.class)
    }

    @Override
    CrudState[] getApplicableStates() {
        return CrudState.get(CrudState.READ)
    }
}
