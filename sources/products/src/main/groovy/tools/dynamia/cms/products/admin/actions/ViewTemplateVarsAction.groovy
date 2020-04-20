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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.products.admin.actions

import org.springframework.beans.factory.annotation.Autowired
import org.zkoss.zul.*
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.products.domain.Product
import tools.dynamia.cms.products.services.ProductTemplateService
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.domain.services.CrudService
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages
import tools.dynamia.zk.util.ZKUtil

/**
 *
 * @author Mario Serrano Leones
 */
@InstallAction
class ViewTemplateVarsAction extends AbstractCrudAction {

    @Autowired
    private ProductTemplateService service

    @Autowired
    private CrudService crudService

    ViewTemplateVarsAction() {
        name = "View Vars"
        menuSupported = true
        image = "table"
    }

    @Override
    void actionPerformed(CrudActionEvent evt) {
        Product product = (Product) evt.data
        if (product != null) {
            product = crudService.reload(product)
            Map<String, Object> vars = new TreeMap<>()
            service.loadDefaultTemplateModel(product, vars)

            Listbox table = new Listbox()
            table.vflex = "1"
            table.hflex = "1"
            table.sizedByContent = true
            table.appendChild(new Listhead())
            table.listhead.appendChild(new Listheader("Name"))
            table.listhead.appendChild(new Listheader("Value"))
            vars.forEach{n, v ->
                Listitem item = new Listitem()
                item.appendChild(new Listcell(n))
                item.appendChild(new Listcell(String.valueOf(v)))
                table.appendChild(item)
            }
            ZKUtil.showDialog(product.name, table, "600px", "500px")
        } else {
            UIMessages.showMessage("Select product", MessageType.WARNING)
        }
    }

    @Override
    CrudState[] getApplicableStates() {
        return CrudState.get(CrudState.READ)
    }

    @Override
    ApplicableClass[] getApplicableClasses() {
        return ApplicableClass.get(Product.class)
    }

}
