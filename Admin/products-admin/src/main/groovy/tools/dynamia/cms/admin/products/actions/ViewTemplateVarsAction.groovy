/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.admin.products.actions

import org.springframework.beans.factory.annotation.Autowired
import org.zkoss.zul.*
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.site.products.domain.Product
import tools.dynamia.cms.site.products.services.ProductTemplateService
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
 * @author mario
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
