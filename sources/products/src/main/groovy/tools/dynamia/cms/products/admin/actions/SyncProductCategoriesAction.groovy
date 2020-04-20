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
