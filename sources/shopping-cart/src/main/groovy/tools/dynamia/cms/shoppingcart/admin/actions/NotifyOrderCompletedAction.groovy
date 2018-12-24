/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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

package tools.dynamia.cms.shoppingcart.admin.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.shoppingcart.domain.ShoppingOrder
import tools.dynamia.cms.shoppingcart.services.ShoppingCartService
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.domain.services.CrudService
import tools.dynamia.ui.UIMessages

@InstallAction
class NotifyOrderCompletedAction extends AbstractCrudAction {

    @Autowired
    private ShoppingCartService service

    @Autowired
    private CrudService crudService

    NotifyOrderCompletedAction() {
        name = "Notify Order"
        menuSupported = true

    }

    @Override
    CrudState[] getApplicableStates() {
        return CrudState.get(CrudState.READ)
    }

    @Override
    ApplicableClass[] getApplicableClasses() {
        return ApplicableClass.get(ShoppingOrder.class)
    }

    @Override
    void actionPerformed(CrudActionEvent evt) {
        final ShoppingOrder shoppingOrder = (ShoppingOrder) evt.data
        if (shoppingOrder != null) {

            UIMessages.showQuestion("Are you sure?",  {
                service.notifyOrderCompleted(crudService.reload(shoppingOrder))
                UIMessages.showMessage("Notification sended ")

            })
        }
    }

}
