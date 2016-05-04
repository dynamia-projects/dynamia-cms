/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.products.actions;

import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.services.ProductTemplateService;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.commons.ApplicableClass;
import tools.dynamia.crud.AbstractCrudAction;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.zk.util.ZKUtil;

/**
 *
 * @author mario
 */
@InstallAction
public class ViewTemplateVarsAction extends AbstractCrudAction {

    @Autowired
    private ProductTemplateService service;

    @Autowired
    private CrudService crudService;

    public ViewTemplateVarsAction() {
        setName("View Vars");
        setMenuSupported(true);
        setImage("table");
    }

    @Override
    public void actionPerformed(CrudActionEvent evt) {
        Product product = (Product) evt.getData();
        if (product != null) {
            product = crudService.reload(product);
            Map<String, Object> vars = new TreeMap<>();
            service.loadDefaultTemplateModel(product, vars);

            Listbox table = new Listbox();
            table.setVflex("1");
            table.setHflex("1");
            table.setSizedByContent(true);
            table.appendChild(new Listhead());
            table.getListhead().appendChild(new Listheader("Name"));
            table.getListhead().appendChild(new Listheader("Value"));
            vars.forEach((n, v) -> {
                Listitem item = new Listitem();
                item.appendChild(new Listcell(n));
                item.appendChild(new Listcell(String.valueOf(v)));
                table.appendChild(item);
            });
            ZKUtil.showDialog(product.getName(), table, "600px", "500px");
        } else {
            UIMessages.showMessage("Select product", MessageType.WARNING);
        }
    }

    @Override
    public CrudState[] getApplicableStates() {
        return CrudState.get(CrudState.READ);
    }

    @Override
    public ApplicableClass[] getApplicableClasses() {
        return ApplicableClass.get(Product.class);
    }

}
