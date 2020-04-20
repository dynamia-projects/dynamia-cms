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
import tools.dynamia.actions.ActionGroup
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.products.domain.ProductsSiteConfig
import tools.dynamia.cms.products.services.ProductsService
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.domain.services.CrudService
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages

/**
 *
 * @author Mario Serrano Leones
 */
@InstallAction
class GenerateTokenAction extends AbstractCrudAction {

    @Autowired
    private ProductsService service

    @Autowired
    private CrudService crudService

    GenerateTokenAction() {
        name = "Generate Token"
        image = "token"
        group = ActionGroup.get("products")
        menuSupported = true
    }

    @Override
    void actionPerformed(final CrudActionEvent evt) {
        final ProductsSiteConfig cfg = (ProductsSiteConfig) evt.data
        if (cfg != null) {
            UIMessages.showQuestion("¿Are you sure want generate a new toke for this site's configuration?", {
                service.generateToken(cfg)
                crudService.save(cfg)
                evt.controller.doQuery()
                UIMessages.showMessage("Token generated succesfully!!")
            })

        } else {
            UIMessages.showMessage("Select product site configuration to generate new token.", MessageType.WARNING)
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
