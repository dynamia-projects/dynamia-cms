/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.products.actions;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.cms.site.products.domain.ProductsSiteConfig;
import com.dynamia.cms.site.products.services.ProductsService;

import tools.dynamia.actions.ActionGroup;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.commons.ApplicableClass;
import tools.dynamia.crud.AbstractCrudAction;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;

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
        setImage("token");
        setGroup(ActionGroup.get("products"));
        setMenuSupported(true);
    }

    @Override
    public void actionPerformed(final CrudActionEvent evt) {
        final ProductsSiteConfig cfg = (ProductsSiteConfig) evt.getData();
        if (cfg != null) {
            UIMessages.showQuestion("¿Are you sure want generate a new toke for this site's configuration?", () -> {
			    service.generateToken(cfg);
			    crudService.save(cfg);
			    evt.getController().doQuery();
			    UIMessages.showMessage("Token generated succesfully!!");
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
