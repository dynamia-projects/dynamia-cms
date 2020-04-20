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
