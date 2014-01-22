/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.products.actions;

import com.dynamia.cms.site.products.domain.ProductsSiteConfig;
import com.dynamia.cms.site.products.services.ProductsService;
import com.dynamia.tools.commons.ApplicableClass;
import com.dynamia.tools.domain.services.CrudService;
import com.dynamia.tools.web.actions.ActionGroup;
import com.dynamia.tools.web.actions.InstallAction;
import com.dynamia.tools.web.crud.AbstractCrudAction;
import com.dynamia.tools.web.crud.CrudActionEvent;
import com.dynamia.tools.web.crud.CrudState;
import com.dynamia.tools.web.ui.MessageType;
import com.dynamia.tools.web.ui.UIMessages;
import com.dynamia.tools.web.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author mario
 */
@InstallAction
public class GenerateTokenAction extends AbstractCrudAction {

    @Autowired
    private ProductsService service;

    @Autowired
    private CrudService crudService;

    public GenerateTokenAction() {
        setName("Generate Token");
        setGroup(ActionGroup.get("products"));
    }

    @Override
    public void actionPerformed(final CrudActionEvent evt) {
        final ProductsSiteConfig cfg = (ProductsSiteConfig) evt.getData();
        if (cfg != null) {
            UIMessages.showQuestion("¿Are you sure want generate a new toke for this site's configuration?", new Callback() {

                @Override
                public void doSomething() {
                    service.generateToken(cfg);
                    crudService.save(cfg);
                    evt.getController().doQuery();
                    UIMessages.showMessage("Token generated succesfully!!");
                }
            });

        } else {
            UIMessages.showMessage("Select product site configuration to generate new token.", MessageType.WARNING);
        }

    }

    @Override
    public CrudState[] getApplicableStates() {
        return CrudState.get(CrudState.READ);
    }

    @Override
    public ApplicableClass[] getApplicableClasses() {
        return ApplicableClass.get(ProductsSiteConfig.class);
    }

}
